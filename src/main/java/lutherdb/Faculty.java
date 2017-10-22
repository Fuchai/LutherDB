/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lutherdb;

/**
 *
 * @author JasonHu
 */
public class Faculty {

    private String name;
    private String department;
    private int startDate;
    private int endDate;
    private String building;
    private String room;


    public Faculty(String name,String department,int startDate,int endDate,String building,String room){
        this.name=name;
        this.department=department;
        this.startDate=startDate;
        this.endDate=endDate;
        this.building=building;
        this.room=room;
    }


    public String getName() {
        return this.name;
    }

    public String getDepartment() {
        return this.department;
    }

    public int getStartDate() {
        return this.startDate;
    }

    public String getBuilding() {
        return this.building;
    }

    public String getRoom(){return this.room;}

    public int getEndDate() {
        return this.endDate;
    }
}
