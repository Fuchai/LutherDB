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
public class Location {

    String building;
    int room;
    String purpose;

    public Location(String building, int room, String purpose){
        this.building=building;
        this.room=room;
        this.purpose=purpose;
    }


    String getBuilding() {
        return this.building;
    }

    int getRoom() {
        return this.room;
    }

    String getPurpose() {
        return this.purpose;
    }
    
}
