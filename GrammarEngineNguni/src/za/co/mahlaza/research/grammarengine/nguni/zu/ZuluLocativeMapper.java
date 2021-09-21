package za.co.mahlaza.research.grammarengine.nguni.zu;

import za.co.mahlaza.research.grammarengine.base.interfaces.LocativeMapper;
import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;

public class ZuluLocativeMapper implements LocativeMapper {
    @Override
    public String getLocativeValue(InternalSlotRootAffix nextAffix, NounClass nounClass) {
        String nc = nounClass.getNounClass();
        if (nc.equals("1") || nc.equals("2") || nc.equals("1a") || nc.equals("2a")) {
            return "ku";
        } else {
            return "e";
        }
    }

    @Override
    public String getLocativeValue(InternalSlotRootAffix nextAffix) {
        //TODO: 
        return null;
    }
}
