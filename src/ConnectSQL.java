import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;


public class ConnectSQL {
    public Connection connect() {
        Dotenv dotenv = Dotenv.load();
        Connection c = null;
        try {
            c = DriverManager
                    .getConnection(dotenv.get("URL"),
                          dotenv.get("USER"), dotenv.get("PASSWORD"));
        } catch (Exception e) {
            System.out.println("ERRO BASE DE DADOS");
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return c;
    }
}
