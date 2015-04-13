package com.ankamagames.wakfu.common.game.nation.government;

import com.ankamagames.wakfu.common.game.nation.*;
import java.util.*;
import org.jetbrains.annotations.*;
import java.nio.*;
import gnu.trove.*;

public class NationGovernment
{
    protected final Nation m_nation;
    private final TLongObjectHashMap<GovernmentInfo> m_governmentByRank;
    protected final TLongLongHashMap m_governmentById;
    private final ArrayList<GovernmentListener> m_listeners;
    
    public NationGovernment(final Nation nation) {
        super();
        this.m_governmentByRank = new TLongObjectHashMap<GovernmentInfo>();
        this.m_governmentById = new TLongLongHashMap();
        this.m_listeners = new ArrayList<GovernmentListener>();
        this.m_nation = nation;
    }
    
    public void addListener(final GovernmentListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeListener(final GovernmentListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public void putMember(final GovernmentInfo info) {
        if (this.m_governmentById.contains(info.getId()) || this.m_governmentByRank.contains(info.getRank().getId())) {
            this.removeMember(info.getId());
        }
        this.m_governmentByRank.put(info.getRank().getId(), info);
        this.m_governmentById.put(info.getId(), info.getRank().getId());
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onMemberAdded(info);
        }
    }
    
    public void removeMember(final long characterId) {
        final long rankId = this.m_governmentById.remove(characterId);
        final GovernmentInfo info = this.m_governmentByRank.remove(rankId);
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onMemberRemoved(info);
        }
    }
    
    public void removeMember(final NationRank rank) {
        final GovernmentInfo info = this.m_governmentByRank.get(rank.getId());
        if (info != null) {
            this.removeMember(info.getId());
        }
    }
    
    public void wipeGovernment() {
        final TLongLongIterator it = this.m_governmentById.iterator();
        while (it.hasNext()) {
            it.advance();
            final GovernmentInfo info = this.m_governmentByRank.remove(it.value());
            it.remove();
            for (int i = 0; i < this.m_listeners.size(); ++i) {
                this.m_listeners.get(i).onMemberRemoved(info);
            }
        }
    }
    
    public void clear() {
        this.m_governmentByRank.clear();
        this.m_governmentById.clear();
    }
    
    @Nullable
    public GovernmentInfo getGovernor() {
        return this.m_governmentByRank.get(NationRank.GOVERNOR.getId());
    }
    
    @Nullable
    public GovernmentInfo getMember(final long id) {
        return this.m_governmentByRank.get(this.m_governmentById.get(id));
    }
    
    @Nullable
    public GovernmentInfo getMember(final NationRank rank) {
        return this.m_governmentByRank.get(rank.getId());
    }
    
    public TLongObjectIterator<GovernmentInfo> governmentIterator() {
        return this.m_governmentByRank.iterator();
    }
    
    public void serialize(final ByteBuffer buffer) {
        buffer.putInt(this.m_governmentByRank.size());
        final TLongObjectIterator<GovernmentInfo> it = this.m_governmentByRank.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().serialize(buffer);
        }
    }
    
    public void unserialize(final ByteBuffer buffer) {
        for (int size = buffer.getInt(), i = 0; i < size; ++i) {
            final GovernmentInfo info = GovernmentInfo.fromBuild(buffer);
            this.m_governmentByRank.put(info.getRank().getId(), info);
            this.m_governmentById.put(info.getId(), info.getRank().getId());
        }
    }
    
    public int serializedSize() {
        int size = 4;
        final TLongObjectIterator<GovernmentInfo> it = this.m_governmentByRank.iterator();
        while (it.hasNext()) {
            it.advance();
            size += it.value().serializedSize();
        }
        return size;
    }
}
