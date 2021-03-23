package com.yuchida.budgetManager;

import com.yuchida.budgetManager.model.DataExport;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.time.LocalDate;

public class ExportDialogController {
    @FXML
    private TextField fileNameTextField;
    private String filename = "default_";
    private String date;
    private DataExport exportManager ;

    public void initialize(){
        date = LocalDate.now().toString();
        filename = filename + date;
        exportManager = DataExport.getInstance();
    }



    public void setFileName() {
        exportManager.setFilename(filename);
    }

    public void handleKeyRelease(KeyEvent keyEvent) {
        filename = fileNameTextField.getText();
        System.out.println(filename);
    }
}
