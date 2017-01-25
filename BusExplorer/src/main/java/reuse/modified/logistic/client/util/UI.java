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
 * A classe UI possui métodos úteis para a construção das interfaces do Bandeira
 * Brasil e do Alope.
 */
public class UI extends PrintableUI {
  /** Diretório de resources com imagens. */
  public static final String ICON_DIRECTORY =
    "/reuse/modified/logistic/client/resources/images";

  /**
   * Ícones
   */

  /** Ícone de Collapsed */
  public static final ImageIcon COLLAPSED_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/collapsed.gif"));
  /** Ícone de Expanded */
  public static final ImageIcon EXPANDED_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/expanded.gif"));
  /** Ícone de um header que está colapsado */
  public static final ImageIcon COLLAPSED_HEADER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/collapsedHeader.gif"));
  /** Ícone de header que está expandido */
  public static final ImageIcon EXPANDED_HEADER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/expandedHeader.gif"));
  /** Ícone de Warning */
  public static final ImageIcon WARNING_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/warning.png"));
  /** Ícone de limpeza em botão */
  public static final ImageIcon BUTTON_CLEAR_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/buttonClear.gif"));
  /** Ícone de composição de mail/notificação em botão (padrão adaptado) */
  public static final ImageIcon BUTTON_MAIL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/buttonMail.gif"));
  /** Ícone de seta para baixo */
  public static final ImageIcon BUTTON_DOWN_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/buttonDown.gif"));
  /** Ícone de seta para cima */
  public static final ImageIcon BUTTON_UP_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/buttonUp.gif"));
  /** Ícone de indicação que a célula pode ser editada */
  public static final ImageIcon EDITABLE_BUTTON_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/editableButton.gif"));
  /** Ícone de indicação que a célula pode ser editada escolhendo valores */
  public static final ImageIcon LIST_EDITABLE_BUTTON_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/listEditableButton.gif"));
  /** Ícone de indicação que a célula tem erro */
  public static final ImageIcon ERROR_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/error.png"));
  /** Ícone em branco para fazer alinhamento com os ícones de edição */
  public static final ImageIcon BLANK_BUTTON_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/blankButton.gif"));
  /** Ícone de indicação que a célula não pode ser editáda */
  public static final ImageIcon NOT_EDITABLE_BUTTON_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/notEditableButton.gif"));
  /** Ícone de indicação de verificação na célula (como um checkbox) */
  public static final ImageIcon TICK_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/tick.gif"));
  /** Ícone de indicação de não verificação na célula */
  public static final ImageIcon NOT_TICK_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/notTick.gif"));
  /** Ícone com a linha que compõe a janela de Sobre */
  public static final ImageIcon ABOUT_LINE_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/aboutLine.gif"));

