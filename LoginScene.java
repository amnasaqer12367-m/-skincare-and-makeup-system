import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScene {

    private Stage stage;
    
    public LoginScene(Stage stage) {
	    this.stage = stage;
	}

	public void showLoginScene() {
    	
        stage.setTitle("ASAL BeautyCare Login");
//main content
        VBox main = new VBox(30);
        main.setAlignment(Pos.CENTER);
        main.setPadding(new Insets(40));
        main.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFF0F5, #FFE4E1);");

      
    	ImageView imageflower = new ImageView(new Image("file:C:/Users/User/Downloads/Desktop/flower.png"));
    	imageflower.setFitWidth(50);
    	imageflower.setFitHeight(50);
    	imageflower.setPreserveRatio(true);
    	
    	Label titleLabel = new Label("ASAL BeautyCare");
        titleLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 36));
        titleLabel.setStyle("-fx-text-fill: #DB7093; -fx-effect: dropshadow(gaussian, rgba(219,112,147,0.5), 10, 0, 0, 3);");
        titleLabel.setGraphic(imageflower);
    

    	ImageView imageView = new ImageView(new Image("file:C:/Users/User/Downloads/Desktop/flower1.png\""));
    	imageView.setFitWidth(50);
    	imageView.setFitHeight(50);
    	imageView.setPreserveRatio(true);
    	
        Label iconLabel = new Label();
        iconLabel.setFont(Font.font(60));
        iconLabel.setGraphic(imageView);
        
        
        VBox iconBox = new VBox(iconLabel);
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setPadding(new Insets(10));

        Label welcome = new Label("Welcome to ASAL BeautyCare System");
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        welcome.setStyle("-fx-text-fill: #8B4513;");

      //to chpoose role
        VBox roleBox = new VBox(25);
        roleBox.setAlignment(Pos.CENTER);
        roleBox.setPadding(new Insets(30, 50, 30, 50));
        roleBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); " +
                                 "-fx-background-radius: 20; " +
                                 "-fx-border-radius: 20; " +
                                 "-fx-border-color: #FFB6C1; " +
                                 "-fx-border-width: 3; " +
                                 "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 15, 0, 0, 0);");

        Label chooseRole = new Label("Choose Your Role:");
        chooseRole.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        chooseRole.setStyle("-fx-text-fill: #C71585;");
//we have admin and customer
       Button admin = createRoleButton("Login as Admin",
            "Admin Dashboard", 
            "Manage products, categories, orders, and view reports", 
            "#FF69B4", "#FF1493");

       Button customer = createRoleButton("Login as Customer",
            "Customer Portal", 
            "Browse products, place orders, and manage your profile", 
            "#20B2AA", "#008B8B");

        Label infoLabel = new Label("select your role to continue to the system");
        infoLabel.setFont(Font.font("Arial", 14));
        infoLabel.setStyle("-fx-text-fill: #696969;");

        roleBox.getChildren().addAll(
            chooseRole, admin, customer, infoLabel
        );

        Label footerLabel = new Label("2026 ASAL BeautyCare System");
        footerLabel.setFont(Font.font("Arial", 12));
        footerLabel.setStyle("-fx-text-fill: #808080;");

        main.getChildren().addAll(titleLabel, iconBox, welcome, roleBox, footerLabel);

        admin.setOnAction(e -> showAdminLogin());
        customer.setOnAction(e -> showCustomerLogin());

        Scene scene = new Scene(main, 900, 700);
        
        try {
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS file not found, using default styling.");
        }
        
        stage.setScene(scene);
        stage.show();
    }
