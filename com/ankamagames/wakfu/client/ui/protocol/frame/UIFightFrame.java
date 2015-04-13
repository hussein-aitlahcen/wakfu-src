package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.effectArea.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.alea.graphics.tacticalView.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.client.ui.script.*;
import com.ankamagames.wakfu.client.ui.protocol.message.overHeadInfos.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.graphics.alea.utils.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.alea.highlightingCells.*;
import com.ankamagames.baseImpl.graphics.*;

public class UIFightFrame implements MessageFrame
{
    private static final UIFightFrame m_instance;
    private MessageBoxControler m_giveUpMessageBoxControler;
    private static final Logger m_logger;
    public static Fight m_fight;
    protected static final Point3 m_lastTarget;
    public static int m_mouseX;
    public static int m_mouseY;
    private boolean m_cellReportMode;
    private DisplayedScreenElement m_oldElem;
    private static final String CELL_REPORT_LAYER_NAME = "targetCell";
    private static final float[] TARGET_CELL_COLOR;
    private static final BeaconDisplayZone m_beaconDisplay;
    private static final EffectAreaDisplayZone m_areaDisplay;
    private static final MovementInformationDisplayZone m_movementDisplayer;
    private boolean m_isOverTimelineCharacter;
    private final OverHeadContainer m_overHeadContainer;
    private MobileFilterImpl m_mobileFilter;
    
    public UIFightFrame() {
        super();
        this.m_giveUpMessageBoxControler = null;
        this.m_overHeadContainer = new OverHeadContainer();
    }
    
    public void onBattleGroundBorderEffectAreaReleased(final BattlegroundBorderEffectArea battlegroundBorderEffectArea) {
        if (this.m_overHeadContainer != null && this.m_overHeadContainer.getOverHead() != null && this.m_overHeadContainer.getOverHead().equals(battlegroundBorderEffectArea)) {
            UIOverHeadInfosFrame.getInstance().hideOverHead(this.m_overHeadContainer);
        }
    }
    
    public static UIFightFrame getInstance() {
        return UIFightFrame.m_instance;
    }
    
