package com.ankamagames.wakfu.common.game.xp.modifications;

import org.apache.log4j.*;
import java.util.*;
import gnu.trove.*;
import java.nio.*;

public class PlayerXpModificationCollection implements Iterable<PlayerXpModification>
{
    private static final Logger m_logger;
    private final TLongObjectHashMap<PlayerXpModification> m_playerXpModifications;
    
    public PlayerXpModificationCollection() {
        super();
        this.m_playerXpModifications = new TLongObjectHashMap<PlayerXpModification>();
    }
    
    public PlayerXpModificationCollection(final PlayerXpModification... modifications) {
        this();
        for (final PlayerXpModification modification : modifications) {
            this.m_playerXpModifications.put(modification.getPlayerId(), modification);
        }
    }
    
    public void addPlayer(final long playerId, final XpModification xpModification) {
        this.m_playerXpModifications.put(playerId, new PlayerXpModification(playerId, xpModification));
    }
    
    public void addSkillOrSpell(final long playerId, final SkillOrSpell skillOrSpell, final XpModification xpModification) {
        if (!this.m_playerXpModifications.containsKey(playerId)) {
            PlayerXpModificationCollection.m_logger.error((Object)("Joueur id=" + playerId + " inconnu, impossible de rajouter un sort/skill pour lui."));
        }
        this.m_playerXpModifications.get(playerId).addSkillOrSpellXpModification(skillOrSpell, xpModification);
    }
    
    public int size() {
        return this.m_playerXpModifications.size();
    }
    
    public PlayerXpModification getPlayerXpModification(final long playerId) {
        return this.m_playerXpModifications.get(playerId);
    }
    
    @Override
    public Iterator<PlayerXpModification> iterator() {
        return new Iterator<PlayerXpModification>() {
            private final long[] keys = PlayerXpModificationCollection.this.m_playerXpModifications.keys();
            private int index = 0;
            
            @Override
            public boolean hasNext() {
                return this.index < this.keys.length;
            }
            
            @Override
            public PlayerXpModification next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final PlayerXpModification playerXpModification = PlayerXpModificationCollection.this.m_playerXpModifications.get(this.keys[this.index++]);
                if (playerXpModification == null) {
                    throw new ConcurrentModificationException();
                }
                return playerXpModification;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    public int serializedSize() {
        final int[] sizeRef = { 1 };
        this.m_playerXpModifications.forEachValue(new TObjectProcedure<PlayerXpModification>() {
            @Override
            public boolean execute(final PlayerXpModification playerXpModification) {
                final int[] val$sizeRef = sizeRef;
                final int n = 0;
                val$sizeRef[n] += playerXpModification.serializedSize();
                return true;
            }
        });
        return sizeRef[0];
    }
    
    public void write(final ByteBuffer buffer) {
        buffer.put((byte)this.m_playerXpModifications.size());
        this.m_playerXpModifications.forEachValue(new TObjectProcedure<PlayerXpModification>() {
            @Override
            public boolean execute(final PlayerXpModification playerXpModification) {
                playerXpModification.write(buffer);
                return true;
            }
        });
    }
    
    public static PlayerXpModificationCollection deserialize(final ByteBuffer buffer) {
        return new PlayerXpModificationCollection().read(buffer);
    }
    
    private PlayerXpModificationCollection read(final ByteBuffer buffer) {
        final byte numPlayers = buffer.get();
        for (int i = 0; i < numPlayers; ++i) {
            final PlayerXpModification xpModification = PlayerXpModification.deserialize(buffer);
            this.m_playerXpModifications.put(xpModification.getPlayerId(), xpModification);
        }
        return this;
    }
    
    static XpModification readXpModification(final ByteBuffer buffer) {
        final long xpDiff = buffer.getLong();
        final short levelDiff = buffer.getShort();
        return new XpModification(xpDiff, levelDiff);
    }
    
    static void writeXpModification(final XpModification xpModification, final ByteBuffer buffer) {
        buffer.putLong(xpModification.getXpDifference());
        buffer.putShort(xpModification.getLevelDifference());
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayerXpModificationCollection.class);
    }
}
