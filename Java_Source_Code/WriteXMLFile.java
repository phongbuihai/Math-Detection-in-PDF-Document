/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Admin
 */
import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteXMLFile {
    String filename;
    String charactervalue;
    String characterfont;
    String characterx;
    String charactery;
    String characterh;
    String characterw;
    String charactertype;
    int id;
    public WriteXMLFile(String s){
        this.filename=s;
    }
    
    /*System.out.println("Write to xml file");
            //Ghi ket qua tung file PDF ra file xml
            String xmlname="D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Ketqua\\"+fileName+".xml";
            WriteXMLFile writeXMLfile= new WriteXMLFile(xmlname);
            System.out.println("Write height to xml file: "+xmlname);
            try{
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root elements
		Document docxml = docBuilder.newDocument();
		Element rootElement = docxml.createElement("characters");
		docxml.appendChild(rootElement);
                 for(int i=0;i<numberofCharacter;i++){
                /*System.out.println("Write value to xml file: "+charactervalue.get(i));
                 System.out.println("Write font to xml file: "+characterfont.get(i));
                 System.out.println("Write x to xml file: "+characterx.get(i));
                  System.out.println("Write type to xml file: "+charactertype.get(i));
                  System.out.println("Write y to xml file: "+charactery.get(i));
                  System.out.println("Write width to xml file: "+characterw.get(i));
                  System.out.println("Write height to xml file: "+characterh.get(i));*/
               // writeXMLfile.setValues(charactervalue.get(i), characterfont.get(i), characterx.get(i), charactery.get(i), characterh.get(i), characterw.get(i), charactertype.get(i), i);
             //   writeXMLfile.writeXML();
                
                  
		
                
		// staff elements
		// set attribute to staff element
                //for(int id=1;id<10;id++){
              /*  Element character = docxml .createElement("character");
		rootElement.appendChild(character);
		Attr attr = docxml .createAttribute("id");
		attr.setValue(Integer.toString(i));
		character.setAttributeNode(attr);

		// shorten way
		// staff.setAttribute("id", "1");

		// value of character elements
		Element value = docxml.createElement("value");
		value.appendChild(docxml.createTextNode(charactervalue.get(i)));
		character.appendChild(value);

		// fontname of character elements
		Element fontname = docxml.createElement("fontname");
		fontname.appendChild(docxml.createTextNode(characterfont.get(i)));
		character.appendChild(fontname);

		// x position of character elements
		Element x = docxml.createElement("x");
		x.appendChild(docxml.createTextNode(characterx.get(i)));
		character.appendChild(x);

		//y position of character elements
		Element y= docxml.createElement("y");
		y.appendChild(docxml.createTextNode(charactery.get(i)));
		character.appendChild(y);
                
                // height elements
		Element h= docxml.createElement("h");
		h.appendChild(docxml.createTextNode(characterh.get(i)));
		character.appendChild(h);
                
                // width elements
		Element w= docxml.createElement("w");
		w.appendChild(docxml.createTextNode(characterw.get(i)));
		character.appendChild(w);
                
                // type of character elements
		Element type= docxml.createElement("type");
		type.appendChild(docxml.createTextNode(charactertype.get(i)));
		character.appendChild(type);
                
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(docxml);
		StreamResult result = new StreamResult(new File(xmlname));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		//System.out.println("File saved!");

	  } 
          
                  
      
            }
            catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } 
            catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
            */
    
    
   
    
    public void setValues(String value, String font, String x, String y,String h, String w, String type, int i){
        
        this.charactervalue=value;
        this.characterfont=font;
        this.characterx=x;
        this.charactery=y;
        this.characterh=h;
        this.characterw=w;
        this.charactertype=type;
        this.id=i;
        
    }
    public void writeXML(){

	  try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("characters");
		doc.appendChild(rootElement);

		// staff elements
		// set attribute to staff element
                //for(int id=1;id<10;id++){
                Element character = doc.createElement("character");
		rootElement.appendChild(character);
		Attr attr = doc.createAttribute("id");
		attr.setValue(Integer.toString(id));
		character.setAttributeNode(attr);

		// shorten way
		// staff.setAttribute("id", "1");

		// value of character elements
		Element value = doc.createElement("value");
		value.appendChild(doc.createTextNode(this.charactervalue));
		character.appendChild(value);

		// fontname of character elements
		Element fontname = doc.createElement("fontname");
		fontname.appendChild(doc.createTextNode(this.characterfont));
		character.appendChild(fontname);

		// x position of character elements
		Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode(this.characterx));
		character.appendChild(x);

		//y position of character elements
		Element y= doc.createElement("y");
		y.appendChild(doc.createTextNode(this.charactery));
		character.appendChild(y);
                
                // height elements
		Element h= doc.createElement("h");
		h.appendChild(doc.createTextNode(this.characterh));
		character.appendChild(h);
                
                // width elements
		Element w= doc.createElement("w");
		w.appendChild(doc.createTextNode(this.characterw));
		character.appendChild(w);
                
                // type of character elements
		Element type= doc.createElement("type");
		type.appendChild(doc.createTextNode(this.charactertype));
		character.appendChild(type);
                
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(this.filename));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		//System.out.println("File saved!");

	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
        
    }
	public static void main(String argv[]) {

	  try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("characters");
		doc.appendChild(rootElement);

		// staff elements
		
		// set attribute to staff element
                for(int id=1;id<10;id++){
                Element character = doc.createElement("character");
		rootElement.appendChild(character);
		Attr attr = doc.createAttribute("id");
		attr.setValue(Integer.toString(id));
		character.setAttributeNode(attr);

		// shorten way
		// staff.setAttribute("id", "1");

		// value of character elements
		Element value = doc.createElement("value");
		value.appendChild(doc.createTextNode("e"));
		character.appendChild(value);

		// fontname of character elements
		Element fontname = doc.createElement("fontname");
		fontname.appendChild(doc.createTextNode("DADPHN+TimesNewRoman,Italic "));
		character.appendChild(fontname);

		// x position of character elements
		Element x = doc.createElement("x");
		x.appendChild(doc.createTextNode("104.29694 "));
		character.appendChild(x);

		//y position of character elements
		Element y= doc.createElement("y");
		y.appendChild(doc.createTextNode("267.7638  "));
		character.appendChild(y);
                
                // height elements
		Element h= doc.createElement("h");
		h.appendChild(doc.createTextNode("6.087278 "));
		character.appendChild(h);
                
                // width elements
		Element w= doc.createElement("w");
		w.appendChild(doc.createTextNode("3.030899"));
		character.appendChild(w);
                
                // type of character elements
		Element type= doc.createElement("type");
		type.appendChild(doc.createTextNode("variable"));
		character.appendChild(type);
                }
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("D:\\Java_Project\\PDF_TEXT\\Proceeding_ACL\\Ketqua\\fileloop.xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		System.out.println("File saved!");

	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
}