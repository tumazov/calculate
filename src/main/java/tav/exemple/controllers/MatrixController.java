package tav.exemple.controllers;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tav.exemple.models.MatrixModel;
import tav.exemple.views.MatrixView;

/**
 * Класс Контроллер
 */
public class MatrixController {

    private  MatrixModel model;

    public MatrixController(MatrixView view) {

        setView(view);
    }
    /** Основной метод контроллера*/
    public void setView(MatrixView view) {

        model = new MatrixModel(0,0F,0F,100,200,2.1F,0.015F);

        view.getObjectSizeField().textProperty().addListener((obs, oldText, newText) -> model.setObjectSize(Integer.parseInt(newText)));
        view.getDistanceField().textProperty().addListener((obs, oldText, newText) -> model.setDistance(Integer.parseInt(newText)));
        view.getApertureField().textProperty().addListener((obs, oldText, newText) -> model.setAperture(Float.parseFloat(newText)));
        view.getSpotField().textProperty().addListener((obs, oldText, newText) -> model.setSpot(Float.parseFloat(newText)));

//        view.getApertureField().textProperty().bindBidirectional(model.apertureProperty(), new NumberStringConverter());

        // Рассчитать гиперфокальное рассстояние
        view.getCalcHyperFocalButton().setOnAction(event -> {

            view.getObjectSizeField().textProperty().addListener((obs, oldText, newText) -> model.setObjectSize(Integer.parseInt(newText)));
            int calcObjectSize = model.getObjectSize();
            view.getDistanceField().textProperty().addListener((obs, oldText, newText) -> model.setDistance(Integer.parseInt(newText)));
            int calcDistance = model.getDistance();

            view.getApertureField().textProperty().addListener((obs, oldText, newText) -> model.setAperture(Float.parseFloat(newText)));
            float calcAperture = model.getAperture();
            view.getSpotField().textProperty().addListener((obs, oldText, newText) -> model.setSpot(Float.parseFloat(newText)));
            float calcSpot = model.getSpot();

            for (int i = 0; i < 4; i++) {
                model = view.tableMatrix().getItems().get(i);
                model.calcHyperFocal(calcAperture, calcSpot, model.getFocus());
                model.calcObjectPixel(calcObjectSize, calcDistance, model.getAnglePixel());
            }
            event.consume();
        });

        // Обнулить таблицу

        MatrixView.getResetButton().setOnAction(event -> {
            for (int i = 0; i < 4; i++) {
                view.getList().get(i).setMatrix(0);
                view.getList().get(i).setSizePixel(0);
                view.getList().get(i).setFocus(0);
            }
            view.tableMatrix().refresh();
            event.consume();
        });
    }
   /**
   * Метод сохранить принтскрин
   */
    public void savePng(Stage primaryStage) {

        MatrixView.getSaveButton().setOnAction(event -> {
            FileChooser savefile = new FileChooser();
            savefile.setTitle("Save File");
            File file = savefile.showSaveDialog(primaryStage);
            if (file != null) {
                try {
                    BufferedImage img = SwingFXUtils.fromFXImage(primaryStage.getScene().snapshot(null), null);
                    ImageIO.write(img, "png", file);
                    System.out.println("save");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Error!");
                }
            }
        });
    }

}
