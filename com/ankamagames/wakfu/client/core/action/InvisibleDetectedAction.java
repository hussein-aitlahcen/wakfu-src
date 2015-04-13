package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;

public class InvisibleDetectedAction extends AbstractFightTimedAction
{
    private static FightLogger m_fightLogger;
    private static long DETECTION_ANIMATION_DURATION;
    private final long[] m_detectorsId;
    private final Point3 m_detectedPosition;
    
    public InvisibleDetectedAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long[] detectors, final Point3 detectedPosition) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_detectorsId = detectors;
        this.m_detectedPosition = detectedPosition;
    }
    
    public long onRun() {
        final CharacterInfo detected = this.getFighterById(this.getTargetId());
        if (detected != null) {
            if (this.consernLocalPlayer()) {
                if (detected != WakfuGameEntity.getInstance().getLocalPlayer()) {
                    String message = WakfuTranslator.getInstance().getString("fight.invisible.detected.by", detected.getControllerName());
                    for (byte i = 0; i < this.m_detectorsId.length; ++i) {
                        final CharacterInfo characterInfo = this.getFighterById(this.m_detectorsId[i]);
                        if (characterInfo != null) {
                            if (i > 0) {
                                message += ",";
                            }
                            message += characterInfo.getControllerName();
                        }
                    }
                    InvisibleDetectedAction.m_fightLogger.info(message);
                }
                else {
                    final String message = WakfuTranslator.getInstance().getString("fight.invisible.detected", detected.getControllerName());
                    InvisibleDetectedAction.m_fightLogger.info(message);
                }
                final FreeParticleSystem freeParticleSystem = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(78900);
                freeParticleSystem.setPosition(this.m_detectedPosition.getX(), this.m_detectedPosition.getY(), this.m_detectedPosition.getZ());
                freeParticleSystem.setDuration(2000);
                freeParticleSystem.setFightId(this.getFightId());
                IsoParticleSystemManager.getInstance().addParticleSystem(freeParticleSystem);
            }
            return InvisibleDetectedAction.DETECTION_ANIMATION_DURATION;
        }
        return 0L;
    }
    
    static {
        InvisibleDetectedAction.m_fightLogger = new FightLogger();
        InvisibleDetectedAction.DETECTION_ANIMATION_DURATION = 1000L;
    }
}
