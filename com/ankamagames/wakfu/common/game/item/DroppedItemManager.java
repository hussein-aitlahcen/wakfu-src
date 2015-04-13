package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import gnu.trove.*;

public abstract class DroppedItemManager<FloorItem extends DroppedItem> extends AbstractFloorItemManager<FloorItem> implements MessageHandler
{
    private static final Logger m_logger;
    private long m_id;
    private long m_tick;
    private int[] item_phases_tick;
    private int item_span_tick;
    private long m_clockId;
    
    public DroppedItemManager() {
        super();
        this.m_clockId = -1L;
    }
    
    public void createClock() {
        this.item_phases_tick = new int[3];
        for (int i = 0; i < 3; ++i) {
            this.item_phases_tick[i] = ItemDropParameters.ITEM_PHASES_SEC[i] / 1;
        }
        this.item_span_tick = 30;
        if (this.m_clockId != -1L) {
            MessageScheduler.getInstance().removeClock(this.m_clockId);
        }
        this.m_clockId = MessageScheduler.getInstance().addClock(this, 1000L, 1);
        this.m_tick = 0L;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (!(message instanceof ClockMessage)) {
            return true;
        }
        ++this.m_tick;
        this.foreachFloorItem(new TObjectProcedure<FloorItem>() {
            @Override
            public boolean execute(final FloorItem item) {
                if (item.getCurrentFightId() > 0) {
                    return true;
                }
                if (item.getRemainingTicksInPhase() > 0L) {
                    item.decrementRemainingTicksInPhase();
                    return true;
                }
                if (item.getPhase() == -1) {
                    DroppedItemManager.this.silentUnspawnItem(item.getId());
                }
                else {
                    item.nextPhase();
                    DroppedItemManager.this.resetRemainingTicksInPhase(item);
                }
                return true;
            }
        });
        return false;
    }
    
    public abstract void unspawnItem(final long p0);
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public void setId(final long id) {
        this.m_id = id;
    }
    
    public long getTick() {
        return this.m_tick;
    }
    
    @Override
    public void addFloorItem(final FloorItem floorItem) {
        this.resetLockAndPhase(floorItem);
    }
    
    public final void resetLockAndPhase(final FloorItem floorItem) {
        if (floorItem.getRemainingTicksInPhase() != -1L) {
            return;
        }
        this.resetRemainingTicksInPhase(floorItem);
    }
    
    private void resetRemainingTicksInPhase(final FloorItem floorItem) {
        int remainingTicksInPhase;
        if (floorItem.getPhase() == -1) {
            remainingTicksInPhase = ((floorItem.getItemSpan() == -1) ? this.item_span_tick : (floorItem.getItemSpan() / 1));
        }
        else {
            remainingTicksInPhase = ((floorItem.getItemPhaseSpan() == null) ? this.item_phases_tick[floorItem.getPhase()] : (floorItem.getItemPhaseSpan()[floorItem.getPhase()] / 1));
        }
        floorItem.setRemainingTicksInPhase(remainingTicksInPhase);
    }
    
    static {
        m_logger = Logger.getLogger((Class)DroppedItemManager.class);
    }
}
