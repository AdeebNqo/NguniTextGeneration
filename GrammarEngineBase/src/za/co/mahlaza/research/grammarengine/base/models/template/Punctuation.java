package za.co.mahlaza.research.grammarengine.base.models.template;

public class Punctuation extends TemplatePortion {
    String value;

    public Punctuation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
