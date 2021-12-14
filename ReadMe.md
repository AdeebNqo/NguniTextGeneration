# Nguni Text generation code

0. Open the  project using Intellij Idea.

# Building the modules

1. From the main menu, select Build | Build Artifacts.
2. Point to the created .jar (e.g., GrammarEngineBase:jar) and select Build.
3. If you now look at the out/artifacts folder, you'll find your .jar file there.
4. Add the jar to your project.

# Loading a template from a file

Here's an example of a method, taken from the OWLSIZ verbaliser, that demonstrates how to load a template from a file.

    public static Template getTemplate(String templateName, String templateURI,  String templatePath) throws UnsupportedOperationException {
        TemplateReader.Init(new ZuluFeatureParser());
        TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
        TemplateReader.IS_DEBUG_ENABLED = true;
        Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);
        return template;
    }

In the event that the above code is a bit confusing, then consider the following:

Suppose that you have created a template, serialised it using turtle, and stored it in a file called `Templates/template1.1.ttl`. Inside the template, you have instance of a ToCT template that you have called `templ1.1`. Then in order to load it, you would use the following:


    String templatePath = "Templates/template1.1.ttl";
    String templateName = "templ1.1";
    String templateURI = "http://people.cs.uct.ac.za/~zmahlaza/templates/owlsiz/";
    TemplateReader.Init(new ZuluFeatureParser());
    TemplateReader.setTemplateOntologyNamespace(URIS.ToCT_NS);
    Template template = TemplateReader.parseTemplate(templateName, templateURI, templatePath);

You can then do whatever you want with the template object. Please note that `templateURI` need not be the same as you may chosen a different one.

You can manipulate the template in memory and then save a template object (or template objects) to a file in the following matter:
 
        String templateURI = ...
      
        Template template = ...
        Collection<Template> templates = ...

        String outputFilename1 = "/tmp/sometemplate.ttl";
        String outputFilename2 = "/tmp/sometemplates.ttl";
        
        TemplateWriter.saveTemplate(template, templateURI, outputFilename1);
        TemplateWriter.saveTemplates(template, templateURI, outputFilename2);
        
# Loading a template from a file with no preset URI

In the event that you want to parse a template file, that may include multiple templates, but do not want to hardcode the templates' URI then you can retrieve your templates in the following manner:

    String templateFilename = ...
    Collection<String> templateURIs = TemplateReader.getTemplateURIs(templateFilename)
    for (String uri: uris) {
        Collection<Template> templates = TemplateReader.parseTemplates(uri, templateFilename);
    }

# Which module to add to your code

While the above snippet only langauge specific classes (e.g., `ZuluFeatureParser`), you may have no need for such code; hence, its important to know which module to build and load into your code.

![NguniTextGenerationModules](https://user-images.githubusercontent.com/2272078/143596408-27455ca4-8c47-472b-9d22-e1be57c3e0a4.png)

If you do not want to make use of the Nguni grammar rules and only want to parse (and save) ToCT templates (for whatever reason) then you can the template *TemplateParsing module. If you also need to make use of the Nguni grammar then import the *EngineNguni module only.

By default, we do not offer a way to build a jar for the GrammarEngineBase module since you almost never use it directly. However, if you need to use the module's code without the other components provided by the high-level modules (colored green in the figure above) then you can setup the jar yourself (see https://stackoverflow.com/a/4901370)
