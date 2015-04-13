package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.inFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class CellReportMessageHandler extends UsingFightMessageHandler<CellReportMessage, Fight>
{
    private static Logger m_logger;
    private CellParticleSystem m_oldParticleSystem;
    
    @Override
    public boolean onMessage(final CellReportMessage msg) {
        final Point3 targetEltCoords = msg.getCellCoords();
        if (this.m_oldParticleSystem != null && IsoParticleSystemManager.getInstance().containCellParticleSystem(this.m_oldParticleSystem)) {
            this.m_oldParticleSystem.stopAndKill();
            IsoParticleSystemManager.getInstance().removeParticleSystem(this.m_oldParticleSystem.getId());
        }
        final CellParticleSystem particleSystem = IsoParticleSystemFactory.getInstance().getCellParticleSystem(78900);
        particleSystem.setPosition(targetEltCoords.getX(), targetEltCoords.getY(), targetEltCoords.getZ());
        particleSystem.setDuration(2000);
        IsoParticleSystemManager.getInstance().addParticleSystem(particleSystem);
        this.m_oldParticleSystem = particleSystem;
        final Long reporterUniqueId = msg.getReporterUniqueId();
        if (reporterUniqueId != null) {
            if (reporterUniqueId == WakfuGameEntity.getInstance().getLocalPlayer().getId()) {
                CellReportMessageHandler.m_logger.info((Object)("<text color=\"" + ChatConstants.CHAT_FIGHT_INFORMATION_COLOR + "\">" + WakfuTranslator.getInstance().getString("fight.reportCell", msg.getCellCoords().getX(), msg.getCellCoords().getY()) + "</text>"));
            }
            else {
                final CharacterInfo fighterFromId = ((Fight)this.m_concernedFight).getFighterFromId(msg.getReporterUniqueId());
                final String controllerName = (fighterFromId != null) ? fighterFromId.getControllerName() : ("reporterId=" + msg.getReporterUniqueId());
                CellReportMessageHandler.m_logger.info((Object)WakfuTranslator.getInstance().getString("fight.hasReceivedCellReport", "<b color=\"" + ChatConstants.CHAT_FIGHT_INFORMATION_COLOR + "\">" + controllerName + "</b>", msg.getCellCoords().getX(), msg.getCellCoords().getY()));
            }
        }
        return false;
    }
    
    static {
        CellReportMessageHandler.m_logger = Logger.getLogger((Class)CellReportMessageHandler.class);
    }
}
