package com.ankamagames.wakfu.client.core.game.actor;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;

public class ActorSkillEndMovementListener implements MobileEndPathListener
{
    protected static final Logger m_logger;
    protected static final boolean DEBUG_MODE = false;
    protected CharacterInfo m_character;
    protected int m_actionId;
    protected int m_resX;
    protected int m_resY;
    
    public ActorSkillEndMovementListener(final CharacterInfo character, final int actionId, final int resX, final int resY) {
        super();
        this.m_character = character;
        this.m_actionId = actionId;
        this.m_resX = resX;
        this.m_resY = resY;
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        final Point3 pos = this.m_character.getPositionConst();
        final int dx = this.m_resX - pos.getX();
        final int dy = this.m_resY - pos.getY();
        if (dx != 0 || dy != 0) {
            mobile.setDirection(Vector3i.getDirection4FromVector(dx, dy));
        }
        if (this.m_actionId < 0) {
            mobile.setAnimation("AnimStatique");
        }
        else {
            final ActionVisual actionVisual = ActionVisualManager.getInstance().get(this.m_actionId);
            if (actionVisual != null) {
                final Resource resource = ResourceManager.getInstance().getResource(this.m_resX, this.m_resY);
                this.m_character.setCurrentInteractiveElement(resource);
                ActionVisualHelper.applyActionVisual(mobile, actionVisual);
            }
        }
        mobile.removeEndPositionListener(this);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorSkillEndMovementListener.class);
    }
}
