package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.util.alignment.*;

public class UITimePointSelectionFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static final UITimePointSelectionFrame m_instance;
    private long m_remainingMillis;
    private CharacterInfo m_fighter;
    private Runnable m_countDownRunnable;
    private Widget m_dialog;
    private boolean m_removingFrame;
    private ProgressBar m_progressBar;
    private AudioSource m_timerSource;
    
    public static UITimePointSelectionFrame getInstance() {
        return UITimePointSelectionFrame.m_instance;
    }
    
    private void selectBonusAndRemoveDialog(final int bonusIndex, final boolean atOnce) {
        if (this.m_removingFrame) {
            return;
        }
        this.m_removingFrame = true;
        synchronized (UITimePointSelectionFrame.m_instance) {
            if (this.m_dialog == null) {
                return;
            }
            short addedTime = 0;
            if (!atOnce && bonusIndex != -1) {
                final String imageId = "list#" + bonusIndex + ".image";
                final Image image = (Image)this.m_dialog.getElementMap().getElement(imageId);
                if (image != null) {
                    final DecoratorAppearance app = image.getAppearance();
                    app.addTween(new ModulationColorTween(Color.WHITE, Color.WHITE_ALPHA, app, 0, 250, 8, true, TweenFunction.PROGRESSIVE));
                }
                addedTime = 1300;
            }
            final PositionTween positionTween = new PositionTween(this.m_dialog.getX(), this.m_dialog.getY(), MasterRootContainer.getInstance().getWidth(), this.m_dialog.getY(), this.m_dialog, addedTime, 250, TweenFunction.PROGRESSIVE);
            positionTween.addTweenEventListener(new TweenEventListener() {
                @Override
                public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                    switch (e) {
                        case TWEEN_ENDED: {
                            ProcessScheduler.getInstance().schedule(new Runnable() {
                                @Override
                                public void run() {
                                    WakfuGameEntity.getInstance().removeFrame(UITimePointSelectionFrame.this);
                                }
                            });
                            break;
                        }
                    }
                }
            });
            this.m_dialog.addTween(positionTween);
        }
    }
    
    private void onChooseTimerEnd() {
        try {
            if (this.m_timerSource != null) {
                this.m_timerSource.setStopOnNullGain(true);
                this.m_timerSource.fade(0.0f, 500.0f);
                this.m_timerSource = null;
            }
            if (WakfuGameEntity.getInstance().hasFrame(this)) {
                WakfuGameEntity.getInstance().removeFrame(this);
            }
        }
        catch (Exception e) {
            UITimePointSelectionFrame.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
    }
    
    private void selectEffect(final WakfuEffect effect, final boolean atOnce) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight fight = localPlayer.getCurrentFight();
        if (fight == null) {
            return;
        }
        if (effect == null) {
            this.selectBonusAndRemoveDialog(-1, atOnce);
        }
        else {
            this.sendSelectMessage(effect);
            this.displayEffectSelected(effect.getEffectId(), atOnce);
        }
    }
    
    public void displayEffectSelected(final int effectId, final boolean atOnce) {
        if (this.m_fighter == null) {
            return;
        }
        final Fight fight = this.m_fighter.getCurrentFight();
        if (fight == null) {
            return;
        }
        WakfuEffect effect;
        TimePointEffect[] effects;
        int i;
        for (effect = fight.getTimeline().getTimeScoreGauges().getEffectById(effectId), effects = (TimePointEffect[])PropertiesProvider.getInstance().getObjectProperty("fight.timePointAvailableBonuses"), i = 0; i < effects.length && effects[i].getEffect().getEffectId() != effect.getEffectId(); ++i) {}
        this.selectBonusAndRemoveDialog(i, atOnce);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 18105: {
                if (!this.m_fighter.isControlledByLocalPlayer()) {
                    return false;
                }
                final UITimePointBonusMessage msg = (UITimePointBonusMessage)message;
                final FighterFieldProvider fighter = this.m_fighter.getFighterFieldProvider();
                fighter.setSelectedBonus(msg.getEffect());
                this.selectEffect(msg.getEffect().getEffect(), true);
                WakfuSoundManager.getInstance().playGUISound(600124L);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public CharacterInfo getFighter() {
        return this.m_fighter;
    }
    
    public void setFighter(final CharacterInfo fighter) {
        this.m_fighter = fighter;
        final TimePointEffect[] availableBonuses = this.m_fighter.getCurrentFight().getTimeline().getAvailableBonusesFor(this.m_fighter.getId());
        PropertiesProvider.getInstance().setPropertyValue("fight.timePointAvailableBonuses", availableBonuses);
        PropertiesProvider.getInstance().setPropertyValue("fight.timePointSelectionChooser", fighter);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private void sendSelectMessage(final WakfuEffect effect) {
        final PointEffectSelectionMessage msg = new PointEffectSelectionMessage();
        msg.setFighterId(this.m_fighter.getId());
        msg.setEffectId(effect.getEffectId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("fight.timePointBonusRemainingTime", 1.0f);
            this.m_countDownRunnable = new CountdownRunnable();
            this.m_removingFrame = false;
            long options = 8208L;
            if (this.m_fighter.isControlledByLocalPlayer() || this.m_fighter.getType() == 5) {
                options |= 0x100L;
            }
            (this.m_dialog = (Widget)Xulor.getInstance().load("timePointBonusSelectionDialog", Dialogs.getDialogPath("timePointBonusSelectionDialog"), options, (short)10000)).setX(MasterRootContainer.getInstance().getWidth());
            (this.m_progressBar = (ProgressBar)this.m_dialog.getElementMap().getElement("progressBar")).setInitialValue(1.0f);
            Xulor.getInstance().putActionClass("wakfu.timePointSelection", TimePointSelectionDialogActions.class);
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    synchronized (UITimePointSelectionFrame.m_instance) {
                        if (UITimePointSelectionFrame.this.m_dialog == null) {
                            return;
                        }
                        UITimePointSelectionFrame.this.m_dialog.setPosition(MasterRootContainer.getInstance().getWidth(), Alignment9.EAST.getY(UITimePointSelectionFrame.this.m_dialog.getHeight(), MasterRootContainer.getInstance().getHeight()));
                        UITimePointSelectionFrame.this.m_dialog.addTween(new PositionTween(UITimePointSelectionFrame.this.m_dialog.getX(), UITimePointSelectionFrame.this.m_dialog.getY(), Alignment9.CENTER.getX(UITimePointSelectionFrame.this.m_dialog.getWidth(), MasterRootContainer.getInstance().getWidth()), UITimePointSelectionFrame.this.m_dialog.getY(), UITimePointSelectionFrame.this.m_dialog, 0, 150, TweenFunction.PROGRESSIVE));
                    }
                }
            }, 200L, 1);
            if (this.m_fighter.isControlledByLocalPlayer()) {
                WakfuSoundManager.getInstance().playGUISound(600123L);
                this.m_timerSource = WakfuSoundManager.getInstance().playGUISound(600126L, true);
            }
            if (this.m_remainingMillis <= 0L) {
                this.m_remainingMillis = 30000L;
            }
            this.m_progressBar.setTweenDuration(this.m_remainingMillis);
            this.m_progressBar.setValue(0.0f);
            ProcessScheduler.getInstance().schedule(this.m_countDownRunnable, this.m_remainingMillis, 1);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            synchronized (UITimePointSelectionFrame.m_instance) {
                this.m_dialog = null;
                Xulor.getInstance().unload("timePointBonusSelectionDialog");
                Xulor.getInstance().removeActionClass("wakfu.timePointSelection");
                ProcessScheduler.getInstance().remove(this.m_countDownRunnable);
            }
            if (this.m_timerSource != null) {
                this.m_timerSource.fade(0.0f, 500.0f);
                this.m_timerSource.setStopOnNullGain(true);
                this.m_timerSource = null;
            }
            this.m_progressBar = null;
            this.m_fighter = null;
            this.m_remainingMillis = 0L;
        }
    }
    
    public void setRemainingMillis(final long remainingMillis) {
        this.m_remainingMillis = remainingMillis;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UITimePointSelectionFrame.class);
        m_instance = new UITimePointSelectionFrame();
    }
    
    public class CountdownRunnable implements Runnable
    {
        @Override
        public void run() {
            UITimePointSelectionFrame.this.onChooseTimerEnd();
        }
    }
}
