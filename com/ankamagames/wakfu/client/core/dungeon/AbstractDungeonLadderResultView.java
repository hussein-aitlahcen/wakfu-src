package com.ankamagames.wakfu.client.core.dungeon;

import com.ankamagames.wakfu.client.ui.component.*;

public abstract class AbstractDungeonLadderResultView extends ImmutableFieldProvider
{
    public static final String RESULT_TEXT_FIELD = "resultText";
    public static final String DATE_FIELD = "date";
    public static final String NATION_ICON_URL_FIELD = "nationIconUrl";
    public static final String NATION_NAME_FIELD = "nationName";
    public static final String PARTY_MEMBERS_FIELD = "partyMembers";
    public static final String[] FIELDS;
    
    @Override
    public String[] getFields() {
        return AbstractDungeonLadderResultView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        return null;
    }
    
    static {
        FIELDS = new String[] { "resultText", "date", "nationIconUrl", "nationName", "partyMembers" };
    }
}
