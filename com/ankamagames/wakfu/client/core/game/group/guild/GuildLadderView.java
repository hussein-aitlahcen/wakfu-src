package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public class GuildLadderView extends ImmutableFieldProvider
{
    public static final String CURRENT_LIST_FIELD = "currentList";
    public static final String CURRENT_PAGE_FIELD = "currentPage";
    public static final String TOTAL_PAGE_FIELD = "totalPage";
    public static final String[] FIELDS;
    private final ArrayList<LadderInfoView> m_ladderInfoView;
    private short m_currentIndex;
    private int m_guildTotalSize;
    
    public GuildLadderView() {
        super();
        this.m_ladderInfoView = new ArrayList<LadderInfoView>();
        this.m_currentIndex = 0;
    }
    
    public void setCurrentIndex(final short currentIndex) {
        this.m_currentIndex = currentIndex;
    }
    
    public short getCurrentIndex() {
        return this.m_currentIndex;
    }
    
    public void setLadderInfo(final ArrayList<GuildLadderInfo> ladderInfo) {
        GuildLadderSortingType sortingType = UIGuildLadderFrame.getInstance().getSortingType();
        this.m_ladderInfoView.clear();
        Comparator<GuildLadderInfo> comparator = null;
        if (sortingType == null) {
            sortingType = GuildLadderSortingType.LEVEL_CRESCENT;
        }
        switch (sortingType) {
            case GUILD_POINTS_CRESCENT: {
                comparator = new Comparator<GuildLadderInfo>() {
                    @Override
                    public int compare(final GuildLadderInfo o1, final GuildLadderInfo o2) {
                        return MathHelper.compare(o2.getGuildPoints(), o1.getGuildPoints());
                    }
                };
                break;
            }
            case GUILD_POINTS_DESCENDING: {
                comparator = new Comparator<GuildLadderInfo>() {
                    @Override
                    public int compare(final GuildLadderInfo o1, final GuildLadderInfo o2) {
                        return MathHelper.compare(o1.getGuildPoints(), o2.getGuildPoints());
                    }
                };
                break;
            }
            case CONQUEST_POINTS_CRESCENT: {
                comparator = new Comparator<GuildLadderInfo>() {
                    @Override
                    public int compare(final GuildLadderInfo o1, final GuildLadderInfo o2) {
                        return MathHelper.compare(o2.getConquestPoints(), o1.getConquestPoints());
                    }
                };
                break;
            }
            case CONQUEST_POINTS_DESCENDING: {
                comparator = new Comparator<GuildLadderInfo>() {
                    @Override
                    public int compare(final GuildLadderInfo o1, final GuildLadderInfo o2) {
                        return MathHelper.compare(o1.getConquestPoints(), o2.getConquestPoints());
                    }
                };
                break;
            }
            case LEVEL_CRESCENT: {
                comparator = new Comparator<GuildLadderInfo>() {
                    @Override
                    public int compare(final GuildLadderInfo o1, final GuildLadderInfo o2) {
                        return MathHelper.compare(o2.getLevel(), o1.getLevel());
                    }
                };
                break;
            }
            case LEVEL_DESCENDING: {
                comparator = new Comparator<GuildLadderInfo>() {
                    @Override
                    public int compare(final GuildLadderInfo o1, final GuildLadderInfo o2) {
                        return MathHelper.compare(o1.getLevel(), o2.getLevel());
                    }
                };
                break;
            }
        }
        if (comparator != null) {
            Collections.sort(ladderInfo, comparator);
        }
        for (int i = 0; i < ladderInfo.size(); ++i) {
            int rank;
            if (sortingType.isCrescent()) {
                rank = this.m_currentIndex * 9 + i + 1;
            }
            else {
                rank = (this.getTotalPage() - this.m_currentIndex - 1) * 9 + (ladderInfo.size() - i);
            }
            this.m_ladderInfoView.add(new LadderInfoView(ladderInfo.get(i), rank));
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentList");
    }
    
    @Override
    public String[] getFields() {
        return GuildLadderView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("currentList")) {
            return this.m_ladderInfoView;
        }
        if (fieldName.equals("currentPage")) {
            return this.m_currentIndex + 1;
        }
        if (fieldName.equals("totalPage")) {
            return this.getTotalPage();
        }
        return null;
    }
    
    private int getTotalPage() {
        return MathHelper.fastCeil(this.m_guildTotalSize / 9.0f);
    }
    
    public void setGuildTotalSize(final int guildTotalSize) {
        this.m_guildTotalSize = guildTotalSize;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "currentPage", "totalPage");
    }
    
    static {
        FIELDS = new String[] { "currentList" };
    }
}
