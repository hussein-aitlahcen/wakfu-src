package com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.ia.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class TackleAwareFightPathFindMethod extends AbstractFightPathFindMethod
{
    public static final Logger m_logger;
    public static final FightPathFindMethod INSTANCE;
    protected static final float DEFAULT_ENNEMY_TACKLE_COST = 0.1f;
    protected static final float DEFAULT_BATTLEGROUND_BORDER_COST = 0.15f;
    
    @Override
    public ClientPathFindResult findPath(final CharacterInfo currentFighter, final CharacterActor actor, final int availableMovementPoints) {
        return new ClientPathFindResult(this.computePath(currentFighter.getCurrentFight().getFightMap(), currentFighter, actor, availableMovementPoints, 0.1f, 0.15f), false);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TackleAwareFightPathFindMethod.class);
        INSTANCE = new TackleAwareFightPathFindMethod();
    }
}
