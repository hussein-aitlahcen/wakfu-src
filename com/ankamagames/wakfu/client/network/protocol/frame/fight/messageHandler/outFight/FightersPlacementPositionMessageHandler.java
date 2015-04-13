package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.action.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FightersPlacementPositionMessageHandler extends UsingFightMessageHandler<FightersPlacementPositionMessage, FightInfo>
{
    @Override
    public boolean onMessage(final FightersPlacementPositionMessage msg) {
        final int fightId = msg.getFightId();
        final long[] characterIds = msg.getPositionsByFighterId().keys();
        final Collection<CharacterInfo> characterToTaunt = new HashSet<CharacterInfo>();
        for (int i = 0; i < characterIds.length; ++i) {
            final long characterId = characterIds[i];
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(characterId);
            if (character != null) {
                if (!character.isOnFight()) {
                    characterToTaunt.add(character);
                }
            }
        }
        this.addTauntActionIfNecessary(fightId, characterToTaunt);
        this.managePlacementAction(msg, fightId);
        FightActionGroupManager.getInstance().executePendingGroup(fightId);
        return false;
    }
    
    private void managePlacementAction(final FightersPlacementPositionMessage msg, final int fightId) {
        final TLongObjectHashMap<Point3> positionsByFighterId = msg.getPositionsByFighterId();
        final PlaceSeveralActorsAction placeAction = new PlaceSeveralActorsAction(TimedAction.getNextUid(), FightActionType.MOVE_CHARACTER.getId(), 0, positionsByFighterId);
        FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, placeAction);
        placeAction.setTeleportFighters(msg.isShouldTeleport());
        FightPlacementManager.INSTANCE.updatePositions(positionsByFighterId);
    }
    
    private void addTauntActionIfNecessary(final int fightId, final Collection<CharacterInfo> characterToTaunt) {
        if (!characterToTaunt.isEmpty()) {
            final TauntAction tauntAction = TauntAction.checkout(TimedAction.getNextUid(), FightActionType.TAUNT.getId(), 0, characterToTaunt);
            FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, tauntAction);
        }
    }
}
