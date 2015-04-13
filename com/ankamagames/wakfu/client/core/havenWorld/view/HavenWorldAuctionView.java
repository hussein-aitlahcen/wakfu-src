package com.ankamagames.wakfu.client.core.havenWorld.view;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.auction.*;

public class HavenWorldAuctionView extends ImmutableFieldProvider
{
    private static final Logger m_logger;
    public static final String START_DATE_FIELD = "startDate";
    public static final String END_DATE_FIELD = "endDate";
    public static final String GUILD_NAME_FIELD = "guildName";
    public static final String CURRENT_BID_FIELD = "currentBid";
    public static final String NEXT_BID_FIELD = "nextBid";
    public static final String HAS_RIGHT_TO_BID_FIELD = "hasRightToBid";
    public static final String[] FIELDS;
    private final int m_havenWorldId;
    private HavenWorldAuctionDefinition m_auctionDefinition;
    private GameDateConst m_startDate;
    private long m_guildId;
    private int m_bidValue;
    private String m_guildName;
    
    @Override
    public String[] getFields() {
        return HavenWorldAuctionView.FIELDS;
    }
    
    public HavenWorldAuctionView(final long guildId, final String guildName, final int bidValue, final int havenWorldId, final long startDate, final HavenWorldAuctionDefinition havenWorldAuctionDefinition) {
        super();
        this.m_guildId = guildId;
        this.m_guildName = guildName;
        this.m_bidValue = bidValue;
        this.m_havenWorldId = havenWorldId;
        this.m_startDate = GameDate.fromLong(startDate);
        this.m_auctionDefinition = havenWorldAuctionDefinition;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("startDate")) {
            if (this.m_auctionDefinition == null) {
                return null;
            }
            return WakfuTranslator.getInstance().formatDateTimeShort(this.m_startDate);
        }
        else if (fieldName.equals("endDate")) {
            if (this.m_auctionDefinition == null) {
                return null;
            }
            final GameDate endDate = new GameDate(this.m_startDate);
            endDate.add(HavenWorldAuctionConstants.AUCTION_DURATION);
            return WakfuTranslator.getInstance().formatDateTimeShort(endDate);
        }
        else {
            if (fieldName.equals("guildName")) {
                return (this.m_guildName != null && this.m_guildName.length() > 0) ? this.m_guildName : WakfuTranslator.getInstance().getString("noOwner");
            }
            if (fieldName.equals("currentBid")) {
                return (this.m_bidValue > 0) ? WakfuTranslator.getInstance().formatNumber(this.m_bidValue) : 0;
            }
            if (fieldName.equals("nextBid")) {
                final String nextBidText = WakfuTranslator.getInstance().formatNumber(this.getNextBidValue());
                return WakfuTranslator.getInstance().getString("kama.shortGain", nextBidText);
            }
            if (!fieldName.equals("hasRightToBid")) {
                return null;
            }
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer.getGuildId() < 0L) {
                return false;
            }
            final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
            final GuildMember member = guildHandler.getMember(localPlayer.getId());
            final long myRank = member.getRank();
            if (myRank == -1L) {
                return false;
            }
            final GuildRank rank = guildHandler.getRank(myRank);
            if (rank == null) {
                return false;
            }
            if (myRank != guildHandler.getBestRank() && !rank.hasAuthorisation(GuildRankAuthorisation.MANAGE_HAVEN_WORLD)) {
                return false;
            }
            if (localPlayer.getGuildId() == this.m_guildId) {
                return false;
            }
            return true;
        }
    }
    
    public int getNextBidValue() {
        return HavenWorldAuctionUtils.getNextBidValue(this.m_bidValue);
    }
    
    public int getHavenWorldId() {
        return this.m_havenWorldId;
    }
    
    public void setGuildId(final long guildId) {
        this.m_guildId = guildId;
    }
    
    public void setBidValue(final int bidValue) {
        this.m_bidValue = bidValue;
    }
    
    public void setGuildName(final String guildName) {
        this.m_guildName = guildName;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldAuctionView.class);
        FIELDS = new String[] { "startDate", "endDate", "guildName", "currentBid", "nextBid", "hasRightToBid" };
    }
}
