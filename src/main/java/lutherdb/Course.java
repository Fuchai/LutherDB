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
public class Course {

    String department;
    String abbre;
    int number;
    String title;
    int credits;

    public Course(String department, String abbre, int number, String title, int credits){
        this.department=department;
        this.abbre=abbre;
        this.number=number;
        this.title=title;
        this.credits=credits;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getAbbreviation() {
        return this.abbre;
    }

    public int getNumber() {
        return this.number;
    }

    public String getTitle() {
        return this.title;
    }

    public int getCredits() {
        return this.credits;
    }
}
