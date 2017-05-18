package busexplorer.panel.certificates;

import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import org.apache.commons.io.FileUtils;
import tecgraf.javautils.gui.GBC;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.io.File;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Certificados
 * 
 * @author Tecgraf
 */
public class CertificateInputDialog extends BusExplorerAbstractInputDialog {
  private JLabel identifierLabel;
  private JTextField identifierField;
  private JLabel certificateLabel;
  private JTextField certificateField;
  private JButton certificateButton;

  private TablePanelComponent<CertificateWrapper> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo.
   * @param panel Painel a ser atualizado após a adição/edição.
   * @param admin Acesso às funcionalidade de administração do barramento.
   */
  public CertificateInputDialog(Window parentWindow,
                                TablePanelComponent<CertificateWrapper> panel, BusAdminFacade admin) {
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
        File certificateFile = new File(getCertificatePath());
        byte[] certificate = FileUtils.readFileToByteArray(certificateFile);
        admin.registerCertificate(getIdentifier(), certificate);
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          panel.refresh(null);
          panel.selectElement(new CertificateWrapper(getIdentifier()), true);
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

    identifierLabel =
      new JLabel(Language.get(this.getClass(),"identifier.label"));
    panel.add(identifierLabel, new GBC(baseGBC).gridy(0).none());

    identifierField = new JTextField(30);
    panel.add(identifierField, new GBC(baseGBC).gridy(1).horizontal());

    certificateLabel =
      new JLabel(Language.get(this.getClass(),"certificate.label"));
    panel.add(certificateLabel, new GBC(baseGBC).gridy(2).none());

    JPanel certificatePane = new JPanel(new GridBagLayout());

    certificateField = new JTextField();
    certificateField.setEditable(false);
    certificatePane.add(certificateField, new GBC(0, 0).right(5).horizontal());

    certificateButton =
      new JButton(Language.get(this.getClass(),"certificate.search"));
    certificateButton.addActionListener(e -> chooseCertificateFile());
    certificatePane.add(certificateButton, new GBC(1, 0).east());

    panel.add(certificatePane, new GBC(baseGBC).gridy(3).horizontal());

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (getIdentifier().equals("")) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.emptyID"));
      return false;
    }

    if (getCertificatePath().equals("")) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.emptyPath"));
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
  public void setEditionMode(CertificateWrapper info) {
    this.identifierField.setText(info.getEntity());
    this.identifierField.setEnabled(false);
  }

  /**
   * Seleciona o certificado no sistema de arquivos e atualiza o campo que
   * indica seu caminho.
   */
  public void chooseCertificateFile() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int status = chooser.showOpenDialog(this);
    if (status == JFileChooser.APPROVE_OPTION) {
      String certificatePath = chooser.getSelectedFile().getAbsolutePath();
      certificateField.setText(certificatePath);
    }
  }

  /**
   * Obtém o identificador da entidade que terá o certificado atualizado.
   *
   * @return o identificador da entidade que terá o certificado atualizado.
   */
  private String getIdentifier() {
    return this.identifierField.getText();
  }

  /**
   * Obtém o caminho para o certificado que será cadastrado.
   *
   * @return o caminho para o certificado que será cadastrado.
   */
  private String getCertificatePath() {
    return this.certificateField.getText();
  }
}
