package busexplorer.panel.healing;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.OpenBusAction;
import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelComponent;
import busexplorer.panel.providers.ProviderDeleteAction;
import busexplorer.panel.providers.ProviderEditAction;
import busexplorer.panel.providers.ProviderRefreshAction;
import busexplorer.panel.providers.ProviderTableProvider;
import busexplorer.panel.providers.ProviderWrapper;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import tecgraf.javautils.gui.table.ObjectTableModel;

public class ConsistencyReportPanel extends RefreshablePanel {

  private final JPanel cards;
  private TablePanelComponent<ProviderWrapper> providersMissingFieldsPane;
  private final JFrame parentWindow;
  private final JPanel loadingPane;

  public ConsistencyReportPanel(JFrame parentWindow) {
    super(new MigLayout("debug, fill, wrap 1"));
    this.parentWindow = parentWindow;
    JPanel header = new JPanel();

    JLabel headingLabel = new JLabel(ApplicationIcons.ICON_HEALTHY_32);
    headingLabel.setText("Relatório de pendências no cadastro da governança");
    header.add(headingLabel, "growx, push");

    JButton refreshButton = new JButton();
    refreshButton.setAction(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        ConsistencyReportPanel.this.refresh(actionEvent);
      }
    });
    refreshButton.setText(Language.get(TablePanelComponent.class, "refresh"));
    refreshButton.setToolTipText(Language.get(TablePanelComponent.class,
      "refresh.tooltip"));
    refreshButton.setIcon(ApplicationIcons.ICON_REFRESH_16);
    this.add(header, "dock north");

    ArrayList actions = new ArrayList<OpenBusAction>();
    actions.add(new ProviderDeleteAction(parentWindow));
    actions.add(new ProviderEditAction(parentWindow));
    actions.add(new ProvidersMissingFieldsRefreshAction(parentWindow));
    this.providersMissingFieldsPane = new TablePanelComponent<>(
      new ObjectTableModel<>(new LinkedList<>(), new ProviderTableProvider()),
      actions, false, false);

    ImageIcon imageIcon = ApplicationIcons.ICON_LOADING_32;
    JLabel iconLabel = new JLabel(imageIcon);
    imageIcon.setImageObserver(iconLabel);
    this.loadingPane = new JPanel();
    this.loadingPane.add(iconLabel);

    this.cards = new JPanel(new CardLayout());
    this.cards.add(loadingPane);
    this.cards.add(providersMissingFieldsPane);

    this.add(cards, "dock center");
    JPanel refreshPane = new JPanel(new MigLayout("align center"));
    refreshPane.add(refreshButton);
    this.add(refreshPane, "dock south");
  }

  @Override
  public void refresh(ActionEvent event) {
    ((CardLayout) this.cards.getLayout()).first(this.cards);
    new BusExplorerTask<Void>(ExceptionContext.Service) {
      @Override
      protected void doPerformTask() throws Exception {
        ConsistencyReportPanel.this.providersMissingFieldsPane.refresh(null);
      }

      @Override
      protected void afterTaskUI() {
        ((CardLayout) ConsistencyReportPanel.this.cards.getLayout()).last(ConsistencyReportPanel.this.cards);
      }
    }.execute(this.parentWindow, Language.get(ProviderRefreshAction.class, "waiting.title"),
      Language.get(ProviderRefreshAction.class, "waiting.msg"), 2, 0);
  }
}
