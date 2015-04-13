package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.ui.component.*;

public abstract class AbstractQuestGoalView extends ImmutableFieldProvider
{
    public static final String DESCRIPTION_FIELD = "description";
    public static final String PROGRESSION_TEXT_FIELD = "progressionText";
    public static final String IS_COMPLETED_FIELD = "isCompleted";
    public static final String IS_FAILED_FIELD = "isFailed";
    public static final String IS_COMPASSED = "isCompassed";
    public static final String CAN_BE_COMPASSED = "canBeCompassed";
    public static final String CAN_BE_COMPASSED_NOW = "canBeCompassedNow";
    public static final String[] FIELDS;
    
    @Override
    public String[] getFields() {
        return AbstractQuestGoalView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("description")) {
            return this.getDescription();
        }
        if (fieldName.equals("progressionText")) {
            final long value = this.getProgressionValue();
            return (value == -1L) ? null : value;
        }
        if (fieldName.equals("isCompleted")) {
            return this.isCompleted();
        }
        if (fieldName.equals("isFailed")) {
            return this.isFailed();
        }
        if (fieldName.equals("isCompassed")) {
            return this.isCompassed();
        }
        if (fieldName.equals("canBeCompassed")) {
            return this.canBeCompassed();
        }
        if (fieldName.equals("canBeCompassedNow")) {
            return this.canBeCompassedNow();
        }
        return null;
    }
    
    protected abstract String getDescription();
    
    protected abstract long getProgressionValue();
    
    protected abstract boolean isCompleted();
    
    protected abstract boolean isCompassed();
    
    protected abstract boolean isFailed();
    
    protected abstract boolean canBeCompassed();
    
    protected abstract boolean canBeCompassedNow();
    
    static {
        FIELDS = new String[] { "description", "progressionText", "isCompleted", "isCompassed", "canBeCompassedNow" };
    }
}
