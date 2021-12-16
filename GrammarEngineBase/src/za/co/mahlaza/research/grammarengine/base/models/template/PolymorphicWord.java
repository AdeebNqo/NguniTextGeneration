package za.co.mahlaza.research.grammarengine.base.models.template;


import za.co.mahlaza.research.grammarengine.base.models.interfaces.Chimera;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.Word;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PolymorphicWord extends Chimera {

    private String identification;

    List<Word> governors;
    private List<InternalSlotRootAffix> wordPortions;
    private List<Feature> features;

    private List<String> fillsSlotResources;
    private List<String> dependsOnWordResources;
    private List<String> reliesOnLabels;

    private String value;

    public PolymorphicWord(List<InternalSlotRootAffix> wordPortions, List<Feature> featuresList) {
        this.wordPortions = wordPortions;
        governors = new LinkedList<>();
        this.value = null;
        features = featuresList;
        fillsSlotResources = new LinkedList<>();
        dependsOnWordResources = new LinkedList<>();

        for (int index = 0; index < this.wordPortions.size()-1; ++index) {
            InternalSlotRootAffix curr = this.wordPortions.get(index);
            InternalSlotRootAffix next = this.wordPortions.get(index+1);
            curr.setNextPart(next);
        }
    }

    public void addSlotItCanFill(String slotName) {
        fillsSlotResources.add(slotName);
    }

    public List<String> getSlotsItCanFill() {
        return fillsSlotResources;
    }


/*    public boolean doesDependOnWord(Resource controlingWordResource) {
        return dependsOnWordResources.contains(controlingWordResource);
    }

    public void addDependsOnWordResource(String dependsOnWordResource) {
        this.dependsOnWordResources.add(dependsOnWordResource);
    }*/

    public List<String> getItemsItReliesOn() {
        return reliesOnLabels;
    }

    public boolean doesReliesOnSomething() {
        return reliesOnLabels != null && reliesOnLabels.size() > 0;
    }

    public boolean reliesOn(String slotLabel) {
        return reliesOnLabels.contains(slotLabel);
    }

    public void setReliesOnLabels(List<String> reliesOnLabels) {
        this.reliesOnLabels = reliesOnLabels;
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Deprecated
    public String getIdentification() {
        return identification;
    }

    @Deprecated
    public void setIdentification(String id) {
        this.identification = id;
    }

    @Override
    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    //for debugging
    public void printAllLabelsItReliesOn() {
        for (String label : reliesOnLabels) {
            System.out.print(label+" , ");
        }
        System.out.println();
    }

    public Copula getCopula() {
        Copula cop = null;
        for (InternalSlotRootAffix slotOrWordPortion : wordPortions) {
            if (slotOrWordPortion.getType().equals("Copula")) {
                cop = (Copula) slotOrWordPortion;
                break;
            }
        }
        return cop;
    }

    public Locative getLocative() {
        Locative loc = null;
        for (InternalSlotRootAffix slotOrWordPortion : wordPortions) {
            if (slotOrWordPortion.getType().equals("Locative")) {
                loc = (Locative) slotOrWordPortion;
                break;
            }
        }
        return loc;
    }

    public List<Concord> getConcords() {
        List<Concord> concords = new LinkedList<>();
        for (InternalSlotRootAffix slotOrWordPortion : wordPortions) {
            if (slotOrWordPortion.getType().equals("AffixChunk")) {
                List<Concord> tmpConcords = ((AffixChunk) slotOrWordPortion).getConcords();
                concords.addAll(tmpConcords);
            }
            if (slotOrWordPortion.getType().equals("Concord")) {
                concords.add((Concord) slotOrWordPortion);
            }
        }
        return concords;
    }

    public List<Slot> getSlots() {
        List<Slot> slots = new LinkedList<>();
        for (InternalSlotRootAffix slotOrWordPortion : wordPortions) {
            if (slotOrWordPortion instanceof  Slot) {
                slots.add((Slot) slotOrWordPortion);
            }
        }
        return slots;
    }

    public List<InternalSlotRootAffix> getAllMorphemes () {
        List<InternalSlotRootAffix> morphemes = new LinkedList<>();
        for (InternalSlotRootAffix slotOrWordPortion : wordPortions) {
            if (slotOrWordPortion  instanceof AffixChunk) {
                AffixChunk affixChunk = (AffixChunk) slotOrWordPortion;
                List<Affix> affixes = affixChunk.getAffixes();
                if (affixes.size() > 0) {
                    for (Affix affix : affixes) {
                        if (affix instanceof Concord) {
                            morphemes.add((Concord) affix);
                        } else if (affix instanceof UnimorphicAffix) {
                            morphemes.add((UnimorphicAffix) affix);
                        }
                    }
                } else {
                    morphemes.add(slotOrWordPortion);
                }
            }
            else {
                morphemes.add(slotOrWordPortion);
            }
        }
        return morphemes;
    }

    public void addMorpheme(InternalSlotRootAffix someMorpheme) {
        wordPortions.add(someMorpheme);
    }

    public void removeMorpheme(InternalSlotRootAffix someMorpheme) {
        wordPortions.remove(someMorpheme);
    }

    public void removeMorpheme(int indexOfMorpheme) {
        wordPortions.remove(indexOfMorpheme);
    }

    public InternalSlotRootAffix getFirstItem() {
        return getItemAt(0);
    }

    public InternalSlotRootAffix getLastItem() {
        int size = getAllMorphemes().size();
        return getItemAt(size-1);
    }

    public InternalSlotRootAffix getItemAt(int index) {
        return getAllMorphemes().get(index);
    }

    @Override
    public void shuffleFillsInLabels() {
        Collections.shuffle(fillsSlotResources);
    }

    @Override
    public String toString() {
        if (value == null || value.isEmpty() || value.isBlank()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<InternalSlotRootAffix> morphemes = getAllMorphemes();
            for (InternalSlotRootAffix slotRootAffix: morphemes) {
                stringBuilder.append(slotRootAffix.getValue());
            }
            return stringBuilder.toString();
        }
        return value;
    }
}
