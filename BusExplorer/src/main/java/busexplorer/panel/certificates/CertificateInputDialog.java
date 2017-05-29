package busexplorer.panel.certificates;

import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import tecgraf.openbus.admin.BusAdminFacade;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Dimension;
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

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.BusCore) {

      @Override
      protected void doPerformTask() throws Exception {
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
    setMinimumSize(new Dimension(380,240));
    JPanel panel = new JPanel(new MigLayout("fill, flowy"));

    identifierLabel =
      new JLabel(Language.get(this.getClass(),"identifier.label"));
    panel.add(identifierLabel, "grow");

    identifierField = new JTextField(30);
    identifierField.getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent documentEvent) {
        if (identifierField.getText().trim().isEmpty()) {
          setErrorMessage(Language.get(CertificateInputDialog.class,
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
    panel.add(identifierField,"grow");

    certificateLabel =
      new JLabel(Language.get(this.getClass(),"certificate.label"));
    panel.add(certificateLabel, "grow");

    JPanel certificatePane = new JPanel(new MigLayout("","[grow][]"));

    certificateField = new JTextField(30);
    certificateField.setEditable(false);
    certificatePane.add(certificateField, "growx");

    certificateButton =
      new JButton(Language.get(this.getClass(),"certificate.search"));
    certificateButton.addActionListener(e -> {
      chooseCertificateFile();
      clearErrorMessage();
    });
    certificatePane.add(certificateButton, "dock east");

    panel.add(certificatePane, "grow");

    return panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasValidFields() {
    if (getIdentifier().trim().isEmpty()) {
      setErrorMessage(Language.get(this.getClass(),
        "error.validation.name"));
      return false;
    }

    if (getCertificatePath().isEmpty()) {
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
