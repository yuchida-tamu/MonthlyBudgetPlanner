package com.yuchida.budgetManager.model;

import com.yuchida.budgetManager.BreakdownItem;
import com.yuchida.budgetManager.BudgetCategory;
import com.yuchida.budgetManager.MonthlyBudget;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class DataExport {
    //use data from monthlyBudgets view table
    //id(id:1), categoryID(_id), category (String), goal, spent, month, itemName (name), amount, categoryID(category)
    private static DataExport instance = new DataExport();
    private String filename;
    //private final String FILE_PATH = "C:\\Dev\\MonthlyBudgetManager\\";
   // public static final String CSV_EXTENSION = ".csv";

    private DataExport(){}
    public static DataExport getInstance(){ return instance; }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void exportAllToCSV  (ObservableList<MonthlyBudget> list)throws IOException {
        FileWriter fw = new FileWriter(filename);
        PrintWriter pw= new PrintWriter(new BufferedWriter(fw));
        ObservableMap<String, BudgetCategory> map;
        int id, goal, spent, amount, itemID;
        String category, itemName, month;

        //header

        pw.print("_id");
        pw.print(",");
        pw.print("category");
        pw.print(",");
        pw.print("goal");
        pw.print(",");
        pw.print("spent");
        pw.print(",");
        pw.print("month");
        pw.print(",");
        pw.print("item_id");
        pw.print(",");
        pw.print("item_name");
        pw.print(",");
        pw.print("amount");
        pw.println();

        for(MonthlyBudget budget : list){
            map = budget.getCategories();

            for(Map.Entry entry : map.entrySet()){
                BudgetCategory budgetCategory = (BudgetCategory) entry.getValue();
                id = budgetCategory.get_id();
                category = budgetCategory.getName();
                goal = (int) budgetCategory.getGoal();
                spent = (int) budgetCategory.getCurrentTotal();
                month = budget.getMonth().toString();
                for(Object e : budgetCategory.getBreakdownItems().entrySet()){
                    Map.Entry<String, BreakdownItem> set = (Map.Entry) e;
                    BreakdownItem item = set.getValue();
                    itemID = item.get_id();
                    itemName = item.getName();
                    amount = (int)item.getAmount();

                    //write data to the file
                    //id(id:1), category (String), goal, spent, month, itemID, itemName (name), amount
                    pw.print(id);
                    pw.print(",");
                    pw.print(category);
                    pw.print(",");
                    pw.print(goal);
                    pw.print(",");
                    pw.print(spent);
                    pw.print(",");
                    pw.print(month);
                    pw.print(",");
                    pw.print(itemID);
                    pw.print(",");
                    pw.print(itemName);
                    pw.print(",");
                    pw.print(amount);
                    pw.println();
                }
            }
        }

        pw.close();
        System.out.println("All data is exported to: " + filename);
    }

    public void exportThisMonthToCSV(MonthlyBudget budget) throws IOException {
        ObservableMap<String, BudgetCategory> map = budget.getCategories();
        FileWriter fw = new FileWriter( filename );
        PrintWriter pw= new PrintWriter(new BufferedWriter(fw));
        int id, goal, spent, amount, itemID;
        String category, itemName, month;

        //header

        pw.print("_id");
        pw.print(",");
        pw.print("category");
        pw.print(",");
        pw.print("goal");
        pw.print(",");
        pw.print("spent");
        pw.print(",");
        pw.print("month");
        pw.print(",");
        pw.print("item_id");
        pw.print(",");
        pw.print("item_name");
        pw.print(",");
        pw.print("amount");
        pw.println();

        for(Map.Entry entry : map.entrySet()){
            BudgetCategory budgetCategory = (BudgetCategory) entry.getValue();
            id = budgetCategory.get_id();
            category = budgetCategory.getName();
            goal = (int) budgetCategory.getGoal();
            spent = (int) budgetCategory.getCurrentTotal();
            month = budget.getMonth().toString();
            for(Object e : budgetCategory.getBreakdownItems().entrySet()){
                Map.Entry<String, BreakdownItem> set = (Map.Entry) e;
                BreakdownItem item = set.getValue();
                itemID = item.get_id();
                itemName = item.getName();
                amount = (int)item.getAmount();

                //write data to the file
                //id(id:1), category (String), goal, spent, month, itemID, itemName (name), amount
                pw.print(id);
                pw.print(",");
                pw.print(category);
                pw.print(",");
                pw.print(goal);
                pw.print(",");
                pw.print(spent);
                pw.print(",");
                pw.print(month);
                pw.print(",");
                pw.print(itemID);
                pw.print(",");
                pw.print(itemName);
                pw.print(",");
                pw.print(amount);
                pw.println();
            }
        }

        pw.close();
        System.out.println("Monthly Budget data is exported to: " + filename);

    }

}
