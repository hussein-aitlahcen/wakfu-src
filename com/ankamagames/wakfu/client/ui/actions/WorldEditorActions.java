package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import com.ankamagames.xulor2.util.*;
import java.awt.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.wakfu.client.core.havenWorld.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.tools.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.havenWorld.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.component.text.builder.content.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.component.text.document.part.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

@XulorActionsTag
public class WorldEditorActions extends HavenWorldDialogsActions
{
    private static final Logger m_logger;
    public static final String PACKAGE = "wakfu.worldEditor";
    private static final int TRANSLATION_X = 30;
    private static final int TRANSLATION_Y = 30;
    private static final int MIN_DRAG = 5;
    private static Point2i m_startDrag;
    private static int m_minDrag;
    private static boolean m_dragging;
    private static boolean m_placingElementMode;
    private static WorldEditorScrollRunnable m_worldEditorScrollRunnable;
    private static int m_clickScreenX;
    private static int m_clickScreenY;
    private static BuildingItem m_selectedBuilding;
    
    public static void close(final MouseEvent event) {
        if (UIWorldEditorFrame.getInstance().getHavenWorldCatalogView().isDirty()) {
            final String msgText = WakfuTranslator.getInstance().getString("question.havenWorldEditorClose");
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        unloadDialog();
                    }
                }
            });
        }
        else {
            unloadDialog();
        }
    }
    
    private static void unloadDialog() {
        Xulor.getInstance().unload("worldEditorDialog");
    }
    
    public static void keyPressed(final KeyEvent event, final WorldEditor worldEditor) {
        switch (event.getKeyCode()) {
            case 39: {
                worldEditor.translate(30.0f, 0.0f);
                break;
            }
            case 37: {
                worldEditor.translate(-30.0f, 0.0f);
                break;
            }
            case 38: {
                worldEditor.translate(0.0f, 30.0f);
                break;
            }
            case 40: {
                worldEditor.translate(0.0f, -30.0f);
                break;
            }
        }
    }
    
    public static void onMouseOut(final MouseEvent e, final WorldEditor worldEditor) {
        CursorFactory.getInstance().unlock();
        GraphicalMouseManager.getInstance().hide();
        PropertiesProvider.getInstance().setPropertyValue("havenWorldOverCatalogEntry", null);
        resetDrag(e, worldEditor);
    }
    
    public static void onMapDrag(final MouseEvent e, final WorldEditor worldEditor) {
        if (WorldEditorActions.m_startDrag == null || WorldEditorActions.m_selectedBuilding != null) {
            return;
        }
        WorldEditorActions.m_dragging = true;
        final int x = e.getX(worldEditor);
        final int y = e.getY(worldEditor);
        final int dx = x - WorldEditorActions.m_startDrag.getX();
        final int dy = y - WorldEditorActions.m_startDrag.getY();
        if (Math.abs(dx) > WorldEditorActions.m_minDrag || Math.abs(dy) > WorldEditorActions.m_minDrag) {
            WorldEditorActions.m_minDrag = 0;
            worldEditor.translate(dx / worldEditor.getZoomFactor(), dy / worldEditor.getZoomFactor());
            WorldEditorActions.m_startDrag.set(x, y);
        }
    }
    
    public static void onMouseWheel(final MouseEvent event, final WorldEditor worldEditor) {
        if (event.getRotations() < 0) {
            worldEditor.zoomIn();
        }
        else {
            worldEditor.zoomOut();
        }
    }
    
    public static void onClick(final MouseEvent me, final WorldEditor worldEditor, final Container buildingMenuContainer) {
        if (WorldEditorActions.m_dragging) {
            WorldEditorActions.m_dragging = false;
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final GuildMember guildMember = guildHandler.getMember(localPlayer.getId());
        if (guildMember == null) {
            return;
        }
        final GuildRank guildRank = guildHandler.getRank(guildMember.getRank());
        if (guildRank == null) {
            return;
        }
        if (!guildRank.hasAuthorisation(GuildRankAuthorisation.MANAGE_HAVEN_WORLD)) {
            return;
        }
        WorldEditorActions.m_clickScreenX = me.getX(worldEditor);
        WorldEditorActions.m_clickScreenY = me.getY(worldEditor);
        buildingMenuContainer.setVisible(false);
        WorldEditorActions.m_selectedBuilding = null;
        if (me.getButton() == 3) {
            worldEditor.setCurrentLayer(UIWorldEditorFrame.getInstance().getHavenWorldCatalogView().getSelectedCategoryLayer());
            worldEditor.setSelectTool();
            if (worldEditor.getCurrentLayer() != ItemLayer.BUILDING) {
                return;
            }
            final ModificationItem buildingUnderMouse = worldEditor.getBuildingUnderMouse(WorldEditorActions.m_clickScreenX, WorldEditorActions.m_clickScreenY);
            if (buildingUnderMouse == null) {
                WorldEditorActions.m_placingElementMode = false;
                CursorFactory.getInstance().unlock();
                GraphicalMouseManager.getInstance().hide();
                return;
            }
            final BuildingItem b = (BuildingItem)buildingUnderMouse;
            final HavenWorldQuotation quotation = UIWorldEditorFrame.getInstance().getQuotation(b);
            if (quotation != null && quotation.getModification().getType() == Modification.Type.REMOVE) {
                return;
            }
            WorldEditorActions.m_selectedBuilding = b;
            if (quotation != null) {
                final String name = quotation.getName();
                final HavenWorldCatalogEntryView catalogEntryView = quotation.getCatalogEntryView();
                PropertiesProvider.getInstance().setPropertyValue("selectedBuilding", catalogEntryView);
            }
            else {
                final HavenWorldElementView element = HavenWorldViewManager.INSTANCE.getElement(WorldEditorActions.m_selectedBuilding.getUid());
                if (element != null) {
                    final String name = element.getName();
                    final HavenWorldCatalogEntryView catalogEntryView = element.getCatalogEntryView();
                    PropertiesProvider.getInstance().setPropertyValue("selectedBuilding", element);
                }
                else {
                    WorldEditorActions.m_selectedBuilding = null;
                }
            }
            final Window w = (Window)buildingMenuContainer.getElementMap().getElement("worldEditorDialog");
            w.addWindowPostProcessedListener(new WindowPostProcessedListener() {
                @Override
                public void windowPostProcessed() {
                    final int containerMidWidth = buildingMenuContainer.getWidth() / 2;
                    final int x = Math.max(0, Math.min(worldEditor.getWidth() - buildingMenuContainer.getWidth(), WorldEditorActions.m_clickScreenX - containerMidWidth));
                    final int y = Math.min(worldEditor.getHeight() - buildingMenuContainer.getHeight(), WorldEditorActions.m_clickScreenY + 15);
                    buildingMenuContainer.setPosition(new Point(x, y));
                    buildingMenuContainer.setVisible(true);
                    w.removeWindowPostProcessedListener(this);
                    WakfuSoundManager.getInstance().playGUISound(600072L);
                }
            });
            w.setNeedsToPostProcess();
            MasterRootContainer.getInstance().addEventListener(Events.MOUSE_CLICKED, new EventListener() {
                @Override
                public boolean run(final Event event) {
                    if (event.getTarget().hasInParentHierarchy(buildingMenuContainer)) {
                        return false;
                    }
                    buildingMenuContainer.setVisible(false);
                    WorldEditorActions.m_selectedBuilding = null;
                    PropertiesProvider.getInstance().setPropertyValue("selectedBuilding", null);
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_CLICKED, this, true);
                    return false;
                }
            }, true);
        }
        if (me.getButton() == 1) {
            worldEditor.useTool(WorldEditorActions.m_clickScreenX, WorldEditorActions.m_clickScreenY);
            final byte selectedCategoryId = UIWorldEditorFrame.getInstance().getHavenWorldCatalogView().getSelectedCategoryId();
            if (selectedCategoryId != HavenWorldCatalogView.CatalogCategory.BUILDING_DECO.getId() && selectedCategoryId != HavenWorldCatalogView.CatalogCategory.PATCH.getId()) {
                worldEditor.setSelectTool();
                WorldEditorActions.m_placingElementMode = false;
            }
        }
    }
    
    public static void evolveBuilding(final Event event, final WorldEditor worldEditor, final Container menuContainer) {
        menuContainer.setVisible(false);
        HavenWorldDialogsActions.evolveBuilding(event, HavenWorldViewManager.INSTANCE.getElement(WorldEditorActions.m_selectedBuilding.getUid()));
    }
    
    public static void removeBuilding(final MouseEvent event, final WorldEditor worldEditor, final Container menuContainer) {
        menuContainer.setVisible(false);
        if (WorldEditorActions.m_selectedBuilding == null) {
            return;
        }
        if (WorldEditorActions.m_selectedBuilding.getUid() < 0L) {
            final HavenWorldQuotation quotation = UIWorldEditorFrame.getInstance().getQuotation(WorldEditorActions.m_selectedBuilding);
            final UIHavenWorldQuotationMessage uiMessage = new UIHavenWorldQuotationMessage(quotation);
            uiMessage.setId(19361);
            Worker.getInstance().pushMessage(uiMessage);
        }
        else {
            worldEditor.setTool(new DeleteBuilding());
            worldEditor.useTool(WorldEditorActions.m_clickScreenX, WorldEditorActions.m_clickScreenY);
            worldEditor.setSelectTool();
        }
    }
    
    public static void moveBuilding(final MouseEvent event, final WorldEditor worldEditor, final Container menuContainer) {
        menuContainer.setVisible(false);
        if (WorldEditorActions.m_selectedBuilding == null) {
            return;
        }
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM5, true);
        WorldEditorActions.m_placingElementMode = true;
        PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogPlacingMode", WorldEditorActions.m_placingElementMode);
        worldEditor.setTool(new MoveBuilding(WorldEditorActions.m_selectedBuilding));
        worldEditor.getTool().tryExecute(event.getX(worldEditor), event.getY(worldEditor));
    }
    
    public static void resetDrag(final MouseEvent event, final WorldEditor worldEditor) {
        WorldEditorActions.m_startDrag = null;
        WorldEditorActions.m_minDrag = 5;
        worldEditor.unhighlightPartition();
        worldEditor.highlightEntity(null);
    }
    
    public static void onMousePress(final MouseEvent event, final WorldEditor worldEditor) {
        if (WorldEditorActions.m_startDrag == null && event.getButton() == 3) {
            WorldEditorActions.m_startDrag = new Point2i(event.getX(worldEditor), event.getY(worldEditor));
            WorldEditorActions.m_minDrag = 5;
        }
    }
    
    public static void onMouseMove(final MouseEvent event, final WorldEditor worldEditor, final Container container) {
        final int screenX = event.getX(worldEditor);
        final int screenY = event.getY(worldEditor);
        final boolean insertPartition = isInsertPartition(worldEditor, screenX, screenY);
        if (insertPartition && setPartitionTool(worldEditor)) {
            return;
        }
        if (!WorldEditorActions.m_placingElementMode) {
            if (insertPartition || worldEditor.getTool() instanceof InsertPartition) {
                CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM5, true);
                GraphicalMouseManager.getInstance().showMouseInformation(null, worldEditor.getWorkingHavenWorld().getPartitionCost() + " §", 30, 0, Alignment9.WEST);
            }
            else {
                CursorFactory.getInstance().unlock();
                GraphicalMouseManager.getInstance().hide();
            }
        }
        else {
            CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM5, true);
        }
        worldEditor.temporaryUseTool(screenX, screenY);
        container.setVisible(false);
        final BuildingItem buildingUnderMouse = worldEditor.getBuildingUnderMouse(screenX, screenY);
        if (WorldEditorActions.m_placingElementMode || WorldEditorActions.m_dragging || worldEditor.getCurrentLayer() != ItemLayer.BUILDING || buildingUnderMouse == WorldEditorActions.m_selectedBuilding) {
            return;
        }
        if (buildingUnderMouse == null) {
            PropertiesProvider.getInstance().setPropertyValue("havenWorldOverCatalogEntry", null);
            return;
        }
        final long uniqueId = buildingUnderMouse.getUid();
        final HavenWorldElementView element = HavenWorldViewManager.INSTANCE.getElement(uniqueId);
        if (element == null) {
            final HavenWorldQuotation quotation = UIWorldEditorFrame.getInstance().getQuotation(buildingUnderMouse);
            PropertiesProvider.getInstance().setPropertyValue("havenWorldOverCatalogEntry", quotation);
        }
        else {
            PropertiesProvider.getInstance().setPropertyValue("havenWorldOverCatalogEntry", element);
        }
        container.setPosition(new Point(screenX, screenY + 15));
        container.setVisible(true);
    }
    
    private static boolean setPartitionTool(final WorldEditor worldEditor) {
        final Tool tool = worldEditor.getTool();
        if (!(tool instanceof InsertPartition)) {
            try {
                worldEditor.setTool(new InsertPartition(tool, worldEditor.getWorkingHavenWorld().getPartitionCost()));
            }
            catch (Exception e) {
                WorldEditorActions.m_logger.error((Object)"", (Throwable)e);
                return true;
            }
        }
        return false;
    }
    
    private static boolean isInsertPartition(final WorldEditor worldEditor, final int screenX, final int screenY) {
        final Point2i patch = worldEditor.getPatchCoordFromMouse(screenX, screenY);
        final int mapX = AbstractHavenWorldTopology.patchCoordXToPartition(patch.getX());
        final int mapY = AbstractHavenWorldTopology.patchCoordYToPartition(patch.getY());
        return InsertPartition.isEmptyPartition(worldEditor, mapX, mapY);
    }
    
    public static void zoomIn(final MouseEvent event, final WorldEditor worldEditor) {
        worldEditor.zoomIn();
    }
    
    public static void zoomOut(final MouseEvent event, final WorldEditor worldEditor) {
        worldEditor.zoomOut();
    }
    
    public static void validate(final MouseEvent event, final WorldEditor worldEditor) {
        UIMessage.send((short)17804);
    }
    
    public static void onMouseOverElement(final Event event, final HavenWorldCatalogEntryView entryView, final PopupElement popupElement) {
        XulorActions.popup(event, popupElement);
        final Widget w = event.getTarget();
        w.getAppearance().setModulationColor(WakfuClientConstants.WAKFU_COLOR);
        PropertiesProvider.getInstance().setPropertyValue("havenWorldOverCatalogEntry", entryView);
        if (entryView.isAvailable()) {
            WakfuSoundManager.getInstance().playGUISound(600193L);
        }
        else {
            WakfuSoundManager.getInstance().playGUISound(600010L);
        }
    }
    
    public static void onMouseOutElement(final Event event, final HavenWorldCatalogEntryView havenWorldCatalogEntryView, final PopupElement popupElement) {
        XulorActions.closePopup(event, popupElement);
        final Widget w = event.getTarget();
        if (havenWorldCatalogEntryView.equals(PropertiesProvider.getInstance().getObjectProperty("havenWorldSelectedCatalogEntry"))) {
            w.getAppearance().setModulationColor(WakfuClientConstants.WAKFU_COLOR);
        }
        else {
            w.getAppearance().setModulationColor(Color.WHITE);
        }
        PropertiesProvider.getInstance().setPropertyValue("havenWorldOverCatalogEntry", null);
    }
    
    public static void toggleQuotationsMode(final Event e) {
        final boolean b = PropertiesProvider.getInstance().getBooleanProperty("havenWorldCatalogQuotationMode");
        PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogQuotationMode", !b);
        unloadConflictContainer(e);
    }
    
    public static void removeEntryFromQuotation(final Event event, final HavenWorldQuotation elementView) {
        final UIHavenWorldQuotationMessage elementMessage = new UIHavenWorldQuotationMessage(elementView);
        elementMessage.setId(19361);
        Worker.getInstance().pushMessage(elementMessage);
    }
    
    public static void onMouseOverQuotationElement(final ItemEvent itemEvent, final WorldEditor worldEditor) {
        final HavenWorldQuotation elementView = (HavenWorldQuotation)itemEvent.getItemValue();
        final Modification modification = elementView.getModification();
        if (modification != null) {
            worldEditor.highlightEntity(modification.getItem());
        }
    }
    
    public static void onMouseOutQuotationElement(final ItemEvent itemEvent, final WorldEditor worldEditor) {
        worldEditor.highlightEntity(null);
    }
    
    public static void selectElementFromCatalog(final ItemEvent event) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ClientGuildInformationHandler guildHandler = localPlayer.getGuildHandler();
        final GuildMember guildMember = guildHandler.getMember(localPlayer.getId());
        if (guildMember == null) {
            return;
        }
        final GuildRank guildRank = guildHandler.getRank(guildMember.getRank());
        if (guildRank == null) {
            return;
        }
        if (!guildRank.hasAuthorisation(GuildRankAuthorisation.MANAGE_HAVEN_WORLD)) {
            Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.noRightToManageHavenWorld"), WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 3L, 7, 1);
            return;
        }
        final HavenWorldCatalogEntryView havenWorldCatalogEntryView = (HavenWorldCatalogEntryView)event.getItemValue();
        if (!havenWorldCatalogEntryView.isAvailable()) {
            return;
        }
        final UICatalogEntryMessage uiMessage = new UICatalogEntryMessage(havenWorldCatalogEntryView);
        uiMessage.setId(19360);
        Worker.getInstance().pushMessage(uiMessage);
        CursorFactory.getInstance().show(CursorFactory.CursorType.CUSTOM5, true);
        WorldEditorActions.m_placingElementMode = true;
        PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogPlacingMode", WorldEditorActions.m_placingElementMode);
        MasterRootContainer.getInstance().addEventListener(Events.MOUSE_CLICKED, new EventListener() {
            @Override
            public boolean run(final Event event) {
                if (!WorldEditorActions.m_dragging && ((MouseEvent)event).getButton() == 3) {
                    WorldEditorActions.cancelAction();
                    MasterRootContainer.getInstance().removeEventListener(Events.MOUSE_CLICKED, this, true);
                }
                return false;
            }
        }, true);
    }
    
    public static void cancelAction() {
        WorldEditorActions.m_placingElementMode = false;
        PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogPlacingMode", WorldEditorActions.m_placingElementMode);
        CursorFactory.getInstance().unlock();
        PropertiesProvider.getInstance().setPropertyValue("havenWorldSelectedCatalogEntry", null);
    }
    
    public static void selectCategory(final ItemEvent event) {
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setId(17805);
        uiMessage.setByteValue(((HavenWorldCatalogCategoryView)event.getItemValue()).getId());
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void selectSubCategoryFromCatalog(final Event event, final HavenWorldCatalogSubCategoryView havenWorldCatalogCategoryView) {
        havenWorldCatalogCategoryView.toggleOpen();
    }
    
    public static void unloadConflictContainer(final Event event) {
        UIWorldEditorFrame.getInstance().onConflictContainerUnload();
    }
    
    public static void onConflictTextClick(final Event event, final WorldEditor worldEditor) {
        final TextView textView = event.getTarget();
        final AbstractContentBlock block = textView.getBlockUnderMouse();
        if (block == null || block.getType() != AbstractContentBlock.BlockType.TEXT) {
            return;
        }
        final AbstractDocumentPart part = block.getDocumentPart();
        if (part.getType() != DocumentPartType.TEXT) {
            return;
        }
        final String coords = ((TextDocumentPart)part).getId();
        if (coords != null && coords.length() > 0) {
            try {
                final String[] split = coords.split(";");
                final int cellX = PrimitiveConverter.getInteger(split[0]);
                final int cellY = PrimitiveConverter.getInteger(split[1]);
                worldEditor.centerOnCell(cellX, cellY);
            }
            catch (Exception e) {
                WorldEditorActions.m_logger.error((Object)"", (Throwable)e);
            }
        }
    }
    
    public static void scrollUp(final Event e, final WorldEditor worldEditor) {
        stopScroll(e);
        final Direction8 direction = Direction8.NORTH;
        startScroll(((MouseEvent)e).getScreenY(), worldEditor, direction);
    }
    
    public static void scrollDown(final Event e, final WorldEditor worldEditor) {
        stopScroll(e);
        startScroll(((MouseEvent)e).getScreenY(), worldEditor, Direction8.SOUTH);
    }
    
    public static void scrollLeft(final Event e, final WorldEditor worldEditor) {
        stopScroll(e);
        startScroll(((MouseEvent)e).getScreenX(), worldEditor, Direction8.WEST);
    }
    
    public static void scrollRight(final Event e, final WorldEditor worldEditor) {
        stopScroll(e);
        startScroll(((MouseEvent)e).getScreenX(), worldEditor, Direction8.EAST);
    }
    
    private static void startScroll(final int startCoordValue, final WorldEditor worldEditor, final Direction8 direction) {
    }
    
    public static void stopScroll(final Event e) {
        if (WorldEditorActions.m_worldEditorScrollRunnable != null) {
            if (e != null && e.getType() == Events.MOUSE_EXITED && WorldEditorActions.m_worldEditorScrollRunnable.isOutEditor((MouseEvent)e)) {
                return;
            }
            ProcessScheduler.getInstance().remove(WorldEditorActions.m_worldEditorScrollRunnable);
            WorldEditorActions.m_worldEditorScrollRunnable = null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)WorldEditorActions.class);
    }
    
    private static class WorldEditorScrollRunnable implements Runnable
    {
        private final WorldEditor m_worldEditor;
        private final Direction8 m_direction;
        private final int m_startCoordValue;
        
        private WorldEditorScrollRunnable(final WorldEditor worldEditor, final Direction8 direction, final int startCoordValue) {
            super();
            this.m_worldEditor = worldEditor;
            this.m_direction = direction;
            this.m_startCoordValue = startCoordValue;
        }
        
        @Override
        public void run() {
            switch (this.m_direction) {
                case NORTH: {
                    this.m_worldEditor.translate(0.0f, -30.0f);
                    break;
                }
                case SOUTH: {
                    this.m_worldEditor.translate(0.0f, 30.0f);
                    break;
                }
                case WEST: {
                    this.m_worldEditor.translate(30.0f, 0.0f);
                    break;
                }
                case EAST: {
                    this.m_worldEditor.translate(-30.0f, 0.0f);
                    break;
                }
            }
        }
        
        public boolean isOutEditor(final MouseEvent e) {
            switch (this.m_direction) {
                case NORTH: {
                    return e.getScreenY() > this.m_startCoordValue;
                }
                case SOUTH: {
                    return e.getScreenY() < this.m_startCoordValue;
                }
                case WEST: {
                    return e.getScreenX() < this.m_startCoordValue;
                }
                case EAST: {
                    return e.getScreenX() > this.m_startCoordValue;
                }
                default: {
                    return false;
                }
            }
        }
    }
}
