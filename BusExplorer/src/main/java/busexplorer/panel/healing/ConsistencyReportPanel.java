package busexplorer.panel.healing;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.japura.gui.CollapsiblePanel;
import org.japura.gui.CollapsibleRootPanel;

import busexplorer.ApplicationIcons;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.RefreshablePanel;
import busexplorer.panel.TablePanelComponent;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import net.miginfocom.swing.MigLayout;

public class ConsistencyReportPanel extends RefreshablePanel {

  private static final String LOADING = "Loading";
  private static final String NO_ISSUES = "No issues";
  private static final String HAS_ISSUES = "Has issues";
  private static final String CANCELLED = "Cancelled";
  private final JPanel cards;
  private final CollapsibleRootPanel collapsibleRootPanel;
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
      new IntegrationMissingBasicInformation(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());
    this.uiComponents.put(getString("label.consumer.missing.basic"),
      new ConsumerMissingBasicInformation(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());
    this.uiComponents.put(getString("label.provider.missing.basic"),
      new ProviderMissingBasicInformation(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());
    this.uiComponents.put(getString("label.provider.missing.contracts"),
      new ProviderMissingContracts(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());
    this.uiComponents.put(getString("label.provider.missing.busquery"),
      new ProviderMissingBusQuery(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());
    this.uiComponents.put(getString("label.provider.missing.authorizations"),
      new ProviderMissingAuthorizations(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());
    this.uiComponents.put(getString("label.authorization.missing.provider"),
      new AuthorizationMissingProvider(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());
    this.uiComponents.put(getString("label.authorization.missing.offer"),
      new AuthorizationMissingOffer(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());
    this.uiComponents.put(getString("label.offer.missing.provider"),
      new OfferMissingProvider(parentWindow, this::discardCollapsiblePanel)
        .buildTableComponent());

    ImageIcon imageIcon = ApplicationIcons.ICON_LOADING_32;
    JLabel iconLabel = new JLabel(imageIcon);
    imageIcon.setImageObserver(iconLabel);
    JPanel loadingPane = new JPanel();
    loadingPane.add(iconLabel);

    JLabel okayPane = new JLabel(getString("label.everything.okay"));
    okayPane.setIcon(ApplicationIcons.ICON_VALIDATE_16);
    okayPane.setHorizontalAlignment(JLabel.CENTER);

    JLabel cancelledPane = new JLabel(getString("label.user.cancelled"));
    cancelledPane.setIcon(ApplicationIcons.ICON_CANCEL_16);
    cancelledPane.setHorizontalAlignment(JLabel.CENTER);

    this.collapsibleRootPanel = new CollapsibleRootPanel(CollapsibleRootPanel.FILL);
    this.collapsibleRootPanel.setBackground(null);
    JScrollPane scrollPane = new JScrollPane(this.collapsibleRootPanel);
    scrollPane.setMaximumSize(this.getSize());
    scrollPane.setViewportBorder(null);
    scrollPane.setBorder(null);
    scrollPane.getInsets().set(0,0,0,0);

    this.cards = new JPanel(new CardLayout());
    this.cards.add(loadingPane, LOADING);
    this.cards.add(okayPane, NO_ISSUES);
    this.cards.add(cancelledPane, CANCELLED);
    this.cards.add(scrollPane, HAS_ISSUES);
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
    refreshButton.setText(getString( "refresh.button"));
    refreshButton.setMnemonic(refreshButton.getText().charAt(0));
    refreshButton.setToolTipText(getString("refresh.tooltip"));
    refreshButton.setIcon(ApplicationIcons.ICON_REFRESH_16);
    refreshPanel.add(refreshButton);
    footer.add(refreshPanel, "grow, push, pad 0");
    this.add(footer, BorderLayout.SOUTH);
  }

  @Override
  public void refresh(ActionEvent event) {
    CardLayout cardLayoutManager = ((CardLayout) this.cards.getLayout());
    Set<Map.Entry<String, TablePanelComponent>> uiElements = this.uiComponents.entrySet();
    cardLayoutManager.show(this.cards, LOADING);
    new BusExplorerTask<Void>(ExceptionContext.Service) {
      @Override
      protected void doPerformTask() throws Exception {
        int i = 0;
        for (Map.Entry<String, TablePanelComponent> element : uiElements) {
          element.getValue().refresh(event);
          this.setProgressStatus(100*i/uiElements.size());
          i++;
        }
      }

      @Override
      protected void afterTaskUI() {
        if (getStatus()) {
          collapsibleRootPanel.removeAll();
          uiElements.stream().forEach(entry -> {
            TablePanelComponent<?> tableComponent = entry.getValue();
            tableComponent.setPreferredSize(new Dimension(400, 100));
            if (tableComponent.getElements().isEmpty() == false) {
              CollapsiblePanel collapsible = new CollapsiblePanel(entry.getKey());
              collapsible.getInsets().set(0, 10, 0, 10);
              collapsible.add(tableComponent);
              collapsibleRootPanel.add(collapsible);
            }
          });
          if (collapsibleRootPanel.getCollapsiblePanels().isEmpty()) {
            cardLayoutManager.show(ConsistencyReportPanel.this.cards, NO_ISSUES);
          } else {
            cardLayoutManager.show(ConsistencyReportPanel.this.cards, HAS_ISSUES);
          }
        } else {
            cardLayoutManager.show(ConsistencyReportPanel.this.cards, CANCELLED);
        }
      }
    }.execute(this.parentWindow, getString("waiting.title"), getString("waiting.msg"), 2, 0, true, false);
  }

  private String getString(String key) {
    return Language.get(this.getClass(), key);
  }

  /**
   * Remove o painel {@link CollapsiblePanel} referente aos dados da análise e, caso não haja mais nenhuma pendência,
   * atualiza o {@link CardLayout} para o painel com mensagem de sucesso.
   *
   * @param component componente da tabela que foi inserida no painel colapsável.
   *
   * @throws IllegalStateException caso o componente da tabela seja nulo, ou não possua um {@link CollapsiblePanel} na hierarquia.
   */
  private void discardCollapsiblePanel(TablePanelComponent component) {
    if (component == null || component.getParent() == null || component.getParent().getParent() == null ||
      !(component.getParent().getParent() instanceof CollapsiblePanel)) {
      throw new IllegalStateException("argument given doesn't respect the component hierarchy expected for "
        + this.getClass().getSimpleName());
    }
    CollapsiblePanel collapsiblePane = (CollapsiblePanel) component.getParent().getParent();
    if (this.collapsibleRootPanel != null) {
      this.collapsibleRootPanel.remove(collapsiblePane);
      // remove itself from JLayeredPane
      component.getParent().remove(component);
      this.collapsibleRootPanel.validate();
      this.collapsibleRootPanel.repaint();
      if (this.collapsibleRootPanel.getCollapsiblePanels().isEmpty()) {
        ((CardLayout) this.cards.getLayout()).show(this.cards, NO_ISSUES);
      }
    }
  }
}
