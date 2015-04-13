package com.ankamagames.wakfu.common.game.dungeon;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;
import java.nio.*;

public abstract class AbstractDungeonLadder
{
    protected static Logger m_logger;
    protected final DungeonLadderDefinition m_definition;
    protected final SortedList<DungeonLadderResult> m_results;
    private final ArrayList<DungeonLadderEventListener> m_eventListeners;
    
    public AbstractDungeonLadder(final DungeonLadderDefinition definition) {
        super();
        this.m_definition = definition;
        this.m_results = new SortedList<DungeonLadderResult>(this.m_definition.getLadderType().getResultComparator());
        this.m_eventListeners = new ArrayList<DungeonLadderEventListener>();
    }
    
    public short getInstanceId() {
        return this.m_definition.getInstanceId();
    }
    
    public ArrayList<DungeonLadderResult> getResults() {
        return this.m_results;
    }
    
    public DungeonLadderDefinition getDefinition() {
        return this.m_definition;
    }
    
    public int insertResult(final DungeonLadderResult result) {
        this.m_results.add(result);
        this.fireLadderChanged();
        return this.m_results.indexOf(result);
    }
    
    protected void removeLastResult() {
        this.m_results.remove(this.m_results.getLast());
    }
    
    public void removeResult(final long resultUid) {
        DungeonLadderResult resultToRemove = null;
        for (int i = 0, size = this.m_results.size(); i < size; ++i) {
            final DungeonLadderResult result = this.m_results.get(i);
            if (result.getUid() == resultUid) {
                resultToRemove = result;
                break;
            }
        }
        this.m_results.remove(resultToRemove);
        this.fireLadderChanged();
    }
    
    public int getEstimatedPositionInLadder(final DungeonLadderResult result) {
        this.m_results.add(result);
        final int position = this.m_results.indexOf(result);
        this.m_results.remove(result);
        return position;
    }
    
    public void reset() {
        this.m_results.clear();
        this.fireLadderReset();
    }
    
    protected void fireLadderChanged() {
        for (int i = 0, size = this.m_eventListeners.size(); i < size; ++i) {
            this.m_eventListeners.get(i).onLadderChanged(this);
        }
    }
    
    protected void fireLadderReset() {
        for (int i = 0, size = this.m_eventListeners.size(); i < size; ++i) {
            this.m_eventListeners.get(i).onLadderReset(this);
        }
    }
    
    public void addEventListener(final DungeonLadderEventListener listener) {
        if (!this.m_eventListeners.contains(listener)) {
            this.m_eventListeners.add(listener);
        }
    }
    
    public DungeonLadderResult getFirstResult() {
        return this.m_results.get(0);
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putShort(this.m_definition.getInstanceId());
        buffer.putShort((short)this.m_results.size());
        for (int i = 0; i < this.m_results.size(); ++i) {
            this.m_results.get(i).serialize(buffer);
        }
    }
    
    public void unserializeResults(final ByteBuffer buffer) {
        final short resultsCount = buffer.getShort();
        for (int i = 0; i < resultsCount; ++i) {
            final DungeonLadderResult result = DungeonLadderResult.fromBuild(buffer);
            this.insertResult(result);
        }
    }
    
    public int serializedSize() {
        int resultsSize = 0;
        for (int i = 0; i < this.m_results.size(); ++i) {
            final DungeonLadderResult dungeonLadderResult = this.m_results.get(i);
            resultsSize += dungeonLadderResult.serializedSize();
        }
        return 4 + resultsSize;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("DungeonLadder(instanceId=").append(this.m_definition.getInstanceId()).append(", ladderType=").append(this.m_definition.getLadderType().name()).append(", resultCount=").append(this.m_results.size()).append(")");
        return sb.toString();
    }
    
    static {
        AbstractDungeonLadder.m_logger = Logger.getLogger((Class)AbstractDungeonLadder.class);
    }
}
