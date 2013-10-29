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
 * Classe de métodos úteis para a construção das interfaces do específicas do
 * Planref (com extensões do Logistic)
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
   * Diretório de resources com imagens-cliente
   */
  public static final String IMG_DIR = "/reuse/modified/planref/client/resources/images";

  /**
   * Diretório de resources com as imagens logotipo do programa.
   */
  public static final String IMG_LOGO_DIR = IMG_DIR + "/logos";

  /**
   * Diretório de resources com as imagens de login do programa.
   */
  private static final String IMG_LOGIN_DIR = IMG_DIR + "/login";

  /**
   * Diretório de imagens associadas às ações do programa
   */
  private static final String IMG_ACTION_DIR = IMG_DIR + "/actions";

  /**
   * Diretório de imagens de uso geral do programa
   */
  private static final String IMG_COMMON_DIR = IMG_DIR + "/common";

  /**
   * Criação interna de uma imagem (ícone) comum ao sistema.
   * 
   * @param name o nome da imagem conforme o padrão "NOME.16.gif"
   * @return o ícone
   */
  final static private ImageIcon buildCommonIcon(final String name) {
    final String path = IMG_COMMON_DIR + "/" + name + "16.gif";
    return new ImageIcon(PlanrefUI.class.getResource(path));
  }

  /**
   * Ícone do programa com tamanho 16x16: sincronizar árvore.
   */
  public static final ImageIcon SYNC_ICON_16 = buildCommonIcon("Sync");

  /**
   * Ícone do programa com tamanho 16x16: não sincronizar árvore.
   */
  public static final ImageIcon NOT_SYNC_ICON_16 = buildCommonIcon("NotSync");

  /**
   * Ícone do programa com tamanho 16x16: aplicar operação.
   */
  public static final ImageIcon APPLY_ICON_16 = buildCommonIcon("Apply");

  /**
   * Ícone do programa com tamanho 16x16: confirmar operação.
   */
  public static final ImageIcon CONFIRM_ICON_16 = buildCommonIcon("Confirm");

  /**
   * Ícone do programa com tamanho 16x16: cancelar operação.
   */
  public static final ImageIcon CANCEL_ICON_16 = buildCommonIcon("Cancel");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon ADD_ICON_16 = buildCommonIcon("Add");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon EDIT_ICON_16 = buildCommonIcon("Edit");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon SELECT_ICON_16 = buildCommonIcon("Select");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon DEL_ICON_16 = buildCommonIcon("Delete");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon RIGHT_ICON_16 = buildCommonIcon("Right");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon FIT_ICON_16 = buildCommonIcon("Fit");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon ZOOM_IN_ICON_16 = buildCommonIcon("ZoomIn");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon ZOOM_OUT_ICON_16 = buildCommonIcon("ZoomOut");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon LEFT_ICON_16 = buildCommonIcon("Left");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon PRINT_ICON_16 = buildCommonIcon("Print");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon REFRESH_ICON_16 = buildCommonIcon("Refresh");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon ASSAY_USER_ICON_16 = buildCommonIcon("User");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon ASSAY_OVERWRITTEN_ICON_16 =
    buildCommonIcon("Overwritten");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon ASSAY_AUTO_ICON_16 =
    buildCommonIcon("Automatic");

  /**
   * Ícone do programa com tamanho 16x16.
   */
  public static final ImageIcon FRAME_ICON_16 = new ImageIcon(PlanrefUI.class
    .getResource(IMG_LOGO_DIR + "/Logo16.gif"));

  /**
   * Ícone do programa com tamanho 32x32.
   */
  public static final ImageIcon FRAME_ICON_32 = new ImageIcon(PlanrefUI.class
    .getResource(IMG_LOGO_DIR + "/Logo32.gif"));

  /**
   * Imagem na tela de login do programa.
   */
  public static final ImageIcon LOGIN_SPLASH_IMAGE = new ImageIcon(
    PlanrefUI.class.getResource(IMG_LOGIN_DIR + "/Splash.gif"));

  /**
   * Cor de seleção do item de planta
   */
  public static final Color PLANT_ITEM_SELECTION_COLOR =
    new Color(112, 178, 12);

  /**
   * Cor de seleção da campanha
   */
  public static final Color CAMPAIGN_SELECTION_COLOR = new Color(112, 178, 12);

  /**
   * Texto padronizado: ação de cancelar
   */
  public static final String CANCEL_STRING = getPlanrefString("cancel");

  /**
   * Texto padronizado: ação de aplicar
   */
  public static final String APPLY_STRING = getPlanrefString("apply");

  /**
   * Texto padronizado: ação de fechar
   */
  public static final String CLOSE_STRING = getPlanrefString("close");

  /**
   * Texto padronizado: ação de confirmação (OK).
   */
  public static final String OK_STRING = getPlanrefString("ok");

  /**
   * Método de busca de uma imagem (ícone) de uma ação com base no seu nome
   * 
   * @param tagName a tag da ação.
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
      System.out.println("Imagem de ação não encontrada: " + pth);
      return null;
    }
  }

  /**
   * Consulta do tamanho da tela com um proproção.
   * 
   * @param ratio a proporção desejada (em relação ao tamanho da tela).
   * @return a dimensão em pixels.
   */
  public static Dimension getScreenSize(final double ratio) {
    final Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();
    dm.height = (int) Math.round(dm.height * ratio);
    dm.width = (int) Math.round(dm.width * ratio);
    return dm;
  }

  /**
   * Consulta de uma string geral (de internacionalização) com base em um tag
   * 
   * @param tag o tag
   * @return o texto com base no padrão Planref.tag
   * @see LNG#get(String)
   */
  public static String getPlanrefString(final String tag) {
    return LNG.get("Planref." + tag);
  }

  /**
   * Busca de uma string de internacionalozação com base um um objeto do sistema
   * 
   * @param cls a classe do objeto referência para internacionalização.
   * @param tag o rótulo a ser buscado.
   * @return a string internacionalizada com base no nome simples da classe
   */
  public static String getClassString(final Class<?> cls, final String tag) {
    final String className = cls.getSimpleName();
    return LNG.get(className + "." + tag);
  }

  /**
   * Busca de uma string de internacionalozação com base um um objeto do sistema
   * 
   * @param object o objeto referência para internacionalização.
   * @param tag o rótulo a ser buscado.
   * @return a string internacionalizada com base no nome simples da classe
   */
  public static String getObjectString(final Object object, final String tag) {
    final Class<?> cls = object.getClass();
    return PlanrefUI.getClassString(cls, tag);
  }

  /**
   * Colocação de uma borda de linha em um componente.
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
   * Colocação de uma borda com texto em um componente.
   * 
   * @param component o componente a ser colocado sob a borda.
   * @param title o título a ser colocado.
   */
  public static void setTitledBorder(final JComponent component,
    final String title) {
    component.setBorder(BorderFactory.createCompoundBorder(BorderFactory
      .createEmptyBorder(BRD_GAP, BRD_GAP, BRD_GAP, BRD_GAP), BorderFactory
      .createCompoundBorder(BorderFactory.createTitledBorder(title),
        BorderFactory.createEmptyBorder(BRD_GAP, BRD_GAP, BRD_GAP, BRD_GAP))));
  }

  /**
   * Colocação de uma borda rebaixada em um componente.
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
   * Tratador de exceções (para o usuário) no Planref.
   * 
   * @param t a exceção TODO Colocar um tratador adequado.
   */
  public static void treatException(final Throwable t) {
    throw new RuntimeException(t); // Colocado por enquanto para depuração
  }

  /**
   * Método que exibe e retorna um fileChooser com o look and feel do planref.
   * 
   * @param owner Janela de onde foi disparado o fileChooser.
   * @param title Título da janela de escolha de arquivos.
   * @param fileFilter Filtros para extensões específicas (pode ser nulo ou
   *        vazio).
   * @param selectionMode Modo de seleção dos arquivos (pode ser nulo ou vazio).
   * 
   * @return o caminha do arquivo quando um é selecionado ou nulo caso
   *         contrário.
   */
  public static String createAndShowPlanrefFileChooser(JFrame owner,
    String title, String fileFilter, int selectionMode) {
    if (owner == null) {
      throw new IllegalArgumentException(
        "O ownerFrame do FileChooser não pode ser nulo!");
    }

    if ((title == null) || (title.equals(""))) {
      throw new IllegalArgumentException(
        "O título do FileChooser não pode ser nulo ou vazio!");
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
