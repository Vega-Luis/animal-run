package com.vegaluis.animalrun;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class SecondaryController extends Thread {

    private static final int FINISH_LINE = 924;

    private long camelTime;
    private long ostrichTime;
    private long deerTime;

    private ImageView camel;
    private ImageView ostrich;
    private ImageView deer;

    @FXML
    private Pane root;

    @FXML
    private Button startRaceButton;

    @FXML
    private Slider camelStep;
    @FXML
    private Slider camelCadence;

    @FXML
    private Slider ostrichStep;
    @FXML
    private Slider ostrichCadence;

    @FXML
    private Slider deerStep;
    @FXML
    private Slider deerCadence;

    @FXML
    private Label camelStepValue;
    @FXML
    private Label camelCadenceValue;

    @FXML
    private Label ostrichStepValue;
    @FXML
    private Label ostrichCadenceValue;

    @FXML
    private Label deerStepValue;
    @FXML
    private Label deerCadenceValue;

    @FXML
    private void initialize() {
        loadAnimals();

        setAnimalsSize();

        alignAnimals();

        // Agregar los animales al pane
        root.getChildren().add(this.camel);
        root.getChildren().add(this.ostrich);
        root.getChildren().add(this.deer);

        linkSpinners();
    }

    private void loadAnimals() {
        Image camelImage = new Image(
                getClass()
                        .getResource("/com/vegaluis/animalrun/sprites/camel.png")
                        .toExternalForm());

        Image ostrichImage = new Image(
                getClass()
                        .getResource("/com/vegaluis/animalrun/sprites/ostrich.png")
                        .toExternalForm());

        Image deerImage = new Image(
                getClass()
                        .getResource("/com/vegaluis/animalrun/sprites/deer.png")
                        .toExternalForm());

        this.camel = new ImageView(camelImage);
        this.ostrich = new ImageView(ostrichImage);
        this.deer = new ImageView(deerImage);
    }

    private void setAnimalsSize() {
        this.camel.setFitWidth(100);
        this.camel.setFitHeight(100);

        this.ostrich.setFitWidth(100);
        this.ostrich.setFitHeight(100);

        this.deer.setFitWidth(100);
        this.deer.setFitHeight(100);
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
        this.camel.setX(0);
        this.camel.setY(50);

        this.ostrich.setX(0);
        this.ostrich.setY(150);

        this.deer.setX(0);
        this.deer.setY(250);
    }

    private void moveAnimal(ImageView animal, Slider step) {
        double stepValue = step.getValue();

        if (Math.random() < 0.2) {
            double factor = 0.5 + Math.random();
            stepValue = stepValue * factor;
        }

        final double newX = Math.min(animal.getX() + stepValue, FINISH_LINE);

        Platform.runLater(() -> animal.setX(newX));
    }

    @FXML
    private void startRace() {
        this.startRaceButton.setDisable(true);

        alignAnimals();

        long startTime = System.currentTimeMillis();
        this.camelTime = 0;
        this.ostrichTime = 0;
        this.deerTime = 0;

        Thread camelThread = new Thread(() -> {
            while (this.camel.getX() < FINISH_LINE) {
                try {
                    Thread.sleep((long) camelCadence.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                moveAnimal(camel, camelStep);
            }
            this.camelTime = System.currentTimeMillis() - startTime;
        });

        Thread ostrichThread = new Thread(() -> {
            while (this.ostrich.getX() < FINISH_LINE) {
                try {
                    Thread.sleep((long) ostrichCadence.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                moveAnimal(ostrich, ostrichStep);
            }
            this.ostrichTime = System.currentTimeMillis() - startTime;
        });

        Thread deerThread = new Thread(() -> {
            while (this.deer.getX() < FINISH_LINE) {
                try {
                    Thread.sleep((long) deerCadence.getValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                moveAnimal(deer, deerStep);
            }
            this.deerTime = System.currentTimeMillis() - startTime;
        });

        Thread monitorThread = new Thread(() -> {
            try {
                camelThread.join();
                ostrichThread.join();
                deerThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<String[]> results = new ArrayList<>();
            Platform.runLater(() -> this.startRaceButton.setDisable(false));

            results.add(new String[] { "Venado", String.valueOf(deerTime) });
            results.add(new String[] { "Camello", String.valueOf(camelTime) });
            results.add(new String[] { "Avestruz", String.valueOf(ostrichTime) });

            results.sort(Comparator.comparingLong(r -> Long.parseLong(r[1])));

            StringBuilder text = new StringBuilder();

            text.append("El ganador es el " + results.get(0)[0] + " con un tiempo de " + results.get(0)[1] + " ms\n\n");

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

        camelThread.start();
        ostrichThread.start();
        deerThread.start();
        monitorThread.start();
    }

}