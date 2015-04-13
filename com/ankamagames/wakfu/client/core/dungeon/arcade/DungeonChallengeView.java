package com.ankamagames.wakfu.client.core.dungeon.arcade;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.dungeon.loader.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class DungeonChallengeView extends ImmutableFieldProvider
{
    public static final String NAME_FIELD = "name";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String SCORE_FIELD = "score";
    public static final String FINISHED_FIELD = "finished";
    public static final String SUCCEEDED_FIELD = "succeeded";
    public static final String[] FIELDS;
    private boolean m_finished;
    private boolean m_succeeded;
    private ChallengeDefinition m_challengeDefinition;
    
    public DungeonChallengeView(final ChallengeDefinition challengeDefinition) {
        super();
        this.m_challengeDefinition = challengeDefinition;
    }
    
    @Override
    public String[] getFields() {
        return DungeonChallengeView.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("name")) {
            return this.m_challengeDefinition.getName();
        }
        if (fieldName.equals("description")) {
            return this.m_challengeDefinition.getDescription();
        }
        if (fieldName.equals("score")) {
            return WakfuTranslator.getInstance().getString("arcadeDungeon.points", this.getScore());
        }
        if (fieldName.equals("finished")) {
            return this.m_finished;
        }
        if (fieldName.equals("succeeded")) {
            return this.m_succeeded;
        }
        return null;
    }
    
    public String getName() {
        return this.m_challengeDefinition.getName();
    }
    
    public String getDescription() {
        return this.m_challengeDefinition.getDescription();
    }
    
    public void setFinished(final boolean finished) {
        this.m_finished = finished;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "finished");
    }
    
    public void setSucceeded(final boolean succeeded) {
        this.m_succeeded = succeeded;
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "succeeded");
    }
    
    public int getScore() {
        return Math.round(this.m_challengeDefinition.getRatio() * UIArcadeDungeonFrame.getInstance().getArcadeDungeonView().getCurrentRoundTotalScore());
    }
    
    static {
        FIELDS = new String[] { "name", "score", "finished", "succeeded" };
    }
}
