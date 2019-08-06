

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
public class ReadFolder {
    public static List<String> FileName = new ArrayList<String>();
    private String folder;
    public ReadFolder(String folder) {
        this.folder=folder;
    }
    public List<String> getFileNames(){
        File folder = new File(this.folder);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
        if (file.isFile()) {
        System.out.println(file.getPath());
        String path=file.getPath();
        String name=file.getName();
        //Get name without extension
        int pos = name.lastIndexOf(".");
        if (pos > 0 && pos < (name.length() - 1)) { // If '.' is not the first or last character.
            name = name.substring(0, pos);
         }
        System.out.println(name);
        FileName.add(name);
        
        }
        }
        return FileName;
    }
    
    public static void main( String[] args ) throws IOException    {
        File folder = new File("D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Split");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
        if (file.isFile()) {
        System.out.println(file.getPath());
        String path=file.getPath();
        String name=file.getName();
        //Get name without extension
        int pos = name.lastIndexOf(".");
        if (pos > 0 && pos < (name.length() - 1)) { // If '.' is not the first or last character.
            name = name.substring(0, pos);
         }
        System.out.println(name);
        FileName.add(name);
        
        }
        }
    }
 
    
    
}
