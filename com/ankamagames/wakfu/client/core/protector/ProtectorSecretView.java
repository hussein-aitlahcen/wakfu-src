package com.ankamagames.wakfu.client.core.protector;

import com.ankamagames.wakfu.client.ui.component.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.client.core.*;

public class ProtectorSecretView extends ImmutableFieldProvider
{
    public static final String ICON_URL = "iconUrl";
    public static final String DESCRIPTION = "description";
    private final ProtectorSecret m_secret;
    
    public ProtectorSecretView(final ProtectorSecret secret) {
        super();
        this.m_secret = secret;
    }
    
    @Override
    public String[] getFields() {
        return null;
    }
    
    private boolean isComplete() {
        final ClientAchievementsContext context = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext();
        return !context.hasObjective(this.m_secret.getAchievementGoalId()) || context.isObjectiveCompleted(this.m_secret.getAchievementGoalId());
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconUrl")) {
            return this.isComplete() ? WakfuConfiguration.getInstance().getIconUrl("protectorSecretIconUrl", "defaultIconPath", this.m_secret.getDiscoveredGfxId()) : WakfuConfiguration.getInstance().getIconUrl("protectorSecretIconUrl", "defaultIconPath", this.m_secret.getSecretGfxId());
        }
        if (fieldName.equals("description")) {
            return this.isComplete() ? WakfuTranslator.getInstance().getString(95, this.m_secret.getSecretId(), new Object[0]) : WakfuTranslator.getInstance().getString("protector.secret.notFound");
        }
        return null;
    }
}
