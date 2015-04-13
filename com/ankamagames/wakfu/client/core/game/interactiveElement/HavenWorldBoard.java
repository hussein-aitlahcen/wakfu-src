package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;
import com.ankamagames.wakfu.client.core.world.dynamicElement.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.*;
import com.ankamagames.wakfu.common.game.interactiveElements.param.type.*;
import com.ankamagames.wakfu.client.ui.mru.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.havenWorld.agt_like.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.*;
import com.ankamagames.wakfu.common.game.havenWorld.auction.*;
import com.ankamagames.wakfu.common.configuration.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.havenWorld.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.*;

public class HavenWorldBoard extends WakfuClientMapInteractiveElement
{
    private static final Logger m_logger;
    private static final short STATE_FOR_SALE = 1;
    private static final short STATE_SOLD = 2;
    private static final short LOCKED = 3;
    private IEHavenWorldBoardParameter m_param;
    private HavenWorldTopology m_havenWorld;
    private HavenWorldMini m_worldMini;
    private GuildInfo m_guildInfo;
    private GameDate m_auctionStartDate;
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("Ne devrait pas passer par ici");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final boolean noHavenWorld = buffer.get() == 0;
                if (noHavenWorld) {
                    HavenWorldBoard.this.m_havenWorld = null;
                    return;
                }
                if (HavenWorldBoard.this.m_param == null) {
                    return;
                }
                HavenWorldManager.loadLibraries();
                final int rawTopologySize = buffer.getInt();
                final byte[] rawTopology = new byte[rawTopologySize];
                buffer.get(rawTopology);
                final int rawBuildingSize = buffer.getInt();
                final byte[] rawBuilding = new byte[rawBuildingSize];
                buffer.get(rawBuilding);
                HavenWorldBoard.this.m_havenWorld = HavenWorldTopology.createFake(rawTopology, rawBuilding);
                final boolean hasStartDate = buffer.get() == 1;
                if (hasStartDate) {
                    HavenWorldBoard.this.m_auctionStartDate = GameDate.fromLong(buffer.getLong());
                }
                HavenWorldBoard.this.m_guildInfo = GuildInfo.decode(buffer);
                HavenWorldBoard.this.forceReinitEntryGuildDynamicElements();
            }
        };
    }
    
    private void forceReinitEntryGuildDynamicElements() {
        final TObjectProcedure<DynamicElement> procedure = new TObjectProcedure<DynamicElement>() {
            @Override
            public boolean execute(final DynamicElement object) {
                if (object.getTypeProvider() instanceof HavenWorldEntryGuildDynamicElementTypeProvider) {
                    final HavenWorldEntryGuildDynamicElementTypeProvider provider = (HavenWorldEntryGuildDynamicElementTypeProvider)object.getTypeProvider();
                    if (provider.getAttachedHavenWorldBoardId() == HavenWorldBoard.this.getId()) {
                        provider.setAttachedHavenWorldBoard(HavenWorldBoard.this);
                        object.initialize();
                    }
                }
                return true;
            }
        };
        final LocalPartition partition = ((AbstractLocalPartitionManager<LocalPartition>)LocalPartitionManager.getInstance()).getCurrentPartition();
        final LocalPartition[] layout = partition.getLayout();
        if (layout != null) {
            for (final LocalPartition p : layout) {
                p.forEachDynamicElement(procedure);
            }
        }
    }
    
    private void createHavenWorldMini() {
        if (this.m_havenWorld == null) {
            return;
        }
        if (this.m_worldMini != null) {
            this.m_worldMini.clear(DisplayedScreenWorld.getInstance());
        }
        (this.m_worldMini = new HavenWorldMini(this.m_havenWorld, 4, 4)).apply(DisplayedScreenWorld.getInstance(), this.m_param.getMiniOriginX(), this.m_param.getMiniOriginY(), this.m_param.getMiniOriginZ());
    }
    
    @Override
    protected void updateView(final ClientInteractiveElementView view) {
        super.updateView(view);
        this.createHavenWorldMini();
    }
    
    @Override
    public void onViewUpdated(final ClientInteractiveElementView view) {
    }
    
    @Override
    public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        this.runScript(action);
        if (action == InteractiveElementAction.READ || action == InteractiveElementAction.REGISTER) {
            WakfuGameEntity.getInstance().pushFrame(new Frame(this));
        }
        this.sendActionMessage(action);
        return true;
    }
    
    public InteractiveElementAction getInteractiveDefaultAction() {
        return InteractiveElementAction.READ;
    }
    
    @Override
    public InteractiveElementAction[] getInteractiveUsableActions() {
        if (this.getState() == 1) {
            return new InteractiveElementAction[] { InteractiveElementAction.READ, InteractiveElementAction.REGISTER };
        }
        if (this.getState() == 2) {
            return new InteractiveElementAction[] { InteractiveElementAction.READ, InteractiveElementAction.ENTER };
        }
        HavenWorldBoard.m_logger.error((Object)("Etat inconnu sur le HavenWorldBoard " + this.getId() + " state=" + this.getState()));
        return new InteractiveElementAction[] { InteractiveElementAction.READ };
    }
    
    @Override
    public void initializeWithParameter() {
        try {
            this.m_param = (IEHavenWorldBoardParameter)IEParametersManager.INSTANCE.getParam(IETypes.HAVEN_WORLD_BOARD, Integer.parseInt(this.m_parameter));
        }
        catch (NumberFormatException e) {
            HavenWorldBoard.m_logger.error((Object)("Erreur de param\u00e9trage d'IE " + this));
        }
        finally {
            if (this.m_param == null) {
                this.m_param = IEHavenWorldBoardParameter.FAKE_PARAM;
            }
        }
    }
    
    @Override
    public AbstractMRUAction[] getInteractiveMRUActions() {
        final MRUGenericInteractiveAction read = MRUActions.HAVEN_WORLD_BOARD_READ.getMRUAction();
        read.setGfxId(MRUGfxConstants.BOOK.m_id);
        read.setActionToExecute(InteractiveElementAction.READ);
        final int havenWorldId = this.m_param.getHavenWorldDefinitionId();
        AbstractMRUAction[] mru;
        if (this.getState() == 1) {
            final MRUGenericInteractiveAction buy = new MRUHavenWorldBoardAction("buy", MRUGfxConstants.PADLOCK_OPEN.m_id, havenWorldId, this.m_auctionStartDate);
            buy.setActionToExecute(InteractiveElementAction.REGISTER);
            mru = new AbstractMRUAction[] { read, buy };
        }
        else if (this.getState() == 2) {
            final MRUGenericInteractiveAction tp = MRUActions.HAVEN_WORLD_BOARD_ENTER.getMRUAction();
            tp.setGfxId(MRUGfxConstants.BAG_DIMENSIONAL.m_id);
            tp.setActionToExecute(InteractiveElementAction.ENTER);
            mru = new AbstractMRUAction[] { read, tp };
        }
        else if (this.getState() == 3) {
            mru = new AbstractMRUAction[] { read };
        }
        else {
            HavenWorldBoard.m_logger.error((Object)("\u00c9tat inconnu sur le HavenWorldBoard " + this.getId() + " state=" + this.getState()));
            mru = new AbstractMRUAction[] { read };
        }
        return mru;
    }
    
    @Override
    public String getName() {
        final int havenWorldId = this.m_param.getHavenWorldDefinitionId();
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(WakfuTranslator.getInstance().getString("havenWorldBoard")).newLine();
        final HavenWorldDefinition def = HavenWorldDefinitionManager.INSTANCE.getWorld(havenWorldId);
        if (def != null) {
            sb.append(WakfuTranslator.getInstance().getString(77, def.getWorldInstanceId(), new Object[0]));
        }
        final HavenWorldAuctionDefinition definition = HavenWorldAuctionDefinitionManager.INSTANCE.getDefinition(havenWorldId);
        final GameDateConst date = WakfuGameCalendar.getInstance().getDate();
        final boolean auctionRunning = definition != null && definition.getStartDate().before(date) && definition.getEndDate().after(date);
        if (this.getState() == 1 && auctionRunning) {
            sb.newLine().append(WakfuTranslator.getInstance().getString("havenWorldBoardForSale"));
        }
        if (this.getState() == 3) {
            sb.newLine().append(WakfuTranslator.getInstance().getString("havenWorldLocked"));
        }
        return sb.finishAndToString();
    }
    
    public IEHavenWorldBoardParameter getParam() {
        return this.m_param;
    }
    
    @Override
    public boolean isUsable() {
        return SystemConfiguration.INSTANCE.getBooleanValue(SystemConfigurationType.HAVEN_WORLDS_ENABLE);
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_param = null;
        this.setOverHeadable(true);
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_param = null;
    }
    
    @Override
    public void onDeSpawn() {
        super.onDeSpawn();
        if (this.m_worldMini != null) {
            this.m_worldMini.clear(DisplayedScreenWorld.getInstance());
        }
        this.m_worldMini = null;
    }
    
    @Override
    public String toString() {
        return "HavenWorldBoard{m_param=" + this.m_param + ", m_havenWorld=" + this.m_havenWorld + ", m_worldMini=" + this.m_worldMini + '}';
    }
    
    public GuildInfo getGuildInfo() {
        return this.m_guildInfo;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldBoard.class);
    }
    
    public static class Factory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        public static final MonitoredPool POOL;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            HavenWorldBoard ie;
            try {
                ie = (HavenWorldBoard)Factory.POOL.borrowObject();
                ie.setPool(Factory.POOL);
            }
            catch (Exception e) {
                HavenWorldBoard.m_logger.error((Object)"Erreur lors de l'extraction d'une CharacterStatue du pool", (Throwable)e);
                ie = new HavenWorldBoard();
            }
            return ie;
        }
        
        static {
            POOL = new MonitoredPool(new ObjectFactory<HavenWorldBoard>() {
                @Override
                public HavenWorldBoard makeObject() {
                    return new HavenWorldBoard();
                }
            });
        }
    }
    
    private static class Frame implements MessageFrame
    {
        private final HavenWorldBoard m_board;
        
        Frame(final HavenWorldBoard board) {
            super();
            this.m_board = board;
        }
        
        @Override
        public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        }
        
        @Override
        public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        }
        
        @Override
        public boolean onMessage(final Message message) {
            switch (message.getId()) {
                case 20090: {
                    this.havenWorldBoardResult((HavenWorldInfoResult)message);
                    return false;
                }
                case 20094: {
                    this.havenWorldAuctionResult((HavenWorldAuctionInfoResultMessage)message);
                    return false;
                }
                default: {
                    return true;
                }
            }
        }
        
        private void havenWorldAuctionResult(final HavenWorldAuctionInfoResultMessage message) {
            final UIHavenWorldBidFrame uiFrame = UIHavenWorldBidFrame.getInstance();
            final HavenWorldDefinition def = HavenWorldDefinitionManager.INSTANCE.getWorld(this.m_board.getParam().getHavenWorldDefinitionId());
            final HavenWorldAuctionDefinition definition = HavenWorldAuctionDefinitionManager.INSTANCE.getDefinition(this.m_board.getParam().getHavenWorldDefinitionId());
            final HavenWorldAuctionView auctionView = new HavenWorldAuctionView(message.getGuildId(), message.getGuildName(), message.getBidValue(), def.getId(), message.getStartDate(), definition);
            uiFrame.setAuctionView(auctionView);
            WakfuGameEntity.getInstance().pushFrame(uiFrame);
            WakfuGameEntity.getInstance().removeFrame(this);
        }
        
        private void havenWorldBoardResult(final HavenWorldInfoResult message) {
            final UIHavenWorldPanelFrame uiFrame = UIHavenWorldPanelFrame.getInstance();
            final HavenWorldDefinition def = HavenWorldDefinitionManager.INSTANCE.getWorld(this.m_board.getParam().getHavenWorldDefinitionId());
            final ArrayList<Building> buildings = message.getBuildings();
            HavenWorldViewManager.INSTANCE.refreshElements(buildings);
            final HavenWorldView havenWorldView = new HavenWorldView(def.getWorldInstanceId(), message.getGuildName(), buildings, message.getGuildRank(), message.getConquestRank());
            uiFrame.setWorldView(havenWorldView);
            WakfuGameEntity.getInstance().pushFrame(uiFrame);
            WakfuGameEntity.getInstance().removeFrame(this);
        }
        
        @Override
        public long getId() {
            return 1L;
        }
        
        @Override
        public void setId(final long id) {
        }
        
        @Override
        public String toString() {
            return "Frame{m_board=" + this.m_board + '}';
        }
    }
}
