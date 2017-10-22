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
public class Major {

    String department;
    String name;

    public Major(String department, String name){
        this.department=department;
        this.name=name;
    }

    public String getDepartment() {
        return this.department;
    }

    public String getName() {
        return this.name;
    }
    
}
