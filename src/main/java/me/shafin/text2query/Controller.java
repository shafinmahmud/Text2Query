/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.shafin.text2query;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author SHAFIN
 */
public class Controller {

    public static void main(String[] args) {

        List<String> queryList = new ArrayList<>();

        Scanner in = new Scanner(System.in);
        //System.out.println("Enter the filepath : ");
        /// String csvFilePath = in.nextLine();
        String csvFilePath = "C:\\Users\\SHAFIN\\Desktop\\markSheet.csv";

        boolean choiceIsNotPicked = true;

        while (choiceIsNotPicked) {
            System.out.println("what type of converstion ? \n1.Mark sheet or \n2.Course Grade sheet ?");
            int jobType = in.nextInt();

            switch (jobType) {
                case 1: {
                    choiceIsNotPicked = false;
                    queryList = Converter.convertMarkSheet(csvFilePath);
                    System.out.println("number of query: "+queryList.size());

                }
                case 2: {
                    choiceIsNotPicked = false;
                    //queryList = Converter.convertCourseGradeSheet(csvFilePath);
                    
                }
            }
        }

//        System.out.println("Enter the output file path : ");
//        String outputPath = in.nextLine();
        String outputPath = "D:\\";

        Scanner nameIn = new Scanner(System.in);
        System.out.println("File name ? ");
        String fileName = nameIn.nextLine();

        if (!queryList.isEmpty()) {
            DataWritter writter = new DataWritter(fileName);
            writter.writeData(queryList, outputPath);
        }

    }

}
