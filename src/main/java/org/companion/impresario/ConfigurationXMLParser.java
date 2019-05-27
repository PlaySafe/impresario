package org.companion.impresario;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

class ConfigurationXMLParser {

    Document parseFrom(InputStream xmlFile) throws IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringComments(true);
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(xmlFile);
            document.normalizeDocument();
            return document;
        }
        catch (ParserConfigurationException | SAXException e) {
            throw new IllegalArgumentException("Invalid XML Format", e);
        }
    }
}
