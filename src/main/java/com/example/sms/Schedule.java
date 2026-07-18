package com.example.sms;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Days;
    private String First;
    private String Second;
    private String Third;
    private String Fourth;
    private String Fifth;
    private String Sixth;
    private String Seventh;
    private String Eighth;

    public Schedule(){}

    public void setDays(String days) {
        this.Days = days;
    }

    public void setFirst(String first) {
        this.First = first;
    }

    public void setSecond(String second) {
        this.Second = second;
    }

    public void setThird(String third) {
        this.Third = third;
    }

    public void setFourth(String fourth){
        this.Fourth=fourth;
    }

    public void setFifth(String fifth) {
        this.Fifth=fifth;
    }

    public void setSixth(String sixth){
        this.Sixth=sixth;
    }

    public void setSeventh(String seventh) {
        this.Seventh = seventh;
    }

    public void setEighth(String eighth) {
        this.Eighth = eighth;
    }

    public Long getId() {
        return id;
    }

    public String getDays() {
        return Days;
    }

    public String getFirst() {
        return First;
    }

    public String getThird() {
        return Third;
    }

    public String getSecond() {
        return Second;
    }

    public String getEighth() {
        return Eighth;
    }

    public String getFifth() {
        return Fifth;
    }

    public String getFourth() {
        return Fourth;
    }

    public String getSeventh() {
        return Seventh;
    }

    public String getSixth() {
        return Sixth;
    }
}

