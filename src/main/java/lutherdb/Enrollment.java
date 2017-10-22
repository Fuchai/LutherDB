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
public class Enrollment {

    int student;
    int section;
    String grade;

    public Enrollment(int student, int section, String grade){
        this.student=student;
        this.section=section;
        this.grade=grade;
    }

    public int getStudent() {
        return this.student;
    }

    public int getSection() {
        return this.section;
    }

    public String getGrade() {
        return this.grade;
    }
}
