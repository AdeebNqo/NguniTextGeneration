package za.co.mahlaza.research.grammarengine.nguni.zu;

import za.co.mahlaza.research.grammarengine.base.interfaces.NounClassPrefixResolver;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ZuluNounClassPrefixResolver implements NounClassPrefixResolver {

    HashMap<String, String> nounClasses = new HashMap<>();

    public ZuluNounClassPrefixResolver () {
        nounClasses.put("1", "umu/um");
        nounClasses.put("1a", "u");
        nounClasses.put("2", "aba/abe");
        nounClasses.put("2a", "o");
        nounClasses.put("3", "umu/um");
        nounClasses.put("4", "imi");
        nounClasses.put("5", "ili/i");
        nounClasses.put("6", "ama");
        nounClasses.put("7", "isi");
        nounClasses.put("8", "izi");
        nounClasses.put("9", "in/im");
        nounClasses.put("10", "izin/izim");
        nounClasses.put("11", "ulu/u");
        nounClasses.put("14", "ubu");
        nounClasses.put("15", "uku");
        nounClasses.put("17", "uku");
    }

    @Override
    public String getFullPrefix(String nounClass) {
        if (nounClasses.containsKey(nounClass)) {
            return nounClasses.get(nounClass);
        }
        else {
            throw new IllegalArgumentException("Cannot resolve a prefix for the noun class "+nounClass);
        }
    }

    @Override
    public String getBasicPrefix(String nounClass) throws Exception {
        String basicPrefix = "";

        String fullPrefix = getFullPrefix(nounClass);
        String[] prefixValues = fullPrefix.split("/");
        if (prefixValues.length == 1) {
            if (fullPrefix.length() < 2) {
                basicPrefix = fullPrefix;
            }
            else if (fullPrefix.matches("[aeiou].*")) {
                basicPrefix = fullPrefix.substring(1);
            }
        }
        else {
            String[] updatedPrefixVals = new String[prefixValues.length];
            final int[] index = {0};
            Stream.of(prefixValues).forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if (s.length() < 2) {
                        updatedPrefixVals[index[0]] = s;
                    }
                    else if (s.matches("[aeiou].*")) {
                        updatedPrefixVals[index[0]] = s.substring(1);
                    }
                    index[0] += 1;
                }
            });
            basicPrefix = String.join("/", updatedPrefixVals);
        }

        return basicPrefix;
    }
}
