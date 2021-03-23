package com.yuchida.budgetManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.time.Month;
import java.util.List;

public class MonthlyBudget  {
    private int _id;

    private double food = 0;
    private double entertainment = 0;
    private double utilities = 0;
    private double education = 0;
    private double miscellaneous = 0;
    private double saving = 0;
    private double total;


    private double foodCurrent = 0;
    private double entertainmentCurrent = 0;
    private double utilitiesCurrent = 0;
    private double educationCurrent = 0;
    private double miscellaneousCurrent = 0;
    private double savingCurrent = 0;
    private double currentTotal;
    private double remaining;

    private BudgetCategory foodBC;
    private BudgetCategory entertainmentBC;
    private BudgetCategory educationBC;
    private BudgetCategory utilitiesBC;
    private BudgetCategory miscellaneousBC;
    private BudgetCategory savingBC;

    private ObservableMap<String, BudgetCategory> categories = FXCollections.observableHashMap();


    private Month month;




    public MonthlyBudget(Month month){

        this.month = month;
        this.total = 0;
        initializeCategoryList();

    }


    public MonthlyBudget(double entertainment, double utilities, double education, double miscellaneous, double saving, double food, Month month) {
        initializeCategoryList();

        this.entertainment = entertainment;
        this.utilities = utilities;
        this.education = education;
        this.miscellaneous = miscellaneous;
        this.saving = saving;
        this.month = month;
        this.food = food;

        this.entertainment = this.entertainmentBC.getGoal();
        this.utilities = this.utilitiesBC.getGoal();
        this.education = this.educationBC.getGoal();
        this.miscellaneous = this.miscellaneousBC.getGoal();
        this.saving = this.savingBC.getGoal();
        this.food = this.foodBC.getGoal();

        this.entertainmentCurrent = this.entertainmentBC.getGoal();
        this.utilitiesCurrent = this.utilitiesBC.getCurrentTotal();
        this.educationCurrent = this.educationBC.getCurrentTotal();
        this.miscellaneousCurrent = this.miscellaneousBC.getCurrentTotal();
        this.savingCurrent = this.savingBC.getCurrentTotal();
        this.foodCurrent = this.foodBC.getCurrentTotal();



        this.total = this.education +
                this.entertainment +
                this.utilities +
                this.miscellaneous +
                this.saving;
    }


    public MonthlyBudget(Month month, double food, double utilities, double education, double entertainment, double miscellaneous, double saving,
                         double currentFood, double currentUtilities, double currentEducation, double currentEntertainment, double currentMiscellaneous,
                         double currentSaving)
    {
        //this(entertainment, utilities, education, miscellaneous, saving, food, month);
        this.month = month;
        this.food = food;
        this.utilities = utilities;
        this.education = education;
        this.entertainment = entertainment;
        this.miscellaneous = miscellaneous;
        this.saving = saving;
        this.foodCurrent = currentFood;
        this.utilitiesCurrent = currentUtilities;
        this.educationCurrent = currentEducation;
        this.entertainmentCurrent = currentEntertainment;
        this.miscellaneousCurrent = currentMiscellaneous;
        this.savingCurrent = currentSaving;

        initializeCategoryList();
    }

    //assign _id with ID from a database record
    public MonthlyBudget (int id, Month month, double food, double utilities, double education, double entertainment, double miscellaneous, double saving,
                          double currentFood, double currentUtilities, double currentEducation, double currentEntertainment, double currentMiscellaneous,
                          double currentSaving)
    {
        this(month, food, utilities, education, entertainment, miscellaneous, saving,
                currentFood, currentUtilities, currentEducation, currentEntertainment, currentMiscellaneous, currentSaving);
        this._id = id;
    }
    //categories List contain 6 BudgetCategories Food ~ Saving
    public MonthlyBudget (int id, List<BudgetCategory> categories, Month month){
        this._id = id;

        this.foodBC = categories.get(0);
        this.utilitiesBC = categories.get(1);
        this.educationBC = categories.get(2);
        this.entertainmentBC = categories.get(3);
        this.miscellaneousBC = categories.get(4);
        this.savingBC = categories.get(5);

        this.categories.put(this.foodBC.getName(), this.foodBC);
        this.categories.put(this.utilitiesBC.getName(), this.utilitiesBC);
        this.categories.put(this.educationBC.getName(), this.educationBC);
        this.categories.put(this.entertainmentBC.getName(), this.entertainmentBC);
        this.categories.put(this.miscellaneousBC.getName(), this.miscellaneousBC);
        this.categories.put(this.savingBC.getName(), this.savingBC);


        System.out.println("Budget created! categories size: " + this.categories.size());

        this.month = month;
    }

    public int get_id(){
        return this._id;
    }


    public double getFood() {
        return this.foodBC.getGoal();
    }

    public void setFood(double food) {
        this.foodBC.setGoal(food);
    }

    public double getFoodCurrent() {
        return foodBC.getCurrentTotal();
    }

    public void setFoodCurrent(double foodCurrent) {
        this.foodCurrent = foodCurrent;
    }

    public double getEntertainment() {
        return entertainmentBC.getGoal();
    }

    public void setEntertainment(double entertainment) {
        this.entertainmentBC.setGoal(entertainment);
    }

    public double getUtilities() {
        return this.utilitiesBC.getGoal();
    }

