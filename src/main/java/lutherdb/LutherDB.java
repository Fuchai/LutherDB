/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lutherdb;

import java.sql.SQLException;

/**
 *
 * @author JasonHu
 */
public class LutherDB {

    /**
     * @param args the command line arguments
     */

    LutherScrapper LS=new LutherScrapper();
    String dbname="JASONDB";
    DatabaseWriter DW=new DatabaseWriter(dbname,true);

    public static void main(String[] args) {
        LutherDB lutherdb=new LutherDB();
        lutherdb.run();
    }

    private void run(){
        LS.scrap();
        System.out.println("Creating tables and filling in data...");
        DB();
        System.out.println("Database has been created and data has been filled out.\n" +
                "Go to URL "+DW.dataSource.getURL());
        TRIGG_VIEW();
        System.out.println("Triggers and Views created.");

    }

    private void TRIGG_VIEW(){
        DW.writeTriggers();
        DW.writeViews();
    }

    private void DB() {
        //uses information stored in LS arraylists to build db
        try {
            DW.createTables();

            DW.writeDepartmentTable(LS.departmentList);
            DW.writeSemesterTable(LS.semesterList);
            DW.writeLocationTable(LS.locationList);
//            DW.query("select * from semester");
            DW.writeFacultyTable(LS.facultyList);
            DW.writeMajorTable(LS.majorslist);

            //
            DW.writeCourseTable(LS.courseList);
            DW.writeSectionTable(LS.sectionList);
            DW.writeStudentTable(LS.studentList);
            DW.writeEnrollmentTable(LS.enrollmentList);

            DW.JohnDoe();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


