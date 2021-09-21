package za.co.mahlaza.research.grammarengine.base.models.interfaces;

public interface InternalSlotOrWordPortion {
    String getValue();
    int getIndex();
    void setIndex(int index);
    String getType();
}
