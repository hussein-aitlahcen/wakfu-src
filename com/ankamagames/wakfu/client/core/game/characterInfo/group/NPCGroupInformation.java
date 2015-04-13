package com.ankamagames.wakfu.client.core.game.characterInfo.group;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.xp.synthlevel.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;

public class NPCGroupInformation
{
    private static final Logger m_logger;
    public static final boolean DEBUG_MODE = false;
    private long m_id;
    private int m_lastKnownGroupSize;
    private int m_spawnedMembersCount;
    private ArrayList<NPCInformation> m_groupMembers;
    
    public NPCGroupInformation(final long id) {
        super();
        this.m_id = id;
        this.m_lastKnownGroupSize = 0;
        this.m_spawnedMembersCount = 0;
        this.m_groupMembers = new ArrayList<NPCInformation>();
    }
    
    public void updateNpc(final NonPlayerCharacter npc) {
        final NPCInformation info = this.getMemberInfo(npc.getId());
        info.setLevel(npc.getLevel());
        info.setReferenceId(npc.getTypeId());
    }
    
    public void updateInformationFromNPC(final NonPlayerCharacter npc, final List<NPCSerializedGroup.Member> members) {
        if (this.m_groupMembers.isEmpty()) {
            this.m_lastKnownGroupSize = members.size();
            for (int i = 0; i < members.size(); ++i) {
                final NPCSerializedGroup.Member member = members.get(i);
                final NPCInformation info = new NPCInformation(0L, member.breedId, member.level);
                this.m_groupMembers.add(info);
            }
        }
        if (this.m_lastKnownGroupSize < members.size() && this.getMemberInfo(npc.getId()) == null) {
            this.m_groupMembers.add(new NPCInformation(npc.getId(), npc.getTypeId(), npc.getLevel()));
            ++this.m_lastKnownGroupSize;
            ++this.m_spawnedMembersCount;
            return;
        }
        if (this.getMemberInfo(npc.getId()) != null) {
            return;
        }
        for (int i = 0; i < this.m_groupMembers.size(); ++i) {
            final NPCInformation info2 = this.m_groupMembers.get(i);
            if (info2.getId() == 0L && info2.getReferenceId() == npc.getTypeId() && info2.getLevel() == npc.getLevel()) {
                info2.setId(npc.getId());
                ++this.m_spawnedMembersCount;
                return;
            }
        }
        this.m_groupMembers.add(new NPCInformation(npc.getId(), npc.getTypeId(), npc.getLevel()));
        ++this.m_lastKnownGroupSize;
        ++this.m_spawnedMembersCount;
    }
    
    public void onNonPlayerCharacterDelete(final NonPlayerCharacter npc) {
        final NPCInformation npcInfo = this.getMemberInfo(npc.getId());
        if (this.m_groupMembers.remove(npcInfo)) {
            --this.m_spawnedMembersCount;
            --this.m_lastKnownGroupSize;
        }
        else {
            NPCGroupInformation.m_logger.warn((Object)("On essaye de remove plusieurs fois le monstre " + npc.getId() + " du NPCGroupInformation."));
        }
        if (this.m_spawnedMembersCount <= 0) {
            NPCGroupInformationManager.getInstance().removeGroup(this);
        }
    }
    
    public void onNonPlayerCharacterRemoved(final NonPlayerCharacter npc) {
        final NPCInformation info = this.getMemberInfo(npc.getId());
        if (info == null) {
            return;
        }
        info.setId(0L);
        --this.m_spawnedMembersCount;
        if (this.m_spawnedMembersCount <= 0) {
            NPCGroupInformationManager.getInstance().removeGroup(this);
        }
    }
    
