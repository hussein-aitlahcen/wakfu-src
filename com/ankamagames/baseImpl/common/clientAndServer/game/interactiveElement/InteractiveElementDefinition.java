package com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;

public interface InteractiveElementDefinition<T extends MapInteractiveElement> extends ExportableEnum
{
    short getFactoryId();
    
    ObjectFactory<T> getFactory();
}
