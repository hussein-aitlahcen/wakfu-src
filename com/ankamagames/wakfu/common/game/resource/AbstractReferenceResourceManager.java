package com.ankamagames.wakfu.common.game.resource;

import gnu.trove.*;

public class AbstractReferenceResourceManager<ReferenceRessource extends AbstractReferenceResource>
{
    protected final TIntObjectHashMap<ReferenceRessource> m_referenceRessources;
    
    public AbstractReferenceResourceManager() {
        super();
        this.m_referenceRessources = new TIntObjectHashMap<ReferenceRessource>();
    }
    
    public void addReferenceResource(final ReferenceRessource referenceRessource) {
        this.m_referenceRessources.put(referenceRessource.getId(), referenceRessource);
    }
    
    public ReferenceRessource getReferenceResource(final int id) {
        return this.m_referenceRessources.get(id);
    }
    
    public boolean isEmpty() {
        return this.m_referenceRessources.isEmpty();
    }
    
    public TIntObjectIterator<ReferenceRessource> iterator() {
        return this.m_referenceRessources.iterator();
    }
}
