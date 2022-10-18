package org.example.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.example.springcourse.FirstRestApp.dto.SensorDTO;
import org.example.springcourse.FirstRestApp.models.Sensor;
import org.example.springcourse.FirstRestApp.services.SensorsService;
import org.springframework.http.ResponseEntity;
import org.example.springcourse.FirstRestApp.util.SensorOrMeasurementsErrorResponse;
import org.example.springcourse.FirstRestApp.util.SensorOrMeasurementsNotCreatedException;
import org.example.springcourse.FirstRestApp.util.SensorOrMeasurementsNotFoundException;
import org.example.springcourse.FirstRestApp.util.SensorsValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController // @Controller + @ResponseBody над каждым методом
@RequestMapping("/sensors")
public class SensorsController {

    private final SensorsService sensorsService;
    private final ModelMapper modelMapper;
    private final SensorsValidator sensorsValidator;

    @Autowired
    public SensorsController(SensorsService sensorsService, ModelMapper modelMapper, SensorsValidator sensorsValidator) {
        this.sensorsService = sensorsService;
        this.modelMapper = modelMapper;
        this.sensorsValidator = sensorsValidator;
    }

    @GetMapping()
    public List<SensorDTO> getSensors() {
        return sensorsService.findAll().stream().map(this::convertToSensorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SensorDTO getSensor(@PathVariable("id") int id) {
        return convertToSensorDTO(sensorsService.findOne(id)); // Jackson конвертирует в JSON
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                             BindingResult bindingResult) {

        sensorsValidator.validate(convertToSensor(sensorDTO), bindingResult);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors){
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new SensorOrMeasurementsNotCreatedException(errorMsg.toString());
        }
        sensorsService.save(convertToSensor(sensorDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<SensorOrMeasurementsErrorResponse> handleException(SensorOrMeasurementsNotFoundException e) {
        SensorOrMeasurementsErrorResponse response = new SensorOrMeasurementsErrorResponse("not found user", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<SensorOrMeasurementsErrorResponse> handleException(SensorOrMeasurementsNotCreatedException e) {
        SensorOrMeasurementsErrorResponse response = new SensorOrMeasurementsErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
