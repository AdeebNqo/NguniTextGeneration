package za.co.mahlaza.research.grammarengine.base.interfaces;

import za.co.mahlaza.research.grammarengine.base.models.template.SlotValues;
import za.co.mahlaza.research.grammarengine.base.models.template.Template;

import java.util.List;

public interface Filter {
    SlotValues filterSlotFillers(List<SlotValues> someSlotFillers);
    Template filterTemplates(List<Template> someTemplates);
}
