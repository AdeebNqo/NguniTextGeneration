package za.co.mahlaza.research.grammarengine.base.models.interfaces;

import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.template.TemplatePortion;

public abstract class Word extends TemplatePortion implements SlotFiller {
    public abstract String getValue();
}
