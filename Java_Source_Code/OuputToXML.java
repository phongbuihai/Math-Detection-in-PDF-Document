/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.pdfbox.pdmodel.font.PDCIDFont;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Admin
 */
public class OuputToXML {
    //Cac thuoc tinh de ghi ra file txt
    public static  List<String> fonts = new ArrayList<String>();
    public static  double Variable_Count=0;
    public static  double Variable_Punction=0;
    public static  List<String> fontnames = new ArrayList<String>();
    public static  List<String> Characters = new ArrayList<String>();
    //Cac thuoc tinh de ghi ra file xml
    public static  List<String> charactervalue= new ArrayList<String>();
    public static  List<String> characterfont = new ArrayList<String>();
    public static  List<String> characterx = new ArrayList<String>();
    public static  List<String> charactery = new ArrayList<String>();
    public static  List<String> characterh = new ArrayList<String>();
    public static  List<String> characterw = new ArrayList<String>();
    public static  List<String> charactertype = new ArrayList<String>();
    
   // public static String outputfolder="D:\\NCKH\\NCS_2017\\NII\\NII_2018\\Work_6_3_2018\\Ketqua\\";
    
    //Kiem tra bien, kem theo dau , va dau .
    //Kiem tra tu viet tat
    //https://github.com/ncbi-nlp/Ab3P
    
    //Kiem tra ky tu la chu cai
    
