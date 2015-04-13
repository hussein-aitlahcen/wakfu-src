package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DisplayFightMessageInterface extends TimedAction implements Releasable
{
    private String m_message;
    private static final MonitoredPool m_staticPool;
    
    public static DisplayFightMessageInterface checkout(final int uniqueId, final int actionType, final int actionId, final String message) {
        try {
            final DisplayFightMessageInterface displayFightMessageInterface = (DisplayFightMessageInterface)DisplayFightMessageInterface.m_staticPool.borrowObject();
            displayFightMessageInterface.setUniqueId(uniqueId);
            displayFightMessageInterface.setActionType(actionType);
            displayFightMessageInterface.setActionId(actionId);
            displayFightMessageInterface.setMessage(message);
            return displayFightMessageInterface;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            DisplayFightMessageInterface.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            DisplayFightMessageInterface.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
        this.m_message = null;
    }
    
    private DisplayFightMessageInterface() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        WakfuSystemMessageManager.getInstance().showMessage(new SystemMessageData(WakfuSystemMessageManager.SystemMessageType.FIGHT_INFO, WakfuTranslator.getInstance().getString(this.m_message), 3000));
        return 200L;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public void setMessage(final String message) {
        this.m_message = message;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<DisplayFightMessageInterface>() {
            @Override
            public DisplayFightMessageInterface makeObject() {
                return new DisplayFightMessageInterface(null);
            }
        });
    }
}
