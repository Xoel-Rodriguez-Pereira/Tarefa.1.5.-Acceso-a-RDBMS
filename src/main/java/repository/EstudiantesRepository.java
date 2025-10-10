package dev.joseluisgs.repositories;

import dev.joseluisgs.models.Estudiante;

import java.util.List;

public interface EstudiantesRepository extends CrudRepository<Estudiante, Integer> {
    List<Estudiante> findAllPaginated(int page, int size);
    List<Estudiante> findByNombre(String nombre);
    List<Estudiante> findByCalificacion(double calificacion);
    List<Estudiante> findByRepetidor(boolean repetidor);
    double mediaCalificaciones();
    double maximaCalificacion();
    double minimaCalificacion();
    int numAprobados();
    int numSuspensos();
}
