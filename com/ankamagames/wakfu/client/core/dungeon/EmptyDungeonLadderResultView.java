package com.ankamagames.wakfu.client.core.dungeon;

import com.ankamagames.wakfu.client.core.*;

public class EmptyDungeonLadderResultView extends AbstractDungeonLadderResultView
{
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("resultText")) {
            return "";
        }
        if (fieldName.equals("date")) {
            return "";
        }
        if (fieldName.equals("nationIconUrl")) {
            return WakfuConfiguration.getInstance().getFlagIconUrl(-1);
        }
        if (fieldName.equals("nationName")) {
            return WakfuTranslator.getInstance().getString(39, 0, new Object[0]);
        }
        if (fieldName.equals("partyMembers")) {
            return new Object[6];
        }
        return null;
    }
}
