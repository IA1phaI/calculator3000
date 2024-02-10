module ru.a1pha {
    requires javafx.controls;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.graphics;
    
    opens ru.a1pha to javafx.fxml;
    exports ru.a1pha;
}
