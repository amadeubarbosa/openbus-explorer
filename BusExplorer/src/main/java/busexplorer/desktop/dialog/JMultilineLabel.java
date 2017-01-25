package busexplorer.desktop.dialog;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Alternativa a JLabel para grandes textos.
 * </p>
 * <p>
 * Obs.: o texto n�o aceita elementos html.
 * </p>
 * 
 * @author Tecgraf
 * @see JLabel
 */
public class JMultilineLabel extends JComponent {
  /*
   * @see
   * http://www.koders.com/java/fid9BE9B31AC9BED01828448BF91A61AFA5AE431E16.
   * aspx?s=JMultilineLabel#L8
   */

  /**
   * Configura��es para renderizar o texto.
   */
  private static final FontRenderContext frc = new FontRenderContext(null,
    true, false);

  /**
   * Texto a ser apresentado.
   */
  private String text;
  /**
   * Margem entre o texto e as bordas do componente.<br>
   * O padr�o � {@code new Insets(0,0,0,0)}.
   */
  private Insets margin;
  /**
   * Largura m�xima que o componente deve ter. O padr�o �
   * {@value Integer#MAX_VALUE}.
   */
  private int maxWidth;
  /**
   * Flag que indica se o alinhamento do texto deve ser justificado.<br>
   * O padr�o � <tt>true</tt>.
   */
  private boolean justify;

  /**
   * Construtor.
   */
  public JMultilineLabel() {
    setFont(new JLabel().getFont());
    margin = new Insets(0, 0, 0, 0);
    maxWidth = Integer.MAX_VALUE;
    justify = true;
  }

  /**
   * Recalcula o tamanho do componente e repinta ele.
   */
  private void morph() {
    revalidate();
    repaint();
  }

  /**
   * Obt�m o texto a ser mostrado.
   * 
   * @return texto a ser mostrado.
   * @see #setText(String)
   */
  public String getText() {
    return text;
  }

  /**
   * Atribui o texto a ser mostrado.
   * 
   * @param text texto a ser mostrado.<br>
   *        Obs.: o texto n�o aceita elementos html.
   * @see #getText()
   */
  public void setText(String text) {
    String old = this.text;
    this.text = text;
    firePropertyChange("text", old, this.text);
    if ((old == null) ? text != null : !old.equals(text)) {
      morph();
    }
  }

  /**
   * Obt�m a largura m�xima.
   * 
   * @return a largura m�xima.
   * @see #setMaxWidth(int)
   */
  public int getMaxWidth() {
    return maxWidth;
  }

  /**
   * Atribui a largura m�xima.
   * 
   * @param maxWidth a largura m�xima.<br>
   *        O padr�o � {@value Integer#MAX_VALUE}.
   * @see #getMaxWidth()
   */
  public void setMaxWidth(int maxWidth) {
    if (maxWidth <= 0) {
      throw new IllegalArgumentException();
    }

    int old = this.maxWidth;
    this.maxWidth = maxWidth;
    firePropertyChange("maxWidth", old, this.maxWidth);
    if (old != this.maxWidth) {
      morph();
    }
  }

  /**
   * <p>
   * Obt�m a margem deste componente.
   * </p>
   * <p>
   * A margem � um espa�o entre as bordas deste componente e o texto.<br>
   * O padr�o � {@code new Insets(0,0,0,0)}.
   * </p>
   * 
   * @return margem deste componente.
   * @see #setMargin(Insets)
   */
  public Insets getMargin() {
    return margin;
  }

  /**
   * Atribui uma margem ao componente.
   * 
   * @param margin espa�o entre as bordas deste componente e o texto. A margem
   *        n�o pode ser null e suas componentes (top, left, bottom, right)
   *        devem ser maior ou igual a zero.
   * 
   * @see #getMargin()
   */
  public void setMargin(Insets margin) {
    if (margin == null || margin.top < 0 || margin.left < 0 || margin.right < 0
      || margin.bottom < 0) {
      throw new IllegalArgumentException();
    }

    Insets old = this.margin;
    this.margin = margin;
    firePropertyChange("margin", old, this.margin);
    if (old != this.margin) {
      morph();
    }
  }

  /**
   * Verifica se o alinhamento do texto � justificado.
   * 
   * @return <tt>true</tt> se o alinhamento do texto � justificado. O padr�o �
   *         <tt>true</tt>.
   * @see #setJustified(boolean)
   */
  public boolean isJustified() {
    return justify;
  }

