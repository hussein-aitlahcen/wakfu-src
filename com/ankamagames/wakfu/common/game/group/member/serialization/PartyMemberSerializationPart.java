package com.ankamagames.wakfu.common.game.group.member.serialization;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import java.util.*;

public abstract class PartyMemberSerializationPart extends BinarSerialPart
{
    protected final PartyMemberInterface m_member;
    private List<PartyMemberPartListener> m_listeners;
    
    PartyMemberSerializationPart(final PartyMemberInterface member) {
        super();
        this.m_member = member;
    }
    
    public void onDataChanged() {
        if (this.m_listeners == null) {
            return;
        }
        for (final PartyMemberPartListener listener : this.m_listeners) {
            listener.onDataChanged();
        }
    }
    
    public void addListener(final PartyMemberPartListener listener) {
        if (this.m_listeners == null) {
            this.m_listeners = new ArrayList<PartyMemberPartListener>();
        }
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
}
