# Nguni Text generation code

0. Open the  project using Intellij Idea.

# Building the GITemplateParsing, GrammarEngineBase, GrammarEngineNguni, and TemplErrorDetection

1. From the main menu, select Build | Build Artifacts.
2. Point to the created .jar (e.g., GrammarEngineBase:jar) and select Build.
3. If you now look at the out/artifacts folder, you'll find your .jar file there.
4. Add the jar to your project.

# Loading template 

Here's an example of a method, taken from the OWLSIZ verbaliser, that demonstrates how to load a template from a file.

    public static Template getTemplate(String templateName, String templateURI,  String templatePath) throws UnsupportedOperationException {
        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        TemplateReader.IS_DEBUG_ENABLED = true;
        Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);
        return template;
    }

In the event that the above code is a bit confusing, then consider the following:

Support you have created a template, serialised it using turtle, and stored it in a file called `Templates/template1.1.ttl`. Inside the template, you have instance of a ToCT template that you have called `templ1.1`. Then in order to load it, you would use the following:


    String templatePath = "Templates/template1.1.ttl";
    String templateName = "templ1.1";
    String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/";

TemplateReader.Init(new ZuluFeatureParser());
TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);

You can then do whatever you want with the template object. Please note that `templateURI` need not be the same as you may chosen a different one.