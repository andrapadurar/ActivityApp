package com.example.practic;

import Domain.Activitate;
import Repository.FileRepository;
import Service.Service;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class HelloApplication extends Application {
    private Service service;

    @Override
    public void start(Stage stage) {
        FileRepository repository = new FileRepository("activitati.txt");
        service = new Service(repository);

        ListView<Activitate> activityListView = new ListView<>();
        ObservableList<Activitate> activities = FXCollections.observableArrayList();
        service.getAllActivitati().stream()
                .sorted(Comparator.comparing(Activitate::getZi))
                .forEach(activities::add);
        activityListView.setItems(activities);

        Label ziLabel = new Label("Ziua");
        Label pasiLabel = new Label("Număr de pași");
        Label somnLabel = new Label("Ore de somn");
        Label descriereLabel = new Label("Descriere activitate");
        Label minuteLabel = new Label("Minute");
        Button addActivityButton = new Button("Adaugă Activitate");
        Button filterButton = new Button("Filtrează");

        TextField ziTextField = new TextField();
        TextField pasiTextField = new TextField();
        TextField somnTextField = new TextField();
        TextField descriereTextField = new TextField();
        TextField minuteTextField = new TextField();
        TextField minMinutesTextField = new TextField();
        TextField maxMinutesTextField = new TextField();

        GridPane activityInputPane = new GridPane();
        activityInputPane.add(ziLabel, 0, 0);
        activityInputPane.add(ziTextField, 1, 0);
        activityInputPane.add(pasiLabel, 0, 1);
        activityInputPane.add(pasiTextField, 1, 1);
        activityInputPane.add(somnLabel, 0, 2);
        activityInputPane.add(somnTextField, 1, 2);
        activityInputPane.add(descriereLabel, 0, 3);
        activityInputPane.add(descriereTextField, 1, 3);
        activityInputPane.add(minuteLabel, 0, 4);
        activityInputPane.add(minuteTextField, 1, 4);
        activityInputPane.add(addActivityButton, 1, 5);

        GridPane filterPane = new GridPane();
        filterPane.add(new Label("Numărul minim de minute"), 0, 0);
        filterPane.add(minMinutesTextField, 1, 0);
        filterPane.add(new Label("Numărul maxim de minute"), 0, 1);
        filterPane.add(maxMinutesTextField, 1, 1);
        filterPane.add(filterButton, 1, 2);
        filterPane.setHgap(10);
        filterPane.setVgap(10);

        activityInputPane.setHgap(10);
        activityInputPane.setVgap(10);

        addActivityButton.setOnAction(event -> {
            try {
                LocalDate zi = LocalDate.parse(ziTextField.getText().trim(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                int pasi = Integer.parseInt(pasiTextField.getText().trim());
                int somn = Integer.parseInt(somnTextField.getText().trim());
                String descriere = descriereTextField.getText().trim();
                int minute = minuteTextField.getText().isEmpty() ? 0 : Integer.parseInt(minuteTextField.getText().trim());

                service.addOrUpdateActivitate(minute, descriere, somn, pasi, zi);
                activities.setAll(service.getAllActivitati().stream()
                        .sorted(Comparator.comparing(Activitate::getZi))
                        .toList());

                ziTextField.clear();
                pasiTextField.clear();
                somnTextField.clear();
                descriereTextField.clear();
                minuteTextField.clear();
            } catch (Exception e) {
                showErrorAlert("Eroare la adăugare/actualizare: " + e.getMessage());
            }
        });


        filterButton.setOnAction(event -> {
            try {
                Integer minMinutes = minMinutesTextField.getText().isEmpty() ? null : Integer.parseInt(minMinutesTextField.getText().trim());
                Integer maxMinutes = maxMinutesTextField.getText().isEmpty() ? null : Integer.parseInt(maxMinutesTextField.getText().trim());

                List<Activitate> filteredActivities = service.filterActivitatiByMinutes(minMinutes, maxMinutes);
                activities.setAll(filteredActivities);

            } catch (Exception e) {
                showErrorAlert("Eroare la filtrare: " + e.getMessage());
            }
        });

        VBox activityManagementBox = new VBox(10, activityInputPane, filterPane);
        activityManagementBox.setAlignment(Pos.TOP_CENTER);
        HBox mainLayout = new HBox(20, activityListView, activityManagementBox);
        HBox.setHgrow(activityManagementBox, Priority.ALWAYS);

        Scene scene = new Scene(mainLayout, 800, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // Aplicare CSS
        stage.setTitle("Gestionare Activități");
        stage.setScene(scene);
        stage.show();

    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Eroare");
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
