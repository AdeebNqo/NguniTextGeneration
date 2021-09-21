package za.co.mahlaza.research;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import za.co.mahlaza.research.errordetection.models.TemplateValidationReport;
import za.co.mahlaza.research.errordetection.models.Report;
import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TemplateErrorDetector {

    private int DEFAULT_TIMEOUT = 300000;
    private final String ontologyPath;
    private OWLReasonerFactory chosenReasonerFactory;

    public TemplateErrorDetector(String ontologyPath) {
        this(null, ontologyPath);
    }
    public TemplateErrorDetector(OWLReasonerFactory reasonerFactory, String ontologyPath) {
        this.ontologyPath = ontologyPath;
        chosenReasonerFactory = reasonerFactory;
    }

    private Report validateTemplateViaReasoner(String templatePath) throws Exception {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        OWLOntology tboxModel = manager.loadOntologyFromOntologyDocument(new File(ontologyPath));
        OWLOntology aboxModel = manager.loadOntologyFromOntologyDocument(new File(templatePath));
        manager.addAxioms(tboxModel, aboxModel.getAxioms());

        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor, DEFAULT_TIMEOUT);

        if (chosenReasonerFactory == null) {
            //using default reasoner Fact++ 1.6.5
            chosenReasonerFactory = new FaCTPlusPlusReasonerFactory();
        }
        OWLReasoner reasoner = chosenReasonerFactory.createReasoner(tboxModel, config);

        boolean status = reasoner.isConsistent();
        Report statusReport = new Report(status);
        if (!status) {
            Node<OWLClass> classes = reasoner.getUnsatisfiableClasses();
            for (OWLClass cls: classes.getEntitiesMinusBottom()) {
                String msg = cls.toString() + "Not satisfiable.";
                statusReport.addMessage(msg);
            }
        }
        return statusReport;
    }

    public TemplateValidationReport getConsistencyReport(String templatePath) throws Exception {
        Report report = validateTemplateViaReasoner(templatePath);
        return new TemplateValidationReport(templatePath, report);
    }

    public Collection<TemplateValidationReport> getConsistencyReports(List<String> templates) throws Exception {
        Collection<TemplateValidationReport> items = new ArrayList<>();
        for (String templatePath : templates) {
            Report report = validateTemplateViaReasoner(templatePath);
            items.add(new TemplateValidationReport(templatePath, report));
        }
        return items;
    }

    public boolean hasInconsistencies(String templatePath) throws Exception {
        Report report = validateTemplateViaReasoner(templatePath);
        return report.isValid();
    }

    public boolean hasInconsistencies(List<String> templates) throws Exception  {
        Collection<TemplateValidationReport> itemReports = getConsistencyReports(templates);
        boolean hasInconsistencies = itemReports.stream().anyMatch(item -> item.isInconsistent());
        return hasInconsistencies;
    }

}
