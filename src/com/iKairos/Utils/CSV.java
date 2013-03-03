package com.iKairos.Utils;

import java.io.File;
import java.io.IOException;

public class CSV {

    private String csvText = "";
    private String filePath;

    public CSV(String[] colHeadings, String fileName) {

        for (int i = 0; i < colHeadings.length; i++) {
            if (i != 0)
                csvText += "," + colHeadings[i];
            else
                csvText += colHeadings[i];
        }

        csvText += "\r\n";
        this.filePath = "E:\\Nimrod\\work\\Traffic\\Software\\Models and Testing\\" + fileName + ".csv";
    }

    public void appendToRow(String cellValue) throws IllegalArgumentException {

        if(cellValue.contains(","))
            throw new IllegalArgumentException("Cell values cannot contain commas");

        csvText += "," + cellValue;
    }

    public void appendToNewLine(String cellValue) {
        csvText += "\r\n" + cellValue;
    }

    public boolean save() {
        try {
            FileUtils.writeStringToFile(new File(filePath), csvText);
            return true;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
