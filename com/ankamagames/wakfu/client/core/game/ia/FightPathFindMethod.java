package com.ankamagames.wakfu.client.core.game.ia;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.ia.fightPathFindMethodsImpl.*;

public interface FightPathFindMethod
{
    ClientPathFindResult findPath(CharacterInfo p0, CharacterActor p1, int p2);
}
