package com.ankamagames.wakfu.client.console.command.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;

public final class NextSelectedFighterCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (!WakfuGameEntity.getInstance().hasFrame(UIFightPlacementFrame.getInstance())) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight currentFight = localPlayer.getCurrentFight();
        if (currentFight == null) {
            return;
        }
        final List<CharacterInfo> controlledCompanions = new ArrayList<CharacterInfo>(currentFight.getControlledCompanions(localPlayer));
        if (controlledCompanions.isEmpty()) {
            return;
        }
        Collections.sort(controlledCompanions, new Comparator<CharacterInfo>() {
            @Override
            public int compare(final CharacterInfo o1, final CharacterInfo o2) {
                return Short.valueOf(o1.getBreedId()).compareTo(Short.valueOf(o2.getBreedId()));
            }
        });
        final BasicCharacterInfo selectedCharacter = UIFightPlacementFrame.getInstance().getSelectedCharacter();
        if (selectedCharacter == null || selectedCharacter == localPlayer) {
            this.changeSelectedCharacter(controlledCompanions.get(0));
        }
        else {
            final int selectedIndex = controlledCompanions.indexOf(selectedCharacter);
            if (selectedIndex == controlledCompanions.size() - 1) {
                this.changeSelectedCharacter(null);
            }
            else {
                this.changeSelectedCharacter(controlledCompanions.get(selectedIndex + 1));
            }
        }
    }
    
    private void changeSelectedCharacter(final CharacterInfo selectedCharacter1) {
        UIFightPlacementFrame.getInstance().setSelectedCharacter(selectedCharacter1);
        UIFightPlacementFrame.getInstance().handleCompanionIconDisplay();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
