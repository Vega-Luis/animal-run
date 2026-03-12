module com.vegaluis.animalrun {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.vegaluis.animalrun to javafx.fxml;
    exports com.vegaluis.animalrun;
}
