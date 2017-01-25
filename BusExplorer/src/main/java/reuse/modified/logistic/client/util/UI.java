package reuse.modified.logistic.client.util;

import tecgraf.javautils.gui.GBC;
import tecgraf.javautils.gui.print.PrintableUI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * A classe UI possui m�todos �teis para a constru��o das interfaces do Bandeira
 * Brasil e do Alope.
 */
public class UI extends PrintableUI {
  /** Diret�rio de resources com imagens. */
  public static final String ICON_DIRECTORY =
    "/reuse/modified/logistic/client/resources/images";

  /**
   * �cones
   */

  /** �cone de Collapsed */
  public static final ImageIcon COLLAPSED_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/collapsed.gif"));
  /** �cone de Expanded */
  public static final ImageIcon EXPANDED_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/expanded.gif"));
  /** �cone de um header que est� colapsado */
  public static final ImageIcon COLLAPSED_HEADER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/collapsedHeader.gif"));
  /** �cone de header que est� expandido */
  public static final ImageIcon EXPANDED_HEADER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/expandedHeader.gif"));
  /** �cone de Warning */
  public static final ImageIcon WARNING_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/warning.png"));
  /** �cone de limpeza em bot�o */
  public static final ImageIcon BUTTON_CLEAR_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/buttonClear.gif"));
  /** �cone de composi��o de mail/notifica��o em bot�o (padr�o adaptado) */
  public static final ImageIcon BUTTON_MAIL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/buttonMail.gif"));
  /** �cone de seta para baixo */
  public static final ImageIcon BUTTON_DOWN_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/buttonDown.gif"));
  /** �cone de seta para cima */
  public static final ImageIcon BUTTON_UP_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/buttonUp.gif"));
  /** �cone de indica��o que a c�lula pode ser editada */
  public static final ImageIcon EDITABLE_BUTTON_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/editableButton.gif"));
  /** �cone de indica��o que a c�lula pode ser editada escolhendo valores */
  public static final ImageIcon LIST_EDITABLE_BUTTON_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/listEditableButton.gif"));
  /** �cone de indica��o que a c�lula tem erro */
  public static final ImageIcon ERROR_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/error.png"));
  /** �cone em branco para fazer alinhamento com os �cones de edi��o */
  public static final ImageIcon BLANK_BUTTON_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/blankButton.gif"));
  /** �cone de indica��o que a c�lula n�o pode ser edit�da */
  public static final ImageIcon NOT_EDITABLE_BUTTON_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/notEditableButton.gif"));
  /** �cone de indica��o de verifica��o na c�lula (como um checkbox) */
  public static final ImageIcon TICK_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/tick.gif"));
  /** �cone de indica��o de n�o verifica��o na c�lula */
  public static final ImageIcon NOT_TICK_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/notTick.gif"));
  /** �cone com a linha que comp�e a janela de Sobre */
  public static final ImageIcon ABOUT_LINE_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/aboutLine.gif"));

