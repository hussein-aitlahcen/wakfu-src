package com.ankamagames.wakfu.client.core.game.group.party;

import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public final class PartyUtils
{
    public static void onPositionUpdate(final PartyModel party) {
        removeAllCompassPoint(party);
        addAllCompassPoint(party);
    }
    
    public static void updateMembersAPS(final PartyModel party) {
        final TLongObjectHashMap<PartyMemberInterface> members = party.getMembers();
        final TLongObjectIterator<PartyMemberInterface> it = members.iterator();
        while (it.hasNext()) {
            it.advance();
            final PartyMemberInterface member = it.value();
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(member.getCharacterId());
            if (character != null) {
                character.updateAdditionalAppearance();
            }
        }
    }
    
    private static void removeAllCompassPoint(final PartyModel party) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final TLongObjectHashMap<PartyMemberInterface> members = party.getMembers();
        final TLongObjectIterator<PartyMemberInterface> it = members.iterator();
        while (it.hasNext()) {
            it.advance();
            final PartyMemberInterface member = it.value();
            if (member.getCharacterId() != localPlayer.getId()) {
                MapManager.getInstance().removeCompass(0, member.getCharacterId());
                if (localPlayer.getInstanceId() == member.getInstanceId()) {
                    continue;
                }
                WorldPositionMarkerManager.getInstance().removePoint(0, member.getCharacterId());
            }
        }
    }
    
    private static void addAllCompassPoint(final PartyModel party) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final TLongObjectHashMap<PartyMemberInterface> members = party.getMembers();
        final TLongObjectIterator<PartyMemberInterface> iterator = members.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final PartyMemberInterface member = iterator.value();
            if (member.isCompanion()) {
                continue;
            }
            final Point3 memberPosition = member.getPosition();
            if (member.getCharacterId() == localPlayer.getId()) {
                continue;
            }
            WorldPositionMarkerManager.getInstance().updatePosition(0, member.getCharacterId(), memberPosition.getX(), memberPosition.getY(), memberPosition.getZ());
            MapManagerHelper.addCompass(member.getCharacterId(), 0, memberPosition.getX(), memberPosition.getY(), memberPosition.getZ(), member.getInstanceId(), member, DisplayableMapPointIconFactory.PARTY_MEMBER_ICON, member.getName(), WakfuClientConstants.MINI_MAP_POINT_COLOR_COMPASS_DEFAULT, MapManager.getInstance());
        }
    }
}