  /** Ícone de cenário base */
  public static final ImageIcon BASE_SCENA_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/baseScena.gif"));
  /** Ícone de cenário vigente */
  public static final ImageIcon CURRENT_APPROVAL_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/currentApprovalScena.gif"));
  /** Ícone de cenário aprovado */
  public static final ImageIcon APPROVAL_SCENA_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/approvalScena.gif"));
  /** Ícone de cenário editável */
  public static final ImageIcon EDITABLE_SCENA_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/editableScena.gif"));
  /** Ícone de cenário editável e bloqueado */
  public static final ImageIcon EDITABLE_BLOCKED_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/editableBlockedScena.gif"));
  /** Ícone de cenário não editável */
  public static final ImageIcon NOT_EDITABLE_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/notEditableScena.gif"));
  /** Ícone de cenário sendo otimizado */
  public static final ImageIcon SOLVING_SCENA_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/solvingScena.gif"));
  /** Ícone de cenário editável que teve erro na otimização */
  public static final ImageIcon EDITABLE_ERROR_SOLVING_SCENA_ICON =
    new ImageIcon(UI.class.getResource(ICON_DIRECTORY
      + "/editableErrorSolvingScena.gif"));
  /** Ícone de cenário editável bloqueado que teve erro na otimização */
  public static final ImageIcon EDITABLE_BLOCKED_ERROR_SOLVING_SCENA_ICON =
    new ImageIcon(UI.class.getResource(ICON_DIRECTORY
      + "/editableBlockedErrorSolvingScena.gif"));
  /** Ícone de cenário não editável que teve erro na otimização */
  public static final ImageIcon NOT_EDITABLE_ERROR_SOLVING_SCENA_ICON =
    new ImageIcon(UI.class.getResource(ICON_DIRECTORY
      + "/notEditableErrorSolvingScena.gif"));
  /** Ícone de cenário de otimização editável */
  public static final ImageIcon SOLVED_EDITABLE_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/editableSolvedScena.gif"));
  /** Ícone de cenário de otimização editável e bloqueado */
  public static final ImageIcon SOLVED_EDITABLE_BLOCKED_SCENA_ICON =
    new ImageIcon(UI.class.getResource(ICON_DIRECTORY
      + "/editableBlockedSolvedScena.gif"));
  /** Ícone de cenário de otimização não editável */
  public static final ImageIcon SOLVED_NOT_EDITABLE_SCENA_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/notEditableSolvedScena.gif"));
  /** Ícone de pasta com permissão de aprovação */
  public static final ImageIcon APPROVABLE_FOLDER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/approvableFolder.gif"));
  /** Ícone de pasta de permissão apenas para leitura */
  public static final ImageIcon READ_ONLY_APPROVE_FOLDER_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/readOnlyApproveFolder.gif"));
  /** Ícone de pasta com permissão para escrita */
  public static final ImageIcon WRITEABLE_FOLDER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/writeableFolder.gif"));
  /** Ícone de pasta com permissão apenas para leitura */
  public static final ImageIcon READ_ONLY_FOLDER_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/readOnlyFolder.gif"));
  /** Ícone do logo da Petrobras para o relatório */
  public static final ImageIcon REPORT_BR_LOGO = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/reportBrLogo.png"));
  /** Ícone com o símbolo de sucesso na validação de uma regra */
  public static final ImageIcon SUCCESS_VALIDATION_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/Success.gif"));
  /** Ícone com o símbolo de falha na validação de uma regra */
  public static final ImageIcon UNSUCCESS_VALIDATION_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/SevereFail.gif"));
  /** Ícone com o símbolo de warning na validação de uma regra */
  public static final ImageIcon WARNING_VALIDATION_ICON = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/WarningFail.gif"));
  /** Ícone que representa um produto */
  public static final ImageIcon PRODUCT_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/product_icon.png"));
  /** Ícone que representa um produto desativado */
  public static final ImageIcon UNACTIVE_PRODUCT_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/unactive_product_icon.gif"));
  /** Ícone que representa um grupamento de produtos */
  public static final ImageIcon PRODUCT_SET_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/productset_icon.png"));
  /** Ícone que representa uma região */
  public static final ImageIcon POINT_SET_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/pointset_icon.png"));
  /** Ícone que indica a remoção de uma linha de uma tabela */
  public static final ImageIcon REMOVE_ROW_ICON = NOT_TICK_ICON;
  /** Ícone que representa uma mistura simples */
  public static final ImageIcon SINGLE_MIXTURE_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/single_mixture.gif"));
  /** Ícone que representa um grupamento de misturas */
  public static final ImageIcon MIXTURE_SET_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/mixture_set.gif"));
  /** Ícone que representa um local de escoamento */
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

  /** Ícone que representa um duto */
  public static final ImageIcon PIPELINE_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/pipelineModal.gif"));
  /** Ícone que representa um navio */
  public static final ImageIcon SHIP_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/shipModal.gif"));
  /** Ícone que representa uma rodovia */
  public static final ImageIcon HIGHWAY_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/highwayModal.gif"));
  /** Ícone que representa uma ferrovia */
  public static final ImageIcon RAILROAD_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/railroadModal.gif"));
  /** Ícone que representa um granel */
  public static final ImageIcon GRANEL_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/granelModal.gif"));
  /** Ícone que representa uma barcaça */
  public static final ImageIcon BARGE_MODAL_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/bargeModal.gif"));

