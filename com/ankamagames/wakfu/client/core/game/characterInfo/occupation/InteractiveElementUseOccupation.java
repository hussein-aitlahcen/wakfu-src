package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.occupation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class InteractiveElementUseOccupation extends AbstractOccupation
{
    protected static final Logger m_logger;
    private long m_estimatedTime;
    private final OccupationInteractiveElement m_machine;
    private final short m_realMachineState;
    private final short m_occupationTypeId;
    
    public InteractiveElementUseOccupation(final OccupationInteractiveElement machine, final short occupationTypeId) {
        super();
        this.m_machine = machine;
        this.m_occupationTypeId = occupationTypeId;
        this.m_realMachineState = this.m_machine.getState();
    }
    
    @Override
    public short getOccupationTypeId() {
        return this.m_occupationTypeId;
    }
    
    @Override
    public boolean isAllowed() {
        if (this.m_machine == null) {
            InteractiveElementUseOccupation.m_logger.error((Object)"[DISTRIBUTION] Impossible d'utiliser une machine de distribution null");
            return false;
        }
        if (this.m_localPlayer.isDead() || this.m_localPlayer.isOnFight()) {
            InteractiveElementUseOccupation.m_logger.info((Object)"[DISTRIBUTION] Un joueur mort ou en combat ne peut utiliser de machine de distribution");
            return false;
        }
        return true;
    }
    
    @Override
    public void begin() {
        this.m_localPlayer.cancelCurrentOccupation(false, true);
        this.m_localPlayer.setCurrentOccupation(this);
        this.m_localPlayer.setDirection(this.m_localPlayer.getPosition().getDirectionTo(this.m_machine.getPosition()));
        ActionVisualHelper.applyActionVisual(this.m_localPlayer.getActor(), this.m_machine.getVisual());
        if (this.m_machine.getUsedState() > 0) {
            this.m_machine.setState(this.m_machine.getUsedState());
        }
        this.m_machine.notifyViews();
        if (this.m_estimatedTime > 0L) {
            this.m_localPlayer.getActionInProgress().startWellAction(this.m_machine.getId(), this.m_estimatedTime);
        }
        else {
            this.m_localPlayer.finishCurrentOccupation();
        }
    }
    
    @Override
    public boolean cancel(final boolean fromServer, final boolean sendMessage) {
        this.m_localPlayer.getActionInProgress().endAction();
        this.m_localPlayer.getActor().setAnimation("AnimStatique");
        this.m_machine.setState(this.m_realMachineState);
        this.m_machine.notifyViews();
        if (sendMessage) {
            final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
            netMsg.setModificationType((byte)3);
            netMsg.setOccupationType(this.getOccupationTypeId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        }
        return true;
    }
    
    @Override
    public boolean finish() {
        final CharacterActor localActor = this.m_localPlayer.getActor();
        this.m_localPlayer.getActionInProgress().endAction();
        this.m_machine.setState(this.m_realMachineState);
        this.m_machine.notifyViews();
        final String animation = AnimationConstants.setAnimationEndSuffix(localActor.getAnimation(), true);
        localActor.setAnimation(animation);
        final OccupationModificationInformationMessage netMsg = new OccupationModificationInformationMessage();
        netMsg.setModificationType((byte)2);
        netMsg.setOccupationType(this.m_occupationTypeId);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        return true;
    }
    
    public void setEstimatedTime(final long estimatedTime) {
        this.m_estimatedTime = estimatedTime;
    }
    
    @Override
    public String toString() {
        return "InteractiveElementUseOccupation{m_estimatedTime=" + this.m_estimatedTime + ", m_machine=" + this.m_machine + ", m_realMachineState=" + this.m_realMachineState + ", m_occupationTypeId=" + this.m_occupationTypeId + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)InteractiveElementUseOccupation.class);
    }
}
