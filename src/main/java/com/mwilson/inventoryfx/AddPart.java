package com.mwilson.inventoryfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPart implements Initializable {
    public TextField fieldID;
    public TextField fieldName;
    public TextField fieldInv;
    public TextField fieldCost;
    public TextField fieldMax;
    public TextField fieldMachineIdCompanyName;
    public TextField fieldMin;
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public Button saveButton;
    public Button cancelButton;
    public Label machineIdCompanyNameLabel;

    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    public void InHouseSelected(ActionEvent actionEvent) {
        machineIdCompanyNameLabel.setText("Machine ID");
    }

    public void OutsourcedSelected(ActionEvent actionEvent) {
        machineIdCompanyNameLabel.setText("Company Name");
    }

    public void OnCancelClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static int getNewID(){
        int id = 1;
        for (int i = 0; i < Inventory.getAllParts().size(); i++){
            id++;
        }
        return id;

    }

    public void OnSaveClicked(ActionEvent actionEvent) {
        try {
            int inventory = Integer.parseInt(fieldInv.getText());
            int min = Integer.parseInt(fieldMin.getText());
            int max = Integer.parseInt(fieldMax.getText());
            if (max < min){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText("Input Error!");
                alert.setContentText("Part minimum must be less than maximum");
                alert.showAndWait();
            }
            else if (inventory < min || inventory > max){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error!");
                alert.setHeaderText("Input Error!");
                alert.setContentText("Part inventory must be between minimum and maximum");
                alert.showAndWait();
            }
            else {
                int id = getNewID();
                String name = fieldName.getText();
                double price = Double.parseDouble(fieldCost.getText());
                if (inHouseRadio.isSelected()){
                    int machId = Integer.parseInt(fieldMachineIdCompanyName.getText());
                    InHouse inHouse = new InHouse(id, name, price, inventory, min, max, machId);
                    Inventory.addPart(inHouse);
                }
                else {
                    String coName = fieldMachineIdCompanyName.getText();
                    Outsourced outsourced = new Outsourced(id, name, price, inventory, min, max, coName);
                    Inventory.addPart(outsourced);
                }
                Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();

            }
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText("Input Error!");
            alert.setContentText("Inventory, Cost, Min, Max, and Machine ID fields must contain numerical values");
            alert.showAndWait();
        }

    }
}
