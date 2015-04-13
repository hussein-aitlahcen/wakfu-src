package com.ankamagames.wakfu.client.core.game.ressource;

import com.ankamagames.wakfu.client.ui.component.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ActionInProgress extends ImmutableFieldProvider
{
    protected static final Logger m_logger;
    public static final long DEFAULT_WAIT_TIME_FOR_HELP = 10000L;
    public static final String REMAINING_SLOTS_FIELD = "remainingSlots";
    public static final String VALID_LEVEL_FIELD = "validLevel";
    public static final String CAN_DO_ALONE = "canDoAlone";
    public static final String SLOTS = "slots";
    public static final String ICON_SKILL = "iconskill";
    public static final String WAIT = "wait";
    public static final String DRAW_NUMBER = "drawNumber";
    private static final short UPDATE_TIME = 100;
    private static final String[] FIELDS;
    private byte m_usedSlots;
    private long m_progressTimerId;
    private ProgressBar m_progressBar;
    private ProgressBar m_progressBarWait;
    private boolean m_waitForOthers;
    private int m_craftId;
    private long m_interactifElementId;
    private boolean m_uiIsEnabled;
    private ActionProgression m_actionProgression;
    private CollectAction m_action;
    private final MessageHandler m_collectMessageHandler;
    
    public ActionInProgress() {
        super();
        this.m_usedSlots = -1;
        this.m_collectMessageHandler = new MessageHandler() {
            @Override
            public boolean onMessage(final Message message) {
                ActionInProgress.this.updateCollectProgress();
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
        return ActionInProgress.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("iconskill")) {
            return this.getIconSkill();
        }
        if (fieldName.equals("drawNumber")) {
            return (this.m_action == null) ? 1 : (this.m_action.getNbRequiredPlayer() - this.m_usedSlots);
        }
        if (fieldName.equals("validLevel")) {
            return false;
        }
        if (fieldName.equals("remainingSlots")) {
            return this.m_usedSlots;
        }
        if (fieldName.equals("canDoAlone")) {
            return this.isDoAlone();
        }
        if (fieldName.equals("slots")) {
            if (this.m_usedSlots == 1 && !this.m_waitForOthers) {
                return 0;
            }
            return this.m_usedSlots;
        }
        else {
            if (fieldName.equals("wait")) {
                return this.m_waitForOthers;
            }
            return null;
        }
    }
    
    public void setUiIsEnabled(final boolean uiIsEnabled) {
        this.m_uiIsEnabled = uiIsEnabled;
    }
    
    public final void endAction() {
        if (this.m_progressTimerId != 0L) {
            this.m_usedSlots = -1;
            this.m_action = null;
            this.m_actionProgression = null;
            MessageScheduler.getInstance().removeClock(this.m_progressTimerId);
            if (this.m_uiIsEnabled) {
                this.m_progressBar = null;
                this.m_progressBarWait = null;
                Xulor.getInstance().unload("collectingProgressDialog");
            }
            this.m_progressTimerId = 0L;
        }
    }
    
    private void firePropertyChanged(final String field) {
        PropertiesProvider.getInstance().firePropertyValueChanged(this, field);
    }
    
    private String getIconSkill() {
        if (this.m_action != null) {
            switch (this.m_action.getVisualId()) {
                case 4:
                case 22: {
                    return "iconaxe";
                }
                case 3:
                case 7:
                case 8:
                case 18:
                case 19:
                case 23: {
                    return "iconcollect";
                }
                case 2: {
                    return "iconreap";
                }
                case 9: {
                    return "iconpickaxe";
                }
                case 16: {
                    return "iconpick";
                }
            }
        }
        return "iconbag";
    }
    
    private boolean isDoAlone() {
        return !this.m_waitForOthers;
    }
    
    public final void startCollect(final Resource element, final CollectAction action, final long actualRemainingDuration, final byte usedSlots, final boolean waitForOthers, final double previousProgress) {
        MessageScheduler.getInstance().removeClock(this.m_progressTimerId);
        this.m_progressTimerId = 0L;
        this.m_action = action;
        final long now = System.currentTimeMillis();
        if (waitForOthers) {
            this.m_actionProgression = new ImpossibleActionProgression(true, now, previousProgress);
        }
        else {
            if (actualRemainingDuration <= 0L) {
                this.endAction();
                this.finishCollect();
                return;
            }
            this.m_actionProgression = new LinearActionProgression(true, now, previousProgress, (1.0 - previousProgress) / actualRemainingDuration);
        }
        this.m_progressTimerId = MessageScheduler.getInstance().addClock(this.m_collectMessageHandler, 100L, 1, -1);
        this.m_waitForOthers = (waitForOthers && this.m_action.getNbRequiredPlayer() > 1);
        if (waitForOthers) {
            this.notifyProgressHalted();
        }
        this.m_usedSlots = usedSlots;
        if (Xulor.getInstance().isLoaded("help" + element.getId())) {
            Xulor.getInstance().unload("help" + element.getId());
        }
        Xulor.getInstance().load("collectingProgressDialog", Dialogs.getDialogPath("collectingProgressDialog"), 73856L, (short)10000);
        Xulor.getInstance().setWatcherTarget("collectingProgressDialog", WakfuGameEntity.getInstance().getLocalPlayer().getActor());
        this.setupProgressBar();
        PropertiesProvider.getInstance().setLocalPropertyValue("collectInProgress", this, "collectingProgressDialog");
        this.firePropertyChanged("canDoAlone");
        this.firePropertyChanged("remainingSlots");
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
    
    private void notifyProgressHalted() {
        final String messageKey = (this.m_action.getNbRequiredPlayer() <= 1) ? "occupation.error.levelRequired" : "collect.warning.needHelp";
        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString(messageKey), 3);
    }
    
    void updateCollectProgress() {
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
            if (this.m_actionProgression.m_autoEndAction) {
                this.endAction();
            }
            else {
                this.finishCollect();
            }
        }
    }
    
    private void finishCollect() {
        WakfuGameEntity.getInstance().getLocalPlayer().finishCurrentOccupation();
        QueueCollectManager.getInstance().executeNextCollectAction();
    }
    
    public final void startCollectMonster(final CollectAction action, final long actualRemainingDuration) {
        this.endAction();
        this.m_action = action;
        final long now = System.currentTimeMillis();
        if (actualRemainingDuration > 2147483647L) {
            this.m_actionProgression = new ImpossibleActionProgression(true, now, 0.0);
            this.notifyProgressHalted();
        }
        else {
            if (actualRemainingDuration <= 0L) {
                this.endAction();
                this.finishCollect();
                return;
            }
            this.m_actionProgression = new LinearActionProgression(true, now, 0.0, 1.0 / actualRemainingDuration);
        }
        this.m_progressTimerId = MessageScheduler.getInstance().addClock(this.m_collectMessageHandler, 100L, 1, -1);
        this.m_usedSlots = 0;
        Xulor.getInstance().load("collectingProgressDialog", Dialogs.getDialogPath("collectingProgressDialog"), 73856L, (short)10000);
        Xulor.getInstance().setWatcherTarget("collectingProgressDialog", WakfuGameEntity.getInstance().getLocalPlayer().getActor());
        PropertiesProvider.getInstance().setLocalPropertyValue("collectInProgress", this, "collectingProgressDialog");
        this.setupProgressBar();
        this.firePropertyChanged("canDoAlone");
        this.firePropertyChanged("remainingSlots");
    }
    
    public void startWellAction(final long elementId, final long collectTime) {
        this.endAction();
        this.m_interactifElementId = elementId;
        this.m_actionProgression = new LinearActionProgression(true, System.currentTimeMillis(), 0.0, 1.0 / collectTime);
        this.m_progressTimerId = MessageScheduler.getInstance().addClock(this.m_collectMessageHandler, 100L, 1, -1);
        this.m_usedSlots = 0;
        Xulor.getInstance().load("collectingProgressDialog", Dialogs.getDialogPath("collectingProgressDialog"), 73856L, (short)10000);
        Xulor.getInstance().setWatcherTarget("collectingProgressDialog", WakfuGameEntity.getInstance().getLocalPlayer().getActor());
        PropertiesProvider.getInstance().setLocalPropertyValue("collectInProgress", this, "collectingProgressDialog");
        this.setupProgressBar();
        this.firePropertyChanged("canDoAlone");
    }
    
    public void startMonsterAction(final long collectTime) {
        this.endAction();
        this.m_actionProgression = new LinearActionProgression(true, System.currentTimeMillis(), 0.0, 1.0 / collectTime, true);
        this.m_progressTimerId = MessageScheduler.getInstance().addClock(this.m_collectMessageHandler, 100L, 1, -1);
        this.m_usedSlots = 0;
        Xulor.getInstance().load("collectingProgressDialog", Dialogs.getDialogPath("collectingProgressDialog"), 73856L, (short)10000);
        Xulor.getInstance().setWatcherTarget("collectingProgressDialog", WakfuGameEntity.getInstance().getLocalPlayer().getActor());
        PropertiesProvider.getInstance().setLocalPropertyValue("collectInProgress", this, "collectingProgressDialog");
        this.setupProgressBar();
        this.firePropertyChanged("canDoAlone");
    }
    
    public void startSeedAction(final long estimatedTime, final int craftId) {
        this.endAction();
        this.m_craftId = craftId;
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_actionProgression = new LinearActionProgression(false, System.currentTimeMillis(), 0.0, 1.0 / estimatedTime);
        this.m_progressTimerId = MessageScheduler.getInstance().addClock(this.m_collectMessageHandler, 100L, 1, -1);
        this.m_usedSlots = 0;
        Xulor.getInstance().load("collectingProgressDialog", Dialogs.getDialogPath("collectingProgressDialog"), 73856L, (short)10000);
        Xulor.getInstance().setWatcherTarget("collectingProgressDialog", localPlayer.getActor());
        PropertiesProvider.getInstance().setLocalPropertyValue("collectInProgress", this, "collectingProgressDialog");
        this.setupProgressBar();
        this.firePropertyChanged("canDoAlone");
    }
    
    public void startSearchTreasureAction(final long estimatedTime) {
        this.endAction();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        this.m_actionProgression = new LinearActionProgression(false, System.currentTimeMillis(), 0.0, 1.0 / estimatedTime);
        this.m_progressTimerId = MessageScheduler.getInstance().addClock(this.m_collectMessageHandler, 100L, 1, -1);
        this.m_usedSlots = 0;
        Xulor.getInstance().load("collectingProgressDialog", Dialogs.getDialogPath("collectingProgressDialog"), 73856L, (short)10000);
        Xulor.getInstance().setWatcherTarget("collectingProgressDialog", localPlayer.getActor());
        PropertiesProvider.getInstance().setLocalPropertyValue("collectInProgress", this, "collectingProgressDialog");
        this.setupProgressBar();
        this.firePropertyChanged("canDoAlone");
    }
    
    public void stopHelp() {
        if (this.m_uiIsEnabled) {
            this.m_progressBar = null;
            this.m_progressBarWait = null;
            Xulor.getInstance().unload("collectingProgressDialog");
        }
        MessageScheduler.getInstance().removeClock(this.m_progressTimerId);
    }
    
    public int getCraftId() {
        return (this.m_action == null) ? this.m_craftId : this.m_action.getCraftId();
    }
    
    public long getInteractifElementId() {
        return this.m_interactifElementId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActionInProgress.class);
        FIELDS = new String[] { "validLevel", "remainingSlots", "canDoAlone", "slots", "iconskill", "wait" };
    }
}
