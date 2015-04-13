package com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.script.*;
import java.util.*;

public abstract class ClientMapInteractiveElement extends MapInteractiveElement implements InteractiveElementActionRunner
{
    private static final Logger m_logger;
    private static final String SCRIPT_PATH = "ie_actions";
    private final ArrayList<ClientInteractiveElementView> m_views;
    private final EnumMap<InteractiveElementAction, Integer> m_actionScripts;
    protected boolean m_isSpawned;
    private final BinarSerialPart GLOBAL_DATA;
    
    public ClientMapInteractiveElement() {
        super();
        this.m_views = new ArrayList<ClientInteractiveElementView>(1);
        this.m_actionScripts = new EnumMap<InteractiveElementAction, Integer>(InteractiveElementAction.class);
        this.GLOBAL_DATA = new BinarSerialPart() {
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("Impossible de s\u00e9rialiser un \u00e9l\u00e9ment interactif c\u00f4t\u00e9 client");
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ClientMapInteractiveElement$1.m_logger.trace((Object)("D\u00e9codage des donn\u00e9es globales (len=" + buffer.remaining() + ")"));
                if (buffer.remaining() > 0) {
                    if (buffer.remaining() < 1) {
                        ClientMapInteractiveElement$1.m_logger.error((Object)"Impossible de d\u00e9s\u00e9rialiser une partie vide");
                        return;
                    }
                    final int nActions = buffer.get() & 0xFF;
                    if (buffer.remaining() != nActions * 6) {
                        ClientMapInteractiveElement$1.m_logger.error((Object)("Impossible de d\u00e9s\u00e9rialiser " + nActions + " actions dans un buffer de " + buffer.remaining() + " octets"));
                        return;
                    }
                    for (int i = 0; i < nActions; ++i) {
                        final short actionId = buffer.getShort();
                        final int scriptId = buffer.getInt();
                        final InteractiveElementAction action = InteractiveElementAction.valueOf(actionId);
                        if (action != null) {
                            ClientMapInteractiveElement.this.m_actionScripts.put(action, Integer.valueOf(scriptId));
                        }
                        else {
                            ClientMapInteractiveElement$1.m_logger.error((Object)("Pas de InteractiveElementAction d'ID=" + actionId));
                        }
                    }
                }
            }
        };
    }
    
    public final Collection<ClientInteractiveElementView> getViews() {
        return this.m_views;
    }
    
    @Override
    public boolean fireAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        if (!this.m_usable) {
            return false;
        }
        final boolean executed = this.onAction(action, user);
        if (!executed) {
            ClientMapInteractiveElement.m_logger.warn((Object)("Action non prise en compte par le ModelControler (ClientMapInteractiveElement) : " + action.toString()));
        }
        return executed;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_views.clear();
        this.m_actionScripts.clear();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
        this.m_isSpawned = false;
        assert this.m_views.isEmpty();
        assert this.m_actionScripts.isEmpty();
    }
    
    public boolean addView(final ClientInteractiveElementView view) {
        view.setInteractiveElement(this);
        this.addChangesListener(view);
        return this.m_views.add(view);
    }
    
    public final void clearViews() {
        for (int i = 0; i < this.m_views.size(); ++i) {
            this.removeChangesListener(this.m_views.get(i));
        }
        this.m_views.clear();
    }
    
    public abstract List<Point3> getApproachPoints();
    
    @Override
    protected BinarSerialPart getGlobalDataPart() {
        return this.GLOBAL_DATA;
    }
    
    @Override
    protected final BinarSerialPart getPersistancePart() {
        return BinarSerialPart.EMPTY;
    }
    
    @Override
    protected final BinarSerialPart getAdditionalPersistancePart() {
        return BinarSerialPart.EMPTY;
    }
    
    public TopologyMapInstance getTopologyMap() {
        final Point3 position = this.getPosition();
        return TopologyMapManager.getMapFromCell(position.getX(), position.getY());
    }
    
    public abstract boolean hasToFinishOnIE();
    
    public final void notifyViews() {
        for (final ClientInteractiveElementView interactiveElementView : this.m_views) {
            this.updateView(interactiveElementView);
        }
    }
    
    protected void updateView(final ClientInteractiveElementView view) {
        view.update();
    }
    
    public void onSpawn() {
        this.m_isSpawned = true;
    }
    
    public void onDeSpawn() {
        this.m_isSpawned = false;
    }
    
    public abstract void onViewUpdated(final ClientInteractiveElementView p0);
    
    public boolean removeView(final ClientInteractiveElementView view) {
        this.removeChangesListener(view);
        return this.m_views.remove(view);
    }
    
    protected final void runScript(final InteractiveElementAction action) {
        final Integer scriptId = this.m_actionScripts.get(action);
        if (scriptId != null && scriptId != 0) {
            final Map<String, Object> var = (Map<String, Object>)Collections.singletonMap("elementId", this.m_id);
            assert LuaManager.getInstance().getPath() != null;
            final String filename = String.format("%s/%d%s", "ie_actions", scriptId, LuaManager.getInstance().getExtension());
            LuaManager.getInstance().runScript(filename, null, var, null, false);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientMapInteractiveElement.class);
    }
}
