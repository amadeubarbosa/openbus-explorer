package busexplorer.panel.offers.properties;

import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import tecgraf.openbus.core.v2_0.services.offer_registry.ServiceProperty;
import busexplorer.panel.PanelActionInterface;
import busexplorer.panel.PanelComponent;
import busexplorer.utils.Utils;
import busexplorer.wrapper.OfferInfo;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de Entidades
 * 
 * @author Tecgraf
 */
public class PropertiesDialog extends JDialog {

  /**
   * O painel do diálogo
   */
  private PanelComponent<ServiceProperty> panel;

  /**
   * Oferta sendo visualizada.
   */
  private OfferInfo offer;

  /**
   * Construtor.
   * 
   * @param owner janela pai
   * @param offer oferta sendo visualizada
   */
  public PropertiesDialog(Window owner, OfferInfo offer) {
    super(owner, Utils.getString(PropertiesDialog.class, "title"),
      JDialog.ModalityType.APPLICATION_MODAL);
    this.offer = offer;
    initPanel();
  }

  /**
   * Inicializa o painel do diálogo.
   */
  private void initPanel() {
    List<PanelActionInterface<ServiceProperty>> actions =
      new ArrayList<PanelActionInterface<ServiceProperty>>();
    actions.add(new PropertiesRefreshAction(this, offer));
    List<ServiceProperty> props =
      Arrays.asList(offer.getDescriptor().properties);
    PropertiesTableProvider provider = new PropertiesTableProvider();
    panel = new PanelComponent<ServiceProperty>(props, provider, actions);
    setContentPane(panel);
    pack();
  }

}
