package com.ankamagames.wakfu.client.core.game.interactiveElement;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;

public enum WakfuClientInteractiveElementViewTypes implements ClientInteractiveElementViewDefinition
{
    IsoView((short)0, (ObjectFactory<ClientInteractiveElementView>)new WakfuClientInteractiveAnimatedElementSceneView.WakfuInteractiveAnimatedElementSceneViewFactory()), 
    MiniMapView((short)1, (ObjectFactory<ClientInteractiveElementView>)null), 
    DimensionalBagIsoView((short)2, (ObjectFactory<ClientInteractiveElementView>)new DimensionalBagView.DimensionalBagViewFactory()), 
    DungeonLadderStatueView((short)3, (ObjectFactory<ClientInteractiveElementView>)new CharacterStatueView.CharacterStatueViewFactory()), 
    StatueView((short)4, (ObjectFactory<ClientInteractiveElementView>)new StatueView.StatueViewFactory());
    
    private short m_viewId;
    private ObjectFactory<ClientInteractiveElementView> m_viewFactory;
    
    private WakfuClientInteractiveElementViewTypes(final short viewId, final ObjectFactory<ClientInteractiveElementView> viewFactory) {
        this.m_viewId = viewId;
        this.m_viewFactory = viewFactory;
    }
    
    @Override
    public short getViewFactoryId() {
        return this.m_viewId;
    }
    
    @Override
    public ObjectFactory<ClientInteractiveElementView> getViewFactory() {
        return this.m_viewFactory;
    }
    
    @Override
    public String getEnumId() {
        return Short.toString(this.m_viewId);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
