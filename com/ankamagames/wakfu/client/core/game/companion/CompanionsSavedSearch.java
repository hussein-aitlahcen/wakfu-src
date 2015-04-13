package com.ankamagames.wakfu.client.core.game.companion;

import com.ankamagames.wakfu.client.ui.component.*;

public class CompanionsSavedSearch extends ImmutableFieldProvider
{
    public static final String IS_ALPHABETICAL_SORTED_FIELD = "isAlphabeticalSorted";
    public static final String IS_LEVEL_SORTED_FIELD = "isLevelSorted";
    public static final String FILTER_TYPE_FIELD = "filterType";
    private CompanionFilterType m_companionFilterType;
    private boolean m_isAlphabeticalSorted;
    private boolean m_isLevelSorted;
    public String[] FIELDS;
    
    public CompanionsSavedSearch() {
        super();
        this.m_companionFilterType = CompanionFilterType.ALL;
        this.m_isAlphabeticalSorted = false;
        this.m_isLevelSorted = false;
        this.FIELDS = new String[] { "isAlphabeticalSorted", "isLevelSorted", "filterType" };
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("isAlphabeticalSorted")) {
            return this.m_isAlphabeticalSorted;
        }
        if (fieldName.equals("isLevelSorted")) {
            return this.m_isLevelSorted;
        }
        if (fieldName.equals("filterType")) {
            return this.m_companionFilterType.getTypeId();
        }
        return null;
    }
    
    public CompanionFilterType getCompanionFilterType() {
        return this.m_companionFilterType;
    }
    
    public void setCompanionFilterType(final CompanionFilterType companionFilterType) {
        this.m_companionFilterType = companionFilterType;
    }
    
    public boolean isAlphabeticalSorted() {
        return this.m_isAlphabeticalSorted;
    }
    
    public void setAlphabeticalSorted(final boolean alphabeticalSorted) {
        this.m_isAlphabeticalSorted = alphabeticalSorted;
    }
    
    public boolean isLevelSorted() {
        return this.m_isLevelSorted;
    }
    
    public void setLevelSorted(final boolean levelSorted) {
        this.m_isLevelSorted = levelSorted;
    }
    
    public String getSavedInfos() {
        return this.m_isAlphabeticalSorted + "," + this.m_isLevelSorted + "," + this.m_companionFilterType.getTypeId();
    }
    
    public void fromSavedInfos(final String savedInfos) {
        final String[] infos = savedInfos.split(",");
        if (infos.length < 3) {
            return;
        }
        this.m_isAlphabeticalSorted = Boolean.parseBoolean(infos[0]);
        this.m_isLevelSorted = Boolean.parseBoolean(infos[1]);
        this.m_companionFilterType = CompanionFilterType.getFromId(Integer.parseInt(infos[2]));
    }
    
    public enum CompanionFilterType
    {
        ALL(0), 
        OWNED(1), 
        UNOWNED(2);
        
        private final int m_typeId;
        
        private CompanionFilterType(final int typeId) {
            this.m_typeId = typeId;
        }
        
        public int getTypeId() {
            return this.m_typeId;
        }
        
        public static CompanionFilterType getFromId(final int id) {
            for (final CompanionFilterType companionFilterType : values()) {
                if (companionFilterType.getTypeId() == id) {
                    return companionFilterType;
                }
            }
            return null;
        }
    }
}
