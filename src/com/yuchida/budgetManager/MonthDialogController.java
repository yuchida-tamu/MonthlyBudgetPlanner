package com.yuchida.budgetManager;

import com.yuchida.budgetManager.model.BudgetDataManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Optional;

public class MonthDialogController {

    @FXML
    private TextField foodField;
    @FXML
    private TextField utilitiesField;
    @FXML
    private TextField educationField;
    @FXML
    private TextField entertainmentField;
    @FXML
    private TextField miscellaneousField;
    @FXML
    private TextField savingField;
    @FXML
    private Text total;

    @FXML
    private DialogPane editDialog;



    public TextField getFoodField() {
        return foodField;
    }

    public void setFoodField(TextField foodField) {
        this.foodField = foodField;
    }

    public TextField getUtilitiesField() {
        return utilitiesField;
    }

    public void setUtilitiesField(TextField utilitiesField) {
        this.utilitiesField = utilitiesField;
    }

    public TextField getEducationField() {
        return educationField;
    }

    public void setEducationField(TextField educationField) {
        this.educationField = educationField;
    }

    public TextField getEntertainmentField() {
        return entertainmentField;
    }

    public void setEntertainmentField(TextField entertainmentField) {
        this.entertainmentField = entertainmentField;
    }

    public TextField getMiscellaneousField() {
        return miscellaneousField;
    }

    public void setMiscellaneousField(TextField miscellaneousField) {
        this.miscellaneousField = miscellaneousField;
    }

    public TextField getSavingField() {
        return savingField;
    }

    public void setSavingField(TextField savingField) {
        this.savingField = savingField;
    }

    public void updateBudgetAll(List<MonthlyBudget> budgets) throws NumberFormatException {
        for(MonthlyBudget budget : budgets){
            updateBudget(budget);
        }
    }

    public void updateBudgetAll(){
        for(MonthlyBudget budget : BudgetDataManager.getInstance().getData()){
            updateBudget(budget);
        }
    }

    public void updateBudget(MonthlyBudget budget) throws NumberFormatException{
        //input validation then update data

            budget.setFood(Double.parseDouble(foodField.getText()));
            budget.setUtilities(Double.parseDouble(utilitiesField.getText()));
            budget.setEducation(Double.parseDouble(educationField.getText()));
            budget.setEntertainment(Double.parseDouble(entertainmentField.getText()));
            budget.setMiscellaneous(Double.parseDouble(miscellaneousField.getText()));
            budget.setSaving(Double.parseDouble(savingField.getText()));

    }

    public void reflectBudgetOnDialog(MonthlyBudget budget){
        foodField.setText(Double.toString(budget.getFood()));
        utilitiesField.setText(Double.toString(budget.getUtilities()));
        educationField.setText(Double.toString(budget.getEducation()));
        entertainmentField.setText(Double.toString((budget.getEntertainment())));
        miscellaneousField.setText(Double.toString(budget.getMiscellaneous()));
        savingField.setText(Double.toString(budget.getSaving()));
        total.setText("Monthly Budget: " + Double.toString(budget.getTotal()));
    }

    public void updateTotal(){
        double totalAmount = 0;
        try{
            totalAmount = Double.parseDouble(foodField.getText()) +
                    Double.parseDouble(utilitiesField.getText()) +
                    Double.parseDouble(entertainmentField.getText()) +
                    Double.parseDouble(educationField.getText()) +
                    Double.parseDouble(miscellaneousField.getText()) +
                    Double.parseDouble(savingField.getText());

        } catch (NumberFormatException e) {
            System.out.println("Input is invalid: Could not compute the total of the budget");
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Invalid Input");
//            alert.setHeaderText(null);
//            alert.setContentText("Please enter valid input (number)");
//            alert.showAndWait();
        }

        total.setText("Monthly Budget: " + totalAmount);
    }

    public void checkInputOnKeyReleased(){

    }


}