    static public boolean isCharacter(String s){
         char ch=s.charAt(0);
         if((ch>='a' && ch<='z') || (ch>='A' && ch<='Z'))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //Kiem tra chu nghieng
    static public boolean  isItalic(String s){
        if(s.contains("Italic")||s.contains("Ital")||s.contains("CMMI")){
        return true;
        }
        else
            return false;
    }
    //Kiem tra dau bang cua truong hop viet tat
    static public boolean  isAbbreviation(String s1,String s2,String s3, String s4, String s5, String s6){
        if(isItalic(s1)&&s2.equals(" ")&&s3.equals("=")&&s4.equals(" ")&&isItalic(s5)&&isItalic(s6)){
        return true;
        }
        else
            return false;
    }
    
    //Kiem tra ky hieu toan bang latex font
    static public boolean  isLatexSymbol(String s){
        if(s.contains("NDYCBJ+CMMI")){
        return true;
        }
        else
            return false;
    }
    //Ham kiem tra ca tu la nghieng
    static public boolean  isWordItalic(String word1,String word2 ){
        if(word1.contains("Ital")&&word2.contains("Ital")){
        return true;
        }
        else
            return false;
    }
    static public boolean  isAccent(String word ){
        if(word.equals("à")||word.equals("À")||word.equals("á")||word.equals("Á")||word.equals("ở")||word.equals("ờ")||word.equals("í")||word.equals("ơ")||word.equals("Â")||word.equals("Ă")||word.equals("â")){
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
   //Kiem tra dau cham, phay
   static public boolean isContainComma(String before, String current, String after){
       if(before.equals(" ")&&isItalic(current)&&(after.equals(",")||after.equals(".")||after.equals(":"))){
       return true;
       }
       else
       return false;
   }
   //Kiem tra tu viet tat co dau bang
   
   static public boolean isAbbreation2(String current, String s1, String s2){
       if(isCharacter(current)&&isItalic(current)&&s1.equals("y")&&s2.equals("b")){
       return true;
       }
       else
       return false;
   }
  
   //Kiem tra 1 dong la ky tu nghieng
   static public boolean  isItalicLine(String current, String middle, String after){
        if(isItalic(current)&&isItalic(after)&&middle.equals(" ")){
        return true;
        }
        else
            return false;
    }
    
   //Copy files
   public static void copy(File src, File dst) throws IOException {
    InputStream in = new FileInputStream(src);
    try {
        OutputStream out = new FileOutputStream(dst);
        try {
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            out.close();
        }
    } finally {
        in.close();
    }
}
   
   
   
    public static void main( String[] args ) throws IOException{
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime begin_time = LocalDateTime.now();  
        System.out.println("System begins at: -----------------"+dtf.format(begin_time)); 
        ReadFolder readfolder= new ReadFolder("D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Ketqua\\Validate_12_5\\PDF\\");
        String inputfolder="D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Ketqua\\Validate_12_5\\PDF\\";
        List<String> folder= readfolder.getFileNames();
        //Doc tung file trong thu muc
        for(String fileName:folder){
        int variabl_per_page=0;
        fonts.clear();
        fontnames.clear();
        Characters.clear();
       // String fileName = "C:\\Users\\Admin\\Desktop\\NII\\Work_6_3_2018\\10.1.1.1.2004_4.pdf";
        String inputfileName=inputfolder+fileName+".pdf";
        //System.out.println(inputfileName);
        PDDocument doc= PDDocument.load(new File(inputfileName));  
        
        //Khoi tao doi tuong
        PDFTextStripper stripper = new PDFTextStripper() {  
         String prevBaseFont = "";  
       
       protected void writeString(String text, List<TextPosition> textPositions) throws IOException  
       {  
         StringBuilder builder = new StringBuilder(); 
         String font="";
         String bb="";
         for (TextPosition position : textPositions)  
             //Kiem tra font khong dung cho toan hoc  
         { 
             
             if(position.getUnicode()!=null&&position.getFont().getName()!=null){  
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
                 
            if(baseFont.contains("NDYCBJ+CMMI")&&isCharacter(character)){
                 font="Font CMMI symbols: "+character+"----font: "+baseFont+"\n";
                 System.out.println("-----------Day la font Marmot"+font);
                 System.out.println(" Day la font CMMI va la variable");
             }  
            /*if(baseFont.contains("Italic")) {
                System.out.println(" Base font la: "+baseFont);
            }*/
             bb=" [(X=" + position.getXDirAdj() + ",Y=" + position.getYDirAdj() + ") height=" + position.getHeightDir() + " width=" +position.getWidthDirAdj() + "]";
             font="Character: "+character+"----font: "+baseFont+" Positions of character: "+bb+"\n";
             
             //Lay du lieu de ghi ra file xml   
             }
             }
            //Lay du lieu de ghi ra file txt
             fonts.add(font);
             fontnames.add(baseFont);
             Characters.add(character);
             
           builder.append(position.getUnicode());  
         }  
         }
         
         writeString(builder.toString());  
       }  
     };  
         
     String content=stripper.getText(doc);  
     doc.close();  
     String pdfLinesWithFont[]= content.split("\\r?\\n"); 
     String outputfolder="D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Ketqua\\Validate_12_5\\Ketqua_2_6\\";
     outputfolder=outputfolder+fileName+".txt";
     String variable_result=outputfolder+"_variable_result.txt";
     //FileWriter fw= new FileWriter("D:\\NCKH\\NCS_2017\\NII\\NII_2018\\Work_6_3_2018\\Ketqua\\font_29_3.txt");
     FileWriter fw= new FileWriter(outputfolder);
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
        //Ap dung voi ACL anatholy dataset  
            /* if(fontnames.get(currentPosition).contains("Italic")){
             System.out.println(fontnames.get(currentPosition));
             }*/
         if(currentPosition>0&&currentPosition<fonts.size()-2){
             //Truong hop bien don, nghieng
             
         if(isCharacter(current)&&isItalic(fontnames.get(currentPosition))&&begin.equals(" ")&&end.equals(" ")&&!isWordItalic(current, end)&&!isItalic(fontnames.get(currentPosition+2))){
             if(!isAccent(current)){
             s=s+"----Determination Result: Variable"+"\n";
            System.out.println(s);
          // System.out.println(fontnames.get(currentPosition));
           //  System.out.println(s);
             charactertype.add("variable");
             Variable_Count++;
             variabl_per_page++;
             }
         }
         //Truong hop bien co chua dau cham hoac dau phay
         if(isContainComma(begin, fontnames.get(currentPosition), end)&&isCharacter(current)&&!isItalic(fontnames.get(currentPosition+1))){
             if(!isAccent(current)){
             s=s+"----Determination Result: Variable with comma, dot or other punctuation character"+"\n";
            System.out.println(s);
          // System.out.println(fontnames.get(currentPosition));
           //  System.out.println(s);
             charactertype.add("variable");
             Variable_Count++;
             Variable_Punction++;
             variabl_per_page++;
             }
         }
         
         }
             
         if((currentPosition>=fonts.size()-2)&&currentPosition<fonts.size()-1){
           if(isCharacter(current)&&isItalic(fontnames.get(currentPosition)) &&begin.equals(" ")&&end.equals(" ")&&!isWordItalic(current, end)){
             s=s+"----result: Variable"+"\n";
            System.out.println(s);
          // System.out.println(fontnames.get(currentPosition));
           //  System.out.println(s);
             charactertype.add("variable");
             Variable_Count++;
             variabl_per_page++;
         }  
             
         }
         
         }
         //Truong hop bien dung dau 1 dong
         if(currentPosition==0){
             String next=Characters.get(currentPosition+1);
             String current=Characters.get(currentPosition);
         if(isCharacter(current)&&isItalic(fontnames.get(currentPosition))&&next.equals(" ")){
             s=s+"----result: "
                     + ""+"\n";
            //  System.out.println(Characters.get(currentPosition));
           //  System.out.println(s);
             charactertype.add("variable");
             Variable_Count++;
             variabl_per_page++;
         }
         
         //Ap dung voi Marmot dataset
         if(isCharacter(current)&&isLatexSymbol(fontnames.get(currentPosition))){
             s=s+"----result: Variable"+"\n";
            //  System.out.println(Characters.get(currentPosition));
           //  System.out.println(s);
             charactertype.add("variable");
             Variable_Count++;
             variabl_per_page++;
         }
        
         }
         
         //Kiem tra variable: Truong hop bien dung cuoi dong
         if(currentPosition==(fonts.size()-1)){
             String before=Characters.get(currentPosition-1);
             String current=Characters.get(currentPosition);
         if(isCharacter(current)&&isItalic(fontnames.get(currentPosition))&&before.equals(" ")){
             s=s+"----result: Variable"+"\n";
            //  System.out.println(Characters.get(currentPosition));
           //  System.out.println(s);
             charactertype.add("variable");
             Variable_Count++;
         }
         if(isCharacter(current)&&isLatexSymbol(fontnames.get(currentPosition))){
             s=s+"----result: Variable"+"\n";
            //  System.out.println(Characters.get(currentPosition));
           //  System.out.println(s);
             charactertype.add("variable");
             Variable_Count++;
             variabl_per_page++;
         }
         }
         charactertype.add("normal");
      //  Ghi ra file txt
         
         fw.write(s);
         //Ghi ra file xml
         
         currentPosition++;
         
     }
     if(variabl_per_page>0){
         FileWriter fw_variable_result= new FileWriter(variable_result);
        //fw.flush();
        fw_variable_result.flush();
        fw_variable_result.write("So luong bien trong trang: "+Double.toString(variabl_per_page));
         System.out.println("File co chua bien la: "+variable_result);
         fw_variable_result.close();
         copy(new File(inputfolder+fileName+".pdf"), new File(outputfolder+fileName+".pdf"));
     }
     fw.close();
    }
        String outputfolder="D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Ketqua\\Validate_12_5\\Ketqua_2_6\\";
        FileWriter fw_ketqua= new FileWriter(outputfolder+"Tonghop_ketqua.txt");
        fw_ketqua.flush();
        fw_ketqua.write("So luong bien co kem theo dau cau trong tai lieu la: "+Double.toString(Variable_Punction)+"\n");
        fw_ketqua.write("So luong bien trong tai lieu la: "+Double.toString(Variable_Count));
        fw_ketqua.close();
        LocalDateTime end_time = LocalDateTime.now();  
        System.out.println("System is terminated at: -----------------"+dtf.format(end_time));  
        System.out.println("So luong bien co kem theo dau cau trong tai lieu la: "+Double.toString(Variable_Punction)+"\n");
        System.out.println("So luong bien trong tai lieu la:"+ Variable_Count);
    }
    
}
