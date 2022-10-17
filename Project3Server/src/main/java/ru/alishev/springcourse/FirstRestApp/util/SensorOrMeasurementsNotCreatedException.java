package ru.alishev.springcourse.FirstRestApp.util;

public class SensorOrMeasurementsNotCreatedException extends RuntimeException {
    public SensorOrMeasurementsNotCreatedException(String msg) {
        super(msg);
    }
}
