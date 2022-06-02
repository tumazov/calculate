package tav.exemple.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import tav.exemple.models.MatrixModel;

/** Класс интерфейса рассчета */
public class MatrixView {
    private Parent view;

    private TableView<MatrixModel> tableMatrix;
    private TextField objectSizeText;
    private TextField distanceText;
    private TextField apertureText;
    private TextField spotText;
    private static Button reset;
    private static Button saveButton;
    private static Button hyperFocalButton;

    /** Источник данных */
    private static ObservableList<MatrixModel> list = FXCollections.observableArrayList();

    public MatrixView() {

        view = createView();
    }

    /** Метод доступа к таблице */
    public TableView<MatrixModel> tableMatrix() {
        return tableMatrix;
    }
    /** Метод доступа текстовому полю знечение апертуры */
    public TextField getApertureField() {
        return apertureText;
    }
    /** Метод доступа к текстовому полю значение размер объекта */
    public TextField getObjectSizeField() {
        return objectSizeText;
    }
    /** Метод доступа к текстовому полю значение дальности до объекта */
    public TextField getDistanceField() {
        return distanceText;
    }
    /** Метод доступа к текстовому полю значение пятна рассеяния */
    public TextField getSpotField() {
        return spotText;
    }
    /** Метод доступа к кнопке выполнить расчет */
    public Button getCalcHyperFocalButton() {
        return hyperFocalButton;
    }
    /** Метод доступа к кнопке обнулить таблицу */
    public static Button  getResetButton() {
        return reset;
    }
    /** Метод доступа к кнопке сохранить принтскрин */
    public static Button  getSaveButton() {
        return saveButton;
    }
    /** Метод доступа к данным в виде списка */
    public ObservableList<MatrixModel> getList() {
        return list;
    }

    private VBox createView() {
        VBox vBox = new VBox(20);
        vBox.setPrefWidth(1120);
        vBox.setPrefHeight(600);
        vBox.setPadding(new Insets(15));
        vBox.setAlignment(Pos.TOP_CENTER);

        vBox.getChildren().add(createTitle());
        vBox.getChildren().add(createLabel());
        vBox.getChildren().add(createTable());
        vBox.getChildren().add(createButtons());

         return vBox;
    }

    private Node createTitle() {
        Label titleLabel = new Label("Расчет оптических параметров");
        titleLabel.setStyle("-fx-background-color: #C0C0C0; -fx-font-weight: 800; -fx-padding: 10px; -fx-font-size: 16pt; -fx-alignment: center;");
        titleLabel.setMaxWidth(600);
        return titleLabel;
    }

