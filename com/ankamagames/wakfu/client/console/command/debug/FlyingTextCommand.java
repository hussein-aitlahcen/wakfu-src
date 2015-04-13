package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class FlyingTextCommand implements Command
{
    protected static final Logger m_logger;
    private static Runnable m_process;
    private static final int MAX_CLAMPING_VALUE = 500;
    private static final float MIN_SCALE = 0.65f;
    private static final float MAX_SCALE = 1.3f;
    private int currentValue;
    
    private float getScale(final int value) {
        final int clampedValue = MathHelper.clamp(value, 0, 500);
        final int step = clampedValue / 50;
        final float percentage = step * 50 / 500.0f;
        return MathHelper.lerp(0.65f, 1.3f, percentage);
    }
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (FlyingTextCommand.m_process == null) {
            FlyingTextCommand.m_process = new Runnable() {
                @Override
                public void run() {
                    FlyingTextCommand.this.currentValue = (FlyingTextCommand.this.currentValue - 10) % 100;
                    final int value = FlyingTextCommand.this.currentValue;
                    final FlyingTextDeformer deformer = new FlyingText.DragonicaTextDeformer(0, 400, 0, 80, FlyingTextCommand.this.getScale(value), 100);
                    final FlyingText flyingText = new FlyingText(FontFactory.createFont("lstw", 0, 55), String.valueOf(value), deformer, 1000);
                    flyingText.setColor(1.0f, 0.0f, 0.0f, 1.0f);
                    final CharacterActor actor = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
                    flyingText.setTarget(actor);
                    AdviserManager.getInstance().addAdviser(flyingText);
                }
            };
            ProcessScheduler.getInstance().schedule(FlyingTextCommand.m_process, 500L, -1);
        }
        else {
            ProcessScheduler.getInstance().remove(FlyingTextCommand.m_process);
            FlyingTextCommand.m_process = null;
            this.currentValue = 0;
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FlyingTextCommand.class);
    }
}
