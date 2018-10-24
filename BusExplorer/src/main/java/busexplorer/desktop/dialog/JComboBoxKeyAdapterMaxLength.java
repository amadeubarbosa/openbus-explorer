package busexplorer.desktop.dialog;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JComboBoxKeyAdapterMaxLength extends KeyAdapter {

  private int maxLength;
  private JComboBox jComboBox;

  public JComboBoxKeyAdapterMaxLength(JComboBox jComboBox, int maxLength) {
    this.jComboBox = jComboBox;
    this.maxLength = maxLength;
    jComboBox.getEditor().getEditorComponent().addKeyListener(this);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    char c = e.getKeyChar();
    if (jComboBox.getEditor().getItem().toString().length() < maxLength) {
      if (!(c == '.' || c == ':' || Character.isAlphabetic(c) || Character.isDigit(c)
              || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
        jComboBox.getToolkit().beep();
        e.consume();
      }
    } else {
      e.consume();
    }
  }

}
