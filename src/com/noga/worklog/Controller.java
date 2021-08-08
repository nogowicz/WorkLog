package com.noga.worklog;

import com.noga.worklog.datamodel.WorkLogData;
import com.noga.worklog.datamodel.WorkLogItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;

import java.io.IOException;
import java.security.Key;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Controller {
    private List<WorkLogItem> workLogItems;

    @FXML
    private ListView<WorkLogItem> workLogItemListView;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    public void initialize() {
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WorkLogItem item = workLogItemListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                WorkLogItem item = workLogItemListView.getSelectionModel().getSelectedItem();
                editItem(item);
            }
        });


        listContextMenu.getItems().addAll(deleteMenuItem);
        listContextMenu.getItems().addAll(editMenuItem);

        workLogItemListView.setItems(WorkLogData.getInstance().getWorkLogItems());
        workLogItemListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        workLogItemListView.getSelectionModel().selectFirst();
        DecimalFormat format = new DecimalFormat("0.##");

        workLogItemListView.setCellFactory(new Callback<ListView<WorkLogItem>, ListCell<WorkLogItem>>() {
            @Override
            public ListCell<WorkLogItem> call(ListView<WorkLogItem> param) {
                ListCell<WorkLogItem> cell = new ListCell<WorkLogItem>() {
                    @Override
                    protected void updateItem(WorkLogItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else if (item.getNotes().isBlank()) {
                            setText("Date: " + item.getDate() + "\t\t\t Wages: " + format.format(item.getWagesTimesHours()) + "\t\t\t Hours: " + item.getHours());
                        } else {
                            setText("Date: " + item.getDate() + "\t\t\t Wages: " + format.format(item.getWagesTimesHours()) + "\t\t\t Hours: " + item.getHours() + "\t\t\t" + item.getNotes());
                        }
                    }

                };

                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );

                return cell;
            }
        });

    }
    //Key configuration

    @FXML
    public void handleOnKeyPressed(KeyEvent keyEvent) {
        WorkLogItem selectedItem = workLogItemListView.getSelectionModel().getSelectedItem();
        if ((keyEvent.isControlDown()) && (keyEvent.getCode().equals(KeyCode.N))) {
            showNewItemDialog();
        } else if ((keyEvent.getCode().equals(KeyCode.DELETE))) {
            deleteItem(selectedItem);
        }
    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New WorkLog Item");
        dialog.setHeaderText("Use this dialog to create a new work log item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("workLogItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            WorkLogItem newItem = controller.processResult();
            workLogItemListView.getSelectionModel().select(newItem);;
        }
    }


    public void deleteItem(WorkLogItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Work Log Item");
        alert.setTitle("Delete Item from: " + item.getDate());
        alert.setContentText("Are You sure ? Press OK to confirm, or cancel to Back out.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            WorkLogData.getInstance().deleteWorkLogItem(item);
        }
    }

    public void editItem(WorkLogItem item) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit WorkLog Item");
        dialog.setHeaderText("Use this dialog to edit work log item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("workLogItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            WorkLogItem newItem = controller.processResult();
            workLogItemListView.getSelectionModel().select(newItem);
        }
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            WorkLogData.getInstance().deleteWorkLogItem(item);
        }
    }

    @FXML
    public void exitApp() {
        Platform.exit();
    }
}
