package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.List;

public class Root extends WordPortion implements InternalSlotRootAffix {

    private List<Feature> features;

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
}
