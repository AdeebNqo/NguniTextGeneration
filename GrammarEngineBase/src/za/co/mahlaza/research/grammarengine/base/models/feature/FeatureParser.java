package za.co.mahlaza.research.grammarengine.base.models.feature;

import java.util.List;

public abstract class FeatureParser {
    public abstract List<String> getRegisteredFeatureProps();
    public abstract Feature getFeature (String featureType);
}
