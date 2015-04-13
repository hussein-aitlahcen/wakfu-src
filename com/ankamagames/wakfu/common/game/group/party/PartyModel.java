package com.ankamagames.wakfu.common.game.group.party;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import java.util.*;
import gnu.trove.*;

public final class PartyModel implements PartyModelInterface
{
    private static final Logger m_logger;
    private final long m_id;
    private long m_leaderId;
    private final TLongObjectHashMap<PartyMemberInterface> m_members;
    private final List<PartyModelListener> m_listeners;
    
    public PartyModel(final long groupId) {
        super();
        this.m_leaderId = -1L;
        this.m_members = new TLongObjectHashMap<PartyMemberInterface>();
        this.m_listeners = new ArrayList<PartyModelListener>();
        this.m_id = groupId;
    }
    
    @Override
    public long getLeaderId() {
        return this.m_leaderId;
    }
    
    @Override
    public void setLeaderId(final long leaderId) {
        final long previousLeaderId = this.m_leaderId;
        this.notifyLeaderChanged(previousLeaderId, this.m_leaderId = leaderId);
    }
    
    @Override
    public boolean isLeader(final PartyMemberInterface member) {
        return member != null && this.m_leaderId == member.getCharacterId();
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public void addMember(final PartyMemberInterface member) {
        member.setGroupId(this.m_id);
        this.m_members.put(member.getCharacterId(), member);
        this.notifyMemberAdded(member);
    }
    
    @Override
    public boolean removeMember(final long memberId) {
        final PartyMemberInterface member = this.m_members.get(memberId);
        return member != null && this.removeMember(member, false);
    }
    
    @Override
    public boolean removeMember(final PartyMemberInterface memberData, final boolean bforce) {
        if (!this.m_members.contains(memberData.getCharacterId())) {
            return false;
        }
        try {
            if (bforce) {
                this.m_members.remove(memberData.getCharacterId());
                this.notifyMemberRemoved(memberData);
                memberData.removeGroupId(GroupIdGenerator.getTypeFromId(this.m_id));
                return true;
            }
            if (this.canRemove(memberData) && this.m_members.remove(memberData.getCharacterId()) != null) {
                this.notifyMemberRemoved(memberData);
                memberData.removeGroupId(GroupIdGenerator.getTypeFromId(this.m_id));
                return true;
            }
            return false;
        }
        catch (Exception e) {
            PartyModel.m_logger.error((Object)"Exception levee", (Throwable)e);
            return false;
        }
    }
    
    @Override
    public boolean canRemove(final PartyMemberInterface member) {
        return member.getGroupId() == this.m_id && this.m_members.containsKey(member.getCharacterId());
    }
    
    @Override
    public void removeAllMembers() {
        final PartyMemberInterface[] members = new PartyMemberInterface[this.m_members.size()];
        this.m_members.getValues(members);
        for (int i = 0; i < members.length; ++i) {
            final PartyMemberInterface member = members[i];
            this.removeMember(member, true);
        }
        this.m_members.clear();
    }
    
    public int getMemberCount() {
        return this.m_members.size();
    }
    
    @Override
    public TLongObjectHashMap<PartyMemberInterface> getMembers() {
        return this.m_members;
    }
    
    @Override
    public TLongObjectHashMap<PartyMemberInterface> getMembersOfType(final int type) {
        final TLongObjectHashMap<PartyMemberInterface> res = new TLongObjectHashMap<PartyMemberInterface>();
        this.m_members.forEachEntry(new MembersTypeProcedure(type, res));
        return res;
    }
    
    @Override
    public List<PartyMemberInterface> getCompanions(final PartyMemberInterface owner) {
        final long clientId = owner.getClientId();
        return this.getCompanions(clientId);
    }
    
    @Override
    public List<PartyMemberInterface> getCompanions(final long clientId) {
        final TLongObjectHashMap<PartyMemberInterface> companions = this.getMembersOfType(2);
        if (companions.isEmpty()) {
            return Collections.emptyList();
        }
        final List<PartyMemberInterface> res = new ArrayList<PartyMemberInterface>();
        companions.forEachValue(new GetCompanionsProcedure(clientId, res));
        return res;
    }
    
    @Override
    public int getRealPlayerCount() {
        return this.m_members.size() - this.getMembersOfType(2).size();
    }
    
    @Override
    public boolean contains(final String data) {
        final TLongObjectIterator<PartyMemberInterface> members = this.m_members.iterator();
        while (members.hasNext()) {
            members.advance();
            final PartyMemberInterface member = members.value();
            if (member.getName().equals(data)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public PartyMemberInterface getMember(final long id) {
        return this.m_members.get(id);
    }
    
    @Override
    public boolean contains(final long id) {
        return this.m_members.contains(id);
    }
    
    @Override
    public boolean isEmpty() {
        return this.m_members.isEmpty();
    }
    
    @Override
    public boolean isFull() {
        return this.m_members.size() >= 6;
    }
    
    @Override
    public void addListener(final PartyModelListener listener) {
        if (this.m_listeners.contains(listener)) {
            return;
        }
        this.m_listeners.add(listener);
    }
    
    public void removeListener(final PartyModelListener listener) {
        this.m_listeners.remove(listener);
    }
    
    private void notifyLeaderChanged(final long previousLeaderId, final long newLeaderId) {
        final Iterable<PartyModelListener> listeners = new ArrayList<PartyModelListener>(this.m_listeners);
        for (final PartyModelListener listener : listeners) {
            listener.onLeaderChange(this, previousLeaderId, newLeaderId);
        }
    }
    
    private void notifyMemberAdded(final PartyMemberInterface member) {
        final Iterable<PartyModelListener> listeners = new ArrayList<PartyModelListener>(this.m_listeners);
        for (final PartyModelListener listener : listeners) {
            listener.onMemberAdded(this, member);
        }
    }
    
    private void notifyMemberRemoved(final PartyMemberInterface member) {
        final Iterable<PartyModelListener> listeners = new ArrayList<PartyModelListener>(this.m_listeners);
        for (final PartyModelListener listener : listeners) {
            listener.onMemberRemoved(this, member);
        }
    }
    
    @Override
    public String toString() {
        return "PartyModel{m_id=" + this.m_id + ", m_leaderId=" + this.m_leaderId + ", m_members=" + this.m_members + ", m_listeners=" + this.m_listeners + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)PartyModel.class);
    }
    
    private static class MembersTypeProcedure implements TLongObjectProcedure<PartyMemberInterface>
    {
        private final int m_type;
        private final TLongObjectHashMap<PartyMemberInterface> m_res;
        
        MembersTypeProcedure(final int type, final TLongObjectHashMap<PartyMemberInterface> res) {
            super();
            this.m_type = type;
            this.m_res = res;
        }
        
        @Override
        public boolean execute(final long a, final PartyMemberInterface b) {
            if (b.getType() == this.m_type) {
                this.m_res.put(a, b);
            }
            return true;
        }
        
        @Override
        public String toString() {
            return "MembersTypeProcedure{m_type=" + this.m_type + ", m_res=" + this.m_res + '}';
        }
    }
    
    private static class GetCompanionsProcedure implements TObjectProcedure<PartyMemberInterface>
    {
        private final long m_clientId;
        private final List<PartyMemberInterface> m_res;
        
        GetCompanionsProcedure(final long clientId, final List<PartyMemberInterface> res) {
            super();
            this.m_clientId = clientId;
            this.m_res = res;
        }
        
        @Override
        public boolean execute(final PartyMemberInterface object) {
            if (object.getClientId() == this.m_clientId) {
                this.m_res.add(object);
            }
            return true;
        }
        
        @Override
        public String toString() {
            return "GetCompanionsProcedure{m_clientId=" + this.m_clientId + ", m_res=" + this.m_res + '}';
        }
    }
}
