/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


/**
 *
 * @author Dell
 */
public class InvoiceLine {
    private String itemName;
    private int count;
    private double price;
    private InvoiceHeader inv;

    public InvoiceLine(String name, int count, double price, InvoiceHeader inv) {
        this.itemName = itemName;
        this.count = count;
        this.price = price;
        this.inv = inv;
    }


    public double getTotal(){
        return count * price;
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public InvoiceHeader getInv() {
        return inv;
    }

    public void setInv(InvoiceHeader inv) {
        this.inv = inv;
    }
    
    public String getAsCSv(){
        return inv.getInvNum()+ " , "+itemName+ " , "+ price+" , "+count;
    }
    
}
