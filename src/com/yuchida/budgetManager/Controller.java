package com.yuchida.budgetManager;

import com.yuchida.budgetManager.model.BudgetDataManager;
import com.yuchida.budgetManager.model.DataExport;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class Controller {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ListView<MonthlyBudget> monthsListView;
    @FXML
    private Text monthTop;
    @FXML
    private Text foodText;
    @FXML
    private Text utilitiesText;
    @FXML
    private Text educationText;
    @FXML
    private Text entertainmentText;
    @FXML
    private Text miscellaneousText;
    @FXML
    private Text savingText;
    @FXML
    private Text balanceText;
    @FXML
    private Text total;
    @FXML
    private Text spending;

    @FXML
    private Text foodCurrentField;
    @FXML
    private Text utilitiesCurrentField;
    @FXML
    private Text educationCurrentField;
    @FXML
    private Text entertainmentCurrentField;
    @FXML
    private Text miscellaneousCurrentField;
    @FXML
    private Text savingCurrentField;
    @FXML
    private GridPane budgetDetailPanel;
    @FXML
    private VBox summaryBox;



    private Font font = new Font("Roboto Regular",10.0);


    public void initialize(){


        summaryBox.setAlignment(Pos.CENTER);

        ObservableList<MonthlyBudget> list = BudgetDataManager.getInstance().getData();
        // store data to HashMap, key is Month
        Map<Month, MonthlyBudget> monthsHashMap = new HashMap<Month, MonthlyBudget>();

        for(MonthlyBudget monthlyBudget : list){
            monthsHashMap.put(monthlyBudget.getMonth(), monthlyBudget);


        }


        //////////

        refreshContentOnSelectionChange();


        monthsListView.setItems(list);
        monthsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        monthsListView.getSelectionModel().select(monthsHashMap.get(LocalDate.now().getMonth()));//select the month of local date




        fillInListView();


    }



    private void fillInListView() {
        monthsListView.setCellFactory(new Callback<ListView<MonthlyBudget>, ListCell<MonthlyBudget>>() {
            @Override
            public ListCell<MonthlyBudget> call(ListView<MonthlyBudget> monthlyBudgetListView) {
                ListCell<MonthlyBudget> cell = new ListCell<MonthlyBudget>(){
                    @Override
                    protected void updateItem(MonthlyBudget monthlyBudget, boolean empty) {
                        super.updateItem(monthlyBudget, empty);

                        if(empty){
                            setText(null);

                        } else {
                            setText(monthlyBudget.getMonth().toString());

                            //differentiate the month of LocalDate
                            if(LocalDate.now().getMonth().equals(monthlyBudget.getMonth())){
                                super.setStyle("-fx-font-weight: bold;");


                                setTextFill(Color.TURQUOISE);
                            } else {
                                super.setStyle("-fx-font-weight: normal;");
                                setTextFill(Color.BLACK);

                            }
                        }
                    }
                };

                //todo add contextMenu to each cell

                cell.setFont(font);
                cell.setStyle("-fx-font-weight: bold;");
                return cell;
            }
        });
    }


    //refresh the budget plan display
    private void refreshContentOnSelectionChange() {
        monthsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MonthlyBudget>() {
            @Override
            public void changed(ObservableValue<? extends MonthlyBudget> observableValue, MonthlyBudget monthlyBudget, MonthlyBudget t1) {
                displayBudgetPlanInfo();
            }
        });
    }

    private void displayBudgetPlanInfo() {
        MonthlyBudget item = monthsListView.getSelectionModel().getSelectedItem();
        //loadCategoryData();
        monthTop.setText(item.getMonth().toString());
        foodText.setText("¥ " + item.getFood());
        utilitiesText.setText("¥ " + item.getUtilities());
        educationText.setText("¥ " + item.getEducation());
        entertainmentText.setText("¥ " + item.getEntertainment());
        miscellaneousText.setText("¥ " + item.getMiscellaneous());
        savingText.setText("¥ " + item.getSaving());
        balanceText.setText("¥ " + item.getRemaining());
        //set the values of Text Fields
        foodCurrentField.setText("¥ " + item.getFoodCurrent());
        utilitiesCurrentField.setText("¥ " + item.getUtilitiesCurrent());
        educationCurrentField.setText("¥ " + item.getEducationCurrent());
        entertainmentCurrentField.setText("¥ " + item.getEntertainmentCurrent());
        miscellaneousCurrentField.setText("¥ " + item.getMiscellaneousCurrent());
        savingCurrentField.setText("¥ " + item.getSavingCurrent());
        //calculate total, spending, remainder
        updateSummary(item);
    }