    public static Point3 getLastTarget() {
        return UIFightFrame.m_lastTarget;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final AleaWorldSceneWithParallax scene = WakfuClientInstance.getInstance().getWorldScene();
        switch (message.getId()) {
            case 19994: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (this.m_cellReportMode) {
                    return this.reportCellMouseMoved(msg);
                }
                UIFightFrame.m_mouseX = msg.getMouseX();
                UIFightFrame.m_mouseY = msg.getMouseY();
                final Point3 target = this.getNearestPoint3(UIFightFrame.m_mouseX, UIFightFrame.m_mouseY, !isHideFightOccluderActivated(), false);
                if (!this.m_isOverTimelineCharacter) {
                    setLastTarget(target);
                }
                assert UIFightFrame.m_fight != null : "UIFightFrame uniquement pour les gens en combat. m_fight ne peut \u00eatre null";
                assert UIFightFrame.m_fight.getFightMap() != null : "UIFightFrame : fight sans fightMap";
                if (target != null && UIFightFrame.m_fight.getCharacterInfoAtPosition(target) == null) {
                    boolean found = false;
                    final BasicEffectAreaManager effectAreaManager = UIFightFrame.m_fight.getEffectAreaManager();
                    final boolean isBorder = UIFightFrame.m_fight.getFightMap().isBorder(target.getX(), target.getY());
                    if (effectAreaManager != null) {
                        final Collection<BasicEffectArea> areaList = effectAreaManager.getActiveEffectAreas();
                        if (areaList != null && !areaList.isEmpty()) {
                            for (final BasicEffectArea basicEffectArea : areaList) {
                                if (!(basicEffectArea instanceof OverHead)) {
                                    continue;
                                }
                                if (basicEffectArea.getType() == EffectAreaType.ENUTROF_DEPOSIT.getTypeId() && localPlayer != basicEffectArea.getOwner()) {
                                    continue;
                                }
                                if (basicEffectArea.isActiveProperty(EffectAreaPropertyType.INVISIBLE_FOR_ENEMIES) && localPlayer.getTeamId() != basicEffectArea.getTeamId()) {
                                    continue;
                                }
                                if (basicEffectArea.isActiveProperty(EffectAreaPropertyType.NO_OVERHEAD)) {
                                    continue;
                                }
                                final boolean validBorder = isBorder && basicEffectArea instanceof BattlegroundBorderEffectArea && !((BattlegroundBorderEffectArea)basicEffectArea).isInvisible();
                                if (validBorder || (basicEffectArea.getWorldCellX() == target.getX() && basicEffectArea.getWorldCellY() == target.getY())) {
                                    this.m_overHeadContainer.setOverHead((OverHead)basicEffectArea);
                                    found = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (found) {
                        final Point2 pt = IsoCameraFunc.getScreenPositionFromCenter(scene, target.getX(), target.getY(), target.getZ());
                        this.m_overHeadContainer.setScreenX(MathHelper.fastRound(pt.m_x));
                        this.m_overHeadContainer.setScreenY(MathHelper.fastRound(pt.m_y));
                        final UIShowOverHeadInfosMessage bubbleOverHeadInfosMessage = new UIShowOverHeadInfosMessage(this.m_overHeadContainer, 0);
                        bubbleOverHeadInfosMessage.addInfo(this.m_overHeadContainer.getFormatedOverheadText(), null);
                        Worker.getInstance().pushMessage(bubbleOverHeadInfosMessage);
                        this.m_overHeadContainer.setDisplayed(StringUtils.isNotBlank(this.m_overHeadContainer.getFormatedOverheadText()));
                    }
                    else if (this.m_overHeadContainer.isDisplayed()) {
                        UIOverHeadInfosFrame.getInstance().hideOverHead(this.m_overHeadContainer);
                        this.m_overHeadContainer.setDisplayed(StringUtils.isNotBlank(this.m_overHeadContainer.getFormatedOverheadText()));
                    }
                }
                return false;
            }
            case 18015: {
                this.refreshAreaDisplay(null);
                if (UIFightFrame.m_fight != null && UIFightFrame.m_fight.getEffectAreaManager() != null) {
                    final Iterator<BasicEffectArea> effectAreaIterator = (Iterator<BasicEffectArea>)UIFightFrame.m_fight.getEffectAreaManager().getActiveEffectAreas().iterator();
                    boolean beaconFound = false;
                    boolean somethingFound = false;
                    while (effectAreaIterator.hasNext()) {
                        final BasicEffectArea basicEffectArea2 = effectAreaIterator.next();
                        if (!basicEffectArea2.contains(UIFightFrame.m_lastTarget.getX(), UIFightFrame.m_lastTarget.getY(), UIFightFrame.m_lastTarget.getZ())) {
                            continue;
                        }
                        final EffectAreaType effectAreaType = EffectAreaType.getTypeFromId(basicEffectArea2.getType());
                        if (effectAreaType == null) {
                            continue;
                        }
                        switch (effectAreaType) {
                            case BEACON: {
                                refreshBeaconDisplay((BeaconEffectArea)basicEffectArea2);
                                beaconFound = true;
                                continue;
                            }
                            case TRAP: {
                                final boolean dontDisplayTrap = UIFightFrame.m_fight.dontDisplayTrap((TrapEffectArea)basicEffectArea2);
                                if (!dontDisplayTrap) {
                                    this.refreshAreaDisplay((AbstractEffectArea)basicEffectArea2);
                                    somethingFound = true;
                                    continue;
                                }
                                continue;
                            }
                            case BOMB: {
                                this.refreshAreaDisplay((AbstractEffectArea)basicEffectArea2);
                                somethingFound = true;
                                continue;
                            }
                            case FECA_GLYPH: {
                                refreshAreaDisplay((AbstractEffectArea)basicEffectArea2, UIFightFrame.m_lastTarget);
                                somethingFound = true;
                                continue;
                            }
                            case FAKE_FIGHTER: {
                                final FakeFighterEffectArea fakeFighter = (FakeFighterEffectArea)basicEffectArea2;
                                switch (fakeFighter.getUserDefinedId()) {
                                    case 4: {
                                        if (basicEffectArea2.getPosition().equalsIgnoringAltitude(UIFightFrame.m_lastTarget)) {
                                            this.refreshAreaDisplay((AbstractEffectArea)basicEffectArea2);
                                            somethingFound = true;
                                            continue;
                                        }
                                        continue;
                                    }
                                    default: {
                                        continue;
                                    }
                                }
                                break;
                            }
                            default: {
                                continue;
                            }
                        }
                    }
                    if (!beaconFound) {
                        refreshBeaconDisplay(null);
                    }
                    if (!somethingFound) {
                        this.refreshAreaDisplay(null);
                    }
                }
                return false;
            }
            case 19992: {
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                if (this.m_cellReportMode) {
                    if (msg.isButtonLeft()) {
                        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                        if (localPlayer.isActiveProperty(WorldPropertyType.CELL_REPORT_DISABLED)) {
                            return false;
                        }
                        final ArrayList<DisplayedScreenElement> hitElements = scene.getDisplayedElementsUnderMousePoint(msg.getMouseX(), msg.getMouseY(), 0.0f, DisplayedScreenElementComparator.CELL_CENTER_DISTANCE);
                        final DisplayedScreenElement targetElt = (hitElements != null && !hitElements.isEmpty()) ? hitElements.get(0) : null;
                        if (targetElt == null) {
                            return false;
                        }
                        final ScreenElement element = targetElt.getElement();
                        final Point3 targetEltCoords = new Point3(element.getCellX(), element.getCellY(), element.getCellZ());
                        final CellReportRequestMessage netMessage = new CellReportRequestMessage();
                        netMessage.setTargetCoords(targetEltCoords);
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                    }
                    if (HighLightManager.getInstance().getLayer("targetCell") != null) {
                        try {
                            HighLightManager.getInstance().clearLayer("targetCell");
                        }
                        catch (Exception e) {
                            UIFightFrame.m_logger.error((Object)"Exception", (Throwable)e);
                        }
                    }
                    return this.m_cellReportMode = false;
                }
                final ArrayList<AnimatedInteractiveElement> displayedElementsMouseOver = ((WakfuWorldScene)scene).selectAllNearestElement(UIFightFrame.m_mouseX, UIFightFrame.m_mouseY);
                if (!displayedElementsMouseOver.isEmpty() && msg.isButtonRight()) {
                    for (int i = 0, size = displayedElementsMouseOver.size(); i < size; ++i) {
                        if (displayedElementsMouseOver.get(i) instanceof CharacterActor) {
                            final CharacterActor actor = displayedElementsMouseOver.get(i);
                            CharacterInfo fighter = actor.getCharacterInfo();
                            if (fighter.isActiveProperty(FightPropertyType.IS_A_COPY_OF_HIS_CONTROLLER)) {
                                fighter = fighter.getController();
                            }
                            if (fighter != null) {
                                if (fighter.getCurrentFightId() == UIFightFrame.m_fight.getId()) {
                                    UITimelineFrame.getInstance().openCloseFighterDescription(fighter);
                                    break;
                                }
                            }
                        }
                    }
                }
                return false;
            }
            case 18013: {
                this.setCellReportMode();
                return false;
            }
            case 18000: {
                return this.giveUpFight();
            }
            case 16713: {
                swapHideFighter();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean giveUpFight() {
        if (UIFightFrame.m_fight != null && !UIFightFrame.m_fight.getModel().fighterCanGiveUp()) {
            return true;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.isActiveProperty(WorldPropertyType.GIVE_UP_DISABLED)) {
            return true;
        }
        (this.m_giveUpMessageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.giveUpFight"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1)).addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    final GiveUpFightRequestMessage netMessage = new GiveUpFightRequestMessage();
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                }
            }
        });
        return false;
    }
    
    private boolean reportCellMouseMoved(final UIWorldSceneMouseMessage msg) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getCurrentFight() == null) {
            UIFightFrame.m_logger.warn((Object)"Ciblage de case hors combat. On d\u00e9senclenche le mode ciblage de case");
            this.setCellReportMode();
            return false;
        }
        final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
        final ArrayList<DisplayedScreenElement> hitElements = scene.getDisplayedElementsUnderMousePoint(msg.getMouseX(), msg.getMouseY(), 0.0f, DisplayedScreenElementComparator.CELL_CENTER_DISTANCE);
        final DisplayedScreenElement targetElt = (hitElements != null && !hitElements.isEmpty()) ? hitElements.get(0) : null;
        if (targetElt != this.m_oldElem) {
            if (targetElt != null) {
                HighLightManager.getInstance().clearLayer("targetCell");
                HighLightManager.getInstance().add(targetElt.getLayerReference(), "targetCell");
                HighLightManager.getInstance().getLayer("targetCell").setColor(UIFightFrame.TARGET_CELL_COLOR);
            }
            this.m_oldElem = targetElt;
        }
        return false;
    }
    
    private static void swapHideFighter() {
        if (UIFightFrame.m_fight != null) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            if (localPlayer.isActiveProperty(WorldPropertyType.HIDE_FIGHTERS_DISABLED)) {
                return;
            }
            final boolean selected = !isHideFightOccluderActivated();
            OptionsDialogActions.proceedHideFightOccluders(selected);
            WakfuClientInstance.getInstance().getGamePreferences().setValue(WakfuKeyPreferenceStoreEnum.HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY, selected);
        }
    }
    
