package za.co.mahlaza.research.grammarengine.base.interfaces;

import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.template.PolymorphicWord;
import za.co.mahlaza.research.grammarengine.base.models.template.Slot;
import za.co.mahlaza.research.grammarengine.base.models.template.SlotValues;
import za.co.mahlaza.research.grammarengine.base.models.template.Template;

import java.util.HashMap;
import java.util.List;

public interface TemplateLinearizer {

    String linearize(Template template, SlotValues slotValues);
    String getSlotLabel(Slot someSlot);
    String getSlotValue(String slotLabel, HashMap<String, SlotFiller> slotValues);
    void insertValueInSlot(Slot someSlot, String someSlotValue);
    String getValueOfPolymorph(List<InternalSlotRootAffix> morphemes);
    void insertValueInPolymorph(PolymorphicWord someChimera, String someChimeraValue);
    void enableDebugging();
}
