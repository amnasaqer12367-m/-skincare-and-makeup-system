import src.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperationsDiscount {

    public static double getDiscountAmount(double subtotal) {

        String sql = """
            SELECT discount_percentage
            FROM Discounts
            WHERE active = 1
              AND CURDATE() BETWEEN start_date AND end_date
              AND ? >= min_amount
            ORDER BY discount_percentage DESC
            LIMIT 1
        """;

        try (
                Connection con = DataBaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setDouble(1, subtotal);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double percent = rs.getDouble("discount_percentage");
                return subtotal * (percent / 100);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
