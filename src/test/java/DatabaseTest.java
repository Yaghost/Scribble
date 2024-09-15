import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {

    @Test
    public void testConnection() throws RuntimeException {
        Connection con = null;

        try {
            String url = "jdbc:sqlite:scribble_db.sqlite";
            con = DriverManager.getConnection(url);

            assertNotNull(con, "A conexão com o banco de dados não deve ser nula.");
        } catch (SQLException ignored) {
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignored) {}
            }
        }
    }

}
