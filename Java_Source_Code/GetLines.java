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
 
/**
 * This is an example on how to extract text line by line from pdf document
 */
public class GetLines extends PDFTextStripper {
    
    static List<String> lines = new ArrayList<String>();
    static List<String> fonts = new ArrayList<String>();
    public GetLines () throws IOException {
    }
 
    /**
     * @throws IOException If there is an error parsing the document.
     */
    public static void main( String[] args ) throws IOException    {
        PDDocument document = null;
        String fileName = "D:\\Java_Project\\PDF_TEXT\\Vehinh\\10.1.1.193.1819_5.pdf";
        try {
            document = PDDocument.load( new File(fileName) );
            PDFTextStripper stripper = new GetLines ();
            stripper.setSortByPosition( true );
            stripper.setStartPage( 0 );
            stripper.setEndPage( document.getNumberOfPages() );
            
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);
             System.out.println("Write lines to file"); 
            // print lines
            FileWriter fw= new FileWriter("D:\\Java_Project\\PDF_TEXT\\Vehinh\\10.1.1.193.1819_5.txt");
            fw.flush();
            System.out.println("Write lines to file"); 
            int i=0;
            // print words
            for(String word:lines){
                System.out.println("Write lines to file");  
                System.out.println(word); 
                if(word.equals(" ")){
                   System.out.println("Space character: =============="); 
                }
                fw.write(word+"\n");
                fw.write("Font of word: "+fonts.get(i)+"\n");
                i++;
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
    protected void writeString(String str, List<TextPosition> textPositions) throws IOException {
        lines.add(str);
        String s="";
        for (TextPosition position : textPositions) {
           s =position.getFont().getName(); 
           fonts.add(s);
            // System.out.println("Font of word: "+s); 
        }
       //  System.out.println(str); 
        // you may process the line here itself, as and when it is obtained
    }
}
 