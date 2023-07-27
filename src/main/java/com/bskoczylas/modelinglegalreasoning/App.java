import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Modeling Legal Reasoning");
        this.primaryStage.setResizable(false);
        showProjectWindow();
    }

    public void showProjectWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project.fxml"));
            Parent root = loader.load();

            ProjectController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}