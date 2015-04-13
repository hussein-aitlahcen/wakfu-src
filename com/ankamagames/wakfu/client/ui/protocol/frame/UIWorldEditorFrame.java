package com.ankamagames.wakfu.client.ui.protocol.frame;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.buildings.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.havenWorld.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.havenWorld.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.catalogEntry.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.conflict.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.core.havenWorld.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.validator.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.wakfu.common.game.world.havenWorld.items.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.tools.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.baseImpl.graphics.ui.progress.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import com.ankamagames.wakfu.client.ui.component.worldEditor.utils.modif.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.common.game.havenWorld.procedure.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.wakfu.common.game.havenWorld.exception.*;

public class UIWorldEditorFrame extends HavenWorldFrame implements HavenWorldListener
{
    private static final boolean DEBUG_MODE = false;
    private static final Logger m_logger;
    private static final UIWorldEditorFrame m_instance;
    private WorldEditor m_worldEditor;
    private HavenWorldCatalogView m_havenWorldCatalogView;
    private final HavenWorldDataProvider m_dataProvider;
    private int m_remainingEditorTaskCount;
    private HavenWorldImagesLibrary m_havenWorldImages;
    private DialogUnloadListener m_dialogUnloadListener;
    private int m_guildMoney;
    
    public UIWorldEditorFrame() {
        super();
        this.m_dataProvider = new HavenWorldDataProvider(new BuildingStruct.Factory());
    }
    
