package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.models.annotations.RelationSetter;

public class TemplatePortion {
    public TemplatePortion nextPart;
    public String serialisedName;

    public TemplatePortion getNextPart() {
        return nextPart;
    }

    @RelationSetter(RangeName = "Next Part", RdfName = "hasNextPart")
    public void setNextPart(TemplatePortion portion) {
        nextPart = portion;
    }

    @RelationSetter(RangeName = "Name", RdfName = "serialisedName")
    public void setSerialisedName(String name) {
        serialisedName = name;
    }

    public String getSerialisedName() {
        return serialisedName;
    }
}
