package repository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import database.DatabaseManager;
import database.DatabaseService;
import models.Pais;

public class PaisesRepositoryImpl implements PaisesRepository {

    private final DatabaseManager db;

    private final Logger logger = LoggerFactory.getLogger(PaisesRepositoryImpl.class.getName());

    public PaisesRepositoryImpl() {
        this.db = new DatabaseService();
    }

    public PaisesRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }

    @Override 
    public List<Pais> findAll() {
        logger.debug("Buscando todos los paises");

        String sql = "SELECT * FROM Paises";
        return db.select(sql).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override 
    public Pais findById(int id) {
        logger.debug("Buscando pais por id: " + id);

        String sql = "SELECT * FROM Paises WHERE id = ?";
        List<Map<String, Object>> result = db.select(sql, id);
        if (result.isEmpty()) {
            return null;
        }
        return toModel(result.get(0));
    }

    @Override 
    public Pais save(Pais item) {
        logger.debug("Guardando pais: " + item);

        LocalDateTime timeStamp = LocalDateTime.now();

        String sql = "INSERT INTO Paises (nombre, fundacion, poblacion, continente, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        Object generatedId = db.insertAndGetId(
                sql,
                item.getNombre(),
                item.getFundacion(),
                item.getPoblacion(),
                item.getContinente(),
                timeStamp,
                timeStamp
        );

        int id = ((BigInteger) generatedId).intValue(); // Ajusta el cast según tipo devuelto
        return new Pais(
                id,
                item.getNombre(),
                item.getFundacion(),
                item.getPoblacion(),
                item.getContinente(),
                timeStamp,
                timeStamp
        );
    }

    @Override 
    public Pais update(int id, Pais item) {
        logger.debug("Actualizando pais por id: " + id);

        Pais pais = findById(id);
        if (pais == null) {
            return null;
        }

        LocalDateTime timeStamp = LocalDateTime.now();

        String sql = "UPDATE Paises SET nombre = ?, fundacion = ?, poblacion = ?, continente = ?, updated_at = ? WHERE id = ?";

        int result = db.update(
                sql,
                item.getNombre(),
                item.getFundacion(),
                item.getPoblacion(),
                item.getContinente(),
                timeStamp,
                id
        );

        if (result <= 0) {
            return null;
        }

        return new Pais(
                id,
                item.getNombre(),
                item.getFundacion(),
                item.getPoblacion(),
                item.getContinente(),
                item.getCreatedAt(),
                timeStamp
        );
    }

    @Override 
    public Pais delete(int id) {
        logger.debug("Borrando pais por id: " + id);

        Pais pais = findById(id);
        if (pais == null) {
            return null;
        }

        String sql = "DELETE FROM Paises WHERE id = ?";
        int result = db.delete(sql, id);
        if (result <= 0) {
            return null;
        }

        return new Pais(
                id,
                pais.getNombre(),
                pais.getFundacion(),
                pais.getPoblacion(),
                pais.getContinente(),
                pais.getCreatedAt(),
                LocalDateTime.now()
        );
    }

    @Override
    public List<Pais> findAllPaginated(int page, int size) {
        logger.debug("Buscando todos los paises paginados, página: " + page + ", tamaño: " + size);

        int myPage = (page < 1) ? 0 : page - 1;
        int mySize = (size < 1) ? 10 : size;

        String sql = "SELECT * FROM Paises LIMIT ? OFFSET ?";
        return db.select(sql, mySize, myPage * mySize).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pais> findByNombre(String nombre) {
        logger.debug("Buscando paises por nombre: " + nombre);

        String sql = "SELECT * FROM Paises WHERE LOWER(nombre) LIKE LOWER(?)";
        return db.select(sql, "%" + nombre + "%").stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pais> findByPoblacion(Long poblacion) {
        logger.debug("Buscando paises por calificación: " + poblacion);

        String sql = "SELECT * FROM Paises WHERE poblacion = ?";
        return db.select(sql, poblacion).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pais> findByContinente(String continente) {
        logger.debug("Buscando paises por continente: " + continente);

        String sql = "SELECT * FROM Paises WHERE continente = ?";
        return db.select(sql, continente).stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long mediaPoblaciones() {
        logger.debug("Calculando media de poblaciones");

        String sql = "SELECT AVG(poblacion) FROM Paises";
        List<Map<String, Object>> result = db.select(sql);
        if (!result.isEmpty()) {
            Object val = result.get(0).values().iterator().next();
            if (val instanceof Number) {
                return ((Number) val).longValue();
            }
        }
        return 0l;
    }

    @Override
    public Long maximaPoblacion() {
        logger.debug("Calculando máxima poblacion");

        String sql = "SELECT MAX(poblacion) FROM Paises";
        List<Map<String, Object>> result = db.select(sql);
        if (!result.isEmpty()) {
            Object val = result.get(0).values().iterator().next();
            if (val instanceof Number) {
                return ((Number) val).longValue();
            }
        }
        return 0l;
    }

    @Override
    public Long minimaPoblacion() {
        logger.debug("Calculando mínima calificación");

        String sql = "SELECT MIN(poblacion) FROM Paises";
        List<Map<String, Object>> result = db.select(sql);
        if (!result.isEmpty()) {
            Object val = result.get(0).values().iterator().next();
            if (val instanceof Number) {
                return ((Number) val).longValue();
            }
        }
        return 0l;
    }

    // Conversión del resultado del DB a modelo Estudiante
    private Pais toModel(Map<String, Object> entity) {
        return new Pais(
                (int) entity.get("id"),
                (String) entity.get("nombre"),
                (int) entity.get("fundacion"),
                (Long) entity.get("poblacion"),
                (String) entity.get("continente"),
                LocalDateTime.parse(((String) entity.get("created_at")).replace(' ','T')), // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS")),
                LocalDateTime.parse(((String) entity.get("updated_at")).replace(' ','T')) // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS"))
        );
    }
}
