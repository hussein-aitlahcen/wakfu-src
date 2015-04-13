package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.*;

public class UIXPGainFrame implements MessageFrame
{
    private static final UIXPGainFrame INSTANCE;
    protected static final Logger m_logger;
    private final Object m_mutex;
    private Container m_container;
    private ProgressBar m_progressBar;
    private TextWidget m_levelTextView;
    private TextWidget m_xpGainTextView;
    private DialogUnloadListener m_dialogUnloadListener;
    private AbstractTween m_tween;
    private ParticleDecorator m_particle;
    private boolean m_loaded;
    private final Runnable m_runnable;
    
    public static UIXPGainFrame getInstance() {
        return UIXPGainFrame.INSTANCE;
    }
    
    private UIXPGainFrame() {
        super();
        this.m_mutex = new Object();
        this.m_runnable = new Runnable() {
            @Override
            public void run() {
                UIXPGainFrame.this.fadeContainer(false);
                UIXPGainFrame.this.removeAps();
            }
        };
    }
    
    public void onXPGain(final XpModification modification, final float previousPercentage, final float newPercentage) {
        synchronized (this.m_mutex) {
            if (!this.m_loaded) {
                return;
            }
            if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() != null) {
                return;
            }
            this.m_container.setVisible(true);
            this.cancelAllTweens();
            this.m_progressBar.setUseIncreaseProgressTween(false);
            this.m_progressBar.setUseDecreaseProgressTween(false);
            final float initialValue = modification.doesLevelUp() ? 0.0f : previousPercentage;
            this.m_progressBar.setInitialValue(initialValue);
            this.m_progressBar.setUseIncreaseProgressTween(true);
            this.m_progressBar.setUseDecreaseProgressTween(true);
            if (initialValue != newPercentage) {
                (this.m_tween = this.m_progressBar.setValue(newPercentage)).setDuration(2500);
                this.m_tween.addTweenEventListener(new TweenEventListener() {
                    @Override
                    public void onTweenEvent(final AbstractTween tw, final TweenEvent e) {
                        if (e == TweenEvent.TWEEN_ENDED) {
                            ProcessScheduler.getInstance().schedule(UIXPGainFrame.this.m_runnable, 2500L, 1);
                            UIXPGainFrame.this.m_tween = null;
                        }
                    }
                });
            }
            else {
                ProcessScheduler.getInstance().schedule(this.m_runnable, 5000L, 1);
            }
            this.m_xpGainTextView.getAppearance().setModulationColor(new Color(Color.WHITE));
            this.fadeWidget(this.m_xpGainTextView, false, 1500, 1000);
            this.m_levelTextView.setText(WakfuTranslator.getInstance().getString("levelShort.custom", WakfuGameEntity.getInstance().getLocalPlayer().getLevel()));
            this.m_xpGainTextView.setText("+" + modification.getXpDifference());
            this.fadeContainer(true);
            if (modification.doesLevelUp()) {
                this.addAps();
            }
        }
    }
    
    private void removeAps() {
        if (this.m_particle != null) {
            this.m_levelTextView.getAppearance().removeDecorator(this.m_particle);
            this.m_particle = null;
        }
    }
    
    private void addAps() {
        this.removeAps();
        (this.m_particle = new ParticleDecorator()).onCheckOut();
        this.m_particle.setFile("6001051.xps");
        this.m_particle.setAlignment(Alignment9.CENTER);
        this.m_levelTextView.getAppearance().add(this.m_particle);
    }
    
    private void cancelAllTweens() {
        ProcessScheduler.getInstance().remove(this.m_runnable);
        this.removeAps();
        this.m_progressBar.removeTween(this.m_tween);
        this.m_tween = null;
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("xpGainDialog");
        if (map == null) {
            return;
        }
        final ElementMapIterator it = new ElementMapIterator(map);
        while (it.hasNext()) {
            final EventDispatcher next = it.next();
            if (next instanceof Widget) {
                ((Widget)next).getAppearance().removeTweensOfType(ModulationColorTween.class);
            }
        }
    }
    
    private void fadeWidget(final Widget widget, final boolean fadeIn, final int delay, final int duration) {
        final Color modulationColor = widget.getAppearance().getModulationColor();
        final int aValue = (modulationColor != null) ? modulationColor.get() : Color.WHITE.get();
        final int bValue = fadeIn ? Color.WHITE.get() : Color.WHITE_ALPHA.get();
        if (aValue != bValue) {
            final Color a = new Color(aValue);
            final Color b = new Color(bValue);
            widget.getAppearance().removeTweensOfType(ModulationColorTween.class);
            final ModulationColorTween tw = new ModulationColorTween(a, b, widget.getAppearance(), delay, duration, 1, false, TweenFunction.PROGRESSIVE);
            widget.getAppearance().addTween(tw);
        }
    }
    
    private void fadeContainer(final boolean fadeIn) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("xpGainDialog");
        if (map == null) {
            return;
        }
        final ElementMapIterator it = new ElementMapIterator(map);
        while (it.hasNext()) {
            final EventDispatcher next = it.next();
            if (next instanceof Widget && next != this.m_xpGainTextView) {
                final Widget widget = (Widget)next;
                this.fadeWidget(widget, fadeIn, 0, 200);
            }
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            final boolean displayXpBar = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_XP_BAR);
            if (displayXpBar) {
                this.loadDialog();
            }
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.unloadDialog();
        }
    }
    
    public void loadDialog(final boolean load) {
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            return;
        }
        if (load == this.m_loaded) {
            return;
        }
        if (load) {
            this.loadDialog();
        }
        else {
            this.unloadDialog();
        }
    }
    
    private void loadDialog() {
        synchronized (this.m_mutex) {
            Xulor.getInstance().load("xpGainDialog", Dialogs.getDialogPath("xpGainDialog"), 8196L, (short)10000);
            final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("xpGainDialog");
            this.m_container = (Container)map.getElement("container");
            this.m_progressBar = (ProgressBar)map.getElement("progressBar");
            this.m_levelTextView = (TextWidget)map.getElement("levelText");
            this.m_xpGainTextView = (TextWidget)map.getElement("xpGainText");
            this.m_container.setVisible(false);
            this.m_loaded = true;
        }
    }
    
    private void unloadDialog() {
        synchronized (this.m_mutex) {
            this.m_loaded = false;
            Xulor.getInstance().unload("xpGainDialog");
            this.m_container = null;
            this.m_progressBar = null;
            this.m_levelTextView = null;
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new UIXPGainFrame();
        m_logger = Logger.getLogger((Class)UIXPGainFrame.class);
    }
}
