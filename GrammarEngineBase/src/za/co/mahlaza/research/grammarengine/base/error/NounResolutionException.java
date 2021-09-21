package za.co.mahlaza.research.grammarengine.base.error;

public class NounResolutionException extends Exception {

    String noun;

    public NounResolutionException(String name) {
        super("Cannot resolve noun class of word = "+name);
        noun = name;
    }

    public String getNoun() {
        return noun;
    }
}
