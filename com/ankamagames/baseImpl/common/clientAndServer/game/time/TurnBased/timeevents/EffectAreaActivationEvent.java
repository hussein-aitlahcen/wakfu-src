package com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.timeevents;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.TurnBased.*;

public class EffectAreaActivationEvent extends DelayableTimeEvent
{
    private BasicEffectArea m_area;
    private long m_applicantId;
    
    @Override
    protected void safelySendTo(final TimeEventHandler handler) {
        handler.handleEffectAreaActivationEvent(this);
    }
    
    EffectAreaActivationEvent() {
        super();
    }
    
    public BasicEffectArea getArea() {
        return this.m_area;
    }
    
    public long getApplicantId() {
        return this.m_applicantId;
    }
    
    void setArea(final BasicEffectArea area) {
        this.m_area = area;
    }
    
    void setApplicantId(final long applicantId) {
        this.m_applicantId = applicantId;
    }
    
    @Override
    protected int specializedSerializedSize() {
        return 16;
    }
    
    @Override
    protected void specializedSerialize(final ByteBuffer buffer) {
        buffer.putLong(this.m_area.getId());
        buffer.putLong(this.m_applicantId);
    }
    
    @Override
    protected void specializedRead(final TimelineUnmarshallingContext ctx, final ByteBuffer buffer) {
        this.m_area = ctx.getArea(buffer.getLong());
        this.m_applicantId = buffer.getLong();
    }
    
    public EffectAreaActivationEvent(final BasicEffectArea area, final long applicantId) {
        this();
        this.m_area = area;
        this.m_applicantId = applicantId;
    }
    
    public static EffectAreaActivationEvent checkOut(final BasicEffectArea area, final long applicantId) {
        return new EffectAreaActivationEvent(area, applicantId);
    }
    
    static EffectAreaActivationEvent checkOut() {
        return new EffectAreaActivationEvent();
    }
    
    @Override
    public long getFighterToAttachToById() {
        return this.m_applicantId;
    }
    
    @Override
    public String toString() {
        return "EffectAreaActivationEvent{m_area=" + this.m_area + ", m_applicantId=" + this.m_applicantId + '}';
    }
}
