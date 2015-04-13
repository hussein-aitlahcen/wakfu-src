package com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement;

import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class ClientInteractiveElementFactory<T extends ClientMapInteractiveElement, C extends ClientInteractiveElementFactoryConfiguration<T>> extends InteractiveElementFactory<T, C>
{
    private final TLongObjectHashMap<int[]> m_instanceViewIds;
    
    public ClientInteractiveElementFactory() {
        super();
        this.m_instanceViewIds = new TLongObjectHashMap<int[]>();
    }
    
    public void addInfo(final InteractiveElementInfo definitions, final int[] views) {
        this.addInfo(definitions);
        this.m_instanceViewIds.put(definitions.getId(), views);
    }
    
    @Override
    protected void setAdditionalData(final T element, final InteractiveElementInfo info) {
        final int[] viewIds = this.m_instanceViewIds.get(element.getId());
        assert viewIds != null : "element interactif " + element.getId() + " n'a pas de vue";
        for (int i = 0; i < viewIds.length; ++i) {
            final int viewId = viewIds[i];
            if (ClientInteractiveElementFactory.m_logger.isTraceEnabled()) {
                ClientInteractiveElementFactory.m_logger.trace((Object)("Adding view " + viewId + " to element " + element.getId()));
            }
            final ClientInteractiveElementView view = this.createView(viewId);
            if (view != null) {
                element.addView(view);
            }
        }
    }
    
    public ClientInteractiveElementView createView(final int viewId) {
        return this.m_configuration.createView(viewId);
    }
}
