package za.co.mahlaza.research.grammarengine.base.interfaces;

import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.List;

public interface SlotFiller {
    void addSlotItCanFill(String slotName);
    List<String> getSlotsItCanFill();
    String getValue();
    void setValue(String value);
    List<Feature> getFeatures();
    void setFeatures(List<Feature> features);

    //for debugging
    void shuffleFillsInLabels();
}
