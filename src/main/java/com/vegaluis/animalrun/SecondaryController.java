package com.vegaluis.animalrun;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;


public class SecondaryController extends Thread{
    private long camelTime;
    private long ostrichTime;
    private long deerTime;
    private ImageView camel;
    private ImageView ostrich;
    private ImageView deer;
    private int fieldSize = 924;
    @FXML
    private Pane root;

    @FXML
    private Button startRaceButton;
    
    @FXML
    private Slider camelStep;

    @FXML
    private  Label camelStepValue;

    @FXML
    private Slider camelCadence;

    @FXML
    private Label camelCadenceValue;

    @FXML
    private Slider ostrichStep;

    @FXML
    private Label ostrichStepValue;

    @FXML
    private Slider ostrichCadence;

    @FXML
    private Label ostrichCadenceValue;

    @FXML
    private Slider deerStep;

    @FXML
    private Label deerStepValue;

    @FXML
    private Slider deerCadence;

    @FXML
    private Label deerCadenceValue;

    @FXML
    private void initialize() {
        
        Image camelImage = new Image(
            getClass().getResource("/com/vegaluis/animalrun/sprites/camel.png").toExternalForm()
        );
        Image ostrichImage = new Image(
            getClass().getResource("/com/vegaluis/animalrun/sprites/ostrich.png").toExternalForm()
        );
        Image deerImage = new Image(
            getClass().getResource("/com/vegaluis/animalrun/sprites/deer.png").toExternalForm()
        );
        this.camel = new ImageView(camelImage);
        this.ostrich = new ImageView(ostrichImage);
        this.deer = new ImageView(deerImage);

        this.camel.setFitWidth(100);
        this.camel.setFitHeight(100);
        this.ostrich.setFitWidth(100);
        this.ostrich.setFitHeight(100);
        this.deer.setFitWidth(100);
        this.deer.setFitHeight(100);
        this.alignAnimals();
        root.getChildren().add(this.camel);
        root.getChildren().add(this.ostrich);
        root.getChildren().add(this.deer);
        linkSpinners();
    }

    private void linkSpinners() {
        camelStepValue.textProperty().bind(camelStep.valueProperty().asString("%.0f"));
        camelCadenceValue.textProperty().bind(camelCadence.valueProperty().asString("%.0f"));
        ostrichStepValue.textProperty().bind(ostrichStep.valueProperty().asString("%.0f"));
        ostrichCadenceValue.textProperty().bind(ostrichCadence.valueProperty().asString("%.0f"));
        deerStepValue.textProperty().bind(deerStep.valueProperty().asString("%.0f"));
        deerCadenceValue.textProperty().bind(deerCadence.valueProperty().asString("%.0f"));
    }

    private void alignAnimals() {
        this.camel.setY(50);
        this.ostrich.setY(150);
        this.deer.setY(250);
        this.camel.setX(0);
        this.ostrich.setX(0);
        this.deer.setX(0);
    }

    @FXML
    private void startRace() {
        this.startRaceButton.setDisable(true);
        this.alignAnimals();
        long startTime = System.currentTimeMillis();
        this.camelTime = 0;
        this.ostrichTime = 0;
        this.deerTime = 0;

        Thread thread1 = new Thread(() -> {
            while(this.camel.getX() < this.fieldSize) {
                try {
                    Thread.sleep((long) camelCadence.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(()-> this.camel.setX(this.camel.getX() + camelStep.getValue()));
            }
            this.camelTime = System.currentTimeMillis() - startTime;

        });

        Thread thread2 = new Thread(() -> {
            while(this.ostrich.getX() < this.fieldSize) {
                try {
                    Thread.sleep((long) ostrichCadence.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(()-> this.ostrich.setX(this.ostrich.getX() + ostrichStep.getValue()));
            }
            this.ostrichTime = System.currentTimeMillis() - startTime;
        });
        Thread thread3 = new Thread(() -> {
            while(this.deer.getX() < this.fieldSize) {
                try {
                    Thread.sleep((long) deerCadence.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(()-> this.deer.setX(this.deer.getX() + deerStep.getValue()));
            }
            this.deerTime = System.currentTimeMillis() - startTime;
        });

        Thread monitor = new Thread(() -> {
            try {
                thread1.join();
                thread2.join();
                thread3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<String[]> results = new ArrayList<>();
        Platform.runLater(() -> this.startRaceButton.setDisable(false));

    results.add(new String[]{"Venado", String.valueOf(deerTime)});
    results.add(new String[]{"Camello", String.valueOf(camelTime)});
    results.add(new String[]{"Avestruz", String.valueOf(ostrichTime)});

    results.sort(Comparator.comparingLong(r -> Long.parseLong(r[1])));

    StringBuilder text = new StringBuilder();

    for (int i = 0; i < results.size(); i++) {
        text.append(i + 1)
            .append("° ")
            .append(results.get(i)[0])
            .append(" - ")
            .append(results.get(i)[1])
            .append(" ms\n");
    }

    Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resultados");
        alert.setHeaderText("Resultados de la carrera");
        alert.setContentText(text.toString());
        alert.showAndWait();
    });

        });

        thread1.start();
        thread2.start();
        thread3.start();
        monitor.start();
    }

}