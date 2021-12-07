package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.List;

public class Root extends WordPortion implements InternalSlotRootAffix {

    private List<Feature> features;
    private InternalSlotRootAffix nextPart;
    private String serialisedName;

    public Root(String value, List<Feature> featureList) {
        this.value = value;
        this.features = featureList;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String getType() {
        return "Root";
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
}
