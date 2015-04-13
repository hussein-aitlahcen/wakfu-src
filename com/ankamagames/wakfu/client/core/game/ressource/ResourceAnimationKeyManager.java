package com.ankamagames.wakfu.client.core.game.ressource;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ResourceAnimationKeyManager
{
    private static final Logger m_logger;
    private static final ResourceAnimationKeyManager m_instance;
    public static final byte MAX_STEP = 5;
    public static final byte DEATH_KEY = 0;
    public static final byte GROW_KEY = 1;
    public static final byte STEP_KEY = 2;
    public static final byte REGRESSION_KEY = 3;
    public static final byte HIT_KEY = 4;
    public final TIntObjectHashMap<String> m_animationKeys;
    
    private ResourceAnimationKeyManager() {
        super();
        this.m_animationKeys = new TIntObjectHashMap<String>(45);
        this.fill((byte)0);
        this.fill((byte)1);
        this.fill((byte)2);
        this.fill((byte)4);
        this.fillTransition((byte)3);
        this.m_animationKeys.compact();
    }
    
    private void fill(final byte key) {
        final String animName = this.getAnimName(key);
        for (byte step = 0; step < 5; ++step) {
            this.m_animationKeys.put(MathHelper.getIntFromFourByte(key, step, (byte)0, (byte)0), animName + step);
        }
    }
    
    private void fillTransition(final byte key) {
        final String animName = this.getAnimName(key);
        for (byte currentStep = 0; currentStep < 5; ++currentStep) {
            for (byte nextStep = 0; nextStep < 5; ++nextStep) {
                this.m_animationKeys.put(MathHelper.getIntFromFourByte(key, currentStep, nextStep, (byte)0), animName + currentStep + "_" + nextStep);
            }
        }
    }
    
    private String getAnimName(final byte key) {
        switch (key) {
            case 0: {
                return "AnimMort";
            }
            case 1: {
                return "AnimPousse";
            }
            case 2: {
                return "AnimEtape";
            }
            case 4: {
                return "AnimHit";
            }
            case 3: {
                return "AnimRegression";
            }
            default: {
                ResourceAnimationKeyManager.m_logger.error((Object)("type d'animation inconnue " + key));
                return "";
            }
        }
    }
    
    public String getAnimationKey(final byte key, final byte currentStep) {
        if (currentStep >= 5) {
            return this.getAnimName(key) + currentStep;
        }
        return this.m_animationKeys.get(MathHelper.getIntFromFourByte(key, currentStep, (byte)0, (byte)0));
    }
    
    public String getAnimationKey(final byte key, final byte currentStep, final byte nextStep) {
        if (currentStep >= 5 || nextStep >= 5) {
            return this.getAnimName(key) + currentStep + "_" + nextStep;
        }
        return this.m_animationKeys.get(MathHelper.getIntFromFourByte(key, currentStep, nextStep, (byte)0));
    }
    
    public static ResourceAnimationKeyManager getInstance() {
        return ResourceAnimationKeyManager.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ResourceAnimationKeyManager.class);
        m_instance = new ResourceAnimationKeyManager();
    }
}
