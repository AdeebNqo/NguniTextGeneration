package za.co.mahlaza.research.errordetection.models;

import java.util.ArrayList;
import java.util.List;

public class Report {

    private boolean status;
    private List<String> messages;

    public Report(boolean status) {
        this.status = status;
        messages = new ArrayList<>();
    }

    public boolean isValid() {
        return status;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public String toString() {
        return "Report {" +
                "\tConsistent=" + status+", \n "+
                "\tmessages=" + String.join(", \n", messages) +
                "\n\t}";
    }
}
