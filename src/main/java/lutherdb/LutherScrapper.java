package lutherdb;

import helper.FakeStudent;
import io.bloco.faker.Faker;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.util.*;

class LutherScrapper {

    // multiple scrapper functionalities that access raw inputs to create
    // arraylists that can be fed into DatabaseWriter() directly to produce data

    Faker faker;

    ArrayList<Faculty> facultyList;
    ArrayList<Location> locationList;
    ArrayList<Semester> semesterList;
    ArrayList<Department> departmentList;
    ArrayList<Student> studentList;
    ArrayList<Course> courseList;
    ArrayList<Major> majorslist;
    ArrayList<Section> sectionList;
    ArrayList<Enrollment> enrollmentList;

    Random randomgenerator=new Random();

    private int totalSemester;
    private int totalFaculty;
    private int totalDepartment;
    private int totalMajors;
    private int totalStudents=1000;

    Map<String, String> acroDeptMap = new HashMap<>();
    Map<String, ArrayList<String>> deptFaculty=new HashMap<>();
    Map<String, Set<Integer>> offices=new HashMap<>();
    private int totalSections;

    //parameters
    public static void main(String[] args) {
        // scrap faculty and calculate the maximum number to draw sample from

        System.out.println("Scrapping...");
        LutherScrapper ls = new LutherScrapper();
        ls.scrapFacultyList("src/main/resources/faculty_listing.txt");
        ls.scrapMajors("src/main/resources/lc_majors.txt");
        ls.scrapStudent(ls.totalStudents, ls.totalSemester, ls.totalFaculty, ls.totalMajors);
        ls.scrapCourses("src/main/resources/lc_courses.txt");
        ls.scrapSections();
        ls.scrapEnrollment(ls.totalSections,ls.totalStudents);

        System.out.println("Scrapping is done.");
    }

    public void scrap(){
        System.out.println("Scrapping...");
        this.scrapFacultyList("src/main/resources/faculty_listing.txt");
        this.scrapMajors("src/main/resources/lc_majors.txt");
        this.scrapStudent(this.totalStudents, this.totalSemester, this.totalFaculty, this.totalMajors);
        this.scrapCourses("src/main/resources/lc_courses.txt");
        this.scrapSections();
        this.scrapEnrollment(this.totalSections,this.totalStudents);

        System.out.println("Scrapping is done.");
    }

    public int getTotalDepartment() {
        return totalDepartment;
    }

    public int getTotalFaculty() {

        return totalFaculty;
    }

    public int getTotalSemester() {

        return totalSemester;
    }

    LutherScrapper() {
        this.faker = new Faker();
    }