  /**
   * Define se o texto deve ter o alinhamento justificado.
   * 
   * @param justify <tt>true</tt> para justificar o alinhamento do texto.
   * @see #isJustified()
   */
  public void setJustified(boolean justify) {
    boolean old = this.justify;
    this.justify = justify;
    firePropertyChange("justified", old, this.justify);
    if (old != this.justify) {
      repaint();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getPreferredSize() {
    return paintOrGetSize(null, getMaxWidth());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    paintOrGetSize((Graphics2D) g, getWidth());
  }

  /**
   * Quebra o texto para caber na largura dada e retorna o tamanho necess�rio
   * para desenhar o componente. Se o gr�fico dado for diferente de null} ,
   * desenha o texto do componente.
   * 
   * @param g gr�fico aonde o texto deve ser desenhado. Se for igual a null} , o
   *        texto n�o ser� desenhado, apenas o tamanho necess�rio para desenhar
   *        o componente ser� retornado.
   * @param width largura que o componente deve ter.
   * 
   * @return tamanho necess�rio para desenhar o componente.
   */
  private Dimension paintOrGetSize(Graphics2D g, int width) {
    Insets insets = getInsets();
    width -= insets.left + insets.right + margin.left + margin.right;
    float w = insets.left + insets.right + margin.left + margin.right;
    float x = insets.left + margin.left, y = insets.top + margin.top;

    if (width == 0) {
      return new Dimension((int) w, (int) y + insets.bottom + margin.bottom);
    }

    float max = 0;
    float last_y;
    for (String line : split(text, '\n')) {
      line = line.isEmpty() ? " " : line;

      AttributedString as = new AttributedString(line);
      as.addAttribute(TextAttribute.FONT, getFont());
      AttributedCharacterIterator aci = as.getIterator();
      LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);

      while (lbm.getPosition() < aci.getEndIndex()) {
        TextLayout textLayout = lbm.nextLayout(width);

        if (g != null && isJustified()
          && textLayout.getVisibleAdvance() > 0.80 * width) {
          textLayout = textLayout.getJustifiedLayout(width);
        }
        if (g != null) {
          textLayout.draw(g, x, y + textLayout.getAscent());
        }

        last_y =
          textLayout.getDescent() + textLayout.getLeading()
            + textLayout.getAscent() + 0.2F;
        y += last_y;

        max = Math.max(max, textLayout.getVisibleAdvance());
      }
    }
    w += max;
    return new Dimension((int) Math.ceil(w), (int) Math.ceil(y) + insets.bottom
      + margin.bottom);
  }

  /**
   * <p>
   * Divide um texto em partes, dado um separador.
   * </p>
   * </p>
   * <p>
   * Diferente da vers�o {@link String#split(String)}, essa mant�m elementos
   * vazios no final. <br>
   * Por exemplo: <blockquote>
   * <table cellpadding=1 cellspacing=0 summary="Exemplo do resultado da divis�o de um texto dado um delimitador.">
   * <tr>
   * <th>Express�o</th>
   * <th>Resultado</th>
   * </tr>
   * <tr>
   * <td align=center>:</td>
   * <td><tt>"boo:and:foo".split("o")</tt></td>
   * </tr>
   * <tr>
   * <td align=center>o</td>
   * <td><tt>{ "b", "", ":and:f" }</tt></td>
   * </tr>
   * <tr>
   * <td align=center>:</td>
   * <td><tt>StringUtils.("boo:and:foo", 'o')</tt></td>
   * </tr>
   * <tr>
   * <td align=center>o</td>
   * <td><tt>{ "b", "", ":and:f", "" }</tt></td>
   * </tr>
   * </table>
   * </blockquote>
   * </p>
   * 
   * @param text Texto a ser dividido.
   * @param delimiter Delimitador do texto.
   * @return texto dividido.
   */
  public static List<String> split(String text, char delimiter) {
    List<String> splitted = new ArrayList<>();
    int begin = 0, cursor = text.indexOf(delimiter, begin);
    while (cursor > 0) {
      splitted.add(text.substring(begin, cursor));
      begin = cursor + 1;
      cursor = text.indexOf(delimiter, begin);
    }
    if (begin < text.length()) {
      splitted.add(text.substring(begin));
    }
    return splitted;
  }
}
