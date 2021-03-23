package com.yuchida.budgetManager;

import com.yuchida.budgetManager.model.BudgetDB;
import com.yuchida.budgetManager.model.BudgetDataManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Monthly Budget Manager");
        primaryStage.setScene(new Scene(root, 850, 650));
        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        try{
            String[] categoryName = {"food", "utilities", "education", "entertainment", "saving", "miscellaneous"};
            //BudgetData.getInstance().loadBudgets();
            startDB();
            BudgetDataManager.getInstance().loadBudgetList();

        } catch (Exception e){
            System.out.println("NoDataFile or error in the loading process");
            //initialize data
            //initializeData();
        }
    }

    @Override
    public void stop() throws Exception {
        try{
            //BudgetData.getInstance().storeBudgets();
        } catch (Exception e){
            System.out.println("something is wrong with storeData method");
        }


        //DataExport.getInstance().exportAllToCSV(BudgetDataManager.getInstance().getData());
        //DataExport.getInstance().exportThisMonthToCSV(BudgetDataManager.getInstance().getData().get(4));
        BudgetDB.getInstance().close();
    }

    private void startDB(){
        if(BudgetDB.getInstance().open()){
            //initializeDB();
            System.out.println("Connected to the database successfully");
        } else {
            System.out.println("Couldn't connect to the database");
        }
    }

    private void initializeDB(){
       if (BudgetDB.getInstance().open()){
           System.out.println("Database created");
           BudgetDB.getInstance().initializeDB();
           BudgetDB.getInstance().initializeMonthsAndBudgets();
           BudgetDB.getInstance().initializeViewTable();
           for(MonthlyBudget budget : BudgetDB.getInstance().getBudgetsAlt()){
               System.out.println(budget.toString());
           }

       }else{
           System.out.println("Database not created");
       }
    }

//    private void initializeData(){
//        MonthlyBudget jan = new MonthlyBudget(Month.JANUARY);
//        System.out.println( jan.toString());
//        MonthlyBudget feb = new MonthlyBudget(Month.FEBRUARY);
//        MonthlyBudget mar = new MonthlyBudget(Month.MARCH);
//        MonthlyBudget apr = new MonthlyBudget(Month.APRIL);
//        MonthlyBudget may = new MonthlyBudget(Month.MAY);
//        MonthlyBudget jun = new MonthlyBudget(Month.JUNE);
//        MonthlyBudget jul = new MonthlyBudget(Month.JULY);
//        MonthlyBudget aug = new MonthlyBudget(Month.AUGUST);
//        MonthlyBudget sep = new MonthlyBudget(Month.SEPTEMBER);
//        MonthlyBudget oct = new MonthlyBudget(Month.OCTOBER);
//        MonthlyBudget nov = new MonthlyBudget(Month.NOVEMBER);
//        MonthlyBudget dec = new MonthlyBudget(Month.DECEMBER);
//
//        ObservableList<MonthlyBudget> list = BudgetData.getInstance().getBudgets();
//        list.add(jan);
//        list.add(feb);
//        list.add(mar);
//        list.add(apr);
//        list.add(may);
//        list.add(jun);
//        list.add(jul);
//        list.add(aug);
//        list.add(sep);
//        list.add(oct);
//        list.add(nov);
//        list.add(dec);
//
//        //sort a list to monthly order
//        SortedList<MonthlyBudget> sortedList = new SortedList<MonthlyBudget>(list, new Comparator<MonthlyBudget>() {
//            @Override
//            public int compare(MonthlyBudget o1, MonthlyBudget o2) {
//                return o1.getMonth().compareTo(o2.getMonth());
//            }
//        });
//
//        try{
//            BudgetData.getInstance().storeBudgets();
//        } catch (IOException e){
//            System.out.println("something is wrong with storeData method");
//        }
//
//        System.out.println("initialized data");
//    }
}
