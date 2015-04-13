package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.handlers.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.event.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class CNationBuffsHandler extends NationBuffsHandler
{
    public CNationBuffsHandler(final Nation nation) {
        super(nation);
        this.registerEventHandler(ProtectorNationBuffEventHandler.INSTANCE);
    }
    
    @Override
    protected void replaceProtectorBuffs(final int protectorId, final int[] buffsId) {
        if (buffsId == null || buffsId.length == 0) {
            this.clearProtectorBuffs(protectorId);
        }
        final IntArray currentBuffs = this.getOrCreateProtectorBuffs(protectorId);
        currentBuffs.clear();
        for (int buffsCount = buffsId.length, i = 0; i < buffsCount; ++i) {
            currentBuffs.put(buffsId[i]);
        }
    }
    
    @Override
    public void requestAddProtectorBuffs(final int protectorId, final int[] buffsId) {
        throw new UnsupportedOperationException("Le client ne doit pas demander directement des modifs sur les buffs de nation. Il doit passer par le protecteur");
    }
    
    @Override
    public void requestRemoveProtectorBuffs(final int protectorId, final int[] buffsId) {
        throw new UnsupportedOperationException("Le client ne doit pas demander directement des modifs sur les buffs de nation. Il doit passer par le protecteur");
    }
    
    @Override
    public void requestClearProtectorBuffs(final int protectorId) {
        throw new UnsupportedOperationException("Le client ne doit pas demander directement des modifs sur les buffs de nation. Il doit passer par le protecteur");
    }
    
    @Override
    public void requestReplaceProtectorBuffs(final int protectorId, final int[] buffsId) {
        this.replaceProtectorBuffs(protectorId, buffsId);
        final LocalPlayerCharacter lcp = WakfuGameEntity.getInstance().getLocalPlayer();
        if (lcp != null) {
            lcp.reloadProtectorBuffs();
        }
    }
}
