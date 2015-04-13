package com.ankamagames.baseImpl.client.proxyclient.base.game.achievements;

import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;

public final class Objective implements ObjectiveDefinition
{
    private final int m_id;
    private final Achievement m_achievement;
    private final String m_shortDescription;
    private final String m_fullDescription;
    private final Variable[] m_relatedVariables;
    private final boolean m_feedback;
    private boolean m_completed;
    private final boolean m_positionFeedback;
    private final short m_x;
    private final short m_y;
    private final short m_z;
    private final short m_worldId;
    
    Objective(final int id, final Achievement achievement, final Variable[] relatedVariables, final String shortDescription, final String fullDescription, final boolean feedback, final boolean positionFeedback, final short x, final short y, final short z, final short worldId) {
        super();
        this.m_id = id;
        this.m_achievement = achievement;
        this.m_positionFeedback = positionFeedback;
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        this.m_worldId = worldId;
        this.m_shortDescription = ((shortDescription != null) ? shortDescription.intern() : null);
        this.m_fullDescription = ((fullDescription != null) ? fullDescription.intern() : null);
        this.m_relatedVariables = relatedVariables;
        this.m_feedback = feedback;
    }
    
    Objective(final Objective objective, final Achievement achievement, final Variable[] relatedVariables) {
        super();
        this.m_id = objective.m_id;
        this.m_achievement = achievement;
        this.m_shortDescription = objective.m_shortDescription;
        this.m_fullDescription = objective.m_fullDescription;
        this.m_feedback = objective.m_feedback;
        this.m_positionFeedback = objective.m_positionFeedback;
        this.m_x = objective.m_x;
        this.m_y = objective.m_y;
        this.m_z = objective.m_z;
        this.m_worldId = objective.m_worldId;
        this.m_relatedVariables = relatedVariables;
    }
    
    @Override
    public AchievementDefinition getParentAchievement() {
        return AchievementsModel.INSTANCE.getAchievementModel(this.m_achievement.getId());
    }
    
    public boolean isCompleted() {
        return this.m_completed;
    }
    
    void setCompleted(final boolean completed) {
        this.m_completed = completed;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public Achievement getAchievement() {
        return this.m_achievement;
    }
    
    public String getShortDescription() {
        return this.m_shortDescription;
    }
    
    public String getFullDescription() {
        return this.m_fullDescription;
    }
    
    public Variable[] getRelatedVariables() {
        return this.m_relatedVariables;
    }
    
    public boolean isFeedback() {
        return this.m_feedback;
    }
    
    public void reset() {
        this.m_completed = false;
    }
    
    public boolean isPositionFeedback() {
        return this.m_positionFeedback;
    }
    
    public short getX() {
        return this.m_x;
    }
    
    public short getY() {
        return this.m_y;
    }
    
    public short getZ() {
        return this.m_z;
    }
    
    public short getWorldId() {
        return this.m_worldId;
    }
}
