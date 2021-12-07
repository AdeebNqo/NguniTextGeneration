package za.co.mahlaza.research.grammarengine.base.models.mola;

public class Languoid {
    public Region region;
    public Period period;

    private String serialisedName;

    public void setSerialisedName(String name) {
        serialisedName = name;
    }
    public String getSerialisedName() {
        return serialisedName;
    }
}
