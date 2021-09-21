package za.co.mahlaza.research.grammarengine.base.interfaces;

import za.co.mahlaza.research.grammarengine.base.models.feature.ConcordType;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;

public interface ConcordMapper {
    String getConcordValue(NounClass someNounClass, ConcordType someConcordType);
    String getConcordValue(NounClass someNounClass, ConcordType someConcordType, String nextMorpheme);
}