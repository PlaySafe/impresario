package org.companion.impresario;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MultipleEquationFactory implements EquationFactory {

    private final XPathExpression xPathEquationSourceUri;
    private final EquationFactory equationFactory;

    public MultipleEquationFactory(MetaData metaData) {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathEquationSourceUri = xPathFactory.newXPath().compile("/Files/Equation/@src");
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
        this.equationFactory = new SingleEquationFactory(metaData);
    }

    @Override
    public Map<String, Equation> compile(File file) throws IOException {
        InputStream stream = new FileInputStream(file);
        return compile(stream);
    }

    @Override
    public Map<String, Equation> compile(InputStream stream) throws IOException {
        try {
            Document document = new DocumentParser().parseFrom(stream);
            NodeList srcFiles = (NodeList) xPathEquationSourceUri.evaluate(document, XPathConstants.NODESET);
            Map<String, Equation> equations = new HashMap<>();
            for (int i = 0; i < srcFiles.getLength(); i++) {
                String eachSrc = srcFiles.item(i).getTextContent();
                Map<String, Equation> config = equationFactory.compile(new File(eachSrc));
                equations.putAll(config);
            }
            return Collections.unmodifiableMap(new HashMap<>(equations));
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
