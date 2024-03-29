package za.co.mahlaza.research.grammarengine.base.models.template;


import za.co.mahlaza.research.grammarengine.base.models.annotations.RelationSetter;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.List;

public class Copula extends Affix implements InternalSlotRootAffix {

    private String identification;
    private List<Feature> featureList;

    private String value;
    private String label;
    private int index;

    private InternalSlotRootAffix nextPart;
    private String serialisedName;

    public Copula( String label, List<Feature> featureList) {
        this.label = label;
        this.featureList = featureList;
    }

    @Deprecated
    public String getIdentification() {
        return identification;
    }

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
        return "Copula";
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
    public void setNextMorphPart(InternalSlotRootAffix nextPart) {
        this.nextPart = nextPart;
    }

    @Override
    public InternalSlotRootAffix getNextMorphPart() {
        return nextPart;
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