  /** Ícone usado para seleção de todos os nós do CheckBoxTree */
  public static final ImageIcon CHECK_BOX_TREE_SELECT_ALL = new ImageIcon(
    UI.class.getResource(ICON_DIRECTORY + "/select-all.png"));
  /** Ícone usado para remover a seleção de todos os nós do CheckBoxTree */
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

  /** Icone de lata de lixo usado em situações de remoção */
  public static final ImageIcon TRASH_BIN_ICON = new ImageIcon(UI.class
    .getResource(ICON_DIRECTORY + "/trash.gif"));

  /**
   * Cores
   */

  /** Cor dos grupos de cenários */
  public static final Color FOLDER_COLOR = new Color(70, 70, 70);
  /** Cor dos cenários */
  public static final Color SCENA_COLOR = new Color(70, 70, 70);
  /** Cor dos cenários bloqueados */
  public static final Color BLOCKED_SCENA_COLOR = Color.blue;
  /** Cor de seleção do cenário */
  public static final Color SCENA_SELECTION_COLOR = new Color(112, 178, 12);
  /** Cor do cenário vigente */
  public static final Color CURRENT_SCENA_COLOR = new Color(0, 0, 0);
  /** Cor do fundo da célula modificada (Azul claro) */
  public static final Color CHANGED_BACKGROUND_COLOR = new Color(224, 241, 255);
  /** Cor do fundo da célula editada (Amarelo claro) */
  public static final Color EDITED_BACKGROUND_COLOR = new Color(255, 255, 183);
  /** Cor do fundo da célula aprovada (Verde claro) */
  public static final Color PRESAVED_BACKGROUND_COLOR =
    new Color(200, 255, 200);
  /** Cor do fundo da célula editável */
  public static final Color EDITABLE_BACKGROUND = Color.white;
  /** Cor do fundo da célula não editável */
  public static final Color NOT_EDITABLE_BACKGROUND = Color.white;
  /** Cor do fonte da célula com valor positivo editável */
  public static final Color POSITIVE_EDIT_COLOR = new Color(0, 0, 0);
  /** Cor do fonte da célula com valor positivo não editável */
  public static final Color POSITIVE_NOT_EDIT_COLOR = new Color(140, 140, 140);
  /** Cor do fonte da célula com valor negativo editável */
  public static final Color NEGATIVE_EDIT_COLOR = new Color(204, 0, 0);
  /** Cor do fonte da célula com valor negativo não editável */
  public static final Color NEGATIVE_NOT_EDIT_COLOR = new Color(230, 138, 138);
  /** Cor da célula selcionada */
  public static final Color CELL_SELECTION_COLOR = new Color(199, 199, 226);
  /** Cor da fonte da janela de informações do sistema */
  public static final Color ABOUT_FONT_COLOR = new Color(67, 87, 114);
  /** Cor do glasspane (translúcido) usado nas janelas bloqueadas */
  public static final Color GLASS_PANE_COLOR = new Color(235, 235, 235, 150);
  /** Cor do fundo do gráfico */
  public static Color CHART_BACKGROUND = new JPanel().getBackground();
  /** Cor do fundo da área de plotagem do gráfico */
  public static Color CHART_PLOT_BACKGROUND = Color.white;
  /** Cor das linhas do grid da área de plotagem do gráfico */
  public static Color CHART_GRID_COLOR = Color.lightGray;
  /** Cor das linhas que marca o Zero do gráfico */
  public static Color CHART_ZERO_MARKER = Color.darkGray;
  /** Cor das linhas que marca o fim do mês no gráfico */
  public static Color CHART_MONTH_MARKER = Color.gray;
  /** A cor para indicar que o campo não foi preenchido corretamente */
  public static final Color FIELD_ERROR_COLOR = new Color(255, 0, 0);
  /** A cor da borda do do item selecionado */
  public static final Color TREE_SELECTION_BORDER_BACKGROUND = UIManager
    .getColor("Tree.selectionBorderColor");

  /**
   * Cursor
   */

  /** Cursor padrão */
  public static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

  /** Cursor redimensionável para direita e esquerda */
  public static final Cursor RESIZE_CURSOR = new Cursor(Cursor.E_RESIZE_CURSOR);

