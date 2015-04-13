package com.ankamagames.wakfu.common.game.characterInfo;

import org.apache.log4j.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class PlayerSynchronizator
{
    private static final Logger m_logger;
    public static PlayerSynchronizator INSTANCE;
    final TLongObjectHashMap<Queue<PlayerSynchroHandler>> m_synchroHandlers;
    
    private PlayerSynchronizator() {
        super();
        this.m_synchroHandlers = new TLongObjectHashMap<Queue<PlayerSynchroHandler>>();
    }
    
    public void addSynchroHandler(final long characterId, @NotNull final PlayerSynchroHandler handler) {
        Queue<PlayerSynchroHandler> currentQueue = this.m_synchroHandlers.get(characterId);
        if (currentQueue == null) {
            currentQueue = new LinkedList<PlayerSynchroHandler>();
            this.m_synchroHandlers.put(characterId, currentQueue);
        }
        currentQueue.offer(handler);
    }
    
    public void notifyPlayerLoaded(final long characterId) {
        final Queue<PlayerSynchroHandler> queue = this.m_synchroHandlers.remove(characterId);
        if (queue == null) {
            return;
        }
        while (!queue.isEmpty()) {
            queue.poll().onPlayerLoaded();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayerSynchronizator.class);
        PlayerSynchronizator.INSTANCE = new PlayerSynchronizator();
    }
}
