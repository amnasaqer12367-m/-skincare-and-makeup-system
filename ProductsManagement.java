import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import src.AlertManager;
import src.Category;
import src.DatabaseOperationsProduct;
import src.Product;

public class ProductsManagement {

	public static TableView<Product> table;
	static ObservableList<Product> data;
	private Stage primaryStage;
	private BorderPane root;

	private TextField searchField;
	private ComboBox<String> searchTypeCombo;

	public ProductsManagement(BorderPane root,Stage primaryStage) {
		this.root = root;
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

		searchBox.getChildren().addAll(new Label("Search by:"), searchTypeCombo, searchField, searchButton,
				showAllButton);

		HBox buttonBox = new HBox(20);
		buttonBox.getStyleClass().add("button-box");

		Button addButton = createStyledButton("Add Product", "button-normal");
		Button editButton = createStyledButton("Edit Product", "button-normal");
		Button deleteButton = createStyledButton("Delete Product", "button-normal");
		Button backButton = createStyledButton("Back", "button-back");

		buttonBox.getChildren().addAll(addButton, editButton, deleteButton, backButton);

		Label titleLabel = new Label("Products Management");
		titleLabel.getStyleClass().add("title-label");

		VBox titleBox = new VBox(10);
		titleBox.getStyleClass().add("title-box");
		titleBox.getChildren().addAll(titleLabel);

		Label productCountLabel = new Label();
		productCountLabel.getStyleClass().add("product-count-label");

		table = new TableView<>();
		table.getStyleClass().add("table-view");
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<Product, Integer> id = new TableColumn<>("ID");
		id.setCellValueFactory(new PropertyValueFactory<>("product_id"));
		id.setMinWidth(50);
		id.getStyleClass().add("column-center");

		TableColumn<Product, Integer> cat = new TableColumn<>("Category ID");
		cat.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getCategory().getCategoryId()).asObject());
		cat.setMinWidth(80);
		cat.getStyleClass().add("column-center");

		TableColumn<Product, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<>("product_name"));
		name.setMinWidth(150);

		TableColumn<Product, Double> price = new TableColumn<>("Price");
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		price.setMinWidth(80);
		price.getStyleClass().add("column-right");

		TableColumn<Product, String> brand = new TableColumn<>("Brand");
		brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
		brand.setMinWidth(100);

		TableColumn<Product, String> description = new TableColumn<>("Description");
		description.setCellValueFactory(new PropertyValueFactory<>("product_description"));
		description.setMinWidth(200);

		TableColumn<Product, Boolean> stock = new TableColumn<>("In Stock");
		stock.setCellValueFactory(new PropertyValueFactory<>("stockStatus"));
		stock.setMinWidth(80);
		stock.getStyleClass().add("column-center");

		table.getColumns().addAll(id, cat, name, price, brand, description, stock);

		data = FXCollections.observableArrayList();
		table.setItems(data);

		refreshTable();
		updateProductCountLabel(productCountLabel);

		searchButton.setOnAction(e -> {
			performSearch();
			updateProductCountLabel(productCountLabel);
		});

		showAllButton.setOnAction(e -> {
			searchField.clear();
			refreshTable();
			updateProductCountLabel(productCountLabel);
		});

		searchField.setOnAction(e -> {
			performSearch();
			updateProductCountLabel(productCountLabel);
		});

		// Add Button
		addButton.setOnAction(c -> showAddScreen());

		// Edit Button
		editButton.setOnAction(n -> {
			Product product = table.getSelectionModel().getSelectedItem();
			if (product == null) {
				AlertManager.showError("Please select a product to edit");
				return;
			}
			showEditScreen(product);
		});

		// Delete Button
		deleteButton.setOnAction(m -> {
			Product product = table.getSelectionModel().getSelectedItem();
			if (product == null) {
				AlertManager.showError("Please select a product to delete");
				return;
			}

			if (AlertManager
					.showConfirmation("Are you sure you want to delete product: " + product.getProduct_name() + "?")) {
				boolean success = DatabaseOperationsProduct.deleteProduct(product.getProduct_id());
				if (success) {
					refreshTable();
					updateProductCountLabel(productCountLabel);
					AlertManager.showInformationMessage("Product deleted successfully!");
				} else {
					AlertManager.showError("Failed to delete product from database!");
				}
			}
		});

		// Back Button
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
		mainContent.getChildren().addAll(searchBox, buttonBox, titleBox, productCountLabel, table);

		root.setCenter(mainContent);
	}

	private void updateProductCountLabel(Label label) {

		int totalProducts = DatabaseOperationsProduct.getProductCount();
		String countText = "Total Products: " + totalProducts;
		label.setText(countText);
	}

	private void performSearch() {
		String keyword = searchField.getText().trim();
		String searchType = searchTypeCombo.getValue();

		if (keyword.isEmpty()) {
			refreshTable();
			return;
		}

		ObservableList<Product> searchResults;

		if ("ID".equals(searchType)) {
			try {
				int productId = Integer.parseInt(keyword);
				Product product = DatabaseOperationsProduct.getProductById(productId);
				searchResults = FXCollections.observableArrayList();
				if (product != null) {
					searchResults.add(product);
				}
			} catch (NumberFormatException e) {
				AlertManager.showError("Invalid ID \n Please enter a valid numeric ID");
				return;
			}
		} else {
			searchResults = searchProductsByName(keyword);
		}

		updateTableData(searchResults);

		if (searchResults.isEmpty()) {
			AlertManager.showInformationMessage("No products found for: " + keyword);
		}
	}

	private ObservableList<Product> searchProductsByName(String keyword) {
		ObservableList<Product> allProducts = DatabaseOperationsProduct.getAllProducts();
		ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

		for (Product product : allProducts) {
			if (product.getProduct_name().toLowerCase().contains(keyword.toLowerCase())) {
				filteredProducts.add(product);
			}
		}

		return filteredProducts;
	}

	private void updateTableData(ObservableList<Product> newData) {
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

	// Add screen
	private void showAddScreen() {
		VBox addBox = new VBox(15);
		addBox.setPadding(new Insets(30));
		addBox.setAlignment(Pos.CENTER);
		addBox.getStyleClass().add("add-edit-box");

		Label title = new Label("Add New Product");
		title.getStyleClass().add("add-edit-title");

		Label productIdLabel = new Label("Product ID will be assigned automatically");
		productIdLabel.getStyleClass().add("info-label");

		TextField category_id = createStyledTextField("Category ID", "");
		TextField name = createStyledTextField("Product Name", "");
		TextField price = createStyledTextField("Price", "");
		TextField brand = createStyledTextField("Brand", "");
		TextArea desc = createStyledTextArea("Description", "");

		CheckBox stock = new CheckBox("In Stock");
		stock.getStyleClass().add("check-box");
		stock.setSelected(true);

		HBox buttons = new HBox(20);
		buttons.setAlignment(Pos.CENTER);

		Button save = createPinkButton("Save", "#ff69b4", "#ff1493");
		Button back = createPinkButton("Back", "#ffb6c1", "#db7093");

		buttons.getChildren().addAll(save, back);

		addBox.getChildren().addAll(title, productIdLabel, category_id, name, price, brand, desc, stock, buttons);

		BorderPane container = new BorderPane(addBox);
		container.getStyleClass().add("add-edit-container");
		root.setCenter(container);

		back.setOnAction(e -> show());

		save.setOnAction(e -> {
			if (!validateInput(category_id, name, price, brand, desc)) {
				return;
			}
			saveProduct(category_id, name, price, brand, desc);
		});
	}

	// Edit screen
	private void showEditScreen(Product product) {
		VBox editBox = new VBox(15);
		editBox.setPadding(new Insets(30));
		editBox.setAlignment(Pos.CENTER);
		editBox.getStyleClass().add("add-edit-box");

		Label title = new Label("Edit Product ");
		title.getStyleClass().add("add-edit-title");

		Label productIdLabel = new Label("Product ID: " + product.getProduct_id());
		productIdLabel.getStyleClass().add("info-label");

		TextField category_id = createStyledTextField("Category ID",
				String.valueOf(product.getCategory().getCategoryId()));
		TextField name = createStyledTextField("Product Name", product.getProduct_name());
		TextField price = createStyledTextField("Price", String.valueOf(product.getPrice()));
		TextField brand = createStyledTextField("Brand", product.getBrand());
		TextArea desc = createStyledTextArea("Description", product.getProduct_description());

		CheckBox stock = new CheckBox("In Stock");
		stock.getStyleClass().add("check-box");


		HBox buttons = new HBox(20);
		buttons.setAlignment(Pos.CENTER);

		Button save = createPinkButton("Update", "#ff69b4", "#ff1493");
		Button back = createPinkButton("Cancel", "#ffb6c1", "#db7093");

		buttons.getChildren().addAll(save, back);

		editBox.getChildren().addAll(title, productIdLabel, category_id, name, price, brand, desc, stock, buttons);

		BorderPane container = new BorderPane(editBox);
		container.getStyleClass().add("add-edit-container");
		root.setCenter(container);

		back.setOnAction(e -> show());

		save.setOnAction(e -> {
			if (!validateInput(category_id, name, price, brand, desc)) {
				return;
			}
			updateProduct(product, category_id, name, price, brand, desc, stock);
		});
	}

	private boolean validateInput(TextField categoryId, TextField name, TextField price, TextField brand,
			TextArea desc) {

		if (categoryId.getText().trim().isEmpty() || name.getText().trim().isEmpty() || price.getText().trim().isEmpty()
				|| brand.getText().trim().isEmpty() || desc.getText().trim().isEmpty()) {

			AlertManager.showError("Please fill in all fields!");
			return false;
		}

		try {
			Integer.parseInt(categoryId.getText().trim());
		} catch (NumberFormatException e) {
			AlertManager.showError("Invalid Category ID \n Category ID must be a valid number!");
			return false;
		}

		try {
			double priceValue = Double.parseDouble(price.getText().trim());
			if (priceValue <= 0) {
				AlertManager.showError("Invalid Price \n Price must be greater than 0!");
				return false;
			}
		} catch (NumberFormatException e) {
			AlertManager.showError("Invalid Price \n Price must be a valid number!");
			return false;
		}

		return true;
	}

	private void saveProduct(TextField categoryId, TextField name, TextField price, TextField brand, TextArea desc
			) {

		Product newProduct = new Product(0, name.getText().trim(), Double.parseDouble(price.getText().trim()),
				brand.getText().trim(), desc.getText().trim(),
				new Category(Integer.parseInt(categoryId.getText().trim()), "", ""));

		boolean success = DatabaseOperationsProduct.addProduct(newProduct);

		if (success) {
			String message = "Product Added Successfully!\n\n" + "Product Details:\n" + "• Product ID: "
					+ newProduct.getProduct_id() + "\n" + "• Product Name: " + newProduct.getProduct_name() + "\n"
					+ "• Category ID: " + newProduct.getCategory().getCategoryId() + "\n" + "• Price: $"
					+ newProduct.getPrice() + "\n" + "• Brand: " + newProduct.getBrand() + "\n" + "• Stock Status: "
					;

			AlertManager.showInformationMessage(message);
			refreshTable();
			show();
		} else {
			AlertManager.showError("Failed to add product!");
		}
	}

	private void updateProduct(Product originalProduct, TextField categoryId, TextField name, TextField price,
                               TextField brand, TextArea desc, CheckBox stock) {

		originalProduct.setProduct_name(name.getText().trim());
		originalProduct.setPrice(Double.parseDouble(price.getText().trim()));
		originalProduct.setBrand(brand.getText().trim());
		originalProduct.setProduct_description(desc.getText().trim());
		originalProduct.setCategory(new Category(Integer.parseInt(categoryId.getText().trim()), "", ""));

		boolean success = DatabaseOperationsProduct.updateProduct(originalProduct);

		if (success) {
			AlertManager.showInformationMessage("Product Updated Successfully!");
			refreshTable();
			show();
		} else {
			AlertManager.showError("Failed to update product!");
		}
	}

	public void refreshTable() {
		ObservableList<Product> allProducts = DatabaseOperationsProduct.getAllProducts();
		updateTableData(allProducts);
	}

	private Button createPinkButton(String text, String baseColor, String hoverColor) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: " + baseColor + "; " + "-fx-text-fill: white; -fx-font-size: 16px; "
				+ "-fx-padding: 12px 25px; -fx-font-weight: bold; "
				+ "-fx-background-radius: 20; -fx-border-radius: 20; "
				+ "-fx-cursor: hand; -fx-font-family: 'Arial Rounded MT Bold';");

		button.setOnMouseEntered(e -> {
			button.setStyle("-fx-background-color: " + hoverColor + "; " + "-fx-text-fill: white; -fx-font-size: 16px; "
					+ "-fx-padding: 12px 25px; -fx-font-weight: bold; "
					+ "-fx-background-radius: 20; -fx-border-radius: 20; "
					+ "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);");
		});

		button.setOnMouseExited(e -> {
			button.setStyle("-fx-background-color: " + baseColor + "; " + "-fx-text-fill: white; -fx-font-size: 16px; "
					+ "-fx-padding: 12px 25px; -fx-font-weight: bold; "
					+ "-fx-background-radius: 20; -fx-border-radius: 20; " + "-fx-cursor: hand;");
		});

		button.setOnMousePressed(e -> {
			button.setStyle("-fx-background-color: #c71585; " + "-fx-text-fill: white; -fx-font-size: 16px; "
					+ "-fx-padding: 12px 25px; -fx-font-weight: bold; "
					+ "-fx-background-radius: 20; -fx-border-radius: 20; " + "-fx-cursor: hand; -fx-translate-y: 2px;");
		});

		button.setOnMouseReleased(e -> {
			button.setStyle("-fx-background-color: " + baseColor + "; " + "-fx-text-fill: white; -fx-font-size: 16px; "
					+ "-fx-padding: 12px 25px; -fx-font-weight: bold; "
					+ "-fx-background-radius: 20; -fx-border-radius: 20; " + "-fx-cursor: hand; -fx-translate-y: 0px;");
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