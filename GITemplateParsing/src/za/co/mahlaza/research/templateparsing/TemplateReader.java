package za.co.mahlaza.research.templateparsing;

import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;
import za.co.mahlaza.research.grammarengine.base.models.feature.FeatureParser;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.Word;
import za.co.mahlaza.research.grammarengine.base.models.template.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import static za.co.mahlaza.research.templateparsing.URIS.*;

//TODO: Use OWLAPI when reading/parsing the data
public class TemplateReader {

    private static FeatureParser featureParser;
    private static TemplatePortion[] actualWords;
    private static String TEMPLATE_NAMESPACE = ToCT_NS;

    public static boolean IS_DEBUG_ENABLED = false;

    public static void Init(FeatureParser fp) {
        featureParser = fp;
    }


    public static void setTemplateOntologyNamespace(String namespace) {
        TEMPLATE_NAMESPACE = namespace;
    }

    public static Template parseTemplate(String templateName, String baseURI, String templateFilename) throws Exception {

        Model templateModel = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(templateFilename);
        templateModel.read(in, baseURI, "TTL"); //TODO: move the TLL to the params so we cant handle diff. serializations

        Template template = parseTemplate(templateName, baseURI, templateModel);
        //template.setUnderlyingModel(templateModel);

        return template;
    }

    public static Template parseTemplate(String templateName, String baseNamespace, Model model) throws Exception {
        Resource template = model.getResource(baseNamespace + templateName);

        if (!model.containsResource(template)) {
            throw new Exception("The specified file does not contain a template called "+templateName);
        }
        List<Resource> allWords = getAllSequentialResources(template, model);

        actualWords = new TemplatePortion[allWords.size()]; //TODO: figure out if the indexing of the words still makes sense

        //creating slot objects
        for (int i=0; i < allWords.size(); i++) {
            Resource currWordResource = allWords.get(i);
            String wordType = getResourceType (currWordResource, model);
            if (wordType.equals("Slot")) {
                TemplatePortion currWord =  getTemplatePortion(currWordResource, model);
                actualWords[i] = currWord;
            }
        }

        //creating all the other non-slot objects
        for (int i=0; i < allWords.size(); i++) {
            Resource currWordResource = allWords.get(i);
            String wordType = getResourceType (currWordResource, model);
            if (!wordType.equals("Slot")) {
                TemplatePortion currWord =  getTemplatePortion(currWordResource, model);
                actualWords[i] = currWord;
            }
        }

        //TODO: For unimorphic, find the polymorph it providesFor and add the unimorph to the

        //getting the template's language family
        //TODO: Read other aspects of the supported language
        Property generateProp = model.getProperty(TEMPLATE_NAMESPACE + "supportsLanguage");
        Property langFamilyProp = model.getProperty(MOLA_NS + "isFamily");
        Resource languageResource = (Resource) template.getProperty(generateProp).getObject();
        Resource languageFamilyResource = (Resource) languageResource.getProperty(langFamilyProp).getObject();
        String languageFamily = languageFamilyResource.getLocalName();

        //creating template
        List<TemplatePortion> templateWords = new ArrayList<>(Arrays.asList(actualWords));
        Template actualTemplate = new Template(templateWords);
        actualTemplate.setLanguageFamily(languageFamily);

        return actualTemplate;
    }

    public static List<SlotFiller> parseSlotFillers(String slotFillersFilename) {
        Model slotFillersModel = FileManager.get().loadModel(slotFillersFilename);
        return parseSlotFillers(slotFillersModel);
    }

    public static List<SlotFiller> parseSlotFillers(Model slotFillersModel) {
        List<SlotFiller> slotFillers = new LinkedList<>();

        ResIterator resIter = slotFillersModel.listResourcesWithProperty(null);
        while (resIter.hasNext()) {
            Resource slotFillerRes = resIter.nextResource();
            SlotFiller slotFiller = getSlotFillerObject(slotFillerRes, slotFillersModel);
            if (slotFiller != null) {
                slotFillers.add(slotFiller);
            }
        }

        return slotFillers;
    }


    public static List<Resource> getAllSequentialResources(Resource someResource, Model someModel) {
        Resource firstItem = getFirstResource(someResource, someModel);
        List<Resource> allItems = getItemsFromFirst(firstItem, someModel);
        Resource lastItem = getLastResource(someResource, someModel);
        if (!allItems.contains(lastItem)) {
            allItems.add(lastItem);
        }
        return allItems;
    }

