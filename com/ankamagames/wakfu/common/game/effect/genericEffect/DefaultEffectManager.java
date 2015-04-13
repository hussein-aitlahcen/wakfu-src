package com.ankamagames.wakfu.common.game.effect.genericEffect;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class DefaultEffectManager
{
    private static final Logger m_logger;
    private static DefaultEffectManager ourInstance;
    private final TIntObjectHashMap<WakfuStandardEffect> m_defaultEffects;
    
    public static DefaultEffectManager getInstance() {
        return DefaultEffectManager.ourInstance;
    }
    
    private DefaultEffectManager() {
        super();
        this.m_defaultEffects = new TIntObjectHashMap<WakfuStandardEffect>();
        this.addDefaultEffect(DefaultEffect.getInstance());
        this.addDefaultEffect(DefaultFightInstantEffectAndDontTrigger.getInstance());
        this.addDefaultEffect(DefaultFightInstantEffectWithChatNotif.getInstance());
        this.addDefaultEffect(DefaultFightInstantEffectWithChatNotifAndDontTrigger.getInstance());
        this.addDefaultEffect(DefaultFightOneFullTurnEffect.getInstance());
        this.addDefaultEffect(DefaultFightOneTurnEffect.getInstance());
        this.addDefaultEffect(DefaultFightUsableEffect.getInstance());
        this.addDefaultEffect(DefaultWorldAndFightUsableEffect.getInstance());
        this.addDefaultEffect(DefaultWorldUsableEffect.getInstance());
        this.addDefaultEffect(EfficencyGenericEffect.getInstance());
        this.addDefaultEffect(DefaultEffectWithoutChatNotification.getInstance());
        this.addDefaultEffect(DefaultFightInstantEffectWithoutChatNotif.getInstance());
    }
    
    void addDefaultEffect(final WakfuStandardEffect effect) throws IllegalArgumentException {
        if (this.m_defaultEffects.containsKey(effect.getEffectId())) {
            throw new IllegalArgumentException("On ne peut pas ajouter un defaultEffect alors qu'un autre est deja enregistr\u00e9 avec le meme id");
        }
        this.m_defaultEffects.put(effect.getEffectId(), effect);
    }
    
    public boolean isDefaultEffect(final int effectId) {
        return this.m_defaultEffects.containsKey(effectId);
    }
    
    public WakfuStandardEffect getDefaultEffect(final int effectId) {
        return this.m_defaultEffects.get(effectId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)DefaultEffectManager.class);
        DefaultEffectManager.ourInstance = new DefaultEffectManager();
    }
}