    public void scrapFacultyList(String textFilePath) {

        // line by line and word by word parsing
        // judges whether this is a name, an office, an extension, an email or an deparmtent
        // four (three) pointers for each
        // if a name, generate a new line,
        // if others, fill in where the pointer is, move to the next pointer

        // To parse, note that a tab always is in between two fields.
        // Generate a long list, separated by tabs, remove whitespaces beginning and end.

        File file = new File(textFilePath);
        ArrayList<String[]> facultyInfo = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            ArrayList<String> processed = new ArrayList<String>();

            while ((line = bufferedReader.readLine()) != null) {
                // parse the line into tab delimited words, remove trailing white spaces
                String[] splitted = line.split("\\t");
                for (String s : splitted) {
                    String k = s.trim();
                    if (!k.isEmpty()) {
                        processed.add(k);
                    }
                }
            }

            // remove weird symbols
            ListIterator<String> it = processed.listIterator();
            while (it.hasNext()) {
                if (it.next().length() <= 3) {
                    it.remove();
                }
            }

            // remove brackets
            it = processed.listIterator();
            while (it.hasNext()) {
                String k = it.next();
                String s = k;
                while (s.contains("(")) {
                    int start = s.indexOf("(");
                    int end = s.indexOf(")");
                    s = s.substring(0, start) + s.substring(end + 1);
                }
                if (s.contains(")")) {
                    System.out.print(k);
                    System.out.print(s);
                    throw new Exception("well");
                }
                it.set(s);
            }

            // remove titles
            it = processed.listIterator();
            while (it.hasNext()) {
                String s = it.next();
                int secondPos = StringUtils.ordinalIndexOf(s, ",", 2);
                if (secondPos != -1) {
                    s = s.substring(0, secondPos);
                    it.set(s);
                }
            }

            // remove empty again
            it = processed.listIterator();
            while (it.hasNext()) {
                if (it.next().isEmpty()) {
                    it.remove();
                }
            }

            // judge and append
            int officePtr = 0;
            String dept = "";
            it = processed.listIterator();
            while (it.hasNext()) {
                String thisLine = it.next();

                if (thisLine.matches("[A-Z\\s&,]+")) {
                    dept = thisLine;
//                    if (dept.contains("SOCIOLOGY")){
//                        System.out.println("I found it");
//                    }
                } else if (thisLine.contains(",")) {
                    String[] entry = new String[4];
                    entry[0] = thisLine;
                    entry[3] = dept;
                    facultyInfo.add(entry);
                } else {
                    try {
                        assert (thisLine.matches(".*[0-9]+.*"));
//                        if(officePtr==240){
//                            System.out.println("I found it.");
//                        }
                        facultyInfo.get(officePtr)[1] = thisLine;
                        officePtr++;
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        System.out.print(it.nextIndex());
                    }
                }
            }

            ListIterator<String[]> fit = facultyInfo.listIterator();
            while (fit.hasNext()) {
                String[] newEntry = new String[4];
                String[] faculPos = fit.next();
                String buildingRoom = faculPos[1];
                //split and test for null
                int i = 0;
                try {
                    while (i < buildingRoom.length() && !Character.isDigit(buildingRoom.charAt(i))) {
                        i++;
                        // i is digit
                    }
                } catch (NullPointerException e) {
                    System.out.println(buildingRoom);
                    System.out.println(i);
                    System.out.println(fit.nextIndex());
                    System.out.println(fit.next()[0]);
                }
                String building = buildingRoom.substring(0, i);
                String roomNumber = buildingRoom.substring(i);
                newEntry[1] = building.trim();
                newEntry[2] = roomNumber.trim();
                newEntry[0] = faculPos[0].trim();
                newEntry[3] = faculPos[3].trim();
                fit.set(newEntry);
            }

            fit = facultyInfo.listIterator();
            while (fit.hasNext()) {
                String[] entry = fit.next();
                if (entry[2].equals("999")) {
                    String[] newEntry = new String[4];
                    newEntry[0] = entry[0];
                    newEntry[3] = entry[3];
                    newEntry[1] = "";
                    newEntry[2] = "";
                    fit.set(newEntry);
                }
            }

            // turn all room numbers to ints.
            try {
                fit = facultyInfo.listIterator();
                while (fit.hasNext()) {
                    String[] entry = fit.next();
                    if (!entry[2].isEmpty()) {
                        if (entry[2].matches(".*[A-Z].*")) {
//                            System.out.println(entry[0]+" "+entry[2]);
                            String[] newEntry = new String[4];
                            newEntry[0] = entry[0];
                            newEntry[1] = entry[1];
                            newEntry[2] = entry[2].substring(0, 3);
                            newEntry[3] = entry[3];
                            fit.set(newEntry);
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Faculty info has been finished

        // creating return results

        // creating locationList
        ArrayList<Location> locationList = new ArrayList<>();

        Map<String, TreeSet<String>> buildingRoom = new HashMap<>();
        ListIterator<String[]> fit;
        fit = facultyInfo.listIterator();
        while (fit.hasNext()) {
            String[] entry = fit.next();
            if (!entry[1].equals("")) {
                if (!buildingRoom.containsKey(entry[1])) {
                    buildingRoom.put(entry[1], new TreeSet<>());
                    if (!entry[2].equals("")) {
                        buildingRoom.get(entry[1]).add(entry[2]);
                    }
                } else {
                    if (!entry[2].equals("")) {
                        buildingRoom.get(entry[1]).add(entry[2]);
                    }
                }
//                    if (entry[2].equals("")){
//                        System.out.println(entry[0]+entry[1]+entry[2]+entry[3]);
//                        throw new Exception("Your mom is crying");
//                    }
            }
        }

        for (Map.Entry<String, TreeSet<String>> entry : buildingRoom.entrySet()) {
            Iterator<String> roomNo = entry.getValue().iterator();
            while (roomNo.hasNext()) {
                String roomString = roomNo.next();
                int a = Integer.parseInt(roomString);
//                if(a==0){
//                    System.out.println("**********************");
//                }
                if (!offices.containsKey(entry.getKey())){
                    offices.put(entry.getKey(),new HashSet<>());
                }
                offices.get(entry.getKey()).add(a);
                locationList.add(new Location(entry.getKey(), a, "office"));
            }
        }

        // create semesterList
        ArrayList<Semester> semesterList = new ArrayList<>();

        int[] yearList = new int[]{2015, 2016, 2017, 2018};
        String[] seasons = new String[]{"Summer1", "Summer2", "Autumn1", "Autumn2", "January", "Spring1", "Spring2"};
        for (int year : yearList) {
            for (String season : seasons) {
                semesterList.add(new Semester(year, season));
            }
        }

        // create department list
        ArrayList<Department> deptList = new ArrayList<>();

        fit = facultyInfo.listIterator();
        String deptName = facultyInfo.get(1)[3];
        ArrayList<String> ofDepartment = new ArrayList<>();
        String[] entry = new String[0];
        while (fit.hasNext()) {
            entry = fit.next();
            if (entry[3].equals(deptName)) {
                if (deptName=="SOCIOLOGY, ANTHROPOLOGY, SOCIAL WORK"){
                    System.out.println("I found it!");
                }
                ofDepartment.add(entry[1]);
            } else {
                // finding the highest
                int freqMax = 0;
                String buildingMax = "";
                Set<String> uniqueBuildings = new HashSet<>(ofDepartment);
                Iterator<String> uniqueBuildingIter = uniqueBuildings.iterator();
                while (uniqueBuildingIter.hasNext()) {
                    String uniqueBuilding = uniqueBuildingIter.next();
                    int freq = Collections.frequency(ofDepartment, uniqueBuilding);
                    if (freq > freqMax) {
                        buildingMax = uniqueBuilding;
                        freqMax = freq;
                    }
                }
                deptList.add(new Department(deptName, buildingMax));
                deptName = entry[3];
                ofDepartment = new ArrayList<>();
            }
        }

        int freqMax = 0;
        String buildingMax = "";
        Set<String> uniqueBuildings = new HashSet<>(ofDepartment);
        Iterator<String> uniqueBuildingIter = uniqueBuildings.iterator();
        while (uniqueBuildingIter.hasNext()) {
            String uniqueBuilding = uniqueBuildingIter.next();
            int freq = Collections.frequency(ofDepartment, uniqueBuilding);
            if (freq > freqMax) {
                buildingMax = uniqueBuilding;
                freqMax = freq;
            }
        }
        deptList.add(new Department(deptName, buildingMax));

        // create facultyList

        ArrayList<Faculty> facultyList = new ArrayList<>();

        // sample from length semesterList.
        int totalSemesters = semesterList.size();

        fit = facultyInfo.listIterator();
        while (fit.hasNext()) {
            entry = fit.next();

            int startSem = randomgenerator.nextInt(totalSemesters);
            int nextRange = totalSemesters - startSem + 1;
            int endSem = randomgenerator.nextInt(nextRange);
            endSem = startSem + endSem;

            if (endSem == totalSemesters) {
                endSem = -1;
            }
            facultyList.add(new Faculty(entry[0], entry[3], startSem, endSem, entry[1], entry[2]));
        }


//        Print
//            for (String[] s: facultyInfo){
//                System.out.print(s[0]+" "+s[1]+" "+s[2]+" "+s[3]+"\n");
//            }
//            System.out.println("******************************");

        this.facultyList = facultyList;
        this.locationList = locationList;
        this.semesterList = semesterList;
        this.departmentList = deptList;
        this.totalSemester = totalSemesters;
        this.totalDepartment = deptList.size();
        this.totalFaculty = facultyList.size();
    }

    public void scrapMajors(String textFilePath) {
        File file = new File(textFilePath);
        ArrayList<Major> majors = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String[] fields;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                fields = line.split("\\|");
                Major maj;
                try {
                    if (fields.length > 3) {
                        maj = new Major(fields[3], fields[1]);
                        String[] acros=fields[0].split(",");
                        for (String acro: acros){
                            acroDeptMap.put(acro, fields[3]);
                        }
                        majors.add(maj);
                    } else {
                        maj = new Major(fields[2], fields[0]);
                        majors.add(maj);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    System.out.println(fields[0] + fields[1] + fields[2]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.majorslist = majors;
        this.totalMajors = majors.size();
    }


    public void scrapStudent(int howMany, int totalSemester, int totalFaculty, int totalMajors) {
        ArrayList<Student> studentList = new ArrayList<>();
        FakeStudent fs = new FakeStudent();
        String[] nameList = fs.getNames(howMany);

        for (int i = 0; i < howMany; i++) {
            studentList.add(new Student(nameList[i], randomgenerator.nextInt(totalSemester),
                    randomgenerator.nextInt(totalMajors), randomgenerator.nextInt(totalFaculty)));
        }
        studentList.add(new Student("John Doe", randomgenerator.nextInt(totalSemester),
                randomgenerator.nextInt(totalMajors), randomgenerator.nextInt(totalFaculty)));
        this.studentList = studentList;
    }


    // now we have
    // faculty, location, semester, department, major, student,
    // we need: enrollment, section, course

    public void scrapCourses(String textFilePath) {
        File file = new File(textFilePath);
        ArrayList<Course> courses = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String[] fields;
            String[] courseNumber;
            String deptName;

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                fields = line.split("\\|");
                courseNumber=fields[0].split("\\s+");
                deptName=acroDeptMap.get(courseNumber[0]);
                courses.add(new Course(deptName,courseNumber[0],Integer.parseInt(courseNumber[1]),
                        fields[1],Integer.parseInt(fields[2])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.courseList=courses;
    }

    public void scrapSections() {
        // get dept_faculty mapping
        ArrayList<Section> sections=new ArrayList<>();
        Iterator<Faculty> fit=facultyList.iterator();

        while(fit.hasNext()){
            Faculty faculty= fit.next();
            if (!deptFaculty.containsKey(faculty.getDepartment())){
                deptFaculty.put(faculty.getDepartment(),new ArrayList<>());
            }
            try{
                deptFaculty.get(faculty.getDepartment()).add(faculty.getName());}
            catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        Iterator<Course> cit=courseList.iterator();

        while(cit.hasNext()){
            Course course= cit.next();

            // get an instructor
            String facultyName=null;
            String deptName = null;
            try {
                deptName = acroDeptMap.get(course.abbre);
                ArrayList<String> pool = deptFaculty.get(deptName);
                int max = pool.size();
                int sample = randomgenerator.nextInt(max);
                facultyName= pool.get(sample);
            }catch (NullPointerException e){
                System.out.println(deptName);
                e.printStackTrace();
            }

            // get a semester
            int whichsemester=randomgenerator.nextInt(totalSemester);
//            if (deptName.equals("VISUAL & PERFORMING ARTS")){
//                System.out.println("VISUAL");
//            }
            // build a room where the department building is.
            String building = null;
            boolean cont=true;
            Iterator<Department> dit=departmentList.iterator();
            while(dit.hasNext()&&cont){
                Department dept=dit.next();
                if (deptName==null){
                    System.out.println("you have no idea what you've done");
                }
                if (dept.name.equals(deptName)){
                    building=dept.building;
                    cont=false;
                }
            }

            offices.put("Rochester",new HashSet<>());
            boolean gotit= false;
            int roomNumber=0;
            while(!gotit){
                roomNumber=randomgenerator.nextInt(400);
                try{
                    if (!offices.get(building).contains(roomNumber)){
                        gotit=true;
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            offices.get(building).add(roomNumber);
            locationList.add(new Location(building,roomNumber,"classroom"));

            //get a starthour
            List<String>possibleStartHours= Arrays.asList("8:00","9:30","11:00", "1:00", "2:30", "4:00");
            Collections.shuffle(possibleStartHours);
            if (course.credits>2){
                for (int i=0;i<2;i++){
                    String startHour = possibleStartHours.get(i);
                    Section sec;
                    try {
                        sec = new Section(course.abbre,course.number,facultyName,whichsemester,building,
                                roomNumber,startHour,deptName);
                        sections.add(sec);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                String startHour = possibleStartHours.get(0);
                Section sec;
                try {
                    sec = new Section(course.abbre,course.number,facultyName,whichsemester,building,
                            roomNumber,startHour,deptName);
                    sections.add(sec);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        this.sectionList=sections;
        this.totalSections=sections.size();
    }

    public void scrapEnrollment(int totalSections,int totalStudents){
        ArrayList<Enrollment> enrollments=new ArrayList<>();
        String[] grades={"A","B","C","D","E","F","G"};
        for(int sid=0; sid<totalStudents; sid++){
            for(int i=0; i<4; i++){
                int secid=randomgenerator.nextInt(totalSections);
                enrollments.add(new Enrollment(sid,secid,
                        grades[randomgenerator.nextInt(7)]));
            }
        }
        this.enrollmentList=enrollments;
    }
}