//    private void loadCategoryData(){
//        MonthlyBudget budget = monthsListView.getSelectionModel().getSelectedItem();
//        String monthName = budget.getMonth().toString();
//        String[] categoryName = {"food", "education", "entertainment", "utilities", "miscellaneous", "saving"};
//        for(String category : categoryName){
//            String filename = monthName + category + ".txt";
//            if(Files.exists(Paths.get(filename))){
//                try{
//                    CategoryData.loadCategoryData(filename);
//                    switch (category){
//                        case "food":
//                            budget.getFoodBC().setBreakdownItems(CategoryData.getBreakdownItems());
//                            //need to update category class instance as well
//                            budget.getFoodBC().loadAndUpdateCategory();
//                            break;
//                        case "education":
//                            budget.getEducationBC().setBreakdownItems(CategoryData.getBreakdownItems());
//                            budget.getEducationBC().loadAndUpdateCategory();
//                            break;
//                        case "entertainment":
//                            budget.getEntertainmentBC().setBreakdownItems(CategoryData.getBreakdownItems());
//                            budget.getEntertainmentBC().loadAndUpdateCategory();
//                            break;
//                        case "utilities":
//                            budget.getUtilitiesBC().setBreakdownItems(CategoryData.getBreakdownItems());
//                            budget.getUtilitiesBC().loadAndUpdateCategory();
//                            break;
//                        case "miscellaneous":
//                            budget.getMiscellaneousBC().setBreakdownItems(CategoryData.getBreakdownItems());
//                            budget.getMiscellaneousBC().loadAndUpdateCategory();
//                            break;
//                        case "saving":
//                            budget.getSavingBC().setBreakdownItems(CategoryData.getBreakdownItems());
//                            budget.getSavingBC().loadAndUpdateCategory();
//                            break;
//                    }
//
//                } catch (IOException e){
//                    System.out.println("Loading category failed");
//                }
//            }
//        }
//    }

    //if ENTER key is pressed, update the corresponding "current" balance data on the detail panel
    public void handleKeyPressed(KeyEvent keyEvent){
        //todo input validation
        MonthlyBudget budget = monthsListView.getSelectionModel().getSelectedItem();
        if(budget != null){
            if(keyEvent.getCode() == KeyCode.ENTER){
                if(isValidInput()){
                    updateCurrentBalance(budget);
                    displayBudgetPlanInfo();
                } else {
                    //todo alert to urge user to enter valid input
                    System.out.println("invalid input");
                }
            }
        } else {
            System.out.println("item is not selected");
        }

    }

    public void handleKeyReleased(){
        MonthlyBudget item = monthsListView.getSelectionModel().getSelectedItem();
        updateSummary(item);

    }

    public void showEditDialog(){

        MonthlyBudget budget = monthsListView.getSelectionModel().getSelectedItem();
        String month = budget.getMonth().toString();
        String title = "Edit the budget plan for " + month;
        requestEditDialog(budget, title, false);
    }

    public void showEditAllDialog() throws IOException {
        String title = "Edit all budget plans";
        MonthlyBudget budget = monthsListView.getSelectionModel().getSelectedItem();
        requestEditDialog(budget, title, true);
    }

    private void requestEditDialog(MonthlyBudget budget, String title, boolean updateAll){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("monthDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("couldn't load a dialog window");
            return;
        }

        MonthDialogController controller = fxmlLoader.getController();
        //if var budget is null, select the first month(JAN) then store it to var budget
        //then reflect the data on the dialog
        if(budget == null){
            monthsListView.getSelectionModel().selectFirst();
            budget = monthsListView.getSelectionModel().getSelectedItem();

        }
        controller.reflectBudgetOnDialog(budget);


        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);


        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            //if this edits all budget plans use updateBudgetAll method
            //else edits only the selected month's plan
            if (updateAll) {
                try{
                    controller.updateBudgetAll(monthsListView.getItems());
                } catch (NumberFormatException e){
                    System.out.println("Error: invalid input");
                    //alert an user, the input is invalid
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Invalid Input");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter valid input (number)");
                    alert.showAndWait();
                    return;
                }

                //Reflect updates of all monthly budgets
                BudgetDataManager.getInstance().updateBudgetGoal();
            } else {
                controller.updateBudget(budget);
                //Reflect updates of budget goals on DB tables
                BudgetDataManager.getInstance().updateBudgetGoal(budget.getCategories());
            }

            updateContentDisplay(budget);
            displayBudgetPlanInfo();
        }
    }

    //set the values using values of text fields
    private void updateCurrentBalance(MonthlyBudget budget){
        //input validation, if input is invalid show alert
        try{
            budget.setFoodCurrent(Double.parseDouble(foodCurrentField.getText()));
            budget.setUtilitiesCurrent(Double.parseDouble(utilitiesCurrentField.getText()));
            budget.setEducationCurrent(Double.parseDouble(educationCurrentField.getText()));
            budget.setEntertainmentCurrent(Double.parseDouble(entertainmentCurrentField.getText()));
            budget.setMiscellaneousCurrent(Double.parseDouble(miscellaneousCurrentField.getText()));
            budget.setSavingCurrent(Double.parseDouble(savingCurrentField.getText()));
        } catch (NumberFormatException e){
            //alert an user, the input is invalid
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid input (number)");
            alert.showAndWait();
        }

    }
    //update the summary section, if spending exceeds the total budget, fill the text red
    //otherwise fill the text blue(or green)
    private void updateSummary(MonthlyBudget item){
        total.setText(Double.toString(item.getTotal()));
        spending.setText(Double.toString(item.getCurrentTotal()));
        balanceText.setText(Double.toString(item.getRemaining()));

        if(item.getTotal() >= item.getCurrentTotal()){
            balanceText.setFill(Color.MEDIUMSPRINGGREEN);
        } else {
            balanceText.setFill(Color.ORANGERED);
        }
    }

    private boolean isValidInput(){


        return true;
    }


    public void handleMouseClicked(MouseEvent mouseEvent) throws IOException {
        MonthlyBudget budget = monthsListView.getSelectionModel().getSelectedItem();
        Pane pane= (Pane) mouseEvent.getSource();
        String id = pane.getId();
        BudgetCategory category = budget.getCategory(id);
        updateCategory(category, budget);

        Dialog<ButtonType> stage = new Dialog<>();
        stage.setTitle("Budget Breakdown Detail");
        stage.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("budgetBreakdown.fxml"));

        try{
            stage.getDialogPane().setContent(fxmlLoader.load());
            //category.loadCategoryDate();
        }catch (IOException e){
            System.out.println("couldn't load the page properly");
        }

        BudgetBreakdownController controller = fxmlLoader.getController();
        controller.setTotalAmount(category.getCurrentTotal());
       // controller.setUncategorizedTextField(category);
        controller.setBudgetCategory(category);
        controller.fillContentArea(category);

        if(category.getCurrentTotal() != 0){

            category.getPieChartGenerator().generatePieChart(controller.getChartPane(), category.getName());
        }
        stage.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        stage.showAndWait();

        updateContentDisplay(budget);
        updateSummary(budget);

    }

    private void updateContentDisplay(MonthlyBudget budget) {
        foodCurrentField.setText("¥ " + budget.getFoodCurrent());
        utilitiesCurrentField.setText("¥ " + budget.getUtilitiesCurrent());
        educationCurrentField.setText("¥ " + budget.getEducationCurrent());
        entertainmentCurrentField.setText("¥ " + budget.getEntertainmentCurrent());
        miscellaneousCurrentField.setText("¥ " + budget.getMiscellaneousCurrent());
        savingCurrentField.setText("¥ " + budget.getSavingCurrent());
    }

    private void updateCategory(BudgetCategory category, MonthlyBudget budget){
        if(category.equals(budget.getFoodBC())){
            category.setGoal(budget.getFood());
            category.updateCurrentTotal(budget.getFoodCurrent());
        } else if (category.equals(budget.getUtilitiesBC())){
            category.setGoal(budget.getUtilities());
            category.updateCurrentTotal(budget.getUtilitiesCurrent());
        } else if (category.equals((budget.getEducationBC()))){
            category.setGoal(budget.getEducation());
            category.updateCurrentTotal(budget.getEducationCurrent());
        } else if (category.equals((budget.getEntertainmentBC()))){
            category.setGoal(budget.getEntertainment());
            category.updateCurrentTotal(budget.getEntertainmentCurrent());
        } else if (category.equals((budget.getMiscellaneousBC()))){
            category.setGoal(budget.getMiscellaneous());
            category.updateCurrentTotal(budget.getMiscellaneousCurrent());
        } else if (category.equals((budget.getSavingBC()))){
            category.setGoal(budget.getSaving());
            category.updateCurrentTotal(budget.getSavingCurrent());
        }
    }

    public void onClickExportThisMonthToCVS(){
//
        openFileChooser(false);
    }

    public void onClickExportAllToCVS(){
        openFileChooser(true);
    }

