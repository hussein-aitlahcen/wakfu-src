package com.ankamagames.wakfu.common.game.havenWorld.action;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public abstract class HavenWorldAction
{
    private long m_actionUID;
    private boolean m_isAdmin;
    private long m_playerId;
    
    public abstract HavenWorldActionType getActionType();
    
    public abstract void check(final ActionChecker p0);
    
    public abstract void compute(final ActionComputer p0);
    
    public final byte[] serialize() {
        final ByteArray bb = new ByteArray();
        this._serialize(bb);
        return bb.toArray();
    }
    
    protected void _serialize(final ByteArray bb) {
        bb.putLong(this.m_actionUID);
        bb.putLong(this.m_playerId);
        bb.put((byte)(this.m_isAdmin ? 1 : 0));
    }
    
    public final void unSerialize(final ByteBuffer bb) {
        this._unserialize(bb);
    }
    
    protected void _unserialize(final ByteBuffer bb) {
        this.m_actionUID = bb.getLong();
        this.m_playerId = bb.getLong();
        this.m_isAdmin = (bb.get() != 0);
    }
    
    public long getActionUID() {
        return this.m_actionUID;
    }
    
    public void setActionUID(final long actionUID) {
        this.m_actionUID = actionUID;
    }
    
    public boolean isAdmin() {
        return this.m_isAdmin;
    }
    
    public void setAdmin(final boolean admin) {
        this.m_isAdmin = admin;
    }
    
    public long getPlayerId() {
        return this.m_playerId;
    }
    
    public void setPlayerId(final long playerId) {
        this.m_playerId = playerId;
    }
    
    @Override
    public String toString() {
        return "HavenWorldAction{m_actionUID=" + this.m_actionUID + ", m_isAdmin=" + this.m_isAdmin + ", m_playerId=" + this.m_playerId + '}';
    }
}
