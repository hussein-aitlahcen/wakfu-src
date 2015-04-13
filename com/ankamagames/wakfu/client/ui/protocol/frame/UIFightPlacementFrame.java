package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.pathfind.*;
import com.ankamagames.baseImpl.graphics.alea.cellSelector.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.worldScene.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.fight.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;

public class UIFightPlacementFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final int GRAPHICAL_MOUSE_ICON_Y_OFFSET = -30;
    private static UIFightPlacementFrame m_instance;
    private final PathFinderParameters m_defaultParameters;
    private ElementSelection m_selection;
    private BasicCharacterInfo m_selectedCharacter;
    
    public UIFightPlacementFrame() {
        super();
        this.m_defaultParameters = new PathFinderParameters();
        this.m_selection = new ElementSelection("fightPlacement", new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
    }
    
    public static UIFightPlacementFrame getInstance() {
        return UIFightPlacementFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter lp = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 18007: {
                final Fight fight = lp.getCurrentFight();
                if (fight.getStatus() != AbstractFight.FightStatus.PLACEMENT) {
                    return false;
                }
                final FighterReadyRequestMessage netMessage = new FighterReadyRequestMessage();
                final CharacterActor actor = lp.getActor();
                if (!fight.containsFighterReady(lp)) {
                    this.m_selectedCharacter = null;
                    this.handleCompanionIconDisplay();
                    netMessage.setReady(true);
                    fight.addFighterReady(lp);
                    if (!fight.isAllOtherPlayerCharacterReady(lp)) {
                        actor.setReadyForFight();
                    }
                    else {
                        WeaponAnimHelper.startUsage(actor, actor.getCurrentAttack());
                    }
                }
                else {
                    netMessage.setReady(false);
                    actor.unSetReadyForFight();
                    fight.removeFighterReady(lp);
                }
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                return false;
            }
            case 18006: {
                final FighterActorDirectionChangeRequestMessage netMessage2 = new FighterActorDirectionChangeRequestMessage();
                if (this.m_selectedCharacter == null) {
                    netMessage2.setFighterId(WakfuGameEntity.getInstance().getLocalPlayer().getId());
                }
                else {
                    netMessage2.setFighterId(this.m_selectedCharacter.getId());
                }
                netMessage2.setDirection8(Direction8.NORTH_EAST);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage2);
                return false;
            }
            case 18004: {
                final FighterActorDirectionChangeRequestMessage netMessage2 = new FighterActorDirectionChangeRequestMessage();
                if (this.m_selectedCharacter == null) {
                    netMessage2.setFighterId(WakfuGameEntity.getInstance().getLocalPlayer().getId());
                }
                else {
                    netMessage2.setFighterId(this.m_selectedCharacter.getId());
                }
                netMessage2.setDirection8(Direction8.SOUTH_EAST);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage2);
                return false;
            }
            case 18005: {
                final FighterActorDirectionChangeRequestMessage netMessage2 = new FighterActorDirectionChangeRequestMessage();
                if (this.m_selectedCharacter == null) {
                    netMessage2.setFighterId(WakfuGameEntity.getInstance().getLocalPlayer().getId());
                }
                else {
                    netMessage2.setFighterId(this.m_selectedCharacter.getId());
                }
                netMessage2.setDirection8(Direction8.SOUTH_WEST);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage2);
                return false;
            }
            case 18003: {
                final FighterActorDirectionChangeRequestMessage netMessage2 = new FighterActorDirectionChangeRequestMessage();
                if (this.m_selectedCharacter == null) {
                    netMessage2.setFighterId(WakfuGameEntity.getInstance().getLocalPlayer().getId());
                }
                else {
                    netMessage2.setFighterId(this.m_selectedCharacter.getId());
                }
                netMessage2.setDirection8(Direction8.NORTH_WEST);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage2);
                return false;
            }
            case 18011: {
                if (lp.isActiveProperty(WorldPropertyType.CALL_HELP_DISABLED)) {
                    return false;
                }
                final CallHelpRequestMessage netMessage3 = new CallHelpRequestMessage();
                netMessage3.setCallHelp(true);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage3);
                final CharacterInfo character = WakfuGameEntity.getInstance().getLocalPlayer();
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("fight.callForHelp", character.getName()));
                chatMessage.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage);
                return false;
            }
            case 18014: {
                if (UIFightFrame.m_fight != null) {
                    if (lp.isActiveProperty(WorldPropertyType.FIGHT_LOCK_DISABLED)) {
                        return false;
                    }
                    final LockFightRequestMessage lockFightRequestMessage = new LockFightRequestMessage();
                    final boolean lock = ((UIMessage)message).getBooleanValue();
                    lockFightRequestMessage.setLock(lock);
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(lockFightRequestMessage);
                }
                return false;
            }
            case 19994:
            case 19995: {
                if (UIFightFrame.getInstance().getCellReportMode()) {
                    return true;
                }
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                if (player.getCurrentFight().containsFighterReady(player)) {
                    return true;
                }
                final FightMap fightMap = player.getCurrentFight().getFightMap();
                final ArrayList<DisplayedScreenElement> hitElements = WakfuClientInstance.getInstance().getWorldScene().getDisplayedElementsUnderMousePoint(msg.getMouseX(), msg.getMouseY(), player.getWorldCellAltitude(), DisplayedScreenElementComparator.Z_ORDER);
                final boolean mousePointAtSelectableMobile = this.findFirstControlledMobileUnderMouse(msg.getMouseX(), msg.getMouseY()) != null;
                final Point3 target = this.findSelectedCellCoord(player, fightMap, hitElements);
                this.m_selection.clear();
                if (target != null && !mousePointAtSelectableMobile) {
                    this.m_selection.add(target.getX(), target.getY(), target.getZ());
                }
                return true;
            }
            case 19992: {
                if (UIFightFrame.getInstance().getCellReportMode()) {
                    return true;
                }
                final UIWorldSceneMouseMessage msg = (UIWorldSceneMouseMessage)message;
                final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
                final Fight fight2 = player.getCurrentFight();
                if (fight2.containsFighterReady(player)) {
                    return true;
                }
                final FightMap fightMap2 = fight2.getFightMap();
                final ArrayList<DisplayedScreenElement> hitElements2 = WakfuClientInstance.getInstance().getWorldScene().getDisplayedElementsUnderMousePoint(msg.getMouseX(), msg.getMouseY(), player.getWorldCellAltitude(), DisplayedScreenElementComparator.Z_ORDER);
                if (msg.isButtonLeft()) {
                    final Point3 target = this.findSelectedCellCoord(player, fightMap2, hitElements2);
                    final boolean selectionChanged = this.handleCharacterSelection(msg.getMouseX(), msg.getMouseY());
                    if (!selectionChanged && target != null) {
                        WakfuSoundManager.getInstance().playGUISound(600071L);
                        if (this.m_selectedCharacter == null || this.m_selectedCharacter == player) {
                            final FightPlacementRequestMessage netMessage4 = new FightPlacementRequestMessage();
                            netMessage4.setPosition(target);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage4);
                        }
                        else {
                            final FightCompanionPlacementRequestMessage requestMessage = new FightCompanionPlacementRequestMessage();
                            requestMessage.setCharacterId(this.m_selectedCharacter.getId());
                            requestMessage.setPosition(target);
                            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(requestMessage);
                        }
                    }
                    return false;
                }
                if (msg.isButtonRight()) {
                    this.m_selectedCharacter = null;
                    GraphicalMouseManager.getInstance().hide();
                    return true;
                }
                return true;
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean handleCharacterSelection(final int mouseX, final int mouseY) {
        final BasicCharacterInfo previousSelectedCharacter = this.m_selectedCharacter;
        final BasicCharacterInfo firstControlledMobileUnderMouse = this.findFirstControlledMobileUnderMouse(mouseX, mouseY);
        if (firstControlledMobileUnderMouse != null) {
            this.m_selectedCharacter = firstControlledMobileUnderMouse;
        }
        this.handleCompanionIconDisplay();
        return this.m_selectedCharacter != previousSelectedCharacter;
    }
    
    public void setSelectedCharacter(final BasicCharacterInfo selectedCharacter) {
        this.m_selectedCharacter = selectedCharacter;
    }
    
    public BasicCharacterInfo getSelectedCharacter() {
        return this.m_selectedCharacter;
    }
    
    public void handleCompanionIconDisplay() {
        if (this.m_selectedCharacter instanceof NonPlayerCharacter) {
            final NonPlayerCharacter npc = (NonPlayerCharacter)this.m_selectedCharacter;
            if (npc.hasProperty(WorldPropertyType.COMPANION)) {
                try {
                    final String iconUrl = String.format(WakfuConfiguration.getInstance().getString("companionIconsPath"), npc.getBreedId());
                    this.showMouseInformation(iconUrl);
                }
                catch (PropertyException e) {
                    UIFightPlacementFrame.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
        }
        else if (this.m_selectedCharacter != null && HeroesLeaderManager.INSTANCE.isHeroOfClient(WakfuGameEntity.getInstance().getLocalPlayer().getClientId(), this.m_selectedCharacter)) {
            try {
                final String iconUrl2 = String.format(WakfuConfiguration.getInstance().getString("breedPortraitIllustrationPath"), this.m_selectedCharacter.getBreedId() + String.valueOf(this.m_selectedCharacter.getSex()));
                this.showMouseInformation(iconUrl2);
            }
            catch (PropertyException e2) {
                UIFightPlacementFrame.m_logger.error((Object)"Exception", (Throwable)e2);
            }
        }
        else {
            GraphicalMouseManager.getInstance().hide();
        }
    }
    
    private void showMouseInformation(final String iconUrl) {
        GraphicalMouseManager.getInstance().showMouseInformation(iconUrl, null, 10, -30, Alignment9.NORTH_WEST);
    }
    
    @Nullable
    private BasicCharacterInfo findFirstControlledMobileUnderMouse(final int mouseX, final int mouseY) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ArrayList<Mobile> mobilesUnderMousePoint = WakfuClientInstance.getInstance().getWorldScene().getMobilesUnderMousePoint(mouseX, mouseY);
        if (mobilesUnderMousePoint == null || mobilesUnderMousePoint.isEmpty()) {
            return null;
        }
        for (final Mobile mobile : mobilesUnderMousePoint) {
            if (!(mobile instanceof CharacterActor)) {
                continue;
            }
            final CharacterActor actor = (CharacterActor)mobile;
            final CharacterInfo characterInfo = actor.getCharacterInfo();
            if (HeroesLeaderManager.INSTANCE.isHeroOfClient(localPlayer.getClientId(), characterInfo)) {
                return characterInfo;
            }
            if (characterInfo.isControlledByLocalPlayer()) {
                return characterInfo;
            }
        }
        return null;
    }
    
    private Point3 findSelectedCellCoord(final LocalPlayerCharacter player, final FightMap fightMap, final ArrayList<DisplayedScreenElement> hitElements) {
        final Point3 target = new Point3();
        final byte teamId = player.getTeamId();
        for (int index = 0; index < hitElements.size(); ++index) {
            final DisplayedScreenElement displayedElement = hitElements.get(index);
            target.set(displayedElement.getElement().getCoordinates());
            if (player.getPosition().equals(target)) {
                return null;
            }
            final byte cellTeam = fightMap.getTeamValidForStartPosition(target.getX(), target.getY());
            if (teamId == cellTeam && fightMap.checkPosition(player, target) && fightMap.getCellHeight(target.getX(), target.getY()) == target.getZ()) {
                return target;
            }
        }
        return null;
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
            PropertiesProvider.getInstance().setPropertyValue("isInFightPlacement", true);
            UIControlCenterContainerFrame.getInstance().setDirectionButtonsEnabled(true);
            Xulor.getInstance().putActionClass("wakfu.fightAction", FightActionDialogActions.class);
            this.m_selectedCharacter = null;
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().setPropertyValue("isInFightPlacement", false);
            UIControlCenterContainerFrame.getInstance().setDirectionButtonsEnabled(false);
            this.cleanUpAps("worldAndFightBarDialog");
            Xulor.getInstance().removeActionClass("wakfu.fightCreationOrPlacement");
            this.m_selection.clear();
            this.m_selectedCharacter = null;
            GraphicalMouseManager.getInstance().hide();
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
        m_logger = Logger.getLogger((Class)UIFightPlacementFrame.class);
        UIFightPlacementFrame.m_instance = new UIFightPlacementFrame();
    }
}
