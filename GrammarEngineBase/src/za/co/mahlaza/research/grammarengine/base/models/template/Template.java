package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.Word;
import za.co.mahlaza.research.grammarengine.base.models.mola.Languoid;

import java.util.LinkedList;
import java.util.List;

public class Template {

    //private Model underlyingModel;
    public boolean TEMPLATE_SPEC_OMITTED_SPACES = true;

    public String serialisedName;

    public List<TemplatePortion> words;
    public Languoid language;

    public Template(List<TemplatePortion> words) {
        this.words = words;

        for (int index = 0; index < words.size()-1; ++index) {
            TemplatePortion curr = this.words.get(index);
            TemplatePortion next = this.words.get(index+1);
            curr.setNextPart(next);
        }
    }


    public List<TemplatePortion> getTemplatePortions() {
        return words;
    }

    public List<UnimorphicWord> getUnimorphicWords() {
        List<UnimorphicWord> unimorphicWords = new LinkedList<>();

        for (int i=0; i<words.size(); i++) {

            TemplatePortion portion = words.get(i);

            if (portion instanceof UnimorphicWord) {
                unimorphicWords.add((UnimorphicWord) portion);
            }
            else if (portion instanceof Phrase) {
                List<Word> words = ((Phrase) portion).getWords();
                for (Word word : words) {
                    if (word instanceof UnimorphicWord) {
                        unimorphicWords.add((UnimorphicWord) word);
                    }
                }
            }
            else if (portion instanceof  Slot) {
                SlotFiller slotFiller = ((Slot) portion).getInsertedValue();

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
            }
        }

        return unimorphicWords;
    }

    public List<PolymorphicWord> getPolymorphicWords() {
        List<PolymorphicWord> tmpWords = new LinkedList<>();
        for (TemplatePortion portion : words) {
            if (portion instanceof PolymorphicWord) {
                tmpWords.add((PolymorphicWord) portion);
            }
            if (portion instanceof Slot) {

                //if slot has polymorphic word instead into it
                SlotFiller slotFiller = ((Slot) portion).getInsertedValue();
                if (slotFiller instanceof PolymorphicWord) {
                    tmpWords.add((PolymorphicWord) slotFiller);
                }
                else if (slotFiller instanceof Phrase) {
                    List<Word> words = ((Phrase) slotFiller).getWords();
                    for (Word word : words) {
                        if (word instanceof PolymorphicWord) {
                            tmpWords.add((PolymorphicWord) word);
                        }
                    }
                }
            }
        }
        return tmpWords;
    }



    public List<Slot> getSlots () {
        List<Slot> tmpSlots = new LinkedList<>();
        for (TemplatePortion portion : words) {
            if (portion instanceof Slot) {
                tmpSlots.add((Slot) portion);
            }
            else if (portion instanceof  PolymorphicWord) {
                PolymorphicWord polymorphicWord = (PolymorphicWord) portion;
                tmpSlots.addAll(polymorphicWord.getSlots());
            }
        }
        return tmpSlots;
    }

/*
    public void setUnderlyingModel(Model underlyingModel) {
        this.underlyingModel = underlyingModel;
    }
*/

    public void setLanguage(Languoid language) {
        this.language = language;
    }

    public Languoid getLanguage() {
        return language;
    }

    public void setSerialisedName(String name) {
        serialisedName = name;
    }

    public String getSerialisedName() {
        return serialisedName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.size()-1; i++) {
            sb.append(words.get(i));
            if (TEMPLATE_SPEC_OMITTED_SPACES) {
                if (!(words.get(i + 1) instanceof Punctuation) && !(words.get(i) instanceof Punctuation)) {
                    sb.append(" ");
                }
            }
        }
        sb.append(words.get(words.size()-1));
        return sb.toString();
    }
}
