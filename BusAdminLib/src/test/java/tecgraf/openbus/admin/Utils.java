package tecgraf.openbus.admin;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import scs.core.ComponentContext;
import scs.core.ComponentId;
import scs.core.IComponentServant;
import tecgraf.openbus.Connection;
import tecgraf.openbus.OpenBusContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilit�ria para os testes.
 * 
 * @author Tecgraf
 */
public class Utils {

  /**
   * L� um arquivo de propriedades.
   * 
   * @param fileName o nome do arquivo.
   * @return as propriedades.
   * @throws IOException caso n�o seja poss�vel ler o arquivo
   */
  static public Properties readPropertyFile(String fileName) throws IOException {
    Properties properties = new Properties();
    InputStream propertiesStream = Utils.class.getResourceAsStream(fileName);
    if (propertiesStream == null) {
      throw new FileNotFoundException(String.format(
        "O arquivo de propriedades '%s' n�o foi encontrado", fileName));
    }
    try {
      properties.load(propertiesStream);
    }
    finally {
      try {
        propertiesStream.close();
      }
      catch (IOException e) {
        System.err
          .println("Ocorreu um erro ao fechar o arquivo de propriedades");
        e.printStackTrace();
      }
    }
    return properties;
  }

  /**
   * Constr�i componente para o teste de verifica��o de CallerChain dentro de um
   * m�todo de despacho.
   * 
   * @param context o contexto da comunica��o com o barramento
   * @return o contexto do componente SCS
   * @throws Exception caso aconte�a algum erro na constru��o do componente SCS
   */
  public static ComponentContext buildTestCallerChainComponent(
    final OpenBusContext context) throws Exception {
    ComponentContext component = buildComponent(context.ORB());
    component.updateFacet("IComponent", new IComponentServant(component) {
      /**
       * M�todo vai lan�ar uma exce��o caso n�o consiga recuperar uma cadeia
       * v�lida. O que far� com que o m�todo de registro de servi�o falhe,
       * fazendo com que o teste tamb�m acuse a falha.
       */
      @Override
      public Object getFacetByName(String arg0) {
        if (context.callerChain() == null) {
          throw new IllegalStateException(
            "CallerChain nunca deveria ser nulo dentro de um m�todo de despacho.");
        }
        return super.getFacetByName(arg0);
      }
    });
    return component;
  }

  /**
   * Constr�i componente para o teste de verifica��o de CallerChain dentro de um
   * m�todo de despacho.
   * 
   * @param context o contexto da comunica��o com o barramento
   * @return o contexto do componente SCS
   * @throws Exception caso aconte�a algum erro na constru��o do componente SCS
   */
  public static ComponentContext buildTestConnectionComponent(
    final OpenBusContext context) throws Exception {
    ComponentContext component = buildComponent(context.ORB());
    component.updateFacet("IComponent", new IComponentServant(component) {
      /**
       * M�todo vai lan�ar uma exce��o caso n�o consiga recuperar uma conex�o. O
       * que far� com que o m�todo de registro de servi�o falhe, fazendo com que
       * o teste tamb�m acuse a falha.
       */
      @Override
      public Object getFacetByName(String arg0) {
        Connection connection = context.currentConnection();
        if (connection == null) {
          throw new IllegalStateException(
            "CurrentConnection nunca deveria ser nulo dentro de um m�todo de despacho.");
        }
        return super.getFacetByName(arg0);
      }
    });
    return component;
  }

  /**
   * Constr�i um componente SCS
   * 
   * @param orb a inst�ncia do {@link ORB} CORBA em uso na comunica��o com o barramento
   * @return o contexto do componente SCS
   * @throws Exception caso aconte�a algum erro na constru��o do componente SCS
   */
  public static ComponentContext buildComponent(ORB orb) throws Exception {
    POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
    poa.the_POAManager().activate();
    ComponentId id =
      new ComponentId("TestComponent", (byte) 1, (byte) 0, (byte) 0, "java");
    return new ComponentContext(orb, poa, id);
  }

  /**
   * Configura o n�vel de log
   * 
   * @param level n�vel do log.
   */
  public static void setLogLevel(Level level) {
    Logger logger = Logger.getLogger("tecgraf.openbus");
    logger.setLevel(level);
    logger.setUseParentHandlers(false);
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(level);
    logger.addHandler(handler);
  }
}
