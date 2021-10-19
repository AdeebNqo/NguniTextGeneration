import za.co.mahlaza.research.TemplateErrorDetector;
import za.co.mahlaza.research.errordetection.models.TemplateValidationReport;
import za.co.mahlaza.research.grammarengine.base.models.template.Template;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluFeatureParser;
import za.co.mahlaza.research.grammarengine.nguni.zu.ZuluUtils;
import za.co.mahlaza.research.templateparsing.TemplateReader;
import za.co.mahlaza.research.templateparsing.URIS;

public class Main {

    public static void main(String[] args) {
        testParseTemplate();
        testZuluUtils();

    }
    public static void testZuluUtils () {
        String noun = "ukudla";
        ZuluUtils zuluUtils = new ZuluUtils();
        System.out.println(noun+" is a noun = "+zuluUtils.isNoun(noun));
    }
    public static void testParseTemplate () {
        String templatePath = "/home/zola/Documents/ToCT Ontology and Code/OWLSIZ/Templates/template1.1.ttl";
        String templateName = "templ1.1";
        String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/";

        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);
    }


    public static Template getTemplate(String templateName, String templateURI, String templatePath) throws UnsupportedOperationException {
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