    public boolean setCellReportMode() {
        if (this.m_cellReportMode) {
            HighLightManager.getInstance().clearLayer("targetCell");
        }
        UIFightMovementFrame.getInstance().clearPathSelection();
        return this.m_cellReportMode = !this.m_cellReportMode;
    }
    
    public boolean getCellReportMode() {
        return this.m_cellReportMode;
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
            MobileManager.getInstance().unselectAllElementExceptThis(null);
            ResourceManager.getInstance().unselectAllElementExceptThis(null);
            AnimatedElementSceneViewManager.getInstance().unselectAllElementExceptThis(null);
            if (HighLightManager.getInstance().getLayer("targetCell") == null) {
                try {
                    HighLightManager.getInstance().createLayer("targetCell");
                }
                catch (Exception e) {
                    UIFightFrame.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
            this.m_cellReportMode = false;
            ShortcutManager.getInstance().enableGroup("fight", true);
            ShortcutManager.getInstance().enableGroup("world", false);
            WorldPositionMarkerManager.getInstance().setVisible(false);
            PropertiesProvider.getInstance().setPropertyValue("isInFight", true);
            PropertiesProvider.getInstance().setPropertyValue("isInFightPlayerTurn", false);
            PropertiesProvider.getInstance().setPropertyValue("controlCenterDisplayMode", 0);
            Xulor.getInstance().putActionClass("wakfu.fightAction", FightActionDialogActions.class);
            final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
            TacticalViewManager.getInstance().activate(wakfuGamePreferences.getBooleanValue(WakfuKeyPreferenceStoreEnum.TACTICAL_VIEW_KEY));
            Xulor.getInstance().putActionClass("wakfu.controlCenterFight", ControlCenterFightDialogActions.class);
            Xulor.getInstance().putActionClass("wakfu.fightCreationOrPlacement", FightCreationOrPlacementDialogActions.class);
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final ShortcutBarManager shortcutBarManager = localPlayer.getShortcutBarManager();
            shortcutBarManager.updateLeftAndRighthands();
            if (wakfuGamePreferences.getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_SWITCH_BARS_MODE_KEY) && shortcutBarManager.getCurrentBarType() == ShortCutBarType.WORLD) {
                shortcutBarManager.setCurrentBarType(ShortCutBarType.FIGHT);
            }
            UIFightFrame.m_fight = localPlayer.getCurrentFight();
            final boolean inPlacement = UIFightFrame.m_fight.getStatus() == AbstractFight.FightStatus.PLACEMENT;
            if (!inPlacement || !UIFightFrame.m_fight.getModel().needPlacementStep()) {
                this.cleanUpAps("worldAndFightBarDialog");
            }
            PropertiesProvider.getInstance().setPropertyValue("isInFightPlacement", inPlacement && UIFightFrame.m_fight.getModel().needPlacementStep());
            UIControlCenterContainerFrame.getInstance().setSundialVisible(false);
            final boolean canGiveUpFight = UIFightFrame.m_fight.getModel().fighterCanGiveUp();
            PropertiesProvider.getInstance().setPropertyValue("canGiveUpFight", canGiveUpFight, true);
            final boolean useTimeScoreGauge = UIFightFrame.m_fight.getModel().isUseTimeScoreGauge();
            PropertiesProvider.getInstance().setPropertyValue("fight.velocity.enable", useTimeScoreGauge, true);
            PropertiesProvider.getInstance().setPropertyValue("cellReportDisabled", localPlayer.isActiveProperty(WorldPropertyType.CELL_REPORT_DISABLED));
            PropertiesProvider.getInstance().setPropertyValue("lockFightDisabled", localPlayer.isActiveProperty(WorldPropertyType.FIGHT_LOCK_DISABLED));
            PropertiesProvider.getInstance().setPropertyValue("callHelpDisabled", localPlayer.isActiveProperty(WorldPropertyType.CALL_HELP_DISABLED));
            PropertiesProvider.getInstance().setPropertyValue("hideFightersDisabled", localPlayer.isActiveProperty(WorldPropertyType.HIDE_FIGHTERS_DISABLED));
            PropertiesProvider.getInstance().setPropertyValue("giveUpDisabled", localPlayer.isActiveProperty(WorldPropertyType.GIVE_UP_DISABLED));
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            final WakfuWorldScene scene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
            scene.setDispatchKeyPressedMessage(false);
            scene.setDispatchKeyReleasedMessage(false);
            this.m_mobileFilter = null;
            clearMovementDisplay();
            this.m_isOverTimelineCharacter = false;
            UIFightTurnFrame.getInstance().setSpellSelectedListener(null);
            if (this.m_overHeadContainer.isDisplayed()) {
                final UIHideOverHeadInfosMessage msg = new UIHideOverHeadInfosMessage(this.m_overHeadContainer);
                Worker.getInstance().pushMessage(msg);
                this.m_overHeadContainer.setDisplayed(false);
            }
            HighLightManager.getInstance().removeLayer("targetCell");
            refreshBeaconDisplay(null);
            this.refreshAreaDisplay(null);
            PropertiesProvider.getInstance().setPropertyValue("isInFight", false);
            PropertiesProvider.getInstance().setPropertyValue("isInFightPlacement", false);
            ShortcutManager.getInstance().enableGroup("fight", false);
            ShortcutManager.getInstance().enableGroup("world", true);
            final LocalPlayerCharacter lpc = WakfuGameEntity.getInstance().getLocalPlayer();
            WorldPositionMarkerManager.getInstance().setVisible(true);
            if (lpc != null) {
                final ShortcutBarManager shortcutBarManager = lpc.getShortcutBarManager();
                shortcutBarManager.hideSymbiotBar();
                final WakfuGamePreferences wakfuGamePreferences = WakfuClientInstance.getInstance().getGamePreferences();
                if (wakfuGamePreferences.getBooleanValue(WakfuKeyPreferenceStoreEnum.AUTO_SWITCH_BARS_MODE_KEY)) {
                    shortcutBarManager.setCurrentBarType(ShortCutBarType.WORLD, true, (byte)wakfuGamePreferences.getIntValue(WakfuKeyPreferenceStoreEnum.CURRENT_ITEM_SHORTCUT_BAR_INDEX));
                }
                shortcutBarManager.updateLeftAndRighthands();
            }
            Xulor.getInstance().removeActionClass("wakfu.fightAction");
            Xulor.getInstance().removeActionClass("wakfu.controlCenterFight");
            UIControlCenterContainerFrame.getInstance().setSundialVisible(true);
            WakfuGameEntity.getInstance().setObservedCharacter(WakfuGameEntity.getInstance().getLocalPlayer());
            UIFightFrame.m_fight = null;
        }
    }
    
