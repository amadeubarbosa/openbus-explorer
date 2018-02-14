package busexplorer.desktop.dialog;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.awt.Window;

import tecgraf.openbus.admin.BusAdminFacade;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;

public class BasicSettingsPanel {

  private BusExplorerTask<Void> retrieveTask;
  private BusExplorerTask<Void> updateTask;
  private JPanel uiElement;

  public static BasicSettingsPanel create(Window parentWindow) {
    String labelsConstraint = "grow";
    String fieldsConstraint = "grow";
    JPanel settingsPanel = new JPanel(new MigLayout("wrap 2, fill, insets 5"));
    settingsPanel.add(new JLabel(Language.get(MainDialog.class, "conf.busloglevel")), labelsConstraint);
    final JSpinner busLogLevelSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 7, 1));
    busLogLevelSpinner.setToolTipText(Language.get(MainDialog.class, "conf.busloglevel.tooltip"));
    settingsPanel.add(busLogLevelSpinner, fieldsConstraint);

    settingsPanel.add(new JLabel(Language.get(MainDialog.class, "conf.oilloglevel")), labelsConstraint);
    final JSpinner oilLogLevelSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 6, 1));
    oilLogLevelSpinner.setToolTipText(Language.get(MainDialog.class, "conf.oilloglevel.tooltip"));
    settingsPanel.add(oilLogLevelSpinner, fieldsConstraint);

    settingsPanel.add(new JLabel(Language.get(MainDialog.class, "conf.maxchannels")), labelsConstraint);
    final JSpinner maxChannelsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1024, 1));
    maxChannelsSpinner.setToolTipText(Language.get(MainDialog.class, "conf.maxchannels.tooltip"));
    settingsPanel.add(maxChannelsSpinner, fieldsConstraint);

    settingsPanel.add(new JLabel(Language.get(MainDialog.class, "conf.maxcachesize")), labelsConstraint);
    final JSpinner maxCacheSizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000000, 1));
    maxCacheSizeSpinner.setToolTipText(Language.get(MainDialog.class, "conf.maxcachesize.tooltip"));
    settingsPanel.add(maxCacheSizeSpinner, fieldsConstraint);

    settingsPanel.add(new JLabel(Language.get(MainDialog.class, "conf.timeout")), labelsConstraint);
    final JSpinner timeoutSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 3600, 1));
    timeoutSpinner.setToolTipText(Language.get(MainDialog.class, "conf.timeout.tooltip"));
    settingsPanel.add(timeoutSpinner, fieldsConstraint);

    final JButton cancelButton = new JButton(Language.get(MainDialog.class, "conf.cancel"));
    cancelButton.setToolTipText(Language.get(MainDialog.class, "conf.cancel.tooltip"));
    cancelButton.setIcon(ApplicationIcons.ICON_CANCEL_16);
    cancelButton.setEnabled(false);

    final JButton applyButton = new JButton(Language.get(MainDialog.class, "conf.apply"));
    applyButton.setIcon(ApplicationIcons.ICON_VALIDATE_16);
    applyButton.setEnabled(false);
    applyButton.setToolTipText(Language.get(MainDialog.class, "conf.apply.tooltip"));

    // force a different disabled text color to make spinner values visible
    // even if user is not allowed to edit
    for (Component c : settingsPanel.getComponents()) {
      if (c instanceof JSpinner) {
        JSpinner spinner = (JSpinner) c;
        ((JSpinner.NumberEditor) spinner.getEditor()).getTextField()
                                                     .setDisabledTextColor(UIManager.getColor("TextField.foreground"));
      }
    }

    settingsPanel.add(cancelButton, "align right, spanx 2, split 2");
    settingsPanel.add(applyButton);

    BasicSettingsPanel result = new BasicSettingsPanel();
    result.uiElement = settingsPanel;
    result.retrieveTask = new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      int maxChannels = 0;
      int maxCacheSize = 0;
      int timeout = 0;
      int busLogLevel = 0;
      int oilLogLevel = 0;

      @Override
      protected void doPerformTask() throws Exception {
        BusAdminFacade admin = Application.login().admin;
        maxChannels = admin.getMaxChannels();
        maxCacheSize = admin.getMaxCacheSize();
        timeout = admin.getCallsTimeout();
        busLogLevel = admin.getLogLevel();
        oilLogLevel = admin.getOilLogLevel();
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          busLogLevelSpinner.setValue(busLogLevel);
          oilLogLevelSpinner.setValue(oilLogLevel);
          maxChannelsSpinner.setValue(maxChannels);
          maxCacheSizeSpinner.setValue(maxCacheSize);
          timeoutSpinner.setValue(timeout);
          applyButton.setEnabled(false);
          cancelButton.setEnabled(false);
        }
      }
    };

    result.updateTask = new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      @Override
      protected void doPerformTask() throws Exception {
        BusAdminFacade admin = Application.login().admin;
        admin.setMaxChannels(((SpinnerNumberModel) maxChannelsSpinner.getModel()).getNumber().intValue());
        admin.setMaxCacheSize(((SpinnerNumberModel) maxCacheSizeSpinner.getModel()).getNumber().intValue());
        admin.setCallsTimeout(((SpinnerNumberModel) timeoutSpinner.getModel()).getNumber().intValue());
        admin.setLogLevel(((SpinnerNumberModel) busLogLevelSpinner.getModel()).getNumber().shortValue());
        admin.setOilLogLevel(((SpinnerNumberModel) oilLogLevelSpinner.getModel()).getNumber().shortValue());
      }

      @Override
      protected void afterTaskUI() {
        applyButton.setEnabled(false);
        cancelButton.setEnabled(false);
      }
    };
    applyButton.addActionListener(actionEvent -> result.getRetrieveTask()
      .execute(parentWindow, Language.get(MainDialog.class, "conf.apply.waiting.title"),
        Language.get(MainDialog.class, "conf.apply.waiting.msg")));
    cancelButton.addActionListener(actionEvent -> result.getRetrieveTask().execute(parentWindow,
      Language.get(MainDialog.class, "conf.apply.waiting.title"),
      Language.get(MainDialog.class, "conf.apply.waiting.msg")));
    ChangeListener activateButtons = changeEvent -> {
      applyButton.setEnabled(true);
      cancelButton.setEnabled(true);
    };
    maxChannelsSpinner.addChangeListener(activateButtons);
    maxCacheSizeSpinner.addChangeListener(activateButtons);
    timeoutSpinner.addChangeListener(activateButtons);
    busLogLevelSpinner.addChangeListener(activateButtons);
    oilLogLevelSpinner.addChangeListener(activateButtons);

    return result;
  }

  public BusExplorerTask<Void> getRetrieveTask() {
    return retrieveTask;
  }

  public BusExplorerTask<Void> getUpdateTask() {
    return updateTask;
  }

  public void activate(boolean shouldActivate) {
    for (Component c : this.panel().getComponents()) {
      if (c instanceof JSpinner) {
        JSpinner spinner = (JSpinner) c;
        spinner.setEnabled(shouldActivate);
        ((JSpinner.NumberEditor) spinner.getEditor()).getTextField().setEditable(shouldActivate);
      }
      else if (c instanceof JTextComponent) {
        ((JTextComponent) c).setEditable(shouldActivate);
      }
      else if (c instanceof JCheckBox) {
        c.setEnabled(shouldActivate);
      }
    }
  }

  public JPanel panel() {
    return uiElement;
  }
}
