package com.ankamagames.wakfu.client.core.game.pvp;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.xulor2.core.*;

public class PvpInteractionProgress extends ImmutableFieldProvider
{
    protected static final Logger m_logger;
    public static final String REMAINING_SLOTS_FIELD = "remainingSlots";
    public static final String VALID_LEVEL_FIELD = "validLevel";
    public static final String CAN_DO_ALONE = "canDoAlone";
    public static final String SLOTS = "slots";
    public static final String ICON_SKILL = "iconskill";
    public static final String WAIT = "wait";
    public static final String DRAW_NUMBER = "drawNumber";
    private static final short UPDATE_TIME = 100;
    private static final String[] FIELDS;
    private long m_progressTimerId;
    private ProgressBar m_progressBar;
    private ProgressBar m_progressBarWait;
    private ActionProgression m_actionProgression;
    private CollectAction m_action;
    private final MessageHandler m_castMessageHandler;
    
    public PvpInteractionProgress() {
        super();
        this.m_castMessageHandler = new MessageHandler() {
            @Override
            public boolean onMessage(final Message message) {
                PvpInteractionProgress.this.updateCastProgress();
                return false;
            }
            
            @Override
            public long getId() {
                return 1L;
            }
            
            @Override
            public void setId(final long id) {
            }
        };
    }
    
    @Override
    public String[] getFields() {
        return PvpInteractionProgress.FIELDS;
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconskill")) {
            return "iconfight";
        }
        if (fieldName.equals("drawNumber")) {
            return 1;
        }
        if (fieldName.equals("validLevel")) {
            return false;
        }
        if (fieldName.equals("remainingSlots")) {
            return 0;
        }
        if (fieldName.equals("canDoAlone")) {
            return true;
        }
        if (fieldName.equals("slots")) {
            return 0;
        }
        if (fieldName.equals("wait")) {
            return false;
        }
        return null;
    }
    
    public final void endAction() {
        if (this.m_progressTimerId != 0L) {
            this.m_action = null;
            this.m_actionProgression = null;
            MessageScheduler.getInstance().removeClock(this.m_progressTimerId);
            this.m_progressBar = null;
            this.m_progressBarWait = null;
            if (Xulor.getInstance().isLoaded("collectingProgressDialog")) {
                Xulor.getInstance().unload("collectingProgressDialog");
            }
            this.m_progressTimerId = 0L;
        }
    }
    
    private void firePropertyChanged(final String field) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
    
    public final void startCast(final long actualRemainingDuration, final double previousProgress) {
        MessageScheduler.getInstance().removeClock(this.m_progressTimerId);
        this.m_progressTimerId = 0L;
        final long now = System.currentTimeMillis();
        if (actualRemainingDuration > 0L) {
            this.m_actionProgression = new LinearActionProgression(true, now, previousProgress, (1.0 - previousProgress) / actualRemainingDuration);
            this.m_progressTimerId = MessageScheduler.getInstance().addClock(this.m_castMessageHandler, 100L, 1, -1);
            Xulor.getInstance().load("collectingProgressDialog", Dialogs.getDialogPath("collectingProgressDialog"), 73856L, (short)10000);
            Xulor.getInstance().setWatcherTarget("collectingProgressDialog", WakfuGameEntity.getInstance().getLocalPlayer().getActor());
            this.setupProgressBar();
            PropertiesProvider.getInstance().setLocalPropertyValue("collectInProgress", this, "collectingProgressDialog");
            this.firePropertyChanged("canDoAlone");
            this.firePropertyChanged("remainingSlots");
            return;
        }
        this.endAction();
        this.finishCast();
    }
    
    private void setupProgressBar() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("collectingProgressDialog");
        if (map == null) {
            return;
        }
        this.m_progressBarWait = (ProgressBar)map.getElement("progressBarWait");
        this.m_progressBar = (ProgressBar)map.getElement("progressBar");
        if (this.m_actionProgression != null && this.m_progressBar != null && this.m_progressBarWait != null) {
            this.m_actionProgression.start(this.m_progressBar, this.m_progressBarWait);
        }
    }
    
    void updateCastProgress() {
        if (this.m_actionProgression == null) {
            return;
        }
        final long now = System.currentTimeMillis();
        if (this.m_progressBar != null && this.m_progressBarWait != null) {
            this.m_actionProgression.onUpdate(now, this.m_progressBar, this.m_progressBarWait);
        }
        this.firePropertyChanged("slots");
        this.firePropertyChanged("drawNumber");
        this.firePropertyChanged("wait");
        if (this.m_actionProgression.getActualProgress(now) >= 1.0) {
            this.endAction();
            this.finishCast();
        }
    }
    
    private void finishCast() {
        PvpInteractionManager.INSTANCE.finishInteraction();
    }
    
    @Override
    public String toString() {
        return "PvpInteractionProgress{m_progressTimerId=" + this.m_progressTimerId + ", m_progressBar=" + this.m_progressBar + ", m_progressBarWait=" + this.m_progressBarWait + ", m_actionProgression=" + this.m_actionProgression + ", m_action=" + this.m_action + ", m_castMessageHandler=" + this.m_castMessageHandler + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)PvpInteractionProgress.class);
        FIELDS = new String[] { "validLevel", "remainingSlots", "canDoAlone", "slots", "iconskill", "wait" };
    }
}
