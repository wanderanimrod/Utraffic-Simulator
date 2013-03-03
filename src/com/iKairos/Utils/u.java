package com.iKairos.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class u {	

	public static void print(String string) {
		System.out.print(string);
	}

	public static void println(String string) {
		System.out.print("\r\n" + string);
	}

	public static void printtb(String string) {
		System.out.print("\t" + string);
	}

	public static void printlntb(String string) {
		System.out.print("\r\n\t" + string);
	}

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

	public static class CSV {

		private String csvText = "";

		private String filePath;

		public CSV(String[] colHeadings, String fileName) {
			for (int i = 0; i < colHeadings.length; i++) {
				if (i != 0) {
					csvText += "," + colHeadings[i];
				}
				else {
					csvText += colHeadings[i];
				}
			}

			csvText += "\r\n";

			this.filePath = "E:\\Nimrod\\work\\Traffic\\Software\\Models and Testing\\" + fileName + ".csv";
		}

        //TODO Throw InvalidArgumentException if the value contains commas
		public void appendToRow(String cellValue) {
			csvText += "," + cellValue;
		}

		public void appendToNewLine(String cellValue) {
			csvText += "\r\n" + cellValue;
		}

		public boolean save() {			
			
			try {				
				writeStringToFile(new File(filePath), csvText);
				return true;
			}
			catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
		}
	}
}
