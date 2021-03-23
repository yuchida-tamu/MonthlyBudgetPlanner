package com.yuchida.budgetManager;

import com.yuchida.budgetManager.model.BudgetDataManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.chart.PieChart;

import java.io.IOException;
import java.time.Month;
import java.util.List;
import java.util.Map;

public class BudgetCategory {
    private int _id;
    private String name;
    private double currentTotal = 0;
    private double goal = 0;
    private String filename;
    private ObservableMap<String, BreakdownItem> breakdownItems = FXCollections.observableHashMap(); //= new Obs<String, BreakdownItem>();
    private PieChartGenerator pieChartGenerator;
    private CategoryData categoryData;

    public BudgetCategory(String name){
        this.name = name;
        this.pieChartGenerator = new PieChartGenerator();
        //initially there is only one item (uncategorized)
        //addItemToListAndChartData("uncategorized", 0);


        this.currentTotal = getCurrentTotal();

    }
    public BudgetCategory(int id, String name, int goal, int spent, ObservableMap<String, BreakdownItem> items){
        this(name);
        this._id = id;
        this.goal = goal;
        this.currentTotal = spent;
        this.breakdownItems = items;
        this.addItemToListAndChartData(items);
    }

    private void addItemToListAndChartData(String itemName, double amount){
        breakdownItems.put(itemName, new BreakdownItem(itemName));
        PieChart.Data data = new PieChart.Data(itemName, getPercentage(amount));
        this.pieChartGenerator.addToDataList(itemName, data);
    }

    private void addItemToListAndChartData(BreakdownItem item){
        breakdownItems.put(item.getName(), item);
        this.updateCurrentTotal();
        PieChart.Data data = new PieChart.Data(item.getName(), getPercentage(item.getAmount()));
        this.pieChartGenerator.addToDataList(item.getName(), data);
    }

    private void addItemToListAndChartData(ObservableMap<String, BreakdownItem> map){
        this.getCurrentTotal();
        for(Map.Entry entry : map.entrySet()){
            BreakdownItem item = (BreakdownItem)entry.getValue();
            PieChart.Data data = new PieChart.Data(item.getName(), getPercentage(item.getAmount()));
            this.pieChartGenerator.addToDataList(item.getName(), data);
        }
    }

    public void loadAndUpdateCategory(){
        this.updateCurrentTotal();
        for(Object obj : breakdownItems.entrySet()){
            Map.Entry<String, BreakdownItem> entry = (Map.Entry<String, BreakdownItem>) obj;
            BreakdownItem item = entry.getValue();
            PieChart.Data data = new PieChart.Data(item.getName(), getPercentage(item.getAmount()));
            //if the data list already has the data, only update the value
            if(this.getPieChartGenerator().getDataList().containsKey(item.getName())){
                this.getPieChartGenerator().updateDataItem(item.getName(), getPercentage(item.getAmount()));
            } else {
                this.pieChartGenerator.addToDataList(item.getName(), data);
            }

        }
    }

    public BudgetCategory(String name, Month month){
        this(name);
        this.filename = month.toString() + name + ".txt";
       // this.categoryData = new CategoryData(this.filename);
    }

    public int get_id(){ return this._id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BreakdownItem getItem(String key){
        return breakdownItems.get(key);
    }

    public void addBreakdownItem(BreakdownItem item){
        addItemToListAndChartData(item);
    }

    public ObservableMap getBreakdownItems() {
        return this.breakdownItems;
    }

    public PieChartGenerator getPieChartGenerator() {
        return pieChartGenerator;
    }

    public void setBreakdownItems(ObservableMap breakdownItems) {
        this.breakdownItems = breakdownItems;
    }



    public double getCurrentTotal() {
        double sum = 0;
        for(Map.Entry entry : breakdownItems.entrySet() ){
            sum += ((BreakdownItem)entry.getValue()).getAmount();
        }

        return sum;
    }

    public void updateCurrentTotal(){
        this.currentTotal = getCurrentTotal();
    }

    public void updateCurrentTotal(double currentTotal) {
        this.currentTotal = currentTotal;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public void setUncategorized(){
        return;
    }

    public double getUncategorized(){
        return breakdownItems.get("uncategorized").getAmount();
    }

    public int getPercentage(String category){
        BreakdownItem item = (BreakdownItem) breakdownItems.get(category);
        try {
            return (int) Math.round(item.getAmount() /currentTotal * 100);
        } catch (ArithmeticException e){
            System.out.println("cant devide by 0");
            return 0;
        }


    }

    public int getPercentage(double amount){
        return (int) Math.round((amount/currentTotal * 100));
    }


    public void saveCategoryData() throws IOException {
        //categoryData.storeBudgets(breakdownItems);
        //CategoryData.saveData(this.filename, breakdownItems);
        //TODO saving logic
        System.out.println("saving");
        BudgetDataManager.getInstance().updateBreakdownItems(breakdownItems);
    }

    public void loadCategoryDate() throws IOException {
        breakdownItems = categoryData.getBreakdownItems();

    }


    public void updateAllItemPercentage() {
        List<PieChart.Data> list = this.pieChartGenerator.getChartData();
        Map<String, Integer> map = this.pieChartGenerator.getDataList();

        for(Map.Entry<String, Integer> entry : map.entrySet()){
            int index = entry.getValue();
            String key = entry.getKey();
            PieChart.Data chartData = list.get(index);
            double updatedAmount = this.breakdownItems.get(key).getAmount();
            chartData.setPieValue( getPercentage(updatedAmount));
        }
    }

    public String printInfo(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry entry : breakdownItems.entrySet()){
            BreakdownItem item = (BreakdownItem)entry.getValue();

            sb.append(item.toString());
        }
        return sb.toString();
    }

}
