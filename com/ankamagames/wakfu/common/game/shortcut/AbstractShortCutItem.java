package com.ankamagames.wakfu.common.game.shortcut;

import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import org.apache.log4j.*;
import org.apache.commons.pool.*;

public abstract class AbstractShortCutItem implements InventoryContent, Releasable, RawConvertible<RawShortcut>
{
    protected static final Logger m_logger;
    protected ShortCutType m_type;
    protected ObjectPool m_pool;
    protected int m_targetReferenceId;
    protected long m_targetUniqueId;
    protected int m_targetGfxId;
    
    public ShortCutType getType() {
        return this.m_type;
    }
    
    protected void setTargetIds(final int referenceId, final long uniqueId, final int gfxId) {
        this.m_targetReferenceId = referenceId;
        this.m_targetUniqueId = uniqueId;
        this.m_targetGfxId = gfxId;
    }
    
    @Override
    public void release() {
        if (this.m_pool != null) {
            try {
                this.m_pool.returnObject(this);
            }
            catch (Exception e) {
                AbstractShortCutItem.m_logger.error((Object)("Exception dans le release de " + this.getClass().toString() + ". Normalement impossible"));
            }
            this.m_pool = null;
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public long getUniqueId() {
        return this.m_targetUniqueId;
    }
    
    @Override
    public int getReferenceId() {
        return this.m_targetReferenceId;
    }
    
    public int getTargetGfxId() {
        return this.m_targetGfxId;
    }
    
    public void setTargetGfxId(final int targetGfxId) {
        this.m_targetGfxId = targetGfxId;
    }
    
    public void setItem(final ShortCutType type, final long uid, final int rid, final int gfxId) {
        this.m_type = type;
        if (type != ShortCutType.USABLE_REFERENCE_ITEM) {
            this.m_targetUniqueId = uid;
        }
        else {
            this.m_targetUniqueId = rid;
        }
        this.m_targetReferenceId = rid;
        this.m_targetGfxId = gfxId;
    }
    
    @Override
    public boolean toRaw(final RawShortcut rawShortcut) {
        rawShortcut.type = this.m_type.getId();
        rawShortcut.targetUniqueId = this.m_targetUniqueId;
        rawShortcut.targetReferenceId = this.m_targetReferenceId;
        rawShortcut.targetGfxId = this.m_targetGfxId;
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawShortcut rawShortcut) {
        this.m_type = ShortCutType.getFromId(rawShortcut.type);
        this.m_targetUniqueId = rawShortcut.targetUniqueId;
        this.m_targetReferenceId = rawShortcut.targetReferenceId;
        this.m_targetGfxId = rawShortcut.targetGfxId;
        return this.m_type != null;
    }
    
    @Override
    public short getQuantity() {
        return 1;
    }
    
    @Override
    public void setQuantity(final short quantity) {
    }
    
    @Override
    public void updateQuantity(final short quantityUpdate) {
    }
    
    @Override
    public boolean canStackWith(final InventoryContent inv) {
        return false;
    }
    
    @Override
    public short getStackMaximumHeight() {
        return 1;
    }
    
    @Override
    public void onCheckOut() {
        this.m_targetReferenceId = 0;
        this.m_targetUniqueId = 0L;
        this.m_targetGfxId = 0;
    }
    
    @Override
    public void onCheckIn() {
        this.m_type = null;
    }
    
    @Override
    public boolean shouldBeSerialized() {
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractShortCutItem.class);
    }
}
