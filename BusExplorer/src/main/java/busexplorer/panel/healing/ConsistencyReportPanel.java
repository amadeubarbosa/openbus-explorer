package busexplorer.panel.healing;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;

public class ConsistencyReportPanel extends RefreshablePanel {

  private final JPanel cards;
  private final LinkedHashMap<JLabel, TablePanelComponent> uiComponents;
  private final JFrame parentWindow;
  private final JPanel loadingPane;
  private final JPanel dataPane;

  public ConsistencyReportPanel(JFrame parentWindow) {
    super(new MigLayout("flowy"));
    this.parentWindow = parentWindow;
    JPanel header = new JPanel();

    JLabel headingLabel = new JLabel(ApplicationIcons.ICON_HEALTHY_32);
    headingLabel.setText(getString("title"));
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

    this.uiComponents = new LinkedHashMap<>();
    this.uiComponents.put(new JLabel(getString("label.provider.missing.basic")),
      new ProviderMissingBasicInformation(parentWindow).buildTableComponent());
    this.uiComponents.put(new JLabel(getString("label.provider.missing.contracts")),
      new ProviderMissingContracts(parentWindow).buildTableComponent());
    this.uiComponents.put(new JLabel(getString("label.provider.missing.busquery")),
      new ProviderMissingBusQuery(parentWindow).buildTableComponent());
    this.uiComponents.put(new JLabel(getString("label.provider.missing.authorizations")),
      new ProviderMissingAuthorizations(parentWindow).buildTableComponent());
    this.uiComponents.put(new JLabel(getString("label.authorization.missing.provider")),
      new AuthorizationMissingProvider(parentWindow).buildTableComponent());
    this.uiComponents.put(new JLabel(getString("label.offer.missing.provider")),
      new OfferMissingProvider(parentWindow).buildTableComponent());

    ImageIcon imageIcon = ApplicationIcons.ICON_LOADING_32;
    JLabel iconLabel = new JLabel(imageIcon);
    imageIcon.setImageObserver(iconLabel);
    this.loadingPane = new JPanel();
    this.loadingPane.add(iconLabel);

    this.dataPane = new JPanel(new MigLayout("fill, flowy"));

    this.cards = new JPanel(new CardLayout());
    this.cards.add(loadingPane);
    this.cards.add(dataPane);

    this.add(cards, "dock center");
    JPanel refreshPane = new JPanel(new MigLayout("align center"));
    refreshPane.add(refreshButton);
    this.add(refreshPane, "dock south");
  }

  @Override
  public void refresh(ActionEvent event) {
    CardLayout cardLayoutManager = ((CardLayout) this.cards.getLayout());
    Set<Map.Entry<JLabel, TablePanelComponent>> uiElements = this.uiComponents.entrySet();
    cardLayoutManager.first(this.cards);
    new BusExplorerTask<Void>(ExceptionContext.Service) {
      @Override
      protected void doPerformTask() throws Exception {
        uiElements.stream().forEach(entry -> entry.getValue().refresh(event));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          ConsistencyReportPanel.this.dataPane.removeAll();
          uiElements.stream().forEach(entry -> {
            JLabel labelComponent = entry.getKey();
            TablePanelComponent<?> tableComponent = entry.getValue();
            if (tableComponent.getElements().isEmpty() == false) {
              ConsistencyReportPanel.this.dataPane.add(labelComponent, "grow");
              ConsistencyReportPanel.this.dataPane.add(tableComponent, "grow, push, pad 0 10 0 -10");
            }
          });
          cardLayoutManager.last(ConsistencyReportPanel.this.cards);
        }
      }
    }.execute(this.parentWindow, getString("waiting.title"), getString("waiting.msg"), 2, 0);
  }

  private String getString(String key) {
    return Language.get(this.getClass(), key);
  }
}
