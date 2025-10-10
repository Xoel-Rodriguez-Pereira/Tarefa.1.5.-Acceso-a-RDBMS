package dev.joseluisgs.repositories;

import dev.joseluisgs.database.DatabaseManager;
import dev.joseluisgs.models.Estudiante;
import database.DatabaseService;
import org.lighthousegames.logging.Logger;
import org.lighthousegames.logging.Logging;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstudiantesRepositoryImpl implements EstudiantesRepository {

    private final DatabaseManager db;
    private final Logger logger = Logging.getLogger();

    public EstudiantesRepositoryImpl() {
        this.db = new DatabaseService();
    }

    public EstudiantesRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }

    @Override
    public List<Estudiante> findAll() {
        logger.debug("Buscando todos los estudiantes");

        String sql = "SELECT * FROM Estudiantes";
        return db.select(sql).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Estudiante findById(int id) {
        logger.debug("Buscando estudiante por id: " + id);

        String sql = "SELECT * FROM Estudiantes WHERE id = ?";
        List<Map<String, Object>> result = db.select(sql, id);
        if (result.isEmpty()) {
            return null;
        }
        return toModel(result.get(0));
    }

    @Override
    public Estudiante save(Estudiante item) {
        logger.debug("Guardando estudiante: " + item);

        LocalDateTime timeStamp = LocalDateTime.now();

        String sql = "INSERT INTO Estudiantes (nombre, fecha_nacimiento, calificacion, repetidor, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        Object generatedId = db.insertAndGetId(
                sql,
                item.getNombre(),
                item.getFechaNacimiento().toString(),
                item.getCalificacion(),
                item.isRepetidor(),
                timeStamp,
                timeStamp
        );

        int id = (int) generatedId; // Ajusta el cast según tipo devuelto
        return new Estudiante(
                id,
                item.getNombre(),
                item.getFechaNacimiento(),
                item.getCalificacion(),
                item.isRepetidor(),
                timeStamp,
                timeStamp
        );
    }

    @Override
    public Estudiante update(int id, Estudiante item) {
        logger.debug("Actualizando estudiante por id: " + id);

        Estudiante estudiante = findById(id);
        if (estudiante == null) {
            return null;
        }

        LocalDateTime timeStamp = LocalDateTime.now();

        String sql = "UPDATE Estudiantes SET nombre = ?, fecha_nacimiento = ?, calificacion = ?, repetidor = ?, updated_at = ? WHERE id = ?";

        int result = db.update(
                sql,
                item.getNombre(),
                item.getFechaNacimiento().toString(),
                item.getCalificacion(),
                item.isRepetidor(),
                timeStamp,
                id
        );

        if (result <= 0) {
            return null;
        }

        return new Estudiante(
                id,
                item.getNombre(),
                item.getFechaNacimiento(),
                item.getCalificacion(),
                item.isRepetidor(),
                item.getCreatedAt(),
                timeStamp
        );
    }

    @Override
    public Estudiante delete(int id) {
        logger.debug("Borrando estudiante por id: " + id);

        Estudiante estudiante = findById(id);
        if (estudiante == null) {
            return null;
        }

        String sql = "DELETE FROM Estudiantes WHERE id = ?";
        int result = db.delete(sql, id);
        if (result <= 0) {
            return null;
        }

        return new Estudiante(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getFechaNacimiento(),
                estudiante.getCalificacion(),
                estudiante.isRepetidor(),
                estudiante.getCreatedAt(),
                LocalDateTime.now()
        );
    }

    @Override
    public List<Estudiante> findAllPaginated(int page, int size) {
        logger.debug("Buscando todos los estudiantes paginados, página: " + page + ", tamaño: " + size);

        int myPage = (page < 1) ? 0 : page - 1;
        int mySize = (size < 1) ? 10 : size;

        String sql = "SELECT * FROM Estudiantes LIMIT ? OFFSET ?";
        return db.select(sql, mySize, myPage * mySize).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Estudiante> findByNombre(String nombre) {
        logger.debug("Buscando estudiantes por nombre: " + nombre);

        String sql = "SELECT * FROM Estudiantes WHERE LOWER(nombre) LIKE LOWER(?)";
        return db.select(sql, "%" + nombre + "%").stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Estudiante> findByCalificacion(double calificacion) {
        logger.debug("Buscando estudiantes por calificación: " + calificacion);

        String sql = "SELECT * FROM Estudiantes WHERE calificacion = ?";
        return db.select(sql, calificacion).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Estudiante> findByRepetidor(boolean repetidor) {
        logger.debug("Buscando estudiantes por repetidor: " + repetidor);

        String sql = "SELECT * FROM Estudiantes WHERE repetidor = ?";
        return db.select(sql, repetidor).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public double mediaCalificaciones() {
        logger.debug("Calculando media de calificaciones");

        String sql = "SELECT AVG(calificacion) FROM Estudiantes";
        List<Map<String, Object>> result = db.select(sql);
        if (!result.isEmpty()) {
            Object val = result.get(0).values().iterator().next();
            if (val instanceof Number) {
                return ((Number) val).doubleValue();
            }
        }
        return 0.0;
    }

    @Override
    public double maximaCalificacion() {
        logger.debug("Calculando máxima calificación");

        String sql = "SELECT MAX(calificacion) FROM Estudiantes";
        List<Map<String, Object>> result = db.select(sql);
        if (!result.isEmpty()) {
            Object val = result.get(0).values().iterator().next();
            if (val instanceof Number) {
                return ((Number) val).doubleValue();
            }
        }
        return 0.0;
    }

    @Override
    public double minimaCalificacion() {
        logger.debug("Calculando mínima calificación");

        String sql = "SELECT MIN(calificacion) FROM Estudiantes";
        List<Map<String, Object>> result = db.select(sql);
        if (!result.isEmpty()) {
            Object val = result.get(0).values().iterator().next();
            if (val instanceof Number) {
                return ((Number) val).doubleValue();
            }
        }
        return 0.0;
    }

    @Override
    public int numAprobados() {
        logger.debug("Calculando número de aprobados");

        String sql = "SELECT COUNT(*) FROM Estudiantes WHERE calificacion >= 5.0";
        List<Map<String, Object>> result = db.select(sql);
        if (!result.isEmpty()) {
            Object val = result.get(0).values().iterator().next();
            if (val instanceof Number) {
                return ((Number) val).intValue();
            }
        }
        return 0;
    }

    @Override
    public int numSuspensos() {
        logger.debug("Calculando número de suspensos");

        String sql = "SELECT COUNT(*) FROM Estudiantes WHERE calificacion < 5.0";
        List<Map<String, Object>> result = db.select(sql);
        if (!result.isEmpty()) {
            Object val = result.get(0).values().iterator().next();
            if (val instanceof Number) {
                return ((Number) val).intValue();
            }
        }
        return 0;
    }

    // Conversión del resultado del DB a modelo Estudiante
    private Estudiante toModel(Map<String, Object> entity) {
        return new Estudiante(
                (int) entity.get("id"),
                (String) entity.get("nombre"),
                LocalDate.parse((String) entity.get("fecha_nacimiento")),
                (double) entity.get("calificacion"),
                ((int) entity.get("repetidor")) == 1,
                LocalDateTime.parse((String) entity.get("created_at")),
                LocalDateTime.parse((String) entity.get("updated_at"))
        );
    }
}
