package cl.duoc.nora.estudiantes.controller;

import cl.duoc.nora.estudiantes.model.Estudiante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import cl.duoc.nora.estudiantes.dao.EstudianteDAO;
import cl.duoc.nora.estudiantes.dao.EstudianteDAOJson;

public class MainController {
    @FXML private TableView<Estudiante> tblEstudiantes;
    @FXML private TableColumn<Estudiante, Integer> colId;
    @FXML private TableColumn<Estudiante, String> colNombre;
    @FXML private TableColumn<Estudiante, String> colCorreo;
    @FXML private TableColumn<Estudiante, String> colCarrera;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtCarrera;

    private final EstudianteDAO dao = new EstudianteDAOJson();
    private final ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList();
    private int siguienteId = 1;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));
        tblEstudiantes.setItems(estudiantes);

        estudiantes.setAll(dao.listar());
        siguienteId = estudiantes.stream().mapToInt(Estudiante::getId).max().orElse(0) + 1;

        tblEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            if (actual != null) {
                txtNombre.setText(actual.getNombre());
                txtCorreo.setText(actual.getCorreo());
                txtCarrera.setText(actual.getCarrera());
            }
        });
    }

    @FXML
    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        String carrera = txtCarrera.getText().trim();

        if (nombre.isBlank() || correo.isBlank() || carrera.isBlank()) {
            alerta("Validación", "Todos los campos son obligatorios.");
            return;
        }
        if (!correo.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            alerta("Validación", "Ingresa un correo válido.");
            return;
        }

        Estudiante seleccionado = tblEstudiantes.getSelectionModel().getSelectedItem();
        Estudiante estudiante;
        if (seleccionado == null) {
            estudiante = new Estudiante(siguienteId++, nombre, correo, carrera);
        } else {
            estudiante = new Estudiante(seleccionado.getId(), nombre, correo, carrera);
        }
        dao.guardar(estudiante);
        estudiantes.setAll(dao.listar());

        limpiarFormulario();
    }

    @FXML
    private void eliminar() {
        Estudiante seleccionado = tblEstudiantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            alerta("Eliminar", "Selecciona un estudiante.");
            return;
        }
        dao.eliminar(seleccionado.getId());
        estudiantes.setAll(dao.listar());
        limpiarFormulario();
    }

    private void alerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    @FXML private void limpiar() { limpiarFormulario(); }

    private void limpiarFormulario() {
        txtNombre.clear(); txtCorreo.clear(); txtCarrera.clear();
        tblEstudiantes.getSelectionModel().clearSelection();
    }
}