/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.shafin.text2query;

/**
 *
 * @author SHAFIN
 */
public class GradeFormatter {
    
    public static String marksToGrade(double marks){
        String gradeLetter ;

        if (marks>= 80) {
            gradeLetter = "A+";
        } else if (marks >= 75 && marks < 80) {
            gradeLetter = "A";
        } else if (marks >= 70 && marks < 75) {
            gradeLetter = "A-";
        } else if (marks >= 65 && marks < 70) {
            gradeLetter = "B+";
        } else if (marks >= 60 && marks < 65) {
            gradeLetter = "B";
        } else if (marks >= 55 && marks < 60) {
            gradeLetter = "B-";
        } else if (marks >= 50 && marks < 55) {
            gradeLetter = "C+";
        } else if (marks >= 45 && marks < 50) {
            gradeLetter = "C";
        } else if (marks >= 40 && marks < 45) {
            gradeLetter = "C-";
        } else if (marks >= 0 && marks < 40) {
            gradeLetter = "F";
        } else {
            gradeLetter = "n";
        }

        return gradeLetter;
    }
}
