package za.co.mahlaza.research.grammarengine.base.models.mola;

public class Language extends Languoid {
    public String customLanguageTag;
    public LanguageFamily langFamily;

    public LanguageFamily getLangFamily() {
        return langFamily;
    }

    public void setLangFamily(LanguageFamily langFamily) {
        this.langFamily = langFamily;
    }
}