    public void refreshAreaDisplay(final AbstractEffectArea trap) {
        refreshAreaDisplay(trap, null);
    }
    
    public static void refreshAreaDisplay(final AbstractEffectArea trap, final Point3 target) {
        if (trap != null && !WakfuGameEntity.getInstance().hasFrame(UIFightSpellCastInteractionFrame.getInstance())) {
            final EffectUser owner = trap.getOwner();
            if (owner instanceof CharacterInfo) {
                if (target == null) {
                    UIFightFrame.m_areaDisplay.refreshAreaDisplay(trap, (CharacterInfo)owner);
                }
                else {
                    UIFightFrame.m_areaDisplay.refreshAreaDisplay(trap, (CharacterInfo)owner, target);
                }
                return;
            }
            UIFightFrame.m_logger.error((Object)((owner == null) ? "Owner du pi\u00e8ge est null" : ("ClassCastException attendu=CharacterInfo : " + owner.getClass())));
        }
        UIFightFrame.m_areaDisplay.clearZoneEffectAndRange();
    }
    
    public static void refreshBeaconDisplay(final BeaconEffectArea beacon) {
        if (beacon != null) {
            UIFightFrame.m_beaconDisplay.refreshBeaconDisplay(beacon, (CharacterInfo)beacon.getOwner());
        }
        else {
            UIFightFrame.m_beaconDisplay.clearZoneEffect();
            UIFightFrame.m_beaconDisplay.clearZoneEffectAndRange();
        }
    }
    
