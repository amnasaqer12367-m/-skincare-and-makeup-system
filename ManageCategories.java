import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.AlertManager;
import src.Category;
import src.DatabaseOperationsCategory;

public class ManageCategories {
    
    public static TableView<Category> table;
    static ObservableList<Category> data;
    private Stage primaryStage;
    private BorderPane root;
    
    private TextField searchField;
    private ComboBox<String> searchTypeCombo;
    
    public ManageCategories(Stage primaryStage) {
        this.root = new BorderPane();
        this.primaryStage = primaryStage;
    }
    
    public void show() {
        
        root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        root.getStyleClass().add("root-pane");
        
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(10, 0, 10, 0));
        searchBox.getStyleClass().add("search-box");
        
        searchTypeCombo = new ComboBox<>();
        searchTypeCombo.getItems().addAll("ID", "Name");
        searchTypeCombo.setValue("ID");
        searchTypeCombo.setPrefWidth(100);
        searchTypeCombo.getStyleClass().add("search-combo");
        
        searchField = new TextField();
        searchField.setPromptText("Enter ID or Name...");
        searchField.setPrefWidth(200);
        searchField.getStyleClass().add("search-field");
        
        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("button-normal");
        
        Button showAllButton = new Button("Show All");
        showAllButton.getStyleClass().add("button-normal");
        
        searchBox.getChildren().addAll(
            new Label("Search by:"), 
            searchTypeCombo, 
            searchField, 
            searchButton,
            showAllButton
        );
        
        HBox buttonBox = new HBox(20);
        buttonBox.getStyleClass().add("button-box");
        
