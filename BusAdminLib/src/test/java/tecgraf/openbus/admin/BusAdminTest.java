package tecgraf.openbus.admin;

import com.google.common.collect.ArrayListMultimap;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import scs.core.ComponentContext;
import tecgraf.openbus.Connection;
import tecgraf.openbus.LocalOffer;
import tecgraf.openbus.OpenBusContext;
import tecgraf.openbus.RemoteOffer;
import tecgraf.openbus.core.ORBInitializer;
import tecgraf.openbus.core.v2_1.BusObjectKey;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

public class BusAdminTest {

  private static final int MAX_OFFERS_TO_REGISTER = 5;
  private static final String TEST_SPECIFIC_PROPERTY = "BusExplorer Test";
  private static String host;
  private static short port;
  private static String entity;
  private static byte[] password;
  private static String domain;

  @BeforeClass
  public static void oneTimeSetUp() throws Exception {
    Properties properties = Utils.readPropertyFile("/test.properties");
    host = properties.getProperty("openbus.host.name");
    port = Short.valueOf(properties.getProperty("openbus.host.port"));
    entity = properties.getProperty("entity.name");
    password = properties.getProperty("entity.password").getBytes();
    domain = properties.getProperty("entity.domain");

    Utils.setLogLevel(Level.FINE);
  }

  @Test
  public void listOfferTest() throws Exception {
    ORB orb = ORBInitializer.initORB();
    OpenBusContext context =
      (OpenBusContext) orb.resolve_initial_references("OpenBusContext");
    Connection conn = context.connectByAddress(host, port);
    conn.loginByPassword(entity, password, domain);
    context.defaultConnection(conn);
    POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
    poa.the_POAManager().activate();

    Object reference = orb.string_to_object(
      String.format("corbaloc::1.0@%s:%d/%s", host, port, BusObjectKey.value));
    BusAdminImpl admin = new BusAdminImpl(reference);

    ArrayList<LocalOffer> locals = new ArrayList<>();
    for (int i = 0; i < MAX_OFFERS_TO_REGISTER; i++) {
      ComponentContext component = Utils.buildComponent(orb);
      ArrayListMultimap<String,String> props = ArrayListMultimap.create();
      props.put("offer.domain", TEST_SPECIFIC_PROPERTY);
      props.put("loop.index", Integer.toString(i));
      // tarefas assíncronas
      locals.add(conn.offerRegistry()
              .registerService(component.getIComponent(), props));
    }
    // sincronização
    ConcurrentLinkedQueue<RemoteOffer> expected = new ConcurrentLinkedQueue<RemoteOffer>();
    locals.parallelStream().forEach(localOffer -> {
      try {
        expected.add(localOffer.remoteOffer());
      } catch (Exception e) {
        Assert.fail(e.getMessage());
      }
    });
    // fim da barreira de sincronização
    Assert.assertEquals(MAX_OFFERS_TO_REGISTER, expected.size());
    // tarefa síncrona porque usa as interfaces de administração diretamente
    List<ServiceOfferDesc> allOffers = admin.getOffers();

    expected.removeIf(serviceOfferDesc -> {
      for (int i=0; i< allOffers.size(); i++) {
        if (allOffers.get(i).ref.owner().id.equals(serviceOfferDesc.owner().id)) {
          return true;
        }
      }
      return false;
    });
    Assert.assertEquals(0, expected.size());
    Assert.assertTrue(conn.logout());
    orb.shutdown(true);
    orb.destroy();
  }

}
