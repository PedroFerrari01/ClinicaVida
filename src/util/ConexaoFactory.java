package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Centraliza a criação de conexões JDBC.
 * As credenciais ficam em src/config.properties (fora do código-fonte),
 * facilitando trocar de MySQL para outro banco sem recompilar as classes.
 */
public class ConexaoFactory {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConexaoFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            } else {
                // valores padrão caso o arquivo não seja encontrado
                props.setProperty("db.url", "jdbc:mysql://localhost:3306/clinica_vida?useSSL=false&serverTimezone=UTC");
                props.setProperty("db.user", "clinica_app");
                props.setProperty("db.password", "clinica123");
                props.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            }
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar config.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(props.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado no classpath: " + props.getProperty("db.driver"), e);
        }
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }
}
