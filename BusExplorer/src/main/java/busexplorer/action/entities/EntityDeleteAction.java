package busexplorer.action.entities;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntity;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;
import busexplorer.wrapper.EntityInfo;

/**
 * Classe de a��o para a remo��o de uma entidade.
 * 
 * @author Tecgraf
 */
public class EntityDeleteAction extends OpenBusAction<EntityInfo> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin
   */
  public EntityDeleteAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(EntityDeleteAction.class.getSimpleName()
      + ".name"));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ActionType getActionType() {
    return ActionType.REMOVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    int option =
      JOptionPane.showConfirmDialog(parentWindow, getString("confirm.msg"),
        getString("confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }

    Task<Object> task = new Task<Object>() {
      @Override
      protected void performTask() throws Exception {
        EntityInfo entity = getPanelComponent().getSelectedElement();
        RegisteredEntity ref = entity.getDescriptor().ref;
        ref.remove();
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          getPanelComponent().removeSelectedElements();
        }
      }
    };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
