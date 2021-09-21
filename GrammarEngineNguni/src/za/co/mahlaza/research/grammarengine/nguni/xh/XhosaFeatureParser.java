package za.co.mahlaza.research.grammarengine.nguni.xh;

import za.co.mahlaza.research.grammarengine.base.models.feature.ConcordType;
import za.co.mahlaza.research.grammarengine.base.models.feature.Feature;
import za.co.mahlaza.research.grammarengine.base.models.feature.FeatureParser;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.templateparsing.URIS;

import java.util.LinkedList;
import java.util.List;

//TODO: use OWLAPI for this class
public class XhosaFeatureParser extends FeatureParser {



    @Override
    public List<String> getRegisteredFeatureProps() {
        List<String> registeredProps = new LinkedList<>();
        registeredProps.add(URIS.NCS_ANNOT_URI + "hasNounClass");
        registeredProps.add(URIS.CAO_ANNOT_NS + "hasConcordType");
        //registeredProps.add(URI + "hasPOS");
        return registeredProps;
    }

    @Override
    public Feature getFeature(String featureType) {
        if (featureType.startsWith("Class")) {
            String classNumber = featureType.replace("Class", "");
            return NounClass.getNounClass(classNumber);
        }
        else if (featureType.endsWith("Concord")) {
            return ConcordType.getConcordType(featureType);
        }
        return null;
    }

}
