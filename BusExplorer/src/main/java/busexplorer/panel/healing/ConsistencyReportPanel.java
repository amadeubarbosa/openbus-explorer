package busexplorer.panel.healing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;
import org.japura.gui.CollapsiblePanel;
import org.japura.gui.CollapsibleRootPanel;

public class ConsistencyReportPanel extends RefreshablePanel {

  private final JPanel cards;
  private final CollapsibleRootPanel collapsiblePane;
  private final LinkedHashMap<String, TablePanelComponent> uiComponents;
  private final JFrame parentWindow;

  public ConsistencyReportPanel(JFrame parentWindow) {
    super(new BorderLayout());
    this.parentWindow = parentWindow;

    JPanel header = new JPanel(new MigLayout("fill, flowy"));
    JLabel headingLabel = new JLabel(ApplicationIcons.ICON_HEALTHY_32);
    headingLabel.setText(getString("title"));
    header.add(headingLabel, "growx, push");
    header.add(new JSeparator(JSeparator.HORIZONTAL), "grow");
    this.add(header, BorderLayout.NORTH);

    this.uiComponents = new LinkedHashMap<>();
    this.uiComponents.put(getString("label.integration.missing.basic"),
      new IntegrationMissingBasicInformation(parentWindow).buildTableComponent());
    this.uiComponents.put(getString("label.consumer.missing.basic"),
      new ConsumerMissingBasicInformation(parentWindow).buildTableComponent());
    this.uiComponents.put(getString("label.provider.missing.basic"),
      new ProviderMissingBasicInformation(parentWindow).buildTableComponent());
    this.uiComponents.put(getString("label.provider.missing.contracts"),
      new ProviderMissingContracts(parentWindow).buildTableComponent());
    this.uiComponents.put(getString("label.provider.missing.busquery"),
      new ProviderMissingBusQuery(parentWindow).buildTableComponent());
    this.uiComponents.put(getString("label.provider.missing.authorizations"),
      new ProviderMissingAuthorizations(parentWindow).buildTableComponent());
    this.uiComponents.put(getString("label.authorization.missing.provider"),
      new AuthorizationMissingProvider(parentWindow).buildTableComponent());
    this.uiComponents.put(getString("label.authorization.missing.offer"),
      new AuthorizationMissingOffer(parentWindow).buildTableComponent());
    this.uiComponents.put(getString("label.offer.missing.provider"),
      new OfferMissingProvider(parentWindow).buildTableComponent());

    ImageIcon imageIcon = ApplicationIcons.ICON_LOADING_32;
    JLabel iconLabel = new JLabel(imageIcon);
    imageIcon.setImageObserver(iconLabel);
    JPanel loadingPane = new JPanel();
    loadingPane.add(iconLabel);

    JLabel okayPane = new JLabel(getString("label.everything.okay"));
    okayPane.setIcon(ApplicationIcons.ICON_VALIDATE_16);
    okayPane.setHorizontalAlignment(JLabel.CENTER);

    this.collapsiblePane = new CollapsibleRootPanel(CollapsibleRootPanel.FILL);
    this.collapsiblePane.setBackground(null);
    JScrollPane scrollPane = new JScrollPane(this.collapsiblePane);
    scrollPane.setMaximumSize(this.getSize());
    scrollPane.setViewportBorder(null);
    scrollPane.setBorder(null);
    scrollPane.getInsets().set(0,0,0,0);

    this.cards = new JPanel(new CardLayout());
    this.cards.add(loadingPane);
    this.cards.add(okayPane);
    this.cards.add(scrollPane);
    this.add(cards, BorderLayout.CENTER);

    JPanel footer = new JPanel(new MigLayout("fill, flowy"));
    footer.add(new JSeparator(JSeparator.HORIZONTAL), "grow");
    JPanel refreshPanel = new JPanel(new MigLayout("align center"));
    JButton refreshButton = new JButton();
    refreshButton.setAction(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        ConsistencyReportPanel.this.refresh(actionEvent);
      }
    });
    refreshButton.setText(Language.get(TablePanelComponent.class, "refresh"));
    refreshButton.setMnemonic(refreshButton.getText().charAt(0));
    refreshButton.setToolTipText(Language.get(TablePanelComponent.class,
      "refresh.tooltip"));
    refreshButton.setIcon(ApplicationIcons.ICON_REFRESH_16);
    refreshPanel.add(refreshButton);
    footer.add(refreshPanel, "grow, push, pad 0");
    this.add(footer, BorderLayout.SOUTH);
  }

  @Override
  public void refresh(ActionEvent event) {
    CardLayout cardLayoutManager = ((CardLayout) this.cards.getLayout());
    Set<Map.Entry<String, TablePanelComponent>> uiElements = this.uiComponents.entrySet();
    cardLayoutManager.first(this.cards);
    new BusExplorerTask<Void>(ExceptionContext.Service) {
      @Override
      protected void doPerformTask() throws Exception {
        uiElements.stream().forEach(entry -> entry.getValue().refresh(event));
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          collapsiblePane.removeAll();
          uiElements.stream().forEach(entry -> {
            TablePanelComponent<?> tableComponent = entry.getValue();
            tableComponent.setPreferredSize(new Dimension(400, 100));
            if (tableComponent.getElements().isEmpty() == false) {
              CollapsiblePanel collapsible = new CollapsiblePanel(entry.getKey());
              collapsible.getInsets().set(0, 10, 0, 10);
              collapsible.add(tableComponent, "grow");
              collapsiblePane.add(collapsible);
            }
          });
          if (collapsiblePane.getComponents().length == 0) {
            cardLayoutManager.next(ConsistencyReportPanel.this.cards);
          } else {
            cardLayoutManager.last(ConsistencyReportPanel.this.cards);
          }
        }
      }
    }.execute(this.parentWindow, getString("waiting.title"), getString("waiting.msg"), 2, 0);
  }

  private String getString(String key) {
    return Language.get(this.getClass(), key);
  }
}