  /** �cone de cen�rio base */
  public static final ImageIcon BASE_SCENA_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/baseScena.gif"));
  /** �cone de cen�rio vigente */
  public static final ImageIcon CURRENT_APPROVAL_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/currentApprovalScena.gif"));
  /** �cone de cen�rio aprovado */
  public static final ImageIcon APPROVAL_SCENA_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/approvalScena.gif"));
  /** �cone de cen�rio edit�vel */
  public static final ImageIcon EDITABLE_SCENA_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/editableScena.gif"));
  /** �cone de cen�rio edit�vel e bloqueado */
  public static final ImageIcon EDITABLE_BLOCKED_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/editableBlockedScena.gif"));
  /** �cone de cen�rio n�o edit�vel */
  public static final ImageIcon NOT_EDITABLE_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/notEditableScena.gif"));
  /** �cone de cen�rio sendo otimizado */
  public static final ImageIcon SOLVING_SCENA_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/solvingScena.gif"));
  /** �cone de cen�rio edit�vel que teve erro na otimiza��o */
  public static final ImageIcon EDITABLE_ERROR_SOLVING_SCENA_ICON =
    new ImageIcon(UI.class.getResource(ICON_DIRECTORY
      + "/editableErrorSolvingScena.gif"));
  /** �cone de cen�rio edit�vel bloqueado que teve erro na otimiza��o */
  public static final ImageIcon EDITABLE_BLOCKED_ERROR_SOLVING_SCENA_ICON =
    new ImageIcon(UI.class.getResource(ICON_DIRECTORY
      + "/editableBlockedErrorSolvingScena.gif"));
  /** �cone de cen�rio n�o edit�vel que teve erro na otimiza��o */
  public static final ImageIcon NOT_EDITABLE_ERROR_SOLVING_SCENA_ICON =
    new ImageIcon(UI.class.getResource(ICON_DIRECTORY
      + "/notEditableErrorSolvingScena.gif"));
  /** �cone de cen�rio de otimiza��o edit�vel */
  public static final ImageIcon SOLVED_EDITABLE_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/editableSolvedScena.gif"));
  /** �cone de cen�rio de otimiza��o edit�vel e bloqueado */
  public static final ImageIcon SOLVED_EDITABLE_BLOCKED_SCENA_ICON =
    new ImageIcon(UI.class.getResource(ICON_DIRECTORY
      + "/editableBlockedSolvedScena.gif"));
  /** �cone de cen�rio de otimiza��o n�o edit�vel */
  public static final ImageIcon SOLVED_NOT_EDITABLE_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/notEditableSolvedScena.gif"));
  /** �cone de pasta com permiss�o de aprova��o */
  public static final ImageIcon APPROVABLE_FOLDER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/approvableFolder.gif"));
  /** �cone de pasta de permiss�o apenas para leitura */
  public static final ImageIcon READ_ONLY_APPROVE_FOLDER_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/readOnlyApproveFolder.gif"));
  /** �cone de pasta com permiss�o para escrita */
  public static final ImageIcon WRITEABLE_FOLDER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/writeableFolder.gif"));
  /** �cone de pasta com permiss�o apenas para leitura */
  public static final ImageIcon READ_ONLY_FOLDER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/readOnlyFolder.gif"));
  /** �cone do logo da Petrobras para o relat�rio */
  public static final ImageIcon REPORT_BR_LOGO = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/reportBrLogo.png"));
  /** �cone com o s�mbolo de sucesso na valida��o de uma regra */
  public static final ImageIcon SUCCESS_VALIDATION_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/Success.gif"));
  /** �cone com o s�mbolo de falha na valida��o de uma regra */
  public static final ImageIcon UNSUCCESS_VALIDATION_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/SevereFail.gif"));
  /** �cone com o s�mbolo de warning na valida��o de uma regra */
  public static final ImageIcon WARNING_VALIDATION_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/WarningFail.gif"));
  /** �cone que representa um produto */
  public static final ImageIcon PRODUCT_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/product_icon.png"));
  /** �cone que representa um produto desativado */
  public static final ImageIcon UNACTIVE_PRODUCT_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/unactive_product_icon.gif"));
  /** �cone que representa um grupamento de produtos */
  public static final ImageIcon PRODUCT_SET_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/productset_icon.png"));
  /** �cone que representa uma regi�o */
  public static final ImageIcon POINT_SET_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/pointset_icon.png"));
  /** �cone que indica a remo��o de uma linha de uma tabela */
  public static final ImageIcon REMOVE_ROW_ICON = NOT_TICK_ICON;
  /** �cone que representa uma mistura simples */
  public static final ImageIcon SINGLE_MIXTURE_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/single_mixture.gif"));
  /** �cone que representa um grupamento de misturas */
  public static final ImageIcon MIXTURE_SET_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/mixture_set.gif"));
  /** �cone que representa um local de escoamento */
  public static final ImageIcon FLOW_POINT_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/Flow.gif"));

