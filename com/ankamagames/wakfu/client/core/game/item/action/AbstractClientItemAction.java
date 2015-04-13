package com.ankamagames.wakfu.client.core.game.item.action;

import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.nio.*;

public abstract class AbstractClientItemAction extends AbstractItemAction
{
    private Point3 m_castPosition;
    
    AbstractClientItemAction(final int id) {
        super(id);
    }
    
    public abstract void parseParameters(final String... p0);
    
    public abstract boolean run(final Item p0);
    
    public boolean isRunnable(final Item item) {
        return this.m_criterion == null || this.m_criterion.isValid(WakfuGameEntity.getInstance().getLocalPlayer(), item, null, null);
    }
    
    void sendRequest(final long itemId) {
        final ItemActionRequestMessage msg = new ItemActionRequestMessage(itemId, this);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    public final void setCastPosition(final Point3 castPosition) {
        this.m_castPosition = castPosition;
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        if (this.isHasScript()) {
            if (this.m_castPosition == null) {
                buffer.put((byte)0);
            }
            else {
                buffer.put((byte)1);
                buffer.putInt(this.m_castPosition.getX());
                buffer.putInt(this.m_castPosition.getY());
                buffer.putShort(this.m_castPosition.getZ());
            }
        }
        return true;
    }
    
    @Override
    public int serializedSize() {
        if (!this.isHasScript()) {
            return 0;
        }
        if (this.m_castPosition == null) {
            return 1;
        }
        return 11;
    }
    
    @Override
    public boolean unserialize(final ByteBuffer buffer) {
        throw new UnsupportedOperationException("Pas de d\u00e9s\u00e9rialization dans le Client");
    }
    
    public String getAdditionalRequirementDescription() {
        return null;
    }
}
