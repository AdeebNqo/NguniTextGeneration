package za.co.mahlaza.research.grammarengine.base.models.interfaces;

import za.co.mahlaza.research.grammarengine.base.models.annotations.RelationSetter;

public abstract class Chimera extends Word {
    @RelationSetter(RangeName = "Value", RdfName = "hasValue")
    public abstract void setValue(String value);
}
