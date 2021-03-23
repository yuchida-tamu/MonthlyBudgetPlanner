package com.yuchida.budgetManager.model;

import com.yuchida.budgetManager.BreakdownItem;
import com.yuchida.budgetManager.BudgetCategory;
import com.yuchida.budgetManager.BudgetData;
import com.yuchida.budgetManager.MonthlyBudget;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.Map;

//The class manages data instances to be used
//data is fetched from DB by using Budget DB
public class BudgetDataManager {
    private static ObservableList<MonthlyBudget> budgets;
    private BudgetDB db = BudgetDB.getInstance();

    private static BudgetDataManager instance = new BudgetDataManager();
    private BudgetDataManager(){}

    public static BudgetDataManager getInstance(){
        return instance;
    }
    //fetch all the budget records from the tables
    //create instance of each budget
    //store it to the budgets variable
    public void loadBudgetList(){
        budgets = db.getBudgetsAlt();
        if(!budgets.isEmpty()){
            System.out.println("Data is loaded from DB successfully");
        }
    }

    //Insert a new record to the details table
    public void saveBreakdownItem(BreakdownItem item){
        int generatedID = db.insertNewDetails(item.getName(), (int) item.getAmount(), item.getCategoryID());
        //set the new item's id
        item.set_id(generatedID);
        System.out.println("Newly generated key: " + item.get_id());
    }

    public void updateBreakdownItems(ObservableMap<String, BreakdownItem> breakdownItems){
        for(Map.Entry entry : breakdownItems.entrySet()){
            BreakdownItem item = (BreakdownItem)entry.getValue();
            System.out.println("saving : " + item.getName() );
            db.updateDetails((int)item.getAmount(), item.get_id());
        }
    }

    public ObservableList<MonthlyBudget> getData(){
        return budgets;
    }

    public void updateBudgetGoal(ObservableMap<String, BudgetCategory> categories) {
        for(Map.Entry entry : categories.entrySet()){
            BudgetCategory item = (BudgetCategory)entry.getValue();
            System.out.println("updating : " + item.getName() );
            db.updateBudgets((int)item.getGoal(), item.get_id());
        }
    }
    //update all records
    public void updateBudgetGoal(){
        for(MonthlyBudget budget : budgets){
            updateBudgetGoal(budget.getCategories());
        }
    }

    //delete an item from the details table
    public void deleteItemFromDetails(BreakdownItem item){
        db.deleteItemFromDetails(item.get_id());
       // int categoryID = db.getDeletedItemsCategoryID(item.get_id());
        //reload the updated db
        loadBudgetList();
    }

    public void getCategoryID(int i){
        db.getDeletedItemsCategoryID(i);
    }
}
