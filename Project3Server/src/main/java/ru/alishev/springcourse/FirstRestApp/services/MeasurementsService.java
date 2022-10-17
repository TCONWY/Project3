package ru.alishev.springcourse.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alishev.springcourse.FirstRestApp.models.Measurement;
import ru.alishev.springcourse.FirstRestApp.repositories.MeasurementsRepository;
import ru.alishev.springcourse.FirstRestApp.util.SensorOrMeasurementsNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsService sensorsService) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsService = sensorsService;
    }

    public List<Measurement> findAll(){
        return measurementsRepository.findAll();
    }

    public Measurement findOne(int id){
        return measurementsRepository.findById(id).orElseThrow(SensorOrMeasurementsNotFoundException::new);
    }

    @Transactional
    public void save(Measurement measurement) {
        System.out.println(measurement.getRain());

        measurement.setSensor(sensorsService.findByName(measurement.getSensor().getName()).get());
        measurement.setCreatedAt(LocalDateTime.now());
        measurementsRepository.save(measurement);
    }
}
