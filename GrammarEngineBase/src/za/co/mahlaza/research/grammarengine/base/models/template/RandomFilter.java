package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.interfaces.Filter;

import java.util.List;
import java.util.Random;

public class RandomFilter implements Filter {

    Random randomizer ;

    public RandomFilter() {
        this.randomizer = new Random();
    }

    @Override
    public SlotValues filterSlotFillers(List<SlotValues> someSlotFillers) {
        int randomPos = randomizer.nextInt(someSlotFillers.size());
        return someSlotFillers.get(randomPos);
    }

    @Override
    public Template filterTemplates(List<Template> someTemplates) {
        int randomPos = randomizer.nextInt(someTemplates.size());
        return someTemplates.get(randomPos);
    }
}
