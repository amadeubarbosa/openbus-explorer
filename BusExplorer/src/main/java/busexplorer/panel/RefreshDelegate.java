package busexplorer.panel;

import java.awt.event.ActionEvent;

/**
 * Interface para delega��o do m�todo 'refresh'. Uso combinado com {@link RefreshablePanel}.
 */
public interface RefreshDelegate {
    void refresh(ActionEvent event);
}
