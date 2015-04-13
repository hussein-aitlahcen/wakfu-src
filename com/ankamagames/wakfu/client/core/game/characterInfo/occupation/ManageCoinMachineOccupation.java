package com.ankamagames.wakfu.client.core.game.characterInfo.occupation;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class ManageCoinMachineOccupation extends AbstractOccupation
{
    private static final Logger m_logger;
    private CoinMachine m_machine;
    
    public ManageCoinMachineOccupation(final CoinMachine machine) {
        super();
        this.m_machine = machine;
    }
    
    @Override
    public short getOccupationTypeId() {
        return 22;
    }
    
    @Override
    public boolean isAllowed() {
        final AbstractOccupation occupation = this.m_localPlayer.getCurrentOccupation();
        return occupation == null || occupation == this;
    }
    
    @Override
    public void begin() {
        ManageCoinMachineOccupation.m_logger.info((Object)"[COIN_MACHINE] Lancement de l'occupation");
        this.m_localPlayer.setCurrentOccupation(this);
    }
    
    @Override
    public boolean cancel(final boolean fromServeur, final boolean sendMessage) {
        ManageCoinMachineOccupation.m_logger.info((Object)"[COIN_MACHINE] On cancel l'occupation");
        if (sendMessage) {
            this.m_machine.fireAction(InteractiveElementAction.STOP_MANAGE, WakfuGameEntity.getInstance().getLocalPlayer());
        }
        return this.finish();
    }
    
    @Override
    public boolean finish() {
        ManageCoinMachineOccupation.m_logger.info((Object)"[COIN_MACHINE] On fini l'occupation");
        return true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ManageCoinMachineOccupation.class);
    }
}
