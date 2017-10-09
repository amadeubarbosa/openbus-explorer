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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tecgraf.openbus.services.governance.v1_0.Consumer;
import tecgraf.openbus.services.governance.v1_0.Contract;
import tecgraf.openbus.services.governance.v1_0.Provider;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
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
    XSSFWorkbook wb = new XSSFWorkbook();
    XSSFFont headerFont = wb.createFont();
    headerFont.setBold(true);
    headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    XSSFCellStyle headStyle = wb.createCellStyle();
    headStyle.setFont(headerFont);
    headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    headStyle.setAlignment(CellStyle.ALIGN_CENTER);

    Sheet integrationsSheet = wb.createSheet(getString("sheet.name.integration"));
    Sheet adjacencySheet = wb.createSheet(getString("sheet.name.adjacency"));
    Sheet contactsSheet = wb.createSheet(getString("sheet.name.contacts"));

    // adjacency
    adjacencySheet.createRow(0);
    // contacts
    Row contactsSheetHeader = contactsSheet.createRow(0);
    String[] contactsColumns = new String[]{
      getString("sheet.contacts.system"),
      getString("sheet.contacts.system.appcode"),
      getString("sheet.contacts.system.manageroffice"),
      getString("sheet.contacts.system.manager"),
      getString("sheet.contacts.system.supportoffice"),
      getString("sheet.contacts.system.support")
    };
    for (int c = 0; c < contactsColumns.length; c++) {
      Cell cell = contactsSheetHeader.createCell(c);
      cell.setCellValue(contactsColumns[c]);
      cell.setCellStyle(headStyle);
    }
    // integrations
    Row integrationsSheetHeader = integrationsSheet.createRow(0);
    String[] columns = new String[]{
      getString("sheet.integration.system"),
      getString("sheet.integration.system.manageroffice"),
      getString("sheet.integration.contract"),
      getString("sheet.integration.provider"),
      getString("sheet.integration.type")
    };
    for (int c = 0; c < columns.length; c++) {
      Cell cell = integrationsSheetHeader.createCell(c);
      cell.setCellValue(columns[c]);
      cell.setCellStyle(headStyle);
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
            Cell hcell = adjacencySheet.getRow(0).createCell(i+1);
            hcell.setCellValue(matrix.get(i));
            hcell.setCellStyle(headStyle);
            Cell vcell = adjacencySheet.createRow(i+1).createCell(0);
            vcell.setCellValue(matrix.get(i));
            vcell.setCellStyle(headStyle);
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
              row.createCell(1).setCellValue(integration.consumer().manageroffice());
              row.createCell(2).setCellValue(contract.name());
              row.createCell(3).setCellValue(integration.provider().name());
              if (providers.stream().anyMatch(provider -> provider.name().equals(integration.consumer().name()))) {
                row.createCell(4).setCellValue(getString("sheet.integration.type.both"));
              } else {
                row.createCell(4).setCellValue(getString("sheet.integration.type.onlyconsumer"));
              }
            });
          });
          consumers.forEach(consumer -> {
              String consumerName = consumer.name();
              // contacts sheet
              Row contactRow = contactsSheet.createRow(matrix.indexOf(consumerName) + 1);
              contactRow.createCell(0).setCellValue(consumerName);
              contactRow.createCell(1).setCellValue(consumer.code());
              contactRow.createCell(2).setCellValue(consumer.manageroffice());
              contactRow.createCell(3).setCellValue(String.join(",", consumer.manager()));
              contactRow.createCell(4).setCellValue(consumer.supportoffice());
              contactRow.createCell(5).setCellValue(String.join(",", consumer.support()));
          });
          providers.forEach(provider -> {
            String providerName = provider.name();
            // contacts sheet
            Row contactRow = contactsSheet.createRow(matrix.indexOf(providerName)+1);
            contactRow.createCell(0).setCellValue(providerName);
            contactRow.createCell(1).setCellValue(provider.code());
            contactRow.createCell(2).setCellValue(provider.manageroffice());
            contactRow.createCell(3).setCellValue(String.join(",", provider.manager()));
            contactRow.createCell(4).setCellValue(provider.supportoffice());
            contactRow.createCell(5).setCellValue(String.join(",", provider.support()));
            // adjacency sheet
            Arrays.stream(provider.contracts()).forEach(contract -> {
              int contractIndex = matrix.indexOf(contract.name());
              int providerIndex = matrix.indexOf(providerName);
              adjacencySheet.getRow(contractIndex+1).getCell(providerIndex+1).setCellValue(1);
            });
            // integrations sheet
            if (!visited.contains(providerName)) {
              Row row = integrationsSheet.createRow(integrationsSheet.getLastRowNum()+1);
              row.createCell(0).setCellValue(providerName);
              row.createCell(1).setCellValue(provider.manageroffice());
              row.createCell(4).setCellValue(getString("sheet.integration.type.onlyprovider"));
            }
          });
          for (int c = 0; c < contactsColumns.length; c++) {
            contactsSheet.autoSizeColumn(c);
          }
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
              protected JDialog createDialog(Component var1) throws HeadlessException {
                JDialog dialog = super.createDialog(var1);
                dialog.setMinimumSize(new Dimension(800, 600));
                return dialog;
              }
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
            chooser.setFileFilter(new FileNameExtensionFilter(getString("export.extensionfilter.name"), "xlsx"));
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
