package busexplorer.panel.integrations;

import busexplorer.Application;
import busexplorer.ApplicationIcons;
import busexplorer.desktop.dialog.ExceptionDialog;
import busexplorer.desktop.dialog.InputDialog;
import busexplorer.exception.handling.ExceptionContext;
import busexplorer.panel.ActionType;
import busexplorer.panel.OpenBusAction;
import busexplorer.utils.BusExplorerTask;
import busexplorer.utils.Language;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tecgraf.openbus.services.governance.v1_0.Consumer;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class IntegrationExportToXLSAction extends OpenBusAction<IntegrationWrapper> {

  public IntegrationExportToXLSAction(Window parentWindow) {
    super(parentWindow);
    putValue(SHORT_DESCRIPTION, getString("tooltip"));
    putValue(SMALL_ICON, ApplicationIcons.ICON_SPREADSHEET_16);
  }

  @Override
  public ActionType getActionType() {
    return ActionType.OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Workbook wb = new XSSFWorkbook();
    Sheet adjacencySheet = wb.createSheet("adjacency matrix");
    adjacencySheet.createRow(0);
    Sheet integrationsSheet = wb.createSheet("integrations");
    Row label = integrationsSheet.createRow(0);
    String[] columns = new String[]{"system-name", "contract", "manager-office", "provider", "type"};
    for (int c = 0; c < columns.length; c++) {
      Cell cell = label.createCell(c);
      cell.setCellValue(columns[c]);
    }
    // to mark providers already listed as integrations
    List<String> visited = new ArrayList<String>();
    // all labels necessary for adjacency matrix
    Vector<String> matrix = new Vector<String>();

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.Service) {

        @Override
        protected void doPerformTask() throws Exception {
          List<Provider> providers = Application.login().extension.getProviders();
          List<Consumer> consumers = Application.login().extension.getConsumers();
          List<Contract> contracts = Application.login().extension.getContracts();
          matrix.addAll(providers.parallelStream().map(Provider::name).collect(Collectors.toList()));
          matrix.addAll(consumers.parallelStream().map(Consumer::name).collect(Collectors.toList()));
          matrix.addAll(contracts.parallelStream().map(Contract::name).collect(Collectors.toList()));

          for (int i = 0; i < matrix.size(); i++) {
            adjacencySheet.getRow(0).createCell(i+1).setCellValue(matrix.get(i));
            adjacencySheet.createRow(i+1).createCell(0).setCellValue(matrix.get(i));
            for (int j = 0; j < matrix.size(); j++) {
              adjacencySheet.getRow(i+1).createCell(j + 1).setCellValue(0);
            }
          }

          Application.login().extension.getIntegrations().forEach(integration -> {
            Arrays.stream(integration.contracts()).forEach(contract -> {
              // adjacency sheet
              int consumerIndex = matrix.indexOf(integration.consumer().name());
              int providerIndex = matrix.indexOf(integration.provider().name());
              int contractIndex = matrix.indexOf(contract.name());
              adjacencySheet.getRow(consumerIndex+1).getCell(contractIndex+1).setCellValue(1);
              adjacencySheet.getRow(contractIndex+1).getCell(providerIndex+1).setCellValue(1);
              // integrations sheet
              Row row = integrationsSheet.createRow(integrationsSheet.getLastRowNum()+1);
              visited.add(integration.consumer().name());
              row.createCell(0).setCellValue(integration.consumer().name());
              row.createCell(1).setCellValue(contract.name());
              row.createCell(2).setCellValue(integration.consumer().manageroffice());
              row.createCell(3).setCellValue(integration.provider().name());
              if (providers.stream().anyMatch(provider -> provider.name().equals(integration.consumer().name()))) {
                row.createCell(4).setCellValue("Consumer/Provider");
              } else {
                row.createCell(4).setCellValue("Consumer");
              }
            });
          });
          providers.forEach(provider -> {
            // adjacency sheet
            Arrays.stream(provider.contracts()).forEach(contract -> {
              int contractIndex = matrix.indexOf(contract.name());
              int providerIndex = matrix.indexOf(provider.name());
              adjacencySheet.getRow(contractIndex+1).getCell(providerIndex+1).setCellValue(1);
            });
            // integrations sheet
            if (!visited.contains(provider.name())) {
              Row row = integrationsSheet.createRow(integrationsSheet.getLastRowNum()+1);
              row.createCell(0).setCellValue(provider.name());
              row.createCell(2).setCellValue(provider.manageroffice());
              row.createCell(4).setCellValue("Provider");
            }
          });
          for (int c = 0; c < columns.length; c++) {
            integrationsSheet.autoSizeColumn(c);
          }
          for (int c = 0; c < matrix.size(); c++) {
            adjacencySheet.autoSizeColumn(c);
          }
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            JFileChooser chooser = new JFileChooser() {
              public void approveSelection() {
                if (getSelectedFile().exists() && getDialogType() == SAVE_DIALOG) {
                  if (InputDialog.showConfirmDialog(parentWindow,
                    getString("replace.message"),
                    getString("replace.title")) != JOptionPane.YES_OPTION) {
                    return;
                  }
                }
                super.approveSelection();
              }
            };
            chooser.setFileFilter(new FileNameExtensionFilter("Microsoft Excel Workbook / Open XML Spreadsheet", "xlsx"));
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int status = chooser.showSaveDialog(this.parentWindow);
            if (status == JFileChooser.APPROVE_OPTION) {
              String filename = chooser.getSelectedFile().getAbsolutePath();
              if (!chooser.getSelectedFile().exists() && !filename.endsWith(".xlsx")) {
                filename += ".xlsx";
              }
              try {
                FileOutputStream fileOut = new FileOutputStream(filename);
                wb.write(fileOut);
                fileOut.flush();
                fileOut.close();
                JOptionPane.showMessageDialog(this.parentWindow,
                  Language.get(IntegrationExportToXLSAction.class,"export.success", filename),
                  getString("export.success.title"), JOptionPane.INFORMATION_MESSAGE);
              } catch (Exception e) {
                ExceptionDialog.createDialog(this.parentWindow, getString("export.error"), e, "").setVisible(true);
              }
            }
          }
        }
      };
    task.execute(parentWindow, getString("waiting.title"),
      getString("waiting.msg"), 2, 0);
  }
}
