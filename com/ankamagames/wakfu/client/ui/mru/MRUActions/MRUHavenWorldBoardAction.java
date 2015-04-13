package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.datas.havenWorld.agt_like.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.auction.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class MRUHavenWorldBoardAction extends MRUGenericInteractiveAction
{
    public static final int ENABLED = 0;
    public static final int IS_NOT_SUBSCRIBED = 1;
    public static final int HAS_NOT_GOT_RIGHT = 2;
    public static final int BAD_GUILD_LEVEL = 3;
    public static final int NO_AUCTION = 4;
    public static final int AUCTION_FINISHED = 5;
    public static final int AUCTION_NOT_STARTED = 6;
    public static final int HAS_ALREADY_A_HAVEN_WORLD = 7;
    public static final int NOT_HAS_GUILD = 8;
    public static final int NO_NATION = 9;
    private GameDateConst m_startDate;
    private ArrayList<Integer> m_disabledReasons;
    private int m_havenWorldId;
    
    public MRUHavenWorldBoardAction() {
        super();
        this.m_disabledReasons = new ArrayList<Integer>();
    }
    
    public MRUHavenWorldBoardAction(final String name, final int gfxId, final int havenWorldId, final GameDateConst startDate) {
        super();
        this.m_disabledReasons = new ArrayList<Integer>();
        this.m_startDate = startDate;
        this.m_name = name;
        this.m_gfxId = gfxId;
        this.m_havenWorldId = havenWorldId;
    }
    
    @Override
    public MRUGenericInteractiveAction getCopy() {
        return new MRUHavenWorldBoardAction(this.m_name, this.m_gfxId, this.m_havenWorldId, this.m_startDate);
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(localPlayer)) {
            this.addDisabledReason(1);
        }
        if (localPlayer.getNationId() <= 0 && localPlayer.getTravellingNationId() > 0) {
            this.addDisabledReason(9);
        }
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        if (guildHandler.getGuildId() <= 0L) {
            this.addDisabledReason(8);
            return false;
        }
        final GuildMember member = guildHandler.getMember(localPlayer.getId());
        final long rankId = member.getRank();
        final GuildRank rank = guildHandler.getRank(rankId);
        if (guildHandler == null || guildHandler.getGuildId() == 0L || !rank.hasAuthorisation(GuildRankAuthorisation.MANAGE_HAVEN_WORLD)) {
            this.addDisabledReason(2);
        }
        if (guildHandler != null && guildHandler.getGuildId() > 0L && guildHandler.getLevel() < 3) {
            this.addDisabledReason(3);
        }
        if (guildHandler != null && guildHandler.getHavenWorldId() > 0) {
            this.addDisabledReason(7);
        }
        final HavenWorldAuctionDefinition definition = HavenWorldAuctionDefinitionManager.INSTANCE.getDefinition(this.m_havenWorldId);
        if (definition == null) {
            this.addDisabledReason(4);
        }
        else {
            final GameDateConst currentDate = WakfuGameCalendar.getInstance().getDate();
            final GameDateConst startDate = this.getStartDate(definition);
            if (startDate.after(currentDate)) {
                this.addDisabledReason(6);
            }
        }
        return this.m_disabledReasons.isEmpty() && this.m_enabled;
    }
    
    private GameDateConst getStartDate(final HavenWorldAuctionDefinition definition) {
        return (this.m_startDate != null) ? this.m_startDate : definition.getStartDate();
    }
    
    public void addDisabledReason(final int disabledReason) {
        if (this.m_disabledReasons.contains(disabledReason)) {
            return;
        }
        this.m_disabledReasons.add(disabledReason);
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.isEnabled() ? MRUHavenWorldBoardAction.DEFAULT_TOOLTIP_COLOR : MRUHavenWorldBoardAction.NOK_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString(this.m_name))._b();
        if (!this.isEnabled()) {
            for (final int reason : this.m_disabledReasons) {
                sb.newLine().addColor(MRUHavenWorldBoardAction.NOK_TOOLTIP_COLOR);
                switch (reason) {
                    case 1: {
                        sb.append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
                        break;
                    }
                    case 8: {
                        sb.append(WakfuTranslator.getInstance().getString("error.playerHasNoGuild"));
                        break;
                    }
                    case 2: {
                        sb.append(WakfuTranslator.getInstance().getString("error.rightToBuyHavenWorld"));
                        break;
                    }
                    case 3: {
                        sb.append(WakfuTranslator.getInstance().getString("error.badGuildLevelToBuyHavenWorld", 3));
                        break;
                    }
                    case 4: {
                        sb.append(WakfuTranslator.getInstance().getString("errorNoAuction"));
                        break;
                    }
                    case 7: {
                        sb.append(WakfuTranslator.getInstance().getString("errorHasAlreadyAHavenWorld"));
                        break;
                    }
                    case 5: {
                        final HavenWorldAuctionDefinition definition = HavenWorldAuctionDefinitionManager.INSTANCE.getDefinition(this.m_havenWorldId);
                        sb.append(WakfuTranslator.getInstance().getString("errorAuctionFinished", WakfuTranslator.getInstance().formatDateShort(definition.getEndDate())));
                        break;
                    }
                    case 6: {
                        final HavenWorldAuctionDefinition definition = HavenWorldAuctionDefinitionManager.INSTANCE.getDefinition(this.m_havenWorldId);
                        sb.append(WakfuTranslator.getInstance().getString("errorAuctionNotStarted", WakfuTranslator.getInstance().formatDateShort(this.getStartDate(definition))));
                        break;
                    }
                    case 9: {
                        sb.append(WakfuTranslator.getInstance().getString("error.playerNoNation"));
                        break;
                    }
                }
                sb.closeText();
            }
        }
        return sb.finishAndToString();
    }
}
