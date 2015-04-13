package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class OpenBackgroundDisplayItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_backgroundDisplayId;
    private boolean m_running;
    
    public OpenBackgroundDisplayItemAction(final int id) {
        super(id);
        this.m_backgroundDisplayId = 0;
        this.m_running = false;
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.m_running) {
            OpenBackgroundDisplayItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action plusieurs fois d'affil\u00e9", (Throwable)new UnsupportedOperationException());
            return false;
        }
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            OpenBackgroundDisplayItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        this.display();
        this.sendRequest(item.getUniqueId());
        return true;
    }
    
    public void display() {
        final UIBackgroundDisplayFrame displayFrame = UIBackgroundDisplayFrame.getInstance();
        displayFrame.loadBackgroundDisplay(this.m_backgroundDisplayId);
        WakfuGameEntity.getInstance().pushFrame(displayFrame);
    }
    
    public boolean isRunning() {
        return this.m_running;
    }
    
    @Override
    public void parseParameters(final String[] params) {
        this.m_backgroundDisplayId = Integer.valueOf(params[0]);
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.OPEN_BACKGROUND_DISPLAY;
    }
    
    static {
        m_logger = Logger.getLogger((Class)OpenBackgroundDisplayItemAction.class);
    }
}
