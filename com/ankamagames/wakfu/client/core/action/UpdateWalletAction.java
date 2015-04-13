package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class UpdateWalletAction extends TimedAction implements Releasable
{
    private int m_amount;
    private static final MonitoredPool m_staticPool;
    
    public static UpdateWalletAction checkout(final int uniqueId, final int actionType, final int actionId, final int ammount) {
        try {
            final UpdateWalletAction updateWalletAction = (UpdateWalletAction)UpdateWalletAction.m_staticPool.borrowObject();
            updateWalletAction.setUniqueId(uniqueId);
            updateWalletAction.setActionType(actionType);
            updateWalletAction.setActionId(actionId);
            updateWalletAction.m_amount = ammount;
            return updateWalletAction;
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors d'un checkOut sur un UpdateWalletAction : ", e);
        }
    }
    
    @Override
    public void release() {
        try {
            UpdateWalletAction.m_staticPool.returnObject(this);
        }
        catch (Exception e) {
            UpdateWalletAction.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + "(normalement impossible)"));
        }
    }
    
    @Override
    public void onCheckOut() {
        this.m_amount = 0;
    }
    
    @Override
    public void onCheckIn() {
    }
    
    private UpdateWalletAction() {
        super(0, 0, 0);
    }
    
    @Override
    protected long onRun() {
        WakfuGameEntity.getInstance().getLocalPlayer().getOwnedDimensionalBag().getWallet().setAmountOfCash(this.m_amount);
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
        this.release();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<UpdateWalletAction>() {
            @Override
            public UpdateWalletAction makeObject() {
                return new UpdateWalletAction(null);
            }
        });
    }
}
