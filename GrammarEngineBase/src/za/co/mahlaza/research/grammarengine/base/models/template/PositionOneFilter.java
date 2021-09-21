package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.interfaces.Filter;

import java.util.List;

public class PositionOneFilter implements Filter {
    @Override
    public SlotValues filterSlotFillers(List<SlotValues> someSlotFillers) {
        return someSlotFillers.get(0);
    }

    @Override
    public Template filterTemplates(List<Template> someTemplates) {
        return someTemplates.get(0);
    }
}
