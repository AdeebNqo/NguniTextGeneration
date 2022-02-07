package za.co.mahlaza.research.grammarengine.base.interfaces;

public interface NounClassPrefixResolver {
    String getFullPrefix(String nounClass) throws Exception;
    String getBasicPrefix(String nounClass) throws Exception;
}
