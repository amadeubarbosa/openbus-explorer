package busexplorer.panel.certificates;

import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;

import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import admin.BusAdmin;
import busexplorer.Application;
import busexplorer.desktop.dialog.BusExplorerAbstractInputDialog;
import busexplorer.panel.PanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Utils;
import exception.handling.ExceptionContext;

/**
 * Classe que d� a especializa��o necess�ria ao Di�logo de Cadastro de
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

  private PanelComponent<CertificateWrapper> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela m�e do Di�logo.
   * @param panel Painel a ser atualizado ap�s a adi��o/edi��o.
   * @param admin Acesso �s funcionalidade de administra��o do barramento.
   */
  public CertificateInputDialog(Window parentWindow,
    PanelComponent<CertificateWrapper> panel, BusAdmin admin) {
    super(parentWindow, LNG.get(CertificateInputDialog.class.getSimpleName() +
      ".title"), admin);
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
        }
      }
    };

    task.execute(this, Utils.getString(this.getClass(), "waiting.title"),
      Utils.getString(this.getClass(), "waiting.msg"));
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
      new JLabel(LNG.get("CertificateInputDialog.identifier.label"));
    panel.add(identifierLabel, new GBC(baseGBC).gridy(0).none());

    identifierField = new JTextField(30);
    panel.add(identifierField, new GBC(baseGBC).gridy(1).horizontal());

    certificateLabel =
      new JLabel(LNG.get("CertificateInputDialog.certificate.label"));
    panel.add(certificateLabel, new GBC(baseGBC).gridy(2).none());

    JPanel certificatePane = new JPanel(new GridBagLayout());

    certificateField = new JTextField();
    certificateField.setEditable(false);
    certificatePane.add(certificateField, new GBC(0, 0).right(5).horizontal());

    certificateButton =
      new JButton(LNG.get("CertificateInputDialog.certificate.search"));
    certificateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        chooseCertificateFile();
      }
    });
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
      setErrorMessage(Utils.getString(this.getClass(),
        "error.validation.emptyID"));
      return false;
    }

    if (getCertificatePath().equals("")) {
      setErrorMessage(Utils.getString(this.getClass(),
        "error.validation.emptyPath"));
      return false;
    }

    clearErrorMessage();
    return true;
  }

  /**
   * Configura o di�logo para trabalhar em modo de edi��o.
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
   * Obt�m o identificador da entidade que ter� o certificado atualizado.
   *
   * @return o identificador da entidade que ter� o certificado atualizado.
   */
  private String getIdentifier() {
    return this.identifierField.getText();
  }

  /**
   * Obt�m o caminho para o certificado que ser� cadastrado.
   *
   * @return o caminho para o certificado que ser� cadastrado.
   */
  private String getCertificatePath() {
    return this.certificateField.getText();
  }
}
