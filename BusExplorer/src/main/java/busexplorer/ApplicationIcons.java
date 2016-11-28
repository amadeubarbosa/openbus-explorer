package busexplorer;

import javax.swing.ImageIcon;
import java.net.URL;

/**
 * Local para imagens internas.
 *
 * @author Tecgraf
 */
public class ApplicationIcons {

  /**
   * Adição.
   */
  public static final ImageIcon ICON_ADD_16 = createImageIcon("Add16.gif");

  /**
   * Edição.
   */
  public static final ImageIcon ICON_EDIT_16 = createImageIcon("Edit16.gif");

  /**
   * Remoção.
   */
  public static final ImageIcon ICON_DELETE_16 =
    createImageIcon("Delete16.gif");

  /**
   * Atualização.
   */
  public static final ImageIcon ICON_REFRESH_16 =
    createImageIcon("Refresh16.gif");

  /**
   * Limpar.
   */
  public static final ImageIcon ICON_CLEAR_16 =
    createImageIcon("Clear16.gif");

  /**
   * Indicador de mais opções.
   */
  public static final ImageIcon ICON_DOWN_4 = createImageIcon("Down4.gif");

  /**
   * Conectar.
   */
  public static final ImageIcon ICON_LOGIN_16 = createImageIcon("Login16.png");

  /**
   * Desconectar.
   */
  public static final ImageIcon ICON_LOGOUT_16 = createImageIcon("Logout16.png");

  /**
   * Validação.
   */
  public static final ImageIcon ICON_VALIDATE_16 = createImageIcon("Validate16.gif");

  /**
   * Reiniciar.
   */
  public static final ImageIcon ICON_RESTART_16 = createImageIcon("Restart16.png");

  /**
   * Propriedades.
   */
  public static final ImageIcon ICON_PROPS_16 = createImageIcon("Props16.png");

  /**
   * Cancelar.
   */
  public static final ImageIcon ICON_CANCEL_16 = createImageIcon("Cancel16.gif");

  /**
   * Caminho para recurso de imagens dentro do classpath
   */
  public static final String IMAGE_RESOURCES_DIR = "/busexplorer/resources/images/";

  /**
   * Montagem da ícone do diretório-padrão.
   *
   * @param imageIconName nome do arquivo de imagem.
   * @return uma imagem (ícone)
   */
  protected static ImageIcon createImageIcon(String imageIconName) {
    URL res = ApplicationIcons.class.getResource(IMAGE_RESOURCES_DIR + imageIconName);
    if (res == null) {
      return null;
    }
    return new ImageIcon(res);
  }
}
