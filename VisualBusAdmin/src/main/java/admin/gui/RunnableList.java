package admin.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

/**
 * Este componente exibe uma lista de items executáveis. Cada um deles possui
 * um objeto <code>Runnable</code> vinculado a si. Ao selecionar um destes
 * items, ele é executado.
 *
 * @author Tecgraf
 */
public class RunnableList extends JList {
  /**
   * Um item da lista executável contém uma string que o representa e um
   * <code>Runnable</code> vinculado. Tipicamente, este último será executado
   * quando o item for selecionado na lista.
   */
  public static class Item {
     /// String que representa o item
     private String string;

     /// <code>Runnable</code> vinculado ao item
     private Runnable runnable;

     /**
      * Construtor da classe RunnableList.Item
      * @param string String a representar o item
      * @param runnable <code>Runnable</code> a ser vinculado ao item
      */
     public Item(String string, Runnable runnable)
     {
       this.string = string;
       this.runnable = runnable;
     }

     /**
      * Representa o item através de uma string
      * @return String que representa o item
      */
     public String toString()
     {
       return string;
     }

     /**
      * Executa o item
      */
     public void run()
     {
       runnable.run();
     }
  }

  /**
   * A classe extende <code>javax.swing.plaf.basic.BasicListUI</code>,
   * vinculando eventos de mouse à execução dos <code>Runnable</code> e exigindo
   * que um item seja clicado diretamente para ser selecionado.
   */
  private class RunnableListUI extends BasicListUI {

    protected MouseInputListener createMouseInputListener() {
      return new MouseInputHandler();
    }

    public class MouseInputHandler extends BasicListUI.MouseInputHandler {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (isInsideItemBounds(e)) {
          super.mouseClicked(e);
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        if (isInsideItemBounds(e)) {
          super.mousePressed(e);
        }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        if (isInsideItemBounds(e)) {
          RunnableList list = (RunnableList)e.getSource();
          super.mouseReleased(e);
          list.runSelectedItem();
        }
      }

      private boolean isInsideItemBounds(MouseEvent e) {
        RunnableList list = (RunnableList)e.getSource();
        Point location = e.getPoint();
        int index = list.locationToIndex(location);
        Rectangle bounds = list.getCellBounds(index, index);
        return index != -1 && bounds.contains(location);
      }
    }
  }

  /**
   * Construtor da classe RunnableList
   * @param elements Vetor de items executáveis
   */
  public RunnableList(RunnableList.Item[] elements) {
    DefaultListModel defaultListModel = new DefaultListModel();
    for (int x = 0; x < elements.length; x++) {
      defaultListModel.addElement(elements[x]);
    }

    RunnableListUI runnableListUI = new RunnableListUI();
    this.setUI(runnableListUI);

    this.setModel(defaultListModel);

    this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Se o usuário utilizar o teclado, a execução de um item selecionado só
    // será realizada ao se pressionar Enter ou barra de espaço.
    KeyListener keyListener = new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER || 
          e.getKeyCode() == KeyEvent.VK_SPACE) { 
          RunnableList list = (RunnableList)e.getSource();
          list.runSelectedItem();
        }
      }
    };
    this.addKeyListener(keyListener);
  }

  /**
   * Executa o item atualmente selecionado
   */
  public void runSelectedItem() {
    RunnableList.Item item = (RunnableList.Item)getSelectedValue();
    item.run();
    
  }
}
