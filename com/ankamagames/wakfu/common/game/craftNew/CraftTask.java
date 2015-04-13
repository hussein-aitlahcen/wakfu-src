package com.ankamagames.wakfu.common.game.craftNew;

import com.ankamagames.wakfu.common.game.craftNew.constant.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import java.nio.*;

public class CraftTask
{
    private final long m_uniqueId;
    private final long m_contractId;
    private CraftTaskState m_craftTaskState;
    private final GameDate m_startDate;
    
    public CraftTask(final long uniqueId, final long contractId) {
        super();
        this.m_startDate = new GameDate(GameDate.NULL_DATE);
        this.m_uniqueId = uniqueId;
        this.m_contractId = contractId;
    }
    
    public long getUniqueId() {
        return this.m_uniqueId;
    }
    
    public CraftTaskState getCraftTaskState() {
        return this.m_craftTaskState;
    }
    
    public void setCraftTaskState(final CraftTaskState craftTaskState) {
        this.m_craftTaskState = craftTaskState;
    }
    
    public GameDate getStartDate() {
        return this.m_startDate;
    }
    
    public void setStartDate(final GameDateConst gameDate) {
        this.m_startDate.set(gameDate);
    }
    
    public long getContractId() {
        return this.m_contractId;
    }
    
    public void serialize(final ByteBuffer bb) {
        bb.putLong(this.m_uniqueId);
        bb.put(this.m_craftTaskState.getId());
        bb.putLong(this.m_startDate.toLong());
    }
}
