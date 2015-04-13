package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class PushFrameAction extends TimedAction implements Releasable
{
    private ArrayList<MessageFrame> m_frameList;
    private static final MonitoredPool m_staticPool;
    
    public static PushFrameAction checkout(final int uniqueId, final int actionType, final int actionId, final ArrayList<MessageFrame> frameList) {
        try {
            final PushFrameAction pushFrameAction = (PushFrameAction)PushFrameAction.m_staticPool.borrowObject();
            pushFrameAction.setUniqueId(uniqueId);
            pushFrameAction.setActionType(actionType);
            pushFrameAction.setActionId(actionId);
            pushFrameAction.setFrameList(frameList);
            return pushFrameAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            PushFrameAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            PushFrameAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_frameList = null;
    }
    
    private PushFrameAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        for (final MessageFrame frame : this.m_frameList) {
            WakfuGameEntity.getInstance().pushFrame(frame);
        }
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    public void setFrameList(final ArrayList<MessageFrame> frameList) {
        this.m_frameList = frameList;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<PushFrameAction>() {
            @Override
            public PushFrameAction makeObject() {
                return new PushFrameAction(null);
            }
        });
    }
}
