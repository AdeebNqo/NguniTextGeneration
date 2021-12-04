package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotOrWordPortion;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.List;

public class Slot extends TemplatePortion implements InternalSlotOrWordPortion, InternalSlotRootAffix {

    private String identification;

    private int index;
    private String value;
    private String label;

    private SlotFiller insertedValue;
    private List<Feature> features;

    private List<String> reliesOnLabels;
    private InternalSlotRootAffix nextPart;

    public Slot(String label, List<Feature> featureList) {
        this("", label, featureList);
    }

    public Slot(String value, String label, List<Feature> featureList) {
        this.value = value;
        this.label = label;
        this.features = featureList;
    }

    public String getValue() {
        return toString();
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String id) {
        this.identification= id;
    }

    public String getLabel() {
        return label;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public SlotFiller getInsertedValue() {
        return insertedValue;
    }

    public void insertValue(SlotFiller insertedValue) {
        this.insertedValue = insertedValue;
        this.value = insertedValue.getValue();
    }

    public boolean reliesOn(String label) {
        return reliesOnLabels.contains(label);
    }

    public boolean doesReliesOnSomething() {
        return reliesOnLabels != null && reliesOnLabels.size() > 0;
    }


    public void setReliesOnLabels(List<String> reliesOnLabels) {
        this.reliesOnLabels = reliesOnLabels;
    }

    public List<String> getReliesOnLabels() {
        return reliesOnLabels;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String getType() {
        return "Slot";
    }

    @Override
    public void setNextPart(InternalSlotRootAffix internalPart) {
        nextPart = internalPart;
    }

    @Override
    public String toString() {
        if (value == null || value.isEmpty()) {
            return "["+label+"]";
        }
        return insertedValue.getValue();
    }
}
