package com.ankamagames.wakfu.common.datas.specific;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.listener.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import gnu.trove.*;
import java.util.*;

public class PropertyManager<T extends PropertyType> extends AbstractPropertyManager<T>
{
    public static final byte WORLD_PROPERTY = 0;
    public static final byte FIGHT_PROPERTY = 1;
    public static final byte ITEM_PROPERTY = 2;
    public static final byte EFFECT_AREA_PROPERTY = 4;
    private final PropertyUpdateListenerNotifier m_listenerNotifier;
    private final ListenerHandler<PropertyUpdateListener> m_listeners;
    
    public PropertyManager() {
        super();
        this.m_listenerNotifier = new PropertyUpdateListenerNotifier();
        this.m_listeners = new ListenerHandler<PropertyUpdateListener>(this.m_listenerNotifier);
    }
    
    public static PropertyManager newInstance(final byte type, final PropertyUpdateListener listener) {
        PropertyManager propertyManager = null;
        switch (type) {
            case 0: {
                propertyManager = new PropertyManager();
                break;
            }
            case 1: {
                propertyManager = new PropertyManager();
                break;
            }
            case 2: {
                propertyManager = new PropertyManager();
                break;
            }
            default: {
                PropertyManager.m_logger.fatal((Object)"type de manager de propri\u00e9t\u00e9 inconnu");
                return null;
            }
        }
        propertyManager.m_listeners.addListener(listener);
        return propertyManager;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_listeners.clear();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_listeners.clear();
    }
    
    public void addListener(final PropertyUpdateListener listener) {
        this.m_listeners.addListener(listener);
    }
    
    public boolean removeListener(final PropertyUpdateListener l) {
        return this.m_listeners.removeListener(l);
    }
    
    private void dispatchUpdate(final T prop) {
        if (this.m_listenerNotifier.property != null) {
            PropertyManager.m_logger.error((Object)"Attention, \u00e9crasement de propri\u00e9t\u00e9 dans le notifier", (Throwable)new Exception());
        }
        this.m_listenerNotifier.property = prop;
        this.m_listeners.notifyListeners();
        this.m_listenerNotifier.property = null;
    }
    
    @Override
    public boolean toRaw(final RawProperties raw) {
        raw.clear();
        final TByteByteIterator iterator = this.m_properties.iterator();
        int i = this.m_properties.size();
        while (i-- > 0) {
            iterator.advance();
            final RawProperties.Property rawProperty = new RawProperties.Property();
            rawProperty.id = iterator.key();
            rawProperty.count = iterator.value();
            raw.properties.add(rawProperty);
        }
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawProperties raw) {
        this.m_properties.clear();
        for (final RawProperties.Property rawProperty : raw.properties) {
            this.m_properties.put(rawProperty.id, rawProperty.count);
        }
        this.dispatchUpdate(null);
        return true;
    }
    
    @Override
    public byte add(final T type) {
        final byte b = super.add(type);
        this.dispatchUpdate(type);
        return b;
    }
    
    @Override
    public byte substract(final T type) {
        final byte b = super.substract(type);
        this.dispatchUpdate(type);
        return b;
    }
    
    @Override
    public void remove(final T type) {
        super.remove(type);
        this.dispatchUpdate(type);
    }
    
    @Override
    public void reset() {
        super.reset();
        this.dispatchUpdate(null);
    }
    
    private static class PropertyUpdateListenerNotifier implements ListenerNotifier<PropertyUpdateListener>
    {
        private PropertyType property;
        
        private PropertyUpdateListenerNotifier() {
            super();
            this.property = null;
        }
        
        @Override
        public void notify(final PropertyUpdateListener listener) {
            listener.onPropertyUpdated(this.property);
        }
    }
}
