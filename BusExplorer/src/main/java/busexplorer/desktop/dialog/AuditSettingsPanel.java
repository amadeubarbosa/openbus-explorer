package busexplorer.desktop.dialog;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.util.Arrays;
import java.util.EventObject;
import java.util.function.Consumer;

import tecgraf.openbus.admin.BusAuditFacade;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.JSwitchBox;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;

public class AuditSettingsPanel {

  public static final String ENVIRONMENT = "environment";
  public static final String APPLICATION = "application";
  private BusExplorerTask<Void> retrieveTask;
  private BusExplorerTask<Void> updateTask;
  private JPanel uiElement;

  private static String getString(String key) {
    return Language.get(AuditSettingsPanel.class, key);
  }

  public static AuditSettingsPanel create(Window parentWindow) {
    String labelsConstraint = "grow";
    String fieldsConstraint = "grow";
    Color onColor = Color.orange;
    Color offColor = Color.lightGray;

    JPanel settingsPanel = new JPanel(new MigLayout("wrap 2, fill, insets 5", "[][grow]"));
    String onLabel = Language.get(JSwitchBox.class, "on");
    String offLabel = Language.get(JSwitchBox.class, "off");
    settingsPanel.add(new JLabel(getString("enabled")), labelsConstraint);
    final JSwitchBox auditEnabled = new JSwitchBox(onLabel, offLabel, onColor, offColor);
    auditEnabled.setSelected(false);
    auditEnabled.setToolTipText(getString("enabled.tooltip"));
    settingsPanel.add(auditEnabled, fieldsConstraint);

    settingsPanel.add(new JLabel(getString("url")), "grow, spany 2");
    final JTextArea auditServiceUrl = new JTextArea(3, 35);
    auditServiceUrl.setLineWrap(true);
    auditServiceUrl.setToolTipText(getString("url.tooltip"));
    settingsPanel.add(new JScrollPane(auditServiceUrl), "cell 1 1 1 2, grow");

    settingsPanel.add(new JLabel(getString("proxy")), labelsConstraint);
    final JTextArea httpProxy = new JTextArea(2, 35);
    httpProxy.setLineWrap(true);
    httpProxy.setToolTipText(getString("proxy.tooltip"));
    settingsPanel.add(new JScrollPane(httpProxy), fieldsConstraint);

    settingsPanel.add(new JLabel(getString("credentials")), labelsConstraint);
    final JPasswordField httpCredentials = new JPasswordField();
    httpCredentials.setToolTipText(getString("credentials.tooltip"));
    settingsPanel.add(httpCredentials, fieldsConstraint);

    settingsPanel.add(new JLabel(getString("fifolimit")), labelsConstraint);
    final JSpinner fifoLimit = new JSpinner(new SpinnerNumberModel(100000, 1, 10000000, 1));
    fifoLimit.setToolTipText(getString("fifolimit.tooltip"));
    settingsPanel.add(fifoLimit, fieldsConstraint);

    settingsPanel.add(new JLabel(getString("fifolength")), labelsConstraint);
    JPanel fifoLengthPanel = new JPanel(new MigLayout("fill, insets 0 3 0 0", "[grow]0[shrink]", "[]"));
    final JLabel fifoLength = new JLabel("0");
    fifoLength.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    fifoLength.setHorizontalAlignment(SwingConstants.RIGHT);
    fifoLength.setToolTipText(getString("fifolength.tooltip"));
    final JButton refreshFifoLength = new JButton(ApplicationIcons.ICON_REFRESH_16);
    refreshFifoLength.addActionListener(event -> new BusExplorerTask<Integer>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        if (Application.login().audit.isAuditCapable()) {
          setResult(Application.login().audit.getAuditFIFOLength());
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          fifoLength.setText(getResult().toString());
        }
      }
    }.execute(parentWindow, Language.get(MainDialog.class, "conf.apply.waiting.title"),
      Language.get(MainDialog.class, "conf.apply.waiting.msg")));
    fifoLengthPanel.add(fifoLength, "growx");
    fifoLengthPanel.add(refreshFifoLength);
    settingsPanel.add(fifoLengthPanel, fieldsConstraint);

    settingsPanel.add(new JLabel(getString("discard")), labelsConstraint);
    final JSwitchBox discardOnExit = new JSwitchBox(onLabel, offLabel, onColor, offColor);
    discardOnExit.setSelected(false);
    discardOnExit.setToolTipText(getString("discard.tooltip"));
    settingsPanel.add(discardOnExit, fieldsConstraint);

    settingsPanel.add(new JLabel(getString("tasks")), labelsConstraint);
    final JSpinner concurrentTasks = new JSpinner(new SpinnerNumberModel(5, 1, 10000, 1));
    concurrentTasks.setToolTipText(getString("tasks.tooltip"));
    settingsPanel.add(concurrentTasks, fieldsConstraint);

    settingsPanel.add(new JLabel(getString("retrytimeout")), labelsConstraint);
    final JSpinner retryTimeout = new JSpinner(new SpinnerNumberModel(5.0, 1.0, 3600.0, 1.0));
    retryTimeout.setToolTipText(getString("retrytimeout.tooltip"));
    settingsPanel.add(retryTimeout, fieldsConstraint);

    settingsPanel.add(new JLabel(getString("environment")), labelsConstraint);
    final JTextArea auditEnvironment = new JTextArea(3, 35);
    auditEnvironment.setLineWrap(true);
    auditEnvironment.setToolTipText(getString("environment.tooltip"));
    settingsPanel.add(new JScrollPane(auditEnvironment), fieldsConstraint);

    settingsPanel.add(new JLabel(getString("application")), labelsConstraint);
    final JTextField auditApplication = new JTextField();
    auditApplication.setHorizontalAlignment(SwingConstants.RIGHT);
    auditApplication.setToolTipText(getString("application.tooltip"));
    settingsPanel.add(auditApplication, fieldsConstraint);

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

    AuditSettingsPanel result = new AuditSettingsPanel();
    result.uiElement = settingsPanel;
    result.retrieveTask = new BusExplorerTask<Void>(ExceptionContext.BusCore) {
      boolean _admin = false;
      boolean _enabled = false;
      String _url = "";
      String _proxy = "";
      char[] _credentials = new char[] {};
      int _fifolimit = 0;
      int _fifolength = 0;
      boolean _discard = false;
      int _tasks = 0;
      double _timeout = 0.0;
      String _environment = "";
      String _application = "";
      boolean _capable = false;

      @Override
      protected void doPerformTask() throws Exception {
        BusAuditFacade audit = Application.login().audit;
        _admin = Application.login().hasAdminRights();
        if (audit.isAuditCapable()) {
          _capable = true;
          _enabled = audit.getAuditEnabled();
          _url = audit.getAuditServiceURL();
          Arrays.fill(_credentials, (char) 0);
          if (_admin) {
            byte[] bytes = audit.getAuditHttpAuth();
            _credentials = new char[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
              _credentials[i] = (char) bytes[i];
            }
          }
          else {
            _credentials = "".toCharArray();
          }
          _proxy = audit.getAuditHttpProxy();
          _fifolimit = audit.getAuditFIFOLimit();
          if (_enabled) {
            _fifolength = audit.getAuditFIFOLength();
          }
          else {
            _fifolength = 0;
          }
          _discard = audit.getAuditDiscardOnExit();
          _tasks = audit.getAuditPublishingTasks();
          _timeout = audit.getAuditPublishingRetryTimeout();
          Arrays.asList(audit.getAuditEventTemplate()).forEach(nameValueString -> {
            if (nameValueString.name.equals(ENVIRONMENT)) {
              _environment = nameValueString.value;
            }
            if (nameValueString.name.equals(APPLICATION)) {
              _application = nameValueString.value;
            }
          });

        }
        else {
          _capable = false;
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          if (_capable) {
            auditEnabled.setSelected(_enabled);
            auditEnabled.setEnabled(_admin);
            auditServiceUrl.setText(_url);
            auditServiceUrl.setEnabled(_admin);
            httpCredentials.setText(String.valueOf(_credentials));
            Arrays.fill(_credentials, (char) 0);
            httpCredentials.setEnabled(_admin);
            httpProxy.setText(_proxy);
            httpProxy.setEnabled(_admin);
            fifoLimit.setValue(_fifolimit);
            fifoLimit.setEnabled(_admin);
            fifoLength.setText(Integer.valueOf(_fifolength).toString());
            fifoLength.setEnabled(_admin);
            discardOnExit.setSelected(_discard);
            discardOnExit.setEnabled(_admin);
            if (_enabled) {
              refreshFifoLength.setEnabled(true);
              concurrentTasks.setValue(_tasks);
              concurrentTasks.setEnabled(false);
            }
            else {
              refreshFifoLength.setEnabled(false);
              concurrentTasks.setEnabled(_admin);
            }
            retryTimeout.setValue(_timeout);
            retryTimeout.setEnabled(_admin);
            auditEnvironment.setText(_environment);
            auditEnvironment.setEnabled(_admin);
            auditApplication.setText(_application);
            auditApplication.setEnabled(_admin);

            applyButton.setEnabled(false);
            cancelButton.setEnabled(false);
          }
          else {
            auditEnabled.setSelected(false);
            auditEnabled.setEnabled(false);
            auditServiceUrl.setEnabled(false);
            httpCredentials.setEnabled(false);
            httpProxy.setEnabled(false);
            fifoLimit.setEnabled(false);
            refreshFifoLength.setEnabled(false);
            fifoLength.setEnabled(false);
            discardOnExit.setEnabled(false);
            concurrentTasks.setEnabled(false);
            retryTimeout.setEnabled(false);
            auditEnvironment.setEnabled(false);
            auditApplication.setEnabled(false);

            applyButton.setEnabled(false);
            cancelButton.setEnabled(false);
          }
        }
      }
    };
    result.updateTask = new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        BusAuditFacade audit = Application.login().audit;

        boolean hasActivationChanged = (audit.getAuditEnabled() != auditEnabled.isSelected());

        audit.setAuditServiceURL(auditServiceUrl.getText().trim());
        audit.setAuditHttpProxy(httpProxy.getText().trim());

        char[] chars = httpCredentials.getPassword();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
          bytes[i] = (byte) chars[i];
        }
        audit.setAuditHttpAuth(bytes);
        Arrays.fill(bytes, (byte) 0);
        Arrays.fill(chars, (char) 0);

        audit.setAuditFIFOLimit(((SpinnerNumberModel) fifoLimit.getModel()).getNumber().intValue());
        audit.setAuditDiscardOnExit(discardOnExit.isSelected());
        audit.setAuditPublishingRetryTimeout(((SpinnerNumberModel) retryTimeout.getModel()).getNumber().doubleValue());
        audit.setAuditEventTemplate(ENVIRONMENT, auditEnvironment.getText().trim());
        audit.setAuditEventTemplate(APPLICATION, auditApplication.getText().trim());

        int updatedConcurrentTasks = ((SpinnerNumberModel) concurrentTasks.getModel()).getNumber().intValue();
        // if agent is not running yet we can set the concurrency level
        if (!audit.getAuditEnabled()) {
          audit.setAuditPublishingTasks(updatedConcurrentTasks);
        }

        if (hasActivationChanged) {
          // start/stop
          audit.setAuditEnabled(auditEnabled.isSelected());
          // after the procedure if the agent was stopped, then now we set the concurrency level
          if (!audit.getAuditEnabled()) {
            audit.setAuditPublishingTasks(updatedConcurrentTasks);
          }
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          // strip blank chars from text fields
          auditServiceUrl.setText(auditServiceUrl.getText().trim());
          httpProxy.setText(httpProxy.getText().trim());
          auditEnvironment.setText(auditEnvironment.getText().trim());
          auditApplication.setText(auditApplication.getText().trim());

          concurrentTasks.setEnabled(!auditEnabled.isSelected());
          refreshFifoLength.setEnabled(auditEnabled.isSelected());
          applyButton.setEnabled(false);
          cancelButton.setEnabled(false);
        }
      }
    };
    applyButton.addActionListener(actionEvent -> result.getUpdateTask()
      .execute(parentWindow, Language.get(MainDialog.class, "conf.apply.waiting.title"),
        Language.get(MainDialog.class, "conf.apply.waiting.msg")));
    cancelButton.addActionListener(actionEvent -> result.getRetrieveTask()
      .execute(parentWindow, Language.get(MainDialog.class, "conf.apply.waiting.title"),
        Language.get(MainDialog.class, "conf.apply.waiting.msg")));

    Consumer<EventObject> activateButtons = arg -> {
      applyButton.setEnabled(true);
      cancelButton.setEnabled(true);
    };
    DocumentListener listener = new DocumentListener() {
      private void testAndActivate() {
        if (!applyButton.isEnabled()) {
          activateButtons.accept(null);
        }
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        testAndActivate();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        testAndActivate();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        testAndActivate();
      }
    };

    auditEnabled.addItemListener(activateButtons::accept);
    discardOnExit.addItemListener(activateButtons::accept);

    auditServiceUrl.getDocument().addDocumentListener(listener);
    httpCredentials.getDocument().addDocumentListener(listener);
    httpProxy.getDocument().addDocumentListener(listener);
    auditEnvironment.getDocument().addDocumentListener(listener);
    auditApplication.getDocument().addDocumentListener(listener);

    fifoLimit.addChangeListener(activateButtons::accept);
    concurrentTasks.addChangeListener(activateButtons::accept);
    retryTimeout.addChangeListener(activateButtons::accept);

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