    private static Resource getFirstResource(Resource container, Model model) {
        Property defaultProp = null;

        Property firstItemPropToct = model.getProperty(ToCT_NS + "hasFirstPart");
        Property firstItemPropCO = model.getProperty(CO_NS + "firstItem");

        if (container.hasProperty(firstItemPropCO)) {
            defaultProp = firstItemPropCO;
        }
        else if (container.hasProperty(firstItemPropToct)) {
            defaultProp = firstItemPropToct;
        }

        return getNthResource(defaultProp, container, model);
    }

    private static Resource getLastResource(Resource container, Model model) {

        Property defaultProp = null;

        Property lastItemPropToct = model.getProperty(ToCT_NS + "hasLastPart");
        Property lastItemPropCO = model.getProperty(CO_NS + "lastItem");

        if (container.hasProperty(lastItemPropCO)) {
            defaultProp = lastItemPropCO;
        }
        else if (container.hasProperty(lastItemPropToct)) {
            defaultProp = lastItemPropToct;
        }

        return getNthResource(defaultProp, container, model);
    }

    private static Resource getNthResource(Property itemProp, Resource container, Model model) {
        Resource item = null;

        if (container.hasProperty(itemProp)) {
            item = (Resource) container.getProperty(itemProp).getObject();
        }

        return item;
    }

    private static List<Resource> getItemsFromFirst(Resource firstResource, Model model) {

        Property nextWordToCtProp = model.getProperty(ToCT_NS + "hasNextPart");
        Property nextWordCOProp = model.getProperty(CO_NS + "nextItem");

        Property nextWordProp = null;
        if (firstResource.hasProperty(nextWordCOProp)) {
            nextWordProp = nextWordCOProp;
        }
        else if (firstResource.hasProperty(nextWordToCtProp)) {
            nextWordProp = nextWordToCtProp;
        }

        //initialising the items
        List<Resource> results = new LinkedList<>();
        //adding the first item
        results.add(firstResource);

        //adding the rest of the resources
        if (firstResource !=null && nextWordProp!=null) {

            Statement nxtprop = firstResource.getProperty(nextWordProp);

            if (nxtprop != null) {
                Resource nextWord = (Resource) nxtprop.getObject();
                while (nextWord != null) {

                    if (nextWord.hasProperty(nextWordCOProp)) {
                        nextWordProp = nextWordCOProp;
                    }
                    else if (nextWord.hasProperty(nextWordToCtProp)) {
                        nextWordProp = nextWordToCtProp;
                    }

                    results.add(nextWord);

                    if (nextWordProp != null && nextWord.hasProperty(nextWordProp)) {
                        Statement prop = nextWord.getProperty(nextWordProp);
                        nextWord = (Resource) prop.getObject();
                    } else {
                        nextWord = null;
                    }
                }
            }
        }
        return results;
    }

    private static String getResourceType (Resource someResource, Model model) {
        Property typeProp = model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        Resource typeOfWordResource =  (Resource) someResource.getProperty(typeProp).getObject();
        String typeOfWord = typeOfWordResource.getLocalName();
        return typeOfWord;
    }

