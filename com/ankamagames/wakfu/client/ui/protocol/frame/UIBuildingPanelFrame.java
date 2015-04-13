package com.ankamagames.wakfu.client.ui.protocol.frame;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.appearance.*;

public class UIBuildingPanelFrame extends HavenWorldFrame
{
    private static final boolean DEBUG_MODE = false;
    private static final Logger m_logger;
    private static final UIBuildingPanelFrame m_instance;
    private HavenWorldElementView m_elementView;
    private DialogUnloadListener m_dialogUnloadListener;
    private Building m_building;
    private HavenWorldBuildingBoard m_board;
    
    public static UIBuildingPanelFrame getInstance() {
        return UIBuildingPanelFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19366: {
                final UIItemMessage uiItemMessage = (UIItemMessage)message;
                final Item item = uiItemMessage.getItem();
                if (!this.isCustomItem(item)) {
                    return false;
                }
                this.m_elementView.setCustomItem(item);
                this.applyItemOnBuilding(this.m_building.getUid(), item.getReferenceId());
                return false;
            }
            case 19367: {
                this.m_elementView.setCustomItem(null);
                this.applyItemOnBuilding(this.m_building.getUid(), 0);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void applyItemOnBuilding(final long uid, final int itemId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
        if (!(currentOccupation instanceof ManageHavenWorldOccupation)) {
            return;
        }
        if (!this.canManageHavenWorld(localPlayer)) {
            return;
        }
        final HavenWorldManageActionRequest havenWorldManageActionRequest = new HavenWorldManageActionRequest();
        havenWorldManageActionRequest.addAction(new BuildingEquip(uid, itemId));
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(havenWorldManageActionRequest);
    }
    
    private boolean canManageHavenWorld(final PlayerCharacter localPlayer) {
        if (this.m_world.getGuildInfo().getGuildId() != localPlayer.getGuildId()) {
            return false;
        }
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final GuildMember member = guildHandler.getMember(localPlayer.getId());
        return member != null && guildHandler.getRank(member.getRank()).hasAuthorisation(GuildRankAuthorisation.MANAGE_HAVEN_WORLD);
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
        if (!isAboutToBeAdded) {
            if (this.m_elementView == null) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("havenWorldBuildingPanelDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIBuildingPanelFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().putActionClass("wakfu.buildingPanel", BuildingPanelDialogActions.class);
            Xulor.getInstance().load("havenWorldBuildingPanelDialog", Dialogs.getDialogPath("havenWorldBuildingPanelDialog"), 1L, (short)10005);
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().addStartPathListener(this);
            PropertiesProvider.getInstance().setPropertyValue("selectedBuilding", this.m_elementView);
        }
        super.onFrameAdd(frameHandler, isAboutToBeAdded);
    }
    
    @Override
    public Building getSelectedBuilding() {
        return this.m_building;
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_board = null;
            this.m_elementView = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            WakfuProgressMonitorManager.getInstance().done();
            PropertiesProvider.getInstance().removeProperty("selectedBuilding");
            Xulor.getInstance().unload("havenWorldBuildingPanelDialog");
            Xulor.getInstance().unload("buildingEvolutionDialog");
            Xulor.getInstance().removeActionClass("wakfu.buildingPanel");
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null) {
                localPlayer.finishCurrentOccupation();
                localPlayer.getActor().removeStartListener(this);
            }
        }
        super.onFrameRemove(frameHandler, isAboutToBeRemoved);
    }
    
    @Override
    public void pathStarted(final PathMobile mobile, final PathFindResult path) {
        WakfuGameEntity.getInstance().removeFrame(this);
    }
    
    @Override
    public void setWorld(final HavenWorld world) {
        super.setWorld(world);
        final long buildingUid = this.m_board.getBuildingUid();
        this.m_building = world.getBuilding(buildingUid);
        if (this.m_building == null) {
            return;
        }
        this.m_elementView = HavenWorldViewManager.INSTANCE.getElement(buildingUid);
    }
    
    public boolean isCustomItem(final Item item) {
        return this.m_building.getDefinition().acceptItem(item.getReferenceId());
    }
    
    public void highLightCustomSlot(final Item item) {
        if (!WakfuGameEntity.getInstance().hasFrame(getInstance())) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("havenWorldBuildingPanelDialog");
        final boolean isCustomItem = this.isCustomItem(item);
        final Container container = (Container)map.getElement("frontSlot");
        final Color c = isCustomItem ? new Color(WakfuClientConstants.HIGHLIGHT_COLOR.get()) : Color.RED;
        this.addColorTween(container, c);
    }
    
    private void addColorTween(final Container container, final Color c) {
        final DecoratorAppearance appearance = container.getAppearance();
        final Color c2 = new Color(Color.WHITE.get());
        appearance.removeTweensOfType(ModulationColorTween.class);
        appearance.setModulationColor(c2);
        final ModulationColorTween tween = new ModulationColorTween(c2, c, appearance, 0, 300, -1, TweenFunction.PROGRESSIVE);
        appearance.addTween(tween);
    }
    
    public void resetSlot() {
        if (!WakfuGameEntity.getInstance().hasFrame(getInstance())) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("havenWorldBuildingPanelDialog");
        final Container container = (Container)map.getElement("frontSlot");
        container.getAppearance().removeTweensOfType(ModulationColorTween.class);
    }
    
    public void setBoard(final HavenWorldBuildingBoard source) {
        this.m_board = source;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIBuildingPanelFrame.class);
        m_instance = new UIBuildingPanelFrame();
    }
}
