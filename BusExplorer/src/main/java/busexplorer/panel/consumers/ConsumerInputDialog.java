package busexplorer.panel.consumers;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdminFacade;
import tecgraf.openbus.services.governance.v1_0.Consumer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
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
  private JLabel nameLabel;
  private JTextField nameTextField;
  private JLabel codeLabel;
  private JTextField codeTextField;
  private JLabel officeLabel;
  private JTextField officeTextField;
  private JLabel supportLabel;
  private JTextField supportTextField;
  private JLabel managerLabel;
  private JTextField managerTextField;
  private JLabel queryLabel;
  private JTextField queryTextField;

  private TablePanelComponent<ConsumerWrapper> panel;

  private ConsumerWrapper editingConsumer = null;

  /**
   * Construtor do diálogo de adição/edição de consumidores.
   *
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel de {@link ConsumerWrapper} a ser atualizado após a adição/edição.
   * @param admin Referência para fachada {@link BusAdminFacade} do Serviço de Configuração.
   */
  public ConsumerInputDialog(Window parentWindow,
                             TablePanelComponent<ConsumerWrapper> panel, BusAdminFacade admin) {
    super(parentWindow, admin);
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

    BusExplorerTask<Object> task =
      new BusExplorerTask<Object>(Application.exceptionHandler(),
        ExceptionContext.BusCore) {

      @Override
      protected void performTask() throws Exception {
        if (editingConsumer == null) {
          Consumer consumer =
            Application.login().extension
              .getConsumerRegistry().add(nameTextField.getText());
          consumer.code(codeTextField.getText());
          consumer.office(officeTextField.getText());
          consumer.support(supportTextField.getText().split(","));
          consumer.manager(managerTextField.getText().split(","));
          consumer.busquery(queryTextField.getText());
          editingConsumer = new ConsumerWrapper(consumer);
        } else {
          editingConsumer.name(nameTextField.getText());
          editingConsumer.code(codeTextField.getText());
          editingConsumer.office(officeTextField.getText());
          editingConsumer.support(Arrays.asList(supportTextField.getText().split(",")));
          editingConsumer.manager(Arrays.asList(managerTextField.getText().split(",")));
          editingConsumer.busquery(queryTextField.getText());
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
    JPanel panel = new JPanel(new GridBagLayout());
    GBC baseGBC = new GBC().gridx(0).insets(5).west();

    nameLabel =
      new JLabel(Language.get(this.getClass(), "name.label"));
    panel.add(nameLabel, new GBC(baseGBC).gridy(0).none());

    nameTextField =
      new JTextField();
    panel.add(nameTextField, new GBC(baseGBC).gridy(1).horizontal());

    codeLabel =
      new JLabel(Language.get(this.getClass(), "code.label"));
    panel.add(codeLabel, new GBC(baseGBC).gridy(2).none());

    codeTextField = new JTextField();
    panel.add(codeTextField, new GBC(baseGBC).gridy(3).horizontal());

    officeLabel =
      new JLabel(Language.get(this.getClass(), "office.label"));
    panel.add(officeLabel, new GBC(baseGBC).gridy(4).none());

    officeTextField = new JTextField();
    panel.add(officeTextField, new GBC(baseGBC).gridy(5).horizontal());

    supportLabel =
      new JLabel(Language.get(this.getClass(), "support.label"));
    panel.add(supportLabel, new GBC(baseGBC).gridy(6).none());

    supportTextField = new JTextField();
    panel.add(supportTextField, new GBC(baseGBC).gridy(7).horizontal());

    managerLabel =
      new JLabel(Language.get(this.getClass(), "manager.label"));
    panel.add(managerLabel, new GBC(baseGBC).gridy(8).none());

    managerTextField = new JTextField();
    panel.add(managerTextField, new GBC(baseGBC).gridy(9).horizontal());

    queryLabel =
      new JLabel(Language.get(this.getClass(), "busquery.label"));
    panel.add(queryLabel, new GBC(baseGBC).gridy(10).none());

    queryTextField = new JTextField();
    panel.add(queryTextField, new GBC(baseGBC).gridy(11).horizontal());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    String name = nameTextField.getText();

    if (name.equals("")) {
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
    this.officeTextField.setText(info.office());
    this.supportTextField.setText(String.join(", ", info.support()));
    this.managerTextField.setText(String.join(", ", info.manager()));
    this.queryTextField.setText(info.busquery());
  }

}
