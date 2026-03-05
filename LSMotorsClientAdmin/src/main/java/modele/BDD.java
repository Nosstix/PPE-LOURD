package modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDD {
    private final String url;
    private final String user;
    private final String password;

    public BDD() {
        String host = AppConfig.get("db.host");
        int port = AppConfig.getInt("db.port", 3306);
        String db = AppConfig.get("db.name");
        this.user = AppConfig.get("db.user");
        this.password = AppConfig.getOrDefault("db.password", "");
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&serverTimezone=Europe/Paris&allowPublicKeyRetrieval=true";
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
