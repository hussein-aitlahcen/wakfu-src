package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class GuildMachine extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private static final int MRU_GFX_ID;
    private static final String TRANSLATION_KEY = "guild.machine";
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case ACTIVATE: {
                if (!WakfuGameEntity.getInstance().hasFrame(UIGuildCreatorFrame.getInstance())) {
                    UIGuildCreatorFrame.getInstance().setMachine(this);
                    WakfuGameEntity.getInstance().pushFrame(UIGuildCreatorFrame.getInstance());
                }
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.ACTIVATE;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        return new InteractiveElementAction[] { InteractiveElementAction.ACTIVATE };
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.setBlockingLineOfSight(true);
        this.setBlockingMovements(true);
        this.setOverHeadable(true);
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final AbstractMRUAction[] mRUActions = { null };
        final MRUGuildCreatorAction action = MRUActions.GUILD_CREATOR_ACTION.getMRUAction();
        action.setGfxId(GuildMachine.MRU_GFX_ID);
        mRUActions[0] = action;
        return mRUActions;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("guild.machine");
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildMachine.class);
        MRU_GFX_ID = MRUGfxConstants.GUILD.m_id;
    }
    
    public static class GuildMachineFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            GuildMachine machine;
            try {
                machine = (GuildMachine)GuildMachineFactory.m_pool.borrowObject();
                machine.setPool(GuildMachineFactory.m_pool);
            }
            catch (Exception e) {
                GuildMachine.m_logger.error((Object)"Erreur lors de l'extraction d'une GuildMachine du pool", (Throwable)e);
                machine = new GuildMachine();
            }
            return machine;
        }
        
        static {
            GuildMachineFactory.m_pool = new MonitoredPool(new ObjectFactory<GuildMachine>() {
                @Override
                public GuildMachine makeObject() {
                    return new GuildMachine();
                }
            });
        }
    }
}
