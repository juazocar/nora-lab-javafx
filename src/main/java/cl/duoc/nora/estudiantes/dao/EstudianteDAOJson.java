package cl.duoc.nora.estudiantes.dao;

import cl.duoc.nora.estudiantes.model.Estudiante;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAOJson implements EstudianteDAO {
    private final Path archivo = Path.of("data", "estudiantes.json");
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<Estudiante> datos = new ArrayList<>();

    public EstudianteDAOJson() {
        cargar();
    }

    private void cargar() {
        try {
            Files.createDirectories(archivo.getParent());
            if (Files.exists(archivo) && Files.size(archivo) > 0) {
                datos.clear();
                datos.addAll(mapper.readValue(archivo.toFile(), new TypeReference<List<Estudiante>>() {}));
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo JSON", e);
        }
    }

    private void persistir() {
        try {
            Files.createDirectories(archivo.getParent());
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo.toFile(), datos);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo JSON", e);
        }
    }

    @Override
    public List<Estudiante> listar() {
        return new ArrayList<>(datos);
    }

    @Override
    public void guardar(Estudiante estudiante) {
        int indice = -1;
        for (int i = 0; i < datos.size(); i++) {
            if (datos.get(i).getId() == estudiante.getId()) { indice = i; break; }
        }
        if (indice >= 0) datos.set(indice, estudiante);
        else datos.add(estudiante);
        persistir();
    }

    @Override
    public void eliminar(int id) {
        datos.removeIf(e -> e.getId() == id);
        persistir();
    }
}