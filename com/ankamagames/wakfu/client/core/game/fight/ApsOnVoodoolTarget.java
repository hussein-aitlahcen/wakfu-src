package com.ankamagames.wakfu.client.core.game.fight;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public final class ApsOnVoodoolTarget
{
    private static final Logger m_logger;
    public static final ApsOnVoodoolTarget INSTANCE;
    private static final int VOODOOL_BREED_ID = 2357;
    private CellParticleSystem m_cellParticleSystem;
    
    public void applyApsOnVoodoolTarget(final boolean selected, final CriterionUser userSelected) {
        if (userSelected.getBreed().getBreedId() != 2357) {
            return;
        }
        CharacterInfo target = null;
        final RunningEffectManager rem = userSelected.getRunningEffectManager();
        for (final RunningEffect re : rem) {
            if (re.getGenericEffect().getActionId() == RunningEffectConstants.VOODOOL_SPLIT_EFFECT.getId()) {
                final VoodoolSplitEffect voodoolSplitEffect = (VoodoolSplitEffect)re;
                final EffectUser effectUser = voodoolSplitEffect.getTarget();
                if (!(effectUser instanceof CharacterInfo)) {
                    continue;
                }
                target = (CharacterInfo)effectUser;
                break;
            }
        }
        if (target == null) {
            ApsOnVoodoolTarget.m_logger.warn((Object)"On ne trouve pas de target au totem");
        }
        this.cleanCellParticleSystem();
        if (selected && target != null) {
            (this.m_cellParticleSystem = IsoParticleSystemFactory.getInstance().getCellParticleSystem(78900)).setPosition(target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude());
            IsoParticleSystemManager.getInstance().addParticleSystem(this.m_cellParticleSystem);
        }
    }
    
    public void cleanCellParticleSystem() {
        if (this.m_cellParticleSystem != null) {
            if (IsoParticleSystemManager.getInstance().containCellParticleSystem(this.m_cellParticleSystem)) {
                this.m_cellParticleSystem.stopAndKill();
                IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_cellParticleSystem.getId());
            }
            this.m_cellParticleSystem = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ApsOnVoodoolTarget.class);
        INSTANCE = new ApsOnVoodoolTarget();
    }
}
