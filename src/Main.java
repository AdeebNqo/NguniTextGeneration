import za.co.mahlaza.research.TemplateErrorDetector;
import za.co.mahlaza.research.errordetection.models.TemplateValidationReport;
import za.co.mahlaza.research.grammarengine.base.models.template.Phrase;
import za.co.mahlaza.research.grammarengine.base.models.template.Punctuation;
import za.co.mahlaza.research.grammarengine.base.models.template.Template;
import za.co.mahlaza.research.grammarengine.base.models.template.TemplatePortion;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluFeatureParser;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluMorphophonoAlternator;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluUtils;
import za.co.mahlaza.research.templateparsing.TemplateReader;
import za.co.mahlaza.research.templateparsing.TemplateWriter;
import za.co.mahlaza.research.templateparsing.URIS;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
//            testParseTemplate();
//            testParseTemplates();
//            testNextPartInATemplate();
//            testZuluUtils();
//            testSaveTemplate();
//            testGetTemplateURIs();
//            testSaveShortTemplate();
//            testMorphemeStopFormation();
            testNasalConditioning();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testNasalConditioning() {
        String lMorpheme = "ezim";
        String rMorpheme = "ne";
        System.out.println("Testing the combination of "+lMorpheme+" and "+rMorpheme);

        ZuluMorphophonoAlternator morphophonoAlternator = new ZuluMorphophonoAlternator();
        try {
            System.out.println("Outcome = "+morphophonoAlternator.joinMorpheme(lMorpheme, rMorpheme));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testMorphemeStopFormation() {
        String lMorpheme = "na";
        String rMorpheme = "hlanu";
        System.out.println("Testing the combination of "+lMorpheme+" and "+rMorpheme);

        ZuluMorphophonoAlternator morphophonoAlternator = new ZuluMorphophonoAlternator();
        try {
            System.out.println("Outcome = "+morphophonoAlternator.joinMorpheme(lMorpheme, rMorpheme));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testGetTemplateURIs() {
        String templatePath = "/tmp/template1.1.ttl";
        Collection<String> uris = TemplateReader.getTemplateURIs(templatePath);
        for (String uri: uris) {
            System.out.println(uri);
        }
    }

    public static void testSaveShortTemplate() {
        System.out.println("INSide testSaveShortTemplate");
        String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/testing/";
        List<TemplatePortion> words = new LinkedList<>();

        Punctuation p1 = new Punctuation(","); p1.setSerialisedName("p1");
        Punctuation p2 = new Punctuation("."); p2.setSerialisedName("p888");
        p1.setNextWordPart(p2);
        words.add(p1);
        words.add(p2);

        Phrase p3 = new Phrase("Hello"); p3.setSerialisedName("somePhrase");
        words.add(p3);

        Template templ = new Template(words);
        templ.setURI(templateURI);
        templ.setSerialisedName("templateTest");

        List<Template> templates = new LinkedList<>();
        templates.add(templ);

        PrintWriter writer = new PrintWriter(System.out);
        try {
            TemplateWriter.saveTemplates(templates, templateURI, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testSaveTemplate() throws Exception {
        String templatePath = "/home/zola/Documents/ToCT Ontology and Code-ArchiveOCt2021/OWLSIZ/Templates/template1.1.ttl";
        String templateName = "templ1.1";
        String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/";

        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);

        String outputFilename = "/tmp/testtemplate1.1.ttl";
        TemplateWriter.saveTemplate(template, templateURI, outputFilename);
    }

    public static void testZuluUtils () {
        String noun = "ukudla";
        ZuluUtils zuluUtils = new ZuluUtils();
        System.out.println(noun+" is a noun = "+zuluUtils.isNoun(noun));
    }

    public static void testNextPartInATemplate() throws Exception {
        String templatePath = "/home/zola/Documents/ToCT Ontology and Code-ArchiveOCt2021/OWLSIZ/Templates/template1.1.ttl";
        String templateName = "templ1.1";
        String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/";

        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);
        System.out.println("Second template portion = "+template.words.get(0).getNextWordPart());
        System.out.println("Second part of the first poly. word = "+template.getPolymorphicWords().get(0).getNextWordPart());
    }

    public static void testParseTemplate() throws Exception {
        String templatePath = "/home/zola/Documents/ToCT Ontology and Code-ArchiveOCt2021/OWLSIZ/Templates/template1.1.ttl";
        String templateName = "templ1.1";
        String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/";

        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);
    }

    public static void testParseTemplates() throws Exception {
        String templatePath = "/home/zola/Documents/ToCT Ontology and Code-ArchiveOCt2021/OWLSIZ/Templates/template1.1.ttl";
        String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/";

        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        Collection<Template> templates = TemplateReader.parseTemplates(templateURI, templatePath);
        for (Template template : templates) {
            System.out.println("Template -> "+template);
        }
    }


    public static Template getTemplate(String templateName, String templateURI, String templatePath) throws Exception {
        //TODO: find a way to handle features better. There could be different kinds of features. Perhaps need to register different parsers?
        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);
        return template;
    }

    public static void testErrDetect() {
        String ontologyPath = "/tmp/ToCT/ToCT.owl";
        String templatePath = "/tmp/ToCT/IsiXhosa GaliWeather Templates/Template14.ttl";
        TemplateErrorDetector detector = new TemplateErrorDetector(ontologyPath);
        try {
            System.out.println("Detecting inconsistencies...");
            TemplateValidationReport item = detector.getConsistencyReport(templatePath);
            System.out.println(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
