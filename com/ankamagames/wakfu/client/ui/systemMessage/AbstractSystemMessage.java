package com.ankamagames.wakfu.client.ui.systemMessage;

import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;

public abstract class AbstractSystemMessage
{
    public static final String TEXT_WIDGET_ID = "text";
    protected int m_fadeDuration;
    private final boolean m_blocking;
    protected final String m_dialogId;
    protected final short m_dialogModalLevel;
    protected final WakfuSystemMessageManager.SystemMessageType m_type;
    private final MessageHandler UNLOAD_DIALOG_HANDLER;
    
    protected AbstractSystemMessage(final WakfuSystemMessageManager.SystemMessageType type, final boolean blocking, final String dialogId, final short dialogModalLevel) {
        super();
        this.UNLOAD_DIALOG_HANDLER = new MessageHandler() {
            @Override
            public boolean onMessage(final Message message) {
                AbstractSystemMessage.this.clean(AbstractSystemMessage.this.m_dialogId);
                Xulor.getInstance().unload(AbstractSystemMessage.this.m_dialogId);
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
        this.m_blocking = blocking;
        this.m_dialogId = dialogId;
        this.m_dialogModalLevel = dialogModalLevel;
        this.m_type = type;
    }
    
    public void showMessage(final SystemMessageData data) {
        MessageScheduler.getInstance().removeAllClocks(WakfuSystemMessageManager.getInstance());
        MessageScheduler.getInstance().removeAllClocks(this.UNLOAD_DIALOG_HANDLER);
        this.m_fadeDuration = data.getFadeDuration();
        final boolean alreadyLoaded = Xulor.getInstance().isLoaded(this.m_dialogId);
        if (!alreadyLoaded) {
            Xulor.getInstance().load(this.m_dialogId, Dialogs.getDialogPath(this.m_dialogId), 8212L, this.m_dialogModalLevel);
        }
        if (data.getDuration() != Integer.MAX_VALUE) {
            MessageScheduler.getInstance().addClock(WakfuSystemMessageManager.getInstance(), data.getDuration(), 0, 1);
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(this.m_dialogId);
        if (map == null) {
            return;
        }
        this.applyTweens(true, this.m_dialogId);
        final TextWidget tv = (TextWidget)map.getElement("text");
        if (tv != null) {
            tv.setText(data.getMessage());
        }
    }
    
    public void hide(final boolean done) {
        if (Xulor.getInstance().isLoaded(this.m_dialogId)) {
            MessageScheduler.getInstance().removeAllClocks(this.UNLOAD_DIALOG_HANDLER);
            if (done) {
                this.applyTweens(false, this.m_dialogId);
            }
            if (done) {
                MessageScheduler.getInstance().addClock(this.UNLOAD_DIALOG_HANDLER, this.m_fadeDuration, 0, 1);
            }
            else {
                this.clean(this.m_dialogId);
            }
        }
    }
    
    protected abstract void applyTweens(final boolean p0, final String p1);
    
    protected abstract void clean(final String p0);
    
    protected void addParticle(final Widget w, final String file) {
        if (w == null) {
            return;
        }
        final ParticleDecorator decorator = new ParticleDecorator();
        decorator.onCheckOut();
        decorator.setFile(file);
        decorator.setAlignment(Alignment9.CENTER);
        w.getAppearance().add(decorator);
    }
    
    protected void removeParticles(final Widget w) {
        if (w == null) {
            return;
        }
        w.getAppearance().destroyAllDecorators();
    }
    
    protected void applyTween(final Widget w, final boolean in) {
        if (w == null) {
            return;
        }
        final EventDispatcher client = w.getAppearance();
        client.removeTweensOfType(ModulationColorTween.class);
        final Color c1 = new Color();
        final Color c2 = new Color();
        if (!in) {
            c1.set(Color.WHITE.get());
            c2.set(Color.WHITE_ALPHA.get());
        }
        else {
            c1.set(Color.WHITE_ALPHA.get());
            c2.set(Color.WHITE.get());
        }
        client.addTween(new ModulationColorTween(c1, c2, (ModulationColorClient)client, 0, this.m_fadeDuration, 1, false, TweenFunction.PROGRESSIVE));
    }
    
    public boolean isBlocking() {
        return this.m_blocking;
    }
    
    public WakfuSystemMessageManager.SystemMessageType getType() {
        return this.m_type;
    }
    
    protected static void reloadAnm(final AnimatedElementViewer anm) {
        if (anm == null) {
            return;
        }
        anm.getAnimatedElement().forceReloadAnimation();
        final AnmInstance anmInstance = anm.getAnimatedElement().getAnmInstance();
        if (anmInstance != null) {
            anmInstance.forceUpdate();
        }
        anm.removeTweensOfType(ModulationColorTween.class);
    }
}
