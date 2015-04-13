package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import java.nio.*;

enum DelayableTimeEventType
{
    RUNNING_EFFECT_ACTIVATION((byte)0, (Class)RunningEffectActivationEvent.class) {
        @Override
        DelayableTimeEvent createTimeEvent() {
            return new RunningEffectActivationEvent();
        }
    }, 
    RUNNING_EFFECT_DEACTIVATION((byte)1, (Class)RunningEffectDeactivationEvent.class) {
        @Override
        DelayableTimeEvent createTimeEvent() {
            return new RunningEffectDeactivationEvent();
        }
    }, 
    EFFECT_AREA_ACTIVATION((byte)2, (Class)EffectAreaActivationEvent.class) {
        @Override
        DelayableTimeEvent createTimeEvent() {
            return new EffectAreaActivationEvent();
        }
    };
    
    byte m_id;
    Class<? extends DelayableTimeEvent> m_klass;
    
    private DelayableTimeEventType(final byte id, final Class<? extends DelayableTimeEvent> klass) {
        this.m_id = id;
        this.m_klass = klass;
    }
    
    static DelayableTimeEventType byClass(final Class<? extends DelayableTimeEvent> klass) {
        for (final DelayableTimeEventType type : values()) {
            if (type.m_klass == klass) {
                return type;
            }
        }
        throw new IllegalArgumentException("Pas s\u00e9rialisable: " + klass.getSimpleName());
    }
    
    static DelayableTimeEventType byId(final byte id) {
        for (final DelayableTimeEventType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Pas s\u00e9rialisable: id " + id);
    }
    
    public void serializeDelayableTimeEvent(final DelayableTimeEvent event, final ByteBuffer buffer) {
        if (event.getClass() != this.m_klass) {
            throw new IllegalArgumentException();
        }
        buffer.put(this.m_id);
        event.specializedSerialize(buffer);
    }
    
    public int serializedSize(final DelayableTimeEvent event) {
        return 1 + event.specializedSerializedSize();
    }
    
    abstract DelayableTimeEvent createTimeEvent();
    
    public static DelayableTimeEvent createTimeEvent(final ByteBuffer buffer) {
        return byId(buffer.get()).createTimeEvent();
    }
}
