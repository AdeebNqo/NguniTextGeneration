package za.co.mahlaza.research.grammarengine.base.interfaces;

import java.util.List;

public interface LogicalErrorDetection {
    void setTemplatePath(String path);
    void setOntologyPath(String path);
    List<Object> detectErrors();
}
