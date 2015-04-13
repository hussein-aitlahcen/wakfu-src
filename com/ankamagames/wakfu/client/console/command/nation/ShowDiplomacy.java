package com.ankamagames.wakfu.client.console.command.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.diplomacy.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;

public final class ShowDiplomacy implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final Nation myNation = WakfuGameEntity.getInstance().getLocalPlayer().getCitizenComportment().getNation();
        final int[] nationsId = NationManager.INSTANCE.getNationsId();
        for (int i = 0; i < nationsId.length; ++i) {
            final int nationId = nationsId[i];
            final Nation targetNation = NationManager.INSTANCE.getNationById(nationId);
            final NationDiplomacyManager targetManager = targetNation.getDiplomacyManager();
            final NationDiplomacyManager diplomacyManager = myNation.getDiplomacyManager();
            final NationAlignement alignement = diplomacyManager.getAlignment(nationId);
            ConsoleManager.getInstance().log(nationId + " -> " + alignement);
            final NationAlignement requested = targetManager.getRequest(myNation.getNationId());
            if (requested != null) {
                ConsoleManager.getInstance().log(nationId + " attend une r\u00e9ponse pour : " + requested);
            }
            final NationAlignement myRequest = diplomacyManager.getRequest(nationId);
            if (myRequest != null) {
                ConsoleManager.getInstance().log(nationId + " doit r\u00e9pondre pour : " + alignement);
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
