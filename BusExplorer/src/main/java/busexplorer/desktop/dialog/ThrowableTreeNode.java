package busexplorer.desktop.dialog;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/** Classe que implementa um nó da árvore de exceções */
public final class ThrowableTreeNode implements TreeNode {
  /** Nó pai */
  private ThrowableTreeNode parentTreeNode;
  /** Nó filho */
  private ThrowableTreeNode childTreeNode;
  /** Exceção lançada */
  private Throwable throwable;

  /**
   * Construtor
   * 
   * @param throwable a exceção do nó a ser criado
   */
  public ThrowableTreeNode(Throwable throwable) {
    this(null, throwable);
  }

  /**
   * Construtor
   * 
   * @param parentTreeNode o nó pai na árvore
   * @param throwable a exceção do nó a ser criado
   */
  private ThrowableTreeNode(ThrowableTreeNode parentTreeNode,
    Throwable throwable) {
    this.parentTreeNode = parentTreeNode;
    this.throwable = throwable;
    Throwable cause = throwable.getCause();
    if (cause != null && cause != this.throwable) {
      this.childTreeNode = new ThrowableTreeNode(this, cause);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<?> children() {
    return new CauseEnumeration();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (!object.getClass().equals(getClass())) {
      return false;
    }
    ThrowableTreeNode node = (ThrowableTreeNode) object;
    return node.throwable.equals(this.throwable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getAllowsChildren() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TreeNode getChildAt(int childIndex) {
    if (this.childTreeNode != null) {
      if (childIndex == 0) {
        return this.childTreeNode;
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChildCount() {
    if (this.childTreeNode != null) {
      return 1;
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getIndex(TreeNode node) {
    if (node.equals(this.childTreeNode)) {
      return 0;
    }
    return -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TreeNode getParent() {
    return this.parentTreeNode;
  }

  /**
   * Obtém a exceção associada a esse nó da árvore.
   * 
   * @return a exceção exibida nesse nó
   */
  public Throwable getThrowable() {
    return this.throwable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return this.throwable.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLeaf() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return trimWhiteSpaces(this.throwable.toString());
  }

  /**
   * Retira os separadores (\b, \n, \t, \f, \r) do texto, separando o texto
   * entre eles com um espaço.
   * 
   * @param text texto com os separadores.
   * 
   * @return texto com separação por espaço.
   */
  private static String trimWhiteSpaces(String text) {
    String[] tokens = text.split("[\b\n\t\f\r]");
    String newText = "";
    String separator = "";
    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i].trim();
      if (token.length() != 0) {
        newText += separator + token;
        separator = " ";
      }
    }
    return newText;
  }

  /**
   * A enumeração para os filhos de um nó da árvore de exceções.
   */
  private final class CauseEnumeration implements
    Enumeration<ThrowableTreeNode> {
    /** Indica se este item já foi lido */
    boolean wasConsumed;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasMoreElements() {
      if (getChildCount() != 0) {
        if (!this.wasConsumed) {
          return true;
        }
      }
      return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThrowableTreeNode nextElement() {
      if (!hasMoreElements()) {
        throw new NoSuchElementException();
      }
      this.wasConsumed = true;
      return ThrowableTreeNode.this.childTreeNode;
    }
  }
}
