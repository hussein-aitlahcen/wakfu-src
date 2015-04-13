package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.common.datas.group.*;
import com.ankamagames.framework.graphics.engine.texture.*;

public class LadderInfoView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String BLAZON_FIELD = "blazon";
    public static final String RANK_FIELD = "rank";
    public static final String LEVEL_FIELD = "level";
    public static final String GUILD_POINTS_FIELD = "guildPoints";
    public static final String CONQUEST_POINTS_FIELD = "conquestPoints";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String[] FIELDS;
    private GuildLadderInfo m_guildLadderInfo;
    private int m_rank;
    
    public LadderInfoView(final GuildLadderInfo guildLadderInfo, final int rank) {
        super();
        this.m_guildLadderInfo = guildLadderInfo;
        this.m_rank = rank;
    }
    
    @Override
    public String[] getFields() {
        return LadderInfoView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_guildLadderInfo.getName();
        }
        if (fieldName.equals("blazon")) {
            final GuildBlazon blazon = new GuildBlazon(this.m_guildLadderInfo.getBlazon());
            if (blazon == null) {
                return null;
            }
            final GuildBannerData data = new GuildBannerData(blazon.getShapeId(), blazon.getSymbolId(), GuildBannerColor.getInstance().getColor(blazon.getSymbolColor()), GuildBannerColor.getInstance().getColor(blazon.getShapeColor()));
            final Texture guildBannerTexture = GuildBannerGenerator.getInstance().getGuildBannerTexture(data);
            data.cleanUp();
            return guildBannerTexture;
        }
        else {
            if (fieldName.equals("rank")) {
                return this.m_rank;
            }
            if (fieldName.equals("level")) {
                return this.m_guildLadderInfo.getLevel();
            }
            if (fieldName.equals("guildPoints")) {
                return this.m_guildLadderInfo.getGuildPoints();
            }
            if (fieldName.equals("conquestPoints")) {
                return this.m_guildLadderInfo.getConquestPoints();
            }
            if (fieldName.equals("description")) {
                final String description = this.m_guildLadderInfo.getDescription();
                return (description.length() == 0) ? null : description;
            }
            return null;
        }
    }
    
    static {
        FIELDS = new String[] { "name", "blazon", "rank", "level", "guildPoints", "conquestPoints", "description" };
    }
}
