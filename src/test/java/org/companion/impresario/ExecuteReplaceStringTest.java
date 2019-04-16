package org.companion.impresario;

import data.FlatWallet;
import data.Wallet;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class ExecuteReplaceStringTest {

    private Map<String, LabelGenerator> labelGenerators;

    public ExecuteReplaceStringTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/execute_replace_string.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        Assert.assertNotNull(labelGenerators);

        this.labelGenerators = labelGenerators;
    }

    @Test
    public void expectReplaceStringBySpecificKey() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("BY_SPECIFIC_KEY");

        String result = labelGenerator.labelOf(null);
        Assert.assertEquals("Hello John!", result);
    }

    @Test
    public void expectReplaceStringDefinitionAtSpecificKeyByField() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_DEFINITION_AT_SPECIFIC_KEY_BY_FIELD");

        Wallet wallet = new FlatWallet("15.33");
        Assert.assertEquals("AMOUNT=15.33", labelGenerator.labelOf(wallet));
    }

    @Test
    public void expectReplaceStringVariableAtSpecificKeyByMap() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("REPLACE_DEFINITION_AT_SPECIFIC_KEY_BY_MAP");
        Map<String, Object> variable = new HashMap<String, Object>() {{
            put("target", "!@#$");
            put("amount", "99.99");
        }};
        Assert.assertEquals("AMOUNT=99.99", labelGenerator.labelOf(variable));
    }
}