//    private void openExportDialog(boolean exportAll){
//        Dialog<ButtonType> dialog = new Dialog<>();
//        dialog.setTitle("Export Dialog");
//        dialog.initOwner(mainBorderPane.getScene().getWindow());
//        FXMLLoader fxmlLoader = new FXMLLoader();
//        fxmlLoader.setLocation(getClass().getResource("exportDialog.fxml"));
//
//
//        try {
//           dialog.getDialogPane().setContent( fxmlLoader.load());
//        } catch (IOException e){
//            System.out.println("Error: couldn't load Export dialog: " + e.getMessage() );
//        }
//        ExportDialogController controller = fxmlLoader.getController();
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//
//        Optional<ButtonType> result = dialog.showAndWait();
//        if(result.isPresent() && result.get().equals(ButtonType.OK)){
//
//            if(exportAll){
//                System.out.println("Export All");
//                try {
//                    controller.setFileName();
//                    DataExport.getInstance().exportAllToCSV(BudgetDataManager.getInstance().getData());
//                } catch (IOException e){
//                    System.out.println("Error: couldn't export: " + e.getMessage() );
//                }
//
//            } else {
//                System.out.println("Export This Month");
//                try {
//                    controller.setFileName();
//                    DataExport.getInstance().exportThisMonthToCSV(monthsListView.getSelectionModel().getSelectedItem());
//                } catch (IOException e){
//                    System.out.println("Error: couldn't export: " + e.getMessage() );
//                }
//            }
//        } else {
//            System.out.println("Export cancelled");
//        }
//    }

    public void openFileChooser(boolean isSavingAll){
        FileChooser fileChooser = new FileChooser();


        //set FileChooser with Extension filter
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);
        //Show the dialog
        File file = fileChooser.showSaveDialog(mainBorderPane.getScene().getWindow());
        if(file != null){
            System.out.println("Exported file is saved at: " + file.toString());
            DataExport.getInstance().setFilename(file.toString());
            try {
                if(isSavingAll){
                    DataExport.getInstance().exportAllToCSV(BudgetDataManager.getInstance().getData());
                } else {
                    DataExport.getInstance().exportThisMonthToCSV(monthsListView.getSelectionModel().getSelectedItem());
                }
            } catch (IOException e) {
                System.out.println("Error: file chooser method failed while saving: " + e.getMessage());
            }

        } else {
            System.out.println("Error: empty file name ");
        }


    }
}