    private static SlotFiller getSlotFillerObject(Resource someResource, Model model) {
        SlotFiller slotFiller = null;

        List<Feature> features = getPortionFeatures(model, someResource);
        String typeOfSlotFiller = getResourceType(someResource, model);

        switch (typeOfSlotFiller) {
            case "UnimorphicWord": {

                Property valueProp = model.getProperty(TEMPLATE_NAMESPACE + "hasValue");
                String value = someResource.getProperty(valueProp).getString();

                slotFiller = new UnimorphicWord(value);
                slotFiller.setFeatures(features);

                //retrieving all the slots that the words fillsIn
                Property fillsInProp = model.getProperty(TEMPLATE_NAMESPACE + "fillsIn");
                StmtIterator filledSlotResources = someResource.listProperties(fillsInProp);
                while (filledSlotResources.hasNext()) {
                    Resource slotResource = filledSlotResources.nextStatement().getObject().asResource();
                    slotFiller.addSlotItCanFill(slotResource.getLocalName());
                }

                //retrieving labels of polymorphs provided for by current work
                Property providesForProp = model.getProperty(TEMPLATE_NAMESPACE + "providesFor");
                if (someResource.hasProperty(providesForProp)) {
                    StmtIterator providesForResource = someResource.listProperties(providesForProp);
                    List<String> provideForList = new LinkedList<>();
                    while (providesForResource.hasNext()) {
                        Resource providesForRes = providesForResource.nextStatement().getObject().asResource();
                        provideForList.add(providesForRes.getLocalName());
                    }
                    ((UnimorphicWord) slotFiller).setProvidesForPolymorphResources(provideForList);
                }

                //retrieving labels for the concords controlled by the unimorph
                Property controlsProp = model.getProperty(TEMPLATE_NAMESPACE + "controls");
                if (someResource.hasProperty(controlsProp)) {
                    StmtIterator controlsResource = someResource.listProperties(controlsProp);
                    List<String> controlsList = new LinkedList<>();
                    while (controlsResource.hasNext()) {
                        Resource providesForRes = controlsResource.nextStatement().getObject().asResource();
                        controlsList.add(providesForRes.getLocalName());
                    }
                    ((UnimorphicWord) slotFiller).setControlsConcordResources(controlsList);
                }

                //((UnimorphicWord) slotFiller).setUnderlyingResource(someResource);
                ((UnimorphicWord) slotFiller).setIdentification(someResource.getLocalName());
                break;
            }
            case "Phrase": {
                Property valueProp = model.getProperty(TEMPLATE_NAMESPACE + "hasValue");

                if (someResource.hasProperty(valueProp)) {
                    String value = someResource.getProperty(valueProp).getString();
                    slotFiller = new Phrase(value);
                }
                else {
                    List<Resource> phraseItems = getAllSequentialResources(someResource, model);

                    List<Word> words = new LinkedList<>();
                    for (Resource res : phraseItems) {
                        TemplatePortion portion = getTemplatePortion(res, model);
                        words.add((Word) portion);
                    }
                    slotFiller = new Phrase(words);
                }

                slotFiller.setFeatures(features);

                //retrieving all the slots that the words fillsIn
                Property fillsInProp = model.getProperty(TEMPLATE_NAMESPACE + "fillsIn");
                if (someResource.hasProperty(fillsInProp)) {
                    StmtIterator filledSlotResources = someResource.listProperties(fillsInProp);
                    while (filledSlotResources.hasNext()) {
                        Resource slotResource = filledSlotResources.nextStatement().getObject().asResource();
                        slotFiller.addSlotItCanFill(slotResource.getLocalName());
                    }
                }
            }
        }
        return slotFiller;
    }


