/*
 */
package me.shafin.text2query;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SHAFIN
 */
public class Converter {

    public static List<String> convertCourseGradeSheet(String filePath, int inputFormat) {

        String csvFile = filePath;
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        List<String> queryList = new ArrayList<>();

        try {
            String examYear = "";
            String semesterNumber = "";

            int lineCounter = 1;

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] dataSplit = line.split(cvsSplitBy);

                if (lineCounter == 1) {
                    String query = "CALL sustord.find_student_batch_id('" + dataSplit[0] + "', '" + dataSplit[1] + "', '" + dataSplit[2] + "', '" + dataSplit[3] + "',@batch_id);";
                    System.out.println(query);
                    queryList.add(query);
                } else if (lineCounter == 2) {
                    String query = "CALL sustord.find_course_id('" + dataSplit[0] + "', '" + dataSplit[1] + "', " + " @course_id);";
                    queryList.add(query);
                    System.out.println(query);

                    examYear = dataSplit[2];
                    semesterNumber = dataSplit[3];

                    query = "CALL sustord.find_syllabus_id(@batch_id,@course_id,@syllabus_id);";
                    System.out.println(query);
                    queryList.add(query);

                    query = "DELETE FROM sustord.course_registration WHERE syllabus_id_fk = @syllabus_id and attend_semester = " + semesterNumber + ";";
                    System.out.println(query);
                    queryList.add(query);

                } else {
                    String query = "call sustord.find_student_info_id('" + dataSplit[0] + "',@reg" + dataSplit[0] + ");";
                    //System.out.println(query);
                    queryList.add(query);

                    String grade;
                    if (inputFormat == 1) {
                        grade = GradeFormatter.marksToGrade(Double.parseDouble(dataSplit[2]));
                    } else {
                        grade = dataSplit[2];
                    }

                    if (!grade.equals("n")) {

                        query = "INSERT INTO `course_registration` (`student_info_id_fk`, `attend_year`, `attend_semester`, `syllabus_id_fk`, `approval`, `grade`) VALUES (@reg" + dataSplit[0] + "," + examYear + "," + semesterNumber + ",@syllabus_id,1,  '" + grade + "');";
                        //System.out.println(query);
                        queryList.add(query);
                    }

                }
                lineCounter++;

            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found " + e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        System.out.println(".......");
        return queryList;
    }

    public static List<String> convertMarkSheet(String filePath) {
        String csvFile = filePath;
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";

        List<String> queryList = new ArrayList<>();

        try {
            int lineCounter = 1;

            String deptCode;
            String degree;
            String degreeType;
            String session;

            String examYear = "";
            String semesterNumber = "";

            List<String> courseCodeList = new ArrayList<>();
            List<String> courseTitleList = new ArrayList<>();

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] dataSplit = line.split(cvsSplitBy);

                if (lineCounter == 1) {
                    deptCode = dataSplit[0];
                    degree = dataSplit[1];
                    degreeType = dataSplit[2];
                    session = dataSplit[3];

                    String query = "CALL sustord.find_student_batch_id('" + deptCode + "', '" + degree + "', '" + degreeType + "', '" + session + "',@batch_id);";
                    //System.out.println(query);
                    queryList.add(query);

                } else if (lineCounter == 2) {
                    examYear = dataSplit[0];
                    semesterNumber = dataSplit[1];

                } else if (lineCounter == 3) {

                    for (String s : dataSplit) {
                        if (!s.isEmpty()) {
                            courseCodeList.add(s);
                        }
                    }

                } else if (lineCounter == 4) {

                    for (int i = 0; i < dataSplit.length; i++) {
                        boolean commaFlag = false;
                        String title = dataSplit[i];

                        if (!title.isEmpty()) {

                            if (title.startsWith("\"")) {
                                title = title.substring(1, dataSplit[i].length());

                                commaFlag = true;

                                int counter = 0;
                                while (commaFlag) {
                                    counter++;

                                    if (dataSplit[i].endsWith("\"")) {
                                        title += "," + dataSplit[i].substring(0, dataSplit[i].length() - 1);
                                        commaFlag = false;
                                    } else {
                                        if (counter != 1) {
                                            title += "," + dataSplit[i];
                                        }
                                        i++;
                                    }
                                }
                            }
                            courseTitleList.add(title);
                        }
                    }
//                    //System.out.println(courseTitleList.size());
//                    for(String s: courseTitleList){
//                        System.out.println(s);
//                    }

                } else {
                    //  System.out.println(dataSplit[0]+" "+dataSplit[1]+" "+dataSplit[2]);
                    String registrationNo = dataSplit[0];

                    String query = "CALL sustord.find_student_info_id('" + registrationNo + "',@reg" + registrationNo + ");";
                    queryList.add(query);

                    query = "DELETE FROM sustord.course_registration WHERE attend_semester = " + semesterNumber + " and student_info_id_fk = @reg" + registrationNo + ";";
                    queryList.add(query);
//System.out.println(dataSplit.length);

                    for (int k = 2; k < dataSplit.length; k++) {
                        String grade = dataSplit[k];
                        if (!grade.equals("n")) {
                            String courseCode = courseCodeList.get(k - 2);
                            String courseTitle = courseTitleList.get(k - 2);
                            query = "CALL sustord.find_course_id('" + courseCode + "', '" + courseTitle + "', @course_code);";
                            queryList.add(query);

                            query = "CALL sustord.find_syllabus_id(@batch_id,@course_code,@syllabus_id);";
                            queryList.add(query);

                            query = "INSERT INTO `course_registration` (`student_info_id_fk`, `attend_year`, `attend_semester`, `syllabus_id_fk`, `approval`, `grade`) VALUES"
                                    + "(@reg" + registrationNo + "," + examYear + "," + semesterNumber + ",@syllabus_id,1,  '" + grade + "');";
                            queryList.add(query);
                        } else {
                            //System.out.println(courseCodeList.get(k - 2)+" "+dataSplit[k]);
                        }

                    }
                }
                lineCounter++;

            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found " + e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }

        System.out.println(".......");
        return queryList;
    }
}
