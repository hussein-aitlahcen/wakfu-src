package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public final class ExtendsRentDurationItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private long m_rentItemId;
    
    ExtendsRentDurationItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
    }
    
    @Override
    public boolean run(final Item item) {
        final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
        if (character.getBags().getItemFromInventories(item.getUniqueId()) == null) {
            ExtendsRentDurationItemAction.m_logger.error((Object)"[ItemAction] On essaye de lancer une action avec un item qui n'est pas dans les bags");
            return false;
        }
        this.changeMouseAndAddListener(item);
        return true;
    }
    
    private void changeMouseAndAddListener(final Item item) {
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM5, true);
        UIImproveGemFrame.getInstance().setEnableFrame(false);
        final MouseReleasedListener mouseListener = new MouseReleasedListener() {
            @Override
            public boolean run(final Event event) {
                if (event == null) {
                    return false;
                }
                if (event instanceof MouseEvent && ((MouseEvent)event).getButton() == 3) {
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                    CursorFactory.getInstance().unlock();
                    UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                    UIImproveGemFrame.getInstance().setEnableFrame(true);
                }
                else if (event instanceof MouseEvent && ((MouseEvent)event).getButton() == 1) {
                    final Widget widget = MasterRootContainer.getInstance().getMouseOver();
                    final RenderableContainer rc = widget.getParentOfType(RenderableContainer.class);
                    if (rc == null) {
                        return false;
                    }
                    final Object value = rc.getItemValue();
                    if (!(value instanceof Item)) {
                        return false;
                    }
                    final Item rentItem = (Item)value;
                    if (!rentItem.isRent()) {
                        return false;
                    }
                    ExtendsRentDurationItemAction.this.m_rentItemId = rentItem.getUniqueId();
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_RELEASED, this, true);
                    CursorFactory.getInstance().unlock();
                    UIWorldInteractionFrame.getInstance().removeMouseReleasedListener(this);
                    UIImproveGemFrame.getInstance().setEnableFrame(true);
                    ExtendsRentDurationItemAction.this.sendRequest(item.getUniqueId());
                }
                return false;
            }
        };
        UIWorldInteractionFrame.getInstance().addMouseReleasedListener(mouseListener);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_RELEASED, mouseListener, true);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.putLong(this.m_rentItemId);
        return true;
    }
    
    @Override
    public int serializedSize() {
        return super.serializedSize() + 8;
    }
    
    @Override
    public void clear() {
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.EXTENDS_RENT_DURATION;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExtendsRentDurationItemAction.class);
    }
}
