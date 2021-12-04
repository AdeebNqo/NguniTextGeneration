package za.co.mahlaza.research.grammarengine.base.models.interfaces;

public interface InternalSlotRootAffix {
    String getType();
    String getValue();
    void setNextPart(InternalSlotRootAffix internalPart);
}
