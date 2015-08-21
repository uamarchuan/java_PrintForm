package app;
import java.sql.*;
import javax.swing.*;

public class sqliteConnection {
	Connection conn=null;
	public static Connection dbConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conn=DriverManager.getConnection("jdbc:sqlite::resource:main.db");
//			JOptionPane.showMessageDialog(null, "Connection successful");
			return conn;
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
	}
}