//cutsimaized buttons
    private Button createRoleButton(String title, String subtitle, String description, 
                                    String baseColor, String hoverColor) {
        
        VBox buttonContent = new VBox(5);
        buttonContent.setAlignment(Pos.CENTER_LEFT);
        buttonContent.setPadding(new Insets(15, 25, 15, 25));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: white;");
        
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        subtitleLabel.setStyle("-fx-text-fill: #FFFAFA;");
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setStyle("-fx-text-fill: #FFFAFA;");
        descLabel.setWrapText(true);
        
        buttonContent.getChildren().addAll(titleLabel, subtitleLabel, descLabel);
        
        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        
       button.setStyle("-fx-background-color: " + baseColor + "; " +
                       "-fx-background-radius: 15; " +
                       "-fx-border-radius: 15; " +
                       "-fx-border-color: " + (baseColor.equals("#FF69B4") ? "#DB7093" : "#008080") + "; " +
                       "-fx-border-width: 2; " +
                       "-fx-cursor: hand; " +
                       "-fx-pref-width: 400; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);");
        
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + hoverColor + "; " +
                           "-fx-background-radius: 15; " +
                           "-fx-border-radius: 15; " +
                           "-fx-border-color: " + (hoverColor.equals("#FF1493") ? "#C71585" : "#006666") + "; " +
                           "-fx-border-width: 2; " +
                           "-fx-cursor: hand; " +
                           "-fx-pref-width: 400; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 15, 0, 0, 5);");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + baseColor + "; " +
                           "-fx-background-radius: 15; " +
                           "-fx-border-radius: 15; " +
                           "-fx-border-color: " + (baseColor.equals("#FF69B4") ? "#DB7093" : "#008080") + "; " +
                           "-fx-border-width: 2; " +
                           "-fx-cursor: hand; " +
                           "-fx-pref-width: 400; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);");
        });
        
        button.setOnMousePressed(e -> {
            button.setStyle("-fx-background-color: " + (baseColor.equals("#FF69B4") ? "#C71585" : "#006666") + "; " +
                           "-fx-background-radius: 15; " +
                           "-fx-border-radius: 15; " +
                           "-fx-border-color: " + (baseColor.equals("#FF69B4") ? "#8B0A50" : "#004D4D") + "; " +
                           "-fx-border-width: 2; " +
                           "-fx-cursor: hand; " +
                           "-fx-pref-width: 400; " +
                           "-fx-translate-y: 2px;");
        });
        
        button.setOnMouseReleased(e -> {
            button.setStyle("-fx-background-color: " + baseColor + "; " +
                           "-fx-background-radius: 15; " +
                           "-fx-border-radius: 15; " +
                           "-fx-border-color: " + (baseColor.equals("#FF69B4") ? "#DB7093" : "#008080") + "; " +
                           "-fx-border-width: 2; " +
                           "-fx-cursor: hand; " +
                           "-fx-pref-width: 400; " +
                           "-fx-translate-y: 0px;");
        });
        
        return button;
    }

    private void showAdminLogin() {
       
    	Stage loginStage = new Stage();
        loginStage.setTitle("Admin Login ASAL BeautyCare");
        
        VBox loginLayout = new VBox(25);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(40));
        loginLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFF0F5, #FFE4E1);");
        
        Label titleLabel = new Label("Admin Login");
        titleLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #DB7093;");
        
        TextField usernameField = createStyledTextField("Username");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(45);
        passwordField.setMaxWidth(300);
        passwordField.setStyle("-fx-font-size: 16px; -fx-background-radius: 10; -fx-border-radius: 10; " +
                              "-fx-border-color: #FFB6C1; -fx-border-width: 2; -fx-padding: 10;");
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button loginButton = createPinkButton("Login", "#FF69B4", "#FF1493");
        Button cancelButton = createPinkButton("Cancel", "#F5AFAF", "#FFB6C1");
        
        buttonBox.getChildren().addAll(loginButton, cancelButton);
        
       Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #FF0000; -fx-font-size: 14px;");
        errorLabel.setVisible(false);
        
        loginLayout.getChildren().addAll(titleLabel, usernameField, passwordField, buttonBox, errorLabel);
        
        loginButton.setOnAction(e -> {
            if (validateAdminLogin(usernameField.getText(), passwordField.getText())) {
                loginStage.close();
                showAdminDashboard();
            } else {
                errorLabel.setText("Invalid username or password!");
                errorLabel.setVisible(true);
            }
        });
        
        cancelButton.setOnAction(e -> loginStage.close());
        
        Scene loginScene = new Scene(loginLayout, 500, 400);
        loginStage.setScene(loginScene);
        loginStage.setResizable(false);
        loginStage.show();
    }

    private void showCustomerLogin() {
       
    	Stage loginStage = new Stage();
        loginStage.setTitle("Customer Login - ASAL BeautyCare");
        
        VBox loginLayout = new VBox(25);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(40));
        loginLayout.setStyle("-fx-background-color: linear-gradient(to bottom right, #FFF0F5, #FFE4E1);");
        
        Label titleLabel = new Label("Customer Login");
        titleLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #20B2AA;");
        
        VBox optionsBox = new VBox(20);
        optionsBox.setAlignment(Pos.CENTER);
        
        Button existingCustomerBtn = createPinkButton("Existing Customer Login", "#20B2AA", "#008B8B");
        Button newCustomerBtn = createPinkButton("New Customer Registration", "#FF69B4", "#FF1493");
        Button guestBtn = createPinkButton("Continue as Guest", "#F5AFAF", "#FFB6C1");
        
        optionsBox.getChildren().addAll(existingCustomerBtn, newCustomerBtn, guestBtn);
        
        loginLayout.getChildren().addAll(titleLabel, optionsBox);
        
        existingCustomerBtn.setOnAction(e -> showExistingCustomerLogin());
        newCustomerBtn.setOnAction(e -> showCustomerRegistration());
        guestBtn.setOnAction(e -> {
            loginStage.close();
            showCustomerDashboard(null); 
        });
        
        Scene loginScene = new Scene(loginLayout, 500, 400);
        loginStage.setScene(loginScene);
        loginStage.setResizable(false);
        loginStage.show();
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefHeight(45);
        field.setMaxWidth(300);
        field.setStyle("-fx-font-size: 16px; -fx-background-radius: 10; -fx-border-radius: 10; " +
                      "-fx-border-color: #FFB6C1; -fx-border-width: 2; -fx-padding: 10;");
        return field;
    }

    private Button createPinkButton(String text, String baseColor, String hoverColor) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + baseColor + "; " +
                       "-fx-text-fill: white; -fx-font-size: 16px; " +
                       "-fx-padding: 12px 30px; -fx-font-weight: bold; " +
                       "-fx-background-radius: 10; -fx-border-radius: 10; " +
                       "-fx-cursor: hand;");
        
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + hoverColor + "; " +
                           "-fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-padding: 12px 30px; -fx-font-weight: bold; " +
                           "-fx-background-radius: 10; -fx-border-radius: 10; " +
                           "-fx-cursor: hand; " +
                           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 3);");
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + baseColor + "; " +
                           "-fx-text-fill: white; -fx-font-size: 16px; " +
                           "-fx-padding: 12px 30px; -fx-font-weight: bold; " +
                           "-fx-background-radius: 10; -fx-border-radius: 10; " +
                           "-fx-cursor: hand;");
        });
        
        return button;
    }

    private boolean validateAdminLogin(String username, String password) {
       return username.equals("admin") && password.equals("admin123");
    }

    private void showAdminDashboard() {
        	new AdminScreen(stage).show();
    
    }

    private void showExistingCustomerLogin() {
      System.out.println("Existing Customer Login  To be implemented");
    }

    private void showCustomerRegistration() {
         System.out.println("Customer Registration To be implemented");
    }

    private void showCustomerDashboard(String customerId) {
        System.out.println("Customer Dashboard - To be implemented");
    }

}