        Button addButton = createStyledButton("Add Category", "button-normal");
        Button editButton = createStyledButton("Edit Category", "button-normal");
        Button deleteButton = createStyledButton("Delete Category", "button-normal");
        Button backButton = createStyledButton("Back", "button-back");
        
        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, backButton);
        
        Label titleLabel = new Label("Categories Management");
        titleLabel.getStyleClass().add("title-label");
        
        VBox titleBox = new VBox(10);
        titleBox.getStyleClass().add("title-box");
        titleBox.getChildren().addAll(titleLabel);
        
        Label categoryCountLabel = new Label();
        categoryCountLabel.getStyleClass().add("product-count-label");
        
        table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
       TableColumn<Category, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        id.setMinWidth(50);
        id.getStyleClass().add("column-center");
        
        TableColumn<Category, String> name = new TableColumn<>("Category Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setMinWidth(200);
        
        TableColumn<Category, String> description = new TableColumn<>("Description");
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        description.setMinWidth(300);
        
        TableColumn<Category, Integer> productsCount = new TableColumn<>("Products");
      
        productsCount.setMinWidth(80);
        productsCount.getStyleClass().add("column-center");
        
        table.getColumns().addAll(id, name, description, productsCount);
        
        data = FXCollections.observableArrayList();
        table.setItems(data);
        
        refreshTable();
        updateCategoryCountLabel(categoryCountLabel);
        
        searchButton.setOnAction(e -> {
            performSearch();
            updateCategoryCountLabel(categoryCountLabel);
        });
        
        showAllButton.setOnAction(e -> {
            searchField.clear();
            refreshTable();
            updateCategoryCountLabel(categoryCountLabel);
        });
        
        searchField.setOnAction(e -> {
            performSearch();
            updateCategoryCountLabel(categoryCountLabel);
        });
        
        addButton.setOnAction(e -> showAddScreen());
        
         editButton.setOnAction(e -> {
            Category category = table.getSelectionModel().getSelectedItem();
            if (category == null) {
                AlertManager.showError("Please select a category to edit");
                return;
            }
            showEditScreen(category);
        });
        
        deleteButton.setOnAction(e -> {
            Category category = table.getSelectionModel().getSelectedItem();
            if (category == null) {
                AlertManager.showError("Please select a category to delete");
                return;
            }
            
            if (AlertManager.showConfirmation(
                "Are you sure you want to delete category: " + category.getName() + "?")) {
                
                boolean success = DatabaseOperationsCategory.deleteCategory(category.getCategoryId());
                if (success) {
                    refreshTable();
                    updateCategoryCountLabel(categoryCountLabel);
                    AlertManager.showInformationMessage("Category deleted successfully!");
                } else {
                    AlertManager.showError("Failed to delete category from database!");
                }
            }
        });
        
        backButton.setOnAction(e -> {
            try {
                AdminScreen dashboard = new AdminScreen(primaryStage);
                dashboard.showDashboard();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        VBox mainContent = new VBox(15);
        mainContent.getStyleClass().add("main-content");
        mainContent.getChildren().addAll(
            searchBox, 
            buttonBox, 
            titleBox, 
            categoryCountLabel, 
            table
        );
        
        root.setCenter(mainContent);
        
        Scene dashboardScene = new Scene(root, 1200, 800);
        primaryStage.setScene(dashboardScene);  
        primaryStage.show();
    }
    
    private void updateCategoryCountLabel(Label label) {
        int totalCategories = DatabaseOperationsCategory.getCategoryCount();
        String countText = "Total Categories: " + totalCategories;
        label.setText(countText);
    }
    
    private void performSearch() {
        String keyword = searchField.getText().trim();
        String searchType = searchTypeCombo.getValue();
        
        if (keyword.isEmpty()) {
            refreshTable();
            return;
        }
        
        ObservableList<Category> searchResults;
        
        if ("ID".equals(searchType)) {
            try {
                int categoryId = Integer.parseInt(keyword);
                Category category = DatabaseOperationsCategory.getCategoryById(categoryId);
                searchResults = FXCollections.observableArrayList();
                if (category != null) {
                    searchResults.add(category);
                }
            } catch (NumberFormatException e) {
                AlertManager.showError("Invalid ID \n Please enter a valid numeric ID");
                return;
            }
        } else {
            searchResults = searchCategoriesByName(keyword);
        }
        
        updateTableData(searchResults);
        
        if (searchResults.isEmpty()) {
            AlertManager.showInformationMessage("No categories found for: " + keyword);
        }
    }
    
    private ObservableList<Category> searchCategoriesByName(String keyword) {
        ObservableList<Category> allCategories = DatabaseOperationsCategory.getAllCategories();
        ObservableList<Category> filteredCategories = FXCollections.observableArrayList();
        
        for (Category category : allCategories) {
            if (category.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                category.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                filteredCategories.add(category);
            }
        }
        
        return filteredCategories;
    }
    
    private void updateTableData(ObservableList<Category> newData) {
        Platform.runLater(() -> {
            data.clear();
            data.addAll(newData);
        });
    }
    
    private Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }
    
    private void showAddScreen() {
        VBox addBox = new VBox(15);
        addBox.setPadding(new Insets(30));
        addBox.setAlignment(Pos.CENTER);
        addBox.getStyleClass().add("add-edit-box");
        
        Label title = new Label("Add New Category");
        title.getStyleClass().add("add-edit-title");
        
        Label categoryIdLabel = new Label("Category ID will be assigned automatically");
        categoryIdLabel.getStyleClass().add("info-label");
        
        TextField name = createStyledTextField("Category Name", "");
        TextArea desc = createStyledTextArea("Description", "");
        
        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        
        Button save = createPinkButton("Save", "#ff69b4", "#ff1493");
        Button back = createPinkButton("Back", "#ffb6c1", "#db7093");
        
        buttons.getChildren().addAll(save, back);
        
        addBox.getChildren().addAll(title, categoryIdLabel, name, desc, buttons);
        
        BorderPane container = new BorderPane(addBox);
        container.getStyleClass().add("add-edit-container");
        root.setCenter(container);
        
        back.setOnAction(e -> show());
        
        save.setOnAction(e -> {
            if (!validateInput(name, desc)) {
                return;
            }
            saveCategory(name, desc);
        });
    }
    
    private void showEditScreen(Category category) {
        VBox editBox = new VBox(15);
        editBox.setPadding(new Insets(30));
        editBox.setAlignment(Pos.CENTER);
        editBox.getStyleClass().add("add-edit-box");
        
        Label title = new Label("Edit Category");
        title.getStyleClass().add("add-edit-title");
        
        Label categoryIdLabel = new Label("Category ID: " + category.getCategoryId());
        categoryIdLabel.getStyleClass().add("info-label");
        
        TextField name = createStyledTextField("Category Name", category.getName());
        TextArea desc = createStyledTextArea("Description", category.getDescription());
        
        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        
        Button save = createPinkButton("Update", "#ff69b4", "#ff1493");
        Button back = createPinkButton("Cancel", "#ffb6c1", "#db7093");
        
        buttons.getChildren().addAll(save, back);
        
        editBox.getChildren().addAll(title, categoryIdLabel, name, desc, buttons);
        
        BorderPane container = new BorderPane(editBox);
        container.getStyleClass().add("add-edit-container");
        root.setCenter(container);
        
        back.setOnAction(e -> show());
        
        save.setOnAction(e -> {
            if (!validateInput(name, desc)) {
                return;
            }
            updateCategory(category, name, desc);
        });
    }
    
    private boolean validateInput(TextField name, TextArea desc) {
        if (name.getText().trim().isEmpty() || desc.getText().trim().isEmpty()) {
            AlertManager.showError("Please fill in all fields!");
            return false;
        }
        
        return true;
    }
    
    private void saveCategory(TextField name, TextArea desc) {
        Category newCategory = new Category(
            0,
            name.getText().trim(), 
            desc.getText().trim()
        );
        
        boolean success = DatabaseOperationsCategory.addCategory(newCategory);
        
        if (success) {
            String message = "Category Added Successfully!\n\n" + 
                           "Category Details:\n" + 
                           "• Category ID: " + newCategory.getCategoryId() + "\n" + 
                           "• Category Name: " + newCategory.getName() + "\n" + 
                           "• Description: " + newCategory.getDescription();
            
            AlertManager.showInformationMessage(message);
            refreshTable();
            show();
        } else {
            AlertManager.showError("Failed to add category!");
        }
    }
    
    private void updateCategory(Category originalCategory, TextField name, TextArea desc) {
        originalCategory.setName(name.getText().trim());
        originalCategory.setDescription(desc.getText().trim());
        
        boolean success = DatabaseOperationsCategory.updateCategory(originalCategory);
        
        if (success) {
            AlertManager.showInformationMessage("Category Updated Successfully!");
            refreshTable();
            show();
        } else {
            AlertManager.showError("Failed to update category!");
        }
    }
    
    public void refreshTable() {
        ObservableList<Category> allCategories = DatabaseOperationsCategory.getAllCategories();
        updateTableData(allCategories);
    }
    
   
    private Button createPinkButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + baseColor + "; " + 
                       "-fx-text-fill: white; -fx-font-size: 16px; " +
                       "-fx-padding: 12px 25px; -fx-font-weight: bold; " +
                       "-fx-background-radius: 20; -fx-border-radius: 20; " +
                       "-fx-cursor: hand; -fx-font-family: 'Arial Rounded MT Bold';");
        
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + hoverColor + "; " + 
                           "-fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-padding: 12px 25px; -fx-font-weight: bold; " +
                           "-fx-background-radius: 20; -fx-border-radius: 20; " +
                           "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + baseColor + "; " + 
                           "-fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-padding: 12px 25px; -fx-font-weight: bold; " +
                           "-fx-background-radius: 20; -fx-border-radius: 20; " + 
                           "-fx-cursor: hand;");
        });
        
        button.setOnMousePressed(e -> {
            button.setStyle("-fx-background-color: #c71585; " + 
                           "-fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-padding: 12px 25px; -fx-font-weight: bold; " +
                           "-fx-background-radius: 20; -fx-border-radius: 20; " + 
                           "-fx-cursor: hand; -fx-translate-y: 2px;");
        });
        
        button.setOnMouseReleased(e -> {
            button.setStyle("-fx-background-color: " + baseColor + "; " + 
                           "-fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-padding: 12px 25px; -fx-font-weight: bold; " +
                           "-fx-background-radius: 20; -fx-border-radius: 20; " + 
                           "-fx-cursor: hand; -fx-translate-y: 0px;");
        });
        
        return button;
    }
    
    private TextField createStyledTextField(String promptText, String initialValue) {
        TextField textField = new TextField(initialValue);
        textField.setPromptText(promptText);
        textField.setMaxWidth(350);
        textField.setPrefHeight(40);
        textField.getStyleClass().add("styled-text-field");
        return textField;
    }
    
    private TextArea createStyledTextArea(String promptText, String initialValue) {
        TextArea textArea = new TextArea(initialValue);
        textArea.setPromptText(promptText);
        textArea.setMaxWidth(350);
        textArea.setPrefHeight(120);
        textArea.setWrapText(true);
        textArea.getStyleClass().add("styled-text-area");
        return textArea;
    }
}