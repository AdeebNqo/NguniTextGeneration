package za.co.mahlaza.research.grammarengine.base.models.template;

public class TemplatePortion {
    public TemplatePortion nextPart;

    public TemplatePortion getNextPart() {
        return nextPart;
    }
    public void setNextPart(TemplatePortion portion) {
        nextPart = portion;
    }
}
