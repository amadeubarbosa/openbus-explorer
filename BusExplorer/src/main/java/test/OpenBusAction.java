package test;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import admin.BusAdmin;

/**
 * A��o que cont�m os principais componentes a serem utilizados na janela
 * principal da aplica��o
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado ao {@link PanelComponent} relacionado a
 *        esta a��o.
 */
public abstract class OpenBusAction<T> extends AbstractAction implements
  PanelActionInterface<T> {

  protected BusAdmin admin;
  protected JFrame parentWindow;
  private PanelComponent<T> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow
   * @param admin
   * @param actionName nome da a��o
   */
  public OpenBusAction(JFrame parentWindow, BusAdmin admin, String actionName) {
    super(actionName);
    this.admin = admin;
    this.parentWindow = parentWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPanelComponent(PanelComponent<T> panel) {
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PanelComponent<T> getPanelComponent() {
    return this.panel;
  }

}
