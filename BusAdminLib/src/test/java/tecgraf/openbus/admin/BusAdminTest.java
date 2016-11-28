package tecgraf.openbus.admin;

import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import scs.core.ComponentContext;
import tecgraf.openbus.Connection;
import tecgraf.openbus.OpenBusContext;
import tecgraf.openbus.core.ORBInitializer;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceProperty;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class BusAdminTest {

  private static String host;
  private static short port;
  private static String entity;
  private static byte[] password;

  @BeforeClass
  public static void oneTimeSetUp() throws Exception {
    Properties properties = Utils.readPropertyFile("/test.properties");
    host = properties.getProperty("openbus.host.name");
    port = Short.valueOf(properties.getProperty("openbus.host.port"));
    entity = properties.getProperty("entity.name");
    password = properties.getProperty("entity.password").getBytes();

    Utils.setLogLevel(Level.FINE);
  }

  @Test
  public void listOfferTest() throws Exception {
    ORB orb = ORBInitializer.initORB();
    OpenBusContext context =
      (OpenBusContext) orb.resolve_initial_references("OpenBusContext");
    Connection conn = context.createConnection(host, port);
    conn.loginByPassword(entity, password);
    context.setDefaultConnection(conn);
    POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
    poa.the_POAManager().activate();
    
    BusAdminImpl admin = new BusAdminImpl();
    admin.connect(host, port, orb);

    int index;
    for (index = 0; index < 5; index++) {
      ComponentContext component = Utils.buildComponent(orb);
      ServiceProperty[] props =
        new ServiceProperty[] {
            new ServiceProperty("offer.domain", "BusAdminLib Test"),
            new ServiceProperty("loop.index", Integer.toString(index)) };
      context.getOfferRegistry().registerService(component.getIComponent(),
        props);
    }
    List<ServiceOfferDesc> found = admin.getOffers();
    Assert.assertTrue(found.size() >= index);
    conn.logout();
    orb.shutdown(true);
    orb.destroy();
  }

}