    private static TemplatePortion getTemplatePortion(Resource lexicalItemResource, Model model) {

        TemplatePortion finalWord = null;
        String typeOfWord = getResourceType(lexicalItemResource, model);

        List<Feature> features = getPortionFeatures(model, lexicalItemResource);

        switch(typeOfWord) {
            case "PolymorphicWord": {
                List<Resource> allMorphemes = getAllSequentialResources(lexicalItemResource, model);

                List<InternalSlotRootAffix> wordPortions = new LinkedList<>();
                for (Resource wordPortionRes : allMorphemes) {
                    InternalSlotRootAffix item = getMorpheme(wordPortionRes, lexicalItemResource, model);
                    if (item != null) {
                        wordPortions.add(item);
                    }
                }
                finalWord = new PolymorphicWord(wordPortions, features);

                //retrieving all the slots that the words fillsIn
                Property fillsInProp = model.getProperty(TEMPLATE_NAMESPACE + "fillsIn");
                if (lexicalItemResource.hasProperty(fillsInProp)) {
                    StmtIterator filledSlotResources = lexicalItemResource.listProperties(fillsInProp);
                    while (filledSlotResources.hasNext()) {
                        Resource slotResource = filledSlotResources.nextStatement().getObject().asResource();
                        ((PolymorphicWord) finalWord).addSlotItCanFill(slotResource.getLocalName());
                    }
                }

                //retrieving labels for unimophs for which current words dependsOn
                Property dependsOnProp = model.getProperty(TEMPLATE_NAMESPACE + "fillsIn");
                if (lexicalItemResource.hasProperty(dependsOnProp)) {
                    StmtIterator dependsOnResources = lexicalItemResource.listProperties(fillsInProp);
                    while (dependsOnResources.hasNext()) {
                        Resource slotResource = dependsOnResources.nextStatement().getObject().asResource();
                        ((PolymorphicWord) finalWord).addSlotItCanFill(slotResource.getLocalName());
                    }
                }

                Property reliesProp = model.getProperty(TEMPLATE_NAMESPACE + "reliesOn");
                if (lexicalItemResource.hasProperty(reliesProp)) {
                    StmtIterator reliesResource = lexicalItemResource.listProperties(reliesProp);
                    List<String> reliesOnList = new LinkedList<>();
                    while (reliesResource.hasNext()) {
                        reliesOnList.add(reliesResource.nextStatement().getObject().asResource().getLocalName());
                    }
                    ((PolymorphicWord) finalWord).setReliesOnLabels(reliesOnList);
                }

                //TODO: what if the inverse of "relies" is used

                //((PolymorphicWord) finalWord).setUnderlyingResource(lexicalItemResource);
                ((PolymorphicWord) finalWord).setIdentification(lexicalItemResource.getLocalName());
                break;
            }
            case "UnimorphicWord": {
                Property valueProp = model.getProperty(TEMPLATE_NAMESPACE + "hasValue");
                String value = lexicalItemResource.getProperty(valueProp).getString();

                finalWord = new UnimorphicWord(value);
                ((UnimorphicWord) finalWord).setFeatures(features);

                //retrieving all the slots that the words fillsIn
                Property fillsInProp = model.getProperty(TEMPLATE_NAMESPACE + "fillsIn");
                if (lexicalItemResource.hasProperty(fillsInProp)) {
                    StmtIterator filledSlotResources = lexicalItemResource.listProperties(fillsInProp);
                    while (filledSlotResources.hasNext()) {
                        Resource slotResource = filledSlotResources.nextStatement().getObject().asResource();
                        ((UnimorphicWord) finalWord).addSlotItCanFill(slotResource.getLocalName());
                    }
                }

                //retrieving labels of polymorphs provided for by current work
                Property providesForProp = model.getProperty(TEMPLATE_NAMESPACE + "providesFor");
                if (lexicalItemResource.hasProperty(providesForProp)) {
                    StmtIterator providesForResource = lexicalItemResource.listProperties(providesForProp);
                    List<String> provideForList = new LinkedList<>();
                    while (providesForResource.hasNext()) {
                        Resource providesForRes = providesForResource.nextStatement().getObject().asResource();
                        provideForList.add(providesForRes.getLocalName());
                    }
                    ((UnimorphicWord) finalWord).setProvidesForPolymorphResources(provideForList);
                }

                //retrieving labels for the concords controlled by the unimorph
                Property controlsProp = model.getProperty(TEMPLATE_NAMESPACE + "controls");
                if (lexicalItemResource.hasProperty(controlsProp)) {
                    StmtIterator controlsResource = lexicalItemResource.listProperties(controlsProp);
                    List<String> controlsList = new LinkedList<>();
                    while (controlsResource.hasNext()) {
                        Resource providesForRes = controlsResource.nextStatement().getObject().asResource();
                        controlsList.add(providesForRes.getLocalName());
                    }
                    ((UnimorphicWord) finalWord).setControlsConcordResources(controlsList);
                }

                //((UnimorphicWord) finalWord).setUnderlyingResource(lexicalItemResource);
                ((UnimorphicWord) finalWord).setIdentification(lexicalItemResource.getLocalName());
                break;
            }
            case "Slot": {
                Property finalLabel = model.getProperty(TEMPLATE_NAMESPACE + "hasLabel");

                String slotLabel = lexicalItemResource.getProperty(finalLabel).getString();
                finalWord = new Slot(slotLabel, features);

                Property reliesProp = model.getProperty(TEMPLATE_NAMESPACE + "reliesOn");
                if (lexicalItemResource.hasProperty(reliesProp)) {
                    StmtIterator reliesResource = lexicalItemResource.listProperties(reliesProp);
                    List<String> reliesOnList = new LinkedList<>();
                    while (reliesResource.hasNext()) {
                        reliesOnList.add(reliesResource.nextStatement().getObject().asResource().getLocalName());
                    }
                    ((Slot) finalWord).setReliesOnLabels(reliesOnList);
                }

                //TODO: what if the inverse of "relies" is used

                //((Slot) finalWord).setUnderlyingResource(lexicalItemResource);
                ((Slot) finalWord).setIdentification(lexicalItemResource.getLocalName());
                break;
            }
            case "Phrase": {
                SlotFiller item = getSlotFillerObject(lexicalItemResource, model);
                if (item instanceof Phrase) {
                    finalWord = (Phrase) item;
                }
                break;
            }
            case "Punctuation": {
                Property valueProp = model.getProperty(TEMPLATE_NAMESPACE + "hasValue");
                String value = lexicalItemResource.getProperty(valueProp).getString();
                finalWord = new Punctuation(value);
                break;
            }

            case "Space": {
                Property valueProp = model.getProperty(TEMPLATE_NAMESPACE + "hasValue");
                String value = lexicalItemResource.getProperty(valueProp).getString();
                finalWord = new Space(value);
                break;
            }

        }
        return finalWord;
    }


