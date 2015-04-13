package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class ModificationRunnable implements Runnable
{
    public static final ModificationRunnable INSTANCE;
    public static final int MODIFICATION_START_DELAY = 250;
    private UIMessage m_uiMessage;
    private boolean m_running;
    private long m_startTime;
    private boolean m_messageSent;
    private static final int DELTA_DELAY_MAX_VALUE = 225;
    private static final int TIME_BEFORE_STARTING = 350;
    private static final int TIME_TO_REACH_DELTA_DELAY_MAX_VALUE = 3500;
    private Button m_currentPressedButton;
    private final EventListener m_mouseReleasedListener;
    
    public ModificationRunnable() {
        super();
        this.m_mouseReleasedListener = new EventListener() {
            @Override
            public boolean run(final Event event) {
                ModificationRunnable.this.cleanModificationRunnable();
                return false;
            }
        };
    }
    
    public void initAndStart(final UIMessage msg, final Button target) {
        this.m_startTime = System.currentTimeMillis();
        this.m_messageSent = false;
        this.m_uiMessage = msg;
        this.m_currentPressedButton = target;
        ProcessScheduler.getInstance().schedule(this, 250L);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, true);
        this.m_running = true;
        this.run();
    }
    
    @Override
    public void run() {
        if (!this.m_running) {
            if (!this.m_messageSent) {
                this.sendMessage();
            }
            ProcessScheduler.getInstance().remove(this);
            return;
        }
        final long pastTime = System.currentTimeMillis() - this.m_startTime;
        if (pastTime < 350L) {
            return;
        }
        this.sendMessage();
        final int deltaDelay = (int)(pastTime / 3500.0f * 225.0f);
        if (deltaDelay > 0 && deltaDelay <= 225) {
            ProcessScheduler.getInstance().remove(this);
            ProcessScheduler.getInstance().schedule(this, 250 - deltaDelay);
        }
    }
    
    private void sendMessage() {
        Worker.getInstance().pushMessage(this.m_uiMessage);
        this.m_messageSent = true;
    }
    
    public boolean isRunning() {
        return this.m_running;
    }
    
    public void stop() {
        if (!this.m_messageSent) {
            this.sendMessage();
        }
        this.m_running = false;
    }
    
    public final void cleanModificationRunnable() {
        this.m_currentPressedButton.getAppearance().exit();
        ProcessScheduler.getInstance().remove(this);
        this.stop();
        MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this.m_mouseReleasedListener, true);
    }
    
    static {
        INSTANCE = new ModificationRunnable();
    }
}
