package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.chaos.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class BackgroundDisplay extends WakfuClientMapInteractiveElement implements ChaosInteractiveElement
{
    private static final Logger m_logger;
    public static final short STATE_NORMAL = 1;
    protected IEBackgroundDisplayParameter m_backgroundParameters;
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setState((short)1);
        this.setVisible(true);
        this.setBlockingMovements(true);
        this.setBlockingLineOfSight(true);
        assert this.m_backgroundParameters == null;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_backgroundParameters = null;
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (action == InteractiveElementAction.ACTIVATE) {
            final UIBackgroundDisplayFrame displayFrame = UIBackgroundDisplayFrame.getInstance();
            displayFrame.loadBackgroundDisplay(this.m_backgroundParameters.getBackgroundDisplayId());
            WakfuGameEntity.getInstance().pushFrame(displayFrame);
            this.sendActionMessage(action);
            return true;
        }
        return false;
    }
    
    @Override
    public void initializeWithParameter() {
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            BackgroundDisplay.m_logger.error((Object)("[LD] L'IE de BackgroundDisplay " + this.m_id + " doit avoir " + 1 + " param\u00e8tre"));
            return;
        }
        final IEBackgroundDisplayParameter param = (IEBackgroundDisplayParameter)IEParametersManager.INSTANCE.getParam(IETypes.BACKGROUND_DISPLAY, Integer.valueOf(params[0]));
        if (param == null) {
            BackgroundDisplay.m_logger.error((Object)("[LD] L'IE de BackgroundDisplay " + this.m_id + " \u00e0 un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_backgroundParameters = param;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final ActionVisual visual = ActionVisualManager.getInstance().get(this.m_backgroundParameters.getVisualId());
        if (visual == null) {
            BackgroundDisplay.m_logger.error((Object)("[LD] Impossible de trouver le visuel " + this.m_backgroundParameters.getVisualId() + " pour l'IE " + this.m_id), (Throwable)new IllegalArgumentException());
            return AbstractMRUAction.EMPTY_ARRAY;
        }
        final MRUBackgroundDisplayAction action = MRUActions.BACKGROUND_DISPLAY_ACTION.getMRUAction();
        action.setGfxId(visual.getMruGfx());
        action.setName("desc.mru." + visual.getMruLabelKey());
        action.setActionToExecute(this.getDefaultAction());
        return new AbstractMRUAction[] { action };
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(79, this.m_backgroundParameters.getId(), new Object[0]);
    }
    
    @Override
    public ChaosIEParameter getChaosIEParameter() {
        return this.m_backgroundParameters;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BackgroundDisplay.class);
    }
    
    public static class BackgroundDisplayFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            BackgroundDisplay element;
            try {
                element = (BackgroundDisplay)BackgroundDisplayFactory.m_pool.borrowObject();
                element.setPool(BackgroundDisplayFactory.m_pool);
            }
            catch (Exception e) {
                BackgroundDisplay.m_logger.error((Object)("Erreur lors de l'extraction d'un " + BackgroundDisplay.class.getName() + " du pool"), (Throwable)e);
                element = new BackgroundDisplay();
            }
            return element;
        }
        
        static {
            BackgroundDisplayFactory.m_pool = new MonitoredPool(new ObjectFactory<BackgroundDisplay>() {
                @Override
                public BackgroundDisplay makeObject() {
                    return new BackgroundDisplay();
                }
            });
        }
    }
}
