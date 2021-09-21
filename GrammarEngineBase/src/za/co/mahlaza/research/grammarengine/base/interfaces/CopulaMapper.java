package za.co.mahlaza.research.grammarengine.base.interfaces;

import za.co.mahlaza.research.grammarengine.base.models.feature.NounClass;
import za.co.mahlaza.research.grammarengine.base.models.interfaces.InternalSlotRootAffix;

public interface CopulaMapper {
    String getCopulaValue(InternalSlotRootAffix nextAffix, NounClass nounClass);
    String getCopulaValue(InternalSlotRootAffix nextAffix);
}
