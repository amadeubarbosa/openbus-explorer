package busexplorer.panel.offers;

import busexplorer.ApplicationIcons;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.offers.properties.PropertiesDialog;
import tecgraf.openbus.admin.BusAdmin;

import java.awt.Window;
import java.awt.event.ActionEvent;

/**
 * Ação que apresenta propriedades da oferta.
 * 
 * @author Tecgraf
 * 
 */
public class OfferPropertiesAction extends OpenBusAction<OfferWrapper> {

  /**
   * Construtor.
   * 
   * @param parentWindow janela pai
   * @param admin biblioteca de admin
   */
  public OfferPropertiesAction(Window parentWindow, BusAdmin admin) {
    super(parentWindow, admin);
    putValue(SHORT_DESCRIPTION, getString("tooltip"));
    putValue(SMALL_ICON, ApplicationIcons.ICON_PROPS_16);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.OTHER_SINGLE_SELECTION;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    OfferWrapper offer = getTablePanelComponent().getSelectedElement();
    PropertiesDialog dialog = new PropertiesDialog(parentWindow, offer, admin);
    dialog.setVisible(true);
  }
}
