import java.sql.*;

public class DataBaseConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/asal_beautycare";
	private static final String USER = "root";
	private static final String PASS = "9876shahd@123#";

	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASS);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// to do the queries
	public static ResultSet executeQuery(String query) {
		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
