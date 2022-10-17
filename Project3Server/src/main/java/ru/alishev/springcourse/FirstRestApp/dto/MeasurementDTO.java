package ru.alishev.springcourse.FirstRestApp.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class MeasurementDTO {
    @NotNull
    private Double temperature;

    @NotNull
    private Boolean rain;

    @NotNull
    private SensorDTO sensor;


    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getRain() {
        return rain;
    }

    public void setRain(Boolean rain) {
        this.rain = rain;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