    public void setUtilities(double utilities) {
        this.utilitiesBC.setGoal(utilities);
    }

    public double getEducation() {
        return this.educationBC.getGoal();
    }

    public void setEducation(double education) {
        this.educationBC.setGoal(education);
    }

    public double getMiscellaneous() {
        return this.miscellaneousBC.getGoal();
    }

    public void setMiscellaneous(double miscellaneous) {
        this.miscellaneousBC.setGoal(miscellaneous);
    }

    public double getSaving() {
        return this.savingBC.getGoal();
    }

    public void setSaving(double saving) {
        this.savingBC.setGoal(saving);
    }

    public Month getMonth() {
        return month;
    }

    public double getEntertainmentCurrent() {
        return entertainmentBC.getCurrentTotal();
    }

    public void setEntertainmentCurrent(double entertainmentCurrent) {
        this.entertainmentBC.getCurrentTotal();
    }

    public double getUtilitiesCurrent() {
        return utilitiesBC.getCurrentTotal();
    }

    public void setUtilitiesCurrent(double utilitiesCurrent) {
        this.utilitiesCurrent = utilitiesCurrent;
    }

    public double getEducationCurrent() {
        return educationBC.getCurrentTotal();
    }

    public void setEducationCurrent(double educationCurrent) {
        this.educationCurrent = educationCurrent;
    }

    public double getMiscellaneousCurrent() {
        return miscellaneousBC.getCurrentTotal();
    }

    public void setMiscellaneousCurrent(double miscellaneousCurrent) {
        this.miscellaneousCurrent = miscellaneousCurrent;
    }

    public double getSavingCurrent() {
        return savingBC.getCurrentTotal();
    }

    public void setSavingCurrent(double savingCurrent) {
        this.savingCurrent = savingCurrent;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public double getTotal(){
        return  getFood() +
                getEducation() +
                getEntertainment() +
                getUtilities() +
                getMiscellaneous() +
                getSaving();
    }

    public double getCurrentTotal(){
        return  getFoodCurrent() +
                getEducationCurrent() +
                getEntertainmentCurrent() +
                getUtilitiesCurrent() +
                getMiscellaneousCurrent() +
                getSavingCurrent();
    }

    public double getRemaining(){
        return getTotal() - getCurrentTotal();
    }

    public BudgetCategory getCategory(String id){
        return categories.get(id);
    }

    public BudgetCategory getFoodBC() {
        return foodBC;
    }

    public BudgetCategory getEntertainmentBC() {
        return entertainmentBC;
    }

    public BudgetCategory getEducationBC() {
        return educationBC;
    }

    public BudgetCategory getUtilitiesBC() {
        return utilitiesBC;
    }

    public BudgetCategory getMiscellaneousBC() {
        return miscellaneousBC;
    }

    public BudgetCategory getSavingBC() {
        return savingBC;
    }

    public ObservableMap<String, BudgetCategory> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        return "MonthlyBudget{" +
                "food=" + food +
                ", entertainment=" + entertainment +
                ", utilities=" + utilities +
                ", education=" + education +
                ", miscellaneous=" + miscellaneous +
                ", saving=" + saving +
                ", total=" + total +
                ", foodCurrent=" + foodCurrent +
                ", entertainmentCurrent=" + entertainmentCurrent +
                ", utilitiesCurrent=" + utilitiesCurrent +
                ", educationCurrent=" + educationCurrent +
                ", miscellaneousCurrent=" + miscellaneousCurrent +
                ", savingCurrent=" + savingCurrent +
                ", currentTotal=" + currentTotal +
                ", remaining=" + remaining +
                ", foodBC=" + foodBC.printInfo() +
                ", entertainmentBC=" + entertainmentBC.printInfo() +
                ", educationBC=" + educationBC.printInfo() +
                ", utilitiesBC=" + utilitiesBC.printInfo() +
                ", miscellaneousBC=" + miscellaneousBC.printInfo() +
                ", savingBC=" + savingBC.printInfo() +
                ", categories=" + categories +
                ", month=" + month +
                '}';
    }

    public void initializeCurrentTotal(){
        this.setFoodCurrent(this.foodBC.getCurrentTotal());
        this.setUtilitiesCurrent(this.utilitiesBC.getCurrentTotal());
        this.setEntertainmentCurrent(this.entertainmentBC.getCurrentTotal());
        this.setEducationCurrent(this.educationBC.getCurrentTotal());
        this.setMiscellaneousCurrent(this.miscellaneousBC.getCurrentTotal());
        this.setSavingCurrent(this.savingBC.getCurrentTotal());
    }

    private void initializeCategoryList(){


        foodBC = new BudgetCategory("food", this.month);
        entertainmentBC = new BudgetCategory("entertainment", this.month);
        educationBC = new BudgetCategory("education", this.month);
        utilitiesBC = new BudgetCategory("utilities", this.month);
        miscellaneousBC = new BudgetCategory("miscellaneous", this.month);
        savingBC = new BudgetCategory("saving", this.month);

        categories.put("food", foodBC);
        categories.put("education", educationBC);
        categories.put("entertainment", entertainmentBC);
        categories.put("utilities", utilitiesBC);
        categories.put("miscellaneous", miscellaneousBC);
        categories.put("saving", savingBC);
    }
}
