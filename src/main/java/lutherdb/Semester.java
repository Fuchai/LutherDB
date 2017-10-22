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
public class Semester {
    int year;
    String season;

    public Semester(int year, String season){
        this.year=year;
        this.season=season;
    }

    int getYear() {
        return this.year;
    }

    String getSeason() {
        return this.season;
    }
    
}
