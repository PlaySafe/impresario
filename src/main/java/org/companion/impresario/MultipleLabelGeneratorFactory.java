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

/**
 * <p>
 * Compiles multiple configuration at once, in case there are more than 1 configuration files,
 * all of their groups will be merged altogether. Accept only XML configuration.
 * </p>
 *
 * <p>
 * The compilation of configuration requires both {@link MetaData} as the references, and
 * XML configuration file as a configuration that correspond to the references
 * </p>
 *
 * <p>
 * The compilation result of multiple configuration from multiple files is the same as
 * the merging of multiple file to a single one. For example,
 * </p>
 *
 * <ul>
 * <li>File-A has group "ABC", "DEF"</li>
 * <li>File-B has group "XYZ"</li>
 * </ul>
 * The group of result will have "ABC", "DEF", "XYZ" without order guarantee.
 */
public class MultipleLabelGeneratorFactory implements LabelGeneratorFactory {

    private final XPathExpression xPathLabelSourceUri;
    private final LabelGeneratorFactory labelGeneratorFactory;

    public MultipleLabelGeneratorFactory(MetaData metaData) {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathLabelSourceUri = xPathFactory.newXPath().compile("/Files/Label/@src");
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
        labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);
    }

    @Override
    public Map<String, LabelGenerator> compile(File file) throws IOException {
        InputStream stream = new FileInputStream(file);
        return compile(stream);
    }

    @Override
    public Map<String, LabelGenerator> compile(InputStream stream) throws IOException {
        try {
            Document document = new DocumentParser().parseFrom(stream);
            NodeList srcFiles = (NodeList) xPathLabelSourceUri.evaluate(document, XPathConstants.NODESET);
            Map<String, LabelGenerator> allConfig = new HashMap<>();
            for (int i = 0; i < srcFiles.getLength(); i++) {
                String eachSrc = srcFiles.item(i).getTextContent();
                Map<String, LabelGenerator> config = labelGeneratorFactory.compile(new File(eachSrc));
                allConfig.putAll(config);
            }
            return Collections.unmodifiableMap(new HashMap<>(allConfig));
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
