package za.co.mahlaza.research.grammarengine.base.models.template;

public class TemplatePortion {
    public TemplatePortion nextPart;
    public String serialisedName;

    public TemplatePortion getNextPart() {
        return nextPart;
    }
    public void setNextPart(TemplatePortion portion) {
        nextPart = portion;
    }

    public void setSerialisedName(String name) {
        serialisedName = name;
    }
    public String getSerialisedName() {
        return serialisedName;
    }
}
