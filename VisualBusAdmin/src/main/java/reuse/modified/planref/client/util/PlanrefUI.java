package reuse.modified.planref.client.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileFilter;

import reuse.modified.logistic.client.util.UI;

import org.jfree.ui.ExtensionFileFilter;

import tecgraf.javautils.LNG;

/**
 * Classe de m�todos �teis para a constru��o das interfaces do espec�ficas do
 * Planref (com extens�es do Logistic)
 * 
 * @see UI
 */
public class PlanrefUI extends UI {

  /**
   * Tamanho das bordas criadas pela classe (em pixels)
   * 
   * @see #setLowBorder(JComponent)
   * @see #setTitledBorder(JComponent, String)
   * @see #setLinedBorder(JComponent)
   */
  final static private int BRD_GAP = 3;

  /**
   * Diret�rio de resources com imagens-cliente
   */
  public static final String IMG_DIR = "/reuse/modified/planref/client/resources/images";

  /**
   * Diret�rio de resources com as imagens logotipo do programa.
   */
  public static final String IMG_LOGO_DIR = IMG_DIR + "/logos";

  /**
   * Diret�rio de resources com as imagens de login do programa.
   */
  private static final String IMG_LOGIN_DIR = IMG_DIR + "/login";

  /**
   * Diret�rio de imagens associadas �s a��es do programa
   */
  private static final String IMG_ACTION_DIR = IMG_DIR + "/actions";

  /**
   * Diret�rio de imagens de uso geral do programa
   */
  private static final String IMG_COMMON_DIR = IMG_DIR + "/common";

  /**
   * Cria��o interna de uma imagem (�cone) comum ao sistema.
   * 
   * @param name o nome da imagem conforme o padr�o "NOME.16.gif"
   * @return o �cone
   */
  final static private ImageIcon buildCommonIcon(final String name) {
    final String path = IMG_COMMON_DIR + "/" + name + "16.gif";
    return new ImageIcon(PlanrefUI.class.getResource(path));
  }

  /**
   * �cone do programa com tamanho 16x16: sincronizar �rvore.
   */
  public static final ImageIcon SYNC_ICON_16 = buildCommonIcon("Sync");

  /**
   * �cone do programa com tamanho 16x16: n�o sincronizar �rvore.
   */
  public static final ImageIcon NOT_SYNC_ICON_16 = buildCommonIcon("NotSync");

  /**
   * �cone do programa com tamanho 16x16: aplicar opera��o.
   */
  public static final ImageIcon APPLY_ICON_16 = buildCommonIcon("Apply");

  /**
   * �cone do programa com tamanho 16x16: confirmar opera��o.
   */
  public static final ImageIcon CONFIRM_ICON_16 = buildCommonIcon("Confirm");

  /**
   * �cone do programa com tamanho 16x16: cancelar opera��o.
   */
  public static final ImageIcon CANCEL_ICON_16 = buildCommonIcon("Cancel");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon ADD_ICON_16 = buildCommonIcon("Add");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon EDIT_ICON_16 = buildCommonIcon("Edit");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon SELECT_ICON_16 = buildCommonIcon("Select");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon DEL_ICON_16 = buildCommonIcon("Delete");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon RIGHT_ICON_16 = buildCommonIcon("Right");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon FIT_ICON_16 = buildCommonIcon("Fit");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon ZOOM_IN_ICON_16 = buildCommonIcon("ZoomIn");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon ZOOM_OUT_ICON_16 = buildCommonIcon("ZoomOut");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon LEFT_ICON_16 = buildCommonIcon("Left");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon PRINT_ICON_16 = buildCommonIcon("Print");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon REFRESH_ICON_16 = buildCommonIcon("Refresh");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon ASSAY_USER_ICON_16 = buildCommonIcon("User");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon ASSAY_OVERWRITTEN_ICON_16 =
    buildCommonIcon("Overwritten");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon ASSAY_AUTO_ICON_16 =
    buildCommonIcon("Automatic");

  /**
   * �cone do programa com tamanho 16x16.
   */
  public static final ImageIcon FRAME_ICON_16 = new ImageIcon(PlanrefUI.class
    .getResource(IMG_LOGO_DIR + "/Logo16.gif"));

  /**
   * �cone do programa com tamanho 32x32.
   */
  public static final ImageIcon FRAME_ICON_32 = new ImageIcon(PlanrefUI.class
    .getResource(IMG_LOGO_DIR + "/Logo32.gif"));

  /**
   * Imagem na tela de login do programa.
   */
  public static final ImageIcon LOGIN_SPLASH_IMAGE = new ImageIcon(
    PlanrefUI.class.getResource(IMG_LOGIN_DIR + "/Splash.gif"));

  /**
   * Cor de sele��o do item de planta
   */
  public static final Color PLANT_ITEM_SELECTION_COLOR =
    new Color(112, 178, 12);

  /**
   * Cor de sele��o da campanha
   */
  public static final Color CAMPAIGN_SELECTION_COLOR = new Color(112, 178, 12);

  /**
   * Texto padronizado: a��o de cancelar
   */
  public static final String CANCEL_STRING = getPlanrefString("cancel");

  /**
   * Texto padronizado: a��o de aplicar
   */
  public static final String APPLY_STRING = getPlanrefString("apply");

  /**
   * Texto padronizado: a��o de fechar
   */
  public static final String CLOSE_STRING = getPlanrefString("close");

  /**
   * Texto padronizado: a��o de confirma��o (OK).
   */
  public static final String OK_STRING = getPlanrefString("ok");

