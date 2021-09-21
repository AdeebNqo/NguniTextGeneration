package za.co.mahlaza.research.errordetection.models;

public class TemplateValidationReport {
    private String templatePath;
    private Report report;

    public TemplateValidationReport(String templatePath, Report report) {
        this.templatePath = templatePath;
        this.report = report;
    }

    public boolean isInconsistent() {
        return !report.isValid();
    }

    public String toString () {
        return "ValidationItem = { \n" +
                "\ttemplate = "+ templatePath +
                "\n" +
                report.toString() +
                "}";
    }
}
