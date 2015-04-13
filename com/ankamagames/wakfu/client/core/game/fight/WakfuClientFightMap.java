package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import org.jetbrains.annotations.*;

public final class WakfuClientFightMap extends WakfuFightMap
{
    private final Collection<CharacterInfo> m_invisibleCharacters;
    
    public WakfuClientFightMap() {
        super();
        this.m_invisibleCharacters = new HashSet<CharacterInfo>();
    }
    
    @Nullable
    @Override
    public FightObstacle getObstacle(final int x, final int y) {
        final byte id = this.getObstacleIdFromPos(x, y);
        if (id < 0) {
            return null;
        }
        final FightObstacle obstacle = this.getObstacleFromId(id);
        if (this.m_invisibleCharacters.contains(obstacle)) {
            return null;
        }
        return obstacle;
    }
    
    public void addInvisibleCharacter(final CharacterInfo info) {
        if (info == null) {
            return;
        }
        this.m_invisibleCharacters.add(info);
    }
    
    public void removeInvisibleCharacter(final CharacterInfo info) {
        if (info == null) {
            return;
        }
        this.m_invisibleCharacters.remove(info);
    }
}
