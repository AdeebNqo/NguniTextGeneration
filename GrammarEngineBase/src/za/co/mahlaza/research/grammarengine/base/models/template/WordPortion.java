package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotOrWordPortion;

public class WordPortion implements InternalSlotOrWordPortion {

    public int index;
    public String value;

    @Override
    public String getValue() {
        return value;
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
    public String getType() {
        return "WordPortion";
    }
}
