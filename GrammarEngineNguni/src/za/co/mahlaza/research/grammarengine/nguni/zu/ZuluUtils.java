package za.co.mahlaza.research.grammarengine.nguni.zu;

import za.co.mahlaza.research.grammarengine.base.interfaces.LangUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class ZuluUtils implements LangUtils {

    final private String path = "/2010.07.17.ZuluSentencesPOStagged(Ukwabelana).txt";
    private HashMap<String, String> wordValues = new HashMap<>();

    public ZuluUtils() {
        InputStream iStream = ZuluUtils.class.getResourceAsStream(path);
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(iStream));

        Stream<String> stream = bufReader.lines();
        stream.forEach(line -> {
            String[] words = line.split(" ");
            for (String word : words) {
                String[] tokens = word.split("_");
                wordValues.put(tokens[0], tokens[1]);
            }
        });
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
