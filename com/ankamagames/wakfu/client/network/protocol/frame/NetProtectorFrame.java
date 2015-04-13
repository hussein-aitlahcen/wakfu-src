package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.game.notificationSystem.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.notificationMessage.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.xulor2.core.netEnabled.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.protector.ecosystem.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.join.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.protector.event.*;
import com.ankamagames.wakfu.common.game.protector.*;

public class NetProtectorFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static NetProtectorFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 15300: {
                this.protectorAttackedMessage((ProtectorAttackedMessage)message);
                return false;
            }
            case 15304: {
                this.protectorDefeatedMessage((ProtectorDefeatedMessage)message);
                return false;
            }
            case 15302: {
                this.protectorDefendedMessage((ProtectorDefendedMessage)message);
                return false;
            }
            case 15306: {
                final ProtectorAcquiredMessage msg = (ProtectorAcquiredMessage)message;
                Protector protector = ProtectorManager.INSTANCE.getProtector(msg.getProtectorId());
                if (protector == null) {
                    protector = ProtectorManager.INSTANCE.getStaticProtector(msg.getProtectorId());
                }
                final String territoryName = WakfuTranslator.getInstance().getString(66, protector.getTerritory().getId(), new Object[0]);
                final String title = WakfuTranslator.getInstance().getString("notification.protectorAcquiredTitle");
                final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.protectorAcquiredText", territoryName), NotificationMessageType.NATION, "1");
                final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
                Worker.getInstance().pushMessage(uiNotificationMessage);
                return false;
            }
            case 15308: {
                final ProtectorNationChangedMessage msg2 = (ProtectorNationChangedMessage)message;
                this.protectorNationChangedMessage(msg2);
                return false;
            }
            case 15320: {
                final ProtectorManagementAnswerMessage msg3 = (ProtectorManagementAnswerMessage)message;
                this.protectorManagementAnswerMessage(msg3);
                return false;
            }
            case 15334: {
                final ProtectorTaxUpdateAnswerMessage msg4 = (ProtectorTaxUpdateAnswerMessage)message;
                this.protectorTaxUpdateAnswerMessage(msg4);
                return false;
            }
            case 15328: {
                final ProtectorSatisfactionChangedMessage msg5 = (ProtectorSatisfactionChangedMessage)message;
                this.protectorSatisfactionChanged(msg5);
                return false;
            }
            case 15317: {
                final GetProtectorFightStakeAnswerMessage msg6 = (GetProtectorFightStakeAnswerMessage)message;
                final int fightStake = msg6.getFightStake();
                final Protector protector2 = ProtectorManager.INSTANCE.getProtector(msg6.getProtectorId());
                if (protector2 == null) {
                    NetProtectorFrame.m_logger.error((Object)("Reponse pour la mise d'un protecteur inconnu de notre manager " + msg6.getProtectorId()));
                    return false;
                }
                this.onProtectorStakeResponse(protector2, fightStake);
                return false;
            }
            case 15326: {
                final InstanceProtectorsUpdateMessage msg7 = (InstanceProtectorsUpdateMessage)message;
                final ArrayList<byte[]> serializedProtectors = msg7.getSerializedProtectors();
                for (final byte[] serializedProtector : serializedProtectors) {
                    final ByteBuffer buffer = ByteBuffer.wrap(serializedProtector);
                    final int id = ProtectorSerializer.INSTANCE.extractProtectorId(buffer);
                    final Protector protector3 = ProtectorManager.INSTANCE.getProtector(id);
                    if (protector3 != null) {
                        buffer.rewind();
                        ProtectorSerializer.INSTANCE.unserialize(buffer, protector3);
                    }
                    else {
                        NetProtectorFrame.m_logger.error((Object)("Le protecteur ID (" + id + ") n'existe pas. Pas de mise \u00e0 jour, et pas de cr\u00e9ation."));
                    }
                }
                MapManager.getInstance().onProtectorsUpdate();
                return false;
            }
            case 15330: {
                final ProtectorEcosystemActionAnswerMessage msg8 = (ProtectorEcosystemActionAnswerMessage)message;
                this.onProtectorEcosystemActionAnswer(msg8);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    private void onProtectorEcosystemActionAnswer(final ProtectorEcosystemActionAnswerMessage msg) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final Protector protector = ProtectorManager.INSTANCE.getProtector(msg.getProtectorId());
        final ProtectorEcosystemHandler handler = protector.getEcosystemHandler();
        final ProtectorEcosystemView ecosystem = handler.getView();
        final int familyId = msg.getFamilyId();
        if (msg.isActionResult()) {
            switch (msg.getAction()) {
                case PROTECT_MONSTER_FAMILY: {
                    final ProtectorEcosystemElement element = ecosystem.getElement(familyId, true);
                    handler.protectMonsterFamily(familyId);
                    protector.getWallet().substractAmount(ProtectorWalletContext.ECOSYSTEM, element.getProtectPrice());
                    final ProtectorWalletHandler walletHandler = (ProtectorWalletHandler)ProtectorView.getInstance().getProtector().getWallet();
                    walletHandler.getView(protector).updateView();
                    element.updateProtectedField();
                    break;
                }
                case PROTECT_RESOURCE_FAMILY: {
                    final ProtectorEcosystemElement element = ecosystem.getElement(familyId, false);
                    handler.protectResourceFamily(familyId);
                    protector.getWallet().substractAmount(ProtectorWalletContext.ECOSYSTEM, element.getProtectPrice());
                    final ProtectorWalletHandler walletHandler = (ProtectorWalletHandler)ProtectorView.getInstance().getProtector().getWallet();
                    walletHandler.getView(protector).updateView();
                    element.updateProtectedField();
                    break;
                }
                case UNPROTECT_MONSTER_FAMILY: {
                    final ProtectorEcosystemElement element = ecosystem.getElement(familyId, true);
                    handler.unprotectMonsterFamily(familyId);
                    element.updateProtectedField();
                    break;
                }
                case UNPROTECT_RESOURCE_FAMILY: {
                    final ProtectorEcosystemElement element = ecosystem.getElement(familyId, false);
                    handler.unprotectResourceFamily(familyId);
                    element.updateProtectedField();
                    break;
                }
                case REINTRODUCE_MONSTER_FAMILY: {
                    final ProtectorEcosystemElement element = ecosystem.getElement(familyId, true);
                    handler.reintroduceMonsterFamily(player, familyId);
                    protector.getWallet().substractAmount(ProtectorWalletContext.ECOSYSTEM, element.getReintroducePrice());
                    final WakfuEcosystemFamilyInfo info = WakfuMonsterZoneManager.getInstance().getMonsterFamilyInfo(familyId);
                    if (info != null) {
                        info.setReintroducing(true);
                    }
                    element.updateExtinctField();
                    final ProtectorWalletHandler walletHandler2 = (ProtectorWalletHandler)ProtectorView.getInstance().getProtector().getWallet();
                    walletHandler2.getView(protector).updateView();
                    break;
                }
                case REINTRODUCE_RESOURCE_FAMILY: {
                    final ProtectorEcosystemElement element = ecosystem.getElement(familyId, false);
                    handler.reintroduceResourceFamily(player, familyId);
                    protector.getWallet().substractAmount(ProtectorWalletContext.ECOSYSTEM, element.getReintroducePrice());
                    final WakfuEcosystemFamilyInfo info = WakfuResourceZoneManager.getInstance().getResourceFamilyInfo(familyId);
                    if (info != null) {
                        info.setReintroducing(true);
                    }
                    element.updateExtinctField();
                    final ProtectorWalletHandler walletHandler2 = (ProtectorWalletHandler)ProtectorView.getInstance().getProtector().getWallet();
                    walletHandler2.getView(protector).updateView();
                    break;
                }
            }
        }
        else {
            NetProtectorFrame.m_logger.warn((Object)("Le serveur a renvoy\u00e9 une erreur pour l'action " + msg.getAction().name() + " pour le protecteur " + msg.getProtectorId() + " et la famille " + msg.getFamilyId()));
        }
        NetEnabledWidgetManager.INSTANCE.setGroupEnabled("protectorEcosystemLock", true);
    }
    
    private void protectorSatisfactionChanged(final ProtectorSatisfactionChangedMessage msg) {
        final int protectorId = msg.getProtectorId();
        final byte satisfactionId = msg.getProtectorSatisfaction();
        final ProtectorSatisfactionLevel protectorSatisfactionLevel = ProtectorSatisfactionLevel.fromId(satisfactionId);
        final Protector protector = ProtectorManager.INSTANCE.getProtector(protectorId);
        protector.getSatisfactionManager().setGlobalSatisfaction(protectorSatisfactionLevel);
        final ProtectorSatisfactionChangedEvent protectorEvent = (ProtectorSatisfactionChangedEvent)ProtectorEvents.PROTECTOR_SATISFACTION_CHANGED.create();
        protectorEvent.setProtectorSatisfactionLevel(protectorSatisfactionLevel);
        protectorEvent.setProtector(protector);
        ProtectorEventDispatcher.INSTANCE.dispatch(protectorEvent);
    }
    
    private void protectorTaxUpdateAnswerMessage(final ProtectorTaxUpdateAnswerMessage msg) {
        final int protectorId = msg.getProtectorId();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean isInOwnDimensionalBag = localPlayer != null && localPlayer.getVisitingDimentionalBag() == localPlayer.getOwnedDimensionalBag();
        Protector protector;
        if (!isInOwnDimensionalBag) {
            protector = ProtectorManager.INSTANCE.getProtector(protectorId);
        }
        else {
            protector = ProtectorView.getInstance().getProtectorCacheForDimensionalBag();
        }
        if (protector == null) {
            NetProtectorFrame.m_logger.error((Object)("R\u00e9ception de donn\u00e9es de taxe de protecteur pour un protecteur inconnu protectorId=" + protectorId));
            return;
        }
        final byte[] taxData = msg.getTaxData();
        ProtectorSerializer.INSTANCE.unserialize(ByteBuffer.wrap(taxData), protector);
        PropertiesProvider.getInstance().firePropertyValueChanged(ProtectorView.getInstance(), "tax");
    }
    
    private void protectorManagementAnswerMessage(final ProtectorManagementAnswerMessage msg) {
        final int protectorId = msg.getProtectorId();
        final Protector protector = ProtectorManager.INSTANCE.getProtector(protectorId);
        if (protector == null) {
            NetProtectorFrame.m_logger.error((Object)("R\u00e9ception de donn\u00e9es de gestion de protecteur pour un protecteur inconnu protectorId=" + protectorId));
            return;
        }
        final byte[] managementData = msg.getManagementData();
        ProtectorSerializer.INSTANCE.unserialize(ByteBuffer.wrap(managementData), protector);
        final byte[] serializedHistory = msg.getManagedTerritoryClimate();
        if (serializedHistory != null && serializedHistory.length > 0) {
            final WeatherHistory weatherHistory = new WeatherHistory();
            weatherHistory.fromBuild(ByteBuffer.wrap(serializedHistory));
            WeatherInfoManager.getInstance().updateFromWeatherHistory(weatherHistory);
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(ProtectorView.getInstance(), ProtectorView.FIELDS);
    }
    
    private void onProtectorStakeResponse(final Protector protector, final int fightStake) {
        final String message = WakfuTranslator.getInstance().getString("protector.question.askMoneyToFight", fightStake, protector.getName());
        final MessageBoxControler controler = Xulor.getInstance().msgBox(message, WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 24L, 102, 0);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if ((type & 0x8) != 0x8) {
                    return;
                }
                NetProtectorFrame.this.tryAttackProtector(protector, fightStake);
            }
        });
    }
    
    void tryAttackProtector(final Protector protector, final int stakeValue) {
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final int kamasCount = player.getKamasCount();
        if (kamasCount < stakeValue) {
            ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("protector.error.notEnoughMoneyToFight"), 3);
            return;
        }
        final NonPlayerCharacter protectorCharacter = protector.getNpc();
        if (protectorCharacter.isOnFight()) {
            final JoinFightProcedure joinFightProc = JoinFight.resumeProtectorAssault(protector);
            joinFightProc.suppressQueries();
            final JoinFightResult joinResult = joinFightProc.tryJoinFight();
            if (joinResult == JoinFightResult.OK) {
                return;
            }
            ErrorsMessageTranslator.getInstance().pushMessage(joinResult.getErrorCode(), 3, new Object[0]);
        }
        else {
            player.askForFightCreation(protectorCharacter);
        }
    }
    
    private void protectorNationChangedMessage(final ProtectorNationChangedMessage msg) {
        final int protectorId = msg.getProtectorId();
        final Protector protector = ProtectorManager.INSTANCE.getProtector(protectorId);
        final Nation nation = NationManager.INSTANCE.getNationById(msg.getNationId());
        if (protector != null) {
            protector.setCurrentNation(nation);
        }
        else {
            NetProtectorFrame.m_logger.error((Object)("R\u00e9ception d'un \u00e9v\u00e9nement pour un protecteur inconnu id=" + msg.getProtectorId()));
        }
    }
    
    private void protectorDefendedMessage(final ProtectorDefendedMessage msg) {
        final Protector protector = ProtectorManager.INSTANCE.getProtector(msg.getProtectorId());
        if (protector != null) {
            final ProtectorDefended protectorDefendedEvent = (ProtectorDefended)ProtectorEvents.PROTECTOR_DEFENDED.create();
            protectorDefendedEvent.setProtector(protector);
            protectorDefendedEvent.setAttackingNation(null);
            ProtectorEventDispatcher.INSTANCE.dispatch(protectorDefendedEvent);
        }
        else {
            NetProtectorFrame.m_logger.error((Object)("R\u00e9ception d'un \u00e9v\u00e9nement pour un protecteur inconnu id=" + msg.getProtectorId()));
        }
    }
    
    private void protectorDefeatedMessage(final ProtectorDefeatedMessage msg) {
        final Protector protector = ProtectorManager.INSTANCE.getProtector(msg.getProtectorId());
        if (protector != null) {
            final ProtectorDefeated protectorDefeatedEvent = (ProtectorDefeated)ProtectorEvents.PROTECTOR_DEFEATED.create();
            protectorDefeatedEvent.setProtector(protector);
            protectorDefeatedEvent.setAttackingNation(NationManager.INSTANCE.getNationById(msg.getNationId()));
            ProtectorEventDispatcher.INSTANCE.dispatch(protectorDefeatedEvent);
            final String territoryName = WakfuTranslator.getInstance().getString(66, protector.getTerritory().getId(), new Object[0]);
            final String title = WakfuTranslator.getInstance().getString("notification.protectorLostTitle");
            final String text = NotificationPanelDialogActions.createLink(WakfuTranslator.getInstance().getString("notification.protectorLostText", territoryName), NotificationMessageType.NATION, "1");
            final UINotificationMessage uiNotificationMessage = new UINotificationMessage(title, text, NotificationMessageType.NATION, 600132);
            Worker.getInstance().pushMessage(uiNotificationMessage);
        }
        else {
            NetProtectorFrame.m_logger.error((Object)("R\u00e9ception d'un \u00e9v\u00e9nement pour un protecteur inconnu id=" + msg.getProtectorId()));
        }
    }
    
    private void protectorAttackedMessage(final ProtectorAttackedMessage msg) {
        final Protector protector = ProtectorManager.INSTANCE.getProtector(msg.getProtectorId());
        if (protector != null) {
            final ProtectorAttacked protectorAttackedEvent = (ProtectorAttacked)ProtectorEvents.PROTECTOR_ATTACKED.create();
            protectorAttackedEvent.setProtector(protector);
            protectorAttackedEvent.setAttackingNation(null);
            ProtectorEventDispatcher.INSTANCE.dispatch(protectorAttackedEvent);
        }
        else {
            NetProtectorFrame.m_logger.error((Object)("R\u00e9ception d'un \u00e9v\u00e9nement pour un protecteur inconnu id=" + msg.getProtectorId()));
        }
    }
    
    private void protectorAcquiredMessage(final ProtectorAcquiredMessage msg) {
        final Protector protector = ProtectorManager.INSTANCE.getProtector(msg.getProtectorId());
        if (protector != null) {
            final ProtectorAttacked protectorAttackedEvent = (ProtectorAttacked)ProtectorEvents.PROTECTOR_ATTACKED.create();
            protectorAttackedEvent.setProtector(protector);
            protectorAttackedEvent.setAttackingNation(null);
            ProtectorEventDispatcher.INSTANCE.dispatch(protectorAttackedEvent);
        }
        else {
            NetProtectorFrame.m_logger.error((Object)("R\u00e9ception d'un \u00e9v\u00e9nement pour un protecteur inconnu id=" + msg.getProtectorId()));
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetProtectorFrame.class);
        NetProtectorFrame.INSTANCE = new NetProtectorFrame();
    }
}
