package admin.desktop;

import reuse.modified.logistic.client.desktop.GlassPane;

/**
 * Janela que permite o seu bloqueio e desbloqueio.
 * 
 * @author Tecgraf
 */
public class SimpleWindow extends BlockableWindow {
  /**
   * Construtor.
   * 
   * @param title título da janela
   **/
  public SimpleWindow(String title) {

    setTitle(title);

    setGlassPane(new GlassPane(this));
  }

  /**
   * Bloqueia esta janela.
   */
  private void block() {
    this.getGlassPane().setVisible(true);
  }

  /**
   * Desbloqueia esta janela.
   */
  private void unblock() {
    this.getGlassPane().setVisible(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void blockWindow(BlockType blockType) {
    SimpleWindowBlockType block = (SimpleWindowBlockType) blockType;

    switch (block.getBlockMode()) {
      case NO_BLOCK:
        return;
      case BLOCK_THIS:
        this.block();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unblockWindow(BlockType blockType) {
    SimpleWindowBlockType block = (SimpleWindowBlockType) blockType;

    switch (block.getBlockMode()) {
      case NO_BLOCK:
        return;
      case BLOCK_THIS:
        this.unblock();
    }
  }

}
