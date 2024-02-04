module ru.a1pha {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.a1pha to javafx.fxml;
    exports ru.a1pha;
}