    public static UIWorldEditorFrame getInstance() {
        return UIWorldEditorFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19360: {
                final UICatalogEntryMessage uiMessage = (UICatalogEntryMessage)message;
                final HavenWorldCatalogEntryView catalogEntryView = uiMessage.getHavenWorldCatalogEntryView();
                switch (catalogEntryView.getCategory()) {
                    case PATCH: {
                        this.m_worldEditor.setTool(new InsertPatch(catalogEntryView.getCatalogEntry()));
                        break;
                    }
                    case BUILDING: {
                        this.m_worldEditor.setTool(new InsertBuilding(catalogEntryView.getCatalogEntry()));
                        break;
                    }
                }
                PropertiesProvider.getInstance().setPropertyValue("havenWorldSelectedCatalogEntry", catalogEntryView);
                this.onConflictContainerUnload();
                return false;
            }
            case 19361: {
                final UIHavenWorldQuotationMessage uiMessage2 = (UIHavenWorldQuotationMessage)message;
                final HavenWorldQuotation element = uiMessage2.getElement();
                final Modification modification = element.getModification();
                if (modification == null) {
                    return false;
                }
                final ArrayList<ConstructionError> conflictOnRevert = this.getConflictOnRevert(modification);
                if (!conflictOnRevert.isEmpty()) {
                    this.onRevertError(conflictOnRevert, modification);
                    return false;
                }
                modification.unapply(this.m_worldEditor);
                this.m_havenWorldCatalogView.removeEntryFromQuotation(element);
                this.refreshCatalogAvailability();
                final HavenWorldCatalogEntryView catalogEntryView2 = element.getCatalogEntryView();
                PropertiesProvider.getInstance().firePropertyValueChanged(catalogEntryView2, catalogEntryView2.getFields());
                PropertiesProvider.getInstance().setPropertyValue("havenWorldSelectedCatalogEntry", null);
                if (this.m_havenWorldCatalogView.getQuotationsSize() == 0) {
                    PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogQuotationMode", false);
                }
                return false;
            }
            case 17805: {
                final UIMessage uiMessage3 = (UIMessage)message;
                this.m_havenWorldCatalogView.setSelectedCategoryId(uiMessage3.getByteValue());
                this.m_worldEditor.setCurrentLayer(this.m_havenWorldCatalogView.getSelectedCategoryLayer());
                PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogQuotationMode", false);
                this.onConflictContainerUnload();
                return false;
            }
            case 17804: {
                final String msgText = WakfuTranslator.getInstance().getString("question.havenWorldModificationConfirm");
                final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
                final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIWorldEditorFrame.this.validModifications();
                        }
                    }
                });
                this.onConflictContainerUnload();
                return false;
            }
            case 17803: {
                final UIHavenWorldModification msg = (UIHavenWorldModification)message;
                this.onModification(msg);
                return false;
            }
            case 17800: {
                return false;
            }
            case 17801: {
                return false;
            }
            case 17806: {
                final UIHavenWorldError msg2 = (UIHavenWorldError)message;
                final Modification modification2 = msg2.getModification();
                modification2.revert(this.m_worldEditor);
                this.onError(msg2.getErrors(), modification2);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private ArrayList<ConstructionError> getConflictOnRevert(final Modification modification) {
        final ModificationItem modifItem = modification.getItem();
        if (modifItem instanceof PartitionItem) {
            final RemovePartitionValidator validator = new RemovePartitionValidator(this.m_worldEditor.getWorkingHavenWorld());
            final PartitionItem item = (PartitionItem)modifItem;
            validator.validate(item.getPatchX(), item.getPatchY());
            return validator.getErrors();
        }
        if (modification instanceof AddPatchModification) {
            final InsertPatchValidator validator2 = new InsertPatchValidator(this.m_worldEditor.getWorkingHavenWorld(), ClientPartitionPatchLibrary.INSTANCE);
            final AddPatchModification patchChange = (AddPatchModification)modification;
            final PatchItem item2 = patchChange.getItem();
            validator2.validate(patchChange.getOldPatchId(), item2.getPatchX(), item2.getPatchY());
            return validator2.getErrors();
        }
        if (modifItem instanceof BuildingItem) {
            return this.validateWorld(modifItem, modification.getType().getReverseModifType());
        }
        throw new UnsupportedOperationException("modification non prise en compte " + modifItem);
    }
    
    private void validModifications() {
        final HavenWorldManageActionRequest havenWorldManageActionRequest = new HavenWorldManageActionRequest();
        for (final HavenWorldQuotation elementView : this.m_havenWorldCatalogView.getOrderedQuotations()) {
            final HavenWorldAction action = this.createAction(elementView);
            if (action == null) {
                continue;
            }
            havenWorldManageActionRequest.addAction(action);
        }
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(havenWorldManageActionRequest);
        this.m_remainingEditorTaskCount = this.m_havenWorldCatalogView.getQuotationsSize();
        PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogQuotationMode", false);
        this.m_havenWorldCatalogView.clearQuotations();
    }
    
    private HavenWorldAction createAction(final HavenWorldQuotation elementView) {
        HavenWorldAction action = null;
        final Modification modification = elementView.getModification();
        final Point2i location = modification.getLocation();
        final HavenWorldCatalogEntryView catalogEntryView = elementView.getCatalogEntryView();
        switch (catalogEntryView.getCategory()) {
            case PARTITION: {
                action = new TopologyCreate(AbstractHavenWorldTopology.patchCoordXToPartition(location.getX()), AbstractHavenWorldTopology.patchCoordYToPartition(location.getY()));
                break;
            }
            case BUILDING: {
                switch (modification.getType()) {
                    case ADD: {
                        final int definitionId = ((HavenWorldCatalogBuildingEntryView)catalogEntryView).getFirstBuildingDefinitionId();
                        action = new BuildingCreate((short)definitionId, (short)location.getX(), (short)location.getY());
                        this.m_havenWorldCatalogView.refreshFields();
                        break;
                    }
                    case REMOVE: {
                        action = new BuildingDelete(modification.getItem().getUid());
                        this.m_havenWorldCatalogView.refreshFields();
                        break;
                    }
                    case MOVE: {
                        action = new BuildingMove(modification.getItem().getUid(), (short)location.getX(), (short)location.getY());
                        break;
                    }
                }
                break;
            }
            case PATCH: {
                final AddPatchModification addPatchModification = (AddPatchModification)modification;
                final short patchX = (short)PartitionPatch.getPatchCoordFromCellX(location.getX());
                final short patchY = (short)PartitionPatch.getPatchCoordFromCellY(location.getY());
                final short patchId = catalogEntryView.getCatalogEntry().getPatchId();
                final short oldPatchId = addPatchModification.getOldPatchId();
                action = new TopologyUpdate(patchX, patchY, patchId, oldPatchId);
                break;
            }
        }
        return action;
    }
    
    public void clearMouse() {
        CursorFactory.getInstance().unlock();
        GraphicalMouseManager.getInstance().hide();
    }
    
    private void onRevertError(final ArrayList<ConstructionError> errors, final Modification modification) {
        final TextWidgetFormater twf = new TextWidgetFormater();
        final String entryName = HavenWorldViewHelper.getCatalogEntryName(modification.getItem());
        if (modification.getType() == Modification.Type.ADD || modification.getType() == Modification.Type.REMOVE) {
            twf.append(WakfuTranslator.getInstance().getString("havenWorldBoardConflictOnRevertMessage", entryName)).newLine();
            this.markError(errors, twf);
        }
        else {
            twf.append(WakfuTranslator.getInstance().getString("havenWorldBoardConflictMessage", entryName)).newLine();
        }
        PropertiesProvider.getInstance().setPropertyValue("havenWorldConflictList", twf.finishAndToString());
    }
    
    private void onError(final ArrayList<ConstructionError> errors, final Modification modification) {
        final TextWidgetFormater twf = new TextWidgetFormater();
        final String entryName = HavenWorldViewHelper.getCatalogEntryName(modification.getItem());
        twf.append(WakfuTranslator.getInstance().getString("havenWorldBoardConflictMessage", entryName)).newLine();
        this.markError(errors, twf);
        PropertiesProvider.getInstance().setPropertyValue("havenWorldConflictList", twf.finishAndToString());
    }
    
    private void markError(final ArrayList<ConstructionError> errors, final TextWidgetFormater twf) {
        for (int i = 0; i < errors.size(); ++i) {
            final ConstructionError error = errors.get(i);
            UIWorldEditorFrame.m_logger.error((Object)error);
            appendError(twf, error);
            this.m_worldEditor.markAsError(error.getItem());
            if (!(error instanceof MissingWorker)) {
                if (!(error instanceof BuildingDependency)) {
                    UIWorldEditorFrame.m_logger.error((Object)("Erreur non g\u00e9r\u00e9e = " + error.toString()));
                }
            }
        }
    }
    
    private static void appendError(final TextWidgetFormater twf, final ConstructionError error) {
        final Point2i cell = error.getCell();
        final String name = HavenWorldViewHelper.getCatalogEntryName(error.getItem());
        twf.newLine().openText();
        twf.addId(cell.getX() + ";" + cell.getY());
        twf.b().u().append(name)._u()._b();
        twf.append(" ").append(WakfuTranslator.getInstance().getString(error.getType().name()));
        twf.closeText();
    }
    
    private void getLinkedEntityName(final TextWidgetFormater twf, final long uniqueId, final HavenWorldCatalogEntryView qe) {
        twf.b().u();
        twf.addId(String.valueOf(uniqueId));
        twf.append(qe.getName());
        twf._u()._b();
    }
    
    private void onModification(final UIHavenWorldModification msg) {
        final Modification modification = msg.getModification();
        switch (modification.getType()) {
            case ADD: {
                final HavenWorldCatalogEntry catalogEntry = modification.getCatalogEntry();
                if (catalogEntry instanceof BuildingCatalogEntry) {
                    this.tryAddBuilding(modification);
                    break;
                }
                if (catalogEntry instanceof PatchCatalogEntry) {
                    this.tryAddPatch(modification, (PatchCatalogEntry)catalogEntry);
                    break;
                }
                this.tryAddPartition(modification);
                break;
            }
            case REMOVE: {
                final MessageBoxControler controler = BuildingPanelDialogActions.createDeleteMessageBox();
                controler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            BuildingPanelDialogActions.deleteBuilding(modification.getItem().getUid());
                        }
                        else {
                            modification.revert(UIWorldEditorFrame.this.m_worldEditor);
                        }
                    }
                });
                break;
            }
            case MOVE: {
                final String msgText = WakfuTranslator.getInstance().getString("question.havenWorldMoveBuilding");
                final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
                final MessageBoxControler controler2 = Xulor.getInstance().msgBox(data);
                controler2.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final BuildingMove move = new BuildingMove(modification.getItem().getUid(), (short)modification.getLocation().getX(), (short)modification.getLocation().getY());
                            final HavenWorldManageActionRequest havenWorldManageActionRequest = new HavenWorldManageActionRequest();
                            havenWorldManageActionRequest.addAction(move);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(havenWorldManageActionRequest);
                        }
                        else {
                            modification.revert(UIWorldEditorFrame.this.m_worldEditor);
                        }
                    }
                });
                break;
            }
            case SELECT: {
                final HavenWorldQuotation quotationEntry = this.m_havenWorldCatalogView.getQuotationEntry(modification);
                if (quotationEntry == null) {
                    UIWorldEditorFrame.m_logger.warn((Object)"\u00c9l\u00e9m\u00e9nt n'appartenant pas au devis, todo design");
                    return;
                }
                PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogQuotationMode", true);
                PropertiesProvider.getInstance().setPropertyValue("havenWorldSelectedCatalogEntry", quotationEntry);
                break;
            }
            default: {}
        }
    }
    
    private void tryAddPartition(final Modification modification) {
        if (!this.m_dataProvider.hasMoney(this.m_world.getPartitionCost())) {
            modification.unapply(this.m_worldEditor);
            this.highLightGuildMoney();
        }
        else {
            WakfuSoundManager.getInstance().playGUISound(600182L);
            this.addQuotation(modification);
        }
    }
    
    private void tryAddPatch(final Modification modification, final PatchCatalogEntry patch) {
        if (!this.m_dataProvider.hasMoney(patch.getKamaCost())) {
            modification.unapply(this.m_worldEditor);
            this.highLightGuildMoney();
            return;
        }
        final int sound = HavenWorldSoundLibrary.INSTANCE.getPatchSound(patch.getCategoryId());
        if (sound != -1) {
            WakfuSoundManager.getInstance().playGUISound(sound);
        }
        this.addQuotation(modification);
    }
    
    private void tryAddBuilding(final Modification modification) {
        final BuildingItem item = modification.getItem();
        final AbstractBuildingDefinition building = item.getBuildingInfo().getDefinition();
        if (building == null) {
            modification.unapply(this.m_worldEditor);
            return;
        }
        final BaseBuildingConditionValidator validator = new BuildingConditionValidator(this.m_dataProvider);
        validator.validate(item.getBuildingInfo());
        if (validator.hasError(ConstructionError.Type.MissingWorker)) {
            this.highLightSidoaList();
        }
        if (validator.hasError(ConstructionError.Type.MissingKama)) {
            this.highLightGuildMoney();
        }
        if (validator.hasError(ConstructionError.Type.MissingBuilding)) {
            UIWorldEditorFrame.m_logger.error((Object)"Manque des batiement ");
        }
        if (validator.hasError(ConstructionError.Type.MissingResources)) {
            this.highlightResources();
        }
        if (validator.hasErrors()) {
            modification.unapply(this.m_worldEditor);
            return;
        }
        final int sound = HavenWorldSoundLibrary.INSTANCE.getBuildingSound(item.getCatalogEntry().getId());
        if (sound != -1) {
            WakfuSoundManager.getInstance().playGUISound(sound);
        }
        this.addQuotation(modification);
    }
    
    private void addQuotation(final Modification modification) {
        final HavenWorldCatalogEntryView catalogEntryView = HavenWorldViewManager.INSTANCE.getCatalogEntryView(modification.getCatalogEntry());
        final HavenWorldQuotation element = HavenWorldQuotation.fromNewEntry(catalogEntryView);
        element.setModification(modification);
        this.addEntryToQuotation(element);
        PropertiesProvider.getInstance().firePropertyValueChanged(catalogEntryView, catalogEntryView.getFields());
    }
    
    private ArrayList<ConstructionError> validateWorld(final ModificationItem modifItem, final Modification.Type modifType) {
        final HavenWorldDataProvider dataProvider = new HavenWorldDataProvider(this.m_dataProvider);
        this.updateDataProvider(dataProvider, modifItem, modifType);
        final WorldValidator worldValidator = new WorldValidator(dataProvider);
        worldValidator.validateRecurs();
        return worldValidator.getErrors();
    }
    
    private boolean validateWorldAndNotify(final Modification modif) {
        final ArrayList<ConstructionError> errors = this.validateWorld(modif.getItem(), modif.getType());
        if (errors.isEmpty()) {
            return true;
        }
        Worker.getInstance().pushMessage(new UIHavenWorldError(modif, errors));
        return false;
    }
    
    private void addEntryToQuotation(final HavenWorldQuotation entryView) {
        this.m_havenWorldCatalogView.addEntryToQuotation(entryView);
        this.refreshCatalogAvailability();
        if (!this.m_havenWorldCatalogView.isAvailable(entryView.getCatalogEntryView())) {
            WorldEditorActions.cancelAction();
            this.m_worldEditor.setSelectTool();
        }
    }
    
    public void refreshCatalogAvailability() {
        this.m_dataProvider.refresh(this.m_guildMoney, this.m_world);
        for (final HavenWorldQuotation quotation : this.m_havenWorldCatalogView.getOrderedQuotations()) {
            final Modification modification = quotation.getModification();
            this.updateDataProvider(this.m_dataProvider, modification.getItem(), modification.getType());
        }
        this.m_havenWorldCatalogView.updateAvailables(this.m_dataProvider);
        this.m_havenWorldCatalogView.refreshFields();
    }
    
    private void updateDataProvider(final HavenWorldDataProvider dataProvider, final ModificationItem modifItem, final Modification.Type modifType) {
        final HavenWorldCatalogEntry entry = modifItem.getCatalogEntry();
        if (entry instanceof BuildingCatalogEntry) {
            final BuildingItem item = (BuildingItem)modifItem;
            if (modifType == Modification.Type.ADD) {
                dataProvider.createBuildingEntry(item.getBuildingInfo());
            }
            if (modifType == Modification.Type.REMOVE) {
                dataProvider.removeBuilding(item.getBuildingInfo());
            }
            return;
        }
        if (entry instanceof PartitionCatalogEntry) {
            final PartitionCatalogEntry item2 = (PartitionCatalogEntry)entry;
            dataProvider.addPartition(item2);
            return;
        }
        if (entry instanceof PatchCatalogEntry) {
            final PatchCatalogEntry item3 = (PatchCatalogEntry)entry;
            dataProvider.setPatch(item3);
        }
    }
    
    public HavenWorldQuotation getQuotation(final BuildingItem item) {
        return this.m_havenWorldCatalogView.getQuotationEntry(item.getUid(), ItemLayer.BUILDING);
    }
    
    public Building getBuilding(final long uId) {
        return this.m_world.getBuilding(uId);
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
            if (this.m_world == null) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("worldEditorDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIWorldEditorFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_havenWorldImages = HavenWorldImagesLibrary.INSTANCE;
            Xulor.getInstance().putActionClass("wakfu.worldEditor", WorldEditorActions.class);
            final EventDispatcher eventDispatcher = Xulor.getInstance().load("worldEditorDialog", Dialogs.getDialogPath("worldEditorDialog"), 256L, (short)10005);
            this.initialiseProperties();
            (this.m_worldEditor = (WorldEditor)eventDispatcher.getElementMap().getElement("worldEditor")).initialize(HavenWorldManager.INSTANCE.getHavenWorld(), this.m_havenWorldImages);
            this.m_worldEditor.setTool(new SelectBuilding());
            final CameraLocationFinder finder = new CameraLocationFinder();
            this.m_world.forEachBuilding(finder);
            final Point2i loc = finder.getLocation();
            if (loc != null) {
                this.m_worldEditor.centerOnCell(loc.getX(), loc.getY());
            }
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().addStartPathListener(this);
            PropertiesProvider.getInstance().setPropertyValue("havenWorldConflictList", null);
            PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogPlacingMode", false);
            WakfuSoundManager.getInstance().playGUISound(600012L);
            WakfuSoundManager.getInstance().fadeAmbiance(0.2f, 1000);
            WakfuSoundManager.getInstance().fadeMusic(0.2f, 1000);
        }
        super.onFrameAdd(frameHandler, isAboutToBeAdded);
    }
    
    @Override
    public Building getSelectedBuilding() {
        final Object objectProperty = PropertiesProvider.getInstance().getObjectProperty("selectedBuilding");
        if (objectProperty == null) {
            return null;
        }
        final long uid = ((HavenWorldElementView)objectProperty).getUniqueId();
        return this.m_world.getBuilding(uid);
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WorldEditorActions.stopScroll(null);
            this.clearMouse();
            this.m_world = null;
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            WakfuProgressMonitorManager.getInstance().done();
            this.m_havenWorldImages = null;
            this.m_remainingEditorTaskCount = 0;
            if (this.m_havenWorldCatalogView != null) {
                this.m_havenWorldCatalogView.clear();
            }
            this.m_havenWorldCatalogView = null;
            PropertiesProvider.getInstance().removeProperty("havenWorldCatalog");
            PropertiesProvider.getInstance().removeProperty("havenWorldConflictList");
            if (this.m_worldEditor != null) {
                this.m_worldEditor.getHavenWorldImages().clearTextures();
            }
            Xulor.getInstance().unload("worldEditorDialog");
            Xulor.getInstance().removeActionClass("wakfu.worldEditor");
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer != null) {
                localPlayer.finishCurrentOccupation();
                localPlayer.getActor().removeStartListener(this);
            }
            WakfuSoundManager.getInstance().playGUISound(600013L);
            WakfuSoundManager.getInstance().fadeAmbiance(1.0f, 1000);
            WakfuSoundManager.getInstance().fadeMusic(1.0f, 1000);
        }
        this.m_worldEditor = null;
        super.onFrameRemove(frameHandler, isAboutToBeRemoved);
    }
    
    private void initialiseProperties() {
        this.m_havenWorldCatalogView = new HavenWorldCatalogView(this.m_dataProvider);
        this.refreshCatalogAvailability();
        PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalog", this.m_havenWorldCatalogView);
        PropertiesProvider.getInstance().setPropertyValue("havenWorldCatalogQuotationMode", false);
        PropertiesProvider.getInstance().setPropertyValue("havenWorldSelectedCatalogEntry", null);
    }
    
    private void highlightResources() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("worldEditorDialog");
        final Widget sidoaImage = (Widget)map.getElement("resourceCostContainer");
        this.fadeWidget(sidoaImage);
    }
    
    public void highLightGuildMoney() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("worldEditorDialog");
        final Widget sidoaImage = (Widget)map.getElement("moneyCostContainer");
        this.fadeWidget(sidoaImage);
    }
    
    public void highLightSidoaList() {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap("worldEditorDialog");
        final Widget sidoaImage = (Widget)map.getElement("sidoaCostContainer");
        this.fadeWidget(sidoaImage);
    }
    
    private void fadeWidget(final Widget i) {
        Color c;
        if (i.getAppearance().getModulationColor() == null) {
            c = Color.WHITE;
        }
        else {
            c = i.getAppearance().getModulationColor();
        }
        final Color c2 = Color.WHITE_ALPHA;
        final AbstractTween t = new ModulationColorTween(c, c2, i.getAppearance(), 0, 250, 4, true, TweenFunction.PROGRESSIVE);
        i.getAppearance().addTween(t);
    }
    
    public void onTaskEnded() {
        if (this.m_remainingEditorTaskCount <= 0) {
            return;
        }
        final ProgressMonitor progressMonitor = WakfuProgressMonitorManager.getInstance().getProgressMonitor();
        progressMonitor.worked(1);
        --this.m_remainingEditorTaskCount;
        if (this.m_remainingEditorTaskCount <= 0) {
            PropertiesProvider.getInstance().firePropertyValueChanged(this.m_havenWorldCatalogView, HavenWorldCatalogView.FIELDS);
            WakfuProgressMonitorManager.getInstance().done();
        }
    }
    
    public HavenWorldDataProvider getDataProvider() {
        return this.m_dataProvider;
    }
    
    public HavenWorldCatalogView getHavenWorldCatalogView() {
        return this.m_havenWorldCatalogView;
    }
    
    private void doPutBuilding(final short refId, final int itemId, final short x, final short y, final long buildingUid, final long creationDate) {
        new Controller(this.m_world).putBuilding(refId, itemId, x, y, buildingUid, creationDate);
        final Building building = this.m_world.getBuilding(buildingUid);
        final BuildingItem item = new BuildingItem(new BuildingStruct(building));
        final AddBuildingModification modification = new AddBuildingModification(item);
        modification.onSuccess(this.m_worldEditor);
        this.setWorld(this.m_world);
        this.refreshCatalogAvailability();
    }
    
    private void doMoveBuilding(final long buildingUid, final short x, final short y) {
        final Building building = this.m_world.getBuilding(buildingUid);
        this.doRemoveBuilding(buildingUid);
        this.doPutBuilding(building.getDefinition().getId(), building.getEquippedItemId(), x, y, buildingUid, building.getCreationDate());
    }
    
    private void doEvolveBuilding(final long buildingUid, final long creationDate) {
        final Building building = this.m_world.getBuilding(buildingUid);
        final BuildingEvolution evolution = HavenWorldDefinitionManager.INSTANCE.getEvolutionFromBuilding(building.getDefinition().getId());
        final short buildingToId = evolution.getBuildingToId();
        this.doRemoveBuilding(buildingUid);
        this.doPutBuilding(buildingToId, building.getEquippedItemId(), building.getX(), building.getY(), buildingUid, creationDate);
        this.setWorld(this.m_world);
        this.refreshCatalogAvailability();
    }
    
    private void doRemoveBuilding(final long buildingUid) {
        final Building building = this.m_world.getBuilding(buildingUid);
        final BuildingItem item = new BuildingItem(new BuildingStruct(building));
        final DeleteBuildingModification modification = new DeleteBuildingModification(item);
        new Controller(this.m_world).doRemoveBuilding(buildingUid);
        modification.onSuccess(this.m_worldEditor);
        this.refreshCatalogAvailability();
    }
    
    public void onSuccess(final HavenWorldAction action) {
        final Modification modification = this.modificationFromAction(action);
        if (modification != null) {
            modification.onSuccess(this.m_worldEditor);
        }
        if (action.getActionType() == HavenWorldActionType.BUILDING_CREATE) {
            final BuildingCreate buildingCreate = (BuildingCreate)action;
            this.doPutBuilding(buildingCreate.getBuildingRefId(), 0, buildingCreate.getX(), buildingCreate.getY(), buildingCreate.getBuildingUid(), buildingCreate.getCreationDate());
            return;
        }
        if (action.getActionType() == HavenWorldActionType.BUILDING_MOVE) {
            final BuildingMove buildingMove = (BuildingMove)action;
            this.doMoveBuilding(buildingMove.getBuildingUid(), buildingMove.getX(), buildingMove.getY());
            return;
        }
        if (action.getActionType() == HavenWorldActionType.BUILDING_DELETE) {
            final BuildingDelete buildingDelete = (BuildingDelete)action;
            this.doRemoveBuilding(buildingDelete.getBuildingUid());
            return;
        }
        if (action.getActionType() == HavenWorldActionType.BUILDING_EVOLUTION) {
            final BuildingEvolve buildingEvolve = (BuildingEvolve)action;
            this.doEvolveBuilding(buildingEvolve.getBuildingUid(), buildingEvolve.getCreationDate());
        }
    }
    
    public void onError(final HavenWorldAction action, final HavenWorldError error) {
        final Modification modification = this.modificationFromAction(action);
        if (modification != null) {
            modification.onError(this.m_worldEditor);
        }
    }
    
    @Nullable
    private Modification modificationFromAction(final HavenWorldAction action) {
        if (action.getActionType() == HavenWorldActionType.BUILDING_CREATE) {
            final BuildingCreate buildingCreate = (BuildingCreate)action;
            return new AddBuildingModification(new BuildingItem(new BuildingStruct(buildingCreate.getBuildingUid(), buildingCreate.getBuildingRefId(), 0, buildingCreate.getX(), buildingCreate.getY())));
        }
        if (action.getActionType() == HavenWorldActionType.BUILDING_DELETE) {
            final BuildingDelete buildingDelete = (BuildingDelete)action;
            final Building building = this.m_world.getBuilding(buildingDelete.getBuildingUid());
            return new DeleteBuildingModification(new BuildingItem(new BuildingStruct(building)));
        }
        if (action.getActionType() == HavenWorldActionType.TOPOLOGY_UPDATE) {
            final TopologyUpdate topologyUpdate = (TopologyUpdate)action;
            final PatchCatalogEntry patchCatalogEntry = HavenWorldDefinitionManager.INSTANCE.getPatchCatalogEntry(topologyUpdate.getPatchId());
            return new AddPatchModification(new PatchItem(patchCatalogEntry, topologyUpdate.getPatchX(), topologyUpdate.getPatchY()), topologyUpdate.getOldPatchId());
        }
        if (action.getActionType() == HavenWorldActionType.BUILDING_MOVE) {
            final BuildingMove buildingMove = (BuildingMove)action;
            final Building building = this.m_world.getBuilding(buildingMove.getBuildingUid());
            return new MoveBuildingModification(new BuildingItem(new BuildingStruct(building)), building.getX(), building.getY());
        }
        return null;
    }
    
    @Override
    public void setWorld(final HavenWorld world) {
        super.setWorld(world);
        this.m_dataProvider.refresh(world);
        world.addListener(this);
    }
    
    @Override
    public void pathStarted(final PathMobile mobile, final PathFindResult path) {
        WakfuGameEntity.getInstance().removeFrame(this);
    }
    
    public void onConflictContainerUnload() {
        PropertiesProvider.getInstance().setPropertyValue("havenWorldConflictList", null);
        this.m_worldEditor.unmarkAsError();
        this.m_worldEditor.refresh();
    }
    
    public int getGuildMoney() {
        return this.m_guildMoney;
    }
    
    public void setGuildMoney(final int guildMoney) {
        this.m_guildMoney = guildMoney;
        final String guildMoneyText = WakfuTranslator.getInstance().formatNumber(guildMoney);
        PropertiesProvider.getInstance().setPropertyValue("guildMoney", guildMoneyText);
    }
    
    @Override
    public void guildChanged(final GuildInfo guildInfo) {
    }
    
    @Override
    public void buildingAdded(final Building building) {
    }
    
    @Override
    public void buildingRemoved(final Building building) {
    }
    
    @Override
    public void partitionAdded(final Partition partition) {
    }
    
    @Override
    public void partitionChanged(final Partition partition) {
    }
    
    @Override
    public void resourcesChanged(final int resources) {
        this.refreshCatalogAvailability();
        if (this.m_havenWorldCatalogView != null) {
            this.m_havenWorldCatalogView.resourcesChanged(resources);
        }
    }
    
    public int getBuildingQuantityOfType(final short id) {
        final BuildingAndEvolutionCount2 computer = new BuildingAndEvolutionCount2(id);
        this.m_dataProvider.forEachBuilding(computer);
        return computer.getCount();
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIWorldEditorFrame.class);
        m_instance = new UIWorldEditorFrame();
    }
    
    private static class Controller extends HavenWorldController
    {
        private Controller(final HavenWorld world) {
            super(world);
        }
        
        public void putBuilding(final short refId, final int itemId, final short x, final short y, final long buildingUid, final long creationDate) {
            try {
                this.addBuilding(new BuildingStruct(buildingUid, refId, itemId, x, y), creationDate);
            }
            catch (HavenWorldException e) {
                UIWorldEditorFrame.m_logger.error((Object)("Probl\u00e8me dr'ajout du groupe refId=" + refId + "en x=" + x + ", y=" + y + " uid=" + buildingUid), (Throwable)e);
            }
        }
        
        public void doRemoveBuilding(final long buildingUid) {
            try {
                this.removeBuilding(buildingUid);
            }
            catch (HavenWorldException e) {
                UIWorldEditorFrame.m_logger.error((Object)("Probl\u00e8me de suppression du batiment uid=" + buildingUid), (Throwable)e);
            }
        }
    }
    
    private static class CameraLocationFinder implements TObjectProcedure<Building>
    {
        private int m_cumulX;
        private int m_cumulY;
        private int m_counter;
        
        @Override
        public boolean execute(final Building object) {
            this.m_cumulX += object.getX();
            this.m_cumulY += object.getY();
            ++this.m_counter;
            return true;
        }
        
        @Nullable
        Point2i getLocation() {
            return (this.m_counter > 0) ? new Point2i(this.m_cumulX / this.m_counter, this.m_cumulY / this.m_counter) : null;
        }
    }
}
