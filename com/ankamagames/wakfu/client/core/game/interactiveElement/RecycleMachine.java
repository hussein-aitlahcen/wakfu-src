package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class RecycleMachine extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    protected IERecycleMachineParameter m_recycleParameters;
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        return false;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.RECYCLE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.RECYCLE };
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final AbstractMRUAction[] mRUActions = new AbstractMRUAction[3];
        final AbstractMRUAction recycleAction = MRUActions.RECYCLE.getMRUAction();
        final MRUMergeGemAction mergePowder = MRUActions.MERGE_GEM.getMRUAction();
        mergePowder.setPowder(true);
        final MRUMergeGemAction mergeGem = MRUActions.MERGE_GEM.getMRUAction();
        mergeGem.setPowder(false);
        mRUActions[0] = recycleAction;
        mRUActions[1] = mergePowder;
        mRUActions[2] = mergeGem;
        return mRUActions;
    }
    
    @Override
    public short getMRUHeight() {
        return 60;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(110, this.m_recycleParameters.getId(), new Object[0]);
    }
    
    @Override
    public void initializeWithParameter() {
        if (StringUtils.isEmptyOrNull(this.m_parameter)) {
            this.m_recycleParameters = null;
            return;
        }
        final String[] params = this.m_parameter.split(";");
        if (params.length != 1) {
            RecycleMachine.m_logger.error((Object)("[LD] L'IE de Stool " + this.m_id + " doit avoir 1 param\u00e8tre"));
            return;
        }
        final IERecycleMachineParameter param = (IERecycleMachineParameter)IEParametersManager.INSTANCE.getParam(IETypes.RECYCLE_MACHINE, Integer.valueOf(params[0]));
        if (param == null) {
            RecycleMachine.m_logger.error((Object)("[LD] L'IE de Recycle " + this.m_id + " \u00e0 un parametre [" + Integer.valueOf(params[0]) + "] qui ne correspond a rien dans les Admins"));
            return;
        }
        this.m_recycleParameters = param;
    }
    
    protected int getParamCount() {
        return 1;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_recycleParameters = null;
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
        assert this.m_recycleParameters == null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)RecycleMachine.class);
    }
    
    public static class RecycleMachineFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            RecycleMachine table;
            try {
                table = (RecycleMachine)RecycleMachineFactory.m_pool.borrowObject();
                table.setPool(RecycleMachineFactory.m_pool);
            }
            catch (Exception e) {
                RecycleMachine.m_logger.error((Object)"Erreur lors de l'extraction d'une RecycleMachine du pool", (Throwable)e);
                table = new RecycleMachine(null);
            }
            return table;
        }
        
        static {
            RecycleMachineFactory.m_pool = new MonitoredPool(new ObjectFactory<RecycleMachine>() {
                @Override
                public RecycleMachine makeObject() {
                    return new RecycleMachine(null);
                }
            });
        }
    }
}
