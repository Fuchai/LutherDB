/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lutherdb;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author JasonHu
 */
public class DatabaseWriter {

    MysqlDataSource dataSource;
    Connection db_connection;
    Statement statement;

    public DatabaseWriter(String dbname,boolean IAMJASON){
        if (IAMJASON) {
            dataSource = new MysqlDataSource();
            dataSource.setUser("root");
            File password = new File("/Users/JasonHu/mysql.password");
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(password);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String passStr = "";
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            try {
                passStr = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dataSource.setPassword(passStr);
            try {
                db_connection=dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                statement=db_connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("Database connection established. Creating database.");
            String sql = "DROP DATABASE IF EXISTS " + dbname;

            try {
                statement.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sql = "CREATE DATABASE " + dbname;
            try {
                statement.execute(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            sql="SET time_zone='US/Central';";
//            try {
//                statement.executeUpdate(sql);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
            System.out.println("Database created under name "+dbname);

            dataSource.setDatabaseName(dbname);
            try {
                db_connection=dataSource.getConnection();
                statement=db_connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("Adaptability is the essence of human.\n" +
                    "I need your mysql log in credentials. Modify DatabaseWriter().");
        }
    }
//
//    // creates database file and tables.
//    public final String SQLITEDBPATH = "jdbc:sqlite:data/";

    public void query(String query){
        try {
            db_connection = dataSource.getConnection();
            statement = db_connection.createStatement();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = query;
        ResultSet resultSet;


        try {
            resultSet=statement.executeQuery(sql);

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
            db_connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void statementQuery(PreparedStatement statement){
        ResultSet resultSet;

        try {
            resultSet=statement.executeQuery();

            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS semester;");
        statement.executeUpdate("CREATE TABLE semester ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
                + "year INTEGER,"
                + "season TEXT);");

//        statement.execute("PRAGMA foreign_keys = ON;");

        statement.executeUpdate("DROP TABLE IF EXISTS location;");
        statement.executeUpdate("CREATE TABLE location ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
                + "building TEXT,"
                + "room INTEGER,"
                + "purpose TEXT);");

        statement.executeUpdate("DROP TABLE IF EXISTS department;");
        statement.executeUpdate("CREATE TABLE department ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
                + "name TEXT,"
                + "building TEXT);");

        statement.executeUpdate("DROP TABLE IF EXISTS course;");
        statement.executeUpdate("CREATE TABLE course ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
                + "abbreviation TEXT,"
                + "number INTEGER,"
                + "credits INTEGER,"
                + "department INTEGER," +
                "title TEXT,"
                + "FOREIGN KEY (department) REFERENCES department(id));");

        statement.executeUpdate("DROP TABLE IF EXISTS faculty;");
        statement.executeUpdate("CREATE TABLE faculty ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
                + "name TEXT," +
                "startDate INTEGER," +
                "endDate INTEGER," +
                "office INTEGER," +
                "department INTEGER,"
                + "FOREIGN KEY (department) REFERENCES department(id),"
                + "FOREIGN KEY (startDate) REFERENCES semester(id),"
                + "FOREIGN KEY (endDate) REFERENCES semester(id),"
                + "FOREIGN KEY (office) REFERENCES location(id));");

        statement.executeUpdate("DROP TABLE IF EXISTS major;");
        statement.executeUpdate("CREATE TABLE major ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                "department INTEGER,"
                + "FOREIGN KEY (department) REFERENCES department(id),"
                + "name TEXT);");

        statement.executeUpdate("DROP TABLE IF EXISTS section;");
        statement.executeUpdate("CREATE TABLE section ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                "course INTEGER," +
                "instructor INTEGER," +
                "offered INTEGER," +
                "location INTEGER,"
                + "FOREIGN KEY (course) REFERENCES course(id),"
                + "FOREIGN KEY (instructor) REFERENCES faculty(id),"
                + "FOREIGN KEY (offered) REFERENCES semester(id),"
                + "FOREIGN KEY (location) REFERENCES location(id),"
                + "startHour TIME);");

        statement.executeUpdate("DROP TABLE IF EXISTS student;");
        statement.executeUpdate("CREATE TABLE student ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,"
                + "name TEXT," +
                "graduationDate INTEGER," +
                "major INTEGER," +
                "advisor INTEGER,"
                + "FOREIGN KEY (graduationDate) REFERENCES semester(id),"
                + "FOREIGN KEY (major) REFERENCES major(id),"
                + "FOREIGN KEY (advisor) REFERENCES faculty(id));");

        statement.executeUpdate("DROP TABLE IF EXISTS enrollment;");
        statement.executeUpdate("CREATE TABLE enrollment ("
                + "id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL," +
                "student INTEGER," +
                "section INTEGER,"
                + "FOREIGN KEY (student) REFERENCES student(id),"
                + "FOREIGN KEY (section) REFERENCES section(id),"
                + "grade TEXT DEFAULT NULL);");

        db_connection.close();
    }

    // multiple write tables for entry inputs
    public void writeStudentTable(ArrayList<Student> studentArray) throws SQLException {
        // open connection
        Connection db_connection = dataSource.getConnection();
//        db_connection.createStatement().execute("PRAGMA foreign_keys = ON;");

        String sql = "INSERT INTO student(name, graduationdate, major, advisor) VALUES"
                + "(?,?,?,?)";
        for (Student student : studentArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, student.getName());
            statement_prepared.setInt(2, student.getGraduationDate()+1);
            statement_prepared.setInt(3, student.getMajor()+1);
            statement_prepared.setInt(4, student.getAdvisor()+1);
            try {
                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println(sql);
                System.out.print("\nException Triggered" + e + "\n");
            }
        }
        db_connection.close();
    }

    public void writeFacultyTable(ArrayList<Faculty> facultyArray) throws SQLException {
        // open connection
        Connection db_connection = dataSource.getConnection();

        String sql = "INSERT INTO faculty(name, department, startdate, enddate, office) VALUES"
                + "(?,(SELECT id FROM department WHERE name=?),?,?," +
                "(SELECT id FROM location WHERE (building=? and room=?)))";
        for (Faculty faculty : facultyArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, faculty.getName());
            statement_prepared.setString(2, faculty.getDepartment());
            statement_prepared.setInt(3, faculty.getStartDate()+1);
            int enddate=faculty.getEndDate();
            if (enddate!=-1){
                statement_prepared.setInt(4, faculty.getEndDate()+1);
            }else{
                statement_prepared.setNull(4, Types.INTEGER);
            }
            statement_prepared.setString(5, faculty.getBuilding());
            statement_prepared.setString(6, faculty.getRoom());
            try {
                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println(statement_prepared);
                System.out.print("\nException Triggered" + e + "\n");
            }
        }
        db_connection.close();
    }

    public void writeDepartmentTable(ArrayList<Department> departmentArray) throws SQLException {
        // open connection
        Connection db_connection = dataSource.getConnection();

        String sql = "INSERT INTO department(name, building) VALUES"
                + "(?,?)";
        for (Department department : departmentArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, department.getName());
            statement_prepared.setString(2, department.getBuilding());
            try {
                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println(sql);
                System.out.print("\nException Triggered" + e + "\n");
            }
        }
        db_connection.close();
    }

    public void writeCourseTable(ArrayList<Course> courseArray) throws SQLException {

        // open connection
        Connection db_connection = dataSource.getConnection();

        String sql = "INSERT INTO course(department, abbreviation, number, title, credits) VALUES"
                + "((select id from department where name=?),?,?,?,?)";
        for (Course course : courseArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, course.getDepartment());
            statement_prepared.setString(2, course.getAbbreviation());
            statement_prepared.setInt(3, course.getNumber());
            statement_prepared.setString(4, course.getTitle());
            statement_prepared.setInt(5, course.getCredits());
            try {
                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println(sql);
                System.out.print("\nException Triggered" + e + "\n");
            }
        }
        db_connection.close();
    }

    public void writeSectionTable(ArrayList<Section> sectionArray) throws SQLException {
        // open connection
        Connection db_connection = dataSource.getConnection();

//            String sql_speical="";
//
//        String sql = "INSERT INTO section(course, instructor, offered, location, startHour) VALUES "
//                + "((SELECT id from COURSE where abbreviation=? and number=?) AS T2 , " +
//                "(SELECT id FROM faculty " +
//                "where name= ? and department= (select DISTINCT id from course where abbreviation=?) AS T3  ," +
//                "?, (SELECT id FROM location where building=? and room=?) AS T4 ,?)";
//        for (Section section : sectionArray) {
//            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
//
//            statement_prepared.setString(1, section.getAbbre());
//            statement_prepared.setInt(2, section.getNumber());
//            statement_prepared.setString(3, section.getInstructor());
//            statement_prepared.setString(4, section.getAbbre());
//            statement_prepared.setInt(5, section.getOffered()+1);
//            statement_prepared.setString(6, section.getBuilding());
//            statement_prepared.setInt(7, section.getRoom());
//            statement_prepared.setTime(8, section.getStartHour());

        String sql = "INSERT INTO section(course, instructor, offered, location, startHour) VALUES (? , ? ,?, ? ,?)";
        String sql_speical="SELECT id from (SELECT id,number from COURSE where abbreviation=?) as T where number=?";
        String sql_special_2="select id from faculty where (name=? and department= (select id from department where name=?))";
        String sql_special_3="SELECT id FROM location where building=? and room=?";

        for (Section section : sectionArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql_speical);

            try {

                int theid;
                statement_prepared.setString(1,section.getAbbre());
                statement_prepared.setInt(2,section.getNumber());
                ResultSet rs;
                rs=statement_prepared.executeQuery();
                rs.next();
                theid=rs.getInt("id");
                statement_prepared.close();


                statement_prepared=db_connection.prepareStatement(sql_special_2);
                int instructor = 0;
                statement_prepared.setString(1,section.getInstructor());
                statement_prepared.setString(2,section.department);
//                System.out.println(statement_prepared);
                rs=statement_prepared.executeQuery();
                rs.next();
                instructor=rs.getInt("id");
                statement_prepared.close();

                statement_prepared=db_connection.prepareStatement(sql_special_3);
                int location = 0;
                statement_prepared.setString(1,section.getBuilding());
                statement_prepared.setInt(2,section.getRoom());
//                System.out.println(statement_prepared);
                rs=statement_prepared.executeQuery();
                rs.next();
                location=rs.getInt("id");
                statement_prepared.close();


                statement_prepared = db_connection.prepareStatement(sql);
                statement_prepared.setInt(1,theid);
                statement_prepared.setInt(2, instructor);
                statement_prepared.setInt(3, section.getOffered()+1);
                statement_prepared.setInt(4, location);
                statement_prepared.setTime(5, section.getStartHour());

                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println("*********************");
                e.printStackTrace();
                System.out.println(statement_prepared);


//                sql="select DISTINCT department from course where abbreviation=?";
//                System.out.println(sql);
//                statement_prepared = db_connection.prepareStatement(sql);
//                statement_prepared.setString(1, section.getAbbre());
//                this.statementQuery(statement_prepared);



//                sql="select DISTINCT department from course where abbreviation=?";
//                System.out.println(sql);
//                statement_prepared = db_connection.prepareStatement(sql);
//                statement_prepared.setString(1, section.getAbbre());
//                this.statementQuery(statement_prepared);
//
//                sql="SELECT id from COURSE where (abbreviation=? and number=?)";
//                System.out.println(sql);
//                statement_prepared = db_connection.prepareStatement(sql);
//                statement_prepared.setString(1, section.getAbbre());
//                statement_prepared.setInt(2, section.getNumber());
//                this.statementQuery(statement_prepared);
//
//                sql="SELECT id FROM faculty where name=?";
//                System.out.println(sql);
//                statement_prepared = db_connection.prepareStatement(sql);
//                statement_prepared.setString(1, section.getInstructor());
//                this.statementQuery(statement_prepared);
////                System.out.println(section.getInstructor());
//
                sql="SELECT id FROM location where (building=? and room=?)";
                System.out.println(sql);
                statement_prepared = db_connection.prepareStatement(sql);
                statement_prepared.setString(1, section.getBuilding());
                statement_prepared.setInt(2, section.getRoom());
                this.statementQuery(statement_prepared);


                System.out.println("&&&&&&&&&&&&&&&&&&&&&&");
//                e.printStackTrace();

            }
        }
        db_connection.close();
    }

    public void writeEnrollmentTable(ArrayList<Enrollment> enrollmentArray) throws SQLException {
        // open connection
        Connection db_connection = dataSource.getConnection();

        String sql = "INSERT INTO enrollment(student, section, grade) VALUES"
                + "(?,?,?)";
        for (Enrollment enrollment : enrollmentArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setInt(1, enrollment.getStudent()+1);
            statement_prepared.setInt(2, enrollment.getSection()+1);
            statement_prepared.setString(3, enrollment.getGrade());
            try {
                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println(sql);
                System.out.print("\nException Triggered" + e + "\n");
            }
        }
        db_connection.close();
    }

    public void writeLocationTable(ArrayList<Location> locationArray) throws SQLException {
        // open connection
        Connection db_connection = dataSource.getConnection();

        String sql = "INSERT INTO location(building, room, purpose) VALUES"
                + "(?,?,?)";
        for (Location location : locationArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, location.getBuilding());
            statement_prepared.setInt(2, location.getRoom());
            statement_prepared.setString(3, location.getPurpose());

            try {
                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println(sql);
                System.out.print("\nException Triggered" + e + "\n");
            }
        }
        db_connection.close();
    }

    public void writeSemesterTable(ArrayList<Semester> semesterArray) throws SQLException {

        // open connection
        Connection db_connection = dataSource.getConnection();

        String sql = "INSERT INTO semester(year, season) VALUES"
                + "(?,?)";
        for (Semester semester : semesterArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setInt(1, semester.getYear());
            statement_prepared.setString(2, semester.getSeason());
            try {
                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println(sql);
                System.out.print("\nException Triggered" + e + "\n");
            }
        }
        db_connection.close();
    }

    public void writeMajorTable(ArrayList<Major> majorArray) throws SQLException {
        // open connection
        Connection db_connection = dataSource.getConnection();

        String sql = "INSERT INTO major(department, name) VALUES"
                + "((SELECT id FROM department WHERE name=?),?)";
        for (Major major : majorArray) {
            PreparedStatement statement_prepared = db_connection.prepareStatement(sql);
            statement_prepared.setString(1, major.getDepartment());
            statement_prepared.setString(2, major.getName());
            try {
                statement_prepared.executeUpdate();
            } catch (SQLException e) {
                System.out.println(sql);
                System.out.print("\nException Triggered" + e + "\n");
            }
        }
        db_connection.close();
    }

}
