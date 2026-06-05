import src.Product;
import src.Warehouse;

public class Inventory {
    private int inventory_id;
    private int quantity;
    private Warehouse warehouse;
    private Product product;

    public Inventory(int inventory_id, int quantity, Warehouse warehouse, Product product) {
        this.inventory_id = inventory_id;
        this.warehouse = warehouse;
        this.product = product;
        this.quantity = quantity;
    }



    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString() {
        return "Inventory{" +
                "inventory_id=" + inventory_id +
                ", warehouse=" + warehouse +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

}
