package com.ankamagames.wakfu.client.ui.mru.MRUActions;

import java.util.*;
import com.ankamagames.wakfu.common.game.nation.Laws.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.nation.Laws.impl.events.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;

public class MRUGenericInteractiveAction extends AbstractLawMRUAction implements MobileEndPathListener
{
    protected String m_name;
    protected int m_gfxId;
    private InteractiveElementAction m_actionToExecute;
    
    public MRUGenericInteractiveAction() {
        super();
    }
    
    private MRUGenericInteractiveAction(final String name, final int gfxId) {
        super();
        this.m_name = name;
        this.m_gfxId = gfxId;
    }
    
    @Override
    public MRUGenericInteractiveAction getCopy() {
        return new MRUGenericInteractiveAction(this.m_name, this.m_gfxId);
    }
    
    @Override
    public String getTranslatorKey() {
        return this.m_name;
    }
    
    @Override
    public List<NationLaw> getTriggeredLaws() {
        if (AbstractLawMRUAction.getCurrentNationAlignment() != NationAlignement.ALLIED) {
            return null;
        }
        final LocalPlayerCharacter citizen = WakfuGameEntity.getInstance().getLocalPlayer();
        final UseInteractiveElementsLawEvent event = new UseInteractiveElementsLawEvent(citizen);
        event.setElementId(((OccupationUsedElement)this.m_source).getId());
        return event.getTriggeringLaws();
    }
    
    @Override
    public List<NationLaw> getProbablyTriggeredLaws() {
        return null;
    }
    
    @Override
    public boolean isRunnable() {
        return !WakfuGameEntity.getInstance().getLocalPlayer().isOnFight() && !ClientTradeHelper.INSTANCE.isTradeRunning();
    }
    
    @Override
    public void run() {
        if (!this.isRunnable()) {
            MRUGenericInteractiveAction.m_logger.error((Object)("Tentative de lancement de l'action '" + this.tag().getEnumLabel() + "' alors que isRunnable retourne que l'action est impossible"));
            return;
        }
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (!localPlayer.cancelCurrentOccupation(false, true)) {
            return;
        }
        localPlayer.getActor().addEndPositionListener(this);
        final ClientMapInteractiveElement interactiveElement = (ClientMapInteractiveElement)this.m_source;
        if (!localPlayer.moveTo(false, true, interactiveElement.getApproachPoints())) {
            localPlayer.getActor().removeEndPositionListener(this);
            this.tryToInteract();
        }
    }
    
    private void tryToInteract() {
        this.tryToInteract(this.m_actionToExecute);
    }
    
    @Override
    public MRUActions tag() {
        return MRUActions.GENERIC_INTERACTIVE_ACTION;
    }
    
    @Override
    public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
        mobile.removeEndPositionListener(this);
        this.tryToInteract();
    }
    
    @Override
    protected int getGFXId() {
        return this.m_gfxId;
    }
    
    @Override
    public boolean isEnabled() {
        return super.isEnabled() && this.hasPermission();
    }
    
    public boolean hasPermission() {
        return ((WakfuClientMapInteractiveElement)this.m_source).checkSubscription();
    }
    
    @Override
    public String getLabel() {
        final TextWidgetFormater sb = new TextWidgetFormater().b().addColor(this.isEnabled() ? MRUGenericInteractiveAction.DEFAULT_TOOLTIP_COLOR : MRUGenericInteractiveAction.NOK_TOOLTIP_COLOR);
        final String translatorKey = this.hasPermission() ? this.m_name : "error.playerNotSubscribed";
        sb.append(WakfuTranslator.getInstance().getString(translatorKey))._b();
        return sb.finishAndToString();
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    public void setGfxId(final int gfxId) {
        this.m_gfxId = gfxId;
    }
    
    public void setActionToExecute(final InteractiveElementAction actionToExecute) {
        this.m_actionToExecute = actionToExecute;
    }
    
    @Override
    public String toString() {
        return "MRUGenericInteractiveAction{m_name='" + this.m_name + '\'' + ", m_gfxId=" + this.m_gfxId + ", m_actionToExecute=" + this.m_actionToExecute + '}';
    }
}
