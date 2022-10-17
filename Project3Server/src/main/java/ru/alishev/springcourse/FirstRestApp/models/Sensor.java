package ru.alishev.springcourse.FirstRestApp.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author Neil Alishev
 */
@Entity
@Table(name = "sensors")
public class Sensor {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should not be between 2 and 30 characters")
    private String name;

    public Sensor() {

    }

    public Sensor(String name, int temperature, String weatherStatus) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
