package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class MRUInteractifMachine extends AbstractLawMRUAction implements MobileEndPathListener
{
    private static MobileEndPathListener m_pathListener;
    private int m_gfxId;
    private String m_mruKey;
    private WakfuClientMapInteractiveElement m_iElement;
    private Color m_tooltipColor;
    private String m_errorMsg;
    
    public MRUInteractifMachine() {
        super();
        this.m_tooltipColor = Color.WHITE;
        this.m_errorMsg = null;
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.INTERACTIF_ACTION;
    }
    
    protected InteractiveElementAction getUsableAction() {
        return this.m_iElement.getDefaultAction();
    }
    
    public void setTooltipColor(final Color tooltipColor) {
        this.m_tooltipColor = tooltipColor;
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        final CharacterActor characterActor = localPlayer.getActor();
        if (MRUInteractifMachine.m_pathListener != null) {
            characterActor.removeEndPositionListener(MRUInteractifMachine.m_pathListener);
        }
        characterActor.addEndPositionListener(MRUInteractifMachine.m_pathListener = this);
        final WakfuClientMapInteractiveElement interactiveElement = (WakfuClientMapInteractiveElement)this.m_source;
        this.m_iElement = interactiveElement;
        final int maskKey = characterActor.getMaskKey();
        final List<Point3> approachPoints = new ArrayList<Point3>(interactiveElement.getApproachPoints());
        for (int i = approachPoints.size() - 1; i >= 0; --i) {
            final Point3 point = approachPoints.get(i);
            final DisplayedScreenElement walkableElement = DisplayedScreenWorld.getInstance().getNearesetWalkableElement(point.getX(), point.getY(), point.getZ(), ElementFilter.NOT_EMPTY);
            if (walkableElement == null) {
                MRUInteractifMachine.m_logger.error((Object)("walkableElement null sur l'approchPoint=" + point));
            }
            else if (walkableElement.getMaskKey() != maskKey) {
                approachPoints.remove(i);
            }
        }
        if (!localPlayer.moveTo(false, true, approachPoints)) {
            localPlayer.getActor().removeEndPositionListener(MRUInteractifMachine.m_pathListener);
            this.tryToInteract(this.getUsableAction());
        }
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled() && ((WakfuClientMapInteractiveElement)this.m_source).checkSubscription();
    }
    
    @Override
    public boolean isRunnable() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return !localPlayer.isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.m_tooltipColor.getRGBtoHex());
        sb.append(WakfuTranslator.getInstance().getString(this.getTranslatorKey()));
        sb._b();
        if (!this.isEnabled()) {
            if (this.m_errorMsg != null && this.m_errorMsg.length() > 0) {
                sb.newLine().addColor(MRUInteractifMachine.NOK_TOOLTIP_COLOR).append(this.m_errorMsg);
            }
            if (!((WakfuClientMapInteractiveElement)this.m_source).checkSubscription()) {
                sb.newLine().addColor(MRUInteractifMachine.NOK_TOOLTIP_COLOR).append(WakfuTranslator.getInstance().getString("error.playerNotSubscribed"));
            }
        }
        return sb.finishAndToString();
    }
    
    @Override
    public String getTranslatorKey() {
        return (this.m_mruKey != null) ? this.m_mruKey : "ielt.use";
    }
    
    @Override
    public AbstractMRUAction getCopy() {
        return new MRUInteractifMachine();
    }
    
    public void setErrorMsg(final String errorMsg) {
        this.m_errorMsg = errorMsg;
    }
    
    @Override
    protected int getGFXId() {
        return this.m_gfxId;
    }
    
    public void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
    
    public void setTextKey(final String mruLabelKey) {
        this.m_mruKey = mruLabelKey;
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(MRUInteractifMachine.m_pathListener);
        if (this.m_iElement.isInApproachPoint(mobile.getWorldCoordinates())) {
            this.tryToInteract(this.getUsableAction());
        }
    }
    
    @Override
    public List<NationLaw> getTriggeredLaws() {
        return null;
    }
    
    @Override
    public List<NationLaw> getProbablyTriggeredLaws() {
        if (AbstractLawMRUAction.getCurrentNationAlignment() != NationAlignement.ALLIED) {
            return null;
        }
        final LocalPlayerCharacter citizen = WakfuGameEntity.getInstance().getLocalPlayer();
        final PutCollectorContentLawEvent event = new PutCollectorContentLawEvent(citizen);
        event.setMoneyAmount(Integer.MAX_VALUE);
        event.setElementId(((OccupationUsedElement)this.m_source).getId());
        return event.getTriggeringLaws();
    }
}
