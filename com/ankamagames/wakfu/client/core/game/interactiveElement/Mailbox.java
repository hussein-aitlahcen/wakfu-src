package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.mailbox.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class Mailbox extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private static final int MRU_GFX = 31;
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        switch (action) {
            case ACTIVATE: {
                Mailbox.m_logger.info((Object)"[MAILBOX] Activation d'une mailbox");
                final SendMailMessage msg = new SendMailMessage();
                msg.setReceiverName(WakfuGameEntity.getInstance().getLocalPlayer().getName());
                msg.setTitle("Test title");
                msg.setMessage("Yeah RastaPouet ... test de message de ouf ^^\r\n!!!Vive les barbus!!!");
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
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
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(31);
        mRUActions[0] = action;
        return mRUActions;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("mailbox.name");
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)Mailbox.class);
    }
    
    public static class MailboxFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            Mailbox mailbox;
            try {
                mailbox = (Mailbox)MailboxFactory.m_pool.borrowObject();
                mailbox.setPool(MailboxFactory.m_pool);
            }
            catch (Exception e) {
                Mailbox.m_logger.error((Object)"Erreur lors de l'extraction d'une Mailbox du pool", (Throwable)e);
                mailbox = new Mailbox();
            }
            return mailbox;
        }
        
        static {
            MailboxFactory.m_pool = new MonitoredPool(new ObjectFactory<Mailbox>() {
                @Override
                public Mailbox makeObject() {
                    return new Mailbox();
                }
            });
        }
    }
}
