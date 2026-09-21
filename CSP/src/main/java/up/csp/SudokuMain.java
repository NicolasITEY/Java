package up.csp;

import javafx.application.Application;
import javafx.stage.Stage;
import up.csp.controller.SudokuController;
import up.csp.model.SudokuModel;
import up.csp.view.SudokuView;

public class SudokuMain extends Application {

    @Override
    public void start(Stage primaryStage) {

        SudokuModel model = new SudokuModel();

        SudokuView view = new SudokuView(primaryStage);

        SudokuController controller = new SudokuController(model, view);

        view.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}