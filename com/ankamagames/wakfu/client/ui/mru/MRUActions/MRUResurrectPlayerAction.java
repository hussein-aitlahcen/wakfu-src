package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.death.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.translator.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.ui.mru.*;

public class MRUResurrectPlayerAction extends AbstractMRUAction implements MobileEndPathListener
{
    private static final byte ERROR_NO_ERROR = 0;
    private static final byte ERROR_NOT_ENOUGH_ITEM = 1;
    private static final byte ERROR_WRONG_LEVEL = 2;
    private static final byte ERROR_PVP_DIED = 3;
    private byte m_errorCode;
    private boolean m_canBeRaised;
    
    @Override
    public MRUActions tag() {
        return MRUActions.RESURRECT_PLAYER_ACTION;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        this.resurrect();
    }
    
    @Override
    public boolean isEnabled() {
        return this.m_canBeRaised && super.isEnabled();
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof PlayerCharacter)) {
            return false;
        }
        final PlayerCharacter player = (PlayerCharacter)this.m_source;
        final AbstractOccupation occ = player.getCurrentOccupation();
        if (occ == null || occ.getOccupationTypeId() != 4) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isDead();
    }
    
    @Override
    public void initFromSource(final Object source) {
        super.initFromSource(source);
        this.m_errorCode = 0;
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        final int itemQty = player.getBags().getQuantityForRefId(9247);
        if (itemQty < 1) {
            this.setEnabled(false);
            this.m_errorCode = 1;
            return;
        }
        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(9247);
        if (player.getLevel() < refItem.getLevel()) {
            this.setEnabled(false);
            this.m_errorCode = 2;
        }
        if (!(this.m_source instanceof PlayerCharacter)) {
            return;
        }
        final PlayerCharacter died = (PlayerCharacter)this.m_source;
        final AbstractOccupation occ = died.getCurrentOccupation();
        if (!(occ instanceof DeadOccupation)) {
            return;
        }
        if (!(this.m_canBeRaised = ((DeadOccupation)occ).canBeRaised())) {
            this.m_errorCode = 3;
        }
    }
    
    private void resurrect() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor localActor = localPlayer.getActor();
        final PlayerCharacter targetPlayer = (PlayerCharacter)this.m_source;
        final int distance = localPlayer.getPosition().getDistance(targetPlayer.getPosition());
        if (distance > 1) {
            localActor.addEndPositionListener(this);
            if (!localPlayer.moveTo(targetPlayer.getPosition(), true, false)) {
                localActor.removeEndPositionListener(this);
                ErrorsMessageTranslator.getInstance().pushMessage(2, 3, new Object[0]);
            }
            return;
        }
        final Direction8 direction = localActor.getWorldCoordinates().getDirectionTo(targetPlayer.getPosition());
        if (direction != null) {
            localActor.setDirection(direction);
        }
        final ActorResurrectPlayerRequestMessage message = new ActorResurrectPlayerRequestMessage(targetPlayer.getId());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(message);
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.resurrect();
    }
    
    @Override
    public String getTranslatorKey() {
        return "desc.mru.resurrect";
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(MRUResurrectPlayerAction.DEFAULT_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString(this.getTranslatorKey()));
        sb._b();
        if (this.m_errorCode != 0) {
            sb.newLine().openText().addColor(Color.RED.getRGBtoHex());
            final AbstractReferenceItem item = ReferenceItemManager.getInstance().getReferenceItem(9247);
            if (this.m_errorCode == 1) {
                sb.append(WakfuTranslator.getInstance().getString("action.error.resurrectionItemNotOwned", item.getName()));
            }
            else if (this.m_errorCode == 2) {
                sb.append(WakfuTranslator.getInstance().getString("collect.error.ItemNotUsable"));
            }
            else if (this.m_errorCode == 3) {
                sb.append(WakfuTranslator.getInstance().getString("error.pvp.diedByAgro"));
            }
        }
        return sb.finishAndToString();
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUResurrectPlayerAction();
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.HAND_BOTH.m_id;
    }
}
