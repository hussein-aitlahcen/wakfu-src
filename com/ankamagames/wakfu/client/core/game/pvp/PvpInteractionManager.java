package com.ankamagames.wakfu.client.core.game.pvp;

import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.constants.*;

public class PvpInteractionManager
{
    public static final PvpInteractionManager INSTANCE;
    private final PvpInteractionProgress m_progress;
    @Nullable
    private PvpInteractionHandler m_handler;
    
    private PvpInteractionManager() {
        super();
        this.m_progress = new PvpInteractionProgress();
    }
    
    public void startInteraction(final PvpInteractionHandler handler) {
        this.m_handler = handler;
        this.m_progress.startCast(NationPvpConstants.INTERACTION_WAITING_DURATION, 0.0);
    }
    
    public void finishInteraction() {
        if (this.m_handler != null) {
            this.m_handler.onFinish();
        }
    }
    
    public void cancelInteraction() {
        this.m_progress.endAction();
        if (this.m_handler != null) {
            this.m_handler.onCancel();
        }
    }
    
    @Override
    public String toString() {
        return "PvpInteractionManager{m_progress=" + this.m_progress + ", m_handler=" + this.m_handler + '}';
    }
    
    static {
        INSTANCE = new PvpInteractionManager();
    }
}
