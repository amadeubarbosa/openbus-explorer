package busexplorer.panel.consumers;

import busexplorer.Application;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import tecgraf.javautils.core.lng.LNG;
import tecgraf.openbus.admin.BusAdmin;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Classe de a��o para a remo��o de uma entidade.
 * 
 * @author Tecgraf
 */
public class ConsumerDeleteAction extends OpenBusAction<ConsumerWrapper> {

  /**
   * Construtor da a��o.
   * 
   * @param parentWindow janela m�e do di�logo que a ser criado pela a��o
   * @param admin inst�ncia do busadmin
   */
  public ConsumerDeleteAction(JFrame parentWindow, BusAdmin admin) {
    super(parentWindow, admin, LNG.get(ConsumerDeleteAction.class.getSimpleName()
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
  public boolean abilityConditions() {
    return Application.login() != null && Application.login().hasAdminRights();
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

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

        @Override
        protected void performTask() throws Exception {
          List<ConsumerWrapper> consumers = getTablePanelComponent().getSelectedElements();
          for (ConsumerWrapper consumer : consumers) {
            Application.login().extension.getConsumerRegistry().remove(consumer.name());
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            getTablePanelComponent().removeSelectedElements();
          }
        }
      };

    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"));
  }
}