    private Node createLabel() {
        HBox hBox = new HBox(15);
        Label objectSizeLabel = new Label("Размер объекта, см");
        Label distanceLabel = new Label("Дальность до объекта, м");
        Label apertureLabel = new Label("Диафрагменное число");
        Label spotLabel = new Label("Пятно рассеяния, мм");
        hyperFocalButton = new Button("Рассчитать гиперфокальное");

        objectSizeText = new TextField();
        objectSizeText.setPromptText("100");
        objectSizeText.setPrefWidth(65);
        textFieldForFloat(objectSizeText);

        distanceText = new TextField();
        distanceText.setPromptText("200");
        distanceText.setPrefWidth(65);
        textFieldForFloat(distanceText);

        apertureText = new TextField();
        apertureText.setPromptText("2.0");
        apertureText.setPrefWidth(65);
        textFieldForFloat(apertureText);

        spotText = new TextField();
        spotText.setPromptText("0.015");
        spotText.setPrefWidth(65);
        textFieldForFloat(spotText);

        hBox.getChildren().addAll(objectSizeLabel,objectSizeText, distanceLabel, distanceText, apertureLabel, apertureText, spotLabel, spotText,
                hyperFocalButton);
        hBox.setAlignment(Pos.TOP_CENTER);
        return hBox;
    }
    /** Метод создать таблицу */
    private Node createTable() {
        readCSV();
        VBox vBox = new VBox(15);
//        vBox.setPrefWidth(1050);
        vBox.setPrefHeight(190);
        tableMatrix = new TableView<>();
        tableMatrix.setMaxWidth(965);
        tableMatrix.setEditable(true);

        TableColumn<MatrixModel, Integer> initialDataMatrix = new TableColumn<>("Исходные данные");
        initialDataMatrix.setStyle("-fx-alignment: CENTER;");

        TableColumn<MatrixModel, Integer> calcDataMatrix = new TableColumn<>("Расчетные параметры");

        TableColumn<MatrixModel, Integer> valueMatrix = new TableColumn<>("Размер \n матрицы,\n пикс");
        valueMatrix.setStyle("-fx-alignment: CENTER;");
        valueMatrix.setMinWidth(120);
        valueMatrix.setCellValueFactory(cellData -> cellData.getValue().matrixProperty().asObject());
//        valueMatrix.setCellValueFactory(new PropertyValueFactory<>("matrix"));
        // редактирование
        valueMatrix.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        valueMatrix.setOnEditCommit((e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setMatrix(Integer.parseInt(String.valueOf(e.getNewValue())));
            tableMatrix.refresh();
        });

        TableColumn<MatrixModel, Float> valueSizePixel = new TableColumn<>("Размер \n пикселя,\n мкм");
        valueSizePixel.setStyle("-fx-alignment: CENTER;");
        valueSizePixel.setMinWidth(120);
        valueSizePixel.setCellValueFactory(cellData -> cellData.getValue().sizePixelProperty().asObject());
        valueSizePixel.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        valueSizePixel.setOnEditCommit((e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setSizePixel(Float.parseFloat(String.valueOf(e.getNewValue())));
            tableMatrix.refresh();
        });

        TableColumn<MatrixModel, Float> valueFocus = new TableColumn<>("Фокусное \n расстояние,\n мм");
        valueFocus.setStyle("-fx-alignment: CENTER;");
        valueFocus.setMinWidth(120);
        valueFocus.setCellValueFactory(cellData -> cellData.getValue().focusProperty().asObject());
        valueFocus.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        valueFocus.setOnEditCommit((e) -> {
            e.getTableView().getItems().get(e.getTablePosition().getRow()).setFocus(Float.parseFloat(String.valueOf(e.getNewValue())));
            tableMatrix.refresh();
        });

        TableColumn<MatrixModel, Float> valueAnglePixel = new TableColumn<>("Угол \n пикселя,\n угл.сек");
        valueAnglePixel.setStyle("-fx-alignment: CENTER;");
        valueAnglePixel.setMinWidth(120);
        valueAnglePixel.setCellValueFactory(cellData -> cellData.getValue().anglePixelProperty().asObject());

        TableColumn<MatrixModel, Float> valueAngleMatrix = new TableColumn<>("Угол \n матрицы,\n угл.град");
        valueAngleMatrix.setStyle("-fx-alignment: CENTER;");
        valueAngleMatrix.setMinWidth(120);
        valueAngleMatrix.setCellValueFactory(cellData -> cellData.getValue().angleMatrixProperty().asObject());

        TableColumn<MatrixModel, Float> valueSizeMatrix = new TableColumn<>("Размер \n матрицы,\n мм");
        valueSizeMatrix.setStyle("-fx-alignment: CENTER;");
        valueSizeMatrix.setMinWidth(120);
        valueSizeMatrix.setCellValueFactory(cellData -> cellData.getValue().sizeMatrixProperty().asObject());

        TableColumn<MatrixModel, Float> valueObjectPixel = new TableColumn<>("Размер \n объекта,\n пиксел");
        valueObjectPixel.setStyle("-fx-alignment: CENTER;");
        valueObjectPixel.setMinWidth(120);
        valueObjectPixel.setCellValueFactory(cellData -> cellData.getValue().objectPixelProperty().asObject());

        TableColumn<MatrixModel, Float> valueHyperfocal = new TableColumn<>("Гипер фокальное \n расстояние,\n м");
        valueHyperfocal.setStyle("-fx-alignment: CENTER;");
        valueHyperfocal.setMinWidth(120);
        valueHyperfocal.setCellValueFactory(cellData -> cellData.getValue().hyperFocalProperty().asObject());

        tableMatrix.setItems(list);

        initialDataMatrix.getColumns().addAll(valueMatrix, valueSizePixel, valueFocus);
        calcDataMatrix.getColumns().addAll(valueAnglePixel, valueAngleMatrix, valueSizeMatrix, valueObjectPixel, valueHyperfocal);

        tableMatrix.getColumns().addAll(initialDataMatrix, calcDataMatrix);

        vBox.getChildren().add(tableMatrix);
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }

    private Node createButtons() {
        HBox hBox = new HBox(30);
        reset = new Button("Обнулить таблицу");
        saveButton= new Button("Принтскрин таблицы");
        hBox.getChildren().addAll(reset, saveButton);
        hBox.setAlignment(Pos.BASELINE_RIGHT);
        return hBox;
    }

    public Parent getView() {
        return view;
    }

    /** Получить данные из файла */
    public void readCSV() {
        String fileName = "src/main/resources/data/dataMatrix.csv";
        File fileRead = new File(fileName);
        try {
            Scanner inputStream = new Scanner(fileRead);
            while (inputStream.hasNext()) {
                String data = inputStream.next();
                String[] valuesList = data.split(",");
                list.add(new MatrixModel(Integer.valueOf(valuesList[0]),Float.valueOf(valuesList[1]),
                        Float.valueOf(valuesList[2]),Integer.valueOf(valuesList[3]),Integer.valueOf(valuesList[4]),
                        Float.parseFloat(valuesList[5]), Float.parseFloat(valuesList[6])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Записать данные в файл для хранения */
    public static void writeCSV() throws IOException {
        File fileWrite = new File("src/main/resources/data/dataMatrix.csv");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileWrite));
            for (MatrixModel wr : list) {
                String text =
                        wr.getMatrix() + "," + wr.getSizePixel() + "," + wr.getFocus() + "," + wr.getObjectSize() + ","
                                + wr.getDistance() + "," + wr.getAperture() + "," + wr.getSpot() +"\n";
                writer.write(text);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        assert writer != null;
        writer.close();
    }
    /** Запсиать в текстовое поле только цифры */
    private void textFieldForFloat(TextField field) {
        field.setTextFormatter(new TextFormatter<Float>(change ->  {
            if (change.getText().matches("^([+-]?\\d*\\.?\\d*)$")) {
                return change;
            }
            return null;
        }));
    }
}

