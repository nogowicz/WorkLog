package com.noga.worklog;

import com.noga.worklog.datamodel.WorkLogData;
import com.noga.worklog.datamodel.WorkLogItem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField hours;

    @FXML
    private TextField wages;

    @FXML
    private TextField notes;

    @FXML
    private CheckBox checkBoxButton;

    public WorkLogItem processResult() {
        LocalDate localDate = datePicker.getValue();
        String hourString = hours.getText().trim();
        String wagesString = wages.getText().trim();
        String notesString = notes.getText().trim();

        double hour = Double.parseDouble(hourString);
        double wage = Double.parseDouble(wagesString);

        WorkLogItem newItem = new WorkLogItem(localDate, hour, wage, notesString);
        WorkLogData.getInstance().addWorkLogItem(newItem);
        return newItem;
    }


    @FXML
    public void onButtonClicked() {
        if (checkBoxButton.isSelected()) {
            notes.setDisable(false);
        }else {
            notes.setDisable(true);
        }
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (InterruptedException event){

                }
            }
        };

        new Thread(task).start();

    }
}
