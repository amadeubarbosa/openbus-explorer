package test;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import scs.core.ComponentContext;
import tecgraf.openbus.assistant.Assistant;
import tecgraf.openbus.assistant.AssistantParams;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceOfferDesc;
import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceProperty;
import admin.BusAdminImpl;

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
  public void listOfferTest() throws Throwable {
    AssistantParams params = new AssistantParams();
    params.interval = 1;
    Assistant assist =
      Assistant.createWithPassword(host, port, entity, password, params);

    try {
      Thread.sleep(1500);
    }
    catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    BusAdminImpl admin = new BusAdminImpl(host, port, assist.orb());

    int index;
    for (index = 0; index < 5; index++) {
      ComponentContext context = Utils.buildComponent(assist.orb());
      ServiceProperty[] props =
        new ServiceProperty[] {
            new ServiceProperty("offer.domain", "Assistant Test"),
            new ServiceProperty("loop.index", Integer.toString(index)) };
      assist.registerService(context.getIComponent(), props);
    }
    Thread.sleep(params.interval * 3 * 1000);
    List<ServiceOfferDesc> found = admin.getOffers();
    Assert.assertTrue(found.size() >= index);
    assist.shutdown();

  }

}
