package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;

public final class MRUActionUtils
{
    public static boolean canManageInHavenWorld(final long ownerId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final HavenWorldDefinition worldFromInstance = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(localPlayer.getInstanceId());
        if (worldFromInstance == null) {
            return false;
        }
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final HavenWorldDefinition guildWorld = HavenWorldDefinitionManager.INSTANCE.getWorld(guildHandler.getHavenWorldId());
        if (guildWorld == null || guildWorld.getWorldInstanceId() != localPlayer.getInstanceId()) {
            return false;
        }
        final GuildMember guildMember = guildHandler.getMember(localPlayer.getId());
        if (guildMember == null) {
            return false;
        }
        final GuildRank guildRank = guildHandler.getRank(guildMember.getRank());
        return guildRank != null && guildRank.hasAuthorisation(GuildRankAuthorisation.MANAGE_HAVEN_WORLD);
    }
}
