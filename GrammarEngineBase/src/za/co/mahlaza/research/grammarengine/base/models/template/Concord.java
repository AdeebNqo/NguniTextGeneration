package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.annotations.RelationSetter;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.feature.ConcordType;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.List;
import java.util.stream.Collectors;

public class Concord extends Affix implements InternalSlotRootAffix {

    private String identification;

    private String value;
    private String label;
    private List<Feature> featureList;

    private SlotFiller controller;

    private int index;
    private InternalSlotRootAffix nextPart;
    private String serialisedName;

    public Concord(String label, List<Feature> featureList) {
        this("", label, featureList);
    }


    public Concord(String value, String label, List<Feature> featureList) {
        this.value = value;
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

    public void addFeature(Feature someConcordFeature) {
        if (featureList != null) {
            featureList.add(someConcordFeature);
        }
    }

    public void addFeatures(List<Feature> someConcordFeatures) {
        someConcordFeatures.stream().forEach(feature -> addFeature(feature));
    }

    public void removeFeature(Feature someConcordFeature) {
        featureList = featureList.stream().filter(feature -> !feature.equals(someConcordFeature)).collect(Collectors.toList());
    }

    public void removeFeatures(List<Feature> someConcordFeatures) {
        someConcordFeatures.stream().forEach(feature -> removeFeature(feature));
    }

    @Override
    public String getValue() {
        return value;
    }

    @RelationSetter(RangeName = "Value", RdfName = "hasValue")
    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    @RelationSetter(RangeName = "Label", RdfName = "hasLabel")
    public void setLabel(String label) {
        this.label = label;
    }

    public ConcordType getConcordType() {
        for (Feature feature : featureList) {
            if (feature instanceof ConcordType) {
                return (ConcordType) feature;
            }
        }
        return null;
    }

    public void setController(SlotFiller controller) {
        this.controller = controller;
    }

    public SlotFiller getController() {
        if (controller != null) {
            return controller;
        }
        return null;
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
        return "Concord";
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
