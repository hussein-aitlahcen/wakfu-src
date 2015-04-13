package com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.common.*;

public interface ClientInteractiveElementViewDefinition extends ExportableEnum
{
    short getViewFactoryId();
    
    ObjectFactory<ClientInteractiveElementView> getViewFactory();
}