    private Point3 getNearestPoint3(final int mouseX, final int mouseY, final boolean singleCellMobileSelectable, final boolean multipleCellsMobileSelectable) {
        if (this.m_mobileFilter == null && UIFightFrame.m_fight != null) {
            this.m_mobileFilter = new MobileFilterImpl(UIFightFrame.m_fight.getId());
        }
        final ArrayList<Point3> list = WorldSceneInteractionUtils.getNearestsFilteredPoint3(WakfuClientInstance.getInstance().getWorldScene(), mouseX, mouseY, singleCellMobileSelectable, multipleCellsMobileSelectable, this.m_mobileFilter);
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (final Point3 p : list) {
            if (HighLightManager.getInstance().contains(p)) {
                return p;
            }
        }
        return list.get(0);
    }
    
    public void hideFighters(final boolean hide) {
        if (UIFightFrame.m_fight == null) {
            return;
        }
        final int fightId = UIFightFrame.m_fight.getId();
        this.hideFighters(hide, fightId);
    }
    
    private void hideFighters(final boolean hide, final int fightId) {
        final ArrayList<AnimatedElement> elements = new ArrayList<AnimatedElement>(500);
        MobileManager.getInstance().queryDisplayed(elements);
        SimpleAnimatedElementManager.getInstance().queryDisplayed(elements);
        AnimatedElementSceneViewManager.getInstance().queryDisplayed(elements);
        ResourceManager.getInstance().queryDisplayed(elements);
        for (int count = elements.size(), i = 0; i < count; ++i) {
            final AnimatedElement ae = elements.get(i);
            if (ae.getCurrentFightId() == fightId) {
                this.hide(elements.get(i), hide);
            }
        }
    }
    
