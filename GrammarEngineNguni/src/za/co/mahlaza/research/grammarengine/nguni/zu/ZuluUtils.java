package za.co.mahlaza.research.grammarengine.nguni.zu;

import za.co.mahlaza.research.grammarengine.base.interfaces.LangUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

public class ZuluUtils implements LangUtils {

    final private String path = "/home/zola/Documents/phd/work/tasks/OWLSIZ/verbaliser/2010.07.17.ZuluSentencesPOStagged(Ukwabelana).txt";
    private HashMap<String, String> wordValues = new HashMap<>();

    public ZuluUtils() {
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(line -> {
                String[] words = line.split(" ");
                for (String word : words) {
                    String[] tokens = word.split("_");
                    wordValues.put(tokens[0], tokens[1]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDeepPreposition(String OP) {
        if (OP.equals("ffff") || OP.equals("cccc") || OP.equals("dddd") || OP.equals("aaaa") | OP.equals("bbbb") || OP.equals("eeee")) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isNoun(String value) {
        //I added ingxenye in the file
        String wordType = wordValues.get(value);
        if (wordType == null) {
            return false;
        }
        return wordType.equals("n");
    }
}
