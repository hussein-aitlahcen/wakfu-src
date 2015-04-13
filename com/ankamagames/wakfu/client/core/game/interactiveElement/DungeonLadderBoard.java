package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DungeonLadderBoard extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        this.sendActionMessage(action);
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ASK_INFORMATIONS;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ASK_INFORMATIONS };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final ActionVisual visual = ActionVisualManager.getInstance().get(83);
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(visual.getMruGfx());
        action.setTextKey("desc.mru." + visual.getMruLabelKey());
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public short getMRUHeight() {
        return 60;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("ie.ladderBoard");
    }
    
    @Override
    public void initializeWithParameter() {
        final String[] params = this.m_parameter.split(";");
        if (params.length != this.getParamCount()) {
            DungeonLadderBoard.m_logger.error((Object)("[LevelDesign] La DungeonLadderBoard " + this.m_id + " doit avoir " + this.getParamCount() + " param\u00e8tres : instanceId"));
            return;
        }
        super.initializeWithParameter();
    }
    
    protected int getParamCount() {
        return 1;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_state = 1;
        this.setVisible(true);
        this.setBlockingLineOfSight(false);
        this.setBlockingMovements(true);
        this.m_overHeadable = true;
        this.m_selectable = true;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DungeonLadderBoard.class);
    }
    
    public static class DungeonLadderBoardFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            DungeonLadderBoard table;
            try {
                table = (DungeonLadderBoard)DungeonLadderBoardFactory.m_pool.borrowObject();
                table.setPool(DungeonLadderBoardFactory.m_pool);
            }
            catch (Exception e) {
                DungeonLadderBoard.m_logger.error((Object)"Erreur lors de l'extraction d'une DungeonLadderBoard du pool", (Throwable)e);
                table = new DungeonLadderBoard();
            }
            return table;
        }
        
        static {
            m_pool = new MonitoredPool(new ObjectFactory<DungeonLadderBoard>() {
                @Override
                public DungeonLadderBoard makeObject() {
                    return new DungeonLadderBoard();
                }
            });
        }
    }
}
