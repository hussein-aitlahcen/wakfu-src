package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.itemizable.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class KrosmozGameBoard extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private IEKrosmozGameBoardParameter m_boardParameters;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action != InteractiveElementAction.ACTIVATE) {
            return false;
        }
        final KrosmozGame game = KrosmozGame.byId(this.m_boardParameters.getGameId());
        if (WakfuGameCalendar.getInstance().getDate().before(game.getUnlockDate())) {
            return false;
        }
        SWFWrapper.INSTANCE.toggleDisplay(game);
        return true;
    }
    
    @Override
    public boolean isUsable() {
        return SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.KROSMOZ_GAMES_ENABLE) && super.isUsable();
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        final KrosmozGame game = KrosmozGame.byId(this.m_boardParameters.getGameId());
        if (WakfuGameCalendar.getInstance().getDate().before(game.getUnlockDate())) {
            return null;
        }
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        final KrosmozGame game = KrosmozGame.byId(this.m_boardParameters.getGameId());
        if (WakfuGameCalendar.getInstance().getDate().before(game.getUnlockDate())) {
            return InteractiveElementAction.EMPTY_ACTIONS;
        }
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        if (!SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.KROSMOZ_GAMES_ENABLE)) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final KrosmozGame game = KrosmozGame.byId(this.m_boardParameters.getGameId());
        if (WakfuGameCalendar.getInstance().getDate().before(game.getUnlockDate())) {
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final MRUInteractifMachine activateAction = MRUActions.INTERACTIF_ACTION.getMRUAction();
        if (!WakfuSWT.isInit()) {
            activateAction.setEnabled(false);
            activateAction.setErrorMsg(WakfuTranslator.getInstance().getString("krosmoz.gameBoard.systemRequirementsNotMet"));
        }
        return new AbstractMRUAction[] { activateAction };
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
        this.setOverHeadable(true);
        assert this.m_boardParameters == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_boardParameters = null;
    }
    
    @Override
    public boolean isBlockingMovements() {
        return true;
    }
    
    @Override
    public String getName() {
        if (this.m_boardParameters != null) {
            return WakfuTranslator.getInstance().getString(135, this.m_boardParameters.getId(), new Object[0]);
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
            KrosmozGameBoard.m_logger.error((Object)("[LD] La borne de krosmoz " + this.m_id + " doit avoir " + 1 + " param\u00e8tre"));
            return;
        }
        int parameterId;
        try {
            parameterId = Integer.valueOf(params[0]);
        }
        catch (NumberFormatException e) {
            KrosmozGameBoard.m_logger.error((Object)("[LD] La borne de krosmoz " + this.m_id + " a un parametre [" + Integer.valueOf(params[0]) + "] qui n'est pas du bon type (id attendu)"));
            return;
        }
        final IEKrosmozGameBoardParameter param = (IEKrosmozGameBoardParameter)IEParametersManager.INSTANCE.getParam(IETypes.KROSMOZ_GAME_BOARD, parameterId);
        if (param == null) {
            KrosmozGameBoard.m_logger.error((Object)("[LD] La borne de krosmoz " + this.m_id + " a un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_boardParameters = param;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
        KrosmozGameBoard.m_logger.info((Object)("[ON VIEW UPDATED] " + view));
    }
    
    @Override
    public void initializeActivationPattern() {
        super.initializeActivationPattern();
        this.m_hasToFinishOnIE = false;
    }
    
    @Override
    public ItemizableInfo getOrCreateItemizableInfo() {
        if (this.m_itemizableInfo == null) {
            this.m_itemizableInfo = new KrosmozGameBoardItemizableInfo(this);
        }
        return this.m_itemizableInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)KrosmozGameBoard.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            KrosmozGameBoard board;
            try {
                board = (KrosmozGameBoard)Factory.m_pool.borrowObject();
                board.setPool(Factory.m_pool);
            }
            catch (Exception e) {
                KrosmozGameBoard.m_logger.error((Object)"Erreur lors de l'extraction d'un Board du pool", (Throwable)e);
                board = new KrosmozGameBoard();
            }
            return board;
        }
        
        static {
            Factory.m_pool = new MonitoredPool(new ObjectFactory<KrosmozGameBoard>() {
                @Override
                public KrosmozGameBoard makeObject() {
                    return new KrosmozGameBoard();
                }
            });
        }
    }
}
