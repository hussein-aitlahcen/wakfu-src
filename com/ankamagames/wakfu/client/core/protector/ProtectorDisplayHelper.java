package com.ankamagames.wakfu.client.core.protector;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.client.core.game.protector.snapshot.*;

public class ProtectorDisplayHelper
{
    private static final Logger m_logger;
    
    @Nullable
    public static AnimatedElement createProtectorAnimation(final int protectorId) {
        final AnimatedElement animation = new AnimatedElementWithDirection(GUIDGenerator.getGUID(), 0.0f, 0.0f, 0.0f);
        final String fileName = protectorId + ".anm";
        try {
            animation.load(WakfuConfiguration.getInstance().getString("ANMGUIPath") + fileName, true);
        }
        catch (IOException e) {
            ProtectorDisplayHelper.m_logger.warn((Object)e.getMessage());
            return null;
        }
        catch (PropertyException e2) {
            ProtectorDisplayHelper.m_logger.warn((Object)e2.getMessage());
            return null;
        }
        animation.setGfxId(fileName);
        return animation;
    }
    
    public static void setAnimation(@NotNull final AnimatedElement animation, @NotNull final ProtectorMood protectorMood) {
        animation.setStaticAnimationKey(protectorMood.getAnimation());
        animation.setAnimation(protectorMood.getAnimation());
    }
    
    public static void setDefaultAnimation(final AnimatedElement animation) {
        setAnimation(animation, ProtectorMood.NEUTRAL);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ProtectorDisplayHelper.class);
    }
}
