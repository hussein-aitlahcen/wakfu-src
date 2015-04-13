package com.ankamagames.wakfu.client.core.game.travel.provider;

import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.travel.character.*;
import com.ankamagames.wakfu.common.game.travel.infos.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public class CannonTravelProvider extends ClientTravelProvider
{
    private static final AnimationEndedListener MESSAGE_LISTENER;
    
    @Override
    public void discover(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
        final TravelHandler travelHandler = WakfuGameEntity.getInstance().getLocalPlayer().getTravelHandler();
        ((WakfuClientMapInteractiveElement)travelMachine).sendActionMessage(((ClientMapInteractiveElement)travelMachine).getDefaultAction());
        travelHandler.addDiscoveredCannon((int)travelMachine.getId());
    }
    
    @Override
    public void activate(final MapInteractiveElement travelMachine, final BasicCharacterInfo user) {
        this.travel(user, travelMachine, -1L);
    }
    
    @Override
    public TravelError checkTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final CannonInfo source = TravelInfoManager.INSTANCE.getInfo(this.getType(), sourceMachine.getId());
        if (ReferenceItemManager.getInstance().getReferenceItem(source.getItemId()) == null) {
            return TravelError.NO_ERROR;
        }
        if (!WakfuAccountPermissionContext.SUBSCRIBER.hasPermission((WakfuAccountInformationHolder)user)) {
            return TravelError.NOT_SUBSCRIBER;
        }
        if (user.getBags().getQuantityForRefId(source.getItemId()) < source.getItemQty()) {
            return TravelError.NOT_ENOUGH_ITEM;
        }
        return TravelError.NO_ERROR;
    }
    
    @Override
    protected void executeTravel(final BasicCharacterInfo user, final MapInteractiveElement sourceMachine, final long destinationId) {
        final TravelMachine machine = (TravelMachine)sourceMachine;
        final Collection<ClientInteractiveElementView> views = machine.getViews();
        for (final ClientInteractiveElementView view : views) {
            if (!(view instanceof WakfuClientInteractiveAnimatedElementSceneView)) {
                continue;
            }
            ((WakfuClientInteractiveAnimatedElementSceneView)view).addAnimationEndedListener(CannonTravelProvider.MESSAGE_LISTENER);
            break;
        }
        machine.setState((short)2);
        machine.notifyViews();
    }
    
    @Override
    public boolean canUse(final LocalPlayerCharacter player, final TravelMachine machine) {
        return this.checkTravel(player, machine, -1L) == TravelError.NO_ERROR;
    }
    
    @Override
    public TravelType getType() {
        return TravelType.CANNON;
    }
    
    @Override
    public String getOverHeadInfo(final TravelMachine travelMachine) {
        return WakfuTranslator.getInstance().getString(84, (int)travelMachine.getId(), new Object[0]);
    }
    
    @Nullable
    @Override
    public String getTravelCostLabel(final TravelMachine machine) {
        final CannonInfo info = TravelInfoManager.INSTANCE.getInfo(this.getType(), machine.getId());
        final AbstractReferenceItem ref = ReferenceItemManager.getInstance().getReferenceItem(info.getItemId());
        if (ref == null) {
            return null;
        }
        final LocalPlayerCharacter user = WakfuGameEntity.getInstance().getLocalPlayer();
        final TextWidgetFormater sb = new TextWidgetFormater();
        final TravelError travelError = this.checkTravel(user, machine, -1L);
        final boolean notEnoughSomething = travelError == TravelError.NOT_ENOUGH_ITEM || travelError == TravelError.NOT_ENOUGH_KAMA;
        sb.addColor((notEnoughSomething ? Color.RED : Color.GREEN).getRGBtoHex());
        sb.append("[").append(ref.getName()).append("]").append(" x").append(info.getItemQty());
        return sb.finishAndToString();
    }
    
    static {
        MESSAGE_LISTENER = new AnimationEndedListener() {
            @Override
            public void animationEnded(final AnimatedElement element) {
                final ClientMapInteractiveElement elt = ((WakfuClientInteractiveAnimatedElementSceneView)element).getInteractiveElement();
                elt.sendActionMessage(elt.getDefaultAction());
            }
        };
    }
}
