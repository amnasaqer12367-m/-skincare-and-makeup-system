import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.Category;
import src.DataBaseConnection;
import src.Product;

import java.sql.*;

public class DatabaseOperationsProduct {

    public static boolean addProduct(Product product) {
        String sql = "INSERT INTO Product (category_id, Product_name, price, brand, Product_description) " +
                "VALUES (?, ?, ?, ?, ?)";  // حذف stock_status

        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, product.getCategory().getCategoryId());
            pstmt.setString(2, product.getProduct_name());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setString(4, product.getBrand());
            pstmt.setString(5, product.getProduct_description());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProduct_id(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET category_id = ?, Product_name = ?, price = ?, " +
                "brand = ?, Product_description = ? WHERE product_id = ?"; // حذف stock_status

        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, product.getCategory().getCategoryId());
            pstmt.setString(2, product.getProduct_name());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setString(4, product.getBrand());
            pstmt.setString(5, product.getProduct_description());
            pstmt.setInt(6, product.getProduct_id());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteProduct(int productId) {
        String sql = "DELETE FROM Product WHERE product_id = ?";

        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableList<Product> getAllProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Product ORDER BY product_id";

        try (Connection con = DataBaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("Product_name"),
                        rs.getDouble("price"),
                        rs.getString("brand"),
                        rs.getString("Product_description"),
                        new Category(rs.getInt("category_id"), "", "")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static Product getProductById(int productId) {
        String sql = "SELECT * FROM Product WHERE product_id = ?";

        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id"),
                        rs.getString("Product_name"),
                        rs.getDouble("price"),
                        rs.getString("brand"),
                        rs.getString("Product_description"),
                        new Category(rs.getInt("category_id"), "", "")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ObservableList<Product> searchProducts(String keyword) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Product WHERE " +
                "Product_name LIKE ? OR " +
                "brand LIKE ? OR " +
                "Product_description LIKE ? " +
                "ORDER BY Product_name";

        try (Connection con = DataBaseConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("product_id"),
                        rs.getString("Product_name"),
                        rs.getDouble("price"),
                        rs.getString("brand"),
                        rs.getString("Product_description"),
                        new Category(rs.getInt("category_id"), "", "")
                );
                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static int getProductCount() {
        String sql = "SELECT COUNT(*) as count FROM Product";

        try (Connection con = DataBaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("count");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
