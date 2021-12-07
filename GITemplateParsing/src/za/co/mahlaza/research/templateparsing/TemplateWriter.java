package za.co.mahlaza.research.templateparsing;


import org.apache.jena.rdf.model.*;
import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.feature.FeatureParser;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotOrWordPortion;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.mola.*;
import za.co.mahlaza.research.grammarengine.base.models.template.*;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static za.co.mahlaza.research.templateparsing.URIS.*;

public class TemplateWriter {

    private static FeatureParser featureParser;
    public static void Init(FeatureParser fp) {
        featureParser = fp;
    }

    public static void saveTemplate(Template template, String templateURI, String filename) throws Exception {
        Collection<Template> templates = new LinkedList<>();
        templates.add(template);
        saveTemplates(templates, templateURI, filename);
    }

    public static void saveTemplates(Collection<Template> templates, String templatesURI, String filename) throws Exception {
        Model model = ModelFactory.createDefaultModel();

        for (Template template : templates) {
            Resource templateRes = model.createResource(templatesURI + template.getSerialisedName());
            addLanguageAndTemplateFragments(templateRes, template, templatesURI, model);
        }

        FileWriter out = new FileWriter(filename);
        model.write(out, "Turtle") ;
        out.close();
    }

    private static void attachValueAndLabel(Resource wordResource, TemplatePortion word, String templatesURI, Model model) {
        Property valueProp = model.getProperty(templatesURI + "hasValue");
        if (word instanceof SlotFiller) {
            String value = ((SlotFiller) word).getValue();
            if (value != null && !value.isEmpty()) {
                wordResource.addLiteral(valueProp, value);
            }
        }
        else if (word instanceof InternalSlotOrWordPortion) {
            String value = ((InternalSlotOrWordPortion) word).getValue();
            if (value != null && !value.isEmpty()) {
                wordResource.addLiteral(valueProp, value);
            }
        }
        if (word instanceof Slot) {
            Property hasLabel = model.getProperty(templatesURI + "hasLabel");
            wordResource.addLiteral(hasLabel, ((Slot) word).getLabel());
        }
        attachToCTType(wordResource, word, model);
    }

    private static void attachToCTType(Resource wordResource, TemplatePortion word, Model model) {
        String wordType = word.getClass().getSimpleName();
        Property typeProp = model.getProperty(RDF_NS+ "type");
        switch (wordType) {
            case "UnimorphicWord": {
                wordResource.addProperty(typeProp, ToCT_NS + "UnimorphicWord");
                break;
            }
            case "PolymorphicWord": {
                wordResource.addProperty(typeProp, ToCT_NS + "PolymorphicWord");
                break;
            }
            case "Slot": {
                wordResource.addProperty(typeProp, ToCT_NS + "Slot");
                break;
            }
            case "Phrase": {
                wordResource.addProperty(typeProp, ToCT_NS + "Phrase");
                break;
            }
            case "Punctuation": {
                wordResource.addProperty(typeProp, ToCT_NS + "Punctuation");
                break;
            }
            case "Space": {
                wordResource.addProperty(typeProp, ToCT_NS + "Space");
                break;
            }
        }
    }

    private static void addMolaType(Resource langResource, Languoid lang, Model model) {
        Property typeProp = model.getProperty(RDF_NS+ "type");
        switch (lang.getClass().getSimpleName()) {
            case "Dialect": {
                langResource.addProperty(typeProp, MOLA_NS + "Dialect");
                break;
            }
            case "Ethnolect": {
                langResource.addProperty(typeProp, MOLA_NS + "Ethnolect");
                break;
            }
            case "Sociolect": {
                langResource.addProperty(typeProp, MOLA_NS + "Sociolect");
                break;
            }
            case "Idiolect": {
                langResource.addProperty(typeProp, MOLA_NS + "Idiolect");
                break;
            }
            case "Pidgin": {
                langResource.addProperty(typeProp, MOLA_NS + "Pidgin");
                break;
            }
        }
    }

