package com.ankamagames.wakfu.common.game.krozmoz;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.util.*;

public class KrosmozFigure implements RawConvertible<RawKrosmozFigure>
{
    private String m_guid;
    private int m_character;
    private int m_pedestal;
    private Date m_acquiredOn;
    private String m_note;
    private boolean m_bound;
    
    public KrosmozFigure() {
        super();
    }
    
    public KrosmozFigure(final String guid, final int character, final int pedestal, final Date acquiredOn, final String note, final boolean bound) {
        super();
        this.m_guid = guid;
        this.m_character = character;
        this.m_pedestal = pedestal;
        this.m_acquiredOn = acquiredOn;
        this.m_note = note;
        this.m_bound = bound;
    }
    
    public String getGuid() {
        return this.m_guid;
    }
    
    public int getCharacter() {
        return this.m_character;
    }
    
    public int getPedestal() {
        return this.m_pedestal;
    }
    
    public Date getAcquiredOn() {
        return this.m_acquiredOn;
    }
    
    public String getNote() {
        return this.m_note;
    }
    
    public boolean isBound() {
        return this.m_bound;
    }
    
    @Override
    public boolean toRaw(final RawKrosmozFigure raw) {
        raw.guid = this.m_guid;
        raw.character = this.m_character;
        raw.pedestal = this.m_pedestal;
        raw.acquiredOn = this.m_acquiredOn.getTime();
        raw.note = this.m_note;
        raw.bound = this.m_bound;
        return true;
    }
    
    @Override
    public boolean fromRaw(final RawKrosmozFigure raw) {
        this.m_guid = raw.guid;
        this.m_character = raw.character;
        this.m_pedestal = raw.pedestal;
        this.m_acquiredOn = new Date(raw.acquiredOn);
        this.m_note = raw.note;
        this.m_bound = raw.bound;
        return true;
    }
}
