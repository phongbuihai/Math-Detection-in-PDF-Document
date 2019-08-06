import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
 
/**
 * This is an example on how to get the x/y coordinates and size of each character in PDF
 */
public class GetFont extends PDFTextStripper {
  public static  List<String> fonts = new ArrayList<String>();
    public GetFont() throws IOException {
    }
 
    /**
     * @throws IOException If there is an error parsing the document.
     */
    public static void main( String[] args ) throws IOException    {
        PDDocument document = null;
        String fileName = "C:\\Users\\Admin\\Desktop\\NII\\Work_6_3_2018\\10.1.1.1.2004_4.pdf";
        try {
            document = PDDocument.load( new File(fileName) );
            PDFTextStripper stripper = new GetFont();
            stripper.setSortByPosition( true );
            stripper.setStartPage( 0 );
            stripper.setEndPage( document.getNumberOfPages() );
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);
            FileWriter fw= new FileWriter("D:\\NCKH\\NCS_2017\\NII\\NII_2018\\Work_6_3_2018\\Ketqua\\font_27_3.txt");
            fw.flush();
            for(String text: fonts){
             fw.write("Font name: "+text+"\n");
            }
             fw.close();
        }
        finally {
            if( document != null ) {
                document.close();
            }
        }
    }
 
    /**
     * Override the default functionality of PDFTextStripper.writeString()
     */
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        
            // print words
            /*for(String word:words){
                System.out.println("Write words to file");   
                fw.write(word+"\n");
            }*/ 
        for (TextPosition text : textPositions) {
            fonts.add(text.getFont().getName());
            System.out.println("Font name: "+text.getFont().getName());
            System.out.println(text.getUnicode()+ " [(X=" + text.getXDirAdj() + ",Y=" +
                    text.getYDirAdj() + ") height=" + text.getHeightDir() + " width=" +
                    text.getWidthDirAdj() + "]");
           
            
        }
        
    }
}