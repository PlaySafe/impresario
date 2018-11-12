package org.companion.impresario;

import data.Address;
import data.DefaultAddress;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class TextManipulationTest {

    private Map<String, LabelGenerator> labelGenerators;

    public TextManipulationTest() throws IOException {
        File metaResource = new File("src/test/resources/meta_label.xml");
        File configResource = new File("src/test/resources/text_manipulation.xml");
        MetaData metaData = new MetaLabelFactory().compile(metaResource);
        LabelGeneratorFactory labelGeneratorFactory = new LabelGeneratorFactory(metaData);
        Map<String, LabelGenerator> labelGenerators = labelGeneratorFactory.compile(configResource);
        Assert.assertNotNull(labelGenerators);

        this.labelGenerators = labelGenerators;
    }

    @Test
    public void expectEmptyStringIfStateIsNull() throws ConditionNotMatchException {
        Address address = new DefaultAddress.Builder()
                .setState(null)
                .build();

        LabelGenerator labelGenerator = labelGenerators.get("EMPTY_IF_STATE_NOT_EXISTS");
        Assert.assertEquals(0, labelGenerator.labelOf(address).length());
    }

    @Test
    public void expectEmptyStringIfStateIsEmpty() throws ConditionNotMatchException {
        Address address = new DefaultAddress.Builder()
                .setState("")
                .build();

        LabelGenerator labelGenerator = labelGenerators.get("EMPTY_IF_STATE_NOT_EXISTS");
        Assert.assertEquals(0, labelGenerator.labelOf(address).length());
    }

    @Test
    public void expectUpperCaseIfStreetExists() throws ConditionNotMatchException {
        Address address = new DefaultAddress.Builder()
                .setStreet(" abc ")
                .build();

        LabelGenerator labelGenerator = labelGenerators.get("UPPER_IF_STREET_EXISTS");
        Assert.assertEquals(" ABC ", labelGenerator.labelOf(address));
    }

    @Test
    public void expectFirst5CityCharsIfLongerThan() throws ConditionNotMatchException {
        Address address = new DefaultAddress.Builder()
                .setCity("BANANA")
                .build();

        LabelGenerator labelGenerator = labelGenerators.get("KEEP_FIRST_5_CITY_CHARS_IF_LONGER_THAN");
        Assert.assertEquals("BANAN", labelGenerator.labelOf(address));
    }

    @Test
    public void expectUnreadableLast3Postal() throws ConditionNotMatchException {
        Address address = new DefaultAddress.Builder()
                .setPostalCode("123456")
                .build();

        LabelGenerator labelGenerator = labelGenerators.get("HIDE_THE_LAST_3_POSTAL_CODE");
        Assert.assertEquals("123XXX", labelGenerator.labelOf(address));
    }

    @Test
    public void expectHideFirst2AndLast3OfProperties() throws ConditionNotMatchException {
        System.setProperty("sensitive.number", "403265925");
        LabelGenerator labelGenerator = labelGenerators.get("HIDE_FIRST_2_AND_LAST_3");
        Assert.assertEquals("XX3265XXX", labelGenerator.labelOf(null));
    }

    @Test
    public void expectStringWithCommaSeparate() throws ConditionNotMatchException {
        LabelGenerator labelGenerator = labelGenerators.get("JOIN_STRING_WITH_COMMA");
        Assert.assertEquals("ABC, DEF", labelGenerator.labelOf(null));
    }
}
