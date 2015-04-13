package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.chat.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.xp.*;

public class WakfuGuildMemberView implements FieldProvider
{
    private static final Logger m_logger;
    public static final String CHARACTER_ID_FIELD = "characterId";
    public static final String NAME_FIELD = "name";
    public static final String SMILEY_DESCRIPTOR_LIBRARY_FIELD = "smileyDescriptorLibrary";
    public static final String FLAG_ICON_URL_FIELD = "flagIconUrl";
    public static final String LEVEL_FIELD = "level";
    public static final String GUILD_POINTS_FIELD = "guildPoints";
    public static final String CONQUEST_POINTS_FIELD = "conquestPoints";
    public static final String RANK_FIELD = "rank";
    public static final String IS_LOCAL_PLAYER_FIELD = "isLocalPlayer";
    public static final String IS_CONNECTED_FIELD = "isConnected";
    public static final String BACKGROUND_COLOR_FIELD = "backgroundColor";
    public static final String CAN_BE_BANNED_FIELD = "canBan";
    public static final String CAN_CHANGE_RANK_FIELD = "canChangeRank";
    public static final String RANKS_FIELD = "ranks";
    public static final String IS_FRIEND = "isFriend";
    public static final String HAS_RIGHT_TO_CHANGE_MEMBER_RANK_FIELD = "hasRightToChangeMemberRank";
    private final GuildMember m_guildMember;
    public static final String[] FIELDS;
    private boolean m_notify;
    
    public WakfuGuildMemberView(final GuildMember guildMember) {
        super();
        this.m_notify = false;
        this.m_guildMember = guildMember;
    }
    
