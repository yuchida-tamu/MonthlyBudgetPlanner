package com.yuchida.budgetManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BudgetData {
    private static BudgetData instance = new BudgetData();
    private static String filename = "MonthlyBudgets.txt";

    private static ObservableList<MonthlyBudget> monthlyBudgets;

    public static BudgetData getInstance(){
        return instance;
    }

    public ObservableList<MonthlyBudget> getBudgets(){
        return monthlyBudgets;
    }

    public void addBudgets(MonthlyBudget budget){
        monthlyBudgets.add(budget);
    }

    public void loadBudgets()throws IOException{
        monthlyBudgets = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);
        String input;

        try{
            while((input = br.readLine()) != null){
                String[] itemPieces = input.split("\t");

                Month month = Month.valueOf(itemPieces[0]);
                double utilities = Double.parseDouble(itemPieces[1]);
                double education = Double.parseDouble(itemPieces[2]);
                double entertainment = Double.parseDouble(itemPieces[3]);
                double miscellaneous = Double.parseDouble(itemPieces[4]);
                double saving = Double.parseDouble(itemPieces[5]);
                double currentUtilities = Double.parseDouble(itemPieces[6]);
                double currentEducation = Double.parseDouble(itemPieces[7]);
                double currentEntertainment = Double.parseDouble(itemPieces[8]);
                double currentMiscellaneous = Double.parseDouble(itemPieces[9]);
                double currentSaving = Double.parseDouble(itemPieces[10]);
                double food = Double.parseDouble(itemPieces[11]);
                double currentFood = Double.parseDouble(itemPieces[12]);


                MonthlyBudget budget = new MonthlyBudget(month, food, utilities, education, entertainment, miscellaneous, saving,
                        currentFood, currentUtilities, currentEducation, currentEntertainment, currentMiscellaneous, currentSaving);

                monthlyBudgets.add(budget);
            }
        } catch (IOException e){
            if(br != null){
                br.close();
            }
        }

    }


    public void storeBudgets() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{
            Iterator<MonthlyBudget> iter = monthlyBudgets.iterator();
            while (iter.hasNext()){
                MonthlyBudget budget = iter.next();
                bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
                        budget.getMonth(),
                        budget.getUtilities(),
                        budget.getEducation(),
                        budget.getEntertainment(),
                        budget.getMiscellaneous(),
                        budget.getSaving(),
                        budget.getUtilitiesCurrent(),
                        budget.getEducationCurrent(),
                        budget.getEntertainmentCurrent(),
                        budget.getMiscellaneousCurrent(),
                        budget.getSavingCurrent(),
                        budget.getFood(),
                        budget.getFoodCurrent()));
                bw.newLine();
            }
        } finally {
            if(bw != null){
                bw.close();
            }
        }
    }
}
