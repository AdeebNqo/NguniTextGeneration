package za.co.mahlaza.research.grammarengine.nguni.zu;

import za.co.mahlaza.research.grammarengine.base.interfaces.CopulaMapper;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;

public class ZuluCopulaMapper implements CopulaMapper {
    @Override
    public String getCopulaValue(InternalSlotRootAffix nextAffix, NounClass nounClass) {
        if (nounClass.getNounClass().equals("11")) {
                return "w";
        }
        return getCopulaValue(nextAffix);
    }

    @Override
    public String getCopulaValue(InternalSlotRootAffix nextAffix) {
        String nextAffixValue = nextAffix.getValue();
        if (nextAffixValue.startsWith("u") || nextAffixValue.startsWith("o") || nextAffixValue.startsWith("a")) {
            return "ng";
        }
        return "y";
    }
}
