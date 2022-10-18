package org.example.springcourse.FirstRestApp.util;

import org.example.springcourse.FirstRestApp.models.Sensor;
import org.example.springcourse.FirstRestApp.services.SensorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorsValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public SensorsValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Sensor.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Sensor sensor = (Sensor) o;

        if(sensorsService.findByName(sensor.getName()).isPresent())
            errors.rejectValue("name", "Имя занято другим пользователем!");
    }
}
