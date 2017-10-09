package busexplorer.panel.offers.properties;

import busexplorer.panel.TablePanelActionInterface;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.offers.OfferWrapper;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.core.v2_1.services.offer_registry.ServiceProperty;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de Entidades
 * 
 * @author Tecgraf
 */
public class PropertiesDialog extends JDialog {
  /**
   * Oferta sendo visualizada.
   */
  private final OfferWrapper offer;

  /**
   * Construtor.
   *  @param owner janela pai
   * @param offer oferta sendo visualizada
   */
  public PropertiesDialog(Window owner, OfferWrapper offer) {
    super(owner, Language.get(PropertiesDialog.class, "title"),
      JDialog.ModalityType.APPLICATION_MODAL);
    this.offer = offer;
    initPanel();
  }

  /**
   * Inicializa o painel do diálogo.
   */
  private void initPanel() {
    JPanel panel = new JPanel(new GridBagLayout());

    List<TablePanelActionInterface<ServiceProperty>> actions =
      new ArrayList<TablePanelActionInterface<ServiceProperty>>();
    actions.add(new PropertiesRefreshAction(this, offer));
    List<ServiceProperty> props =
      Arrays.asList(offer.getDescriptor().properties);
    PropertiesTableProvider provider = new PropertiesTableProvider();
    TablePanelComponent<ServiceProperty> propertiesPanel = new
            TablePanelComponent<ServiceProperty>(props, provider, actions);
    panel.add(propertiesPanel, new GBC(0, 0).both());

    JButton closeButton = new JButton(Language.get(PropertiesDialog.class,
      "button.close"));
    closeButton.addActionListener(e -> dispose());
    panel.add(closeButton, new GBC(0, 1).none().east().insets(9, 9, 9, 9));

    setContentPane(panel);
    pack();
  }

}
