package admin.desktop;

import javax.swing.JFrame;

/**
 * Janela que permite o bloqueio e desbloqueio dela e de outras janelas
 * relacionadas, de acordo com o tipo de bloqueio.
 * 
 * @author Tecgraf
 */
public abstract class BlockableWindow extends JFrame {
  /**
   * Bloqueia a janela de acordo com o tipo de bloqueio
   * 
   * @param blockType tipo de bloqueio
   */
  public abstract void blockWindow(BlockType blockType);

  /**
   * Desbloqueia a janela de acordo com o tipo de bloqueio
   * 
   * @param blockType tipo de bloqueio
   */
  public abstract void unblockWindow(BlockType blockType);

}
