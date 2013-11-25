package busexplorer.action.entities;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.Task;
import tecgraf.openbus.core.v2_0.services.offer_registry.admin.v1_0.RegisteredEntityDesc;
import test.ActionType;
import test.OpenBusAction;
import admin.BusAdmin;

/**
 * Classe de a��o para a remo��o de uma entidade.
 * 
 * @author Tecgraf
 */
public class EntityDeleteAction extends OpenBusAction<RegisteredEntityDesc> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin
   */
  public EntityDeleteAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get("EntityDeleteAction.name"));
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
      JOptionPane.showConfirmDialog(parentWindow, LNG
        .get("DeleteAction.confirm.msg"),
        LNG.get("DeleteAction.confirm.title"), JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (option != JOptionPane.YES_OPTION) {
      return;
    }

    Task<Boolean> task = new Task<Boolean>() {
      @Override
      protected void performTask() throws Exception {
        // TODO: verificar consistencia.
        // � mais pr�tico permitir a remo��o de apenas uma entidade por vez 
        RegisteredEntityDesc entity = getPanelComponent().getSelectedElement();
        setResult(true);
        if (!admin.removeEntity(entity.id)) {
          setResult(false);
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus() && getResult()) {
          getPanelComponent().removeSelectedElements();
        }
      }
    };

    task.execute(parentWindow, LNG.get("DeleteAction.waiting.title"), LNG
      .get("DeleteAction.waiting.msg"));
  }
}
