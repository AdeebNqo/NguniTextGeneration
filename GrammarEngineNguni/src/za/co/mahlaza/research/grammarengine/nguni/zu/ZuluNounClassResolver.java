package za.co.mahlaza.research.grammarengine.nguni.zu;

import za.co.mahlaza.research.grammarengine.base.interfaces.NounClassResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

//TODO: implement a statical noun class resolver and use it here
public class ZuluNounClassResolver implements NounClassResolver {

    //The file we used is a cleaned version of MK's nncPairs. In particular, we have done the following things
    //ufulawa,3m turned into ufulawa,3a
    //amanzi,6m turned into amanzi,6
    //irhaba,9a turned into irhaba,9
    //ivazi,9a turned into ivazi,9
    //ithiyetha yokuhlinzela,9a turned into ithiyetha yokuhlinzela,9
    //indoda,6 turned into indoda,5
    //added umakhalekhukhwini,1a
    //added uZola,1a
    //added iNokia 3310,5
    //added ifoni,5
    //added umfundi,1
    //added ukubhukuda,15
    //added ihebhu,5
    //added umdlalo,3
    private String path;
    private HashMap<String, String> nounValues = new HashMap<>();

    public ZuluNounClassResolver(String path) {
        this.path = path;
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(line -> {
                String[] vals = line.split(",");
                nounValues.put(vals[0], vals[1]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNoun(String noun) {
        return nounValues.containsKey(noun);
    }

    public String getNounClass(String noun) {
        return nounValues.containsKey(noun) ? nounValues.get(noun) : null;
    }
}
