package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.guildStorage.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import com.ankamagames.wakfu.common.game.havenWorld.buff.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.framework.kernel.*;

public class NetHavenWorldFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final NetHavenWorldFrame INSTANCE;
    private ManageHavenWorldOccupation m_cacheOccupation;
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 15650: {
                final CharacterEnterHavenWorldMessage msg = (CharacterEnterHavenWorldMessage)message;
                onCharacterEnterHavenWorldMessage(msg);
                return false;
            }
            case 5510: {
                final HavenWorldTopologyUpdateMessage msg2 = (HavenWorldTopologyUpdateMessage)message;
                this.onHavenWorldTopologyUpdateMessage(msg2);
                return false;
            }
            case 5512: {
                final HavenWorldBuildingCreationMessage msg3 = (HavenWorldBuildingCreationMessage)message;
                this.onHavenWorldBuildingAddedMessage(msg3);
                return false;
            }
            case 5514: {
                this.onHavenWorldBuildingRemovedMessage((HavenWorldBuildingRemovedMessage)message);
                return false;
            }
            case 5524: {
                this.onHavenWorldBuildingEquippedMessage((HavenWorldBuildingEquippedMessage)message);
                return false;
            }
            case 5516: {
                final HavenWorldEditorMessage havenWorldEditorMessage = (HavenWorldEditorMessage)message;
                final HavenWorld world = havenWorldEditorMessage.getWorld();
                if (havenWorldEditorMessage.getError() != HavenWorldError.NO_ERROR) {
                    final String errorString = WakfuTranslator.getInstance().getString("havenWorldForbidden");
                    Xulor.getInstance().msgBox(errorString, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 1027L, 102, 1);
                    return false;
                }
                if (this.m_cacheOccupation == null) {
                    return false;
                }
                this.m_cacheOccupation.getUiFrame().setWorld(world);
                if (this.m_cacheOccupation.isAllowed()) {
                    this.m_cacheOccupation.begin();
                }
                else {
                    this.m_cacheOccupation.cancel(false, true);
                }
                return false;
            }
            case 15652: {
                final HavenWorldManageActionResult havenWorldManageActionResult = (HavenWorldManageActionResult)message;
                final HavenWorldError error = havenWorldManageActionResult.getError();
                if (error == HavenWorldError.NO_ERROR) {
                    final HavenWorldAction action = havenWorldManageActionResult.getAction();
                    if (WakfuGameEntity.getInstance().hasFrame(UIWorldEditorFrame.getInstance())) {
                        UIWorldEditorFrame.getInstance().onSuccess(action);
                    }
                    UIWorldEditorFrame.getInstance().onTaskEnded();
                    return false;
                }
                if (WakfuGameEntity.getInstance().hasFrame(UIWorldEditorFrame.getInstance())) {
                    final HavenWorldAction action = havenWorldManageActionResult.getAction();
                    UIWorldEditorFrame.getInstance().onError(action, error);
                }
                displayError(error);
                return false;
            }
            case 5522: {
                final HavenWorldUpdateMessage msg4 = (HavenWorldUpdateMessage)message;
                if (this.m_cacheOccupation == null || this.m_cacheOccupation.getUiFrame() == null) {
                    return false;
                }
                final HavenWorld world = this.m_cacheOccupation.getUiFrame().getWorld();
                if (world.getWorldInstanceId() != msg4.getWorldId()) {
                    return false;
                }
                final HavenWorldController controller = new HavenWorldController(world);
                controller.setResources(msg4.getResources());
                return false;
            }
            case 20080: {
                final GuildStorageMoneyResultMessage guildStorageMoneyResultMessage = (GuildStorageMoneyResultMessage)message;
                final int money = guildStorageMoneyResultMessage.getMoney();
                UIWorldEditorFrame.getInstance().setGuildMoney(money);
                if (WakfuGameEntity.getInstance().hasFrame(UIWorldEditorFrame.getInstance())) {
                    UIWorldEditorFrame.getInstance().refreshCatalogAvailability();
                }
                return false;
            }
            case 15655: {
                this.havenWorldBuildingFactorUpdate((HavenWorldBuildingFactorUpdate)message);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void havenWorldBuildingFactorUpdate(final HavenWorldBuildingFactorUpdate msg) {
        final HavenWorldZoneBuffs buffs = HavenWorldZoneBuffsManager.INSTANCE.getBuffs(msg.getWorldId());
        buffs.setAdminBuildDurationPercentModificator(msg.getFactor());
    }
    
    private static void displayError(final HavenWorldError error) {
        String errorString = null;
        switch (error) {
            case MONEY_NEEDED: {
                if (WakfuGameEntity.getInstance().hasFrame(UIWorldEditorFrame.getInstance())) {
                    errorString = "havenWorldErrorWait";
                    break;
                }
                errorString = "havenWorldMoneyNeeded";
                break;
            }
            case NO_ACTIONS:
            case ACTION_IN_PROGRESS: {
                errorString = "havenWorldErrorWait";
                break;
            }
            case BUILDING_ELEMENT_ALREADY_EXIST:
            case BUILDING_NOT_FOUND:
            case BUILDING_ELEMENT_NOT_FOUND:
            case BUILDING_ELEMENT_REMAINING:
            case PARTITION_NOT_FOUND:
            case PARTITION_ALREADY_EXIST:
            case BUILDING_ALREADY_EXIST: {
                NetHavenWorldFrame.m_logger.error((Object)"L'action dans l'\u00e9diteur de havre monde a \u00e9chou\u00e9 !");
                errorString = "havenWorldErrorWait";
                WakfuGameEntity.getInstance().removeFrame(UIWorldEditorFrame.getInstance());
                break;
            }
            case INTERACTIVE_ELEMENTS_PRESENT: {
                NetHavenWorldFrame.m_logger.error((Object)"L'action dans l'\u00e9diteur de havre monde a \u00e9chou\u00e9 !");
                errorString = "havenWorld.error.iePresent";
                break;
            }
            case BUILDING_NEEDED:
            case WORKERS_NEEDED: {
                NetHavenWorldFrame.m_logger.error((Object)"L'action dans l'\u00e9diteur de havre monde a \u00e9chou\u00e9 ! hack?");
                errorString = "havenWorldErrorWait";
                WakfuGameEntity.getInstance().removeFrame(UIWorldEditorFrame.getInstance());
                break;
            }
            default: {
                NetHavenWorldFrame.m_logger.error((Object)("Code d'erreur d'havre monde non g\u00e9r\u00e9 " + error));
                break;
            }
        }
        if (errorString != null) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(errorString), WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 1027L, 7, 1);
        }
    }
    
    private void onHavenWorldBuildingEquippedMessage(final HavenWorldBuildingEquippedMessage message) {
        final HavenWorldTopology havenWorld = HavenWorldManager.INSTANCE.getHavenWorld();
        havenWorld.onBuildingEquipped(message.getBuildingUid(), message.getItemId());
    }
    
    private void onHavenWorldBuildingAddedMessage(final HavenWorldBuildingCreationMessage message) {
        final HavenWorldTopology havenWorld = HavenWorldManager.INSTANCE.getHavenWorld();
        havenWorld.onBuildingAdded(message.getInfo());
    }
    
    private void onHavenWorldBuildingRemovedMessage(final HavenWorldBuildingRemovedMessage message) {
        final HavenWorldTopology havenWorld = HavenWorldManager.INSTANCE.getHavenWorld();
        havenWorld.onBuildingRemoved(message.getBuildingUid());
    }
    
    private void onHavenWorldTopologyUpdateMessage(final HavenWorldTopologyUpdateMessage message) {
        final short partitionX = message.getPartitionX();
        final short partitionY = message.getPartitionY();
        final short topLeftPatch = message.getTopLeftPatch();
        final short topRightPatch = message.getTopRightPatch();
        final short bottomLeftPatch = message.getBottomLeftPatch();
        final short bottomRightPatch = message.getBottomRightPatch();
        final HavenWorldTopology havenWorld = HavenWorldManager.INSTANCE.getHavenWorld();
        havenWorld.onTopologyUpdate(partitionX, partitionY, topLeftPatch, topRightPatch, bottomLeftPatch, bottomRightPatch);
        if (this.m_cacheOccupation == null || this.m_cacheOccupation.getUiFrame() == null) {
            return;
        }
        final HavenWorld world = this.m_cacheOccupation.getUiFrame().getWorld();
        final HavenWorldController controller = new HavenWorldController(world);
        if (world.getPartition(partitionX, partitionY) == null) {
            controller.addPartition(partitionX, partitionY);
        }
        controller.modifyPatches(partitionX, partitionY, topLeftPatch, topRightPatch, bottomLeftPatch, bottomRightPatch);
        if (WakfuGameEntity.getInstance().hasFrame(UIWorldEditorFrame.getInstance())) {
            UIWorldEditorFrame.getInstance().refreshCatalogAvailability();
        }
    }
    
    private static void onCharacterEnterHavenWorldMessage(final CharacterEnterHavenWorldMessage message) {
        final byte[] rawTopology = message.getRawTopology();
        final byte[] rawBuildings = message.getRawBuildings();
        HavenWorldManager.INSTANCE.setRawTopology(rawTopology);
        HavenWorldManager.INSTANCE.setRawBuildings(rawBuildings);
        HavenWorldManager.INSTANCE.setGuildInfo(message.getGuildInfo());
    }
    
    @Override
    public long getId() {
        return 1L;
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
    
    public void setCacheOccupation(final ManageHavenWorldOccupation cacheOccupation) {
        this.m_cacheOccupation = cacheOccupation;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetHavenWorldFrame.class);
        INSTANCE = new NetHavenWorldFrame();
    }
}
