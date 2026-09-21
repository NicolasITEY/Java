package app;

import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewManager;

/**
 * Main class for the application
 */
public class Main extends Application {

    /**
     * Default constructor for Main.
     */
    public Main() {
        super();
    }

    /**
     * Starts the JavaFX application by initializing the main menu view and setting up the primary stage.
     * 
     * @param primaryStage the primary stage for this application, onto which the application scene can be set. 
     * The primary stage will be embedded in the browser if the application is launched as an applet.
     */
    @Override
    public void start(Stage primaryStage) {
        ViewManager manager = new ViewManager(primaryStage);
        
        manager.showMainMenu();
        
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * The main method serves as the entry point for the application. It launches the JavaFX 
     * application by calling the launch method, which in turn calls the start method to set
     * up the primary stage and display the main menu.
     * 
     * @param args the command line arguments passed to the application, which can be used for 
     * various purposes such as configuration or debugging. In this case, they are not utilized.
     */
    public static void main(String[] args) {
        launch(args);
    }
}