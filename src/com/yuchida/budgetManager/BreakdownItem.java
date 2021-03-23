package com.yuchida.budgetManager;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class BreakdownItem {
    private int _id;
    private String name;
    private double amount;
    private int categoryID;
    private String categoryName;
    private Label nameLabel;
    private Text totalAmount;
    private TextField amountSpent;

    public BreakdownItem(String name){
        this.name = name;
        amount = 0;
    }

    public BreakdownItem(String name, double amount){
        this.name = name;
        this.amount = amount;
    }

    public BreakdownItem(String name, double amount,  int categoryID){
        this(name, amount);

        this.categoryID = categoryID;
    }

    public BreakdownItem(int id, String name, double amount,  int categoryID){
        this(name, amount, categoryID);

        this._id = id;
    }

    public void addExpense(double spending){
        amount += spending;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public int getCategoryID(){ return this.categoryID; }

    public int get_id(){return this._id;}

    public void set_id(int id){this._id = id; }

    public void createNewItemPanel(){
        nameLabel = new Label(name);
        totalAmount = new Text(Double.toString(amount));
        amountSpent = new TextField();
    }

    @Override
    public String toString() {
        return this.name +", ￥" +this.amount + " | ";
    }
}