  /** Icone de legenda dos tipos de ponto e regioes */
  public static final ImageIcon POINT_LEGEND_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/legenda.png"));

  /** Icone de legenda dos tipos de ponto e regioes em Mouse Over */
  public static final ImageIcon POINT_LEGEND_ICON_OVER = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/legenda.png"));

  /** Imagem de legenda dos tipos de pontos */
  public static ImageIcon POINT_TYPE_LEGEND = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/legenda_tipos_ponto.gif"));

  /** Icone de legenda dos produtos e grupamentos */
  public static final ImageIcon PRODUCT_LEGEND_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/legenda.png"));

  /** Icone de legenda dos produtos e grupamentos em Mouse Over */
  public static final ImageIcon PRODUCT_LEGEND_ICON_OVER = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/legenda.png"));

  /** Imagem de legenda dos produtos e grupamentos */
  public static ImageIcon PRODUCT_TYPE_LEGEND = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/legenda_tipos_produto.gif"));

  /** �cone que representa um duto */
  public static final ImageIcon PIPELINE_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/pipelineModal.gif"));
  /** �cone que representa um navio */
  public static final ImageIcon SHIP_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/shipModal.gif"));
  /** �cone que representa uma rodovia */
  public static final ImageIcon HIGHWAY_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/highwayModal.gif"));
  /** �cone que representa uma ferrovia */
  public static final ImageIcon RAILROAD_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/railroadModal.gif"));
  /** �cone que representa um granel */
  public static final ImageIcon GRANEL_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/granelModal.gif"));
  /** �cone que representa uma barca�a */
  public static final ImageIcon BARGE_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/bargeModal.gif"));

  /** �cone usado para sele��o de todos os n�s do CheckBoxTree */
  public static final ImageIcon CHECK_BOX_TREE_SELECT_ALL = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/select-all.png"));
  /** �cone usado para remover a sele��o de todos os n�s do CheckBoxTree */
  public static final ImageIcon CHECK_BOX_TREE_UNSELECT_ALL = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/unselect-all.png"));

  /** Icone de bola verde pequena */
  public static final ImageIcon SMALL_GREEN_BALL_IMAGE = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/small_green_ball.gif"));

  /** Icone de bola vermelha pequena */
  public static final ImageIcon SMALL_RED_BALL_IMAGE = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/small_red_ball.gif"));

  /** Icone usado no terceiro estado do <code>TriStateCheckBox</code> */
  public static final ImageIcon THIRD_STATE_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/third_state.png"));

  /** Icone de lata de lixo usado em situa��es de remo��o */
  public static final ImageIcon TRASH_BIN_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/trash.gif"));

  /**
   * Cores
   */

