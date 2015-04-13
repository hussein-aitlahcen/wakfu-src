package com.ankamagames.wakfu.client.core.game.characterInfo.group;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;

public class NPCGroupInformationManager
{
    private static final Logger m_logger;
    private static final NPCGroupInformationManager m_instance;
    private TLongObjectHashMap<NPCGroupInformation> m_groups;
    
    private NPCGroupInformationManager() {
        super();
        this.m_groups = new TLongObjectHashMap<NPCGroupInformation>();
    }
    
    public NPCGroupInformation getGroupInformation(final long groupId) {
        return this.m_groups.get(groupId);
    }
    
    public void updateGroupInformationFromNPC(final NonPlayerCharacter npc, final List<NPCSerializedGroup.Member> members) {
        final long groupId = npc.getGroupId();
        if (groupId == 0L) {
            return;
        }
        NPCGroupInformation groupInformation = this.m_groups.get(groupId);
        if (groupInformation == null) {
            groupInformation = new NPCGroupInformation(npc.getGroupId());
            this.m_groups.put(groupId, groupInformation);
        }
        groupInformation.updateInformationFromNPC(npc, members);
    }
    
    public void onNonPlayerCharacterRemoved(final NonPlayerCharacter npc, final boolean deleteInformations) {
        final long groupId = npc.getGroupId();
        if (groupId != 0L) {
            final NPCGroupInformation groupInformation = this.m_groups.get(groupId);
            if (groupInformation != null) {
                if (deleteInformations) {
                    groupInformation.onNonPlayerCharacterDelete(npc);
                }
                else {
                    groupInformation.onNonPlayerCharacterRemoved(npc);
                }
            }
        }
    }
    
    public void removeGroup(final NPCGroupInformation groupInformation) {
        this.m_groups.remove(groupInformation.getId());
    }
    
    public static NPCGroupInformationManager getInstance() {
        return NPCGroupInformationManager.m_instance;
    }
    
    public void removeAll() {
        this.m_groups.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)NPCGroupInformationManager.class);
        m_instance = new NPCGroupInformationManager();
    }
}
