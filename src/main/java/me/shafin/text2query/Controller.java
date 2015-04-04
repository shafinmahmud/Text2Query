/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.shafin.text2query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SHAFIN
 */
public class Controller {

    public static void main(String[] args) {

        String csvFile = "C:\\Users\\SHAFIN\\Desktop\\testInput.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        
        List<String> queryList = new ArrayList<>();
        String fileName = "empty";

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
                    
                    fileName = dataSplit[0];
                    examYear = dataSplit[2];
                    semesterNumber = dataSplit[3];
                                                    
                    query = "CALL sustord.find_syllabus_id(@batch_id,@course_id,@syllabus_id);";
                    System.out.println(query);
                    queryList.add(query);
                    
                    query = "DELETE FROM sustord.course_registration WHERE syllabus_id_fk = @syllabus_id and attend_semester = "+semesterNumber+";";
                    System.out.println(query);
                    queryList.add(query);
                    
                } else {
                    String query = "call sustord.find_student_info_id('" + dataSplit[0] + "',@reg"+dataSplit[0]+");";
                    //System.out.println(query);
                    queryList.add(query);
                    query = "INSERT INTO `course_registration` (`student_info_id_fk`, `attend_year`, `attend_semester`, `syllabus_id_fk`, `approval`, `grade`) VALUES (@reg"+dataSplit[0]+","+examYear+","+semesterNumber+",@syllabus_id,1,  '"+dataSplit[2]+"');";
                    //System.out.println(query);
                    queryList.add(query);
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
                    writeData(queryList, fileName);
                } catch (IOException e) {
                }
            }
        }

        System.out.println(".......");
    }
    
    public static boolean writeData(List<String> dataList,String fileName){
        try {
            File file = new File("D:\\"+fileName+".txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream("D:\\"+fileName+".txt");
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8")) {
                //String singleString = "";
                System.out.println(dataList.size());
                for(String s:dataList){
                    outputStreamWriter.write(s);
                }
                
            }

            return true;

        } catch (IOException e) {
            return false;

        }
    }

}
