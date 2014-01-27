package rp3.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlDom {

	public static Document getDocument(InputStream is){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			
			db = dbf.newDocumentBuilder();
			Document d = db.parse(is);
			return d;
			
		} catch (ParserConfigurationException e) {			
			e.printStackTrace();
		} catch (SAXException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}			
		return null;
	}
}
