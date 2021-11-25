#Nguni Text generation code

0. Open the  project using Intellij Idea.

#Building the GITemplateParsing, GrammarEngineBase, GrammarEngineNguni, and TemplErrorDetection

1. From the main menu, select Build | Build Artifacts.
2. Point to the created .jar (e.g., GrammarEngineBase:jar) and select Build.
3. If you now look at the out/artifacts folder, you'll find your .jar file there.
4. Add the jar to your project.

#Loading template 

Here's an example of a method, taken from the OWLSIZ verbaliser, that demonstrates how to load a template from a file.

    public static Template getTemplate(String templateName, String templateURI,  String templatePath) throws UnsupportedOperationException {
        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        TemplateReader.IS_DEBUG_ENABLED = true;
        Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);
        return template;
    }
