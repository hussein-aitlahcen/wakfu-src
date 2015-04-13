package com.ankamagames.wakfu.client.core.game.achievements.ui;

import com.ankamagames.wakfu.client.ui.component.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;

public abstract class AbstractQuestView extends ImmutableFieldProvider
{
    public static final int ACHIEVEMENT = 1;
    public static final int CHALLENGE = 2;
    public static final int FIGHT_CHALLENGE = 3;
    public static final String NAME = "name";
    public static final String ICON_URL = "iconUrl";
    public static final String STYLE = "style";
    public static final String TYPE = "type";
    public static final String REMAINING_TIME = "remainingTime";
    public static final String GOALS = "goals";
    public static final String RANKING = "ranking";
    public static final String OPENED = "isOpened";
    public static final String BACKGROUND_TEXT = "backgroundText";
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.getName();
        }
        if (fieldName.equals("iconUrl")) {
            return this.getIconUrl();
        }
        if (fieldName.equals("style")) {
            return this.getStyle();
        }
        if (fieldName.equals("type")) {
            return this.getType();
        }
        if (fieldName.equals("remainingTime")) {
            return this.getRemainingTime();
        }
        if (fieldName.equals("goals")) {
            return this.getGoals();
        }
        if (fieldName.equals("ranking")) {
            return this.getRanking();
        }
        if (fieldName.equals("isOpened")) {
            return this.isOpened();
        }
        if (fieldName.equals("backgroundText")) {
            return this.getBackgroundText();
        }
        return null;
    }
    
    protected abstract String getBackgroundText();
    
    protected abstract String getName();
    
    protected abstract String getIconUrl();
    
    public abstract int getType();
    
    public abstract int getId();
    
    protected abstract String getRemainingTime();
    
    protected abstract ArrayList<AbstractQuestGoalView> getGoals();
    
    protected abstract String getRanking();
    
    protected abstract String getStyle();
    
    public boolean isOpened() {
        return QuestConfigManager.INSTANCE.isOpened(this.getId());
    }
    
    public void setOpened(final boolean opened) {
        QuestConfigManager.INSTANCE.setOpened(this.getId(), opened);
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "isOpened");
    }
    
    public void updateRemainingTime() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "remainingTime");
    }
    
    public void updateGoals() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "goals");
    }
    
    public void updateRanking() {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "ranking");
    }
}
