import za.co.mahlaza.research.TemplateErrorDetector;
import za.co.mahlaza.research.errordetection.models.TemplateValidationReport;
import za.co.mahlaza.research.grammarengine.base.models.template.Template;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluFeatureParser;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluUtils;
import za.co.mahlaza.research.templateparsing.TemplateReader;
import za.co.mahlaza.research.templateparsing.URIS;

import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        try {
            testParseTemplate();
            testParseTemplates();
            testNextPartInATemplate();
            testZuluUtils();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        System.out.println("Second template portion = "+template.words.get(0).getNextPart());
        System.out.println("Second part of the first poly. word = "+template.getPolymorphicWords().get(0).getNextPart());
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
