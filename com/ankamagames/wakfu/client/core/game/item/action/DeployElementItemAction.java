package com.ankamagames.wakfu.client.core.game.item.action;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.room.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.action.*;

public class DeployElementItemAction extends AbstractClientItemAction
{
    private static final Logger m_logger;
    private long m_elementTemplateId;
    private Point3 m_position;
    
    public DeployElementItemAction(final int id) {
        super(id);
    }
    
    @Override
    public void parseParameters(final String[] params) {
        this.m_elementTemplateId = Long.parseLong(params[0]);
    }
    
    private static boolean isInOwnDimensionalBag() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return localPlayer.getVisitingDimentionalBag() == localPlayer.getOwnedDimensionalBag();
    }
    
    private static boolean isInOwnHavenWorld() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final short instanceId = localPlayer.getInstanceId();
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final int havenWorldId = guildHandler.getHavenWorldId();
        final HavenWorldDefinition world = HavenWorldDefinitionManager.INSTANCE.getWorld(havenWorldId);
        return world != null && world.getWorldInstanceId() == instanceId;
    }
    
    private UIInteractionFrame getInteractionFrame() {
        if (isInOwnDimensionalBag()) {
            return UIDimensionalBagInteractionFrame.getInstance();
        }
        return UIHavenWorldInteractionFrame.getInstance();
    }
    
    @Override
    public boolean run(final Item item) {
        final boolean inOwnHavenWorld = isInOwnHavenWorld();
        if (!isInOwnDimensionalBag() && !inOwnHavenWorld) {
            final String errorMessage = WakfuTranslator.getInstance().getString("error.deploy.notInBag");
            ChatManager.getInstance().pushMessage(errorMessage, 3);
            return false;
        }
        final UIInteractionFrame frame = this.getInteractionFrame();
        if (WakfuGameEntity.getInstance().hasFrame(frame)) {
            WakfuGameEntity.getInstance().removeFrame(frame);
            return false;
        }
        final WakfuClientMapInteractiveElement element = WakfuClientInteractiveElementFactory.getInstance().createDummyInteractiveElement(this.m_elementTemplateId);
        if (element == null) {
            DeployElementItemAction.m_logger.error((Object)("TemplateId=" + this.m_elementTemplateId + " inconnu"));
            return false;
        }
        if (element instanceof MerchantDisplay && inOwnHavenWorld) {
            final String errorMessage2 = WakfuTranslator.getInstance().getString("error.cannotDeployMerchantDisplayInHavenWorld");
            ChatManager.getInstance().pushMessage(errorMessage2, 3);
            element.release();
            return false;
        }
        element.setOverHeadable(false);
        element.notifyViews();
        for (final ClientInteractiveElementView view : element.getViews()) {
            if (view instanceof ClientInteractiveAnimatedElementSceneView) {
                final ClientInteractiveAnimatedElementSceneView clientView = (ClientInteractiveAnimatedElementSceneView)view;
                AnimatedElementSceneViewManager.getInstance().addElement(clientView);
            }
        }
        final ItemizableInfo itemizableInfo = element.getOrCreateItemizableInfo();
        frame.setElement(itemizableInfo);
        frame.setOnValidate(new Runnable() {
            @Override
            public void run() {
                for (final ClientInteractiveElementView view : element.getViews()) {
                    if (view instanceof ClientInteractiveAnimatedElementSceneView) {
                        final ClientInteractiveAnimatedElementSceneView clientView = (ClientInteractiveAnimatedElementSceneView)view;
                        AnimatedElementSceneViewManager.getInstance().removeElement(clientView);
                    }
                }
                DeployElementItemAction.this.sendRequest(item.getUniqueId(), itemizableInfo.getDragInfo().getDragPoint());
                element.release();
            }
        });
        frame.setOnCancel(new Runnable() {
            @Override
            public void run() {
                final RoomContentResult cellResult = frame.getCellResult();
                if (cellResult != null && cellResult.hasErrorKey()) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString(cellResult.getErrorKey()), 3);
                }
                for (final ClientInteractiveElementView view : element.getViews()) {
                    if (view instanceof ClientInteractiveAnimatedElementSceneView) {
                        final ClientInteractiveAnimatedElementSceneView clientView = (ClientInteractiveAnimatedElementSceneView)view;
                        AnimatedElementSceneViewManager.getInstance().removeElement(clientView);
                    }
                }
                element.release();
            }
        });
        WakfuGameEntity.getInstance().pushFrame(frame);
        return true;
    }
    
    public void sendRequest(final long itemId, final Point3 dragPoint) {
        this.m_position = dragPoint;
        this.sendRequest(itemId);
    }
    
    @Override
    public boolean serialize(final ByteBuffer buffer) {
        super.serialize(buffer);
        buffer.putInt(this.m_position.getX());
        buffer.putInt(this.m_position.getY());
        buffer.putShort(this.m_position.getZ());
        return true;
    }
    
    @Override
    public int serializedSize() {
        return super.serializedSize() + 4 + 4 + 2;
    }
    
    @Override
    public void clear() {
        this.m_position = null;
    }
    
    @Override
    public ItemActionConstants getType() {
        return ItemActionConstants.DEPLOY_ELEMENT;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DeployElementItemAction.class);
    }
}
