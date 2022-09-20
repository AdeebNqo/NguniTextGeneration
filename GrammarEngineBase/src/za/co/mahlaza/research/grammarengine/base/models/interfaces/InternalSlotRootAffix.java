package za.co.mahlaza.research.grammarengine.base.models.interfaces;

public interface InternalSlotRootAffix {
    String getType();
    String getValue();
    void setValue(String value);
    void setNextMorphPart(InternalSlotRootAffix internalPart);
    InternalSlotRootAffix getNextMorphPart();

    public void setSerialisedName(String name);
    public String getSerialisedName();
}
