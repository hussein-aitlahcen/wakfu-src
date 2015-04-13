package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.wakfu.common.game.nation.government.data.*;

public class GovernorInfoFieldProvider extends GovernmentMemberInfoFieldProvider
{
    public static final String TITLE_FIELD = "title";
    public static final String NB_MANDATES_FIELD = "nbMandates";
    static final String[] LOCAL_ALL_FIELDS;
    static final String[] LOCAL_FIELDS;
    private int m_nbMandates;
    private GovernorHonorificTitleView m_title;
    
    public GovernorInfoFieldProvider(final GovernmentInfo governorInfo) {
        super(governorInfo);
        final GovernorData data = (GovernorData)governorInfo.getData();
        this.m_nbMandates = data.getNbMandate();
        this.m_title = NationDisplayer.getInstance().getHonorificTitleView(data.getTitleId());
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("title")) {
            return (this.m_title == null) ? null : this.m_title;
        }
        if (fieldName.equals("isGovernor")) {
            return true;
        }
        if (fieldName.equals("nbMandates")) {
            return this.m_nbMandates;
        }
        return super.getFieldValue(fieldName);
    }
    
    public void setTitle(final GovernorHonorificTitleView title) {
        this.m_title = title;
    }
    
    public GovernorHonorificTitleView getTitle() {
        return this.m_title;
    }
    
    @Override
    public String[] getFields() {
        return GovernorInfoFieldProvider.LOCAL_ALL_FIELDS;
    }
    
    static {
        LOCAL_FIELDS = new String[] { "title", "nbMandates" };
        LOCAL_ALL_FIELDS = new String[GovernorInfoFieldProvider.LOCAL_FIELDS.length + GovernmentMemberInfoFieldProvider.LOCAL_ALL_FIELDS.length];
        System.arraycopy(GovernorInfoFieldProvider.LOCAL_FIELDS, 0, GovernorInfoFieldProvider.LOCAL_ALL_FIELDS, 0, GovernorInfoFieldProvider.LOCAL_FIELDS.length);
        System.arraycopy(GovernmentMemberInfoFieldProvider.LOCAL_ALL_FIELDS, 0, GovernorInfoFieldProvider.LOCAL_ALL_FIELDS, GovernorInfoFieldProvider.LOCAL_FIELDS.length, GovernmentMemberInfoFieldProvider.LOCAL_ALL_FIELDS.length);
    }
}
