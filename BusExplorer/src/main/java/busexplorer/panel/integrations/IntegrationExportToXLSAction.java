package busexplorer.panel.integrations;

import busexplorer.Application;
import busexplorer.desktop.dialog.ExceptionDialog;
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

public class IntegrationExportToXLSAction extends OpenBusAction<IntegrationWrapper> {

  public IntegrationExportToXLSAction(Window parentWindow) {
    super(parentWindow);
  }

  @Override
  public ActionType getActionType() {
    return ActionType.OTHER;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Workbook wb = new XSSFWorkbook();
    Sheet integrationsSheet = wb.createSheet("integrations");
    Row label = integrationsSheet.createRow(0);
    String[] columns = new String[]{"system-name", "contract", "manager-office", "provider", "type"};
    for (int c = 0; c < columns.length; c++) {
      Cell cell = label.createCell(c);
      cell.setCellValue(columns[c]);
    }
    // to mark providers already listed as integrations
    List<String> visited = new ArrayList<String>();

    BusExplorerTask<Void> task =
      new BusExplorerTask<Void>(ExceptionContext.Service) {

        @Override
        protected void doPerformTask() throws Exception {
          List<Provider> providers = Application.login().extension.getProviders();
          Application.login().extension.getIntegrations().forEach(integration -> {
            Arrays.stream(integration.contracts()).forEach(contract -> {
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
        }

        @Override
        protected void afterTaskUI() {
          if (getStatus()) {
            JFileChooser chooser = new JFileChooser() {
              public void approveSelection() {
                if (getSelectedFile().exists() && getDialogType() == SAVE_DIALOG) {
                  int result = JOptionPane.showConfirmDialog(this,
                    getString("replace.message"), getString("replace.title"),
                    JOptionPane.YES_NO_OPTION);
                  switch (result) {
                    case JOptionPane.YES_OPTION:
                      super.approveSelection();
                      return;
                    default:
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
