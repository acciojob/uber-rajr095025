package com.driver.model;


import javax.persistence.*;
import java.sql.Driver;

@Entity
public class Cab{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;

    private int perKmRate;

    private boolean available;


    @OneToOne
    @JoinColumn
    com.driver.model.Driver driver;
    public Cab() {
    }

    public Cab(int perKmRate, boolean available) {
        this.perKmRate = perKmRate;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPerKmRate() {
        return perKmRate;
    }

    public void setPerKmRate(int perKmRate) {
        this.perKmRate = perKmRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public com.driver.model.Driver getDriver() {
        return driver;
    }

    public void setDriver(com.driver.model.Driver driver) {
        this.driver = driver;
    }
}