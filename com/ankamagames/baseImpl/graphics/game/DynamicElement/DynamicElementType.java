package com.ankamagames.baseImpl.graphics.game.DynamicElement;

import com.ankamagames.framework.external.*;

public interface DynamicElementType extends ExportableEnum
{
    DynamicElementTypeProvider createProvider();
}
