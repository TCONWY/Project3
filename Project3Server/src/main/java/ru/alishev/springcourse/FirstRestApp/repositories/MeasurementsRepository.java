package ru.alishev.springcourse.FirstRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alishev.springcourse.FirstRestApp.models.Measurement;
import ru.alishev.springcourse.FirstRestApp.models.Sensor;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
}
