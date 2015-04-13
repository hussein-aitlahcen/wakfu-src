package com.ankamagames.wakfu.client.console.command.debug;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class FlyingImageCommand implements Command
{
    protected static final Logger m_logger;
    private static Runnable m_process;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        if (FlyingImageCommand.m_process == null) {
            FlyingImageCommand.m_process = new Runnable() {
                @Override
                public void run() {
                    final Widget widget = (Widget)Xulor.getInstance().getEnvironment().getElementMap("minimapDialog").getElement("window");
                    if (widget == null) {
                        return;
                    }
                    final CharacterActor target = WakfuGameEntity.getInstance().getLocalPlayer().getActor();
                    final FlyingImage.LinkToUIFlyingImageDeformer deformer = new FlyingImage.LinkToUIFlyingImageDeformer(WakfuClientInstance.getInstance().getWorldScene(), new FlyingWidgetWidgetUIDelegate(widget));
                    final AnimatedElementSceneViewManager manager = AnimatedElementSceneViewManager.getInstance();
                    final FlyingImage flyingImage = new FlyingImage(WakfuConfiguration.getInstance().getIconUrl("challengeFlyingImagePath", "defaultIconPath", "challengeProposal"), 32, 32, deformer, 2000);
                    flyingImage.setTarget(target);
                    final HashSet<Adviser> advisers = AdviserManager.getInstance().getAdvisers(target);
                    flyingImage.setWaitingTime(((advisers != null) ? advisers.size() : 0) * 600);
                    AdviserManager.getInstance().addAdviser(flyingImage);
                }
            };
            ProcessScheduler.getInstance().schedule(FlyingImageCommand.m_process, 5000L, -1);
        }
        else {
            ProcessScheduler.getInstance().remove(FlyingImageCommand.m_process);
            FlyingImageCommand.m_process = null;
        }
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FlyingImageCommand.class);
    }
}