    @Override
    public String[] getFields() {
        return new String[0];
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("characterId")) {
            return this.m_guildMember.getId();
        }
        if (fieldName.equals("name")) {
            return this.m_guildMember.getName();
        }
        if (fieldName.equals("smileyDescriptorLibrary")) {
            final boolean isLocalPlayer = this.m_guildMember.getId() == WakfuGameEntity.getInstance().getLocalPlayer().getId();
            final byte smiley = this.m_guildMember.getSmiley();
            return isLocalPlayer ? WakfuGuildView.getInstance().getSmileyFromId(smiley) : AvatarSmileyView.getView(this.m_guildMember.getBreedId(), this.m_guildMember.getSex(), this.m_guildMember.getSmiley());
        }
        if (fieldName.equals("level")) {
            final long xp = this.m_guildMember.getXp();
            return WakfuGameEntity.getInstance().getLocalPlayer().getXpTable().getLevelByXp(xp);
        }
        if (fieldName.equals("conquestPoints")) {
            return StringUtils.withThousandSeparator(0);
        }
        if (fieldName.equals("guildPoints")) {
            return StringUtils.withThousandSeparator(this.m_guildMember.getGuildPoints());
        }
        if (fieldName.equals("rank")) {
            return WakfuGuildView.getInstance().getRankView(this.m_guildMember.getRank());
        }
        if (fieldName.equals("ranks")) {
            final WakfuGuildView guildView = WakfuGuildView.getInstance();
            final ArrayList<GuildRankView> guildRankViews = new ArrayList<GuildRankView>();
            for (final GuildRankView guildRankView : guildView.getGuildRankViews()) {
                if (WakfuGuildView.getInstance().getMyRank().hasAuthorisation(GuildRankAuthorisation.CHANGE_MEMBER_RANK, guildRankView.getRank().getPosition())) {
                    guildRankViews.add(guildRankView);
                }
            }
            return guildRankViews;
        }
        if (fieldName.equals("canChangeRank")) {
            return (!this.isGuildMaster() || WakfuGuildView.getInstance().isLocalPlayerLeader()) && WakfuGuildView.getInstance().getMyRank().hasAuthorisation(GuildRankAuthorisation.EDIT_RANK);
        }
        if (fieldName.equals("isLocalPlayer")) {
            return this.isLocalPlayer();
        }
        if (fieldName.equals("isConnected")) {
            return this.isConnected();
        }
        if (fieldName.equals("backgroundColor")) {
            if (this.isGuildMaster()) {
                return "1,0.97,0.7,0.9";
            }
            if (this.isConnected()) {
                return "1,1,1,0.9";
            }
            return "1,1,1,0.3";
        }
        else if (fieldName.equals("hasRightToChangeMemberRank")) {
            final WakfuGuildView guildView = WakfuGuildView.getInstance();
            if (guildView.getGuild() == null) {
                return false;
            }
            if (guildView.getGuild().memberSize() == 1) {
                return false;
            }
            final GuildRank myRank = guildView.getMyRank();
            if (myRank == null) {
                return false;
            }
            final GuildRankView rankView = WakfuGuildView.getInstance().getRankView(this.m_guildMember.getRank());
            return !this.isGuildMaster() && myRank.hasAuthorisation(GuildRankAuthorisation.CHANGE_MEMBER_RANK, rankView.getRank().getPosition());
        }
        else if (fieldName.equals("canBan")) {
            final GuildRank rank = WakfuGuildView.getInstance().getMyRank();
            if (rank == null) {
                return false;
            }
            final GuildRankView rankView2 = WakfuGuildView.getInstance().getRankView(this.m_guildMember.getRank());
            return !this.isGuildMaster() && rank.hasAuthorisation(GuildRankAuthorisation.REMOVE_MEMBER, rankView2.getRank().getPosition());
        }
        else {
            if (fieldName.equals("flagIconUrl")) {
                final int nationId = this.m_guildMember.getNationId();
                return WakfuConfiguration.getInstance().getFlagIconUrl((nationId == 0) ? -1 : nationId);
            }
            if (fieldName.equals("isFriend")) {
                return WakfuUserGroupManager.getInstance().getFriendGroup().getUserById(this.m_guildMember.getId()) != null;
            }
            return null;
        }
    }
    
    private boolean isGuildMaster() {
        return this.getRank() == WakfuGuildView.getInstance().getBestRank();
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public boolean isLocalPlayer() {
        return WakfuGameEntity.getInstance().getLocalPlayer().getId() == this.m_guildMember.getId();
    }
    
    public void setNotify(final boolean notify) {
        this.m_notify = notify;
    }
    
    public void onRankChanged() {
        final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
        final String rankName = WakfuGuildView.getInstance().getRankView(this.m_guildMember.getRank()).getName();
        String notifTitleTranslatorKey;
        String notifText;
        if (this.m_guildMember.getId() == lpc.getId()) {
            notifTitleTranslatorKey = "notification.guildRankChangeTitle";
            notifText = WakfuTranslator.getInstance().getString("notification.guildRankChangeSelfText", rankName);
        }
        else {
            notifTitleTranslatorKey = "notification.guildRankChangeTitle";
            notifText = WakfuTranslator.getInstance().getString("notification.guildRankChangeText", this.getName(), rankName);
        }
        final String title = WakfuTranslator.getInstance().getString(notifTitleTranslatorKey);
        final String text = NotificationPanelDialogActions.createLink(notifText, NotificationMessageType.SOCIAL);
        final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.SOCIAL);
        Worker.getInstance().pushMessage(uiNotificationMessage);
    }
    
    public long getRank() {
        return this.m_guildMember.getRank();
    }
    
    public int getGuildPoints() {
        return this.m_guildMember.getGuildPoints();
    }
    
    public short getLevel() {
        final XpTable xpTable = WakfuGameEntity.getInstance().getLocalPlayer().getXpTable();
        return xpTable.getLevelByXp(this.m_guildMember.getXp());
    }
    
    public String getName() {
        return (this.m_guildMember.getName() == null) ? "" : this.m_guildMember.getName();
    }
    
    public long getId() {
        return this.m_guildMember.getId();
    }
    
    public boolean isConnected() {
        return this.m_guildMember.isConnected();
    }
    
    public byte getSmiley() {
        return this.m_guildMember.getSmiley();
    }
    
    public byte getSex() {
        return this.m_guildMember.getSex();
    }
    
    public short getBreedId() {
        return this.m_guildMember.getBreedId();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuGuildMemberView.class);
        FIELDS = new String[] { "characterId", "name", "smileyDescriptorLibrary", "flagIconUrl", "level", "guildPoints", "conquestPoints", "rank", "isLocalPlayer", "isConnected", "backgroundColor", "canBan", "canChangeRank", "ranks", "isFriend", "hasRightToChangeMemberRank" };
    }
}
