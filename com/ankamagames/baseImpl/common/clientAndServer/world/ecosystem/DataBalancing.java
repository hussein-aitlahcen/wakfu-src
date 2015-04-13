package com.ankamagames.baseImpl.common.clientAndServer.world.ecosystem;

import org.jdom.*;

public class DataBalancing
{
    public final int m_id;
    public final int m_minQuantity;
    public final int m_maxQuantity;
    
    public DataBalancing(final int id, final int minQuantity, final int maxQuantity) {
        super();
        this.m_id = id;
        this.m_minQuantity = minQuantity;
        this.m_maxQuantity = maxQuantity;
    }
    
    final Element writeXml(final String dataName) {
        final Element node = new Element(dataName);
        node.setAttribute("id", String.valueOf(this.m_id));
        node.setAttribute("minQuantity", String.valueOf(this.m_minQuantity));
        node.setAttribute("maxQuantity", String.valueOf(this.m_maxQuantity));
        return node;
    }
}
