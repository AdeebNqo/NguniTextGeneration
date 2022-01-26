package za.co.mahlaza.research.grammarengine.base.models.template;


import za.co.mahlaza.research.grammarengine.base.models.annotations.RelationSetter;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.List;

public class Locative extends Affix implements InternalSlotRootAffix {

    private String identification;
    private List<Feature> featureList;

    private String value;
    private String label;
    private int index;

    private InternalSlotRootAffix nextPart;
    private String serialisedName;

    public Locative( String label, List<Feature> featureList) {
        this.label = label;
        this.featureList = featureList;
    }

    @Deprecated
    public String getIdentification() {
        return identification;
    }

    @Deprecated
    public void setIdentification(String id) {
        this.identification = id;
    }

    @Override
    public String getValue() {
        return toString();
    }

    @RelationSetter(RangeName = "Value", RdfName = "hasValue")
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return "["+label+"]";
    }

    @Override
    public String getType() {
        return "Locative";
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    public InternalSlotRootAffix getNextPart() {
        return nextPart;
    }

    public void setNextPart(InternalSlotRootAffix nexPart) {
        this.nextPart = nexPart;
    }

    @Override
    public String getSerialisedName() {
        return serialisedName;
    }

    @Override
    public void setSerialisedName(String serialisedName) {
        this.serialisedName = serialisedName;
    }
}
