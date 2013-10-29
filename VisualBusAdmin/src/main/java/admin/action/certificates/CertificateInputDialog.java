package admin.action.certificates;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;

import reuse.modified.planref.client.util.crud.CRUDPanel;
import tecgraf.javautils.LNG;
import tecgraf.javautils.gui.GBC;
import admin.BusAdmin;
import admin.desktop.dialog.BusAdminAbstractInputDialog;
import admin.wrapper.IdentifierWrapper;

/**
 * Classe que dá a especialização necessária ao Diálogo de Cadastro de
 * Certificados
 * 
 * @author Tecgraf
 */
public class CertificateInputDialog extends
  BusAdminAbstractInputDialog<IdentifierWrapper> {
  private JButton certificateButton;
  private JLabel identifierLabel;
  private JLabel certificateLabel;
  private JTextField identifierField;
  private JTextField certificateField;

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   * @param blockType Modo de Bloqueio da janela mãe.
   */
  public CertificateInputDialog(JFrame parentWindow, String title,
    CRUDPanel<IdentifierWrapper> panel, BusAdmin admin) {
    super(parentWindow, title, panel, admin);
  }

  public void chooseCertificateFile() {
    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int status = chooser.showOpenDialog(this);
    if (status == JFileChooser.APPROVE_OPTION) {
	String certificatePath = chooser.getSelectedFile().getAbsolutePath();
	certificateField.setText(certificatePath);
    }
  }

  @Override
  protected boolean accept() {
    if (hasValidFields()) {
      String identifier = getIdentifier();

      setNewRow(new IdentifierWrapper(identifier));
      if (apply()) {
        return true;
      }
    }
    return false;
  }

  private String getIdentifier() {
    return this.identifierField.getText();
  }

  private String getCertificatePath() {
    return this.certificateField.getText();
  }

  @Override
  protected JPanel buildFields() {
    JPanel panel = new JPanel(new GridBagLayout());

    identifierLabel =
      new JLabel(LNG.get("CertificateInputDialog.identifier.label"));
    panel.add(identifierLabel, new GBC(0, 0).west());

    identifierField = new JTextField(30);
    panel.add(identifierField, new GBC(0, 1).gridwidth(2).fillx());

    certificateLabel =
      new JLabel(LNG.get("CertificateInputDialog.certificate.label"));
    panel.add(certificateLabel, new GBC(0, 2).west());

    certificateField = new JTextField(30);
    certificateField.setEditable(false);
    panel.add(certificateField, new GBC(0, 3).west());

    certificateButton =
      new JButton(LNG.get("CertificateInputDialog.certificate.search"));
    certificateButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chooseCertificateFile();
      }
      });
    panel.add(certificateButton, new GBC(1, 3).west());

    return panel;
  }

  @Override
  protected void openBusCall() throws Exception {
    IdentifierWrapper newIdentifierWrapper = getNewRow();
    String identifier = newIdentifierWrapper.getIdentifier();

    File certificateFile = new File(getCertificatePath());
    byte[] certificate = FileUtils.readFileToByteArray(certificateFile);

    admin.registerCertificate(identifier, certificate);
  }
}
