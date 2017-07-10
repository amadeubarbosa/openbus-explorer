package busexplorer.panel.consumers;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import org.omg.CORBA.BAD_PARAM;
import tecgraf.openbus.services.governance.v1_0.Consumer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
import java.awt.Window;
import java.util.Arrays;

/**
 * Diálogo de adição/edição de consumidores que será responsável pela manutenção
 * de objetos {@link ConsumerWrapper} (para uso nos componentes swing) a partir de
 * uma referência remota do objeto do consumidor {@link Consumer}.
 *
 * @see ConsumerWrapper
 * @see Consumer
 *
 * @author Tecgraf
 */
public class ConsumerInputDialog extends BusExplorerAbstractInputDialog {
  private JTextField nameTextField;
  private JTextField codeTextField;
  private JTextField supportOfficeTextField;
  private JTextField managerOfficeTextField;
  private JTextField supportTextField;
  private JTextField managerTextField;
  private JTextArea queryTextField;

  private TablePanelComponent<ConsumerWrapper> panel;

  private ConsumerWrapper editingConsumer = null;

  /**
   * Construtor do diálogo de adição/edição de consumidores.
   *
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel de {@link ConsumerWrapper} a ser atualizado após a adição/edição.
   */
  public ConsumerInputDialog(Window parentWindow,
                             TablePanelComponent<ConsumerWrapper> panel) {
    super(parentWindow);
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean accept() {
    if (!hasValidFields()) {
      return false;
    }

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
        if (editingConsumer == null) {
          Consumer consumer;
          try {
            consumer = Application.login().extension
              .getConsumerRegistry().add(nameTextField.getText());
          } catch (BAD_PARAM e) {
            throw new IllegalArgumentException(
              Language.get(ConsumerInputDialog.class, "error.alreadyinuse.name"), e);
          }
          consumer.code(codeTextField.getText().trim());
          consumer.supportOffice(supportOfficeTextField.getText().trim());
          consumer.managerOffice(managerOfficeTextField.getText().trim());
          consumer.support(supportTextField.getText().trim().split(","));
          consumer.manager(managerTextField.getText().trim().split(","));
          consumer.busquery(queryTextField.getText().trim());
          editingConsumer = new ConsumerWrapper(consumer);
        } else {
          try {
            editingConsumer.name(nameTextField.getText().trim());
          } catch (BAD_PARAM e) {
            throw new IllegalArgumentException(
              Language.get(ConsumerInputDialog.class, "error.alreadyinuse.name"), e);
          }
          editingConsumer.code(codeTextField.getText().trim());
          editingConsumer.supportOffice(supportOfficeTextField.getText().trim());
          editingConsumer.managerOffice(managerOfficeTextField.getText().trim());
          editingConsumer.support(Arrays.asList(supportTextField.getText().trim().split(",")));
          editingConsumer.manager(Arrays.asList(managerTextField.getText().trim().split(",")));
          editingConsumer.busquery(queryTextField.getText().trim());
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
          panel.selectElement(editingConsumer, true);
        }
      }
    };

    task.execute(this, Language.get(this.getClass(), "waiting.title"),
      Language.get(this.getClass(), "waiting.msg"));
    return task.getStatus();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected JPanel buildFields() {
    setMinimumSize(new Dimension(400, 550));
    JPanel panel = new JPanel(new MigLayout("fill, flowy"));

    JLabel nameLabel = new JLabel(Language.get(this.getClass(), "name.label"));
    panel.add(nameLabel, "grow");

    nameTextField =
      new JTextField();
    nameTextField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        if (nameTextField.getText().trim().isEmpty()) {
          setErrorMessage(Language.get(ConsumerInputDialog.class,
            "error.validation.name"));
        } else {
          clearErrorMessage();
        }
      }

      @Override
      public void removeUpdate(DocumentEvent documentEvent) {
        this.insertUpdate(documentEvent); //no difference
      }

      @Override
      public void changedUpdate(DocumentEvent documentEvent) {

      }
    });
    panel.add(nameTextField, "grow");

    JLabel codeLabel = new JLabel(Language.get(this.getClass(), "code.label"));
    panel.add(codeLabel, "grow");

    codeTextField = new JTextField();
    panel.add(codeTextField, "grow");

    JLabel supportOfficeLabel = new JLabel(Language.get(this.getClass(), "supportoffice.label"));
    panel.add(supportOfficeLabel, "grow");

    supportOfficeTextField = new JTextField();
    panel.add(supportOfficeTextField, "grow");

    JLabel supportLabel = new JLabel(Language.get(this.getClass(), "support.label"));
    panel.add(supportLabel, "grow");

    supportTextField = new JTextField();
    panel.add(supportTextField, "grow");

    JLabel managerOfficeLabel = new JLabel(Language.get(this.getClass(), "manageroffice.label"));
    panel.add(managerOfficeLabel, "grow");

    managerOfficeTextField = new JTextField();
    panel.add(managerOfficeTextField, "grow");

    JLabel managerLabel = new JLabel(Language.get(this.getClass(), "manager.label"));
    panel.add(managerLabel, "grow");

    managerTextField = new JTextField();
    panel.add(managerTextField, "grow");

    JLabel queryLabel = new JLabel(Language.get(this.getClass(), "busquery.label"));
    panel.add(queryLabel, "grow");

    queryTextField = new JTextArea(5, 20);
    queryTextField.setLineWrap(true);
    panel.add(new JScrollPane(queryTextField), "grow");

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (nameTextField.getText().trim().isEmpty()) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.name"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  /**
   * Configura o diálogo para trabalhar em modo de edição.
   *
   * @param info o dado sendo editado.
   */
  public void setEditionMode(ConsumerWrapper info) {
    this.editingConsumer = info;
    this.nameTextField.setText(info.name());
    this.codeTextField.setText(info.code());
    this.supportOfficeTextField.setText(info.supportOffice());
    this.managerOfficeTextField.setText(info.managerOffice());
    this.supportTextField.setText(String.join(", ", info.support()));
    this.managerTextField.setText(String.join(", ", info.manager()));
    this.queryTextField.setText(info.busquery());
  }

}
