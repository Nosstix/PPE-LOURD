package modele;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("config.properties introuvable dans classpath");
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger config.properties : " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        String v = props.getProperty(key);
        if (v == null) throw new IllegalArgumentException("Clé config manquante: " + key);
        return v.trim();
    }

    public static String getOrDefault(String key, String def) {
        String v = props.getProperty(key);
        return v == null ? def : v.trim();
    }

    public static int getInt(String key, int def) {
        try { return Integer.parseInt(getOrDefault(key, String.valueOf(def))); }
        catch (Exception e) { return def; }
    }
}
