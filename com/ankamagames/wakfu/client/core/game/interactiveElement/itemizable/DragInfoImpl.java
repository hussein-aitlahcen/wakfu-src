package com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.clientToServer.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public final class DragInfoImpl implements DragInfo
{
    private final Point3 m_dragPoint;
    private boolean m_isBeingDragged;
    private Runnable m_checkValidation;
    private final WakfuClientMapInteractiveElement m_element;
    
    public DragInfoImpl(final WakfuClientMapInteractiveElement element) {
        super();
        this.m_dragPoint = new Point3();
        this.m_element = element;
    }
    
    @Override
    public boolean isBeingDragged() {
        return this.m_isBeingDragged;
    }
    
    @Override
    public Point3 getDragPoint() {
        return this.m_dragPoint;
    }
    
    @Override
    public void startDrag() {
        this.m_isBeingDragged = true;
        this.m_dragPoint.set(this.m_element.getPosition());
        this.notifyViews();
    }
    
    private void notifyViews() {
        this.m_element.notifyViews();
    }
    
    @Override
    public void setDragPoint(final int x, final int y, final short z) {
        this.m_dragPoint.set(x, y, z);
        this.notifyViews();
    }
    
    @Override
    public void validateDrag() {
        final InteractiveElementMoveRequestMessage moveMsg = new InteractiveElementMoveRequestMessage();
        moveMsg.setElementId(this.m_element.getId());
        moveMsg.setDestination(this.m_dragPoint);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(moveMsg);
        this.m_checkValidation = new Runnable() {
            @Override
            public void run() {
                DragInfoImpl.this.m_isBeingDragged = false;
            }
        };
        ProcessScheduler.getInstance().schedule(this.m_checkValidation, 1000L, 1);
        this.notifyViews();
    }
    
    @Override
    public void cancelDrag() {
        this.m_dragPoint.reset();
        this.m_isBeingDragged = false;
        this.notifyViews();
    }
    
    @Override
    public void clearCheckValidation() {
        if (this.m_checkValidation != null) {
            ProcessScheduler.getInstance().remove(this.m_checkValidation);
            this.m_checkValidation = null;
        }
    }
    
    @Override
    public void clear() {
        this.m_isBeingDragged = false;
        this.m_checkValidation = null;
    }
}
