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
public class Student {

    String name;
    int graduationDate;
    int major;
    int advisor;

    public Student(String name,int graduationDate,int major,int advisor){
        this.name=name;
        this.graduationDate=graduationDate;
        this.major=major;
        this.advisor=advisor;
    }

    public String getName() {
        return this.name;
    }

    public int getGraduationDate() {
        return this.graduationDate;
    }

    public int getMajor() {
        return this.major;
    }

    public int getAdvisor() {
        return this.advisor;
    }
}
