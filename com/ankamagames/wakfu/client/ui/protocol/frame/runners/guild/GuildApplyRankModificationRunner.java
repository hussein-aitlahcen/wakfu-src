package com.ankamagames.wakfu.client.ui.protocol.frame.runners.guild;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.wordsModerator.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;

public class GuildApplyRankModificationRunner implements MessageRunner
{
    @Override
    public boolean run(final Message message) {
        final ModifiedGuildRankView guildRankView = (ModifiedGuildRankView)PropertiesProvider.getInstance().getObjectProperty("selectedGuildRank");
        if (guildRankView == null) {
            return false;
        }
        final String rankName = guildRankView.getName();
        if (!rankName.equals(WakfuGuildView.getInstance().getRankView(guildRankView.getId()).getName()) && !WordsModerator.getInstance().validateName(rankName)) {
            final MessageBoxData msgData = new MessageBoxData(102, 0, WakfuTranslator.getInstance().getString("error.guild.rank.invalidName"), 2L);
            msgData.setIconUrl(WakfuMessageBoxConstants.getMessageBoxIconUrl(1));
            MessageBoxManager.getInstance().msgBox(msgData);
            return false;
        }
        WakfuGuildView.getInstance().applyRankModifications(guildRankView);
        PropertiesProvider.getInstance().setPropertyValue("rankModificationDirty", false);
        return false;
    }
    
    @Override
    public int getProtocolId() {
        return 18213;
    }
}
