package ru.alishev.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.MeasurementDTO;
import ru.alishev.springcourse.FirstRestApp.models.Measurement;
import ru.alishev.springcourse.FirstRestApp.services.MeasurementsService;
import ru.alishev.springcourse.FirstRestApp.services.SensorsService;
import ru.alishev.springcourse.FirstRestApp.util.MeasurementValidator;
import ru.alishev.springcourse.FirstRestApp.util.SensorOrMeasurementsErrorResponse;
import ru.alishev.springcourse.FirstRestApp.util.SensorOrMeasurementsNotCreatedException;
import ru.alishev.springcourse.FirstRestApp.util.SensorOrMeasurementsNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final MeasurementsService measurementsService;
    private  final SensorsService sensorsService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, SensorsService sensorsService, ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.measurementsService = measurementsService;
        this.sensorsService = sensorsService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    @GetMapping()
    public List<MeasurementDTO> getMeasurements() {
        return measurementsService.findAll().stream().map(this::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO,
                                           BindingResult bindingResult){
        System.out.println(measurementDTO.getSensor() + "qwe");
        Measurement measurementToAdd = convertToMeasurement(measurementDTO);
        measurementValidator.validate(measurementToAdd, bindingResult);
        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }
            throw new SensorOrMeasurementsNotCreatedException(errorMsg.toString());
        }
        measurementsService.save(measurementToAdd);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @GetMapping("/rainyDaysCount")
    public Long getRainsCount() {
        return measurementsService.findAll().stream().filter(Measurement::getRain).count();
    }

    @ExceptionHandler
    private ResponseEntity<SensorOrMeasurementsErrorResponse> handleException(SensorOrMeasurementsNotFoundException e) {
        SensorOrMeasurementsErrorResponse response = new SensorOrMeasurementsErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

}
