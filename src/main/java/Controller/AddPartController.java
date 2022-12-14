package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** Creates the Add Part window of the application. */
public class AddPartController implements Initializable {
    public TextField idField;
    public TextField nameField;
    public TextField invField;
    public TextField priceField;
    public TextField maxField;
    public TextField machineIdCompanyNameField;
    public TextField fieldMin;
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public Button saveButton;
    public Button cancelButton;
    public Label machineIdCompanyNameLabel;
    public ToggleGroup toggleGroup;

    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    /** Changes the label of the field to 'Machine ID' if the In House radio button is selected. */
    public void onInHouseSelected(ActionEvent actionEvent) {
        machineIdCompanyNameLabel.setText("Machine ID");
    }

    /** Changes the label of the field to 'Company Name' if the Outsourced radio button is selected. */
    public void onOutsourcedSelected(ActionEvent actionEvent) {
        machineIdCompanyNameLabel.setText("Company Name");
    }

    /** Returns the user to the Main Window when the 'Cancel' button is clicked. */
    public void onCancelClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/src/main/java/View/MainWindow.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /** Generates a new ID for a part.
     * ID numbers start at 9001 and are incremented by one as the amount of parts in the inventory increases. */
    public static int generateNewID(){
        int id = 1;
        for (int i = 0; i < Inventory.getAllParts().size(); i++){
            id++;
        }
        id = id + 9000;
        return id;

    }

    /** Creates a new part with the given information and saves it to the inventory.
     * All fields must be filled with the proper input.
     * User must confirm before part is saved.*/
    public void onSaveClicked(ActionEvent actionEvent) {
        try {
            int inv = Integer.parseInt(invField.getText());
            int min = Integer.parseInt(fieldMin.getText());
            int max = Integer.parseInt(maxField.getText());
            if (max < min){
                MainController.displayInfoAlert("Input Error","Part minimum must be less than maximum");
            }
            else if (inv < min || inv > max){
                MainController.displayInfoAlert("Input Error","Part inventory must be between minimum and maximum");
            }
            else {
                int id = generateNewID();
                String name = nameField.getText();
                if (name.equals("")){
                    MainController.displayInfoAlert("Input Error", "Part name cannot be blank");
                    return;
                }
                double price = Double.parseDouble(priceField.getText());
                if (inHouseRadio.isSelected()){
                    int machId = Integer.parseInt(machineIdCompanyNameField.getText());
                    InHouse inHouse = new InHouse(id, name, price, inv, min, max, machId);
                    Inventory.addPart(inHouse);
                }
                else {
                    String coName = machineIdCompanyNameField.getText();
                    if (coName.equals("")){
                        MainController.displayInfoAlert("Input Error", "Company name cannot be blank");
                        return;
                    }
                    Outsourced outsourced = new Outsourced(id, name, price, inv, min, max, coName);
                    Inventory.addPart(outsourced);
                }
                Parent root = FXMLLoader.load(getClass().getResource("/src/main/java/View/MainWindow.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setTitle("Inventory Management System");
                stage.setScene(scene);
                stage.show();

            }
        } catch (Exception e){
            MainController.displayInfoAlert("Input Error","Inventory, Cost, Min, Max, and Machine ID fields must contain numerical values");
        }

    }
}
