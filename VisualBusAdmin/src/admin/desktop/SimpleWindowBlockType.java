package admin.desktop;

/**
 * Tipo de bloqueio para janelas SimpleWindow
 * 
 * @author Tecgraf
 */
public class SimpleWindowBlockType implements BlockType {
  /**
   * Tipo de bloqueio.
   */
  private Type blockType;

  /**
   * Tipos de bloqueio para janelas SimpleWindow
   * 
   * @author Tecgraf
   */
  public enum Type {
    /** Não bloqueia a janela */
    NO_BLOCK,
    /** Bloqueia a própria janela */
    BLOCK_THIS
  }

  /**
   * Construtor.
   * 
   * @param type tipo de bloqueio a ser utilizado pela janela
   */
  public SimpleWindowBlockType(Type type) {
    setBlockType(type);
  }

  /**
   * Seta o tipo de bloqueio
   * 
   * @param blockType tipo de bloqueio da janela
   */
  public void setBlockType(Type blockType) {
    this.blockType = blockType;
  }

  /**
   * @return o tipo de bloqueio
   */
  public Type getBlockMode() {
    return this.blockType;
  }
}
