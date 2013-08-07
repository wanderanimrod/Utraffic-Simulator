package com.iKairos.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static void writeStringToFile(File file, String fileText) throws IOException {

        FileOutputStream fos = new FileOutputStream(file);

        fos.write(fileText.getBytes());

        try {
            fos.flush();
            fos.close();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
