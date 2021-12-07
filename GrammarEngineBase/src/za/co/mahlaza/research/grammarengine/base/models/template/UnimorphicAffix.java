package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.List;

public class UnimorphicAffix extends Affix implements InternalSlotRootAffix {

    private String value;
    private int index;
    private List<Feature> features;

    private InternalSlotRootAffix nextPart;
    private String serialisedName;

    public UnimorphicAffix(String value, List<Feature> featureList) {
        this.value = value;
        this.features = featureList;
    }

    @Override
    public String getValue() {
        return value;
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
        return "UnimorphicAffix";
    }

    @Override
    public void setNextPart(InternalSlotRootAffix nextPart) {
        this.nextPart = nextPart;
    }

    @Override
    public String getSerialisedName() {
        return serialisedName;
    }

    @Override
    public void setSerialisedName(String serialisedName) {
        this.serialisedName = serialisedName;
    }

    @Override
    public String toString() {
        return value;
    }
}
