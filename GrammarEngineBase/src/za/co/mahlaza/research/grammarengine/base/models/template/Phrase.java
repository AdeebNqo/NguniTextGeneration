package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.annotations.RelationSetter;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.Word;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Phrase extends TemplatePortion implements SlotFiller {

    private List<Feature> features;
    private List<Word> words;
    private String value;

    private List<String> fillsSlots;

    public Phrase(List<Word> words) {
        this(words, null);
    }

    public Phrase(String value) {
        this(new LinkedList<>(), value);
    }

    public Phrase(List<Word> words, String value) {
        fillsSlots = new LinkedList<>();
        this.words = words;
        this.value = value;
    }

    public void addSlotItCanFill(String slotName) {
        fillsSlots.add(slotName);
    }

    public List<String> getSlotsItCanFill() {
        return fillsSlots;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    @Override
    public String getValue() {
        return toString();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public List<Feature> getFeatures() {
        return features;
    }

    @Override
    public void shuffleFillsInLabels() {
        Collections.shuffle(fillsSlots);
    }

    @Override
    public String toString() {
        if (value == null || value.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Word word : words) {
                sb.append(word.getValue());
                sb.append(" "); //TODO: handle this better with the SPACE concept
            }
            return sb.toString().strip();
        }
        return value;
    }
}