    private void hide(final AnimatedElement target, final boolean hide) {
        if (target instanceof CharacterActor) {
            hide(((CharacterActor)target).getCharacterInfo(), hide);
            return;
        }
        hideAnimatedElement(target, hide);
    }
    
    public static void hide(final CharacterInfo fighter, final boolean hide) {
        final CharacterActor actor = fighter.getActor();
        if (fighter.isInvisible()) {
            if (fighter.isInLocalPlayerTeam()) {
                actor.setDesiredAlpha(hide ? 0.2f : 0.4f);
            }
        }
        else {
            hideAnimatedElement(actor, hide);
        }
    }
    
    private static void hideAnimatedElement(final AnimatedElement anim, final boolean hide) {
        if (hide) {
            anim.setDesiredAlpha(0.5f);
        }
        else {
            anim.setDesiredAlpha(anim.getOriginalAlpha());
            anim.resetAlpha();
        }
    }
    
    public static void hideFighter(final CharacterInfo fighter) {
        if (UIFightFrame.m_fight == null || fighter.getCurrentFightId() != UIFightFrame.m_fight.getId()) {
            return;
        }
        hide(fighter, isHideFightOccluderActivated());
    }
    
    public static boolean isHideFightOccluderActivated() {
        return WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.HIDE_FIGHT_OCCLUDERS_ACTIVATED_KEY);
    }
    
    public void hide(final AnimatedElement target) {
        if (UIFightFrame.m_fight == null || target.getCurrentFightId() != UIFightFrame.m_fight.getId()) {
            return;
        }
        this.hide(target, isHideFightOccluderActivated());
    }
    
    public void applyCountDownEnd() {
        applyCountDownEnd("worldAndFightBarDialog");
    }
    
    private static void applyCountDownEnd(final String elementMapId) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final Fight fight = player.getCurrentFight();
        if (fight == null) {
            return;
        }
        final Timeline timeline = fight.getTimeline();
        if (timeline == null) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(elementMapId);
        if (map == null) {
            return;
        }
        Widget w = null;
        switch (fight.getStatus()) {
            case PLACEMENT: {
                w = (Widget)map.getElement("readyForFightButton");
                break;
            }
            case ACTION: {
                if (timeline.getCurrentFighter() == player) {
                    w = (Widget)map.getElement("endTurnButton");
                    break;
                }
                break;
            }
            default: {
                w = null;
                break;
            }
        }
        if (w == null || !w.getVisible()) {
            return;
        }
        Color c2 = new Color(0.531f, 0.812f, 0.835f, 1.0f);
        c2 = new Color(Color.WHITE.get());
        w.getAppearance().addTween(new ModulationColorTween(c2, c2, w.getAppearance(), 0, 500, 12, TweenFunction.PROGRESSIVE));
        final Widget container = (Widget)map.getElement("apsContainer");
        container.setVisible(true);
    }
    
    public static boolean updateMovementDisplay() {
        if (UIFightFrame.m_fight == null) {
            return false;
        }
        UIFightFrame.m_movementDisplayer.clearZoneEffectAndRange();
        final CharacterInfo target = (CharacterInfo)UIFightFrame.m_fight.getCharacterInfoAtPosition(UIFightFrame.m_lastTarget);
        return target != null && UIFightFrame.m_movementDisplayer.refreshMovementInformationDisplay(target);
    }
    
    public static void clearMovementDisplay() {
        UIFightFrame.m_movementDisplayer.clearZoneEffectAndRange();
    }
    
    public static void setLastTarget(final Point3 target) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().getCurrentFight() == null) {
            UIFightFrame.m_logger.warn((Object)"message recu par UIFightFrame hors combat");
            return;
        }
        if (UIFightFrame.m_fight.getStatus() != AbstractFight.FightStatus.ACTION) {
            return;
        }
        if (target != null && !target.equals(UIFightFrame.m_lastTarget)) {
            UIFightFrame.m_lastTarget.set(target);
            final UIMessage tcellChanged = new UIMessage();
            tcellChanged.setId(18015);
            Worker.getInstance().pushMessage(tcellChanged);
        }
    }
    
    public void setOverTimelineCharacter(final boolean overTimelineCharacter) {
        if (UIFightFrame.m_fight != null) {
            this.m_isOverTimelineCharacter = overTimelineCharacter;
        }
    }
    
    public static void highlightFighter(final CharacterInfo fighter, final boolean highlight) {
        if (UIFightFrame.m_fight != null) {
            UIFightFrame.m_fight.getTimeline().highlightFighter(fighter, highlight);
        }
    }
    
    private void cleanUpAps(final String elementMapId) {
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(elementMapId);
        if (map != null) {
            final Widget container = (Widget)map.getElement("apsContainer");
            if (container != null) {
                container.setVisible(false);
            }
        }
    }
    
    static {
        m_instance = new UIFightFrame();
        m_logger = Logger.getLogger((Class)UIFightFrame.class);
        UIFightFrame.m_fight = null;
        m_lastTarget = new Point3();
        TARGET_CELL_COLOR = new float[] { 0.9f, 0.9f, 0.9f, 0.5f };
        m_beaconDisplay = BeaconDisplayZone.getInstance();
        m_areaDisplay = EffectAreaDisplayZone.getInstance();
        m_movementDisplayer = new MovementInformationDisplayZone();
        final WakfuWorldScene worldScene = (WakfuWorldScene)WakfuClientInstance.getInstance().getWorldScene();
        worldScene.addHighlightCellProvidersToUpdate(UIFightFrame.m_movementDisplayer);
        worldScene.addHighlightCellProvidersToUpdate(UIFightFrame.m_beaconDisplay);
        worldScene.addHighlightCellProvidersToUpdate(UIFightFrame.m_areaDisplay);
    }
    
    private static class OverHeadContainer implements OverHeadTarget
    {
        private int m_screenX;
        private int m_screenY;
        private int m_screenHeight;
        private OverHead m_overHead;
        private boolean m_isDisplayed;
        
        @Override
        public void setScreenX(final int x) {
            this.m_screenX = x;
        }
        
        @Override
        public void setScreenY(final int y) {
            this.m_screenY = y;
        }
        
        public boolean isDisplayed() {
            return this.m_isDisplayed;
        }
        
        public void setDisplayed(final boolean displayed) {
            this.m_isDisplayed = displayed;
        }
        
        @Override
        public void setScreenTargetHeight(final int height) {
            this.m_screenHeight = height;
        }
        
        @Override
        public int getScreenX() {
            return this.m_screenX;
        }
        
        @Override
        public int getScreenY() {
            return this.m_screenY;
        }
        
        @Override
        public int getScreenTargetHeight() {
            return this.m_screenHeight;
        }
        
        @Override
        public void addWatcher(final ScreenTargetWatcher watcher) {
        }
        
        @Override
        public void removeWatcher(final ScreenTargetWatcher watcher) {
        }
        
        @Override
        public boolean isPositionComputed() {
            return false;
        }
        
        @Override
        public float getWorldX() {
            return 0.0f;
        }
        
        @Override
        public float getWorldY() {
            return 0.0f;
        }
        
        @Override
        public float getAltitude() {
            return 0.0f;
        }
        
        @Override
        public int getWorldCellX() {
            return 0;
        }
        
        @Override
        public int getWorldCellY() {
            return 0;
        }
        
        @Override
        public short getWorldCellAltitude() {
            return 0;
        }
        
        @Override
        public int getIconId() {
            return this.m_overHead.getIconId();
        }
        
        @Override
        public String getFormatedOverheadText() {
            return this.m_overHead.getFormatedOverheadText();
        }
        
        @Override
        public Color getOverHeadborderColor() {
            return this.m_overHead.getOverHeadborderColor();
        }
        
        @Override
        public void fireVisibilityChanged(final boolean visible, final VisibleChangedListener.VisibleChangedCause cause) {
        }
        
        @Override
        public void addVisibleChangedListener(final VisibleChangedListener listener) {
        }
        
        @Override
        public void removeVisibleChangedListener(final VisibleChangedListener listener) {
        }
        
        public void setOverHead(final OverHead overHead) {
            this.m_overHead = overHead;
        }
        
        public OverHead getOverHead() {
            return this.m_overHead;
        }
        
        @Override
        public String toString() {
            return "OverHeadContainer{m_screenX=" + this.m_screenX + ", m_screenY=" + this.m_screenY + ", m_screenHeight=" + this.m_screenHeight + ", m_overHead=" + this.m_overHead + ", m_isDisplayed=" + this.m_isDisplayed + '}';
        }
    }
    
    private static class MobileFilterImpl implements MobileFilter
    {
        final int m_fightId;
        
        MobileFilterImpl(final int fightId) {
            super();
            this.m_fightId = fightId;
        }
        
        @Override
        public boolean accept(final Mobile mobile) {
            return mobile.getCurrentFightId() == this.m_fightId;
        }
        
        @Override
        public String toString() {
            return "MobileFilterImpl{m_fightId=" + this.m_fightId + '}';
        }
    }
}
