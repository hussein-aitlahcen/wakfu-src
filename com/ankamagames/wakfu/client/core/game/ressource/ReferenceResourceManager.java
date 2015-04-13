package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.wakfu.common.game.resource.*;
import gnu.trove.*;

public class ReferenceResourceManager extends AbstractReferenceResourceManager<ReferenceResource>
{
    private static final ReferenceResourceManager m_instance;
    
    public static ReferenceResourceManager getInstance() {
        return ReferenceResourceManager.m_instance;
    }
    
    public boolean forEachResource(final TObjectProcedure<ReferenceResource> procedure) {
        return this.m_referenceRessources.forEachValue((TObjectProcedure<ReferenceRessource>)procedure);
    }
    
    static {
        m_instance = new ReferenceResourceManager();
    }
}
