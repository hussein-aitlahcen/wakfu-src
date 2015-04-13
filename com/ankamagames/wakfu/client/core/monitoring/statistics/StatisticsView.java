package com.ankamagames.wakfu.client.core.monitoring.statistics;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.gameStats.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;

public final class StatisticsView implements FieldProvider
{
    private static final Logger m_logger;
    private long m_lastTimestamp;
    private static final String KEY_UPTIME_ROOT = "uptime";
    private static final String KEY_SESSIONS_ROOT = "sessions";
    private static final String KEY_NB_PLAYERS = "sessions.allSessions.value";
    private static final String KEY_MAX_PLAYERS = "sessions.allSessions.max";
    private static final String KEY_DATE_MAX_PLAYERS = "sessions.allSessions.maxTime";
    private static final String KEY_NB_ADMIN_PLAYERS = "sessions.adminSessions.value";
    private static final String KEY_MAX_ADMIN_PLAYERS = "sessions.adminSessions.max";
    private static final String KEY_DATE_MAX_ADMIN_PLAYERS = "sessions.adminSessions.maxTime";
    public static final String CATEGORIES = "categories";
    private static final String[] ALL_FIELDS;
    private static final StatisticsView m_instance;
    private final Collection<CategoryView> m_categories;
    
    public StatisticsView() {
        super();
        this.m_lastTimestamp = -1L;
        this.m_categories = new ArrayList<CategoryView>();
    }
    
    public static StatisticsView getInstance() {
        return StatisticsView.m_instance;
    }
    
    private Iterable<String> getRequests() {
        final Collection<String> requests = new ArrayList<String>();
        for (final CategoryView category : this.m_categories) {
            requests.addAll(category.getRequests());
        }
        return requests;
    }
    
    @Override
    public String[] getFields() {
        return StatisticsView.ALL_FIELDS.clone();
    }
    
    @Nullable
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("categories")) {
            return this.m_categories;
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    private void updateView() {
        for (final CategoryView cat : this.m_categories) {
            cat.update();
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, StatisticsView.ALL_FIELDS);
    }
    
    public void createView() {
        final CategoryView serversCat = new CategoryView("Servers");
        final ElementView pingProxy = new PingView("Proxy", (byte)0);
        final ElementView pingConnex = new PingView("Connection", (byte)1);
        final SimpleElementView pingWorld = new PingView("World", (byte)2);
        pingWorld.addLinkedElement(new DateElementView("Started", "uptime.world.started"));
        final SimpleElementView pingGame = new PingView("Game", (byte)3);
        final List<Node> gservers = Statistics.getInstance().getRootPackage().select("uptime.game.?").getAllNodes(true);
        for (final Node node : gservers) {
            pingGame.addLinkedElement(new DateElementView(node.getName() + " - started", "uptime.game." + node.getName() + ".started"));
            pingGame.addLinkedElement(new DateElementView(node.getName() + " - connected", "uptime.game." + node.getName() + ".connected"));
        }
        final SimpleElementView pingGlobal = new PingView("Global", (byte)6);
        pingGlobal.addLinkedElement(new DateElementView("Started", "uptime.global.started"));
        pingGlobal.addLinkedElement(new DateElementView("Connected", "uptime.global.connected"));
        final ElementView pingChat = new PingView("Chat", (byte)4);
        serversCat.addRequest("uptime.*");
        serversCat.addElement(pingProxy);
        serversCat.addElement(pingConnex);
        serversCat.addElement(pingWorld);
        serversCat.addElement(pingGame);
        serversCat.addElement(pingGlobal);
        serversCat.addElement(pingChat);
        final CategoryView generalCat = new CategoryView("General");
        final ElementView lastUpdateTime = new ConstantDateElementView("Last update", Long.valueOf(this.m_lastTimestamp));
        final SimpleElementView nbPlayers = new AmmountElementView("Player population", "sessions.allSessions.value");
        nbPlayers.addLinkedElement(new AmmountElementView("Max", "sessions.allSessions.max"));
        nbPlayers.addLinkedElement(new DateElementView("Max date", "sessions.allSessions.maxTime"));
        final SimpleElementView nbAdminPlayers = new AmmountElementView("Admin population", "sessions.adminSessions.value");
        nbAdminPlayers.addLinkedElement(new AmmountElementView("Max", "sessions.adminSessions.max"));
        nbAdminPlayers.addLinkedElement(new DateElementView("Max date", "sessions.adminSessions.maxTime"));
        generalCat.addElement(lastUpdateTime);
        generalCat.addElement(nbPlayers);
        generalCat.addElement(nbAdminPlayers);
        generalCat.addRequest("sessions.*");
        this.m_categories.clear();
        this.m_categories.add(serversCat);
        this.m_categories.add(generalCat);
        this.updateView();
    }
    
    public void clear() {
        this.m_categories.clear();
    }
    
    public long getLastTimestamp() {
        return this.m_lastTimestamp;
    }
    
    public void setLastTimestamp(final long lastTimestamp) {
        this.m_lastTimestamp = lastTimestamp;
    }
    
    public void sendUpdateRequest() {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            StatisticsView.m_logger.error((Object)"You must be connected!");
            return;
        }
        networkEntity.sendMessage(new PingMessage((byte)0, 1));
        networkEntity.sendMessage(new PingMessage((byte)2, 1));
        networkEntity.sendMessage(new PingMessage((byte)3, 1));
        networkEntity.sendMessage(new PingMessage((byte)1, 1));
        networkEntity.sendMessage(new PingMessage((byte)4, 1));
        networkEntity.sendMessage(new PingMessage((byte)6, 1));
        final int maxSize = 900;
        final long timeStamp = System.currentTimeMillis();
        int size = StatisticsUpdateRequestMessage.getSizeOfEmptyMessage();
        StatisticsUpdateRequestMessage netMessage = new StatisticsUpdateRequestMessage();
        netMessage.setTimeStamp(timeStamp);
        for (final String req : this.getRequests()) {
            final byte[] data = StringUtils.toUTF8(req);
            if (data.length + 4 + size > 900) {
                networkEntity.sendMessage(netMessage);
                netMessage = new StatisticsUpdateRequestMessage();
                netMessage.setTimeStamp(timeStamp);
                size = StatisticsUpdateRequestMessage.getSizeOfEmptyMessage();
            }
            netMessage.addSerialisedRequest(data);
            size += data.length + 4;
        }
        if (netMessage.getSerializedRequestsCount() >= 0) {
            networkEntity.sendMessage(netMessage);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)StatisticsView.class);
        ALL_FIELDS = new String[] { "categories" };
        m_instance = new StatisticsView();
    }
}
