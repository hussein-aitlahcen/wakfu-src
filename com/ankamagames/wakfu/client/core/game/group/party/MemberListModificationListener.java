package com.ankamagames.wakfu.client.core.game.group.party;

import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;

public final class MemberListModificationListener implements PartyModelListener
{
    @Override
    public void onMemberAdded(final PartyModelInterface party, final PartyMemberInterface member) {
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(member.getCharacterId());
        if (character != null) {
            character.updateAdditionalAppearance();
        }
        UICompanionsEmbeddedFrame.refreshAllCompanionsLists();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (member.isCompanion()) {
            return;
        }
        final Point3 memberPosition = member.getPosition();
        if (member.getCharacterId() != localPlayer.getId()) {
            MapManagerHelper.addCompass(member.getCharacterId(), 0, memberPosition.getX(), memberPosition.getY(), memberPosition.getZ(), member.getInstanceId(), member, DisplayableMapPointIconFactory.COMPASS_POINT_ICON, member.getName(), WakfuClientConstants.MINI_MAP_POINT_COLOR_COMPASS_DEFAULT, MapManager.getInstance());
        }
    }
    
    @Override
    public void onMemberRemoved(final PartyModelInterface party, final PartyMemberInterface member) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(member.getCharacterId());
        UICompanionsEmbeddedFrame.refreshAllCompanionsLists();
        if (character != null && !character.isLocalPlayer()) {
            character.updateAdditionalAppearance();
        }
        if (member.getCharacterId() != localPlayer.getId()) {
            MapManager.getInstance().removeCompass(0, member.getCharacterId());
            WorldPositionMarkerManager.getInstance().removePoint(0, member.getCharacterId());
        }
    }
    
    @Override
    public void onLeaderChange(final PartyModelInterface party, final long previousLeader, final long newLeader) {
    }
}
