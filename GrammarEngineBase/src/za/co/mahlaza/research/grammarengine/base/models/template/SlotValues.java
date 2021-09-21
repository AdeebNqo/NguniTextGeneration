package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;

import java.util.HashMap;
import java.util.List;

public abstract class SlotValues {

/*    private String baseURI;
    private String slotFillerFilename;
    List<SlotFiller> slotFillers;
    HashMap<String, SlotFiller> SV = new HashMap<>();*/

    public abstract void shuffle();
    public abstract HashMap<String, SlotFiller> getValues ();
    public abstract int getMaxSlotFillsIn(List<SlotFiller> slotFillers);
}
