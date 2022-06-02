package tav.exemple.models;

import java.math.BigDecimal;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Класс расчета данных таблицы
 */
public class MatrixModel {
    /** Переменная количество пикселей матрицы */
    private final IntegerProperty matrix;
    /** Переменная размер пикселя в мкм */
    private final FloatProperty sizePixel;
    /** Переменная фокусное расстояние в мм */
    private final FloatProperty focus;
    /** Расчетное значение угол пикселя в угл.сек. */
    private final FloatProperty anglePixel;
    /** Расчетное значение поле зрения матрицы  градусах */
    private final FloatProperty angleMatrix;
    /** Расчетное значение размер матрицы в мм*/
    private final FloatProperty sizeMatrix;
    /** Расчетное значение гиперфокальное расстояние в м */
    private final FloatProperty hyperFocal;
    /** Расчетное значение размер объекта в пикселях в кадре */
    private final FloatProperty objectPixel;

    /** Переменная размер объекта в см */
    private final IntegerProperty objectSize;
    /** Переменная дальность до объекта в м */
    private final IntegerProperty distance;
    /** Переменная апертура объектива */
    private final FloatProperty aperture;
    /** Переменная пятно рассеяния в мм */
    private final FloatProperty spot;

    /** Конструктор с данными */
    public MatrixModel(Integer matrix,Float sizePixel, Float focus,Integer objectSize, Integer distance, Float aperture, Float spot) {

        this.matrix = new SimpleIntegerProperty(matrix);
        this.sizePixel = new SimpleFloatProperty(sizePixel);
        this.focus = new SimpleFloatProperty(focus);
        this.anglePixel = new SimpleFloatProperty();
        this.angleMatrix = new SimpleFloatProperty();
        this.sizeMatrix = new SimpleFloatProperty();
        this.hyperFocal = new SimpleFloatProperty();
        this.objectPixel = new SimpleFloatProperty();

        this.objectSize = new SimpleIntegerProperty(objectSize);
        this.distance = new SimpleIntegerProperty(distance);
        this.aperture = new SimpleFloatProperty(aperture);
        this.spot = new SimpleFloatProperty(spot);
    }
    /** Получить  количество пикселей матрицы */
    public int getMatrix() {
        return matrix.get();
    }
    /** Установить  количество пикселей матрицы */
    public void  setMatrix(int matrix){
        this.matrix.set(matrix);
    }

    /** Количество пикселей матрицы */
    public IntegerProperty matrixProperty() {
        return this.matrix;
    }
    /** Получить  размер пикселя */
    public float getSizePixel() {
        return sizePixel.get();
    }
    /** Установить  размер пикселя */
    public void setSizePixel(float sizePixel) {
        this.sizePixel.set(sizePixel);
    }
    /** Размер пикселя */
    public FloatProperty sizePixelProperty() {
        return sizePixel;
    }
    /** Установить  размер объекта */
    public void setObjectSize(int objectSize) {
        this.objectSize.set(objectSize);
    }
    /** Получить размер объекта */
    public int getObjectSize() {
        return objectSize.get();
    }
    /** Установить  дальность до объекта */
    public void setDistance(int distance) {
        this.distance.set(distance);
    }
    /** Получить дальность до объекта */
    public int getDistance() {
        return distance.get();
    }
    /** Установить  пятно рассеяния */
    public void setSpot(float spot) {
        this.spot.set(spot);
    }
    /** Получить  пятно рассеяния */
    public float getSpot() {
        return spot.get();
    }
    /** Установить  значение апертуры объектива */
    public void setAperture(float aperture) {
        this.aperture.set(aperture);
    }
    /** Получить  значение апертуры объектива */
    public float getAperture() {
        return aperture.get();
    }
    /** Установить  значение фокусного расстояния */
    public void setFocus(float focus) {
        this.focus.set(focus);
    }
    /** Получить  значение фокусного расстояния */
    public float getFocus() {
        return focus.get();
    }
    /** Фокусное расстояние */
    public FloatProperty focusProperty() {
        return focus;
    }
    /** Гиперфокальное расстояние */
    public FloatProperty hyperFocalProperty() {
        return this.hyperFocal;
    }
    /** Размер объекта в пикселях */
    public FloatProperty objectPixelProperty() {
        return this.objectPixel;
    }
    /** Рассчитать размер объекта в пикселях */
    public void calcObjectPixel(int calcObjectSize, int calcDistance, float calcAnglePixel) {
        objectPixel.set((float) ((Math.round((calcObjectSize / (calcDistance * Math.tan((calcAnglePixel / 3600) / 57.3) * 100)) * 100))/100));
        System.out.println(getObjectSize());
        System.out.println(objectSize);
    }
    /** Рассчитать гиперфокальное расстояние */
    public void calcHyperFocal(float calcAperture, float calcSpot, float calcFocus) {
        hyperFocal.set(Math.round(((calcFocus * calcFocus) / (calcAperture * calcSpot)) * 0.001));
//        hyperFocal.set(BigDecimal.valueOf((focus * focus) / (aperture * spot)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
    }
    /** Получить значение угла пикселя */
    public float getAnglePixel() {
        return anglePixel.get();
    }
    /** Рассчитать  угол пикселя */
    public FloatProperty anglePixelProperty() {
        //a=2*arctg(h/2f)
//        anglePixel.set((double)(Math.round( var*100))/100);
        anglePixel.set((float) (Math.round((3600 * (180/Math.PI) * 2 * Math.atan((sizePixel.get()/1000) / (2 * focus.get())))*100))/100);
        return anglePixel;
    }
    /** Рассчитать поле зрения матрицы */
    public FloatProperty angleMatrixProperty() {
        angleMatrix.set((float) (Math.round((((180/Math.PI) * 2 * Math.atan(sizeMatrix.get() / (2 * focus.get()))))*100))/100);
        return angleMatrix;
    }
    /** Рассчитать размер матрицы */
    public FloatProperty sizeMatrixProperty() {
        sizeMatrix.set(BigDecimal.valueOf(matrix.get() * (sizePixel.get())/1000).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        return sizeMatrix;
    }

}


