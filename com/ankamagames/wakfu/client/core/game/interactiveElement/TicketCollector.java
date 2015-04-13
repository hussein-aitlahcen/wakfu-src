package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class TicketCollector extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private int m_itemId;
    private String m_translatorKey;
    private int m_MRUGfxId;
    private GameInterval m_boatFrequency;
    private boolean m_locked;
    private final BinarSerialPart SHARED_DATAS;
    
    public TicketCollector() {
        super();
        this.m_translatorKey = "";
        this.SHARED_DATAS = new BinarSerialPart(1) {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                TicketCollector.this.m_locked = (buffer.get() == 1);
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => pas de s\u00e9rialisation");
            }
        };
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        if (action == InteractiveElementAction.ACTIVATE) {
            this.notifyViews();
            final LocalPlayerCharacter player = WakfuGameEntity.getInstance().getLocalPlayer();
            final Item item = (Item)player.getBags().getFirstItemFromInventory(this.m_itemId);
            if (item == null) {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("inventory.error.lackItem"), 3);
                return false;
            }
            if (this.m_locked) {
                ChatManager.getInstance().pushMessage(WakfuTranslator.getInstance().getString("boat.cantTravel"), 3);
                return false;
            }
            this.sendActionMessage(action);
        }
        return true;
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    public int getItemId() {
        return this.m_itemId;
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
        final AbstractMRUAction[] mRUActions = { null };
        final MRUInteractifMachine action = MRUActions.INTERACTIF_ACTION.getMRUAction();
        action.setGfxId(this.m_MRUGfxId);
        mRUActions[0] = action;
        return mRUActions;
    }
    
    @Override
    public short getMRUHeight() {
        return 60;
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString(this.m_translatorKey);
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        final String[] params = this.m_parameter.split(";");
        if (params.length != 9) {
            TicketCollector.m_logger.error((Object)("[LevelDesign] La controlleuse de ticket " + this.m_id + " doit avoir 3 param\u00e8tres : itemId, id translator, et id mruGfx"));
            return;
        }
        this.m_itemId = Integer.valueOf(params[0]);
        this.m_translatorKey = params[1];
        this.m_MRUGfxId = Integer.valueOf(params[2]);
        final short boatCapacity = Short.valueOf(params[3]);
        this.m_boatFrequency = new GameInterval(0, Integer.valueOf(params[4]), 0, 0);
        final GameInterval shippingDuration = new GameInterval(0, Integer.valueOf(params[5]), 0, 0);
        final int shippingScenario = Integer.valueOf(params[6]);
        final short boatInstance = Short.valueOf(params[7]);
        final int boatScenario = Integer.valueOf(params[8]);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_boatFrequency = null;
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
        this.m_itemId = 0;
        this.m_translatorKey = "";
        this.m_MRUGfxId = 0;
        assert this.m_boatFrequency == null;
        this.m_locked = false;
    }
    
    public GameIntervalConst getBoatFrequency() {
        return this.m_boatFrequency;
    }
    
    public void setLocked(final boolean locked) {
        this.m_locked = locked;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TicketCollector.class);
    }
    
    public static class TicketCollectorFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            TicketCollector table;
            try {
                table = (TicketCollector)TicketCollectorFactory.m_pool.borrowObject();
                table.setPool(TicketCollectorFactory.m_pool);
            }
            catch (Exception e) {
                TicketCollector.m_logger.error((Object)"Erreur lors de l'extraction d'une DistributionMachine du pool", (Throwable)e);
                table = new TicketCollector();
            }
            return table;
        }
        
        static {
            TicketCollectorFactory.m_pool = new MonitoredPool(new ObjectFactory<TicketCollector>() {
                @Override
                public TicketCollector makeObject() {
                    return new TicketCollector();
                }
            });
        }
    }
}
