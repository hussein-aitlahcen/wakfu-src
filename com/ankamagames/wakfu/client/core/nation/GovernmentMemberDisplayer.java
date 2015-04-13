package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.government.*;

public class GovernmentMemberDisplayer extends CandidateInfoFieldProvider
{
    public static final String RANK_FIELD = "rank";
    static final String[] LOCAL_ALL_FIELDS;
    static final String[] LOCAL_FIELDS;
    private NationRankDisplayer m_nationRankDisplayer;
    
    public GovernmentMemberDisplayer(final GovernmentInfo governmentInfo, final NationRankDisplayer nationRankDisplayer) {
        super(governmentInfo);
        this.m_nationRankDisplayer = nationRankDisplayer;
    }
    
    @Override
    public String[] getFields() {
        return GovernmentMemberDisplayer.LOCAL_ALL_FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("rank")) {
            return this.m_nationRankDisplayer;
        }
        return super.getFieldValue(fieldName);
    }
    
    static {
        LOCAL_FIELDS = new String[] { "rank" };
        LOCAL_ALL_FIELDS = new String[GovernmentMemberDisplayer.LOCAL_FIELDS.length + CandidateInfoFieldProvider.COMMON_FIELDS.length];
        System.arraycopy(GovernmentMemberDisplayer.LOCAL_FIELDS, 0, GovernmentMemberDisplayer.LOCAL_ALL_FIELDS, 0, GovernmentMemberDisplayer.LOCAL_FIELDS.length);
        System.arraycopy(CandidateInfoFieldProvider.COMMON_FIELDS, 0, GovernmentMemberDisplayer.LOCAL_ALL_FIELDS, GovernmentMemberDisplayer.LOCAL_FIELDS.length, CandidateInfoFieldProvider.COMMON_FIELDS.length);
    }
}
