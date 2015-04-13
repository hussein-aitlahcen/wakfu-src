package com.ankamagames.wakfu.client.console.command.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.nation.*;

public final class ShowCitizenScores implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CitizenComportment citizen = localPlayer.getCitizenComportment();
        final int[] nationsId = NationManager.INSTANCE.getNationsId();
        for (int i = 0; i < nationsId.length; ++i) {
            final int nationId = nationsId[i];
            final int score = citizen.getCitizenScoreForNation(nationId);
            ConsoleManager.getInstance().log(nationId + " score : " + score);
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