  /** Cor dos grupos de cen�rios */
  public static final Color FOLDER_COLOR = new Color(70, 70, 70);
  /** Cor dos cen�rios */
  public static final Color SCENA_COLOR = new Color(70, 70, 70);
  /** Cor dos cen�rios bloqueados */
  public static final Color BLOCKED_SCENA_COLOR = Color.blue;
  /** Cor de sele��o do cen�rio */
  public static final Color SCENA_SELECTION_COLOR = new Color(112, 178, 12);
  /** Cor do cen�rio vigente */
  public static final Color CURRENT_SCENA_COLOR = new Color(0, 0, 0);
  /** Cor do fundo da c�lula modificada (Azul claro) */
  public static final Color CHANGED_BACKGROUND_COLOR = new Color(224, 241, 255);
  /** Cor do fundo da c�lula editada (Amarelo claro) */
  public static final Color EDITED_BACKGROUND_COLOR = new Color(255, 255, 183);
  /** Cor do fundo da c�lula aprovada (Verde claro) */
  public static final Color PRESAVED_BACKGROUND_COLOR =
    new Color(200, 255, 200);
  /** Cor do fundo da c�lula edit�vel */
  public static final Color EDITABLE_BACKGROUND = Color.white;
  /** Cor do fundo da c�lula n�o edit�vel */
  public static final Color NOT_EDITABLE_BACKGROUND = Color.white;
  /** Cor do fonte da c�lula com valor positivo edit�vel */
  public static final Color POSITIVE_EDIT_COLOR = new Color(0, 0, 0);
  /** Cor do fonte da c�lula com valor positivo n�o edit�vel */
  public static final Color POSITIVE_NOT_EDIT_COLOR = new Color(140, 140, 140);
  /** Cor do fonte da c�lula com valor negativo edit�vel */
  public static final Color NEGATIVE_EDIT_COLOR = new Color(204, 0, 0);
  /** Cor do fonte da c�lula com valor negativo n�o edit�vel */
  public static final Color NEGATIVE_NOT_EDIT_COLOR = new Color(230, 138, 138);
  /** Cor da c�lula selcionada */
  public static final Color CELL_SELECTION_COLOR = new Color(199, 199, 226);
  /** Cor da fonte da janela de informa��es do sistema */
  public static final Color ABOUT_FONT_COLOR = new Color(67, 87, 114);
  /** Cor do glasspane (transl�cido) usado nas janelas bloqueadas */
  public static final Color GLASS_PANE_COLOR = new Color(235, 235, 235, 150);
  /** Cor do fundo do gr�fico */
  public static Color CHART_BACKGROUND = new JPanel().getBackground();
  /** Cor do fundo da �rea de plotagem do gr�fico */
  public static Color CHART_PLOT_BACKGROUND = Color.white;
  /** Cor das linhas do grid da �rea de plotagem do gr�fico */
  public static Color CHART_GRID_COLOR = Color.lightGray;
  /** Cor das linhas que marca o Zero do gr�fico */
  public static Color CHART_ZERO_MARKER = Color.darkGray;
  /** Cor das linhas que marca o fim do m�s no gr�fico */
  public static Color CHART_MONTH_MARKER = Color.gray;
  /** A cor para indicar que o campo n�o foi preenchido corretamente */
  public static final Color FIELD_ERROR_COLOR = new Color(255, 0, 0);
  /** A cor da borda do do item selecionado */
  public static final Color TREE_SELECTION_BORDER_BACKGROUND = UIManager
    .getColor("Tree.selectionBorderColor");

  /**
   * Cursor
   */

  /** Cursor padr�o */
  public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

