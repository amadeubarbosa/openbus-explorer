package admin;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import com.google.common.collect.ArrayListMultimap;
import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import scs.core.ComponentContext;
import tecgraf.openbus.Connection;
import tecgraf.openbus.LocalOffer;
import tecgraf.openbus.OpenBusContext;
import tecgraf.openbus.core.ORBInitializer;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceOfferDesc;

//TODO teste inútil; remover ou modificar.
public class BusAdminTest {

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

    BusAdminImpl admin = new BusAdminImpl();
    admin.getAdminFacets(host, port, orb);

    int index;
    for (index = 0; index < 5; index++) {
      ComponentContext component = Utils.buildComponent(orb);
      ArrayListMultimap<String, String> props = ArrayListMultimap.create();
      props.put("offer.domain", "BusAdminLib Test");
      props.put("loop.index", Integer.toString(index));
      LocalOffer local = conn.offerRegistry().registerService(component
        .getIComponent(), props);
      Assert.assertNotNull(local.remoteOffer(10000));
    }
    List<ServiceOfferDesc> found = admin.getOffers();
    Assert.assertTrue(found.size() >= index);

    conn.logout();
    orb.shutdown(true);
    orb.destroy();
  }
}
