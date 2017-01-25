package busexplorer.panel;

import java.awt.event.ActionEvent;

/**
 * Interface para delegação do método 'refresh'. Uso combinado com {@link RefreshablePanel}.
 */
public interface RefreshDelegate {
    void refresh(ActionEvent event);
}
