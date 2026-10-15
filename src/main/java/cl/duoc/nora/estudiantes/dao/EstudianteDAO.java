package cl.duoc.nora.estudiantes.dao;

import cl.duoc.nora.estudiantes.model.Estudiante;
import java.util.List;

public interface EstudianteDAO {
    List<Estudiante> listar();
    void guardar(Estudiante estudiante);
    void eliminar(int id);
}