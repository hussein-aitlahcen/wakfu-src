package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.baseImpl.graphics.ui.*;

public class ShowXulorSceneCommand implements Command
{
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final UIScene xulorScene = WakfuClientInstance.getInstance().getXulorScene();
        if (args.size() == 3) {
            if (args.get(2).equals("0")) {
                WakfuClientInstance.getInstance().getRenderer().removeScene(xulorScene);
            }
            else {
                WakfuClientInstance.getInstance().getRenderer().pushScene(xulorScene, true);
            }
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
}
