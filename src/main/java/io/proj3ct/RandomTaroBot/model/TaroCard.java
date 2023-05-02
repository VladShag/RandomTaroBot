package io.proj3ct.RandomTaroBot.model;

import javax.persistence.Entity;
import javax.persistence.Id;



@Entity(name = "tarocards")
public class TaroCard {
    @Id
    private Integer id;

    private String name;
    private String generalSense;
    private String loveSense;
    private String workSense;
    private String healthSense;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeneralSense() {
        return generalSense;
    }

    public void setGeneralSense(String generalSense) {
        this.generalSense = generalSense;
    }

    public String getLoveSense() {
        return loveSense;
    }

    public void setLoveSense(String loveSense) {
        this.loveSense = loveSense;
    }

    public String getWorkSense() {
        return workSense;
    }

    public void setWorkSense(String workSense) {
        this.workSense = workSense;
    }

    public String getHealthSense() {
        return healthSense;
    }

    public void setHealthSense(String healthSense) {
        this.healthSense = healthSense;
    }
}
