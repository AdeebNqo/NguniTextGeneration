package za.co.mahlaza.research.grammarengine.base.interfaces;

public interface NounClassResolver {
    boolean hasNoun(String noun) ;
    String getNounClass(String noun);
}
