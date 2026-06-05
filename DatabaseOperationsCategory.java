import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import src.Category;

public class DatabaseOperationsCategory {
    
	  public static ObservableList<Category> getAllCategories() {
	        ObservableList<Category> categories = FXCollections.observableArrayList();
	        
	        categories.add(new Category(1, "Skincare", "Face creams, serums, cleansers"));
	        categories.add(new Category(2, "Makeup", "Lipstick, foundation, eyeshadow"));
	        categories.add(new Category(3, "Hair Care", "Shampoo, conditioner, hair masks"));
	        categories.add(new Category(4, "Fragrances", "Perfumes, body mists"));
	        categories.add(new Category(5, "Bath & Body", "Body wash, lotions, scrubs"));
	        
	        return categories;
	    }
	    
	    public static Category getCategoryById(int categoryId) {
	       for (Category cat : getAllCategories()) {
	            if (cat.getCategoryId() == categoryId) {
	                return cat;
	            }
	        }
	        return null;
	    }
	    
	    public static boolean addCategory(Category category) {
	      
	    	return true; 
	    }
	    
	    public static boolean updateCategory(Category category) {
	      
	    	
	    	return true; 
	    }
	    
	    public static boolean deleteCategory(int categoryId) {
	      
	    	return true; 
	    }
	    
	    public static int getCategoryCount() {
	      
	    	return getAllCategories().size();
	    }
	    
	   
	
}