  /** Cursor redimension�vel para direita e esquerda */
  public static final Cursor RESIZE_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);

  /** ************************ FONTES ****************************** */

  /** Fonte para impress�o dos relat�rios. */
  public static final Font REPORT_TITLE_FONT = new Font("SansSerif", Font.BOLD,
    10);

  /** Fonte do t�tulo da propriedade na tela de propriedades do cen�rio */
  public static final Font HEADER_FONT = new Font("Verdana", Font.BOLD, 11);

  /** Fonte da propriedade na tela de propriedades do cen�rio */
  public static final Font INFO_FONT = new Font("Verdana", Font.PLAIN, 11);

  /** Fonte da mensagem de notifica��o */
  public static final Font NOTIFICATION_FONT = INFO_FONT;

  /** Fonte utilizada no gr�fico */
  public static Font CHART_FONT = new Font("SansSerif", Font.PLAIN, 9);

  /** Fonte pequena utilizada no gr�fico */
  public static Font SMALL_CHART_FONT = new Font("SansSerif", Font.PLAIN, 8);

  /** Fonte utilizada no subt�tulo do gr�fico */
  public static Font SUBTITLE_CHART_FONT = new Font("Verdana", Font.PLAIN, 10);

  /** Fonte utilizada no t�tulo do gr�fico */
  public static Font TITLE_CHART_FONT = new Font("Verdana", Font.BOLD, 11);

  /** Fonte usada nas tabelas */
  public static final Font BOLD_TABLE_FONT = new Font("Verdana", Font.BOLD, 11);

  /** Fonte para uso em TextAreas e Labels */
  public static final Font DESCRIPTION_FONT =
    new Font("Tahoma", Font.PLAIN, 11);

  /** ************************ TABELAS **************************** * */

  /** Altura adicionada �s linhas das tabelas */
  public static final int TABLE_ROW_GAP = 4;

  /** Espa�o entre os �cones e o fonte nas tabelas */
  public static final int TABLE_ICON_TEXT_GAP =
    new JLabel().getIconTextGap() + 2;

  /**
   * Insere os bot�es passados como par�metro em um painel com BoxLayout e
   * iguala o tamanho dos bot�es.
   * 
   * @param buttons array com os bot�es
   * 
   * @return o painel criado.
   */
  public static JPanel buildButtonPanel(JButton... buttons) {
    equalizeButtonSizes(buttons);
    JPanel buttonPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    int x = 0;
    c.gridx = x++;
    c.gridy = 0;
    c.anchor = GridBagConstraints.EAST;
    c.weightx = 1;
    c.weighty = 0;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.insets = new Insets(0, 0, 0, 0);
    buttonPanel.add(new JLabel(), c);

    for (JButton button : buttons) {
      c.gridx = x++;
      c.weightx = 0;
      c.weighty = 0;
      c.fill = GridBagConstraints.NONE;
      c.insets = new Insets(0, 0, 5, 5);
      buttonPanel.add(button, c);
    }
    return buttonPanel;
  }

  /**
   * Atribui um tamanho espec�fico para a largura de determinada coluna do
   * GridBagLayout.
   * 
   * @param gbl o grid bag layout
   * @param col a coluna
   * @param newColWidth o tamanho da coluna
   */
  public static void setColumnMinWidth(GridBagLayout gbl, int col,
    int newColWidth) {
    int[] colWidths = gbl.columnWidths;
    if (colWidths == null) {
      colWidths = new int[col + 1];
    }
    else if (colWidths.length < (col + 1)) {
      colWidths = new int[col + 1];
      System.arraycopy(gbl.columnWidths, 0, colWidths, 0,
        gbl.columnWidths.length);
    }
    colWidths[col] = newColWidth;
    gbl.columnWidths = colWidths;
  }

  /**
   * Iguala o tamanho dos bot�es contidos no array passado como par�metro.
   * 
   * @param buttons o array com os bot�es
   */
  public static void equalizeButtonSizes(JButton[] buttons) {
    Dimension maxSize = new Dimension(0, 0);
    int i;

    // Encontra maior dimens�o
    for (i = 0; i < buttons.length; ++i) {
      maxSize.width =
        Math.max(maxSize.width, buttons[i].getPreferredSize().width);
      maxSize.height =
        Math.max(maxSize.height, buttons[i].getPreferredSize().height);
    }

    // Atribui novos valores para "preferred" e "maximum size", uma vez que
    // BoxLayout leva ambos os valores em considera��o. */
    for (i = 0; i < buttons.length; ++i) {
      buttons[i].setPreferredSize((Dimension) maxSize.clone());
      buttons[i].setMaximumSize((Dimension) maxSize.clone());
      buttons[i].setMinimumSize((Dimension) maxSize.clone());
    }
  }

  /**
   * Cria um painel "b�sico" diagramado por um <code>GridBagLayout</code>. Este
   * tipo de painel � utilizado, tipicamente, em intera��es que configuram um
   * conjunto de par�metros textuais ou num�ricos, e � composto por um n�mero
   * vari�vel de linhas de componentes com as seguintes caracter�sticas:
   * 
   * <ul>
   * <li>o primeiro compontente de cada linha � normalmente um label</li>
   * <li>o segundo componente � um campo de texto ou uma combo-box</li>
   * <li>o terceiro componente � um label ou um bot�o</li>
   * </ul>
   * 
   * 
   * @param rows "array" contendo os componentes a serem inseridos no painel.
   *        Cada elemento deste array cont�m os componentes da linha
   *        correspondente.
   * 
   * @return o painel criado
   */
  public static JPanel createBasicGridPanel(JComponent[][] rows) {
    JPanel basicGridPanel = new JPanel();
    populateBasicGridPanel(basicGridPanel, rows);
    return basicGridPanel;
  }

  /**
   * Cria o painel.
   * 
   * @param basicGridPanel painel a ter os componentes inseridos.
   * @param rows "array" contendo os componentes a serem inseridos no painel.
   *        Cada elemento deste array cont�m os componentes da linha
   *        correspondente.
   */
  public static void populateBasicGridPanel(JPanel basicGridPanel,
    JComponent[][] rows) {
    basicGridPanel.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridheight = 1;
    constraints.insets = new Insets(2, 2, 2, 5);
    constraints.anchor = GridBagConstraints.WEST;
    for (int rowNumber = 0; rowNumber < rows.length; rowNumber++) {
      JComponent[] row = rows[rowNumber];
      if ((row == null) || (row.length == 0)) {
        continue;
      }
      JComponent firstCell = row[0];
      JComponent middleCell = (row.length > 1) ? row[1] : null;
      JComponent lastCell = (row.length > 2) ? row[2] : null;
      constraints.gridy = rowNumber; // linha do painel
      constraints.gridwidth = 1;
      constraints.weightx = 0;
      constraints.weighty = 0;
      constraints.fill = GridBagConstraints.NONE;
      if (firstCell != null) {
        constraints.gridx = 0;
        basicGridPanel.add(firstCell, constraints);
      }
      if (lastCell != null) {
        constraints.gridx = 2;
        basicGridPanel.add(lastCell, constraints);
      }

      // O componente do meio � expandido horizontalmente
      if (middleCell != null) {
        constraints.weightx = 100;
        constraints.weighty = 100;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        if (lastCell == null) {
          constraints.gridwidth = 2;
        }
        else {
          constraints.gridwidth = 1;
        }
        basicGridPanel.add(middleCell, constraints);
      }
    }
  }

  /**
   * Verifica se o nome � composto apenas por caracteres alfanum�ricos,
   * underscore (_) ou ponto (.) ou traco (-).
   * 
   * @param name Nome a ser validado
   * 
   * @return true se o nome estiver de acordo com as especifica��es do sistema
   */
  public static boolean isValidFileName(String name) {
    return !(name.equals("") || !name.matches("^[\\w_\\.\\-]+$"));
  }

  /**
   * Obt�m um tamanho preferencial para a altura das linhas de uma tabela. Esse
   * tamanho preferencial � a maior altura encontrada em todas as linhas.
   * 
   * @param table a tabela
   * @param rowIndex o �ndice da linha
   * @param margin a margem
   * @return a maior altura dos valores das c�lulas de uma linha
   */
  public static int getPreferredRowHeight(JTable table, int rowIndex, int margin) {
    int height = table.getRowHeight();
    for (int c = 0; c < table.getColumnCount(); c++) {
      TableCellRenderer renderer = table.getCellRenderer(rowIndex, c);
      Component comp = table.prepareRenderer(renderer, rowIndex, c);
      int h = comp.getPreferredSize().height + 2 * margin;
      height = Math.max(height, h);
    }
    return height;
  }

  /**
   * Obt�m um tamanho preferencial para a largura da coluna de uma tabela. Esse
   * tamanho preferencial � a maior largura encontrada em todas as linhas dessa
   * coluna.
   * 
   * @param table a tabela
   * @param colIndex o �ndice da coluna
   * @param margin a margem
   * @return a maior largura dos valores das c�lulas de uma coluna
   */
  public static int getPreferredColWidth(JTable table, int colIndex, int margin) {
    int width = 0;
    for (int r = 0; r < table.getRowCount(); r++) {
      TableCellRenderer renderer = table.getCellRenderer(r, colIndex);
      Component comp = table.prepareRenderer(renderer, r, colIndex);
      int w = comp.getPreferredSize().width + 2 * margin;
      width = Math.max(width, w);
    }
    return width;
  }

  /**
   * A altura de todas as linhas de uma tabela � alterada para a altura
   * preferencial obtida em fun��o da maior altura existente na tabela
   * 
   * @param table a tabela
   * @param margin a margem
   */
  public static void packRowHeight(JTable table, int margin) {
    packRowHeight(table, 0, table.getRowCount(), margin);
  }

  /**
   * A altura das linhas entre <code>start</code> e <code>end</code> de uma
   * tabela � alterada para a altura preferencial obtida em fun��o da maior
   * altura existente na tabela
   * 
   * @param table a tabela
   * @param start linha inicial
   * @param end linha final
   * @param margin a margem
   */
  public static void packRowHeight(JTable table, int start, int end, int margin) {
    for (int r = start; r < end; r++) {
      int h = getPreferredRowHeight(table, r, margin);
      if (table.getRowHeight(r) != h) {
        table.setRowHeight(r, h);
      }
    }
  }

  /**
   * Ajusta a largura de todas as colunas de uma tabela. O tamanho ser�
   * calculado para que seja suficiente para o cabe�alho e a c�lula cujo
   * conte�do tenha a maior largura.
   * 
   * @param table a tabela a ser ajustada
   * @param margin o n�mero de pixels adionados na margem esquerda e direita
   */
  public static void packColumns(JTable table, int margin) {
    for (int c = 0; c < table.getColumnCount(); c++) {
      packColumn(table, c, 2);
    }
  }

  /**
   * Ajusta a largura de uma coluna de uma tabela. O tamanho ser� calculado para
   * que seja suficiente para o cabe�alho e a c�lula cujo conte�do tenha a maior
   * largura.
   * 
   * @param table a tabela a ser ajustada
   * @param vColIndex o �ndice da coluna
   * @param margin o n�mero de pixels adionados na margem esquerda e direita
   */
  public static void packColumn(JTable table, int vColIndex, int margin) {
    DefaultTableColumnModel colModel =
      (DefaultTableColumnModel) table.getColumnModel();
    TableColumn col = colModel.getColumn(vColIndex);
    int width;

    // Obt�m a largura do header da coluna
    TableCellRenderer renderer = col.getHeaderRenderer();
    if (renderer == null) {
      renderer = table.getTableHeader().getDefaultRenderer();
    }
    Component comp =
      renderer.getTableCellRendererComponent(table, col.getHeaderValue(),
        false, false, 0, 0);
    width = comp.getPreferredSize().width;

    // Obt�m a largura m�xima da coluna
    for (int r = 0; r < table.getRowCount(); r++) {
      renderer = table.getCellRenderer(r, vColIndex);
      comp =
        renderer.getTableCellRendererComponent(table, table.getValueAt(r,
          vColIndex), false, false, r, vColIndex);
      width = Math.max(width, comp.getPreferredSize().width);
    }

    // Adiciona a margim
    width += 2 * margin;

    // Atribui a nova largura como tamanho preferencial
    col.setPreferredWidth(width);
  }

  /**
   * Obt�m um tamanho da tabela em fun��o do n�mero de linhas
   * 
   * @param table a tabela
   * @return o tamanho preferencial
   */
  public static Dimension getPreferredSize(JTable table) {
    int heightSize = table.getRowCount() * table.getRowHeight();
    int widthSize = table.getColumnModel().getTotalColumnWidth();
    //Define a largura m�xima da tabela        
    if (widthSize > 750) {
      widthSize = 750;
    }
    // Define a altura m�nima da tabela
    if (heightSize < (2 * table.getRowHeight())) {
      heightSize = 2 * table.getRowHeight();
    }
    // Define a altura m�xima da tabela
    if (heightSize > (25 * table.getRowHeight())) {
      heightSize = 25 * table.getRowHeight();
    }
    return new Dimension(widthSize, heightSize);
  }

  /**
   * Faz a sincroniza��o das barras de scroll entre diferentes painel de scroll.
   * Quando uma delas � arrastada, as outras acompanham.
   * 
   * @param scrollPanes os paineis de scroll a serem sincronizados.
   */
  public static void synchronizeScrollPanes(JScrollPane... scrollPanes) {
    class ScrollBarListener implements AdjustmentListener {
      private JScrollBar[] scrollBars;
      private boolean isAdjusting;

      /**
       * Listener de barras de rolagens.
       * 
       * @param scrollBars barras de rolagens.
       */
      ScrollBarListener(JScrollBar[] scrollBars) {
        this.scrollBars = scrollBars;
        this.isAdjusting = false;
      }

      @Override
      public void adjustmentValueChanged(AdjustmentEvent ev) {
        if (isAdjusting) {
          return;
        }
        isAdjusting = true;
        for (JScrollBar scrollBar : scrollBars) {
          if (ev.getSource() != scrollBar) {
            scrollBar.setValue(ev.getValue());
          }
        }
        isAdjusting = false;
      }
    }
    JScrollBar[] horizontalScrollBars = new JScrollBar[scrollPanes.length];
    JScrollBar[] verticalScrollBars = new JScrollBar[scrollPanes.length];
    AdjustmentListener horizontalListener =
      new ScrollBarListener(horizontalScrollBars);
    AdjustmentListener verticalListener =
      new ScrollBarListener(verticalScrollBars);
    for (int i = 0; i < scrollPanes.length; i++) {
      verticalScrollBars[i] = scrollPanes[i].getVerticalScrollBar();
      verticalScrollBars[i].addAdjustmentListener(verticalListener);
      horizontalScrollBars[i] = scrollPanes[i].getHorizontalScrollBar();
      horizontalScrollBars[i].addAdjustmentListener(horizontalListener);
    }
  }

  /**
   * Obt�m o fonte da linha de totais de determinada tabela.
   * 
   * @param table tabela a ter o fonte obtido.
   * 
   * @return fonte da linha de totais.
   */
  public static Font getTableTotalFont(JTable table) {
    Font font = table.getFont();
    if (font.equals(BOLD_TABLE_FONT)) {
      return font.deriveFont(font.getSize() + 1F);
    }
    return font.deriveFont(Font.BOLD);
  }

  /**
   * Ajusta os fontes e altura da tabela.
   * 
   * @param table tabela a ter os fontes e altura ajustados.
   */
  public static void adjustFontAndHeightTableUI(JTable table) {
    adjustHeightTableUI(table);
  }

  /**
   * Ajusta a altura das linhas da tabela.
   * 
   * @param table tabela a ter a altura ajustada.
   */
  private static void adjustHeightTableUI(JTable table) {
    table.setRowHeight(table.getRowHeight() + TABLE_ROW_GAP);

    JTableHeader header = table.getTableHeader();
    if (header.getClass().isAssignableFrom(JTableHeader.class)
      && header.getDefaultRenderer().getClass().isAssignableFrom(
        DefaultTableCellRenderer.class)) {
      Dimension dimension = header.getPreferredSize();
      dimension.height += TABLE_ROW_GAP;
      header.setPreferredSize(dimension);
    }
  }

  /**
   * Cria um separador com um r�tulo. Pode ser usado como alternativa aos
   * paineis com borda.
   * 
   * @param labelText o texto usado no r�tulo
   * @return o componente criado
   */
  public static JComponent makeLabelSeparator(String labelText) {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(new JLabel(labelText), new GBC(0, 0).insets(0, 0, 0, 5));
    panel.add(new JSeparator(), new GBC(1, 0).insets(5, 0, 0, 0).width(
      GBC.REMAINDER).horizontal().west());
    return panel;
  }
}