  /** ************************ FONTES ****************************** */

  /** Fonte para impressão dos relatórios. */
  public static final Font REPORT_TITLE_FONT = new Font("SansSerif", Font.BOLD,
    10);

  /** Fonte do título da propriedade na tela de propriedades do cenário */
  public static final Font HEADER_FONT = new Font("Verdana", Font.BOLD, 11);

  /** Fonte da propriedade na tela de propriedades do cenário */
  public static final Font INFO_FONT = new Font("Verdana", Font.PLAIN, 11);

  /** Fonte da mensagem de notificação */
  public static final Font NOTIFICATION_FONT = INFO_FONT;

  /** Fonte utilizada no gráfico */
  public static Font CHART_FONT = new Font("SansSerif", Font.PLAIN, 9);

  /** Fonte pequena utilizada no gráfico */
  public static Font SMALL_CHART_FONT = new Font("SansSerif", Font.PLAIN, 8);

  /** Fonte utilizada no subtítulo do gráfico */
  public static Font SUBTITLE_CHART_FONT = new Font("Verdana", Font.PLAIN, 10);

  /** Fonte utilizada no título do gráfico */
  public static Font TITLE_CHART_FONT = new Font("Verdana", Font.BOLD, 11);

  /** Fonte usada nas tabelas */
  public static final Font BOLD_TABLE_FONT = new Font("Verdana", Font.BOLD, 11);

  /** Fonte para uso em TextAreas e Labels */
  public static final Font DESCRIPTION_FONT =
    new Font("Tahoma", Font.PLAIN, 11);

  /** ************************ TABELAS **************************** * */

  /** Altura adicionada às linhas das tabelas */
  public static final int TABLE_ROW_GAP = 4;

  /** Espaço entre os ícones e o fonte nas tabelas */
  public static final int TABLE_ICON_TEXT_GAP =
    new JLabel().getIconTextGap() + 2;

  /**
   * Insere os botões passados como parâmetro em um painel com BoxLayout e
   * iguala o tamanho dos botões.
   * 
   * @param buttons array com os botões
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
   * Atribui um tamanho específico para a largura de determinada coluna do
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
   * Iguala o tamanho dos botões contidos no array passado como parâmetro.
   * 
   * @param buttons o array com os botões
   */
  public static void equalizeButtonSizes(JButton[] buttons) {
    Dimension maxSize = new Dimension(0, 0);
    int i;

    // Encontra maior dimensão
    for (i = 0; i < buttons.length; ++i) {
      maxSize.width =
        Math.max(maxSize.width, buttons[i].getPreferredSize().width);
      maxSize.height =
        Math.max(maxSize.height, buttons[i].getPreferredSize().height);
    }

    // Atribui novos valores para "preferred" e "maximum size", uma vez que
    // BoxLayout leva ambos os valores em consideração. */
    for (i = 0; i < buttons.length; ++i) {
      buttons[i].setPreferredSize((Dimension) maxSize.clone());
      buttons[i].setMaximumSize((Dimension) maxSize.clone());
      buttons[i].setMinimumSize((Dimension) maxSize.clone());
    }
  }

  /**
   * Cria um painel "básico" diagramado por um <code>GridBagLayout</code>. Este
   * tipo de painel é utilizado, tipicamente, em interações que configuram um
   * conjunto de parâmetros textuais ou numéricos, e é composto por um número
   * variável de linhas de componentes com as seguintes características:
   * 
   * <ul>
   * <li>o primeiro compontente de cada linha é normalmente um label</li>
   * <li>o segundo componente é um campo de texto ou uma combo-box</li>
   * <li>o terceiro componente é um label ou um botão</li>
   * </ul>
   * 
   * 
   * @param rows "array" contendo os componentes a serem inseridos no painel.
   *        Cada elemento deste array contém os componentes da linha
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
   *        Cada elemento deste array contém os componentes da linha
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

      // O componente do meio é expandido horizontalmente
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
   * Verifica se o nome é composto apenas por caracteres alfanuméricos,
   * underscore (_) ou ponto (.) ou traco (-).
   * 
   * @param name Nome a ser validado
   * 
   * @return true se o nome estiver de acordo com as especificações do sistema
   */
  public static boolean isValidFileName(String name) {
    return !(name.equals("") || !name.matches("^[\\w_\\.\\-]+$"));
  }

