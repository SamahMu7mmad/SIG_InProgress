/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Dell
 */
public class InvoiceHeader {
    private int invNum;
    private Date date;
    private String customerName;
    private ArrayList<InvoiceLine> lines;

    public InvoiceHeader(int invNum, Date date, String name) {
        this.invNum = invNum;
        this.date = date;
        this.customerName = name;
    }

    public double getTotal(){
        double total = 0.0;
        for (int i =0; i<lines.size();i++){
            InvoiceLine line = lines.get(i);
            total += line.getTotal();
        }
        return total;
    }
    public ArrayList<InvoiceLine> getLines() {
        return lines;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getInvNum() {
        return invNum;
    }

    public void setInvNum(int invNum) {
        this.invNum = invNum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    

    @Override
    public String toString() {
        return "InvoiceHeader{" + "invNum=" + invNum + ", date=" + date + ", name=" + customerName + '}';
    }
    
}
