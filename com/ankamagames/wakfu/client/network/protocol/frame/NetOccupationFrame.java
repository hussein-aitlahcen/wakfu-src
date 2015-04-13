package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.skill.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.death.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.framework.kernel.*;

public class NetOccupationFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetOccupationFrame m_instance;
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 4170: {
                final SimpleOccupationModificationMessage msg = (SimpleOccupationModificationMessage)message;
                final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(msg.getConcernedPlayerId());
                if (character == null) {
                    return false;
                }
                if (msg.getOccupationType() == 4 && msg.getModificationType() == 1 && character.isOnFight()) {
                    return true;
                }
                this.simpleOccupationModification(msg.getOccupationType(), msg.getData(), msg.getConcernedPlayerId(), msg.getModificationType());
                return false;
            }
            case 4172: {
                final PlantOccupationStartMessage msg2 = (PlantOccupationStartMessage)message;
                if (msg2.getModificationType() != 1 && msg2.getOccupationType() != 2) {
                    NetOccupationFrame.m_logger.error((Object)"Erreur : on recoit un PLANT START MESSAGE pour une occupation ne d\u00e9butant pas / une occupation autre que PLANT");
                    return false;
                }
                final int visualId = msg2.getVisualId();
                final ActionVisual visual = ActionVisualManager.getInstance().get(visualId);
                if (msg2.getConcernedPlayerId() == localPlayer.getId()) {
                    final long duration = msg2.getDuration();
                    final int craftId = msg2.getCraftId();
                    final PlantOccupation occupation = new PlantOccupation(msg2.getResX(), msg2.getResY());
                    occupation.setDuration(duration);
                    occupation.setCraftId(craftId);
                    occupation.setVisual(visual);
                    if (occupation.isAllowed()) {
                        occupation.begin();
                    }
                    return false;
                }
                final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(msg2.getConcernedPlayerId());
                if (character2 == null) {
                    NetOccupationFrame.m_logger.error((Object)"[PLANT OCCUPATION] Uknown Mobile");
                    return false;
                }
                final CharacterActor actor = character2.getActor();
                if (actor.getCurrentPath() != null) {
                    final ActorSkillEndMovementListener skillEndListener = new ActorSkillEndMovementListener(character2, visualId, msg2.getResX(), msg2.getResY());
                    character2.setSkillEndMovementListener(skillEndListener);
                    return false;
                }
                final ActionVisual actionVisual = ActionVisualManager.getInstance().get(visualId);
                if (actionVisual != null) {
                    final Resource ressourceOnCell = ResourceManager.getInstance().getResource(msg2.getResX(), msg2.getResY());
                    if (ressourceOnCell != null) {
                        NetOccupationFrame.m_logger.error((Object)"On a recu un d\u00e9but de plantation sur une place non libre ! ");
                    }
                    ActionVisualHelper.applyActionVisual(actor, actionVisual);
                    return false;
                }
                return false;
            }
            case 4218: {
                final SearchTreasureOccupationStartMessage msg3 = (SearchTreasureOccupationStartMessage)message;
                if (msg3.getModificationType() != 1 && msg3.getOccupationType() != 26) {
                    NetOccupationFrame.m_logger.error((Object)"Erreur : on recoit un SEARCH TREASURE START MESSAGE pour une occupation ne d\u00e9butant pas / une occupation autre que SEARCH_TREASURE");
                    return false;
                }
                final int visualId = msg3.getVisualId();
                final ActionVisual visual = ActionVisualManager.getInstance().get(visualId);
                if (msg3.getConcernedPlayerId() == localPlayer.getId()) {
                    final long duration = msg3.getDuration();
                    final SearchTreasureOccupation occupation2 = new SearchTreasureOccupation(msg3.getX(), msg3.getY(), msg3.getZ());
                    occupation2.setDuration(duration);
                    occupation2.setVisual(visual);
                    if (occupation2.isAllowed()) {
                        occupation2.begin();
                    }
                    return false;
                }
                final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(msg3.getConcernedPlayerId());
                if (character2 == null) {
                    NetOccupationFrame.m_logger.error((Object)"[SEARCH TREASURE OCCUPATION] Uknown Mobile");
                    return false;
                }
                final CharacterActor actor = character2.getActor();
                if (actor.getCurrentPath() != null) {
                    final ActorSkillEndMovementListener skillEndListener = new ActorSkillEndMovementListener(character2, visualId, msg3.getX(), msg3.getY());
                    character2.setSkillEndMovementListener(skillEndListener);
                    return false;
                }
                final ActionVisual actionVisual = ActionVisualManager.getInstance().get(visualId);
                if (actionVisual != null) {
                    final Resource ressourceOnCell = ResourceManager.getInstance().getResource(msg3.getX(), msg3.getY());
                    if (ressourceOnCell != null) {
                        NetOccupationFrame.m_logger.error((Object)"On a recu un d\u00e9but de recherche de tr\u00e9sor sur une place non libre ! ");
                    }
                    ActionVisualHelper.applyActionVisual(actor, actionVisual);
                    return false;
                }
                return false;
            }
            case 4174: {
                final CollectOccupationModifMessage msg4 = (CollectOccupationModifMessage)message;
                if (msg4.getOccupationType() != 3) {
                    NetOccupationFrame.m_logger.error((Object)"Message COLLECT START re\u00e7u avec de mauvais param\u00e8tres");
                    return false;
                }
                final byte craftId2 = msg4.getCraftId();
                if (msg4.getConcernedPlayerId() == localPlayer.getId()) {
                    final byte flag = msg4.getFlag();
                    if (flag == 7) {
                        return false;
                    }
                    if (flag == 4) {
                        ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("collect.error.resourceOccupied"), 3);
                        return false;
                    }
                    if (!localPlayer.getCraftHandler().contains(craftId2)) {
                        NetOccupationFrame.m_logger.error((Object)("le joueur ne poss\u00e8de pas ce skill " + craftId2));
                        return false;
                    }
                    if (flag == 5) {
                        NetOccupationFrame.m_logger.error((Object)"COLLECT START : erreur, on obtient un message success.");
                    }
                    else {
                        final Resource resource = ResourceManager.getInstance().getResource(msg4.getX(), msg4.getY());
                        final CollectAction action = resource.getActionFromActionId(msg4.getActionId());
                        if (resource == null) {
                            return false;
                        }
                        final boolean waitForOthers = flag == 1;
                        final CollectOccupation collectOccupation = new CollectOccupation(action, resource);
                        collectOccupation.setEstimatedTime(msg4.getEstimatedTime());
                        collectOccupation.setUsedSlots(msg4.getUsedSlots());
                        collectOccupation.setWaitForOthers(waitForOthers);
                        if (collectOccupation.isAllowed()) {
                            collectOccupation.begin(msg4.getProgress());
                        }
                    }
                }
                else {
                    final CharacterInfo character3 = CharacterInfoManager.getInstance().getCharacter(msg4.getConcernedPlayerId());
                    if (character3 == null) {
                        NetOccupationFrame.m_logger.error((Object)"[COLLECT MODIF] Pas de character retrouv\u00e9");
                    }
                    else {
                        final CharacterActor actor2 = character3.getActor();
                        final Resource source = ResourceManager.getInstance().getResource(msg4.getX(), msg4.getY());
                        if (source == null) {
                            NetOccupationFrame.m_logger.error((Object)"Collect Start : aucune ressource \u00e0 la position indiqu\u00e9e");
                            return false;
                        }
                        final CollectAction action2 = source.getActionFromActionId(msg4.getActionId());
                        if (action2 == null) {
                            NetOccupationFrame.m_logger.error((Object)("pas d'action avec l'id " + msg4.getActionId()));
                        }
                        if (source == localPlayer.getCurrentInteractiveElement()) {
                            final boolean waitForOthers2 = msg4.getFlag() == 1;
                            if (localPlayer.getCurrentOccupation().getOccupationTypeId() == 3) {
                                final CollectOccupation occupation3 = (CollectOccupation)localPlayer.getCurrentOccupation();
                                occupation3.setEstimatedTime(msg4.getEstimatedTime());
                                occupation3.setUsedSlots(msg4.getUsedSlots());
                                occupation3.setWaitForOthers(waitForOthers2);
                                if (occupation3.isAllowed()) {
                                    occupation3.update(msg4.getProgress());
                                }
                            }
                        }
                        final ReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(msg4.getItemRefId());
                        if (actor2.getCurrentPath() != null) {
                            actor2.addEndPositionListener(new MobileEndPathListener() {
                                @Override
                                public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                                    if (item != null) {
                                        actor2.applyEquipment(item, EquipmentPosition.ACCESSORY.m_id);
                                    }
                                    NetOccupationFrame.this.startAnimation(action2, character3, source, actor2, craftId2);
                                    actor2.removeEndPositionListener(this);
                                }
                            });
                        }
                        else {
                            if (item != null) {
                                actor2.applyEquipment(item, EquipmentPosition.ACCESSORY.m_id);
                            }
                            this.startAnimation(action2, character3, source, actor2, craftId2);
                        }
                    }
                }
                return false;
            }
            case 4168: {
                final CollectMonsterOccupationModifMessage msg5 = (CollectMonsterOccupationModifMessage)message;
                if (msg5.getOccupationType() != 6) {
                    NetOccupationFrame.m_logger.error((Object)"Message COLLECT MONSTER START re\u00e7u avec de mauvais param\u00e8tres");
                    return false;
                }
                final NonPlayerCharacter monster = (NonPlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg5.getMonsterID());
                if (monster == null) {
                    NetOccupationFrame.m_logger.error((Object)"Message COLLECT MONSTER START re\u00e7u avec une cible invalide");
                    return false;
                }
                final CollectAction skillAction = monster.getBreed().getCollectAction(msg5.getActionID());
                if (skillAction == null) {
                    NetOccupationFrame.m_logger.error((Object)("skillAction inconnu id= " + msg5.getActionID()));
                    return false;
                }
                if (msg5.getConcernedPlayerId() == localPlayer.getId()) {
                    if (skillAction.getCraftId() != 0 && !localPlayer.getCraftHandler().contains(skillAction.getCraftId())) {
                        NetOccupationFrame.m_logger.error((Object)("le joueur ne poss\u00e8de pas ce craft " + skillAction.getCraftId()));
                        return false;
                    }
                    final CollectMonsterOccupation collectOccupation2 = new CollectMonsterOccupation(skillAction, monster);
                    collectOccupation2.setEstimatedTime(msg5.getCollectTime());
                    if (collectOccupation2.isAllowed()) {
                        collectOccupation2.begin();
                    }
                }
                else {
                    final CharacterInfo character2 = CharacterInfoManager.getInstance().getCharacter(msg5.getConcernedPlayerId());
                    if (character2 == null) {
                        NetOccupationFrame.m_logger.error((Object)"[COLLECT MODIF] Pas de character retrouv\u00e9");
                    }
                    else {
                        final CharacterActor actor = character2.getActor();
                        final int actionId = skillAction.getVisualId();
                        if (actor.getCurrentPath() != null) {
                            final ActorSkillEndMovementListener skillEndListener2 = new ActorSkillEndMovementListener(character2, actionId, monster.getPosition().getX(), monster.getPosition().getY());
                            character2.setSkillEndMovementListener(skillEndListener2);
                        }
                        else {
                            final ActionVisual actionVisual2 = ActionVisualManager.getInstance().get(actionId);
                            if (actionVisual2 != null) {
                                character2.setCurrentInteractiveElement(monster.getActor());
                                final String[] actorAnimation = actor.getAnimation().split("-");
                                final String skillAnimation = (monster == null) ? "" : ActionVisualHelper.getAnimationName(actionVisual2);
                                if (!actorAnimation[0].equals(skillAnimation)) {
                                    ActionVisualHelper.applyActionVisual(actor, actionVisual2);
                                }
                            }
                            else {
                                NetOccupationFrame.m_logger.error((Object)("Action Description non trouv\u00e9e pour le monstre " + monster.getId() + " et le skill " + skillAction.getCraftId()));
                            }
                        }
                    }
                }
                return false;
            }
            case 4206: {
                final StartCollectOnInteractiveElementMessage msg6 = (StartCollectOnInteractiveElementMessage)message;
                if (msg6.getOccupationType() != 13 && msg6.getOccupationType() != 28) {
                    NetOccupationFrame.m_logger.error((Object)"Message DISTRIBUTION re\u00e7u avec de mauvais param\u00e8tres");
                    return false;
                }
                final OccupationInteractiveElement machine = (OccupationInteractiveElement)LocalPartitionManager.getInstance().getInteractiveElement(msg6.getInteractifElementId());
                if (machine == null) {
                    NetOccupationFrame.m_logger.error((Object)"Message START_COLLECT_ON_INTERACTIVE_ELEMENT re\u00e7u avec une cible invalide");
                    return false;
                }
                final CharacterInfo user = CharacterInfoManager.getInstance().getCharacter(msg6.getConcernedPlayerId());
                if (user == null) {
                    NetOccupationFrame.m_logger.error((Object)"Message START_COLLECT_ON_INTERACTIVE_ELEMENT re\u00e7u avec un character player invalide");
                    return false;
                }
                final InteractiveElementUseOccupation collectOccupation3 = new InteractiveElementUseOccupation(machine, msg6.getOccupationType());
                collectOccupation3.setEstimatedTime(msg6.getCollectTime());
                if (collectOccupation3.isAllowed()) {
                    collectOccupation3.begin();
                }
                return false;
            }
            case 15724: {
                final SitOccupationMessage msg7 = (SitOccupationMessage)message;
                final Stool stool = (Stool)LocalPartitionManager.getInstance().getInteractiveElement(msg7.getInteractiveElementId());
                if (stool == null) {
                    NetOccupationFrame.m_logger.error((Object)"Message SIT_OCCUPATION_START_MESSAGE re\u00e7u avec un stool invalide");
                    return false;
                }
                final CharacterInfo user = CharacterInfoManager.getInstance().getCharacter(msg7.getUserId());
                if (user == null) {
                    NetOccupationFrame.m_logger.error((Object)"Message SIT_OCCUPATION_START_MESSAGE re\u00e7u avec un character player invalide");
                    return false;
                }
                final SitOccupation occupation4 = new SitOccupation(user, stool);
                if (occupation4.isAllowed()) {
                    occupation4.begin();
                    if (user == localPlayer) {
                        WakfuGameEntity.getInstance().pushFrame(UISitOccupationFrame.getInstance());
                    }
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void startAnimation(final CollectAction action, final CharacterInfo character, final Resource source, final CharacterActor actor, final byte craftId) {
        final ActionVisual actionVisual = ActionVisualManager.getInstance().get(action.getVisualId());
        if (actionVisual == null) {
            NetOccupationFrame.m_logger.error((Object)("Action Description non trouv\u00e9e pour ressource " + source.getId() + " et  la skill " + craftId));
            return;
        }
        character.setCurrentInteractiveElement(source);
        final String[] actorAnimation = actor.getAnimation().split("-");
        final String skillAnimation = ActionVisualHelper.getAnimationName(actionVisual);
        if (!actorAnimation[0].equals(skillAnimation)) {
            ActionVisualHelper.applyActionVisual(actor, actionVisual);
        }
    }
    
    public void simpleOccupationModification(final short occupationType, final byte[] data, final long concernedPlayerId, final short modificationType) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        Label_0727: {
            switch (occupationType) {
                case 14: {
                    final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(concernedPlayerId);
                    if (character == null) {
                        NetOccupationFrame.m_logger.error((Object)("[RIDE] Personnage inexistant : " + concernedPlayerId));
                        return;
                    }
                    if (modificationType == 1) {
                        final RideOccupation occupation = new RideOccupation(character);
                        if (occupation.isAllowed()) {
                            occupation.begin();
                        }
                    }
                    else if (modificationType == 2) {
                        if (character.getCurrentOccupation() != null && character.getCurrentOccupation().getOccupationTypeId() == 14) {
                            character.finishCurrentOccupation();
                        }
                        else {
                            NetOccupationFrame.m_logger.error((Object)("[RIDE] Demande d'arr\u00eat d'occupation alors que le personnage est en " + character.getCurrentOccupation()));
                        }
                    }
                    else if (modificationType == 3) {
                        if (character.getCurrentOccupation() != null && character.getCurrentOccupation().getOccupationTypeId() == 14) {
                            character.cancelCurrentOccupation(true, false);
                        }
                        else {
                            NetOccupationFrame.m_logger.error((Object)("[RIDE] Demande d'arr\u00eat d'occupation alors que le personnage est en " + character.getCurrentOccupation()));
                        }
                    }
                }
                case 20: {
                    final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(concernedPlayerId);
                    if (character == null) {
                        NetOccupationFrame.m_logger.error((Object)("[EMOTE] Personnage inexistant : " + concernedPlayerId));
                        return;
                    }
                    if (modificationType == 1) {
                        final EmoteOccupation occupation2 = new EmoteOccupation(character);
                        occupation2.build(data);
                        if (occupation2.isAllowed()) {
                            occupation2.begin();
                        }
                    }
                    else if (modificationType == 2) {
                        if (character.getCurrentOccupation() != null && character.getCurrentOccupation().getOccupationTypeId() == 20) {
                            character.finishCurrentOccupation();
                        }
                        else {
                            NetOccupationFrame.m_logger.error((Object)("[EMOTE] Demande d'arr\u00eat d'occupation alors que le personnage est en " + character.getCurrentOccupation()));
                        }
                    }
                }
                case 1: {
                    final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(concernedPlayerId);
                    if (character != null) {
                        if (modificationType == 1) {
                            final RestOccupation occupation3 = new RestOccupation(character);
                            if (occupation3.isAllowed()) {
                                occupation3.begin();
                            }
                        }
                        else if (modificationType == 2) {
                            if (character.getCurrentOccupation() != null && character.getCurrentOccupation().getOccupationTypeId() == 1) {
                                character.finishCurrentOccupation();
                            }
                            else {
                                NetOccupationFrame.m_logger.error((Object)("[REST_OCCUPATION] Demande d'annulation alors que le personnage est en " + character.getCurrentOccupation()));
                            }
                        }
                    }
                    else {
                        NetOccupationFrame.m_logger.error((Object)("[REST_OCCUPATION] Personnage inexistant : " + concernedPlayerId));
                    }
                }
                case 3: {
                    if (modificationType != 3) {
                        break Label_0727;
                    }
                    if (concernedPlayerId == localPlayer.getId()) {
                        if (localPlayer.getCurrentOccupation() == null) {
                            NetOccupationFrame.m_logger.info((Object)"Pas d'occupation en cours, occupation d\u00e9ja termin\u00e9e");
                            return;
                        }
                        if (localPlayer.getCurrentOccupation().getOccupationTypeId() == 3) {
                            localPlayer.cancelCurrentOccupation(true, false);
                            return;
                        }
                        break Label_0727;
                    }
                    else {
                        final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(concernedPlayerId);
                        if (character == null) {
                            NetOccupationFrame.m_logger.error((Object)"[COLLECT ANNULATION] Pas de character retrouv\u00e9");
                            return;
                        }
                        final CharacterActor actor = character.getActor();
                        if (actor.getActionInProgress() != null) {
                            actor.getActionInProgress().endAction();
                            return;
                        }
                        break Label_0727;
                    }
                    break;
                }
                case 4: {
                    final PlayerCharacter playerConcerned = (concernedPlayerId == localPlayer.getId()) ? localPlayer : ((PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(concernedPlayerId));
                    if (playerConcerned == null) {
                        NetOccupationFrame.m_logger.info((Object)"Demande de d\u00e9but ou de fin d'occupation (death) pour un joueur non connu");
                        return;
                    }
                    switch (modificationType) {
                        case 1: {
                            final DeadOccupation deadOccupation = new DeadOccupation(playerConcerned);
                            deadOccupation.build(data);
                            if (deadOccupation.isAllowed()) {
                                deadOccupation.begin();
                            }
                            return;
                        }
                        case 2: {
                            final AbstractOccupation occ = playerConcerned.getCurrentOccupation();
                            if (!(occ instanceof DeadOccupation)) {
                                return;
                            }
                            playerConcerned.finishCurrentOccupation();
                            return;
                        }
                        default: {
                            return;
                        }
                    }
                    break;
                }
                case 5: {
                    if (concernedPlayerId == localPlayer.getId() && modificationType == 3) {
                        NetOccupationFrame.m_logger.info((Object)"Recu demande d'annulation du browsing du local player");
                        final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
                        if (currentOccupation != null && currentOccupation instanceof BrowseFleaOccupation) {
                            localPlayer.cancelCurrentOccupation(true, false);
                        }
                    }
                }
                case 7: {
                    if (concernedPlayerId == localPlayer.getId() && modificationType == 3) {
                        NetOccupationFrame.m_logger.info((Object)"Recu demande d'annulation du managing du local player");
                        final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
                        if (currentOccupation != null && currentOccupation instanceof ManageFleaOccupation) {
                            localPlayer.cancelCurrentOccupation(true, false);
                        }
                    }
                }
                case 16: {
                    final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(concernedPlayerId);
                    if (character != null) {
                        if (modificationType == 3) {
                            character.cancelCurrentOccupation(true, false);
                        }
                        else if (modificationType == 2) {
                            character.finishCurrentOccupation();
                        }
                    }
                    else {
                        NetOccupationFrame.m_logger.error((Object)("[REST_OCCUPATION] Personnage inexistant : " + concernedPlayerId));
                    }
                }
                case 25: {
                    while (true) {
                        if (modificationType == 1) {
                            if (modificationType == 3) {
                                NetOccupationFrame.m_logger.info((Object)"Recu demande d'annulation du managing du local player");
                                final AbstractOccupation currentOccupation = localPlayer.getCurrentOccupation();
                                if (currentOccupation != null && currentOccupation instanceof ManageFleaOccupation) {
                                    localPlayer.cancelCurrentOccupation(true, false);
                                }
                            }
                            return;
                        }
                        continue;
                    }
                }
                default: {
                    final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(concernedPlayerId);
                    if (character != null) {
                        if (modificationType == 3) {
                            character.cancelCurrentOccupation(true, false);
                        }
                        else if (modificationType == 2) {
                            character.finishCurrentOccupation();
                        }
                    }
                    else {
                        NetOccupationFrame.m_logger.error((Object)("[OCCUPATION] Personnage inexistant : " + concernedPlayerId));
                    }
                    if (modificationType == 3) {
                        if (concernedPlayerId != localPlayer.getId()) {
                            NetOccupationFrame.m_logger.error((Object)"Message d'annulation re\u00e7u pour un autre client que le localplayer");
                            return;
                        }
                        if (localPlayer.getCurrentOccupation() == null) {
                            NetOccupationFrame.m_logger.info((Object)"Occupation d\u00e9ja termin\u00e9e");
                            return;
                        }
                        if (localPlayer.getCurrentOccupation().getOccupationTypeId() == occupationType) {
                            localPlayer.cancelCurrentOccupation(true, false);
                        }
                        else {
                            NetOccupationFrame.m_logger.error((Object)"Message d'annulation recu pour une occupation diff\u00e9rente de celle en cours");
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    public static NetOccupationFrame getInstance() {
        return NetOccupationFrame.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetOccupationFrame.class);
        NetOccupationFrame.m_instance = new NetOccupationFrame();
    }
}
