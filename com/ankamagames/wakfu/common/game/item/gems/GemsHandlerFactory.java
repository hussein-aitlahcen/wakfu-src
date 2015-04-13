package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public class GemsHandlerFactory
{
    public static Gems create(final AbstractReferenceItem holderDefinition) {
        return new Gems(holderDefinition);
    }
    
    public static Gems copy(final AbstractReferenceItem holderDefinition, final Gems gemsHandler) {
        return new Gems(holderDefinition, gemsHandler);
    }
    
    public static Gems create(final AbstractReferenceItem holderDefinition, final RawGems rawGems) {
        final Gems gemsHandler = new Gems(holderDefinition);
        gemsHandler.fromRaw(rawGems);
        return gemsHandler;
    }
}
