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
public class Department {

    String name;
    String building;

    Department(String name,String building){
        this.name=name;
        this.building=building;
    }

    public String getName() {
        return this.name;
    }

    public String getBuilding() {
        return this.building;
    }
}
