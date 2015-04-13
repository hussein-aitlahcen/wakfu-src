package com.ankamagames.wakfu.client.core.game.dialog;

import com.ankamagames.framework.external.*;

public enum WakfuClientDialogChoiceType implements ExportableEnum
{
    NONE(0), 
    INFO(1), 
    CRAFT_LEARNING(2), 
    CRAFT_TEST(3), 
    QUEST_MULTIPLY(4), 
    QUEST_AVAILABLE(5), 
    QUEST_UNAVAILABLE(6), 
    QUEST_RUNNING(7), 
    QUEST_FINISHED(8), 
    NATION(9);
    
    private int m_id;
    
    private WakfuClientDialogChoiceType(final int id) {
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static WakfuClientDialogChoiceType getFromId(final byte typeId) {
        for (final WakfuClientDialogChoiceType wakfuClientDialogChoiceType : values()) {
            if (wakfuClientDialogChoiceType.m_id == typeId) {
                return wakfuClientDialogChoiceType;
            }
        }
        return WakfuClientDialogChoiceType.NONE;
    }
}
