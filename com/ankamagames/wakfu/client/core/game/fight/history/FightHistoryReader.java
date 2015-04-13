package com.ankamagames.wakfu.client.core.game.fight.history;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.fight.fightHistory.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;
import java.util.*;
import gnu.trove.*;

public class FightHistoryReader extends FightHistory implements Iterable<PlayerFightHistory>
{
    private final PlayerFightHistorySerializer m_fightHistorySerializer;
    private final List<LongIntPair> m_collectedLoots;
    
    public FightHistoryReader() {
        super();
        this.m_fightHistorySerializer = new PlayerFightHistorySerializer();
        this.m_collectedLoots = new ArrayList<LongIntPair>();
    }
    
    public static FightHistoryReader deserialize(final ByteBuffer buffer) {
        return new FightHistoryReader().read(buffer);
    }
    
    private FightHistoryReader read(final ByteBuffer buffer) {
        final byte numPlayers = buffer.get();
        for (int i = 0; i < numPlayers; ++i) {
            this.readPlayerHistory(buffer);
        }
        final byte collectedLootsSize = buffer.get();
        for (int j = 0; j < collectedLootsSize; ++j) {
            this.m_collectedLoots.add(new LongIntPair(buffer.getLong(), buffer.getInt()));
        }
        final TLongIntHashMap collectedKamas = new TLongIntHashMap();
        final byte collectedKamasSize = buffer.get();
        for (int k = 0; k < collectedKamasSize; ++k) {
            collectedKamas.put(buffer.getLong(), buffer.getInt());
        }
        this.setCollectedKamas(collectedKamas);
        this.computeCollectedByPlayers();
        return this;
    }
    
    private void computeCollectedByPlayers() {
        for (final LongIntPair collectedLoot : this.m_collectedLoots) {
            final PlayerFightHistory playerFightHistory = this.m_playerHistories.get(collectedLoot.getFirst());
            if (playerFightHistory == null) {
                continue;
            }
            playerFightHistory.addCollectedLoot(collectedLoot.getSecond(), (short)1);
        }
        this.getCollectedKamas().forEachEntry(new TLongIntProcedure() {
            @Override
            public boolean execute(final long a, final int b) {
                final PlayerFightHistory playerFightHistory = FightHistoryReader.this.m_playerHistories.get(a);
                if (playerFightHistory == null) {
                    return true;
                }
                playerFightHistory.setCollectedKamas(b);
                return true;
            }
        });
    }
    
    private void readPlayerHistory(final ByteBuffer buffer) {
        this.m_fightHistorySerializer.deserialize(buffer);
        final PlayerFightHistory playerFightHistory = this.m_fightHistorySerializer.getFightHistory();
        this.m_playerHistories.put(playerFightHistory.getCharacterId(), playerFightHistory);
    }
    
    public int size() {
        return this.m_playerHistories.size();
    }
    
    @Override
    public Iterator<PlayerFightHistory> iterator() {
        return new Iterator<PlayerFightHistory>() {
            private final long[] keys = FightHistoryReader.this.m_playerHistories.keys();
            private int index = 0;
            
            @Override
            public boolean hasNext() {
                return this.index < this.keys.length;
            }
            
            @Override
            public PlayerFightHistory next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final PlayerFightHistory playerFightHistory = FightHistoryReader.this.m_playerHistories.get(this.keys[this.index++]);
                if (playerFightHistory == null) {
                    throw new ConcurrentModificationException();
                }
                return playerFightHistory;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public List<LongIntPair> getCollectedLoots() {
        return this.m_collectedLoots;
    }
}