    private static InternalSlotRootAffix getMorpheme (Resource subLexicalResource, Resource lexicalResource,  Model model) {
        InternalSlotRootAffix finalMorpheme = null;
        
        Property typeProp = model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
        Resource typeOfMorphemeResource =  (Resource) subLexicalResource.getProperty(typeProp).getObject();
        String typeOfMorpheme = typeOfMorphemeResource.getLocalName();

        List<Feature> features = getPortionFeatures(model, subLexicalResource);

        switch (typeOfMorpheme) {
            case "Concord": {
                Property labelProp = model.getProperty(TEMPLATE_NAMESPACE + "hasLabel");
                if (subLexicalResource.hasProperty(labelProp)) {
                    String label = subLexicalResource.getProperty(labelProp).getString();

                    Concord concord = new Concord(label, features);
                    //concord.setUnderlyingResource(subLexicalResource);
                    concord.setIdentification(subLexicalResource.getLocalName());

                    finalMorpheme = concord;
                }
                break;
            }
            case "Root" : {
                Property valueProp = model.getProperty(TEMPLATE_NAMESPACE + "hasValue");
                String rootValue = subLexicalResource.getProperty(valueProp).getString();
                finalMorpheme = new Root(rootValue, features);
                break;
            }
            case "UnimorphicAffix" : {
                Property valueProp = model.getProperty(TEMPLATE_NAMESPACE + "hasValue");
                String value = subLexicalResource.getProperty(valueProp).getString();
                finalMorpheme = new UnimorphicAffix(value, features);
                break;
            }
            case "AffixChunk": {
                Property valueProp = model.getProperty(TEMPLATE_NAMESPACE + "hasValue");
                String value = subLexicalResource.getProperty(valueProp).getString();

                List<Affix> affixes = new LinkedList<>();
                Property containsProp = model.getProperty(TEMPLATE_NAMESPACE + "constitutes");
                if (subLexicalResource.hasProperty(containsProp)) {
                    StmtIterator containedResources = subLexicalResource.listProperties(containsProp);
                    if (containedResources != null) {
                        while (containedResources.hasNext()) {
                            Resource affixResource = containedResources.nextStatement().getObject().asResource();
                            Affix affix = (Affix) getMorpheme(affixResource, lexicalResource, model);
                            affixes.add(affix);
                        }
                    }
                }
                finalMorpheme = new AffixChunk(value, affixes, features);
                break;
            }
            case "Slot": {
                TemplatePortion tempPort = getTemplatePortion(subLexicalResource, model);
                if (tempPort instanceof Slot) {
                    finalMorpheme = (Slot) tempPort;
                }
                break;
            }
            case "Copula": {
                Property labelProp = model.getProperty(TEMPLATE_NAMESPACE + "hasLabel");
                if (subLexicalResource.hasProperty(labelProp)) {
                    String label = subLexicalResource.getProperty(labelProp).getString();

                    Copula concord = new Copula(label, features);
                    //concord.setUnderlyingResource(subLexicalResource);

                    finalMorpheme = concord;
                }
                break;
            }
            case "Locative": {
                Property labelProp = model.getProperty(TEMPLATE_NAMESPACE + "hasLabel");
                if (subLexicalResource.hasProperty(labelProp)) {
                    String label = subLexicalResource.getProperty(labelProp).getString();

                    Locative locPref = new Locative(label, features);
                    //locPref.setUnderlyingResource(subLexicalResource);

                    finalMorpheme = locPref;
                }
                break;
            }
        }

        return finalMorpheme;
    }

    private static List<Feature> getPortionFeatures (Model model, Resource someResource) {
        List<Feature> features = new LinkedList<>();
        if (featureParser != null) {
            List<String> featureProps = featureParser.getRegisteredFeatureProps();
            for (String featureProp: featureProps) {
                Property property = model.getProperty(featureProp);

                Statement stmt = someResource.getProperty(property);
                if (stmt != null) {
                    Resource labelResource = (Resource) stmt.getObject();
                    Property typeProp = model.getProperty(RDF_NS+"type");
                    String featureType = labelResource.getProperty(typeProp).getResource().getLocalName();

                    Feature feature = featureParser.getFeature(featureType);
                    if (feature != null) {
                        features.add(feature);
                    }
                }
            }
        }
        return features;
    }

}
