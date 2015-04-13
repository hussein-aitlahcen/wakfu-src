package com.ankamagames.wakfu.client.console.command.display;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ColorizePictoCommand implements Command
{
    private static float alpha;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final long x = localPlayer.getWorldCellX();
        final long y = localPlayer.getWorldCellY();
        final long z = localPlayer.getWorldCellAltitude();
        final long hash = HighLightManager.getHandle((int)x, (int)y, (int)z);
        if (ColorizePictoCommand.alpha == 1.0f) {
            ColorizePictoCommand.alpha = 0.2f;
        }
        else {
            ColorizePictoCommand.alpha = 1.0f;
        }
        HiddenElementManager.getInstance().set(hash, 1.0f, 1.0f, 1.0f, ColorizePictoCommand.alpha);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        ColorizePictoCommand.alpha = 1.0f;
    }
}
