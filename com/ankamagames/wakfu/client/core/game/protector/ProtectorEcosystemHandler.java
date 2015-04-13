package com.ankamagames.wakfu.client.core.game.protector;

import com.ankamagames.wakfu.client.core.protector.ecosystem.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public class ProtectorEcosystemHandler extends AbstractProtectorEcosystemHandler
{
    private ProtectorEcosystemView m_view;
    
    public ProtectorEcosystemHandler(final ProtectorBase protector) {
        super(protector);
    }
    
    public void requestProtectMonsterFamily(final int monsterFamilyId) {
        this.sendActionRequest(ProtectorEcosystemAction.PROTECT_MONSTER_FAMILY, monsterFamilyId);
    }
    
    public void requestProtectResourceFamily(final int resourceFamilyId) {
        this.sendActionRequest(ProtectorEcosystemAction.PROTECT_RESOURCE_FAMILY, resourceFamilyId);
    }
    
    public void requestUnprotectMonsterFamily(final int monsterFamilyId) {
        this.sendActionRequest(ProtectorEcosystemAction.UNPROTECT_MONSTER_FAMILY, monsterFamilyId);
    }
    
    public void requestUnprotectResourceFamily(final int resourceFamilyId) {
        this.sendActionRequest(ProtectorEcosystemAction.UNPROTECT_RESOURCE_FAMILY, resourceFamilyId);
    }
    
    public void requestReintroduceMonsterFamily(final int monsterFamilyId) {
        this.sendActionRequest(ProtectorEcosystemAction.REINTRODUCE_MONSTER_FAMILY, monsterFamilyId);
    }
    
    public void requestReintroduceResourceFamily(final int monsterFamilyId) {
        this.sendActionRequest(ProtectorEcosystemAction.REINTRODUCE_RESOURCE_FAMILY, monsterFamilyId);
    }
    
    @Override
    public boolean reintroduceMonsterFamily(final BasicCharacterInfo user, final int monsterFamilyId) {
        return true;
    }
    
    @Override
    public boolean reintroduceResourceFamily(final BasicCharacterInfo user, final int resourceFamilyId) {
        return true;
    }
    
    @Override
    public boolean canMonsterFamilyBeReintroduced(final int monsterFamilyId) {
        return false;
    }
    
    @Override
    public boolean canResourceFamilyBeReintroduced(final int resourceFamilyId) {
        return false;
    }
    
    private void sendActionRequest(final ProtectorEcosystemAction action, final int familyId) {
        final ProtectorEcosystemActionRequestMessage request = new ProtectorEcosystemActionRequestMessage();
        request.setProtectorId(this.m_protector.getId());
        request.setActionId(action.getId());
        request.setFamilyId(familyId);
        WakfuClientInstance.getInstance();
        WakfuClientInstance.getGameEntity().getNetworkEntity().sendMessage(request);
    }
    
    public ProtectorEcosystemView getView() {
        if (this.m_view == null) {
            this.m_view = new ProtectorEcosystemView(this.m_protector);
        }
        return this.m_view;
    }
}
