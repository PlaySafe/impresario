package org.companion.impresario;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecuteConditionNotMatchTest {

    private final Map<String, LabelGenerator> labelGenerators;

    private final List<String> groupNames = new ArrayList<String>() {{
        add("CHAT_AT");
        add("CHOOSE");
        add("CHOOSE_NONE");
        add("CONCAT");
        add("CUT_OFF");
        add("DIVISION");
        add("EXPONENTIAL");
        add("JOIN");
        add("LENGTH");
        add("LOWER");
        add("MODULO");
        add("MULTIPLICATION");
        add("REPLACE_ALL");
        add("REPLACE_ONE");
        add("ROUND_DOWN");
        add("ROUND_HALF_UP");
        add("ROUND_UP");
        add("SUBSTRING");
        add("SUBTRACTION");
        add("UPPER");
    }};

    public ExecuteConditionNotMatchTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/execute_condition_not_match.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        Assert.assertNotNull(labelGenerators);

        this.labelGenerators = labelGenerators;
    }

    @Test
    public void expectExceptionIfConditionNotMatch() throws ConditionNotMatchException {
        Assert.assertNotEquals(0, groupNames.size());
        for (String groupName : groupNames) {
            LabelGenerator labelGenerator = labelGenerators.get(groupName);
            Assert.assertNotNull(labelGenerator);
            try {
                labelGenerator.labelOf(null);
                Assert.fail("Expect condition not match from group " + groupName);
            }
            catch (ConditionNotMatchException e) {
                // Expect all those config to throw this exception
            }

        }
    }

}
