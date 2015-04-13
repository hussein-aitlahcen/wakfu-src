package com.ankamagames.wakfu.client.console.command.commonIn;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;

public class ZoomOutCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final AleaWorldSceneWithParallax worldScene = WakfuClientInstance.getInstance().getWorldScene();
        worldScene.setDesiredZoomFactor(worldScene.getDesiredZoomFactor() - 0.1f);
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
