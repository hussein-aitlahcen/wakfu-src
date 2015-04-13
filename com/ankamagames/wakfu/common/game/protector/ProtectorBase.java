package com.ankamagames.wakfu.common.game.protector;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.nation.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.dialog.*;
import com.ankamagames.wakfu.common.game.dialog.*;

public abstract class ProtectorBase implements DialogSource
{
    protected static Logger m_logger;
    private int m_id;
    private Nation m_nativeNation;
    private Nation m_currentNation;
    private AbstractTerritory m_territory;
    private final ArrayList<ProtectorNationChangedListener> m_listeners;
    
    protected ProtectorBase(final int id) {
        super();
        this.m_listeners = new ArrayList<ProtectorNationChangedListener>();
        this.m_id = id;
        this.m_nativeNation = null;
        this.m_currentNation = null;
    }
    
    protected void setId(final int id) {
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public long getDialogSourceId() {
        return this.m_id;
    }
    
    @Override
    public AbstractDialogSourceType getDialogSourceType() {
        return DialogSourceType.PROTECTOR;
    }
    
    public void addProtectorListener(final ProtectorNationChangedListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    public void removeProtectorListener(final ProtectorNationChangedListener listener) {
        this.m_listeners.remove(listener);
    }
    
    public Nation getNativeNation() {
        return this.m_nativeNation;
    }
    
    public void setNativeNation(final Nation nativeNation) {
        this.m_nativeNation = nativeNation;
        if (this.m_currentNation == null) {
            this.setCurrentNation(this.m_nativeNation);
        }
    }
    
    public Nation getCurrentNation() {
        return this.m_currentNation;
    }
    
    public void setCurrentNation(final Nation currentNation) {
        final Nation previous = this.m_currentNation;
        this.m_currentNation = currentNation;
        for (int i = 0; i < this.m_listeners.size(); ++i) {
            this.m_listeners.get(i).onNationChanged(this, currentNation);
        }
        this.onNationChanged(currentNation, previous);
    }
    
    protected void onNationChanged(final Nation currentNation, final Nation previousNation) {
    }
    
    public AbstractTerritory getTerritory() {
        return this.m_territory;
    }
    
    public void setTerritory(final AbstractTerritory territory) {
        if (this.m_territory != null) {
            this.m_territory.setProtector(null);
        }
        this.m_territory = territory;
        if (this.m_territory != null) {
            this.m_territory.setProtector(this);
        }
    }
    
    @Override
    public String toString() {
        return "Protector {" + "id=" + this.m_id + ", nativeNation=" + (Object)((this.m_nativeNation != null) ? this.m_nativeNation.getNationId() : null) + ", currentNation=" + (Object)((this.m_currentNation != null) ? this.m_currentNation.getNationId() : null) + ", territory=" + (Object)((this.m_territory != null) ? this.m_territory.getId() : null) + '}';
    }
    
    public int getCurrentNationId() {
        if (this.m_currentNation == null) {
            return -1;
        }
        return this.m_currentNation.getNationId();
    }
    
    public int getNativeNationId() {
        if (this.m_nativeNation == null) {
            return -1;
        }
        return this.m_nativeNation.getNationId();
    }
    
    public abstract ProtectorSatisfactionLevel getSatisfactionLevel();
    
    public boolean hasNation() {
        return this.getCurrentNationId() != 0;
    }
    
    static {
        ProtectorBase.m_logger = Logger.getLogger((Class)ProtectorBase.class);
    }
}
