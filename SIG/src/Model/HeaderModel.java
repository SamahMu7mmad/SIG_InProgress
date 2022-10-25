/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Dell
 */
public class HeaderModel extends AbstractTableModel{
    private ArrayList<InvoiceHeader> data;
    private String[] columns = {"No.", "Customer Name", "Date", "Total"}; //will need to implement a methode to calculate Total 

    public HeaderModel(ArrayList<InvoiceHeader> data) {
        this.data = data;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader newhdr = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return newhdr.getInvNum();
            case 1:
                return newhdr.getCustomerName();
            case 2:
                return newhdr.getDate();
        }
        return "";
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
}
