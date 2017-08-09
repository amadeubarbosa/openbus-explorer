package busexplorer.panel;

import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.BusQuery;
import tecgraf.openbus.core.v2_1.services.ServiceFailure;
import tecgraf.openbus.core.v2_1.services.offer_registry.admin.v1_0.RegisteredEntityDesc;

import javax.script.ScriptException;
import javax.swing.JOptionPane;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.function.Function;

public class BusQueryValidateAction<T,String> extends OpenBusAction {

  private final Function<T, String> extract;
  private final T uicomponent;

  public BusQueryValidateAction(Window parent, T component, Function<T, String> extract) {
    super(parent);
    this.uicomponent = component;
    this.extract = extract;
    putValue(SHORT_DESCRIPTION, getString("tooltip"));
    putValue(SMALL_ICON, ApplicationIcons.ICON_GAUGE_16);
  }

  boolean isValidExpression() throws ScriptException {
    String expression = this.extract.apply(this.uicomponent);
    return new BusQuery((java.lang.String) expression).isValid();
  }

  ArrayList<RegisteredEntityDesc> evaluateExpression() throws  ScriptException, ServiceFailure {
    String expression = this.extract.apply(this.uicomponent);
    return new BusQuery((java.lang.String) expression).filterEntities();
  }

  @Override
  public boolean abilityConditions() {
    try {
      return this.isValidExpression();
    } catch (ScriptException ex) {
      return false;
    }
  }

  @Override
  public ActionType getActionType() {
    return ActionType.OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent ev) {
    BusExplorerTask<ArrayList<RegisteredEntityDesc>> task =
      new BusExplorerTask<ArrayList<RegisteredEntityDesc>>(ExceptionContext.BusCore) {

        boolean assignmentTest = false;

        @Override
        protected void doPerformTask() throws Exception {
          if (BusQueryValidateAction.this.isValidExpression()) {
            setResult(BusQueryValidateAction.this.evaluateExpression());
          } else {
            assignmentTest = true;
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            if (!assignmentTest && (getResult().size() > 0)) {
              JOptionPane.showMessageDialog(parentWindow, getString("result.success"),
                getString("waiting.title"), JOptionPane.INFORMATION_MESSAGE);
            } else {
              if (assignmentTest) {
                JOptionPane.showMessageDialog(parentWindow, getString("prohibited.assignment"),
                  getString("waiting.title"), JOptionPane.ERROR_MESSAGE);
              } else {
                JOptionPane.showMessageDialog(parentWindow, getString("result.none"),
                  getString("waiting.title"), JOptionPane.ERROR_MESSAGE);
              }
            }
          }
        }
      };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);

  }
}
