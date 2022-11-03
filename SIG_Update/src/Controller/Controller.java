package Controller;

import Model.InvoiceHeader;
import Model.HeaderModel;
import Model.InvoiceLine;
import Model.LineModel;
import View.HeaderDialog;
import View.SIGFrame;
import View.LineDialog;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.JFileChooser;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.Files;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;

public class Controller implements ActionListener, ListSelectionListener {

    private SIGFrame frame;
    private HeaderDialog headerDialog;
    private LineDialog lineDialog;

    public Controller(SIGFrame frame) {
        this.frame = frame;
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                ArrayList<InvoiceHeader> invoicesArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] headerParts = headerLine.split(",");
                    int invoiceNum = Integer.parseInt(headerParts[0]);
                    String invoiceDate = headerParts[1];
                    String customerName = headerParts[2];
                    InvoiceHeader invoice = new InvoiceHeader(invoiceNum, invoiceDate, customerName);
                    invoicesArray.add(invoice);
                }
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    for (String lineLine : lineLines) {
                        String lineParts[] = lineLine.split(",");
                        int invoiceNum = Integer.parseInt(lineParts[0]);
                        String itemName = lineParts[1];
                        double itemPrice = Double.parseDouble(lineParts[2]);
                        int count = Integer.parseInt(lineParts[3]);
                        InvoiceHeader inv = null;
                        for (InvoiceHeader invoice : invoicesArray) {
                            if (invoice.getNum() == invoiceNum) {
                                inv = invoice;
                                break;
                            }
                        }

                        InvoiceLine line = new InvoiceLine(itemName, itemPrice, count, inv);
                        inv.getLines().add(line);
                    }
                }
                frame.setInvoices(invoicesArray);
                HeaderModel invoicesTableModel = new HeaderModel(invoicesArray);
                frame.setInvoicesTableModel(invoicesTableModel);
                frame.getInvoiceTable().setModel(invoicesTableModel);
                frame.getInvoicesTableModel().fireTableDataChanged();  //use fireTableDataChanged to update JTable based on changes
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveFile() {
        ArrayList<InvoiceHeader> invoices = frame.getInvoices();
        String headers = "";
        String lines = "";
        for (InvoiceHeader invoice : invoices) {
            String headerCSV = invoice.getAsCSV();
            headers += headerCSV;
            headers += "\n";

            for (InvoiceLine line : invoice.getLines()) {
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter fileWritr = new FileWriter(headerFile);
                fileWritr.write(headers);
                fileWritr.flush();
                fileWritr.close();
                result = fc.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    FileWriter lfw = new FileWriter(lineFile);
                    lfw.write(lines);
                    lfw.flush();
                    lfw.close();
                }
            }
        } catch (Exception ex) {

        }
    }

    private void createNewInvoice() {
        headerDialog = new HeaderDialog(frame);
        headerDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedRow = frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1) {
            frame.getInvoices().remove(selectedRow);
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createNewItem() {
        lineDialog = new LineDialog(frame);
        lineDialog.setVisible(true);
    }

    private void deleteItem() {
        int selectedRow = frame.getLineTable().getSelectedRow();

        if (selectedRow != -1) {
            LineModel linesTableModel = (LineModel) frame.getLineTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void createInvoiceCancel() {
        headerDialog.setVisible(false);
        headerDialog.dispose();  //use dispose instead of Exit-on-close to close the open dialog only and keep the app open
        headerDialog = null;
    }

    private void createInvoice() {
        String date = headerDialog.getInvDateField().getText();
        String customer = headerDialog.getCustNameField().getText();
        int num = frame.getNextInvoiceNum();

        InvoiceHeader invoice = new InvoiceHeader(num, date, customer);
        frame.getInvoices().add(invoice);
        frame.getInvoicesTableModel().fireTableDataChanged();
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }

    private void createNewLine() {
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            InvoiceHeader invoice = frame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item, price, count, invoice);
            invoice.getLines().add(line);
            LineModel linesTableModel = (LineModel) frame.getLineTable().getModel();
            linesTableModel.fireTableDataChanged();
            frame.getInvoicesTableModel().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void cxlNewLine() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
        switch (actionCommand) {
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create New Item":
                createNewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "createInvoiceCancel":
                createInvoiceCancel();
                break;
            case "createInvoiceOK":
                createInvoice();
                break;
            case "createLineOK":
                createNewLine();
                break;
            case "createLineCancel":
                cxlNewLine();
                break;
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1) {
            InvoiceHeader currentInvoice = frame.getInvoices().get(selectedIndex);
            frame.getInvoiceNumLabel().setText("" + currentInvoice.getNum());
            frame.getInvoiceDateLabel().setText(currentInvoice.getDate());
            frame.getCustomerNameLabel().setText(currentInvoice.getCustomer());
            frame.getInvoiceTotalLabel().setText("" + currentInvoice.getInvoiceTotal());
            LineModel linesTableModel = new LineModel(currentInvoice.getLines());
            frame.getLineTable().setModel(linesTableModel);
            linesTableModel.fireTableDataChanged();
        }
    }
}
