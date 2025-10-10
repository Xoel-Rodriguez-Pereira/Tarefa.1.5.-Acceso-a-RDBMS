package dev.joseluisgs.config;

import org.lighthousegames.logging.Logger;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Logger logger = Logger.Companion.logging();

    // Valores por defecto
    private static String databaseUrl = "jdbc:sqlite:alumnado.db";
    private static boolean databaseInitTables = false;
    private static boolean databaseInitData = false;
    private static String storageData = "data";

    static {
        try {
            logger.debug("Cargando configuración");
            Properties properties = new Properties();
            InputStream input = ClassLoader.getSystemResourceAsStream("config.properties");
            if (input != null) {
                properties.load(input);
                databaseUrl = properties.getProperty("database.url", databaseUrl);
                databaseInitTables = Boolean.parseBoolean(properties.getProperty("database.init.tables", String.valueOf(databaseInitTables)));
                databaseInitData = Boolean.parseBoolean(properties.getProperty("database.init.data", String.valueOf(databaseInitData)));
                storageData = properties.getProperty("storage.data", storageData);
                logger.debug("Configuración cargada correctamente");
            } else {
                logger.error("Archivo de configuración 'config.properties' no encontrado");
                logger.error("Usando valores por defecto");
            }
        } catch (Exception e) {
            logger.error("Error cargando configuración: " + e.getMessage());
            logger.error("Usando valores por defecto");
        }
    }

    public static String getDatabaseUrl() {
        return databaseUrl;
    }

    public static boolean getDatabaseInitTables() {
        return databaseInitTables;
    }

    public static boolean getDatabaseInitData() {
        return databaseInitData;
    }

    public static String getStorageData() {
        return storageData;
    }
}
