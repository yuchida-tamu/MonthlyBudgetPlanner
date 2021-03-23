package com.yuchida.budgetManager;

import com.yuchida.budgetManager.model.BudgetDataManager;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class BudgetBreakdownController {
    @FXML
    private DialogPane dialogPane;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Text totalAmount;
    @FXML
    private Text uncategorizedTotalText;
    @FXML
    private TextField newItemTextField;
    @FXML
    private VBox breakDownItemBox;
    @FXML
    private Pane chartPane;

    private BudgetCategory budgetCategory;



    public void setTotalAmount(double amount){
        totalAmount.setText("¥ " + amount);
    }

    public void setUncategorizedTextField(BudgetCategory category){
        category.setUncategorized();
        uncategorizedTotalText.setText("¥ " + category.getUncategorized());
    }

    public void generatePieChart(){
        budgetCategory.getPieChartGenerator().generatePieChart(chartPane, budgetCategory.getName());
    }

    public void generatePieChart(String name, PieChartGenerator pieChartGenerator){
        pieChartGenerator.generatePieChart(chartPane, name);

    }

    public void setBudgetCategory(BudgetCategory budgetCategory) {
        this.budgetCategory = budgetCategory;
    }

    public void addItem(){
        //when a new Item is added, the initial value is set to 0
        String newItemName = newItemTextField.getText();
        //if user tries to create item without setting a name
        //it will warn
        if(newItemName.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Input");
            alert.setContentText("Please enter item name");
            alert.showAndWait();
            return;
        }
        String idName = newItemName.toLowerCase();

        //check if the item already exists
        if(itemAlreadyExists(idName)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Input");
            alert.setContentText("The item already exists!");
            newItemTextField.clear();
            alert.showAndWait();

            return;
        } else {
            //create a new breakdownitem
            BreakdownItem item = new BreakdownItem(idName, 0, budgetCategory.get_id());
            budgetCategory.addBreakdownItem(item);
            //insert the new item into the DB table
            BudgetDataManager.getInstance().saveBreakdownItem(item);
            createFXMLElement(item);
        }

        newItemTextField.clear();
    }

    private boolean itemAlreadyExists(String name){
        return budgetCategory.getBreakdownItems().containsKey(name);
    }

    public void fillContentArea(BudgetCategory category){
        //clear the box first
        breakDownItemBox.getChildren().clear();
        //load items

        Map items = category.getBreakdownItems();
        for(Object entry : items.entrySet()){
            Map.Entry<String, BreakdownItem> itemEntry = (Map.Entry<String, BreakdownItem>) entry;
            BreakdownItem item = itemEntry.getValue();

            createFXMLElement(item);

        }
    }

    private void createFXMLElement(BreakdownItem item){
        String name = item.getName();
        double amount = item.getAmount();
        Label label = new Label(name);
        Text amountText = new Text("¥ " + amount);
        amountText.setId(name + "TotalText");//set the newItemName as its Text id
        TextField textField = new TextField();
        textField.setId(name);//set the text id as newItemName
        Button deleteBtn = new Button("delete");
        //setStyleClass
        textField.getStyleClass().add("itemTextField");
        label.getStyleClass().add("itemLabel");



        //set region to fill space between the elements
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        EventHandler<? super KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                handleKeyReleased(keyEvent);
            }


        };

        deleteBtn.setOnAction((ActionEvent event) -> {
            System.out.println("click");
            BudgetDataManager.getInstance().deleteItemFromDetails(item);
            budgetCategory.getBreakdownItems().remove(item.getName());
            fillContentArea(budgetCategory);
        });


        textField.setOnKeyReleased(eventHandler); // attach OnKeyReleased ="#handleKeyEntered" to the textfield
        HBox box = new HBox(label, region1, amountText,region2,textField, deleteBtn);
//        box.getChildren().add(label);
//        box.getChildren().add(amountText);
//        box.getChildren().add(textField);

        breakDownItemBox.getChildren().add(box);
    }

    public void save(){

    }

    public void handleKeyReleased(KeyEvent keyEvent) {
        if(keyEvent.getCode()!= KeyCode.ENTER){
            return;
        }
        TextField textField = (TextField) keyEvent.getSource();
        String key = textField.getId();

        BreakdownItem item = budgetCategory.getItem(key);
        try{
            double input = Double.parseDouble(textField.getText());
            item.addExpense(input);
            budgetCategory.updateCurrentTotal(item.getAmount());
            //update the percentage of every items
            budgetCategory.updateAllItemPercentage();
            setTotalAmount(budgetCategory.getCurrentTotal());//set the Total Text to its updated amount
            //update chart data

            budgetCategory.getPieChartGenerator().updateDataItem(item.getName(), budgetCategory.getPercentage(item.getAmount()));

            Scene scene = dialogPane.getScene();
            Text total = (Text) scene.lookup("#" + key + "TotalText");

            total.setText("¥ " + item.getAmount());
        }catch (NumberFormatException e){
            System.out.println("invalid input");
        }
        generatePieChart();
        textField.clear();
    }

    public void handleOK(ActionEvent actionEvent) throws IOException {
        budgetCategory.saveCategoryData();
        Stage stage = (Stage)dialogPane.getScene().getWindow();

        stage.close();
    }

    public Pane getChartPane(){
        return chartPane;
    }
}
