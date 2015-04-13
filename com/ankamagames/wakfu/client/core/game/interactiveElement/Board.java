package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Board extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private IEBoardParameter m_boardParameters;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return null;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return InteractiveElementAction.EMPTY_ACTIONS;
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        return AbstractMRUAction.EMPTY_ARRAY;
    }
    
    @Override
    public byte getHeight() {
        return 4;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
        assert this.m_boardParameters == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_boardParameters = null;
    }
    
    @Override
    public String getName() {
        if (this.m_boardParameters != null) {
            return WakfuTranslator.getInstance().getString(78, this.m_boardParameters.getId(), new Object[0]);
        }
        return "#ERROR#";
    }
    
    @Override
    public int getOverheadDelayPreference() {
        return 0;
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        this.m_state = 1;
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            Board.m_logger.error((Object)("[LD] Le panneau " + this.m_id + " doit avoir " + 1 + " param\u00e8tre"));
            return;
        }
        int parameterId;
        try {
            parameterId = Integer.valueOf(params[0]);
        }
        catch (NumberFormatException e) {
            Board.m_logger.error((Object)("[LD] Le panneau " + this.m_id + " a un parametre [" + Integer.valueOf(params[0]) + "] qui n'est pas du bon type (id attendu)"));
            return;
        }
        final IEBoardParameter param = (IEBoardParameter)IEParametersManager.INSTANCE.getParam(IETypes.BOARD, parameterId);
        if (param == null) {
            Board.m_logger.error((Object)("[LD] Le panneau " + this.m_id + " a un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_boardParameters = param;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        Board.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public void initializeActivationPattern() {
        super.initializeActivationPattern();
        this.m_hasToFinishOnIE = false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)Board.class);
    }
    
    public static class BoardFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Board board;
            try {
                board = (Board)BoardFactory.m_pool.borrowObject();
                board.setPool(BoardFactory.m_pool);
            }
            catch (Exception e) {
                Board.m_logger.error((Object)"Erreur lors de l'extraction d'un Board du pool", (Throwable)e);
                board = new Board();
            }
            return board;
        }
        
        static {
            BoardFactory.m_pool = new MonitoredPool(new ObjectFactory<Board>() {
                @Override
                public Board makeObject() {
                    return new Board();
                }
            });
        }
    }
}