  /**
   * M�todo de busca de uma imagem (�cone) de uma a��o com base no seu nome
   * 
   * @param tagName a tag da a��o.
   * @return a imagem
   * @see #IMG_ACTION_DIR
   */
  public static ImageIcon getActionImage(final String tagName) {
    final String name = tagName + ".16.gif";
    final String pth = IMG_ACTION_DIR + "/" + name;
    try {
      return new ImageIcon(PlanrefUI.class.getResource(pth));
    }
    catch (RuntimeException e) {
      System.out.println("Imagem de a��o n�o encontrada: " + pth);
      return null;
    }
  }

  /**
   * Consulta do tamanho da tela com um propro��o.
   * 
   * @param ratio a propor��o desejada (em rela��o ao tamanho da tela).
   * @return a dimens�o em pixels.
   */
  public static Dimension getScreenSize(final double ratio) {
    final Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
    dm.height = (int) Math.round(dm.height * ratio);
    dm.width = (int) Math.round(dm.width * ratio);
    return dm;
  }

  /**
   * Consulta de uma string geral (de internacionaliza��o) com base em um tag
   * 
   * @param tag o tag
   * @return o texto com base no padr�o Planref.tag
   * @see LNG#get(String)
   */
  public static String getPlanrefString(final String tag) {
    return LNG.get("Planref." + tag);
  }

  /**
   * Busca de uma string de internacionaloza��o com base um um objeto do sistema
   * 
   * @param cls a classe do objeto refer�ncia para internacionaliza��o.
   * @param tag o r�tulo a ser buscado.
   * @return a string internacionalizada com base no nome simples da classe
   */
  public static String getClassString(final Class<?> cls, final String tag) {
    final String className = cls.getSimpleName();
    return LNG.get(className + "." + tag);
  }

  /**
   * Busca de uma string de internacionaloza��o com base um um objeto do sistema
   * 
   * @param object o objeto refer�ncia para internacionaliza��o.
   * @param tag o r�tulo a ser buscado.
   * @return a string internacionalizada com base no nome simples da classe
   */
  public static String getObjectString(final Object object, final String tag) {
    final Class<?> cls = object.getClass();
    return PlanrefUI.getClassString(cls, tag);
  }

  /**
   * Coloca��o de uma borda de linha em um componente.
   * 
   * @param component o componente a ser colocado sob a borda.
   */
  public static void setLinedBorder(final JComponent component) {
    final Color clr = Color.lightGray;
    component.setBorder(BorderFactory.createCompoundBorder(BorderFactory
      .createEmptyBorder(BRD_GAP, BRD_GAP, BRD_GAP, BRD_GAP), BorderFactory
      .createCompoundBorder(BorderFactory.createLineBorder(clr), BorderFactory
        .createEmptyBorder(BRD_GAP, BRD_GAP, BRD_GAP, BRD_GAP))));
  }

  /**
   * Coloca��o de uma borda com texto em um componente.
   * 
   * @param component o componente a ser colocado sob a borda.
   * @param title o t�tulo a ser colocado.
   */
  public static void setTitledBorder(final JComponent component,
    final String title) {
    component.setBorder(BorderFactory.createCompoundBorder(BorderFactory
      .createEmptyBorder(BRD_GAP, BRD_GAP, BRD_GAP, BRD_GAP), BorderFactory
      .createCompoundBorder(BorderFactory.createTitledBorder(title),
        BorderFactory.createEmptyBorder(BRD_GAP, BRD_GAP, BRD_GAP, BRD_GAP))));
  }

  /**
   * Coloca��o de uma borda rebaixada em um componente.
   * 
   * @param component o componente a ser colocado sob a borda.
   */
  public static void setLowBorder(final JComponent component) {
    final CompoundBorder border =
      BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(
        BRD_GAP, BRD_GAP, BRD_GAP, BRD_GAP), BorderFactory
        .createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
          BorderFactory.createEmptyBorder(BRD_GAP, BRD_GAP, BRD_GAP, BRD_GAP)));
    component.setBorder(border);
  }

  /**
   * Tratador de exce��es (para o usu�rio) no Planref.
   * 
   * @param t a exce��o TODO Colocar um tratador adequado.
   */
  public static void treatException(final Throwable t) {
    throw new RuntimeException(t); // Colocado por enquanto para depura��o
  }

  /**
   * M�todo que exibe e retorna um fileChooser com o look and feel do planref.
   * 
   * @param owner Janela de onde foi disparado o fileChooser.
   * @param title T�tulo da janela de escolha de arquivos.
   * @param fileFilter Filtros para extens�es espec�ficas (pode ser nulo ou
   *        vazio).
   * @param selectionMode Modo de sele��o dos arquivos (pode ser nulo ou vazio).
   * 
   * @return o caminha do arquivo quando um � selecionado ou nulo caso
   *         contr�rio.
   */
  public static String createAndShowPlanrefFileChooser(JFrame owner,
    String title, String fileFilter, int selectionMode) {
    if (owner == null) {
      throw new IllegalArgumentException(
        "O ownerFrame do FileChooser n�o pode ser nulo!");
    }

    if ((title == null) || (title.equals(""))) {
      throw new IllegalArgumentException(
        "O t�tulo do FileChooser n�o pode ser nulo ou vazio!");
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(selectionMode);

    if ((fileFilter != null) && (!(fileFilter.equals("")))) {
      FileFilter filter = new ExtensionFileFilter(fileFilter, fileFilter);
      fileChooser.setFileFilter(filter);
      fileChooser.setAcceptAllFileFilterUsed(false);
    }

    int option = fileChooser.showDialog(owner, title);
    if (option == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile().getAbsolutePath();
    }
    else {
      return null;
    }
  }
}
