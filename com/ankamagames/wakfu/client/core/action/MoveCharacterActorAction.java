package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class MoveCharacterActorAction extends TimedAction
{
    private final long m_characterId;
    private final Point3 m_point3;
    private final Direction8 m_directionFromIndex;
    
    public MoveCharacterActorAction(final long characterId, final Point3 point3, final Direction8 directionFromIndex) {
        super(TimedAction.getNextUid(), FightActionType.DELAYED_ACTOR_MOVEMENT.getId(), 0);
        this.m_characterId = characterId;
        this.m_point3 = point3;
        this.m_directionFromIndex = directionFromIndex;
    }
    
    @Override
    protected long onRun() {
        this.moveCharacter();
        return 0L;
    }
    
    @Override
    protected void onActionFinished() {
    }
    
    private void moveCharacter() {
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(this.m_characterId);
        if (character == null) {
            return;
        }
        NetActorsFrame.getInstance().moveCharacterActor(character, this.m_point3, this.m_directionFromIndex);
    }
}
