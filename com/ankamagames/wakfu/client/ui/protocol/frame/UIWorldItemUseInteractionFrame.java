package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.game.item.*;

public class UIWorldItemUseInteractionFrame implements MessageFrame
{
    private static final float[] USE_ITEM_CELL_OK_COLOR;
    private static final float[] USE_ITEM_CELL_KO_COLOR;
    private static final Logger m_logger;
    private static final UIWorldItemUseInteractionFrame m_instance;
    private Item m_item;
    protected CharacterInfo m_character;
    private final ElementSelection m_elementsSelection;
    private final Point3 m_lastTarget;
    private boolean m_frameAdded;
    
    private UIWorldItemUseInteractionFrame() {
        super();
        this.m_lastTarget = new Point3();
        this.m_elementsSelection = new ElementSelection("useItemSelectCell", UIWorldItemUseInteractionFrame.USE_ITEM_CELL_OK_COLOR);
    }
    
    public static UIWorldItemUseInteractionFrame getInstance() {
        return UIWorldItemUseInteractionFrame.m_instance;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public void setCharacter(final CharacterInfo fighter) {
        this.m_character = fighter;
    }
    
    public void setSelectedItem(final Item selectedItem) {
        this.m_item = selectedItem;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    protected EffectContainer getEffectContainer() {
        return this.m_item;
    }
    
    private boolean isUseValid() {
        if (this.m_item == null || this.m_character == null) {
            return false;
        }
        final Point3 position = this.m_character.getPositionConst();
        final int distance = this.m_lastTarget.getDistance(position);
        final ReferenceItem referenceItem = (ReferenceItem)this.m_item.getReferenceItem();
        switch (referenceItem.getUsageTarget()) {
            case DISTANCE: {
                return referenceItem.getUseRangeMin() <= distance && distance <= referenceItem.getUseRangeMax() && Math.abs(position.getZ() - this.m_lastTarget.getZ()) <= this.m_character.getJumpCapacity();
            }
            case WORLD: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    protected void sendUseItemMessage(final int castPositionX, final int castPositionY, final short castPositionZ) {
        final PlayerUseItemRequestMessage netMsg = new PlayerUseItemRequestMessage();
        netMsg.setItemUid(this.m_item.getUniqueId());
        netMsg.setPosition(castPositionX, castPositionY, castPositionZ);
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
    }
    
    protected String getCastMouseIcon() {
        if (this.m_item == null) {
            return null;
        }
        return (String)this.m_item.getFieldValue("iconUrl");
    }
    
    private Point3 getNearestPoint3(final int mouseX, final int mouseY, final boolean mobileSelectable) {
        return WorldSceneInteractionUtils.getNearestPoint3(WakfuClientInstance.getInstance().getWorldScene(), mouseX, mouseY, mobileSelectable);
    }
    
    public Point3 getLastTarget() {
        return new Point3(this.m_lastTarget);
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19994: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                this.selectCell(msg.getMouseX(), msg.getMouseY());
                return false;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (!msg.isButtonLeft()) {
                    WakfuGameEntity.getInstance().removeFrame(this);
                    return false;
                }
                if (this.m_character != null && this.m_elementsSelection.contains(this.m_lastTarget.getX(), this.m_lastTarget.getY(), this.m_lastTarget.getZ()) && this.isUseValid()) {
                    final int castPositionX = this.m_lastTarget.getX();
                    final int castPositionY = this.m_lastTarget.getY();
                    final short castPositionZ = this.m_lastTarget.getZ();
                    UIItemManagementFrame.runItemAction(this.m_character, this.getItem(), new Point3(this.m_lastTarget));
                    if (this.getItem().iterator().hasNext()) {
                        this.sendUseItemMessage(castPositionX, castPositionY, castPositionZ);
                    }
                    final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("chat.item.selfUse", UIChatFrame.getItemFormatedForChatLinkString(this.m_item)));
                    chatMessage.setPipeDestination(4);
                    ChatManager.getInstance().pushMessage(chatMessage);
                }
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean selectCell(final int mouseX, final int mouseY) {
        final Point3 target = this.getNearestPoint3(mouseX, mouseY, true);
        if (target == null) {
            this.m_lastTarget.set(Integer.MAX_VALUE, Integer.MAX_VALUE, (short)32767);
            this.m_elementsSelection.clear();
            return true;
        }
        if (target.equals(this.m_lastTarget)) {
            return true;
        }
        this.m_lastTarget.set(target);
        this.m_elementsSelection.clear();
        if (this.isUseValid()) {
            this.m_elementsSelection.setColor(UIWorldItemUseInteractionFrame.USE_ITEM_CELL_OK_COLOR);
        }
        else {
            this.m_elementsSelection.setColor(UIWorldItemUseInteractionFrame.USE_ITEM_CELL_KO_COLOR);
        }
        if (this.m_item != null && this.m_character != null) {
            this.m_elementsSelection.add(this.m_lastTarget.getX(), this.m_lastTarget.getY(), this.m_lastTarget.getZ());
        }
        return false;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (this.m_frameAdded) {
            return;
        }
        this.m_frameAdded = true;
        final String iconName = this.getCastMouseIcon();
        if (iconName != null) {
            CursorFactory.getInstance().show(CursorFactory.CursorType.HAND, true);
            GraphicalMouseManager.getInstance().showMouseInformation(iconName, null, 10, -30, Alignment9.NORTH_WEST);
        }
        else {
            CursorFactory.getInstance().unlock();
            GraphicalMouseManager.getInstance().hide();
        }
        final AleaWorldSceneWithParallax scene = WakfuClientInstance.getInstance().getWorldScene();
        this.selectCell(scene.getMouseX(), scene.getMouseY());
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!this.m_frameAdded) {
            return;
        }
        this.m_elementsSelection.clear();
        CursorFactory.getInstance().unlock();
        GraphicalMouseManager.getInstance().hide();
        this.m_frameAdded = false;
        this.m_lastTarget.set(Integer.MAX_VALUE, Integer.MAX_VALUE, (short)32767);
    }
    
    static {
        USE_ITEM_CELL_OK_COLOR = new float[] { 0.0f, 0.3f, 0.6f, 0.6f };
        USE_ITEM_CELL_KO_COLOR = new float[] { 1.0f, 0.0f, 0.0f, 0.6f };
        m_logger = Logger.getLogger((Class)UIWorldItemUseInteractionFrame.class);
        m_instance = new UIWorldItemUseInteractionFrame();
    }
}
