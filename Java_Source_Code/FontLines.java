/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDCIDFont;
import org.apache.pdfbox.pdmodel.font.PDFont;

/**
 *
 * @author Admin
 */
public class FontLines {
    public static  List<String> fonts = new ArrayList<String>();
    public static  List<String> fontnames = new ArrayList<String>();
    public static  List<String> Characters = new ArrayList<String>();
   // public static String outputfolder="D:\\NCKH\\NCS_2017\\NII\\NII_2018\\Work_6_3_2018\\Ketqua\\";
    //Kiem tra ky tu la chu cai
    static public boolean isCharacter(String s){
        if(Character.isLetter(s.charAt(0))){
        return true;
        }
        else
            return false;
    }
    //Kiem tra chu nghieng
    static public boolean  isItalic(String s){
        if(s.contains("Italic")){
        return true;
        }
        else
            return false;
    }
    //Ham kiem tra ca tu la nghieng
    static public boolean  isWordItalic(String word1,String word2 ){
        if(word1.contains("Italic")&&word2.contains("Italic")){
        return true;
        }
        else
            return false;
    }
    //Ham kiem tra tu gom 1 ky tu
   static public boolean  isSingleWord(String before,String s, String after){
        if(before.equals(" ")&&after.equals(" ")){
        return true;
        }
        else
            return false;
    }
    
    public static void main( String[] args ) throws IOException{
        //PDDocument document = null;
        ReadFolder readfolder= new ReadFolder("D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Split");
        String inputfolder="D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Split\\";
        List<String> folder= readfolder.getFileNames();
        for(String fileName:folder){
        fonts.clear();
       // String fileName = "C:\\Users\\Admin\\Desktop\\NII\\Work_6_3_2018\\10.1.1.1.2004_4.pdf";
        String inputfileName=inputfolder+fileName+".pdf";
        System.out.println(inputfileName);
        PDDocument doc= PDDocument.load(new File(inputfileName));  
        PDFTextStripper stripper = new PDFTextStripper() {  
         String prevBaseFont = "";  
      
       protected void writeString(String text, List<TextPosition> textPositions) throws IOException  
       {  
         StringBuilder builder = new StringBuilder(); 
         String font="";
         String bb="";
         for (TextPosition position : textPositions)  
             
         { if(position.getUnicode()!=null&&position.getFont().getName()!=null){  
             String baseFont = position.getFont().getName(); 
             String character=position.getUnicode();
             if(baseFont.contains("CambriaMath")||baseFont.contains("DAEAGA")){
                 System.out.println("Loi font ky hieu toan");
             }
            if(baseFont.contains("GGMKOH")){
                 font="Special highlight symbols: "+character+"----font: "+baseFont+"\n";
             }
            else{
             if(character.equals(" ")){
                   //System.out.println("Space between two words");  
                    font="Space character: "+"----font: "+baseFont+"\n";
                }
             else{
                 
                /* System.out.println(text.getUnicode()+ " [(X=" + text.getXDirAdj() + ",Y=" +
                    text.getYDirAdj() + ") height=" + text.getHeightDir() + " width=" +
                    text.getWidthDirAdj() + "]");
                 +position.getTextMatrix().getTranslateX()+"Positions y: "+position.getTextMatrix().getTranslateY(
                 */
                 
             bb=" [(X=" + position.getXDirAdj() + ",Y=" + position.getYDirAdj() + ") height=" + position.getHeightDir() + " width=" +position.getWidthDirAdj() + "]";
             font="Character: "+character+"----font: "+baseFont+" Positions of character: "+bb+"\n";
                 
             }
             }
             fonts.add(font);
             fontnames.add(baseFont);
             Characters.add(character);
            // System.out.println("Character: "+character+"----font: "+baseFont);
          /* if (baseFont != null && !baseFont.equals(prevBaseFont))  
           {  
             builder.append('[').append(baseFont).append(']');  
             prevBaseFont = baseFont;  
           }  */
           builder.append(position.getUnicode());  
         }  
         }
         
         writeString(builder.toString());  
       }  
     };  
         
     String content=stripper.getText(doc);  
     doc.close();  
     String pdfLinesWithFont[]= content.split("\\r?\\n"); 
     /*if(isCharacter("N")&&isItalic("TimesNewRomanPS-ItalicMT")){
         System.out.println("This is a Variable");
     }*/
    
    /* for(String s:pdfLinesWithFont){
      System.out.println(s);
     }*/
     //Output characters and font to file
     String outputfolder="D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Ketqua\\";
     outputfolder=outputfolder+fileName+".txt";
     //FileWriter fw= new FileWriter("D:\\NCKH\\NCS_2017\\NII\\NII_2018\\Work_6_3_2018\\Ketqua\\font_29_3.txt");
     FileWriter fw= new FileWriter(outputfolder);
     fw.flush();
    // System.out.println("Write Output font to file: "+outputfolder);
     System.out.println("Write Output font to file: "+fileName+".txt");
     int currentPosition =0;
     for(String s:fonts){
         //Kiem tra Variabl, Truong hop bien dung o giua dong
         if(currentPosition>0&&currentPosition<fonts.size()-1){
             String begin=Characters.get(currentPosition-1);
             String end=Characters.get(currentPosition+1);
             String current=Characters.get(currentPosition);
         /*Conditions of variable: 
            (1) Single character
            (2) Italic
            (3) Not an italic word
             */
             
         if(isCharacter(current)&&isItalic(fontnames.get(currentPosition))&&isSingleWord(begin,current, end)&&!isWordItalic(fontnames.get(currentPosition), fontnames.get(currentPosition+1))){
             s=s+"----result: Variable"+"\n";
             System.out.println(Characters.get(currentPosition));
             System.out.println(s);
             
             //Ve bounding box
            

      //Drawing a rectangle
               
         }
         }
         //Truong hop bien dung dau 1 dong
         if(currentPosition==0){
             String next=Characters.get(currentPosition+1);
             String current=Characters.get(currentPosition);
         if(isCharacter(current)&&isItalic(fontnames.get(currentPosition))&&next.equals(" ")){
             s=s+"----result: Variable"+"\n";
              System.out.println(Characters.get(currentPosition));
             System.out.println(s);
         }
         }
         
         //Kiem tra variable: Truong hop bien dung cuoi dong
         if(currentPosition==(fonts.size()-1)){
             String before=Characters.get(currentPosition-1);
             String current=Characters.get(currentPosition);
         if(isCharacter(current)&&isItalic(fontnames.get(currentPosition))&&before.equals(" ")){
             s=s+"----result: Variable"+"\n";
              System.out.println(Characters.get(currentPosition));
             System.out.println(s);
         }
         }
      //   System.out.println("Variable detection is done");
         fw.write(s);
         currentPosition++;
          
     }
     fw.close();
    }
    
    }
    
}