    private static void addLanguageAndTemplateFragments(Resource templateRes, Template template, String templatesURI, Model model) {
        //creating language
        Property generateProp = model.getProperty(ToCT_NS + "supportsLanguage");
        Languoid lang = template.getLanguage();
        Resource langRes = model.createResource(templatesURI + lang.getSerialisedName());
        addMolaType(langRes, lang, model);
        templateRes.addProperty(generateProp, langRes);

        List<TemplatePortion> portions = template.words;
        List<Resource> wordResources = new ArrayList<>(portions.size());

        //creating the first word
        TemplatePortion firstWord = portions.get(0);
        Resource firstItemRes = model.createResource(templatesURI + firstWord.getSerialisedName());
        attachValueAndLabel(firstItemRes, firstWord, templatesURI, model);
        wordResources.add(firstItemRes);
        Property firstItemPropToct = model.getProperty(ToCT_NS + "hasFirstPart");
        templateRes.addProperty(firstItemPropToct, firstItemRes);
        addPolymorphicWordFragments(firstItemRes, firstWord, templatesURI, model);

        //creating middle words
        for (int index = 1; index < portions.size()-1; ++index) {
            TemplatePortion midWord = portions.get(index);
            Resource midWordRes = model.createResource(templatesURI + midWord.getSerialisedName());
            attachValueAndLabel(midWordRes, midWord, templatesURI, model);
            wordResources.add(midWordRes);
            addPolymorphicWordFragments(midWordRes, midWord, templatesURI, model);
        }

        //creating the last word
        TemplatePortion lastWord = portions.get(portions.size()-1);
        Resource lastItemRes = model.createResource(templatesURI + firstWord.getSerialisedName());
        attachValueAndLabel(lastItemRes, lastWord, templatesURI, model);
        wordResources.add(lastItemRes);
        addPolymorphicWordFragments(lastItemRes, lastWord, templatesURI, model);

        //setting the order of the words
        for (int index=0; index < wordResources.size()-1; ++index) {
            Resource curr = wordResources.get(index);
            Resource next = wordResources.get(index+1);
            Property nextWordToCtProp = model.getProperty(ToCT_NS + "hasNextPart");
            curr.addProperty(nextWordToCtProp, next);
        }
    }

    private static void addPolymorphicWordFragments(Resource wordRes, TemplatePortion word, String templatesURI, Model model) {
        if (word instanceof PolymorphicWord) {
            Property typeProp = model.getProperty(RDF_NS+ "type");

            PolymorphicWord castWord = (PolymorphicWord) word;
            List<InternalSlotRootAffix> affixes = castWord.getAllMorphemes();
            List<Resource> affixesRes = new LinkedList<>();
            for (int index=0; index < affixes.size(); ++index) {
                InternalSlotRootAffix affix = affixes.get(index);
                Resource affixRes = model.createResource(templatesURI + affix.getSerialisedName());
                affixRes.addProperty(typeProp, ToCT_NS + affix.getType());
                if (index == 0) {
                    Property firstItemPropToct = model.getProperty(ToCT_NS + "hasFirstPart");
                    wordRes.addProperty(firstItemPropToct, affixRes);
                }
                else if (index == affixes.size()-1) {
                    Property lastItemPropToct = model.getProperty(ToCT_NS + "hasLastPart");
                    wordRes.addProperty(lastItemPropToct, affixRes);
                }
                affixesRes.add(affixRes);
                attachValueAndLabel(affixRes, affix, templatesURI, model);
            }
            for (int index=0; index < affixes.size()-1; ++index) {
                Resource curr = affixesRes.get(index);
                Resource next = affixesRes.get(index+1);
                Property nextWordToCtProp = model.getProperty(ToCT_NS + "hasNextPart");
                curr.addProperty(nextWordToCtProp, next);
            }
        }
    }

    private static void attachValueAndLabel(Resource wordResource, InternalSlotRootAffix word, String templatesURI, Model model) {
        Property valueProp = model.getProperty(templatesURI + "hasValue");
        String value = word.getValue();
        if (value != null && !value.isEmpty()) {
            wordResource.addLiteral(valueProp, value);
        }

        if (word instanceof Slot) {
            Property hasLabel = model.getProperty(templatesURI + "hasLabel");
            wordResource.addLiteral(hasLabel, ((Slot) word).getLabel());
        }
        if (word instanceof Concord) {
            Property hasLabel = model.getProperty(templatesURI + "hasLabel");
            wordResource.addLiteral(hasLabel, ((Concord) word).getLabel());
        }
    }
}
