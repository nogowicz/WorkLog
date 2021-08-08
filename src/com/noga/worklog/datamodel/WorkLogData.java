package com.noga.worklog.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Iterator;

public class WorkLogData {
    private static WorkLogData instance = new WorkLogData();
    private static String fileName = "WorkLogItemsList.txt";

    private ObservableList<WorkLogItem> workLogItems;
    private DateTimeFormatter formatter;

    public static WorkLogData getInstance() {
        return instance;
    }

    private WorkLogData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<WorkLogItem> getWorkLogItems() {
        return workLogItems;
    }

    public void addWorkLogItem(WorkLogItem item) {
        workLogItems.add(item);
    }

    public static double roundTo2DecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public void loadTodoItems() throws IOException {

        workLogItems = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String dateString = itemPieces[0];
                String hoursString = itemPieces [1];
                String wagesString = itemPieces[2];
                String notes = itemPieces[3];

                LocalDate date = LocalDate.parse(dateString, formatter);
                double hours = Double.valueOf(hoursString);
                double wages = Double.valueOf(wagesString);


                WorkLogItem workLogItem = new WorkLogItem(date, hours, wages, notes);
                workLogItems.add(workLogItem);
            }
        }   finally {
            if (br != null) {
                br.close();
                workLogItems.sort(workLogItemComparator);
            }
        }
    }
    public void storeWorkLogItems() throws IOException {
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);

        try {
            Iterator<WorkLogItem> iter = workLogItems.iterator();
            while(iter.hasNext()) {
                WorkLogItem item = iter.next();
                bw.write(String.format("%s\t%s\t%s\t%s\t%s",
                        item.getDate().format(formatter),
                        item.getHours(),
                        item.getWages(),
                        item.getNotes(),
                        item.getWagesTimesHours()));
                bw.newLine();
            }
        }   finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public void deleteWorkLogItem(WorkLogItem item) {
        workLogItems.remove(item);
    }

    Comparator<WorkLogItem> workLogItemComparator = Comparator.comparing(WorkLogItem::getDate);
}
