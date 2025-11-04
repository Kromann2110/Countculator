module dk.easv.countculator1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens dk.easv.countculator1 to javafx.fxml;
    exports dk.easv.countculator1;
}