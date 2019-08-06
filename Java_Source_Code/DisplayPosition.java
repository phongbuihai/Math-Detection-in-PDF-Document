/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
//https://pdfbox.apache.org/docs/2.0.5/javadocs/org/apache/pdfbox/examples/util/DrawPrintTextLocations.html
//https://stackoverflow.com/questions/46901648/how-to-identify-and-correct-bounding-box-issues


//https://stackoverflow.com/questions/46080131/text-coordinates-when-stripping-from-pdfbox
//https://www.tutorialspoint.com/pdfbox/pdfbox_adding_rectangles.htm
//https://stackoverflow.com/questions/38433534/pdfbox-inconsistent-co-ordinate-system-how-do-i-draw-a-rectangle
//https://www.programcreek.com/java-api-examples/?class=org.apache.pdfbox.pdmodel.edit.PDPageContentStream&method=addRect
//https://www.programcreek.com/java-api-examples/?code=phoenixctms/ctsms/ctsms-master/core/src/main/java/org/phoenixctms/ctsms/pdf/PDFUtil.java

import java.awt.BasicStroke;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
  
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import javax.imageio.ImageIO;
import org.apache.fontbox.util.BoundingBox;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDCIDFont;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.interactive.pagenavigation.PDThreadBead;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;

public class DisplayPosition  extends PDFTextStripper{
    
    
    private BufferedImage image;
    private AffineTransform flipAT;
    private AffineTransform rotateAT;
    private final String filename;
    static final int SCALE = 4;
    private Graphics2D g2d;
    private final PDDocument document;
    /**
     * Instantiate a new PDFTextStripper object.
     *
     * @param document
     * @param filename
     * @throws IOException If there is an error loading the properties.
     */
    public DisplayPosition(PDDocument document, String filename) throws IOException
    {
        this.document = document;
        this.filename = filename;
    }
    
    
     private void stripPage(int page) throws IOException
    {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        image = pdfRenderer.renderImage(page, SCALE);
        
        PDPage pdPage = document.getPage(page);
        PDRectangle cropBox = pdPage.getCropBox();
        // flip y-axis
        flipAT = new AffineTransform();
        flipAT.translate(0, pdPage.getBBox().getHeight());
        flipAT.scale(1, -1);
        // page may be rotated
        rotateAT = new AffineTransform();
        int rotation = pdPage.getRotation();
        if (rotation != 0)
        {
            PDRectangle mediaBox = pdPage.getMediaBox();
            switch (rotation)
            {
                case 90:
                    rotateAT.translate(mediaBox.getHeight(), 0);
                    break;
                case 270:
                    rotateAT.translate(0, mediaBox.getWidth());
                    break;
                case 180:
                    rotateAT.translate(mediaBox.getWidth(), mediaBox.getHeight());
                    break;
                default:
                    break;
            }
            rotateAT.rotate(Math.toRadians(rotation));
        }
        g2d = image.createGraphics();
        g2d.setStroke(new BasicStroke(0.1f));
        g2d.scale(SCALE, SCALE);
        setStartPage(page + 1);
        setEndPage(page + 1);
        Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
        writeText(document, dummy);
        
        // beads in green
        g2d.setStroke(new BasicStroke(0.4f));
        List<PDThreadBead> pageArticles = pdPage.getThreadBeads();
        for (PDThreadBead bead : pageArticles)
        {
            PDRectangle r = bead.getRectangle();
            GeneralPath p = r.transform(Matrix.getTranslateInstance(-cropBox.getLowerLeftX(), cropBox.getLowerLeftY()));
            Shape s = flipAT.createTransformedShape(p);
            s = rotateAT.createTransformedShape(s);
            g2d.setColor(Color.green);
            g2d.draw(s);
        }
        g2d.dispose();
        String imageFilename = filename;
        int pt = imageFilename.lastIndexOf('.');
        imageFilename = imageFilename.substring(0, pt) + "-marked-" + (page + 1) + ".png";
        ImageIO.write(image, "png", new File(imageFilename));
    }
    
    
    public static void main( String[] args ) throws IOException{
        /*PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);*/
       // PDFont font = PDType1Font.HELVETICA_BOLD;
        
        //Loading an existing document
        PDDocument document = null;
            try
            {
                System.out.println("Xu ly file PDF");
                document = PDDocument.load(new File("D:\\Java_Project\\PDF_TEXT\\Vehinh\\Vidu.pdf"));
                DrawPrintTextLocations stripper = new DrawPrintTextLocations(document, "D:\\Java_Project\\PDF_TEXT\\Vehinh\\Vidu.pdf");
                stripper.setSortByPosition(true);
                for (int page = 0; page < document.getNumberOfPages(); ++page)
                {
                   // stripper.stripPage(page);
                }
            }
            finally
            {
                if (document != null)
                {
                    document.close();
                }
            }
    }
    
     @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException
    {
        for (TextPosition text : textPositions)
        {
            System.out.println("String[" + text.getXDirAdj() + ","
                    + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
                    + text.getXScale() + " height=" + text.getHeightDir() + " space="
                    + text.getWidthOfSpace() + " width="
                    + text.getWidthDirAdj() + "]" + text.getUnicode());
            // in red:
            // show rectangles with the "height" (not a real height, but used for text extraction 
            // heuristics, it is 1/2 of the bounding box height and starts at y=0)
            Rectangle2D.Float rect = new Rectangle2D.Float(
                    text.getXDirAdj(),
                    (text.getYDirAdj() - text.getHeightDir()),
                    text.getWidthDirAdj(),
                    text.getHeightDir());
            g2d.setColor(Color.red);
            g2d.draw(rect);
            // in blue:
            // show rectangle with the real vertical bounds, based on the font bounding box y values
            // usually, the height is identical to what you see when marking text in Adobe Reader
            PDFont font = text.getFont();
            BoundingBox bbox = font.getBoundingBox();
            // advance width, bbox height (glyph space)
            float xadvance = font.getWidth(text.getCharacterCodes()[0]); // todo: should iterate all chars
            rect = new Rectangle2D.Float(0, bbox.getLowerLeftY(), xadvance, bbox.getHeight());
            
            // glyph space -> user space
            // note: text.getTextMatrix() is *not* the Text Matrix, it's the Text Rendering Matrix
            AffineTransform at = text.getTextMatrix().createAffineTransform();
            if (font instanceof PDType3Font)
            {
                // bbox and font matrix are unscaled
                at.concatenate(font.getFontMatrix().createAffineTransform());
            }
            else
            {
                // bbox and font matrix are already scaled to 1000
                at.scale(1/1000f, 1/1000f);
            }
            Shape s = at.createTransformedShape(rect);
            s = flipAT.createTransformedShape(s);
            s = rotateAT.createTransformedShape(s);
            g2d.setColor(Color.blue);
            g2d.draw(s);
        }
    }
    
    
    
    
}
