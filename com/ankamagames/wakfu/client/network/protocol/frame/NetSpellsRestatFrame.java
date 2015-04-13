package com.ankamagames.wakfu.client.network.protocol.frame;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.game.restat.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.restat.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;

public final class NetSpellsRestatFrame extends MessageRunnerFrame
{
    static final Logger m_logger;
    public static final NetSpellsRestatFrame INSTANCE;
    
    private NetSpellsRestatFrame() {
        super(new MessageRunner[] { new SpellsRestatNeededMessageRunner(), new SpellsRestatResultMessageRunner() });
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetSpellsRestatFrame.class);
        INSTANCE = new NetSpellsRestatFrame();
    }
    
    private static class SpellsRestatNeededMessageRunner implements MessageRunner<SpellsRestatNeededMessage>
    {
        @Override
        public boolean run(final SpellsRestatNeededMessage msg) {
            if (msg.isGlobalRestat()) {
                final GlobalSpellRestatComputer computer = new GlobalSpellRestatComputer();
                computer.setToCurrentValues();
                UISpellsRestatFrame.INSTANCE.init(computer);
            }
            else {
                final TObjectLongHashMap<Elements> xpPerElement = msg.getXpPerElement();
                final List<Elements> concernedElements = Arrays.asList(xpPerElement.keys(new Elements[0]));
                final PerElementSpellRestatComputer computer2 = new PerElementSpellRestatComputer(concernedElements);
                for (final Elements element : concernedElements) {
                    final long xp = xpPerElement.get(element);
                    if (xp > 0L) {
                        computer2.setTotalXpForElement(element, xp);
                    }
                }
                UISpellsRestatFrame.INSTANCE.init(computer2);
            }
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 13200;
        }
    }
    
    private static class SpellsRestatResultMessageRunner implements MessageRunner<SpellsRestatResultMessage>
    {
        private static final int RESTAT_INSTANCE_ID = 487;
        
        @Override
        public boolean run(final SpellsRestatResultMessage msg) {
            if (msg.isSuccess()) {
                WakfuGameEntity.getInstance().removeFrame(UISpellsRestatFrame.INSTANCE);
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                localPlayer.unloadAptitudeEffects();
                localPlayer.getSpellInventory().destroyAll();
                localPlayer.getSpellInventory().fromRaw(msg.getRawInventory());
                localPlayer.getSpellInventoryManager().initialize();
                localPlayer.reloadAptitudeEffects(localPlayer.getOwnContext());
                final ArrayList<ShortcutBar> spellsBars = localPlayer.getShortcutBarManager().getSpellsBars(false);
                for (final ShortcutBar bar : spellsBars) {
                    bar.clean();
                }
                localPlayer.updateAllElementMasteries();
                localPlayer.reloadBuffs(localPlayer.getEffectContext());
                if (localPlayer.getInstanceId() == 487) {
                    this.displayMessageBox("notification.restatSpellsSuccess");
                }
                else {
                    this.displayMessageBox("notification.restatSpellsSuccessSimple");
                }
            }
            else {
                Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("notification.restatSpellsFail"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 3L, 102, 1);
            }
            return false;
        }
        
        private void displayMessageBox(final String resultMsgKey) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(resultMsgKey), WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 2L, 102, 1);
        }
        
        @Override
        public int getProtocolId() {
            return 13202;
        }
    }
}
