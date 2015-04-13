package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import com.ankamagames.xulor2.property.*;

public class NationRankDisplayer implements FieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String OWNER_NAME_FIELD = "ownerName";
    public static final String LOCAL_PLAYER_HAS_RIGHT_TO_NOMINATE = "localPlayerHasRightToNominate";
    public static final String LOCAL_PLAYER_HAS_RIGHT_TO_REVOKE = "localPlayerHasRightToRevoke";
    public static final String ICON_URL = "iconUrl";
    public static final String IS_SELF = "isSelf";
    private NationRank m_rank;
    private String m_owner;
    static final String[] COMMON_FIELDS;
    static final String[] RANK_FIELDS;
    private static final String[] LOCAL_ALL_FIELDS;
    
    public NationRankDisplayer(final NationRank rank) {
        super();
        this.m_rank = rank;
    }
    
    public NationRank getRank() {
        return this.m_rank;
    }
    
    public long getRankId() {
        return this.m_rank.getId();
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("localPlayerHasRightToRevoke")) {
            final NationGovernment government = NationDisplayer.getInstance().getNation().getGovernment();
            final GovernmentInfo localMember = government.getMember(WakfuGameEntity.getInstance().getLocalPlayer().getId());
            if (localMember == null) {
                return false;
            }
            return NationRankRightChecker.hasRankToRevoke(localMember.getRank(), this.m_rank);
        }
        else if (fieldName.equals("localPlayerHasRightToNominate")) {
            final NationGovernment government = NationDisplayer.getInstance().getNation().getGovernment();
            final GovernmentInfo localMember = government.getMember(WakfuGameEntity.getInstance().getLocalPlayer().getId());
            if (localMember == null) {
                return false;
            }
            return NationRankRightChecker.hasRankToNominate(localMember.getRank(), this.m_rank);
        }
        else {
            if (fieldName.equals("iconUrl")) {
                return WakfuConfiguration.getInstance().getIconUrl("governmentRankIconPath", "defaultIconPath", this.m_rank.getBaseId());
            }
            if (fieldName.equals("description")) {
                return WakfuTranslator.getInstance().getString(40, (short)this.m_rank.getId(), new Object[0]);
            }
            if (fieldName.equals("ownerName")) {
                return this.m_owner;
            }
            if (fieldName.equals("isSelf")) {
                return WakfuGameEntity.getInstance().getLocalPlayer().getName().equals(this.m_owner);
            }
            return null;
        }
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(57, (int)this.m_rank.getId(), new Object[0]);
    }
    
    public void setOwner(final String owner) {
        this.m_owner = owner;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "ownerName");
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
    
    @Override
    public String[] getFields() {
        return NationRankDisplayer.LOCAL_ALL_FIELDS;
    }
    
    static {
        COMMON_FIELDS = new String[] { "name", "ownerName" };
        RANK_FIELDS = new String[] { "localPlayerHasRightToNominate", "localPlayerHasRightToRevoke", "iconUrl", "description" };
        LOCAL_ALL_FIELDS = new String[NationRankDisplayer.RANK_FIELDS.length + NationRankDisplayer.COMMON_FIELDS.length];
        System.arraycopy(NationRankDisplayer.RANK_FIELDS, 0, NationRankDisplayer.LOCAL_ALL_FIELDS, 0, NationRankDisplayer.RANK_FIELDS.length);
        System.arraycopy(NationRankDisplayer.COMMON_FIELDS, 0, NationRankDisplayer.LOCAL_ALL_FIELDS, NationRankDisplayer.RANK_FIELDS.length, NationRankDisplayer.COMMON_FIELDS.length);
    }
}
