package org.companion.impresario;

import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ExecuteNonExistingMethodTest {

    private final LabelGenerator labelGenerator;

    public ExecuteNonExistingMethodTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_data.xml");
        File configResource = new File("src/test/resources/execute_non_existing_method.xml");
        MetaData metaData = new MetaDataFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new SingleLabelGeneratorFactory(metaData);

        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        this.labelGenerator = labelGenerators.get("NON_EXISTING_METHOD");
    }

    @Test
    public void expectExceptionIfNoSuchMethodToExecute() throws ConditionNotMatchException {
        Assert.assertThrows(NoSuchExecutableMethodException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                labelGenerator.labelOf(new Object());
            }
        });
    }
}
