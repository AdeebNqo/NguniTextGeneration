package za.co.mahlaza.research.grammarengine.nguni;

import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.Word;
import za.co.mahlaza.research.grammarengine.base.interfaces.*;
import za.co.mahlaza.research.grammarengine.base.models.template.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MMNguniTemplateLinearizer implements TemplateLinearizer {

    private ConcordMapper concordMapper;
    private CopulaMapper copulaMapper;
    private MorphophonoAlternator morphophonoAlternator;
    private LocativeMapper locativeMapper;

    public boolean IS_DEBUG_ENABLED = false;

    //TODO: create factory classes for the mappers
    public MMNguniTemplateLinearizer(ConcordMapper concordMapper, CopulaMapper copulaMapper, LocativeMapper locativeMapper, MorphophonoAlternator morphophonoAlternator) {
        this.concordMapper = concordMapper;
        this.morphophonoAlternator = morphophonoAlternator;
        this.copulaMapper = copulaMapper;
        this.locativeMapper = locativeMapper;
    }

    @Override
    public void enableDebugging() {
        IS_DEBUG_ENABLED = true;
    }

    @Override
    public String linearize(Template template, SlotValues SV) {

        StringBuilder templateValue = new StringBuilder();

        //Inserting values into slots
        List<Slot> slots = template.getSlots();
        HashMap<String, SlotFiller> slotValues = SV.getValues();


        for (Slot slot: slots) {
            String label = slot.getIdentification(); //getUnderlyingResource().getLocalName();
            SlotFiller slotFiller = slotValues.get(label);
            if (slotFiller != null) {
                slot.insertValue(slotFiller);
            }
            //TODO: throw an exception, missing slot filler.
        }

        parseWordDependencies(template);

        List<PolymorphicWord> templateWords = template.getPolymorphicWords();
        //forming the chimeras
        for (PolymorphicWord currWord : templateWords) {
            //setting values of concords using governors
            //List<Concord> concords =
            for (Concord concord : currWord.getConcords()) {
                SlotFiller gov = concord.getController();

                //System.out.println("Gov "+gov);
                if (gov != null) {
                    List<Feature> features = gov.getFeatures();
                    //System.out.println("Features = "+features.size());
                    for (Feature feature : features) {
                        if (feature instanceof NounClass) {

                            String concordValue = concordMapper.getConcordValue(((NounClass) feature), concord.getConcordType());
                            if (concordValue.contains("/")) {
                                //if class has two concords then choose appropriate one
                                int concordIndex = concord.getIndex();
                                InternalSlotRootAffix nextItem = currWord.getItemAt(concordIndex + 1);
                                String nextMorphemeVal = nextItem.getValue();
                                concordValue = concordMapper.getConcordValue((NounClass) feature, concord.getConcordType(), nextMorphemeVal);
                            }
                            concord.setValue(concordValue);
                            break;
                        }
                    }
                }
            }

            //TODO: use encapsulation when setting the value of the copula and locative below

            //getting copula and setting its value
            Copula copula = currWord.getCopula();
            InternalSlotRootAffix affix = getAffixThatFollowsCopula(currWord);
            List<Feature> features = currWord.getFeatures();
            if (features.size() != 0) {
                for (Feature ft : features) {
                    if (ft instanceof NounClass) {
                        NounClass nClass = (NounClass) ft;
                        String copulaValue = copulaMapper.getCopulaValue(affix, nClass);
                        copula.setValue(copulaValue);
                        break;
                    }
                }
            }
            else if (affix instanceof Slot) {
                features = ((Slot) affix).getInsertedValue().getFeatures();
                if (features != null) {
                    for (Feature ft : features) {
                        if (ft instanceof NounClass) {
                            NounClass nClass = (NounClass) ft;
                            String copulaValue = copulaMapper.getCopulaValue(affix, nClass);
                            copula.setValue(copulaValue);
                            break;
                        }
                    }
                }
                else {
                    String copulaValue = copulaMapper.getCopulaValue(affix);
                    copula.setValue(copulaValue);
                }
            }
            //getting locative and setting its value
            Locative loc = currWord.getLocative();
            if (loc != null) {
                InternalSlotRootAffix affixNextToLoc = getAffixThatFollowsLocative(currWord);
                List<Feature> locFeatures = currWord.getFeatures();
                if (locFeatures.size() != 0) {
                    for (Feature ft : features) {
                        System.out.println(ft);
                        if (ft instanceof NounClass) {
                            NounClass nClass = (NounClass) ft;
                            String locValue = locativeMapper.getLocativeValue(affix, nClass);
                            copula.setValue(locValue);
                            break;
                        }
                    }
                } else if (affixNextToLoc instanceof Slot) {
                    features = ((Slot) affixNextToLoc).getInsertedValue().getFeatures();
                    if (features != null) {
                        for (Feature ft : features) {
                            if (ft instanceof NounClass) {
                                NounClass nClass = (NounClass) ft;
                                String locValue = locativeMapper.getLocativeValue(affix, nClass);
                                loc.setValue(locValue);
                                break;
                            }
                        }
                    }
                }
            }

            List<InternalSlotRootAffix> morphemes = currWord.getAllMorphemes();
            String value = getValueOfPolymorph(morphemes);
            insertValueInPolymorph(currWord, value);
        }


        List<TemplatePortion> templatePortions = template.getTemplatePortions();
        //collecting the linearized words
        for (int i = 0; i < templatePortions.size()-1; i++) {

            templateValue.append(templatePortions.get(i));
            if (!(templatePortions.get(i+1) instanceof Punctuation) && !(templatePortions.get(i) instanceof Punctuation)) {
                templateValue.append(" ");
            }
        }
        templateValue.append(templatePortions.get(templatePortions.size()-1));

        return templateValue.toString().trim();
    }

    private static void parseWordDependencies(Template template) {
        //case 1 -- when polymorphs in slots depend on unimorphs
        for (UnimorphicWord unimorphicWord : template.getUnimorphicWords()) {
            List<Slot> slots = template.getSlots();
            for (Slot slot : slots) {
                if (slot.doesReliesOnSomething()) {
                    if (slot.reliesOn(unimorphicWord.getIdentification())) {
                        //if slot relies on unimorphic word
                        SlotFiller slotFiller = slot.getInsertedValue();
                        List<PolymorphicWord> polymorphicWords = getPolymorphicWordsFromSlotFiller(slotFiller);
                        for (PolymorphicWord polymorphicWord : polymorphicWords) {
                            List<Concord> concords = polymorphicWord.getConcords();
                            for (Concord concord : concords) {
                                if (unimorphicWord.doesControlConcord(concord.getIdentification())) {
                                    concord.setController(unimorphicWord);
                                }
                            }
                        }
                    }
                }
            }
        }
        //case 2 -- when polymorphs are providedFor by unimorphs placed in slots
        for (Slot slot : template.getSlots()) {
            for (PolymorphicWord polymorphicWord : template.getPolymorphicWords()) {
                if (polymorphicWord.doesReliesOnSomething()) {
                    if (polymorphicWord.reliesOn(slot.getIdentification())) {
                        //if poly relies on the slot
                        SlotFiller slotFiller = slot.getInsertedValue();
                        List<UnimorphicWord> unimorphicWords = getUnimorphicWordsFromSlotFiller(slotFiller);
                        for (UnimorphicWord unimorphicWord : unimorphicWords) {
                            List<Concord> concords = polymorphicWord.getConcords();
                            for (Concord concord : concords) {
                                if (unimorphicWord.doesControlConcord(concord.getIdentification())) {
                                    concord.setController(unimorphicWord);
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public static InternalSlotRootAffix getAffixThatFollowsCopula(PolymorphicWord polymorph) {
        InternalSlotRootAffix nextAffix = null;

        List<InternalSlotRootAffix> affixes = polymorph.getAllMorphemes();
        for (int i=0; i<affixes.size(); i++) {
            if (affixes.get(i) instanceof Copula) {
                nextAffix = affixes.get(i+1);
                break;
            }
        }
        return nextAffix;
    }

    public static InternalSlotRootAffix getAffixThatFollowsLocative(PolymorphicWord polymorph) {
        InternalSlotRootAffix nextAffix = null;

        List<InternalSlotRootAffix> affixes = polymorph.getAllMorphemes();
        for (int i=0; i<affixes.size(); i++) {
            if (affixes.get(i) instanceof Locative) {
                nextAffix = affixes.get(i+1);
                break;
            }
        }
        return nextAffix;
    }


    private static List<UnimorphicWord> getUnimorphicWordsFromSlotFiller(SlotFiller slotFiller) {
        List<UnimorphicWord> unimorphicWords = new LinkedList<>();

        if (slotFiller instanceof UnimorphicWord) {
            unimorphicWords.add((UnimorphicWord) slotFiller);
        }
        else if (slotFiller instanceof Phrase) {
            List<Word> words = ((Phrase) slotFiller).getWords();
            for (Word word : words) {
                if (word instanceof UnimorphicWord) {
                    unimorphicWords.add((UnimorphicWord) word);
                }
            }
        }

        return unimorphicWords;
    }

    private static List<PolymorphicWord> getPolymorphicWordsFromSlotFiller(SlotFiller slotFiller) {
        List<PolymorphicWord> polymorphicWords = new LinkedList<>();

        if (slotFiller instanceof  PolymorphicWord) {
            polymorphicWords.add((PolymorphicWord) slotFiller);
        }
        else if (slotFiller instanceof Phrase) {
            List<Word> words = ((Phrase) slotFiller).getWords();
            for (Word word : words) {
                if (word instanceof PolymorphicWord) {
                    polymorphicWords.add((PolymorphicWord) word);
                }
            }
        }

        return polymorphicWords;
    }

    private static List<PolymorphicWord> getPolymorphicWordsWithLabels(Template template, List<String> labels) {
        List<PolymorphicWord> supportedPolyWords = new LinkedList<>();

        List<PolymorphicWord> polymorphicWords = template.getPolymorphicWords();
        for (PolymorphicWord word : polymorphicWords) {
            if (labels.contains(word.getIdentification())) {
                supportedPolyWords.add(word);
            }
        }


        return supportedPolyWords;
    }

    @Override
    public String getSlotLabel(Slot someSlot) {
        return someSlot.getLabel();
    }

    @Override
    public String getSlotValue(String slotLabel, HashMap<String, SlotFiller> slotValues) {
        return slotValues.get(slotLabel).getValue();
    }

    @Override
    public void insertValueInSlot(Slot someSlot, String someSlotValue) {
        someSlot.setValue(someSlotValue);
    }

    @Override
    public String getValueOfPolymorph(List<InternalSlotRootAffix> morphemes) {
        String leftValue = null;
        for (int i=0; i<morphemes.size()-1; i++) {
            InternalSlotRootAffix leftMorph = morphemes.get(i);
            InternalSlotRootAffix rightMorph = morphemes.get(i+1);
            if (leftValue == null) {
                leftValue = leftMorph.toString();
            }
            try {
                String newMorphValue = morphophonoAlternator.joinMorpheme(leftValue, rightMorph.toString());
                leftValue = newMorphValue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return leftValue;
    }

    @Override
    public void insertValueInPolymorph(PolymorphicWord someChimera, String someChimeraValue) {
        someChimera.setValue(someChimeraValue);
    }
}
