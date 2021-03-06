package busexplorer;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;

/**
 * Local para imagens internas.
 *
 * @author Tecgraf
 */
public class ApplicationIcons {

  /**
   * Aplica��o.
   */
  public static final Image[] BUSEXPLORER_LIST =
    new Image[]{
      createImageIcon("BusExplorer16.png").getImage(),
      createImageIcon("BusExplorer32.png").getImage(),
      createImageIcon("BusExplorer48.png").getImage(),
      createImageIcon("BusExplorer96.png").getImage()
    };

  /**
   * Adi��o.
   */
  public static final ImageIcon ICON_ADD_16 = createImageIcon("Add16.png");

  /**
   * Edi��o.
   */
  public static final ImageIcon ICON_EDIT_16 = createImageIcon("Edit16.png");

  /**
   * Remo��o.
   */
  public static final ImageIcon ICON_DELETE_16 =
    createImageIcon("Delete16.gif");

  /**
   * Atualiza��o.
   */
  public static final ImageIcon ICON_REFRESH_16 =
    createImageIcon("Refresh16.png");

  /**
   * Limpar.
   */
  public static final ImageIcon ICON_CLEAR_16 =
    createImageIcon("Clear16.png");

  /**
   * Indicador de mais op��es.
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
   * Valida��o.
   */
  public static final ImageIcon ICON_VALIDATE_16 = createImageIcon("Validate16.png");

  /**
   * Reiniciar.
   */
  public static final ImageIcon ICON_RESTART_16 = createImageIcon("Restart16.png");

  /**
   * Restaurar padr�es.
   */
  public static final ImageIcon ICON_RESTORE_16 = createImageIcon("RestoreDefaults16.png");

  /**
   * Depurar.
   */
  public static final ImageIcon ICON_DEBUG_16 = createImageIcon("Debug16.png");

  /**
   * Propriedades.
   */
  public static final ImageIcon ICON_PROPS_16 = createImageIcon("Props16.png");

  /**
   * Copiar.
   */
  public static final ImageIcon ICON_COPY_16 = createImageIcon("Copy16.png");

  /**
   * Planilha.
   */
  public static final ImageIcon ICON_SPREADSHEET_16 = createImageIcon("Spreadsheet16.png");

  /**
   * Ajuda.
   */
  public static final ImageIcon ICON_HELP_16 = createImageIcon("Help16.png");

  /**
   * Medidor.
   */
  public static final ImageIcon ICON_GAUGE_16 = createImageIcon("Gauge16.png");

  /**
   * Cancelar.
   */
  public static final ImageIcon ICON_CANCEL_16 = createImageIcon("Cancel16.gif");

  /**
   * Pend�ncias.
   */
  public static final ImageIcon ICON_HEALTHY_32 = createImageIcon("Healthy32.png");

  /**
   * Anima��o do carregamento.
   */
  public static final ImageIcon ICON_LOADING_32 = createImageIcon("Loading32.gif");

  /**
   * Caminho para recurso de imagens dentro do classpath
   */
  public static final String IMAGE_RESOURCES_DIR = "/busexplorer/resources/images/";

  /**
   * Montagem da �cone do diret�rio-padr�o.
   *
   * @param imageIconName nome do arquivo de imagem.
   * @return uma imagem (�cone)
   */
  protected static ImageIcon createImageIcon(String imageIconName) {
    URL res = ApplicationIcons.class.getResource(IMAGE_RESOURCES_DIR + imageIconName);
    if (res == null) {
      return null;
    }
    return new ImageIcon(res);
  }
}
