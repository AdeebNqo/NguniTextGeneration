package za.co.mahlaza.research.grammarengine.base.interfaces;

import java.io.OutputStream;
import java.util.List;

public interface ErrorFormatter {
    void setLogicalErrors(List<Object> logicalErrors);
    List<Object> getErrorMessages();
    void writeErrorMessages(OutputStream outStream);
}
