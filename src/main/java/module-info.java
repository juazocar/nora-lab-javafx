module cl.duoc.nora.estudiantes {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    exports cl.duoc.nora.estudiantes;
    exports cl.duoc.nora.estudiantes.model;

    opens cl.duoc.nora.estudiantes.controller to javafx.fxml;
    opens cl.duoc.nora.estudiantes.model to com.fasterxml.jackson.databind;
}