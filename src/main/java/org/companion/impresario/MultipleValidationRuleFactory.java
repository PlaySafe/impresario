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
 * <li>File-A has group "QWERTY", "ASDF"</li>
 * <li>File-B has group "AWDX"</li>
 * </ul>
 * The group of result will have "QWERTY", "ASDF", "AWDX" without order guarantee.
 */
public class MultipleValidationRuleFactory implements ValidationRuleFactory {

    private final XPathExpression xPathValidationRuleSourceUri;
    private final ValidationRuleFactory validationRuleFactory;

    public MultipleValidationRuleFactory(MetaData metaData) {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        try {
            xPathValidationRuleSourceUri = xPathFactory.newXPath().compile("/Files/ValidationRule/@src");
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
        this.validationRuleFactory = new SingleValidationRuleFactory(metaData);
    }

    @Override
    public Map<String, ValidationRule> compile(File file) throws IOException {
        InputStream stream = new FileInputStream(file);
        return compile(stream);
    }

    @Override
    public Map<String, ValidationRule> compile(InputStream stream) throws IOException {
        try {
            Document document = new ConfigurationXMLParser().parseFrom(stream);
            NodeList srcFiles = (NodeList) xPathValidationRuleSourceUri.evaluate(document, XPathConstants.NODESET);
            Map<String, ValidationRule> allConfig = new HashMap<>();
            for (int i = 0; i < srcFiles.getLength(); i++) {
                String eachSrc = srcFiles.item(i).getTextContent();
                Map<String, ValidationRule> config = validationRuleFactory.compile(new File(eachSrc));
                allConfig.putAll(config);
            }
            return Collections.unmodifiableMap(new HashMap<>(allConfig));
        }
        catch (XPathExpressionException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
