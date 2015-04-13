package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class SearchTreasureItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private int m_seedPositionX;
    private int m_seedPositionY;
    
    public SearchTreasureItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        if (params.length != 2) {
            SearchTreasureItemAction.m_logger.error((Object)"[LD] Mauvais nombre de param\u00e8tres sur une action d'item");
        }
    }
    
    @Override
    public boolean run(final Item item) {
        if (!this.checkUsability()) {
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
            if (WakfuGameEntity.getInstance().hasFrame(UISearchTreasureInteractionFrame.INSTANCE)) {
                WakfuGameEntity.getInstance().removeFrame(UISearchTreasureInteractionFrame.INSTANCE);
            }
            else {
                UISearchTreasureInteractionFrame.INSTANCE.setSelectedItem(item);
                UISearchTreasureInteractionFrame.INSTANCE.selectRange();
                WakfuGameEntity.getInstance().pushFrame(UISearchTreasureInteractionFrame.INSTANCE);
            }
        }
        return true;
    }
    
    private boolean checkUsability() {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
        return exchanger == null;
    }
    
    public void sendRequest(final Item item, final int seedPositionX, final int seedPositionY) {
        UISearchTreasureInteractionFrame.INSTANCE.createTimeOut();
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
        return ItemActionConstants.SEARCH_TREASURE;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SearchTreasureItemAction.class);
    }
}
