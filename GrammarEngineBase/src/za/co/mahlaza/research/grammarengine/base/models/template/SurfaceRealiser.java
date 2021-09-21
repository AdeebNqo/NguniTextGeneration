package za.co.mahlaza.research.grammarengine.base.models.template;

import za.co.mahlaza.research.grammarengine.base.interfaces.ErrorFormatter;
import za.co.mahlaza.research.grammarengine.base.interfaces.Filter;
import za.co.mahlaza.research.grammarengine.base.interfaces.LogicalErrorDetection;
import za.co.mahlaza.research.grammarengine.base.interfaces.TemplateLinearizer;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SurfaceRealiser {

    private Filter templateFilter;
    private TemplateLinearizer currLinearizer;

    private LogicalErrorDetection errorDetection;
    private ErrorFormatter errorFormatter;

    public SurfaceRealiser(Filter templateFilter, TemplateLinearizer linearizer, LogicalErrorDetection someErrDetector, ErrorFormatter someErrFormatter) {
        this.templateFilter = templateFilter;
        currLinearizer = linearizer;

        errorDetection = someErrDetector;
        errorFormatter = someErrFormatter;
    }

    public SurfaceRealiser(TemplateLinearizer linearizer, LogicalErrorDetection someErrDetector, ErrorFormatter someErrFormatter) {
        this(null, linearizer, someErrDetector, someErrFormatter);
    }

    public SurfaceRealiser(TemplateLinearizer linearizer) {
        this(linearizer, null, null);
    }

    public String generateText(SlotValues slotValues, List<Template> templates) {
        Template template = templateFilter.filterTemplates(templates);
        return currLinearizer.linearize(template, slotValues);
    }

    public String generateText(SlotValues slotValues, Template template) {
        return currLinearizer.linearize(template, slotValues);
    }

    public boolean hasErrors() {
        return !getFormatttedErrors().isEmpty();
    }

    public List<Object> getFormatttedErrors() {
        List<Object> formattedErrors = new ArrayList<>();
        if (errorDetection != null) {
            List<Object> logicalErrors = new ArrayList<>();
            logicalErrors = errorDetection.detectErrors();
            errorFormatter.setLogicalErrors(logicalErrors);
            formattedErrors = errorFormatter.getErrorMessages();
        }
        return formattedErrors;
    }

    public void writeFormatttedErrors(OutputStream stream) {
        List<Object> logicalErrors = errorDetection.detectErrors();
        errorFormatter.setLogicalErrors(logicalErrors);
        errorFormatter.writeErrorMessages(stream);
    }
}
