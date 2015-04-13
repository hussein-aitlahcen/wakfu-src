package com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement;

import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import gnu.trove.*;

public abstract class ClientInteractiveElementFactoryConfiguration<T extends ClientMapInteractiveElement> extends InteractiveElementFactoryConfiguration<T>
{
    private final TShortObjectHashMap<ObjectFactory<ClientInteractiveElementView>> m_viewFactories;
    private final TIntObjectHashMap<Properties> m_viewProperties;
    
    public ClientInteractiveElementFactoryConfiguration() {
        super();
        this.m_viewFactories = new TShortObjectHashMap<ObjectFactory<ClientInteractiveElementView>>();
        this.m_viewProperties = new TIntObjectHashMap<Properties>();
    }
    
    public final void setViewFactories(final ClientInteractiveElementViewDefinition[] interactiveElementsEnum) {
        for (final ClientInteractiveElementViewDefinition def : interactiveElementsEnum) {
            this.m_viewFactories.put(def.getViewFactoryId(), def.getViewFactory());
        }
    }
    
    public final void setViewProperties(final int viewModelId, final short viewType, final int gfx, final byte height, final int color, final int particleId, final int particleOffsetZ) {
        this.m_viewProperties.put(viewModelId, new Properties(viewType, gfx, height, color, particleId, particleOffsetZ));
    }
    
    public final TIntIntHashMap getGfxWithViewType(final short viewType) {
        final TIntIntHashMap map = new TIntIntHashMap();
        final TIntObjectIterator<Properties> it = this.m_viewProperties.iterator();
        while (it.hasNext()) {
            it.advance();
            final Properties properties = it.value();
            if (properties.m_viewType == viewType) {
                map.put(it.key(), properties.m_viewGfxId);
            }
        }
        return map;
    }
    
    public final ObjectFactory<ClientInteractiveElementView> getViewFactory(final int viewModelId) {
        final Properties properties = this.m_viewProperties.get(viewModelId);
        if (properties == null) {
            ClientInteractiveElementFactoryConfiguration.m_logger.error((Object)("Aucune d\u00e9finition pour la vue de viewModelId=" + viewModelId));
            return null;
        }
        final short viewTypeId = properties.m_viewType;
        final ObjectFactory<ClientInteractiveElementView> factory = this.m_viewFactories.get(viewTypeId);
        if (factory == null) {
            ClientInteractiveElementFactoryConfiguration.m_logger.error((Object)("Aucune factory d'enregistr\u00e9e pour le viewTypeId=" + viewTypeId));
        }
        return factory;
    }
    
    public final ClientInteractiveElementView createView(final int viewModelId) {
        final ObjectFactory<ClientInteractiveElementView> factory = this.getViewFactory(viewModelId);
        if (factory == null) {
            return null;
        }
        final Properties properties = this.m_viewProperties.get(viewModelId);
        if (properties == null) {
            return null;
        }
        final ClientInteractiveElementView view = factory.makeObject();
        view.setViewModelId(viewModelId);
        view.setViewGfxId(properties.m_viewGfxId);
        view.setViewHeight(properties.m_height);
        view.setBehindMobile(properties.m_height == 0);
        view.setColor(properties.m_color);
        view.setParticleSystemId(properties.m_particleId, properties.m_particleOffsetZ);
        return view;
    }
    
    private static class Properties
    {
        private final short m_viewType;
        final int m_viewGfxId;
        final byte m_height;
        final int m_color;
        final int m_particleId;
        final int m_particleOffsetZ;
        
        private Properties(final short viewType, final int viewGfxId, final byte height, final int color, final int particleId, final int particleOffsetZ) {
            super();
            this.m_viewType = viewType;
            this.m_viewGfxId = viewGfxId;
            this.m_height = height;
            this.m_color = color;
            this.m_particleId = particleId;
            this.m_particleOffsetZ = particleOffsetZ;
        }
    }
}