    public boolean hasKnownBusyMember() {
        for (int i = 0, size = this.m_groupMembers.size(); i < size; ++i) {
            final CharacterInfo npc = CharacterInfoManager.getInstance().getCharacter(this.m_groupMembers.get(i).getId());
            if (npc != null) {
                final PropertyManager<WorldPropertyType> properties = npc.getWorldProperties();
                if (properties != null && properties.isActiveProperty(WorldPropertyType.BUSY)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<NPCInformation> getMembersInformations() {
        return this.m_groupMembers;
    }
    
    protected NPCInformation getMemberInfo(final long npcId) {
        for (final NPCInformation groupMember : this.m_groupMembers) {
            if (groupMember.getId() == npcId) {
                return groupMember;
            }
        }
        return null;
    }
    
    public boolean needsUpdate() {
        return this.m_lastKnownGroupSize != this.m_groupMembers.size();
    }
    
    public double getSyntheticGroupLevel() {
        final MutableSyntheticLevelCalc levelSynthetizer = new MutableSyntheticLevelCalc(new short[0]);
        for (int i = 0, size = this.m_groupMembers.size(); i < size; ++i) {
            final NPCInformation npcInformation = this.m_groupMembers.get(i);
            levelSynthetizer.addLevel(npcInformation.getLevel());
        }
        return levelSynthetizer.getSyntheticLevel();
    }
    
    public int getRealGroupLevel() {
        int level = 0;
        for (int i = 0, size = this.m_groupMembers.size(); i < size; ++i) {
            final NPCInformation npcInformation = this.m_groupMembers.get(i);
            level += npcInformation.getLevel();
        }
        return level;
    }
    
    public long getId() {
        return this.m_id;
    }
    
    public boolean isSpawned(final NonPlayerCharacter npc) {
        for (int i = 0; i < this.m_groupMembers.size(); ++i) {
            final NPCInformation info = this.m_groupMembers.get(i);
            if (info.getId() == npc.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public void updateId(final NonPlayerCharacter npc) {
        if (this.isSpawned(npc)) {
            return;
        }
        for (int i = 0; i < this.m_groupMembers.size(); ++i) {
            final NPCInformation info = this.m_groupMembers.get(i);
            if (info.getReferenceId() == npc.getBreedId() && info.getLevel() == npc.getLevel()) {
                info.setId(npc.getId());
                return;
            }
        }
    }
    
    public static NPCInformation getInfoFrom(final NonPlayerCharacter npc) {
        return new NPCInformation(npc.getClientId(), npc.getBreedId(), npc.getLevel());
    }
    
    static {
        m_logger = Logger.getLogger((Class)NPCGroupInformation.class);
    }
    
    public static class NPCInformation
    {
        private long m_id;
        private short m_referenceId;
        private short m_level;
        
        protected NPCInformation(final long id, final short referenceId, final short level) {
            super();
            this.m_id = id;
            this.m_referenceId = referenceId;
            this.m_level = level;
        }
        
        public long getId() {
            return this.m_id;
        }
        
        public void setId(final long id) {
            this.m_id = id;
        }
        
        public short getReferenceId() {
            return this.m_referenceId;
        }
        
        public void setReferenceId(final short referenceId) {
            this.m_referenceId = referenceId;
        }
        
        public short getLevel() {
            return this.m_level;
        }
        
        public void setLevel(final short level) {
            this.m_level = level;
        }
        
        public String toString(final boolean extended, final CharacterActorSelectionChangeListener.DescribedNonPlayerCharacterType describedNonPlayerCharacterType) {
            final StringBuffer buffer = new StringBuffer();
            final MonsterBreed breed = MonsterBreedManager.getInstance().getBreedFromId(this.m_referenceId);
            if (extended) {
                buffer.append("[").append(this.m_id).append("] ");
            }
            if (describedNonPlayerCharacterType != CharacterActorSelectionChangeListener.DescribedNonPlayerCharacterType.HOODED_MONSTER) {
                buffer.append(breed.getName());
            }
            else {
                buffer.append(WakfuTranslator.getInstance().getString("hooded.monster"));
            }
            switch (describedNonPlayerCharacterType) {
                case PNJ: {
                    final TextWidgetFormater formater = new TextWidgetFormater();
                    formater.openText();
                    formater.addColor(TerritoryViewConstants.NEUTRAL.getRGBtoHex());
                    formater.append(WakfuTranslator.getInstance().getString(77, WakfuGameEntity.getInstance().getLocalPlayer().getInstanceId(), new Object[0])).closeText();
                    buffer.append("\n").append(formater.finishAndToString());
                }
                case PROTECTOR: {}
                case MOB: {
                    buffer.append("\n(").append(WakfuTranslator.getInstance().getString("levelShort.custom", this.m_level)).append(")");
                    break;
                }
            }
            if (extended) {
                final CharacterInfo info = CharacterInfoManager.getInstance().getCharacter(this.m_id);
                if (info != null && info instanceof NonPlayerCharacter) {
                    final NonPlayerCharacter npc = (NonPlayerCharacter)info;
                    for (final WorldPropertyType property : WorldPropertyType.values()) {
                        if (npc.isActiveProperty(property)) {
                            buffer.append("\n");
                            buffer.append(property.name());
                        }
                    }
                }
            }
            return buffer.toString();
        }
    }
}
