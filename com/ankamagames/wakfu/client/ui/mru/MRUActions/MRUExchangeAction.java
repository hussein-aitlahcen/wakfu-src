package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.common.account.subscription.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import java.util.*;

public class MRUExchangeAction extends AbstractLawMRUAction
{
    public static final int ENABLED = 0;
    public static final int IS_IN_PRISON = 1;
    public static final int SOMEONE_IS_NOT_SUBSCRIBED = 2;
    private static final int LOCAL_PLAYER_NOT_SUBSCRIPTION_RIGHT = 3;
    private static final int TARGET_NOT_SUBSCRIPTION_RIGHT = 4;
    private MobileEndPathListener m_listener;
    private int m_disableReason;
    
    @Override
    public MRUActions tag() {
        return MRUActions.CHARACTER_EXCHANGE_ACTION;
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUExchangeAction();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUExchangeAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor localActor = localPlayer.getActor();
        if (this.m_listener != null) {
            localActor.removeEndPositionListener(this.m_listener);
        }
        final PlayerCharacter character = (PlayerCharacter)this.m_source;
        if (!ChatHelper.controlAction(new Action(character.getName(), 3))) {
            return;
        }
        final int dx = character.getWorldCellX() - localActor.getWorldCellX();
        final int dy = character.getWorldCellY() - localActor.getWorldCellY();
        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
            localActor.setDirection(Vector3i.getDirection8FromVector(dx, dy));
            final ExchangeInvitationRequestMessage netMessage = new ExchangeInvitationRequestMessage();
            netMessage.setOtherUserId(character.getId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
            return;
        }
        localActor.addEndPositionListener(this.m_listener = new MobileEndPathListener() {
            @Override
            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                mobile.removeEndPositionListener(this);
                final PlayerCharacter character = (PlayerCharacter)MRUExchangeAction.this.m_source;
                final int dx = character.getWorldCellX() - mobile.getWorldCellX();
                final int dy = character.getWorldCellY() - mobile.getWorldCellY();
                if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
                    mobile.setDirection(Vector3i.getDirection8FromVector(dx, dy));
                    final ExchangeInvitationRequestMessage netMessage = new ExchangeInvitationRequestMessage();
                    netMessage.setOtherUserId(character.getId());
                    WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                }
            }
        });
        localPlayer.moveTo(((PlayerCharacter)this.m_source).getPosition(), true, true);
    }
    
    @Override
    public boolean isEnabled() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final PlayerCharacter playerCharacter = (PlayerCharacter)this.m_source;
        final SubscriptionLevel localSubscriptionLevel = localPlayer.getAccountInformationHandler().getActiveSubscriptionLevel();
        final SubscriptionLevel targetSubscriptionLevel = playerCharacter.getAccountInformationHandler().getActiveSubscriptionLevel();
        if (localSubscriptionLevel == SubscriptionLevel.EU_FREE || targetSubscriptionLevel == SubscriptionLevel.EU_FREE) {
            if (localSubscriptionLevel != targetSubscriptionLevel) {
                this.m_disableReason = 2;
                return false;
            }
        }
        else {
            if (!localPlayer.hasSubscriptionRight(SubscriptionRight.EXCHANGE)) {
                this.m_disableReason = 3;
                return false;
            }
            if (!playerCharacter.hasSubscriptionRight(SubscriptionRight.EXCHANGE)) {
                this.m_disableReason = 4;
                return false;
            }
        }
        if (localPlayer.isInPrisonInstance() || playerCharacter.isInPrisonInstance()) {
            this.m_disableReason = 1;
            return false;
        }
        return true;
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final CharacterInfo source = (CharacterInfo)this.m_source;
        if (source.isOnFight()) {
            return false;
        }
        if (source.isActiveProperty(WorldPropertyType.EXCHANGE_DISABLED)) {
            return false;
        }
        if (source.getCurrentOccupation() != null && source.getCurrentOccupation().getOccupationTypeId() == 4) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isWaitingForResult() && !localPlayer.isActiveProperty(WorldPropertyType.EXCHANGE_DISABLED) && !localPlayer.isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public String getTranslatorKey() {
        return "trade";
    }
    
    @Override
    public String getLabel() {
        if (!this.isEnabled()) {
            final TextWidgetFormater sb = new TextWidgetFormater();
            sb.b().append(WakfuTranslator.getInstance().getString("desc.mru." + this.getTranslatorKey()))._b().append("\n");
            sb.openText().addColor(MRUExchangeAction.NOK_TOOLTIP_COLOR);
            switch (this.m_disableReason) {
                case 2: {
                    sb.append(WakfuTranslator.getInstance().getString("error.someoneNotSubscribed")).closeText();
                    break;
                }
                case 4: {
                    sb.append(WakfuTranslator.getInstance().getString("error.targetNotSubscriptionRight")).closeText();
                    break;
                }
                case 3: {
                    sb.append(WakfuTranslator.getInstance().getString("error.playerNotSubscriptionRight")).closeText();
                    break;
                }
                case 1: {
                    sb.append(WakfuTranslator.getInstance().getString("exchange.forbidden.in.prison")).closeText();
                    break;
                }
            }
            return sb.finishAndToString();
        }
        if (!(this.m_source instanceof CharacterInfo)) {
            return this.getTranslatorKey();
        }
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(MRUExchangeAction.DEFAULT_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString("exchange.with", ((CharacterInfo)this.m_source).getName()))._b();
        return sb.finishAndToString();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.EXCHANGE.m_id;
    }
    
    @Override
    public List<NationLaw> getTriggeredLaws() {
        if (AbstractLawMRUAction.getCurrentNationAlignment() != NationAlignement.ALLIED) {
            return null;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ExchangeLawEvent exchangeLawEvent = new ExchangeLawEvent(localPlayer, (Citizen)this.m_source);
        final List<NationLaw> triggeredLaws = new ArrayList<NationLaw>();
        triggeredLaws.addAll(exchangeLawEvent.getTriggeringLaws());
        return triggeredLaws;
    }
    
    @Override
    public List<NationLaw> getProbablyTriggeredLaws() {
        return null;
    }
}
