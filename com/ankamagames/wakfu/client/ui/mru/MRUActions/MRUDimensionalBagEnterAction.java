package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class MRUDimensionalBagEnterAction extends AbstractMRUAction implements MobileEndPathListener
{
    private DimensionalBagInteractiveElement m_dimensionalBag;
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUDimensionalBagEnterAction();
    }
    
    @Override
    public boolean isRunnable() {
        if (!(this.m_source instanceof DimensionalBagInteractiveElement)) {
            return false;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(localPlayer.getInstanceId());
        return (worldInfo == null || worldInfo.getGrouptype() != GroupType.PARTY) && !localPlayer.isDead() && !localPlayer.isWaitingForResult() && !ClientTradeHelper.INSTANCE.isTradeRunning() && !localPlayer.isOnFight();
    }
    
    @Override
    public boolean isEnabled() {
        final DimensionalBagInteractiveElement dimensionalBagIE = (DimensionalBagInteractiveElement)this.m_source;
        return (!dimensionalBagIE.isLocked() || !AdminRightHelper.checkRights(WakfuGameEntity.getInstance().getLocalAccount().getAdminRights(), AdminRightHelper.NO_RIGHT)) && super.isEnabled() && this.isNationAllowed();
    }
    
    private boolean isNationAllowed() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getVisitingDimentionalBag() != null) {
            return true;
        }
        final Territory currentTerritory = localPlayer.getCurrentTerritory();
        if (currentTerritory != null) {
            final ProtectorBase protector = currentTerritory.getProtector();
            if (protector != null) {
                final CitizenComportment comportment = localPlayer.getCitizenComportment();
                if (!CitizenAuthorizationRules.getInstance().canEnterBag(comportment, protector.getCurrentNationId())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUDimensionalBagEnterAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
        player.cancelCurrentOccupation(false, false);
        this.m_dimensionalBag = (DimensionalBagInteractiveElement)this.m_source;
        final Point3 approachPoint = PointHelper.getClosestPointFrom(player.getPositionConst(), this.m_dimensionalBag.getApproachPoints());
        if (approachPoint == null) {
            return;
        }
        if (approachPoint.getDistance(player.getPositionConst()) == 0) {
            final int dx = this.m_dimensionalBag.getWorldCellX() - player.getWorldCellX();
            final int dy = this.m_dimensionalBag.getWorldCellY() - player.getWorldCellY();
            player.setDirection(Vector3i.getDirection8FromVector(dx, dy));
            final PSEnterRequestMessage enterRequestMessage = new PSEnterRequestMessage();
            enterRequestMessage.setWorldObjectId(this.m_dimensionalBag.getInfoProvider().getOwnerId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(enterRequestMessage);
            if (player.getCitizenComportment().getPvpState().isActive()) {
                PvpInteractionManager.INSTANCE.startInteraction(new PvpInteractionHandler() {
                    @Override
                    public void onFinish() {
                        final PSEnterRequestMessage enterRequestMessage = new PSEnterRequestMessage();
                        enterRequestMessage.setWorldObjectId(MRUDimensionalBagEnterAction.this.m_dimensionalBag.getInfoProvider().getOwnerId());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(enterRequestMessage);
                    }
                    
                    @Override
                    public void onCancel() {
                    }
                });
            }
        }
        else {
            player.getActor().addEndPositionListener(this);
            player.moveTo(approachPoint, false, true);
        }
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.DIMENSIONAL_BAG_ENTER_ACTION;
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        final int dx = this.m_dimensionalBag.getWorldCellX() - mobile.getWorldCellX();
        final int dy = this.m_dimensionalBag.getWorldCellY() - mobile.getWorldCellY();
        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
            mobile.setDirection(Vector3i.getDirection8FromVector(dx, dy));
            final PSEnterRequestMessage enterRequestMessage = new PSEnterRequestMessage();
            enterRequestMessage.setWorldObjectId(this.m_dimensionalBag.getInfoProvider().getOwnerId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(enterRequestMessage);
        }
    }
    
    @Override
    protected int getGFXId() {
        return MRUGfxConstants.BAG_DIMENSIONAL.m_id;
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.isEnabled() ? MRUDimensionalBagEnterAction.DEFAULT_TOOLTIP_COLOR : MRUDimensionalBagEnterAction.NOK_TOOLTIP_COLOR);
        sb.append(WakfuTranslator.getInstance().getString(this.getTranslatorKey()));
        sb._b();
        return sb.finishAndToString();
    }
    
    @Override
    public String getTranslatorKey() {
        final DimensionalBagInteractiveElement dimensionalBagIE = (DimensionalBagInteractiveElement)this.m_source;
        if (dimensionalBagIE.isLocked()) {
            return "bag.forbiddenLocked";
        }
        if (!this.isNationAllowed()) {
            return "bag.forbiddenInEnnemyTerritory";
        }
        return "desc.mru.bagEnterOther";
    }
}
