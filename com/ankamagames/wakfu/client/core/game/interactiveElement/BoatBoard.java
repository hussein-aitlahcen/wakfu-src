package com.ankamagames.wakfu.client.core.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.bubble.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class BoatBoard extends Board
{
    private static final Logger m_logger;
    private InteractiveBubble m_bubble;
    private int m_collectorId;
    private boolean m_boatDocked;
    private GameDate m_nextArrival;
    private GameDate m_nextDeparture;
    private final BinarSerialPart SHARED_DATAS;
    private Runnable m_timer;
    
    public BoatBoard() {
        super();
        this.SHARED_DATAS = new BinarSerialPart(15) {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                BoatBoard.this.m_nextArrival = new GameDate(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getShort());
                BoatBoard.this.m_nextDeparture = new GameDate(buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.get(), buffer.getShort());
                BoatBoard.this.m_boatDocked = (buffer.get() == 0);
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => pas de s\u00e9rialisation");
            }
        };
    }
    
    @Override
    protected BinarSerialPart getSynchronizationSpecificPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_bubble = null;
        this.m_nextArrival = null;
        this.m_nextDeparture = null;
        if (this.m_timer != null) {
            ProcessScheduler.getInstance().remove(this.m_timer);
            this.m_timer = null;
        }
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_collectorId = 0;
        this.m_boatDocked = false;
        assert this.m_bubble == null;
        assert this.m_nextArrival == null;
        assert this.m_nextDeparture == null;
        assert this.m_timer == null;
    }
    
    protected void showBubble() {
        final String bubbleId = "interactiveBubbleDialog" + AdviserManager.getInstance().getNewUniqueId();
        this.m_bubble = (InteractiveBubble)Xulor.getInstance().load(bubbleId, Dialogs.getDialogPath("interactiveBubbleDialog2"), 256L, (short)30000);
        final StaticLayoutData sld = new StaticLayoutData();
        sld.onCheckOut();
        sld.setAlign(Alignment17.CENTER);
        this.m_bubble.add(sld);
        this.m_bubble.setVisible(false);
        this.m_bubble.setForcedDisplaySpark(false);
        this.m_bubble.setUseTargetPositionning(false);
        this.m_bubble.addEventListener(Events.MOUSE_CLICKED, new MouseClickedListener() {
            @Override
            public boolean run(final Event event) {
                ProcessScheduler.getInstance().remove(BoatBoard.this.m_timer);
                Xulor.getInstance().unload(bubbleId);
                MasterRootContainer.getInstance().getLayeredContainer().removeWidget(BoatBoard.this.m_bubble);
                BoatBoard.this.m_timer = null;
                return true;
            }
        }, false);
        this.m_timer = new Runnable() {
            @Override
            public void run() {
                BoatBoard.this.m_bubble.setText(BoatBoard.this.computeBubbleText());
            }
        };
        ProcessScheduler.getInstance().scheduleAndInvokeNow(this.m_timer, 1000L);
        this.m_bubble.show();
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("ie.boatBoard.small." + this.m_id);
    }
    
    private String computeBubbleText() {
        final GameDateConst current = WakfuGameCalendar.getInstance().getDate();
        final GameInterval arrival = current.timeTo(this.m_nextArrival);
        final GameInterval departure = current.timeTo(this.m_nextDeparture);
        final String arrivalTanslator = WakfuTranslator.getInstance().getString("boatBoard.nextArrival", this.getDisplayDate(this.m_nextArrival), this.getDisplayInterval(arrival));
        final String departureTanslator = WakfuTranslator.getInstance().getString("boatBoard.nextDeparture", this.getDisplayDate(this.m_nextDeparture), this.getDisplayInterval(departure));
        final String boat = WakfuTranslator.getInstance().getString(this.m_boatDocked ? "boatBoard.boatDocked" : "boatBoard.boatLeaved");
        String firstParam;
        String secondParam;
        if (this.m_boatDocked) {
            firstParam = departureTanslator;
            secondParam = arrivalTanslator;
        }
        else {
            firstParam = arrivalTanslator;
            secondParam = departureTanslator;
        }
        return WakfuTranslator.getInstance().getString("ie.boatBoard.large." + this.m_id, this.getDisplayDate(current), boat, firstParam, secondParam);
    }
    
    private String getDisplayInterval(final GameIntervalConst interval) {
        final StringBuilder string = new StringBuilder("");
        if (interval.getHours() != 0) {
            string.append(interval.getHours()).append("H");
        }
        if (interval.getMinutes() != 0) {
            string.append(interval.getMinutes()).append("m");
        }
        return string.append(interval.getSeconds()).append("s").toString();
    }
    
    private String getDisplayDate(final GameDateConst date) {
        final StringBuilder string = new StringBuilder("");
        if (date.getHours() != 0) {
            string.append(date.getHours()).append("H");
        }
        if (date.getMinutes() != 0) {
            string.append(date.getMinutes()).append("m");
        }
        return string.append(date.getSeconds()).append("s").toString();
    }
    
    public int getCollectorId() {
        return this.m_collectorId;
    }
    
    @Override
    public void initializeWithParameter() {
        super.initializeWithParameter();
        final String[] params = this.m_parameter.split(";");
        this.m_collectorId = Integer.valueOf(params[1]);
    }
    
    public void updateNextArrival(final GameIntervalConst duration) {
        this.m_nextArrival.add(duration);
    }
    
    public void undateNextDeparture(final GameIntervalConst duration) {
        this.m_nextDeparture.add(duration);
    }
    
    public GameDateConst getNextArrival() {
        return this.m_nextArrival;
    }
    
    public GameDateConst getNextDeparture() {
        return this.m_nextDeparture;
    }
    
    public void setBoatDocked(final boolean boatDocked) {
        this.m_boatDocked = boatDocked;
    }
    
    @Override
    public boolean checkSubscription() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        return WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(localPlayer);
    }
    
    static {
        m_logger = Logger.getLogger((Class)BoatBoard.class);
    }
    
    public static class BoatBoardFactory extends ObjectFactory<WakfuClientMapInteractiveElement>
    {
        private static MonitoredPool m_pool;
        
        @Override
        public WakfuClientMapInteractiveElement makeObject() {
            BoatBoard board;
            try {
                board = (BoatBoard)BoatBoardFactory.m_pool.borrowObject();
                board.setPool(BoatBoardFactory.m_pool);
            }
            catch (Exception e) {
                BoatBoard.m_logger.error((Object)"Erreur lors de l'extraction d'un Chest du pool", (Throwable)e);
                board = new BoatBoard();
            }
            return board;
        }
        
        static {
            BoatBoardFactory.m_pool = new MonitoredPool(new ObjectFactory<BoatBoard>() {
                @Override
                public BoatBoard makeObject() {
                    return new BoatBoard();
                }
            });
        }
    }
}
