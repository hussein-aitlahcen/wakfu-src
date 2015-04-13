package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.craft.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class SeedItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_craftId;
    private short m_levelMin;
    private int m_resourceId;
    private int m_seedPositionX;
    private int m_seedPositionY;
    
    public SeedItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        if (params.length != 4) {
            SeedItemAction.m_logger.error((Object)"[LD] Mauvais nombre de param\u00e8tres sur une action d'item");
            return;
        }
        final int m_visualId = Integer.parseInt(params[0]);
        this.m_craftId = Integer.parseInt(params[1]);
        this.m_levelMin = Short.parseShort(params[2]);
        this.m_resourceId = Integer.parseInt(params[3]);
    }
    
    @Override
    public boolean run(final Item item) {
        if (!this.checkUsability()) {
            return false;
        }
        if (WakfuGameEntity.getInstance().hasFrame(UISeedInteractionFrame.getInstance()) && UISeedInteractionFrame.getInstance().getItem() == item) {
            WakfuGameEntity.getInstance().removeFrame(UISeedInteractionFrame.getInstance());
            return false;
        }
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM1, true);
        final MouseReleasedListener mouseListener = new MouseReleasedListener() {
            @Override
            public boolean run(final Event event) {
                if (event == null || (event instanceof MouseEvent && ((MouseEvent)event).getButton() == 3)) {
                    UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                    CursorFactory.getInstance().unlock();
                }
                return false;
            }
        };
        UIWorldInteractionFrame.getInstance().addMouseReleasedListener(mouseListener);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, mouseListener, true);
        if (item != null) {
            if (WakfuGameEntity.getInstance().hasFrame(UISeedInteractionFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(UISeedInteractionFrame.getInstance());
            }
            else {
                UISeedInteractionFrame.getInstance().setSelectedItem(item);
                UISeedInteractionFrame.getInstance().selectRange();
                WakfuGameEntity.getInstance().pushFrame(UISeedInteractionFrame.getInstance());
            }
        }
        return true;
    }
    
    private boolean checkUsability() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final CraftHandler craftHandler = player.getCraftHandler();
        final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
        if (exchanger != null) {
            return false;
        }
        if (!craftHandler.contains(this.m_craftId)) {
            ErrorsMessageTranslator.getInstance().pushMessage(21, 3, new Object[0]);
            return false;
        }
        final short level = craftHandler.getLevel(this.m_craftId);
        if (level < this.m_levelMin) {
            ErrorsMessageTranslator.getInstance().pushMessage(4, 3, new Object[0]);
            return false;
        }
        final ReferenceResource res = ReferenceResourceManager.getInstance().getReferenceResource(this.m_resourceId);
        if (res == null) {
            ErrorsMessageTranslator.getInstance().pushMessage(1, 3, new Object[0]);
            return false;
        }
        if (isInHavenWorldButNotOwn()) {
            final String errorMessage = WakfuTranslator.getInstance().getString("error.notInOwnHavenWorld");
            ChatManager.getInstance().pushMessage(errorMessage, 3);
            return false;
        }
        return !WakfuGameEntity.getInstance().getLocalPlayer().isTemporaryTransferInventoryActive();
    }
    
    private static boolean isInHavenWorldButNotOwn() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final short instanceId = localPlayer.getInstanceId();
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final int havenWorldId = guildHandler.getHavenWorldId();
        final HavenWorldDefinition currentInstanceWorld = HavenWorldDefinitionManager.INSTANCE.getWorldFromInstance(instanceId);
        if (currentInstanceWorld == null) {
            return false;
        }
        final HavenWorldDefinition ownWorld = HavenWorldDefinitionManager.INSTANCE.getWorld(havenWorldId);
        return ownWorld != null && ownWorld.getWorldInstanceId() != instanceId;
    }
    
    public int getCraftId() {
        return this.m_craftId;
    }
    
    public short getLevelMin() {
        return this.m_levelMin;
    }
    
    public int getResourceId() {
        return this.m_resourceId;
    }
    
    public void sendRequest(final Item item, final int seedPositionX, final int seedPositionY) {
        UISeedInteractionFrame.getInstance().createTimeOut();
        this.m_seedPositionX = seedPositionX;
        this.m_seedPositionY = seedPositionY;
        this.sendRequest(item.getUniqueId());
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.putInt(this.m_seedPositionX);
        buffer.putInt(this.m_seedPositionY);
        return true;
    }
    
    @Override
    public int serializedSize() {
        return super.serializedSize() + 4 + 4;
    }
    
    @Override
    public void clear() {
        this.m_seedPositionX = 0;
        this.m_seedPositionY = 0;
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.SEED_ACTION;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SeedItemAction.class);
    }
}
