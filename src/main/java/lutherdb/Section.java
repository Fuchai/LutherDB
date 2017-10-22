/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lutherdb;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author JasonHu
 */
public class Section {

    String abbre;
    int number;
    String instructor;
    int offered;
    String building;
    int room;
    java.sql.Time startHour;
    String department;

    public Section(String abbre, int number, String instructor, int offered, String building, int room, String startHour, String department) throws ParseException {
        this.abbre=abbre;
        this.number=number;
        this.instructor=instructor;
        this.offered=offered;
        this.building=building;
        this.department=department;
        this.room=room;
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        this.startHour=new java.sql.Time(formatter.parse(startHour).getTime());
    }

    public int getNumber() {
        return number;
    }

    public String getAbbre() {
        return abbre;
    }

    public String getInstructor() {
        return this.instructor;
    }

    public int getOffered() {
        return this.offered;
    }

    public int getRoom() {
        return this.room;
    }

    public String getBuilding() {
        return building;
    }

    public Time getStartHour() {
        return this.startHour;
    }
}
