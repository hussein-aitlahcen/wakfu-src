package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.game.personalSpace.room.content.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.isometric.maskableLayer.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.serverToClient.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.emote.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.ui.protocol.message.shortcut.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.framework.graphics.engine.fadeManager.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.actor.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient.*;
import com.ankamagames.wakfu.client.core.account.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.core.game.shortcut.*;
import com.ankamagames.wakfu.common.game.shortcut.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.common.game.resource.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.skill.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.resource.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.scenario.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.monster.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.group.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.xp.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.zone.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.wakfu.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.aptitudenew.*;
import com.ankamagames.wakfu.client.core.game.achievements.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.achievements.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.playerTitle.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.travel.*;
import com.ankamagames.wakfu.client.core.krosmoz.collection.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.krosmoz.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.lock.*;
import com.ankamagames.wakfu.common.game.fightChallenge.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fightChallenge.*;
import com.ankamagames.wakfu.client.core.emote.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.wakfu.common.game.item.action.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.zone.*;
import com.ankamagames.wakfu.common.game.group.party.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import gnu.trove.*;

public class NetActorsFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final NetActorsFrame m_instance;
    public static final boolean DEBUG_SPAWNS = false;
    
    public static NetActorsFrame getInstance() {
        return NetActorsFrame.m_instance;
    }
    
    private void addPlayersVisitingBagRoomContent(final ClientMapInteractiveElement element) {
        final LocalPlayerCharacter player = WakfuClientInstance.getGameEntity().getLocalPlayer();
        final DimensionalBagView bag = player.getVisitingDimentionalBag();
        if (bag != null && element instanceof RoomContent) {
            bag.putRoomContent((RoomContent)element);
        }
    }
    
    private void removePlayersVisitingBagRoomContent(final ClientMapInteractiveElement element) {
        final LocalPlayerCharacter player = WakfuClientInstance.getGameEntity().getLocalPlayer();
        final DimensionalBagView bag = player.getVisitingDimentionalBag();
        if (bag != null && element instanceof RoomContent) {
            bag.removeRoomContent((RoomContent)element);
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 204: {
                final DynamicInteractiveElementSpawnMessage spawnMessage = (DynamicInteractiveElementSpawnMessage)message;
                final TLongObjectIterator<ObjectTriplet<Long, byte[], Long>> it = spawnMessage.getInteractiveElements().iterator();
                while (it.hasNext()) {
                    it.advance();
                    final long elementId = it.key();
                    final long templateId = it.value().getFirst();
                    final byte[] elementDatas = it.value().getSecond();
                    final Long longPos = it.value().getThird();
                    final WakfuClientMapInteractiveElement element = WakfuClientInteractiveElementFactory.getInstance().createDummyInteractiveElement(templateId);
                    if (element != null) {
                        final Point3 position = PositionValue.fromLong(longPos);
                        element.setPosition(position);
                        element.fromBuild(elementDatas);
                        element.unsetDummy();
                        element.setId(elementId);
                        for (final ClientInteractiveElementView view : element.getViews()) {
                            if (view instanceof ClientInteractiveAnimatedElementSceneView) {
                                final ClientInteractiveAnimatedElementSceneView clientView = (ClientInteractiveAnimatedElementSceneView)view;
                                AnimatedElementSceneViewManager.getInstance().addElement(clientView);
                                clientView.fadeIfOnScreen();
                                MaskableHelper.setUndefined(clientView);
                            }
                        }
                        element.notifyViews();
                        this.addPlayersVisitingBagRoomContent(element);
                        LocalPartitionManager.getInstance().addInteractiveElement(element);
                    }
                    else {
                        NetActorsFrame.m_logger.error((Object)("Impossible de spawner l'\u00ef¿½l\u00ef¿½ment interactif instanceId=" + elementId));
                    }
                }
                return false;
            }
            case 200: {
                final InteractiveElementSpawnMessage spawnMessage2 = (InteractiveElementSpawnMessage)message;
                final TLongObjectIterator<byte[]> it2 = spawnMessage2.getInteractiveElements().iterator();
                while (it2.hasNext()) {
                    it2.advance();
                    try {
                        final long elementId = it2.key();
                        final byte[] elementDatas2 = it2.value();
                        NetActorsFrame.m_logger.trace((Object)("[INTERACTIVE_ELEMENT_SPAWN_MESSAGE] Element id=" + elementId + ", data.length=" + elementDatas2.length + " byte(s)"));
                        final WakfuClientMapInteractiveElement element2 = WakfuClientMapInteractiveElement.spawnNewElement(elementId, elementDatas2);
                        if (element2 != null) {
                            LocalPartitionManager.getInstance().addInteractiveElement(element2);
                        }
                        else {
                            NetActorsFrame.m_logger.error((Object)("spawn d'un element interactif inconnu id=" + elementId));
                        }
                    }
                    catch (Exception e) {
                        NetActorsFrame.m_logger.error((Object)"", (Throwable)e);
                    }
                }
                return false;
            }
            case 206: {
                final InteractiveElementDespawnMessage despawnMessage = (InteractiveElementDespawnMessage)message;
                final TLongArrayList interactiveElementIds = despawnMessage.getInteractiveElementIds();
                for (int i = 0, size = interactiveElementIds.size(); i < size; ++i) {
                    final long elementId2 = interactiveElementIds.getQuick(i);
                    final WakfuClientMapInteractiveElement element3 = (WakfuClientMapInteractiveElement)LocalPartitionManager.getInstance().getInteractiveElement(elementId2);
                    if (element3 != null) {
                        if (element3.isUsingDespawnAnimation()) {
                            element3.runDespawnAnimation();
                            for (final ClientInteractiveElementView view2 : element3.getViews()) {
                                if (view2 instanceof AnimatedElement) {
                                    ((AnimatedElement)view2).addAnimationEndedListener(new AnimationEndedListener() {
                                        @Override
                                        public void animationEnded(final AnimatedElement element) {
                                            final ClientMapInteractiveElement iElement = ((ClientInteractiveElementView)element).getInteractiveElement();
                                            if (iElement != null) {
                                                NetActorsFrame.this.removePlayersVisitingBagRoomContent(iElement);
                                                LocalPartitionManager.getInstance().removeInteractiveElement(iElement);
                                            }
                                            else {
                                                NetActorsFrame.m_logger.warn((Object)("Impossible de retirer un \u00ef¿½l\u00ef¿½ment interactif ID=" + elementId2 + ", il n'est r\u00ef¿½f\u00ef¿½renc\u00ef¿½ dans aucune partition."));
                                            }
                                            element.removeAnimationEndedListener(this);
                                        }
                                    });
                                }
                            }
                        }
                        else {
                            this.removePlayersVisitingBagRoomContent(element3);
                            LocalPartitionManager.getInstance().removeInteractiveElement(element3);
                        }
                    }
                    else {
                        NetActorsFrame.m_logger.warn((Object)("Impossible de retirer un \u00ef¿½l\u00ef¿½ment interactif ID=" + elementId2 + ", il n'est r\u00ef¿½f\u00ef¿½renc\u00ef¿½ dans aucune partition."));
                    }
                }
                return false;
            }
            case 202: {
                final InteractiveElementUpdateMessage updateMessage = (InteractiveElementUpdateMessage)message;
                final long elementId3 = updateMessage.getElementId();
                final byte[] datas = updateMessage.getSharedDatas();
                NetActorsFrame.m_logger.trace((Object)("[INTERACTIVE_ELEMENT_UPDATE_MESSAGE] elementId=" + elementId3));
                if (datas != null) {
                    final WakfuClientMapInteractiveElement element3 = (WakfuClientMapInteractiveElement)LocalPartitionManager.getInstance().getInteractiveElement(elementId3);
                    if (element3 != null) {
                        element3.fromBuild(datas);
                        element3.notifyViews();
                    }
                    else {
                        NetActorsFrame.m_logger.error((Object)"L'\u00ef¿½l\u00ef¿½ment interactif \u00ef¿½ mettre \u00ef¿½ jour n'est pas dans les partitions g\u00ef¿½r\u00ef¿½es par le client.");
                    }
                }
                else {
                    NetActorsFrame.m_logger.warn((Object)"Message de mise \u00ef¿½ jour d'\u00ef¿½l\u00ef¿½ment interactif re\u00ef¿½u sans aucune donn\u00ef¿½e \u00ef¿½ mettre \u00ef¿½ jour.");
                }
                return false;
            }
            case 4102: {
                final ActorSpawnMessage msg = (ActorSpawnMessage)message;
                for (int j = 0; j < msg.getCharacterTypes().size(); ++j) {
                    final long characterId = msg.getCharacterIds().get(j);
                    if (localPlayer == null || characterId != localPlayer.getId()) {
                        try {
                            final CharacterInfo previousChar = CharacterInfoManager.getInstance().getCharacter(characterId);
                            if (previousChar != null) {
                                if (previousChar.isWaitEndAnimationToBeDespawned()) {
                                    previousChar.getActor().clearAnimationEndedListener();
                                    CharacterInfoManager.getInstance().removeCharacter(previousChar);
                                }
                                else {
                                    if (msg.isMyFightSpawn()) {
                                        if (!previousChar.isSpawnInMyFight()) {
                                            previousChar.setSpawnInMyFight(true);
                                        }
                                        continue;
                                    }
                                    if (previousChar.isSpawnInWorld()) {}
                                    CharacterInfoManager.getInstance().removeCharacter(previousChar);
                                }
                            }
                            final byte type = msg.getCharacterTypes().get(j);
                            CharacterInfo character = null;
                            switch (type) {
                                case 0: {
                                    character = new PlayerCharacter();
                                    break;
                                }
                                case 1: {
                                    character = NonPlayerCharacter.createNpc();
                                    break;
                                }
                                case 4: {
                                    character = new InteractiveNonPlayerCharacter();
                                    break;
                                }
                            }
                            if (character == null) {
                                NetActorsFrame.m_logger.error((Object)("Spawn d'acteur de type inconnu : " + type));
                            }
                            else {
                                boolean addToFight = false;
                                character.fromBuild(msg.getCharacterSerialized().get(j));
                                character.initialize();
                                if (msg.isMyFightSpawn()) {
                                    character.setSpawnInMyFight(true);
                                }
                                else {
                                    character.setSpawnInWorld(true);
                                }
                                if (CharacterInfoManager.getInstance().addAndSpawnCharacter(character)) {
                                    character.initializeAfterCharacterAddedToWorld();
                                    if (character.getCurrentFightId() != -1) {
                                        addToFight = true;
                                    }
                                    final int px = MapConstants.getMapCoordFromCellX(character.getPositionConst().getX());
                                    final int py = MapConstants.getMapCoordFromCellY(character.getPositionConst().getY());
                                    if (character instanceof PlayerCharacter) {
                                        final PlayerCharacter playerCharacter = (PlayerCharacter)character;
                                        if (localPlayer.getPartyComportment().isInParty() && localPlayer.getPartyComportment().getParty().contains(characterId)) {
                                            UIPartyFrame.getInstance().onCharacterSpawn(characterId);
                                        }
                                        playerCharacter.updateAdditionalAppearance();
                                    }
                                    final CharacterActor actor = character.getActor();
                                    if (addToFight) {
                                        this.addCharacterToFight(character, actor);
                                    }
                                    if (character.isInvisibleForLocalPlayer()) {
                                        character.onPropertyUpdated(FightPropertyType.INVISIBLE);
                                    }
                                    else {
                                        actor.fadeIfOnScreen();
                                    }
                                    if (character.getType() == 5) {
                                        actor.setVisible(false);
                                    }
                                    if (character instanceof NonPlayerCharacter && character.isDead()) {
                                        ((NonPlayerCharacter)character).onNPCDeath();
                                    }
                                    MaskableHelper.setUndefined(actor);
                                }
                                else {
                                    character.release();
                                }
                            }
                        }
                        catch (Exception e2) {
                            NetActorsFrame.m_logger.error((Object)("Exception lors du spawn de l'actor " + characterId), (Throwable)e2);
                        }
                    }
                }
                return false;
            }
            case 4104: {
                final ActorDespawnMessage msg2 = (ActorDespawnMessage)message;
                for (final ObjectPair<Byte, Long> actorId : msg2.getActorsIds()) {
                    this.despawnActor(actorId.getSecond(), actorId.getFirst(), false, msg2.isApplyApsOnDespawn());
                }
                return false;
            }
            case 15720: {
                final VisualAnimationMessage msg3 = (VisualAnimationMessage)message;
                final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(msg3.getCharacterId());
                final int visualId = msg3.getVisualId();
                final int linkedRefId = msg3.getLinkedItemRefId();
                final boolean endAnimation = msg3.isEndAnimation();
                character2.getActor().setVisualAnimation(visualId, endAnimation);
                return false;
            }
            case 15722: {
                final EmoteLearnedMessage msg4 = (EmoteLearnedMessage)message;
                final int emoteId = msg4.getEmoteId();
                final LocalPlayerCharacter hero = HeroesManager.INSTANCE.getHero(msg4.getCharacterId());
                final ClientEmoteHandler clientEmoteHandler = hero.getEmoteHandler();
                clientEmoteHandler.learnEmote(emoteId);
                final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("emote.learnt", WakfuTranslator.getInstance().getString(80, emoteId, new Object[0])));
                chatMessage.setPipeDestination(4);
                ChatManager.getInstance().pushMessage(chatMessage);
                PropertiesProvider.getInstance().firePropertyValueChanged(clientEmoteHandler, clientEmoteHandler.getFields());
                hero.getShortcutBarManager().setCurrentBarType(ShortCutBarType.WORLD);
                final UIShortcutMessage uiMessage = new UIShortcutMessage();
                uiMessage.setItem(clientEmoteHandler.getEmote(emoteId));
                uiMessage.setShorcutBarNumber(-1);
                uiMessage.setPosition(-1);
                uiMessage.setBooleanValue(false);
                uiMessage.setForce(true);
                uiMessage.setId(16700);
                Worker.getInstance().pushMessage(uiMessage);
                return false;
            }
            case 4116: {
                final ActorSetActionMessage actionMsg = (ActorSetActionMessage)message;
                final long id = actionMsg.getActorId();
                final CharacterInfo character3 = CharacterInfoManager.getInstance().getCharacter(id);
                if (character3 != null) {
                    final CharacterActor actor2 = character3.getActor();
                    if (actor2.getCurrentPath() != null) {
                        if (!actionMsg.isEnd()) {
                            final ActorSkillEndMovementListener skillEndListener = new ActorSkillEndMovementListener(character3, actionMsg.getActionId(), actionMsg.getResX(), actionMsg.getResY());
                            character3.setSkillEndMovementListener(skillEndListener);
                        }
                    }
                    else {
                        final ActionVisual actionVisual = ActionVisualManager.getInstance().get(actionMsg.getActionId());
                        if (actionVisual != null) {
                            final Resource source = ResourceManager.getInstance().getResource(actionMsg.getResX(), actionMsg.getResY());
                            ClientMapInteractiveElement element4 = null;
                            if (source == null) {
                                element4 = LocalPartitionManager.getInstance().getInteractiveElement(actionMsg.getElementId());
                            }
                            character3.setCurrentInteractiveElement(source);
                            final String actorAnimation = actor2.getAnimation();
                            final String[] skillAnimation = ((source != null) ? actionVisual.getAnimLink() : ((element4 != null) ? "AnimCraft-Debut" : "")).split("-");
                            if (actionMsg.isEnd()) {
                                final String animationEnd = skillAnimation[0].isEmpty() ? "AnimStatique" : (skillAnimation[0] + "-Fin");
                                actor2.setAnimation(animationEnd);
                                actor2.getActionInProgress().stopHelp();
                            }
                            else if (!actorAnimation.equals(skillAnimation[0])) {
                                if (source != null && element4 != null) {
                                    int x;
                                    int y;
                                    short z;
                                    if (source != null) {
                                        x = source.getWorldCellX();
                                        y = source.getWorldCellY();
                                        z = source.getWorldCellAltitude();
                                    }
                                    else {
                                        x = element4.getWorldCellX();
                                        y = element4.getWorldCellY();
                                        z = element4.getWorldCellAltitude();
                                    }
                                    Direction8 directionTo = character3.getPositionConst().getDirectionTo(x, y, z);
                                    if (directionTo == null) {
                                        directionTo = character3.getPositionConst().getDirectionTo(new Point3(actionMsg.getResX(), actionMsg.getResY(), (short)0));
                                    }
                                    if (directionTo != null) {
                                        actor2.setDirection(directionTo);
                                    }
                                }
                                if (element4 == null) {
                                    ActionVisualHelper.applyActionVisual(actor2, actionVisual);
                                }
                                else {
                                    actor2.setAnimation("AnimCraft-Debut");
                                }
                            }
                        }
                    }
                }
                return false;
            }
            case 4115: {
                final ActorStopMovementMessage msg5 = (ActorStopMovementMessage)message;
                final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(msg5.getActorId());
                if (character2 != null) {
                    final CharacterActor actor3 = character2.getActor();
                    actor3.stopMoving();
                    if (!actor3.moveTo(new Point3(msg5.getX(), msg5.getY(), msg5.getZ()), false, false)) {
                        actor3.setWorldPosition(msg5.getX(), msg5.getY(), msg5.getZ());
                    }
                }
                else {
                    NetActorsFrame.m_logger.error((Object)"[ACTOR_STOP_MOVEMENT_MESSAGE] Unknow mobile");
                }
                return false;
            }
            case 4118: {
                final ActorChangeDirectionMessage msg6 = (ActorChangeDirectionMessage)message;
                final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(msg6.getPlayerId());
                if (character2 != null) {
                    final CharacterActor actor3 = character2.getActor();
                    if (actor3.getCurrentPath() == null || actor3.getCurrentPath().getPathLength() == 0) {
                        actor3.setDirection(msg6.getDirection());
                        character2.removeSkillEndMovementListener();
                    }
                    else {
                        final Direction8 direction = msg6.getDirection();
                        actor3.addEndPositionListener(new MobileEndPathListener() {
                            @Override
                            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                                mobile.setDirection(direction);
                                mobile.removeEndPositionListener(this);
                            }
                        });
                    }
                }
                else {
                    NetActorsFrame.m_logger.error((Object)"[ACTOR_DIRECTION_CHANGE_MESSAGE] Unknow mobile");
                }
                return false;
            }
            case 4126: {
                final ActorTeleportMessage msg7 = (ActorTeleportMessage)message;
                final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(msg7.getActorId());
                if (info != null) {
                    if (info.isOnFight()) {
                        NetActorsFrame.m_logger.error((Object)"on ne catche pas un teleport dans les frames de fight ?");
                        return true;
                    }
                    final int posX = msg7.getX();
                    final int posY = msg7.getY();
                    final short posZ = msg7.getAltitude();
                    NetActorsFrame.m_logger.trace((Object)("T\u00ef¿½l\u00ef¿½portation de l'acteur ID=" + msg7.getActorId() + " vers [" + posX + ":" + posY + ":" + msg7.getAltitude() + "]"));
                    final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
                    if (info == localPlayer && !msg7.isGenerateMove()) {
                        final AleaIsoCamera camera = scene.getIsoCamera();
                        final double camX = camera.getCameraExactIsoWorldX();
                        final double camY = camera.getCameraExactIsoWorldY();
                        final double squareDistance = (camX - posX) * (camX - posX) + (camY - posY) * (camY - posY);
                        if (squareDistance > 400.0 || msg7.forceLoading()) {
                            final int lastWorld = MapManagerHelper.getWorldId();
                            WakfuSceneFader.getInstance().sceneLoadingTransition(lastWorld);
                            MapManagerHelper.initializeSceneCamera(scene, posX, posY, posZ);
                        }
                    }
                    if (info == localPlayer) {
                        final CharacterActor localActor = localPlayer.getActor();
                        final FadeListener onMapLoaded = new FadeListener() {
                            @Override
                            public void onFadeInEnd() {
                                WakfuSoundManager.getInstance().waitForMapLoad();
                            }
                            
                            @Override
                            public void onFadeOutEnd() {
                            }
                            
                            @Override
                            public void onFadeInStart() {
                            }
                            
                            @Override
                            public void onFadeOutStart() {
                                WakfuSoundManager.getInstance().stopWaitingForMapLoad();
                                FadeManager.getInstance().removeListener(this);
                                info.getActor().onCellChanged(posX, posY, msg7.getAltitude());
                            }
                        };
                        localActor.removePositionListener(LocalPartitionManager.getInstance());
                        this.moveCharacter(info, posX, posY, posZ, msg7.isGenerateMove());
                        localActor.addPositionListener(LocalPartitionManager.getInstance());
                        if (FadeManager.getInstance().isFadeIn()) {
                            FadeManager.getInstance().addListener(onMapLoaded);
                        }
                        else {
                            onMapLoaded.onFadeInEnd();
                        }
                    }
                    else {
                        this.moveCharacter(info, posX, posY, posZ, msg7.isGenerateMove());
                    }
                }
                else {
                    NetActorsFrame.m_logger.error((Object)("Impossible de trouver l'acteur ID=" + msg7.getActorId() + " pour le t\u00ef¿½l\u00ef¿½porter."));
                }
                return false;
            }
            case 4114: {
                final ActorPathUpdateMessage msg8 = (ActorPathUpdateMessage)message;
                final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(msg8.getActorId());
                if (info != null) {
                    final CharacterActor actor3 = info.getActor();
                    final Direction8Path path = msg8.getPath();
                    actor3.updateActorPath(path);
                    if (info instanceof PlayerCharacter) {
                        NetActorsFrame.m_logger.trace((Object)("Update de chemin re\u00ef¿½ue pour le personnage " + info.getName() + "(" + info.getId() + ") : currentPos=[" + actor3.getCurrentWorldX() + ":" + actor3.getCurrentWorldY() + "] newPath=" + path));
                    }
                }
                else {
                    NetActorsFrame.m_logger.error((Object)("Impossible de d\u00ef¿½placer le personnage " + msg8.getActorId() + " car il n'existe pas !"));
                }
                return false;
            }
            case 4127: {
                final ActorMoveToMessage msg9 = (ActorMoveToMessage)message;
                final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(msg9.getActorId());
                if (character2 == null) {
                    return false;
                }
                this.moveCharacterActor(msg9, character2);
                return false;
            }
            case 4210: {
                final ActorNonCombatSkillXpGainedMessage msg10 = (ActorNonCombatSkillXpGainedMessage)message;
                final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(msg10.getActorId());
                if (character2 != null) {
                    character2.addSkillXp(msg10.getSkillId(), msg10.getXpGained(), msg10.isLevelGained());
                }
                if (Xulor.getInstance().isLoaded("spellsDialog")) {
                    final UISpellsPageFrame spellsDlg = UISpellsPageFrame.getInstance();
                    spellsDlg.updateAll();
                }
                return false;
            }
            case 8036: {
                final AggroActorIsWatchingLPCMessage msg11 = (AggroActorIsWatchingLPCMessage)message;
                final Iterator<Map.Entry<Long, Byte>> it3 = msg11.getActorIdIterator();
                while (it3.hasNext()) {
                    final Map.Entry<Long, Byte> entry = it3.next();
                    final CharacterInfo character3 = CharacterInfoManager.getInstance().getCharacter(entry.getKey());
                    if (entry.getValue() == 1) {
                        if (character3 == null || localPlayer == null) {
                            continue;
                        }
                        character3.setDirection(Vector3.getDirection4FromVector(localPlayer.getWorldCellX() - character3.getWorldCellX(), localPlayer.getWorldCellY() - character3.getWorldCellY()));
                        character3.getActor().addCrossSwordParticleSystem((byte)(-1));
                    }
                    else {
                        if (character3 == null) {
                            continue;
                        }
                        character3.getActor().clearCrossSwordParticleSystem();
                    }
                }
                return false;
            }
            case 4216: {
                final ActorUpdatePropertiesMessage msg12 = (ActorUpdatePropertiesMessage)message;
                final TLongObjectHashMap<byte[]> serializedPropertiesActorInfos = msg12.getSerializedPropertiesActorInfos();
                final TLongObjectIterator<byte[]> iterator = serializedPropertiesActorInfos.iterator();
                while (iterator.hasNext()) {
                    iterator.advance();
                    final CharacterInfo character3 = CharacterInfoManager.getInstance().getCharacter(iterator.key());
                    if (character3 != null) {
                        character3.fromBuild(iterator.value());
                    }
                }
                return false;
            }
            case 8402: {
                final ActorSpellLearnMessage msg13 = (ActorSpellLearnMessage)message;
                NetActorsFrame.m_logger.trace((Object)"[SPELL] reception d'un message d'apprentissage de nouveaux sorts");
                final ArrayList<ObjectTriplet<Integer, Long, Short>> learnedSpells = msg13.getLearnedSpells();
                for (final ObjectTriplet<Integer, Long, Short> spell : learnedSpells) {
                    try {
                        SpellLevel spellLevel;
                        if (spell.getFirst() != 539) {
                            spellLevel = new SpellLevel(SpellManager.getInstance().getSpell(spell.getFirst()), spell.getThird(), spell.getSecond());
                        }
                        else {
                            spellLevel = new WeaponSkillSpellLevel(localPlayer, SpellManager.getInstance().getSpell(spell.getFirst()), spell.getSecond(), localPlayer.getSkillInventory().getSkillsFromType(SkillType.WEAPON_SKILL));
                        }
                        localPlayer.getSpellInventory().add(spellLevel);
                        localPlayer.getBreedInfo().addSpell(spellLevel.getSpell(), spellLevel);
                        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventSpellLearn(spellLevel.getSpell().getId()));
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("spell.newSpellLearn", ChatConstants.CHAT_FIGHT_EFFECT_COLOR, SpellManager.getInstance().getSpell(spell.getFirst()).getName()), 4);
                        UIControlCenterContainerFrame.getInstance().highLightSpellsButton();
                        if (!spellLevel.getSpell().isPassive()) {
                            continue;
                        }
                        for (final WakfuEffect e3 : spellLevel) {
                            if (e3.getEffectType() != 0) {
                                continue;
                            }
                            e3.execute(spellLevel, localPlayer, localPlayer.getOwnContext(), RunningEffectConstants.getInstance(), localPlayer.getWorldCellX(), localPlayer.getWorldCellY(), localPlayer.getWorldCellAltitude(), localPlayer, null, false);
                        }
                    }
                    catch (InventoryCapacityReachedException e5) {
                        NetActorsFrame.m_logger.error((Object)"[SPELL] Plus de place dans l'inventaire des sort pour en apprendre un nouveau");
                    }
                    catch (ContentAlreadyPresentException e6) {
                        NetActorsFrame.m_logger.error((Object)"[SPELL] Sort d\u00ef¿½j\u00ef¿½ appri et pr\u00ef¿½sent dans l'inventaire");
                    }
                }
                if (Xulor.getInstance().isLoaded("spellsDialog")) {
                    PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer.getBreedInfo(), localPlayer.getBreedInfo().getFields());
                }
                return false;
            }
            case 4000: {
                final ErrorResultMessage msg14 = (ErrorResultMessage)message;
                NetActorsFrame.m_logger.trace((Object)"[ERROR] reception d'une erreur");
                final int resultId = msg14.getResultId();
                ErrorsMessageTranslator.getInstance().pushMessage(resultId, 3, new Object[0]);
                if (ClientEventErrorMessageReceived.allow(resultId)) {
                    ClientGameEventManager.INSTANCE.fireEvent(new ClientEventErrorMessageReceived(resultId));
                }
                return false;
            }
            case 4124: {
                final CharacterHealthUpdateMessage healthUpdateMessage = (CharacterHealthUpdateMessage)message;
                localPlayer.getHpRegenHandler().synchronizeValue(healthUpdateMessage.getValue(), healthUpdateMessage.getHealthRegen());
                return false;
            }
            case 5402: {
                final SymbiotHealthUpdateMessage symbiotUpdateMessage = (SymbiotHealthUpdateMessage)message;
                final CharacterInfo character4 = CharacterInfoManager.getInstance().getCharacter(symbiotUpdateMessage.getCharacterId());
                if (character4 != null && character4 instanceof LocalPlayerCharacter) {
                    final Symbiot symbiot = ((LocalPlayerCharacter)character4).getSymbiot();
                    if (symbiot != null) {
                        PropertiesProvider.getInstance().firePropertyValueChanged(SymbiotView.getInstance(), SymbiotView.FIELDS);
                    }
                }
                return false;
            }
            case 4130: {
                final CharacterUpdateMessage updateMessage2 = (CharacterUpdateMessage)message;
                final CharacterInfo character4 = CharacterInfoManager.getInstance().getCharacter(updateMessage2.getCharacterId());
                if (character4 != null) {
                    character4.fromBuild(updateMessage2.getSerializedData());
                }
                else {
                    NetActorsFrame.m_logger.error((Object)("[CHARACTER_UPDATE_MESSAGE] Impossible de trouver le CharacterInfo ID=" + updateMessage2.getCharacterId()));
                }
                return false;
            }
            case 1028: {
                final ClientAccountInformationUpdateMessage updateMessage3 = (ClientAccountInformationUpdateMessage)message;
                final byte[] accountInformations = updateMessage3.getSerializedAccountInformations();
                if (accountInformations != null) {
                    final LocalAccountInformations localAccountInformations = new LocalAccountInformations();
                    localAccountInformations.fromBuild(accountInformations);
                    WakfuGameEntity.getInstance().setLocalAccount(localAccountInformations);
                }
                ClientEffectApplier.INSTANCE.reloadSubscriptionState(WakfuGameEntity.getInstance().getLocalPlayer());
                return false;
            }
            case 15132: {
                final CrimePurgationStartedMessage msg15 = (CrimePurgationStartedMessage)message;
                final int nationId = msg15.getNationId();
                localPlayer.getCitizenComportment().startCrimePurgation(nationId);
                return false;
            }
            case 15133: {
                ((ClientCitizenComportment)localPlayer.getCitizenComportment()).cancelCrimePurgation();
                return false;
            }
            case 4204: {
                final ResourceSynchroMessage msg16 = (ResourceSynchroMessage)message;
                final byte flag = msg16.getFlag();
                if (flag == 7) {
                    return false;
                }
                if (flag == 4) {
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("collect.error.resourceOccupied"), 4);
                    return false;
                }
                if (!localPlayer.getCraftHandler().contains(msg16.getCraftId())) {
                    NetActorsFrame.m_logger.error((Object)("le joueur ne poss\u00ef¿½de pas ce skill " + msg16.getCraftId()));
                    return false;
                }
                if (flag == 5) {
                    final ArrayList<ObjectPair<Long, Item>> items = msg16.getItems();
                    for (final ObjectPair<Long, Item> item : items) {
                        final Item concernedItem = item.getSecond();
                        final short saveQuantity = concernedItem.getQuantity();
                        final AbstractBag container = localPlayer.getBags().get(item.getFirst());
                        if (container != null && container.getContentChecker() != null && container.getContentChecker().canAddItem((Inventory<Item>)container.getInventory(), concernedItem) >= 0) {
                            try {
                                container.add(concernedItem);
                                ItemFeedbackHelper.sendChatItemAddedMessage(concernedItem, 4);
                                if (!WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.ADD_SEEDS_TO_SHORTCUT_BAR)) {
                                    continue;
                                }
                                final AbstractReferenceItem referenceItem = concernedItem.getReferenceItem();
                                final ArrayList<ShortcutBar> itemsBars = WakfuGameEntity.getInstance().getLocalPlayer().getShortcutBarManager().getItemsBars(false);
                                boolean containsRef = false;
                                for (final ShortcutBar sb : itemsBars) {
                                    final ArrayList<ShortCutItem> allWithReferenceId = sb.getAllWithReferenceId(referenceItem.getId());
                                    for (final ShortCutItem sci : allWithReferenceId) {
                                        if (sci.getType() == ShortCutType.USABLE_REFERENCE_ITEM) {
                                            containsRef = true;
                                            break;
                                        }
                                    }
                                    if (containsRef) {
                                        break;
                                    }
                                }
                                if (containsRef) {
                                    continue;
                                }
                                final AbstractItemAction itemAction = referenceItem.getItemAction();
                                if (!(itemAction instanceof SeedItemAction)) {
                                    continue;
                                }
                                final UIShortcutMessage uiMessage2 = new UIShortcutMessage();
                                uiMessage2.setItem(container.getFirstWithReferenceId(referenceItem.getId()));
                                uiMessage2.setShorcutBarNumber(-1);
                                uiMessage2.setPosition(-1);
                                uiMessage2.setBooleanValue(false);
                                uiMessage2.setForce(true);
                                uiMessage2.setId(16700);
                                Worker.getInstance().pushMessage(uiMessage2);
                            }
                            catch (Exception e4) {
                                NetActorsFrame.m_logger.error((Object)"Exception", (Throwable)e4);
                            }
                        }
                    }
                    if (localPlayer.getCurrentOccupation() != null && localPlayer.getCurrentOccupation().getOccupationTypeId() == 3) {
                        localPlayer.finishCurrentOccupation();
                    }
                    return false;
                }
            }
            case 4200: {
                final ResourceSpawnMessage msg17 = (ResourceSpawnMessage)message;
                if (ResourceManager.getInstance().resourceAlreadyExist(msg17.getX(), msg17.getY())) {
                    NetActorsFrame.m_logger.error((Object)("La resource " + msg17.getTypeId() + " en " + msg17.getX() + ":" + msg17.getY() + ":" + msg17.getZ() + " existe d\u00ef¿½j\u00ef¿½ sur le client"));
                }
                else {
                    final ReferenceResource referenceResource = ReferenceResourceManager.getInstance().getReferenceResource(msg17.getTypeId());
                    Resource res;
                    if (referenceResource instanceof MonsterReferenceResource) {
                        res = MonsterResource.checkOut(msg17.getX(), msg17.getY(), msg17.getZ(), msg17.getTypeId(), msg17.getStep(), msg17.isJustGrow(), msg17.isAutoRespawn());
                    }
                    else {
                        res = Resource.checkOut(msg17.getX(), msg17.getY(), msg17.getZ(), msg17.getTypeId(), msg17.getStep(), msg17.isJustGrow(), msg17.isAutoRespawn());
                    }
                    if (res != null) {
                        ResourceManager.getInstance().addRessource(res);
                        res.fadeIfOnScreen();
                    }
                }
                return false;
            }
            case 4202: {
                final PartitionResourceInfoMessage msg18 = (PartitionResourceInfoMessage)message;
                for (final ResourceInfo resourceInfo : msg18.getResources()) {
                    if (ResourceManager.getInstance().resourceAlreadyExist(resourceInfo.getResourceX(), resourceInfo.getResourceY())) {
                        NetActorsFrame.m_logger.error((Object)("La resource " + resourceInfo.getRefId() + " en " + resourceInfo.getResourceX() + ":" + resourceInfo.getResourceY() + ":" + resourceInfo.getResourceZ() + " existe d\u00ef¿½j\u00ef¿½ sur le client"));
                    }
                    else {
                        final ReferenceResource referenceResource2 = ReferenceResourceManager.getInstance().getReferenceResource(resourceInfo.getRefId());
                        Resource res2;
                        if (referenceResource2 instanceof MonsterReferenceResource) {
                            res2 = MonsterResource.checkOut(resourceInfo.getResourceX(), resourceInfo.getResourceY(), resourceInfo.getResourceZ(), resourceInfo.getRefId(), resourceInfo.getEvolutionStep(), resourceInfo.isJustGrowth(), resourceInfo.isAutoRespawn());
                        }
                        else {
                            res2 = Resource.checkOut(resourceInfo.getResourceX(), resourceInfo.getResourceY(), resourceInfo.getResourceZ(), resourceInfo.getRefId(), resourceInfo.getEvolutionStep(), resourceInfo.isJustGrowth(), resourceInfo.isAutoRespawn());
                        }
                        if (res2 == null) {
                            continue;
                        }
                        ResourceManager.getInstance().addRessource(res2);
                        res2.fadeIfOnScreen();
                    }
                }
                return false;
            }
            case 4201: {
                final ResourceModificationMessage msg19 = (ResourceModificationMessage)message;
                final int x2 = msg19.getX();
                final int y2 = msg19.getY();
                final short stateId = msg19.getStateId();
                final int apsId = msg19.getApsId();
                final Resource resource = ResourceManager.getInstance().getResource(x2, y2);
                if (resource != null) {
                    resource.setEvolutionStep((byte)stateId, true);
                    if (apsId > 0) {
                        final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsId);
                        system.setPosition(resource.getWorldCellX(), resource.getWorldCellY(), resource.getWorldCellAltitude());
                        IsoParticleSystemManager.getInstance().addParticleSystem(system);
                    }
                }
                return false;
            }
            case 4142: {
                final ActorPlantResultMessage msg20 = (ActorPlantResultMessage)message;
                if (!msg20.isSuccess()) {
                    if (!UISeedInteractionFrame.getInstance().isNoFeedbackMode()) {
                        ErrorsMessageTranslator.getInstance().pushMessage(13, 4, new Object[0]);
                    }
                }
                else if (WakfuGameEntity.getInstance().hasFrame(UISeedInteractionFrame.getInstance())) {
                    UISeedInteractionFrame.getInstance().onSeedSucceed();
                }
                UISeedInteractionFrame.getInstance().executeNextSeedAction();
                ItemFeedbackHelper.sendChatItemRemovedMessage(UISeedInteractionFrame.getInstance().getItem(), (short)1);
                return false;
            }
            case 4180: {
                final ActorSearchTreasureResultMessage msg21 = (ActorSearchTreasureResultMessage)message;
                if (!msg21.isSuccess()) {
                    if (!UISearchTreasureInteractionFrame.INSTANCE.isNoFeedbackMode()) {
                        ErrorsMessageTranslator.getInstance().pushMessage(38, 3, new Object[0]);
                    }
                }
                else if (WakfuGameEntity.getInstance().hasFrame(UISearchTreasureInteractionFrame.INSTANCE)) {
                    UISearchTreasureInteractionFrame.INSTANCE.onSeedSucceed();
                    ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("searchTreasureSuccess"), 4);
                }
                final Item item2 = UISearchTreasureInteractionFrame.INSTANCE.getItem();
                if (item2 == null) {
                    NetActorsFrame.m_logger.error((Object)"[SEARCH_TREASURE] On creuse sans item !?");
                }
                else {
                    final AbstractItemAction itemAction2 = item2.getReferenceItem().getItemAction();
                    if (itemAction2 != null && itemAction2.isMustConsumeItem()) {
                        ItemFeedbackHelper.sendChatItemRemovedMessage(item2, (short)1);
                    }
                }
                UISearchTreasureInteractionFrame.INSTANCE.executeNextSeedAction();
                return false;
            }
            case 9508: {
                final SearchTreasureResultMessage msg22 = (SearchTreasureResultMessage)message;
                final int errorId = msg22.getErrorId();
                ErrorsMessageTranslator.getInstance().pushMessage(errorId, 3, new Object[0]);
                if (ClientEventErrorMessageReceived.allow(errorId)) {
                    ClientGameEventManager.INSTANCE.fireEvent(new ClientEventErrorMessageReceived(errorId));
                }
                UISearchTreasureInteractionFrame.INSTANCE.onErrorRecieved(errorId);
                return false;
            }
            case 9506: {
                final ResourceResultMessage msg23 = (ResourceResultMessage)message;
                final int errorId = msg23.getErrorId();
                ErrorsMessageTranslator.getInstance().pushMessage(errorId, 3, new Object[0]);
                if (ClientEventErrorMessageReceived.allow(errorId)) {
                    ClientGameEventManager.INSTANCE.fireEvent(new ClientEventErrorMessageReceived(errorId));
                }
                UISeedInteractionFrame.getInstance().onErrorRecieved(errorId);
                return false;
            }
            case 5206: {
                final ActorEquipmentUpdateMessage msg24 = (ActorEquipmentUpdateMessage)message;
                final CharacterInfo character5 = CharacterInfoManager.getInstance().getCharacter(msg24.getActorId());
                if (character5 == null) {
                    return false;
                }
                final byte[] updatedItems = msg24.getUpdatedItems().keys();
                for (byte k = 0; k < updatedItems.length; ++k) {
                    final byte updatedItem = updatedItems[k];
                    if (msg24.getUpdatedItems().get(updatedItem) == -1) {
                        character5.getEquipmentAppearance().remove(updatedItem);
                    }
                    else {
                        final int itemId = msg24.getUpdatedItems().get(updatedItem);
                        character5.getEquipmentAppearance().put(updatedItem, itemId);
                    }
                }
                character5.reloadItemEffects();
                character5.refreshDisplayEquipment();
                return false;
            }
            case 11106: {
                final ActorEquipmentPositionCleared msg25 = (ActorEquipmentPositionCleared)message;
                final Mobile mobile = MobileManager.getInstance().getMobile(msg25.getPlayerId());
                if (mobile instanceof CharacterActor) {
                    for (int l = 0; l < msg25.getPositions().size(); ++l) {
                        final short pos = msg25.getPositions().get(l);
                        ((CharacterActor)mobile).unapplyEquipment(pos);
                    }
                }
                return false;
            }
            case 10002: {
                final DimensionalBagDespawnMessage msg26 = (DimensionalBagDespawnMessage)message;
                DimensionalBagManager.getInstance().remove(msg26.getOwnerId());
                return false;
            }
            case 4125: {
                final CharacterEnterPartitionMessage msg27 = (CharacterEnterPartitionMessage)message;
                LocalPartitionManager.getInstance().setCenterPartition(msg27.getWorldX(), msg27.getWorldY());
                return false;
            }
            case 4108: {
                final MonsterBehaviourMessage msg28 = (MonsterBehaviourMessage)message;
                final long npcId = msg28.getCharacterId();
                final long targetId = msg28.getTargetId();
                final MonsterBehaviour behaviour = MonsterBehaviourManager.getInstance().getBehaviour(msg28.getBehaviourId());
                if (behaviour != null) {
                    final NonPlayerCharacter character6 = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(npcId);
                    final CharacterInfo target = CharacterInfoManager.getInstance().getCharacter(targetId);
                    MonsterBehaviourAction behaviourAction;
                    if (target != null) {
                        behaviourAction = new MonsterBehaviourAction(TimedAction.getNextUid(), 0, 0, behaviour.getScriptId(), character6, target);
                    }
                    else {
                        behaviourAction = new MonsterBehaviourAction(TimedAction.getNextUid(), 0, 0, behaviour.getScriptId(), character6, targetId);
                    }
                    if (character6 != null) {
                        if (character6.getCurrentBehaviourAction() != null) {
                            character6.getCurrentBehaviourAction().forceActionEnd();
                        }
                        character6.setCurrentBehaviourAction(behaviourAction);
                        final CharacterActor characterActor = character6.getActor();
                        if (characterActor.getCurrentPath() != null && behaviour.needsToWaitPathEnd()) {
                            characterActor.addEndPositionListener(new MobileEndPathListener() {
                                @Override
                                public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                                    mobile.removeEndPositionListener(this);
                                    MonsterActionGroupManager.INSTANCE.addAction(behaviourAction);
                                    MonsterActionGroupManager.INSTANCE.executeAllAction();
                                }
                            });
                        }
                        else {
                            MonsterActionGroupManager.INSTANCE.addAction(behaviourAction);
                            MonsterActionGroupManager.INSTANCE.executeAllAction();
                        }
                    }
                }
                else {
                    NetActorsFrame.m_logger.error((Object)("R\u00ef¿½ception d'un MonsterBehaviour inconnu id=" + msg28.getBehaviourId()));
                }
                return false;
            }
            case 4530: {
                final MonsterActionMessage msg29 = (MonsterActionMessage)message;
                final long npcId = msg29.getNpcId();
                final long playerId = msg29.getPlayerId();
                final long actionId = msg29.getActionId();
                final NonPlayerCharacter npc = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(npcId);
                if (npc == null) {
                    NetActorsFrame.m_logger.warn((Object)("Reception d'un messsage d'action pour le monstre " + npcId + " alors qu'on ne le connait pas"));
                    return false;
                }
                final PlayerCharacter player = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(playerId);
                if (player == null) {
                    NetActorsFrame.m_logger.warn((Object)("Reception d'un messsage d'action pour le monstre " + npcId + "\tiniti\u00ef¿½ par le joueur " + playerId + " alors qu'on ne le connait pas"));
                    return false;
                }
                final AbstractClientMonsterAction action = npc.getBreed().getAction(actionId);
                if (action == null) {
                    NetActorsFrame.m_logger.error((Object)("Reception d'un messsage d'action " + actionId + " inconnu pour le monstre " + npcId));
                    return false;
                }
                final ActionVisual visual = action.getVisual();
                ActionVisualHelper.applyActionVisual(player.getActor(), visual);
                final MonsterActionAction scriptedAction = new MonsterActionAction(TimedAction.getNextUid(), 0, 0, action.getScriptId(), npc, player);
                MonsterActionGroupManager.INSTANCE.addAction(scriptedAction);
                MonsterActionGroupManager.INSTANCE.executeAllAction();
                return false;
            }
            case 4110: {
                final MonsterEvolutionMessage msg30 = (MonsterEvolutionMessage)message;
                final NonPlayerCharacter npc2 = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg30.getCharacterId());
                final MonsterEvolutionStep step = MonsterEvolutionStepManager.getInstance().getEvolutionStep(msg30.getEvolutionStepId());
                final MRU currentMRU = UIMRUFrame.getInstance().getCurrentMRU();
                if (currentMRU != null) {
                    for (int m = 0, size2 = currentMRU.getSourcesCount(); m < size2; ++m) {
                        final MRUable source2 = currentMRU.getSource(m);
                        if (source2 instanceof NonPlayerCharacter && ((NonPlayerCharacter)source2).getId() == npc2.getId()) {
                            UIMRUFrame.getInstance().closeCurrentMRU();
                            break;
                        }
                    }
                }
                final MonsterEvolutionAction action2 = new MonsterEvolutionAction(TimedAction.getNextUid(), 0, 0, step.getScriptId(), msg30.getCharacterId(), step.getEvolvedBreedId(), msg30.getNewLevel());
                MonsterActionGroupManager.INSTANCE.addAction(action2);
                MonsterActionGroupManager.INSTANCE.executeAllAction();
                return false;
            }
            case 4526: {
                final MonsterCollectNotificationMessage msg31 = (MonsterCollectNotificationMessage)message;
                final NonPlayerCharacter npc2 = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg31.getCharacterId());
                if (npc2 != null) {
                    npc2.setCollectAvailable(msg31.getActionId(), msg31.isCollectAvailable());
                }
                return false;
            }
            case 4134: {
                final MonsterGroupUpdateMessage msg32 = (MonsterGroupUpdateMessage)message;
                final long newGroupId = msg32.getNewGroupId();
                final long character7 = msg32.getCharacter();
                final NonPlayerCharacter npc3 = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(character7);
                if (npc3 != null) {
                    NPCGroupInformationManager.getInstance().onNonPlayerCharacterRemoved(npc3, true);
                    npc3.setGroupId(newGroupId);
                    NPCGroupInformationManager.getInstance().updateGroupInformationFromNPC(npc3, Collections.EMPTY_LIST);
                }
                return false;
            }
            case 4112: {
                final MonsterStateMessage msg33 = (MonsterStateMessage)message;
                final NonPlayerCharacter npc2 = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg33.getCharacterId());
                if (npc2 == null) {
                    NetActorsFrame.m_logger.error((Object)"Tentative de modification des \u00ef¿½tats d'un NPC qui n'existe pas");
                    return false;
                }
                byte stateId2 = msg33.getStateId();
                final boolean remove = stateId2 < 0;
                if (remove) {
                    stateId2 = (byte)Math.abs(stateId2);
                }
                final WorldPropertyType state = WorldPropertyType.getPropertyFromId(stateId2);
                try {
                    if (remove) {
                        MonsterTimedPropertyManager.getInstance().removeProperty(npc2, state);
                    }
                    else {
                        if (state == WorldPropertyType.DEAD && !npc2.isDead() && !npc2.isOnFight()) {
                            npc2.onNPCDeath();
                        }
                        MonsterTimedPropertyManager.getInstance().addPropertyAndUpdate(npc2, state);
                    }
                }
                catch (NullPointerException ex) {}
                return false;
            }
            case 9200: {
                final ActorPlayAnimationMessage msg34 = (ActorPlayAnimationMessage)message;
                final long characterId2 = msg34.getCharacterId();
                final String linkAnimation = msg34.getLinkAnimation();
                final CharacterInfo character8 = CharacterInfoManager.getInstance().getCharacter(characterId2);
                if (character8 != null && linkAnimation != null) {
                    character8.getActor().setAnimation(linkAnimation);
                }
                return false;
            }
            case 4220: {
                final OtherPlayerLevelUpMessage msg35 = (OtherPlayerLevelUpMessage)message;
                final long characterId2 = msg35.getPlayerId();
                final long playerXp = msg35.getPlayerXp();
                if (localPlayer.getId() == characterId2) {
                    return false;
                }
                final PlayerCharacter character9 = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(characterId2);
                if (character9 != null) {
                    character9.setXp(playerXp);
                }
                return false;
            }
            case 9202: {
                final ActorChangeStaticAnimationKeyMessage msg36 = (ActorChangeStaticAnimationKeyMessage)message;
                final long characterId2 = msg36.getCharacterId();
                final String linkAnimation = msg36.getLinkAnimation();
                final CharacterInfo character8 = CharacterInfoManager.getInstance().getCharacter(characterId2);
                if (character8 != null && linkAnimation != null) {
                    character8.getActor().setStaticAnimationKey(linkAnimation);
                }
                return false;
            }
            case 9201: {
                final ActorPlayApsMessage msg37 = (ActorPlayApsMessage)message;
                final long characterId2 = msg37.getCharacterId();
                final int apsId2 = msg37.getApsId();
                final int duree = msg37.getDuree();
                final CharacterInfo character10 = CharacterInfoManager.getInstance().getCharacter(characterId2);
                if (character10 != null && apsId2 != 0 && duree != 0) {
                    final FreeParticleSystem system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(apsId2);
                    final int systemId = system.getId();
                    if (msg37.isFollow()) {
                        final IsoWorldTarget target2 = character10.getActor();
                        if (target2 != null) {
                            system.setTarget(target2);
                        }
                    }
                    else {
                        system.setWorldPosition(character10.getWorldCellX(), character10.getWorldCellY(), character10.getWorldCellAltitude());
                    }
                    IsoParticleSystemManager.getInstance().addParticleSystem(system);
                    ProcessScheduler.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                            IsoParticleSystemManager.getInstance().removeParticleSystem(systemId);
                        }
                    }, duree * 1000, 1);
                }
                return false;
            }
            case 15402: {
                final ActorPlayEmoteAnswerMessage msg38 = (ActorPlayEmoteAnswerMessage)message;
                final long characterId2 = msg38.getCharacterId();
                final int emoteId2 = msg38.getEmoteId();
                final HashMap<String, Object> vars = msg38.getVariables();
                final CharacterInfo character10 = CharacterInfoManager.getInstance().getCharacter(characterId2);
                if (character10 != null && emoteId2 != 0) {
                    character10.playEmote(emoteId2, vars, true);
                }
                return false;
            }
            case 5504: {
                final PlayerTitleChangedMessage msg39 = (PlayerTitleChangedMessage)message;
                final PlayerCharacter pc = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg39.getPlayerId());
                if (pc != null) {
                    pc.setCurrentTitle(msg39.getCurrentTitle());
                }
                return false;
            }
            case 5506: {
                final PlayerTitleListMessage msg40 = (PlayerTitleListMessage)message;
                WakfuGameEntity.getInstance().getLocalPlayer().setAvailableTitles(msg40.getAvailableTitles());
                return false;
            }
            case 5302: {
                throw new UnsupportedOperationException("On ne devrait plus passer par l\u00ef¿½ mais par la GameActionPlayScript");
            }
            case 15206: {
                final BoatEventMessage msg41 = (BoatEventMessage)message;
                NetActorsFrame.m_logger.info((Object)("[TRANSPORT] Message d'evenement de transport : " + msg41.getType()));
                final TicketCollector collector = (TicketCollector)LocalPartitionManager.getInstance().getInteractiveElement(msg41.getCollectorId());
                switch (msg41.getType()) {
                    case ARRIVAL: {
                        collector.setLocked(false);
                        break;
                    }
                    case DEPARTURE: {
                        collector.setLocked(true);
                        break;
                    }
                    case COLLECTOR_SUCCESS: {
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("boat.startTravel"), 4);
                        return false;
                    }
                    case COLLECTOR_ERROR: {
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("boat.cantTravel"), 3);
                        return false;
                    }
                    case BOAT_FULL: {
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("boat.full"), 3);
                        return false;
                    }
                }
                final ArrayList<ClientMapInteractiveElement> elements = LocalPartitionManager.getInstance().getInteractiveElementsFromPartitions();
                for (int i2 = 0, size3 = elements.size(); i2 < size3; ++i2) {
                    final ClientMapInteractiveElement clientMapInteractiveElement = elements.get(i2);
                    if (clientMapInteractiveElement instanceof BoatBoard) {
                        final BoatBoard board = (BoatBoard)clientMapInteractiveElement;
                        if (board.getCollectorId() == msg41.getCollectorId()) {
                            switch (msg41.getType()) {
                                case ARRIVAL: {
                                    board.updateNextArrival(collector.getBoatFrequency());
                                    board.setBoatDocked(true);
                                    break;
                                }
                                case DEPARTURE: {
                                    board.undateNextDeparture(collector.getBoatFrequency());
                                    board.setBoatDocked(false);
                                    break;
                                }
                            }
                        }
                    }
                }
                return false;
            }
            case 15200: {
                final AddZoneBuffMessage msg42 = (AddZoneBuffMessage)message;
                final ZoneBuff zoneBuff = ZoneBuffManager.getInstance().getZoneBuff(msg42.getBuffId());
                if (zoneBuff != null) {
                    NetActorsFrame.m_logger.info((Object)("Buff de zone id=" + msg42.getBuffId() + " appliqu\u00ef¿½ pour " + msg42.getBuffRemainingTime() + " ms"));
                    localPlayer.getZoneBuffManager().addBuff(new ZoneBuffInstance(zoneBuff, msg42.getBuffRemainingTime()));
                    localPlayer.reloadZoneEffects();
                }
                else {
                    NetActorsFrame.m_logger.error((Object)("Buff de zone id=" + msg42.getBuffId() + " inconnu"));
                }
                return false;
            }
            case 15202: {
                final RemoveZoneBuffMessage msg43 = (RemoveZoneBuffMessage)message;
                boolean removed = false;
                for (final ZoneBuffInstance buffInstance : localPlayer.getActiveZoneBuffs()) {
                    if (buffInstance.getBuffId() == msg43.getBuffId()) {
                        NetActorsFrame.m_logger.info((Object)("Buff de zone id=" + msg43.getBuffId() + " retir\u00ef¿½"));
                        localPlayer.getZoneBuffManager().removeBuff(buffInstance);
                        localPlayer.reloadZoneEffects();
                        removed = true;
                        break;
                    }
                }
                if (!removed) {
                    NetActorsFrame.m_logger.error((Object)("Buff de zone id=" + msg43.getBuffId() + " inconnu"));
                }
                return false;
            }
            case 15204: {
                final SetZoneBuffsMessage msg44 = (SetZoneBuffsMessage)message;
                localPlayer.getZoneBuffManager().clearBuffs();
                for (int i3 = 0; i3 < msg44.getBuffCount(); ++i3) {
                    final ZoneBuff zoneBuff2 = ZoneBuffManager.getInstance().getZoneBuff(msg44.getBuffId(i3));
                    if (zoneBuff2 != null) {
                        NetActorsFrame.m_logger.info((Object)("Buff de zone id=" + msg44.getBuffId(i3) + " appliqu\u00ef¿½ pour " + msg44.getBuffRemainingTime(i3) + " ms"));
                        localPlayer.getZoneBuffManager().addBuff(new ZoneBuffInstance(zoneBuff2, msg44.getBuffRemainingTime(i3)));
                    }
                    else {
                        NetActorsFrame.m_logger.error((Object)("Buff de zone id=" + msg44.getBuffId(i3) + " inconnu"));
                    }
                }
                localPlayer.reloadZoneEffects();
                return false;
            }
            case 4184: {
                final ActorRecycleResultMessage msg45 = (ActorRecycleResultMessage)message;
                final float depollutionFactor = msg45.getDepollutionFactor();
                final TLongShortHashMap recycledItems = msg45.getRecycledItems();
                final TLongObjectHashMap<CrystalItem> crystalItems = msg45.getCrystalItems();
                final AbstractReferenceItem referenceItem2 = ReferenceItemManager.getInstance().getReferenceItem(4620);
                recycledItems.forEachEntry(new TLongShortProcedure() {
                    @Override
                    public boolean execute(final long itemUID, final short quantity) {
                        final AbstractBag source = localPlayer.getBags().getFirstContainerWith(itemUID);
                        if (source != null) {
                            final Item item = source.getWithUniqueId(itemUID);
                            if (item != null && item.getQuantity() > quantity) {
                                source.updateQuantity(itemUID, (short)(-quantity));
                            }
                            else {
                                source.removeWithUniqueId(itemUID);
                            }
                        }
                        return true;
                    }
                });
                crystalItems.forEachEntry(new TLongObjectProcedure<CrystalItem>() {
                    @Override
                    public boolean execute(final long id, final CrystalItem ci) {
                        final AbstractBag source = localPlayer.getBags().get(ci.getBagId());
                        if (source != null) {
                            final Item crystal = Item.newInstance(referenceItem2);
                            crystal.setQuantity(ci.getQuantity());
                            try {
                                final short position = ci.getPosition();
                                if (position != -1) {
                                    source.addAt(crystal, position);
                                }
                                else {
                                    source.add(crystal);
                                }
                            }
                            catch (InventoryCapacityReachedException e) {
                                crystal.release();
                                NetActorsFrame.m_logger.error((Object)"Exception", (Throwable)e);
                            }
                            catch (ContentAlreadyPresentException e2) {
                                crystal.release();
                                NetActorsFrame.m_logger.error((Object)"Exception", (Throwable)e2);
                            }
                            catch (PositionAlreadyUsedException e3) {
                                crystal.release();
                                NetActorsFrame.m_logger.error((Object)"Exception", (Throwable)e3);
                            }
                        }
                        return false;
                    }
                });
                WakfuGameEntity.getInstance().removeFrame(UIRecycleFrame.getInstance());
                return false;
            }
            case 12602: {
                this.wakfuGaugeModificationMessage(message);
                return false;
            }
            case 8408: {
                final ActorLevelUpAptitudeResultMessage msg46 = (ActorLevelUpAptitudeResultMessage)message;
                final TShortShortHashMap results = msg46.getResults();
                final TShortShortIterator it4 = results.iterator();
                while (it4.hasNext()) {
                    it4.advance();
                    for (int i2 = 0, size3 = it4.value(); i2 < size3; ++i2) {
                        WakfuGameEntity.getInstance().getLocalPlayer().levelUpAptitude(it4.key(), msg46.isConsumePoints());
                    }
                }
                return false;
            }
            case 8417: {
                final LevelUpNewAptitudeResultMessage msg47 = (LevelUpNewAptitudeResultMessage)message;
                final TIntShortHashMap results2 = msg47.getResults();
                results2.forEachEntry(new TIntShortProcedure() {
                    @Override
                    public boolean execute(final int bonusId, final short level) {
                        localPlayer.getAptitudeBonusInventory().addLevel(bonusId, level);
                        localPlayer.getAptitudeBonusInventory().removePointsFor(bonusId, level);
                        return true;
                    }
                });
                localPlayer.reloadNewAptitudeEffects(localPlayer.getOwnContext());
                AptitudesView.INSTANCE.resetChanges();
                return false;
            }
            case 15602: {
                final AchievementCompleteMessage msg48 = (AchievementCompleteMessage)message;
                final long characterId2 = msg48.getCharacterId();
                final int achievementId = msg48.getAchievementId();
                final long unlockTime = msg48.getUnlockTime();
                final Fight fight = localPlayer.getCurrentOrObservedFight();
                if (fight != null) {
                    FightActionGroupManager.getInstance().addActionToPendingGroup(localPlayer.getCurrentOrObservedFightId(), new GenericAction(new Runnable() {
                        @Override
                        public void run() {
                            NetActorsFrame.achievementComplete(characterId2, achievementId, unlockTime);
                        }
                    }));
                    FightActionGroupManager.getInstance().executePendingGroup(fight);
                }
                else {
                    achievementComplete(characterId2, achievementId, unlockTime);
                }
                return false;
            }
            case 15600: {
                final AchievementObjectiveCompleteMessage msg49 = (AchievementObjectiveCompleteMessage)message;
                final long characterId2 = msg49.getCharacterId();
                final int objectiveId = msg49.getObjectiveId();
                final Fight fight2 = localPlayer.getCurrentOrObservedFight();
                if (localPlayer.getCurrentOrObservedFight() != null) {
                    FightActionGroupManager.getInstance().addActionToPendingGroup(fight2, new GenericAction(new Runnable() {
                        @Override
                        public void run() {
                            NetActorsFrame.achievementObjectiveComplete(characterId2, objectiveId);
                        }
                    }));
                    FightActionGroupManager.getInstance().executePendingGroup(fight2);
                }
                else {
                    achievementObjectiveComplete(characterId2, objectiveId);
                }
                return false;
            }
            case 15604: {
                final AchievementVariableUpdateMessage msg50 = (AchievementVariableUpdateMessage)message;
                final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(msg50.getCharacterId());
                if (context != null) {
                    context.updateVariable(msg50.getVariableId(), msg50.getVariableValue());
                }
                return false;
            }
            case 15606: {
                final AchievementActivatedMessage msg51 = (AchievementActivatedMessage)message;
                final Fight fight3 = localPlayer.getCurrentOrObservedFight();
                if (localPlayer.getCurrentOrObservedFight() != null) {
                    FightActionGroupManager.getInstance().addActionToPendingGroup(fight3, new GenericAction(new Runnable() {
                        @Override
                        public void run() {
                            NetActorsFrame.achievementActivated(msg51.getCharacterId(), msg51.getAchievementId());
                        }
                    }));
                    FightActionGroupManager.getInstance().executePendingGroup(fight3);
                }
                else {
                    achievementActivated(msg51.getCharacterId(), msg51.getAchievementId());
                }
                return false;
            }
            case 15608: {
                final AchievementFollowedMessage msg52 = (AchievementFollowedMessage)message;
                final Fight fight3 = localPlayer.getCurrentOrObservedFight();
                if (localPlayer.getCurrentOrObservedFight() != null) {
                    FightActionGroupManager.getInstance().addActionToPendingGroup(fight3, new GenericAction(new Runnable() {
                        @Override
                        public void run() {
                            NetActorsFrame.achievementFollowed(msg52.getCharacterId(), msg52.getAchievementId(), msg52.isFollowed());
                        }
                    }));
                    FightActionGroupManager.getInstance().executePendingGroup(fight3);
                }
                else {
                    achievementFollowed(msg52.getCharacterId(), msg52.getAchievementId(), msg52.isFollowed());
                }
                return false;
            }
            case 15612: {
                final AchievementActivationRequestMessage msg53 = (AchievementActivationRequestMessage)message;
                final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(msg53.getCharacterId());
                if (context != null) {
                    context.requestAchievementActivation(msg53.getAchievementId(), msg53.getInviterId());
                }
                return false;
            }
            case 15614: {
                final AchievementFailedMessage msg54 = (AchievementFailedMessage)message;
                final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(msg54.getCharacterId());
                if (context != null) {
                    context.failAchievement(msg54.getAchievementId());
                }
                return false;
            }
            case 15618: {
                final AchievementSharedFeedbackMessage msg55 = (AchievementSharedFeedbackMessage)message;
                final PartyModelInterface party = localPlayer.getPartyComportment().getParty();
                final PartyMemberInterface member = party.getMember(msg55.getPlayerId());
                if (member == null) {
                    return false;
                }
                final String achievementName = WakfuTranslator.getInstance().getString(62, msg55.getAchievementId(), new Object[0]);
                String chatMsg;
                if (msg55.isAccept()) {
                    chatMsg = WakfuTranslator.getInstance().getString("chat.notify.questShareAccepted", member.getName(), achievementName);
                }
                else {
                    chatMsg = WakfuTranslator.getInstance().getString("chat.notify.questShareRefused", member.getName(), achievementName);
                }
                ChatManager.getInstance().pushMessage(chatMsg, 4);
                return false;
            }
            case 15616: {
                final AchievementSharedAnswerMessage msg56 = (AchievementSharedAnswerMessage)message;
                final TLongArrayList sharedWith = msg56.getSharedWith();
                String chatMsg2;
                if (sharedWith.isEmpty()) {
                    chatMsg2 = WakfuTranslator.getInstance().getString("chat.notify.questSharedWithNobody");
                }
                else {
                    final TextWidgetFormater sb2 = new TextWidgetFormater();
                    final PartyModelInterface party2 = localPlayer.getPartyComportment().getParty();
                    for (int i4 = 0, size4 = sharedWith.size(); i4 < size4; ++i4) {
                        if (i4 != 0) {
                            sb2.append(", ");
                        }
                        final PartyMemberInterface member2 = party2.getMember(sharedWith.get(i4));
                        sb2.append(member2.getName());
                    }
                    chatMsg2 = WakfuTranslator.getInstance().getString("chat.notify.questSharedWith", sb2.finishAndToString());
                }
                ChatManager.getInstance().pushMessage(chatMsg2, 4);
                return false;
            }
            case 15610: {
                final AchievementResetMessage msg57 = (AchievementResetMessage)message;
                final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(msg57.getCharacterId());
                if (context != null) {
                    context.reset(msg57.getAchievementId());
                }
                return false;
            }
            case 5502: {
                final PlayerTitleUnlockedMessage msg58 = (PlayerTitleUnlockedMessage)message;
                final int titleId = msg58.getTitleId();
                if (HeroesManager.INSTANCE.getHero(msg58.getCharacterId()).addAvailableTitle(titleId)) {
                    final String title = WakfuTranslator.getInstance().getString(34, titleId, new Object[0]);
                    final String chatMsg3 = WakfuTranslator.getInstance().getString("chat.notify.titleUnlocked", title);
                    ChatManager.getInstance().pushMessage(chatMsg3, 4);
                    ClientGameEventManager.INSTANCE.fireEvent(new ClientEventTitleUnlocked(titleId));
                }
                else {
                    NetActorsFrame.m_logger.error((Object)("R\u00ef¿½ception d'un d\u00ef¿½blocage de titre id=" + titleId + " qui n'a pas pu \u00ef¿½tre trait\u00ef¿½ correctement dans le client"));
                }
                return false;
            }
            case 15726: {
                final DimensionalBagViewLearnedMessage msg59 = (DimensionalBagViewLearnedMessage)message;
                final int viewId = msg59.getViewId();
                if (localPlayer.getPersonalSpaceHandler().learnView(viewId)) {
                    final String chatMsg2 = WakfuTranslator.getInstance().getString("chat.notify.dimensionalBagViewUnlocked");
                    ChatManager.getInstance().pushMessage(chatMsg2, 4);
                }
                return false;
            }
            case 15727: {
                final TravelDiscoveredMessage msg60 = (TravelDiscoveredMessage)message;
                this.onTravelDiscovered(localPlayer, msg60);
                return false;
            }
            case 8414: {
                final ActorAptitudePointGainMessage msg61 = (ActorAptitudePointGainMessage)message;
                this.onAptitudePointGainMessage(msg61);
                return false;
            }
            case 14011: {
                final KrosmozFigureListResultMessage msg62 = (KrosmozFigureListResultMessage)message;
                KrosmozCollectionView.INSTANCE.setFigures(msg62.getFigures());
                return false;
            }
            case 14013: {
                final KrosmozFigureAddResultMessage msg63 = (KrosmozFigureAddResultMessage)message;
                if (msg63.isOk()) {
                    KrosmozCollectionView.INSTANCE.onFigureAdded(msg63.getFigure());
                }
                return false;
            }
            case 14015: {
                final KrosmozFigureDeleteResultMessage msg64 = (KrosmozFigureDeleteResultMessage)message;
                if (msg64.isOk()) {
                    KrosmozCollectionView.INSTANCE.onFigureRemoved(msg64.getFigure());
                }
                return false;
            }
            case 15500: {
                final LockActivatedMessage msg65 = (LockActivatedMessage)message;
                WakfuGameEntity.getInstance().getLocalPlayer().getLockContext().setLockDate(msg65.getLockId(), msg65.getLockDate());
                return false;
            }
            case 15502: {
                final LockIncrementedMessage msg66 = (LockIncrementedMessage)message;
                WakfuGameEntity.getInstance().getLocalPlayer().getLockContext().setCurrentLockValue(msg66.getLockId(), msg66.getCurrentLockValue());
                return false;
            }
            case 15510: {
                final FightChallengesActivatedMessage msg67 = (FightChallengesActivatedMessage)message;
                final TIntArrayList challenges = msg67.getChallenges();
                final TIntArrayList dropLevels = msg67.getDropLevel();
                final TIntArrayList xpLevels = msg67.getXpLevel();
                final Fight fight2 = localPlayer.getCurrentOrObservedFight();
                if (fight2 != null) {
                    FightActionGroupManager.getInstance().addActionToPendingGroup(localPlayer.getCurrentOrObservedFightId(), new GenericAction(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0, size = challenges.size(); i < size; ++i) {
                                final int id = challenges.get(i);
                                final int dropLevel = dropLevels.get(i);
                                final int xpLevel = xpLevels.get(i);
                                final FightChallenge challenge = FightChallengeManager.INSTANCE.getChallenge(id);
                                if (challenge != null) {
                                    WakfuGameEntity.getInstance().getLocalPlayer().getFightChallengesContext().addChallenge(challenge, dropLevel, xpLevel);
                                }
                            }
                        }
                    }));
                    FightActionGroupManager.getInstance().executePendingGroup(fight2);
                }
                return false;
            }
            case 15512: {
                final FightChallengeEndedMessage msg68 = (FightChallengeEndedMessage)message;
                final Fight fight3 = localPlayer.getCurrentOrObservedFight();
                if (fight3 != null) {
                    FightActionGroupManager.getInstance().addActionToPendingGroup(localPlayer.getCurrentOrObservedFightId(), new GenericAction(new Runnable() {
                        @Override
                        public void run() {
                            WakfuGameEntity.getInstance().getLocalPlayer().getFightChallengesContext().setChallengeState(msg68.getChallengeId(), msg68.getState());
                        }
                    }));
                    FightActionGroupManager.getInstance().executePendingGroup(fight3);
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void onTravelDiscovered(final LocalPlayerCharacter localPlayer, final TravelDiscoveredMessage msg) {
        final TravelType travelType = msg.getTravelType();
        final TravelHandler travelHandler = localPlayer.getTravelHandler();
        switch (travelType) {
            case ZAAP: {
                travelHandler.addDiscoveredZaap(msg.getMachineId());
                break;
            }
            case DRAGO: {
                travelHandler.addDiscoveredDrago(msg.getMachineId());
                break;
            }
            case CANNON: {
                travelHandler.addDiscoveredCannon(msg.getMachineId());
                break;
            }
            case BOAT: {
                travelHandler.addDiscoveredBoat(msg.getMachineId());
            }
        }
    }
    
    private void moveCharacter(final CharacterInfo info, final int posX, final int posY, final short posZ, final boolean generateMove) {
        NetActorsFrame.m_logger.info((Object)("tp " + info.getId() + " to " + posX + ", " + posY));
        if (generateMove) {
            info.getActor().moveTo(posX, posY, posZ, false, false);
        }
        else {
            info.teleport(posX, posY, posZ, generateMove);
        }
    }
    
    public static void achievementActivated(final long characterId, final int achievementId) {
        final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(characterId);
        if (context != null) {
            context.activateAchievement(achievementId);
        }
    }
    
    public static void achievementFollowed(final long characterId, final int achievementId, final boolean followed) {
        final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(characterId);
        if (context != null) {
            context.setFollowed(achievementId, followed);
        }
    }
    
    public static void achievementObjectiveComplete(final long characterId, final int objectiveId) {
        final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(characterId);
        if (context != null) {
            context.completeObjective(objectiveId);
            final Objective obj = AchievementsModel.INSTANCE.getObjective(objectiveId);
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventAchievementObjectiveUnlocked(objectiveId, obj.getAchievement().getId()));
        }
    }
    
    public static void achievementComplete(final long characterId, final int achievementId, final long unlockTime) {
        final ClientAchievementsContext context = AchievementContextManager.INSTANCE.getContext(characterId);
        if (context != null) {
            context.completeAchievement(achievementId, unlockTime);
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventAchievementUnlocked(achievementId));
        }
    }
    
    private void onAptitudePointGainMessage(final ActorAptitudePointGainMessage msg) {
        final byte aptitudeTypeId = msg.getAptitudeType();
        final int points = msg.getPoints();
        final AptitudeType aptitudeType = AptitudeType.getFromId(aptitudeTypeId);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        localPlayer.getAptitudeInventory().setAvailablePoints(aptitudeType, localPlayer.getAptitudeInventory().getAvailablePoints(aptitudeType) + points);
        localPlayer.getAptitudeInventory().setWonPoints(aptitudeType, localPlayer.getAptitudeInventory().getWonPoints(aptitudeType) + points);
        PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer, "availableCommonPoints", "availableAptitudePoints", "hasAptitudePoints");
    }
    
    public void addCharacterToFight(final CharacterInfo character, final CharacterActor actor) {
        final FightInfo fight = FightManager.getInstance().getFightById(character.getCurrentFightId());
        if (fight == null || !(fight instanceof ExternalFightInfo)) {
            return;
        }
        final ExternalFightInfo externalFight = (ExternalFightInfo)fight;
        externalFight.spawnFighter(character);
        WeaponAnimHelper.prepareAnimForFight(character);
        if (character instanceof NonPlayerCharacter && (fight.getStatus() == AbstractFight.FightStatus.PLACEMENT || fight.getStatus() == AbstractFight.FightStatus.CREATION)) {
            actor.addCrossSwordParticleSystem((byte)(-1));
        }
        if (character.mustGoOffPlay()) {
            final ChangeActivityAction action = new ChangeActivityAction(TimedAction.getNextUid(), FightActionType.CHANGE_ACTIVITY.getId(), 0, (byte)0, fight.getId(), false);
            action.setTargetId(character.getId());
            FightActionGroupManager.getInstance().addActionToPendingGroup(fight.getId(), action);
            FightActionGroupManager.getInstance().executePendingGroup(fight);
        }
        if (character.mustGoOutOfPlay()) {
            final ChangeActivityAction action = new ChangeActivityAction(TimedAction.getNextUid(), FightActionType.CHANGE_ACTIVITY.getId(), 0, (byte)2, fight.getId(), false);
            action.setTargetId(character.getId());
            FightActionGroupManager.getInstance().addActionToPendingGroup(fight.getId(), action);
            FightActionGroupManager.getInstance().executePendingGroup(fight);
        }
    }
    
    private void wakfuGaugeModificationMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final PlayerWakfuGaugeModificationMessage msg = (PlayerWakfuGaugeModificationMessage)message;
        final long playerId = msg.getPlayerId();
        final int newWakfuGaugeValue = msg.getNewWakfuGaugeValue();
        final PlayerCharacter playerCharacter = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(playerId);
        if (playerCharacter == null) {
            NetActorsFrame.m_logger.error((Object)("R\u00ef¿½ception d'un update de jauge wakfu pour un joueur inconnu id=" + playerId));
            return;
        }
        if (playerCharacter.getCurrentFight() != null) {
            FightActionGroupManager.getInstance().addActionToPendingGroup(playerCharacter.getCurrentFightId(), new Action(TimedAction.getNextUid(), FightActionType.WAKFU_GAUGE_MODIFICATION.getId(), 0) {
                @Override
                public void run() {
                    playerCharacter.setWakfuGauge(newWakfuGaugeValue);
                    if (playerId == localPlayer.getId()) {
                        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventWakfuGaugeUpdate());
                    }
                    this.fireActionFinishedEvent();
                }
                
                @Override
                protected void onActionFinished() {
                }
            });
        }
        else {
            playerCharacter.setWakfuGauge(newWakfuGaugeValue);
            if (playerId == localPlayer.getId()) {
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventWakfuGaugeUpdate());
            }
        }
    }
    
    private void moveCharacterActor(final ActorMoveToMessage msg, final CharacterInfo info) {
        this.moveCharacterActor(info, new Point3(msg.getX(), msg.getY(), msg.getZ()), Direction8.getDirectionFromIndex(msg.getDirection()));
    }
    
    public void moveCharacterActor(final CharacterInfo info, final Point3 destination, final Direction8 direction) {
        final CharacterActor actor = info.getActor();
        if (!actor.moveTo(destination, false, actor.getAvailableDirections() == 8)) {
            actor.teleportTo(destination, true, false, actor.getAvailableDirections() == 8);
            actor.setDirection(direction);
        }
        actor.addEndPositionListener(new MobileEndPathListener() {
            @Override
            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                mobile.setDirection(direction);
                mobile.removeEndPositionListener(this);
            }
        });
        if (NetActorsFrame.m_logger.isTraceEnabled() && info instanceof PlayerCharacter) {
            NetActorsFrame.m_logger.trace((Object)String.format("Update de chemin re\u00ef¿½ue pour le personnage %s(%d) : currentPos=[%d:%d] to=%s", info.getName(), info.getId(), actor.getCurrentWorldX(), actor.getCurrentWorldY(), destination));
        }
    }
    
    public void despawnActor(final long actorId, final byte type, final boolean fightDespawn, final boolean applyAPS) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer != null && actorId == localPlayer.getId()) {
            return;
        }
        try {
            Label_0104: {
                switch (type) {
                    case 0: {
                        final PlayerCharacter pc = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(actorId);
                        if (pc == null) {
                            break Label_0104;
                        }
                        final CitizenComportment comportment = pc.getCitizenComportment();
                        if (comportment != null) {
                            comportment.getNation().onMemberDisconnection(pc.getId());
                        }
                        break Label_0104;
                    }
                    case 1:
                    case 4: {
                        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(actorId);
                        if (character == null) {
                            return;
                        }
                        if (fightDespawn) {
                            if (!character.isSpawnInMyFight()) {
                                NetActorsFrame.m_logger.info((Object)("Despawn de " + actorId + " de myFight alors qu'il ne l'\u00ef¿½tait pas => ignor\u00ef¿½"));
                                return;
                            }
                            character.setSpawnInMyFight(false);
                        }
                        else {
                            if (!character.isSpawnInWorld()) {
                                NetActorsFrame.m_logger.info((Object)("Despawn de " + actorId + " de world alors qu'il ne l'\u00ef¿½tait pas => ignor\u00ef¿½"));
                                return;
                            }
                            character.setSpawnInWorld(false);
                            final MRU currentMRU = UIMRUFrame.getInstance().getCurrentMRU();
                            if (currentMRU != null) {
                                for (int i = 0, size = currentMRU.getSourcesCount(); i < size; ++i) {
                                    if (currentMRU.getSource(i) == character) {
                                        UIMRUFrame.getInstance().closeCurrentMRU();
                                        break;
                                    }
                                }
                            }
                            if (localPlayer.getCurrentOccupation() != null && localPlayer.getCurrentInteractiveElement() == character.getActor()) {
                                localPlayer.getCurrentOccupation().cancel(false, true);
                            }
                        }
                        if (character.isSpawnInWorld()) {
                            break;
                        }
                        if (character.getCurrentFight() != null) {
                            character.getCurrentFight().addFighterToDespawnAtEndOfFight(character);
                            break;
                        }
                        this.finalDespawn(applyAPS, character);
                        break;
                    }
                    default: {
                        NetActorsFrame.m_logger.error((Object)("Unknown Actor Type " + type + " for actor " + actorId));
                        break;
                    }
                }
            }
        }
        catch (Exception e) {
            NetActorsFrame.m_logger.error((Object)("Exception lors du despawn de l'acteur " + actorId), (Throwable)e);
        }
    }
    
    public void finalDespawn(final boolean applyAPS, final CharacterInfo character) {
        this.removeFromFightAndPlayAps(character, applyAPS);
        if (character.getActor().isWaitEndAnimation()) {
            character.setWaitEndAnimationToBeDespawned(true);
            character.getActor().addAnimationEndedListener(new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    if (element instanceof Actor) {
                        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(element.getId());
                        CharacterInfoManager.getInstance().removeCharacter(character);
                    }
                }
            });
        }
        else {
            CharacterInfoManager.getInstance().removeCharacter(character);
        }
    }
    
    private void removeFromFightAndPlayAps(@NotNull final CharacterInfo character, final boolean applyAPS) {
        this.removeFromFight(character);
        this.playAps(character, applyAPS);
    }
    
    private void removeFromFight(final CharacterInfo character) {
        if (character.getCurrentFightId() == -1) {
            return;
        }
        final FightInfo fight = FightManager.getInstance().getFightById(character.getCurrentFightId());
        if (fight == null) {
            return;
        }
        if (fight instanceof ExternalFightInfo) {
            ((ExternalFightInfo)fight).removeFighter(character);
        }
    }
    
    private void playAps(final CharacterInfo character, final boolean applyAPS) {
        if (applyAPS) {
            final FreeParticleSystem particle = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(800030);
            particle.setTarget(character.getActor());
            IsoParticleSystemManager.getInstance().addParticleSystem(particle);
        }
        else {
            IsoParticleSystemManager.getInstance().clearParticlesOnTarget(character.getActor());
        }
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
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetActorsFrame.class);
        m_instance = new NetActorsFrame();
    }
}