  /**
   * Obtém um tamanho preferencial para a altura das linhas de uma tabela. Esse
   * tamanho preferencial é a maior altura encontrada em todas as linhas.
   * 
   * @param table a tabela
   * @param rowIndex o índice da linha
   * @param margin a margem
   * @return a maior altura dos valores das células de uma linha
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
   * Obtém um tamanho preferencial para a largura da coluna de uma tabela. Esse
   * tamanho preferencial é a maior largura encontrada em todas as linhas dessa
   * coluna.
   * 
   * @param table a tabela
   * @param colIndex o índice da coluna
   * @param margin a margem
   * @return a maior largura dos valores das células de uma coluna
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
   * A altura de todas as linhas de uma tabela é alterada para a altura
   * preferencial obtida em função da maior altura existente na tabela
   * 
   * @param table a tabela
   * @param margin a margem
   */
  public static void packRowHeight(JTable table, int margin) {
    packRowHeight(table, 0, table.getRowCount(), margin);
  }

  /**
   * A altura das linhas entre <code>start</code> e <code>end</code> de uma
   * tabela é alterada para a altura preferencial obtida em função da maior
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
   * Ajusta a largura de todas as colunas de uma tabela. O tamanho será
   * calculado para que seja suficiente para o cabeçalho e a célula cujo
   * conteúdo tenha a maior largura.
   * 
   * @param table a tabela a ser ajustada
   * @param margin o número de pixels adionados na margem esquerda e direita
   */
  public static void packColumns(JTable table, int margin) {
    for (int c = 0; c < table.getColumnCount(); c++) {
      packColumn(table, c, 2);
    }
  }

  /**
   * Ajusta a largura de uma coluna de uma tabela. O tamanho será calculado para
   * que seja suficiente para o cabeçalho e a célula cujo conteúdo tenha a maior
   * largura.
   * 
   * @param table a tabela a ser ajustada
   * @param vColIndex o índice da coluna
   * @param margin o número de pixels adionados na margem esquerda e direita
   */
  public static void packColumn(JTable table, int vColIndex, int margin) {
    DefaultTableColumnModel colModel =
      (DefaultTableColumnModel) table.getColumnModel();
    TableColumn col = colModel.getColumn(vColIndex);
    int width;

    // Obtém a largura do header da coluna
    TableCellRenderer renderer = col.getHeaderRenderer();
    if (renderer == null) {
      renderer = table.getTableHeader().getDefaultRenderer();
    }
    Component comp =
      renderer.getTableCellRendererComponent(table, col.getHeaderValue(),
        false, false, 0, 0);
    width = comp.getPreferredSize().width;

    // Obtém a largura máxima da coluna
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
   * Obtém um tamanho da tabela em função do número de linhas
   * 
   * @param table a tabela
   * @return o tamanho preferencial
   */
  public static Dimension getPreferredSize(JTable table) {
    int heightSize = table.getRowCount() * table.getRowHeight();
    int widthSize = table.getColumnModel().getTotalColumnWidth();
    //Define a largura máxima da tabela        
    if (widthSize > 750) {
      widthSize = 750;
    }
    // Define a altura mínima da tabela
    if (heightSize < (2 * table.getRowHeight())) {
      heightSize = 2 * table.getRowHeight();
    }
    // Define a altura máxima da tabela
    if (heightSize > (25 * table.getRowHeight())) {
      heightSize = 25 * table.getRowHeight();
    }
    return new Dimension(widthSize, heightSize);
  }

  /**
   * Faz a sincronização das barras de scroll entre diferentes painel de scroll.
   * Quando uma delas é arrastada, as outras acompanham.
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
   * Obtém o fonte da linha de totais de determinada tabela.
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
   * Cria um separador com um rótulo. Pode ser usado como alternativa aos
   * paineis com borda.
   * 
   * @param labelText o texto usado no rótulo
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
