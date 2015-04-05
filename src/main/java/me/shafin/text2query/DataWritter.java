/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.shafin.text2query;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 *
 * @author SHAFIN
 */
public class DataWritter {

    private String FILE_NAME;

    public DataWritter(String fileName) {
        this.FILE_NAME = fileName;
    }

    public boolean writeData(List<String> dataList, String folderPath) {
        try {
            File file = new File(folderPath + getFILE_NAME() + ".txt");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(folderPath + getFILE_NAME() + ".txt");
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8")) {
                //String singleString = "";
                System.out.println(dataList.size());
                for (String s : dataList) {
                    outputStreamWriter.write(s);
                }

            }

            return true;

        } catch (IOException e) {
            return false;

        }
    }

    /**
     * @return the FILE_NAME
     */
    public String getFILE_NAME() {
        return FILE_NAME;
    }

    /**
     * @param FILE_NAME the FILE_NAME to set
     */
    public void setFILE_NAME(String FILE_NAME) {
        this.FILE_NAME = FILE_NAME;
    }
}
