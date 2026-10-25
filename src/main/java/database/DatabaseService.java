package database;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.Config;

public class DatabaseService implements DatabaseManager {

    private final Logger logger = LoggerFactory.getLogger(DatabaseService.class.getName());

    private Connection connection = null;

    public DatabaseService() {
        initConexion();
        if (Config.getDatabaseInitTables()) {
            initTablas();
        }
        if (Config.getDatabaseInitData()) {
            initData();
        }
    }

    private void initConexion() {
        logger.debug("Iniciando conexión con la base de datos");
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(Config.getDatabaseUrl(), Config.getDatabaseUser(), Config.getDatabasePassword());
            }
            logger.debug("Conexión con la base de datos inicializada");
        } catch (SQLException e) {
            logger.error("Error al iniciar la conexión: " + e.getMessage());
        }
    }

    private void initTablas() {
        logger.debug("Creando tablas");
        try {
            URL resourceUrl = ClassLoader.getSystemResource("tables.sql");
            Path path = Paths.get(resourceUrl.toURI());
            String tables = Files.readString(path);
            PreparedStatement statement = connection.prepareStatement(tables);
            statement.executeUpdate();

            logger.debug("Tabla estudiantes creada");
        } catch (Exception e) {
            logger.error("Error al crear las tablas: " + e.getMessage());
        }
    }

    private void initData() {
        logger.debug("Iniciando carga de datos");
        try {
            URL resourceUrl = ClassLoader.getSystemResource("data.sql");
            Path path = Paths.get(resourceUrl.toURI());
            String data = Files.readString(path);
            PreparedStatement statement = connection.prepareStatement(data);
            statement.executeUpdate();
            logger.debug("Datos cargados");
        } catch (Exception e) {
            logger.error("Error al cargar los datos: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> select(String sql, Object... params) {
        logger.debug("Ejecutando consulta: " + sql);
        List<Map<String, Object>> result = new ArrayList<>();
        initConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData meta = resultSet.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnName(i), resultSet.getObject(i));
                }
                result.add(row);
            }
        } catch (SQLException e) {
            logger.error("Error en la consulta: " + e.getMessage());
        }

        logger.debug("Consulta finalizada con " + result.size() + " resultados");
        return result;
    }

    @Override
    public int insert(String sql, Object... params) {
        logger.debug("Ejecutando inserción: " + sql);
        int result = 0;
        initConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error en la inserción: " + e.getMessage());
        }

        logger.debug("Inserción finalizada con " + result + " filas afectadas");
        return result;
    }

    @Override
    public Object insertAndGetId(String sql, Object... params) {
        logger.debug("Ejecutando inserción y obteniendo ID: " + sql);
        Object result = 0;
        initConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(statement, params);
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                result = generatedKeys.getObject(1);
            }
        } catch (SQLException e) {
            logger.error("Error al insertar y obtener ID: " + e.getMessage());
        }

        logger.debug("Inserción finalizada con ID: " + result);
        return result;
    }

    @Override
    public int update(String query, Object... params) {
        logger.debug("Ejecutando actualización: " + query);
        int result = 0;
        initConexion();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al actualizar: " + e.getMessage());
        }
        logger.debug("Actualización finalizada con " + result + " filas afectadas");
        return result;
    }

    @Override
    public int delete(String query, Object... params) {
        logger.debug("Ejecutando borrado: " + query);
        int result = 0;
        initConexion();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParameters(statement, params);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al borrar: " + e.getMessage());
        }
        logger.debug("Borrado finalizado con " + result + " filas afectadas");
        return result;
    }

    private void setParameters(PreparedStatement statement, Object[] params) throws SQLException {
        logger.debug("Pasando parámetros a la consulta: " + Arrays.toString(params));
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}