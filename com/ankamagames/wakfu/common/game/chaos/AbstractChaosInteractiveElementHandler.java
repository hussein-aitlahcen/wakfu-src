package com.ankamagames.wakfu.common.game.chaos;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import org.apache.commons.lang3.*;

public abstract class AbstractChaosInteractiveElementHandler implements InteractiveElementActionRunner
{
    private static final Logger m_logger;
    public static final short STATE_DESTROYED = -1;
    public static final short STATE_BASE = 0;
    public static final AbstractChaosInteractiveElementHandler NO_CHAOS;
    private boolean m_destroyed;
    protected final ChaosInteractiveElement m_element;
    
    protected AbstractChaosInteractiveElementHandler(@Nullable final ChaosInteractiveElement element) {
        super();
        this.m_destroyed = false;
        this.m_element = element;
    }
    
    public abstract int getSerializedInventorySize();
    
    public abstract byte[] serializeInventory();
    
    public abstract void unSerializeInventory(final byte[] p0);
    
    @Override
    public boolean fireAction(final InteractiveElementAction action, final InteractiveElementUser user) {
        final boolean executed = this.onAction(action, user);
        if (!executed) {
            AbstractChaosInteractiveElementHandler.m_logger.warn((Object)("L'action " + action.toString() + " n'a pas \u00e9t\u00e9 trait\u00e9e. (user = " + user.toString() + ")"));
        }
        return executed;
    }
    
    public ChaosInteractiveCategory getCategory() {
        return this.m_element.getChaosIEParameter().getChaosCategory();
    }
    
    public void changeState(final short state) {
        if (this.m_element != null) {
            this.m_element.setState(state);
            this.m_element.notifyChangesListeners();
        }
        this.m_destroyed = (state == -1);
    }
    
    public boolean isDestroyed() {
        return this.m_destroyed;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractChaosInteractiveElementHandler.class);
        NO_CHAOS = new NoChaosInteractiveElementHandler();
    }
    
    private static class NoChaosInteractiveElementHandler extends AbstractChaosInteractiveElementHandler
    {
        private NoChaosInteractiveElementHandler() {
            super(null);
        }
        
        @Override
        public boolean fireAction(final InteractiveElementAction action, final InteractiveElementUser user) {
            return false;
        }
        
        @Override
        public boolean onAction(final InteractiveElementAction action, final InteractiveElementUser user) {
            return false;
        }
        
        @Override
        public ChaosInteractiveCategory getCategory() {
            return ChaosInteractiveCategory.NO_CHAOS;
        }
        
        @Nullable
        @Override
        public InteractiveElementAction getDefaultAction() {
            return null;
        }
        
        @Override
        public InteractiveElementAction[] getUsableActions() {
            return InteractiveElementAction.EMPTY_ACTIONS;
        }
        
        @Override
        public void sendActionMessage(final InteractiveElementAction action) {
        }
        
        @Override
        public int getSerializedInventorySize() {
            return 0;
        }
        
        @Override
        public byte[] serializeInventory() {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        
        @Override
        public void unSerializeInventory(final byte[] serializedInventory) {
        }
    }
}
