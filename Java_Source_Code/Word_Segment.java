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
 * This is an example on how to extract words from PDF document
 */
public class Word_Segment extends PDFTextStripper {
    
    static List<String> words = new ArrayList<String>();
 
    public Word_Segment() throws IOException {
    }
 
    /**
     * @throws IOException If there is an error parsing the document.
     */
    public static void main( String[] args ) throws IOException    {
        PDDocument document = null;
        String fileName = "C:\\Users\\Admin\\Desktop\\NII\\Work_6_3_2018\\10.1.1.1.2004_4.pdf"; // replace with your PDF file name
        try {
            document = PDDocument.load( new File(fileName) );
            PDFTextStripper stripper = new Word_Segment();
            stripper.setSortByPosition( true );
            stripper.setStartPage( 0 );
            stripper.setEndPage( document.getNumberOfPages() );
 
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            stripper.writeText(document, dummy);
            FileWriter fw= new FileWriter("D:\\NCKH\\NCS_2017\\NII\\NII_2018\\Work_6_3_2018\\Ketqua\\word_27_3.txt");
            fw.flush();
            // print words
            for(String word:words){
                System.out.println("Write words to file");  
                fw.write("Content: "+word+"------number of characters: "+word.length()+"\n");
                
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
        String[] wordsInStream = str.split(getWordSeparator());
        
        if(wordsInStream!=null){
            for(String word :wordsInStream){
                
                words.add(word);
            }
        }
    }
}