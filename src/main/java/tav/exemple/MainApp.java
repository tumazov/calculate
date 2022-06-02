package tav.exemple;

import java.io.IOException;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import tav.exemple.controllers.MatrixController;
import tav.exemple.views.MatrixView;


/**
 * Главный класс приложения calculateFx
 * @author Тумазов Андрей
 */
public class MainApp extends Application{

    @Override
    public void start(Stage primaryStage)  {

        MatrixView mainView = new MatrixView();
        MatrixController controller = new MatrixController(mainView);

        primaryStage.setTitle("Расчет матрицы");
        primaryStage.setScene(new Scene(mainView.getView()));
        primaryStage.show();

        controller.savePng(primaryStage);

    }
    /** Стартовый метод приложения */
    public static void main(String[] args) {
        launch(args);
    }

    /** Запись в файл данных при закрытии приложения */
    @Override
    public void stop() {
        try {
            MatrixView.writeCSV();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

