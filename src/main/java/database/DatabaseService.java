package database;

import dev.joseluisgs.config.Config;
import dev.joseluisgs.database.DatabaseManager;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.lighthousegames.logging.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.*;
import java.util.*;

public class DatabaseService implements DatabaseManager {
    private static final Logger logger = Logger.Companion.logging();

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
                connection = DriverManager.getConnection(Config.getDatabaseUrl());
            }
            logger.debug("Conexión con la base de datos inicializada");
        } catch (SQLException e) {
            logger.error("Error al iniciar la conexión: " + e.getMessage());
        }
    }

    private void initTablas() {
        logger.debug("Creando tablas");
        try (Reader tablas = new BufferedReader(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("tables.sql")))) {
            scriptRunner(tablas, true);
            logger.debug("Tabla estudiantes creada");
        } catch (Exception e) {
            logger.error("Error al crear las tablas: " + e.getMessage());
        }
    }

    private void initData() {
        logger.debug("Iniciando carga de datos");
        try (Reader data = new BufferedReader(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("data.sql")))) {
            scriptRunner(data, true);
            logger.debug("Datos cargados");
        } catch (Exception e) {
            logger.error("Error al cargar los datos: " + e.getMessage());
        }
    }

    private void scriptRunner(Reader reader, boolean logWriter) {
        logger.debug("Ejecutando script SQL con log: " + logWriter);
        try {
            ScriptRunner sr = new ScriptRunner(connection);
            sr.setLogWriter(logWriter ? new PrintWriter(System.out) : null);
            sr.runScript(reader);
        } catch (Exception e) {
            logger.error("Error al ejecutar el script SQL: " + e.getMessage());
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
    public int update(String sql, Object... params) {
        logger.debug("Ejecutando actualización:
