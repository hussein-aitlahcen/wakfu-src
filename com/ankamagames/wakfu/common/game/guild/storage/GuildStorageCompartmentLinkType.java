package com.ankamagames.wakfu.common.game.guild.storage;

import java.util.*;
import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;

public enum GuildStorageCompartmentLinkType
{
    GUILD_1(1), 
    GUILD_2(2), 
    GUILD_3(3), 
    GUILD_4(4), 
    HOUSE_1(1), 
    HOUSE_2(2), 
    HOUSE_3(3), 
    HOUSE_4(4), 
    HOUSE_5(5), 
    HOUSE_6(6), 
    HOUSE_7(7), 
    HOUSE_8(8), 
    MANSION_1(1), 
    MANSION_2(2), 
    MANSION_3(3), 
    MANSION_4(4), 
    COLLECT(1);
    
    private final ArrayList<GuildStorageCompartmentType> m_types;
    private final byte m_typeIndex;
    private GuildRankAuthorisation m_authorization;
    
    private GuildStorageCompartmentLinkType(final int typeIndex) {
        this.m_types = new ArrayList<GuildStorageCompartmentType>();
        this.m_typeIndex = MathHelper.ensureByte(typeIndex);
    }
    
    static void init() {
        final GuildStorageCompartmentLinkType guild1 = GuildStorageCompartmentLinkType.GUILD_1;
    }
    
    private void addType(final GuildStorageCompartmentType type) {
        this.m_types.add(type);
        type.setLinkType(this);
    }
    
    public int getFirstLinkedCompartmentId() {
        if (this.m_types.isEmpty()) {
            return -1;
        }
        return this.m_types.get(0).m_id;
    }
    
    public GuildRankAuthorisation getRemoveAuthorization() {
        return this.m_authorization;
    }
    
    public byte getTypeIndex() {
        return this.m_typeIndex;
    }
    
    public boolean forEachType(final TObjectProcedure<GuildStorageCompartmentType> procedure) {
        for (int i = 0, size = this.m_types.size(); i < size; ++i) {
            if (!procedure.execute(this.m_types.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public ArrayList<GuildStorageCompartmentType> getTypes() {
        return this.m_types;
    }
    
    public GuildStorageCompartmentType getCompartmentTypeByLevel(final byte level) {
        if (level < 0 || level >= this.m_types.size()) {
            return null;
        }
        return this.m_types.get(level);
    }
    
    static {
        GuildStorageCompartmentLinkType.GUILD_1.addType(GuildStorageCompartmentType.GUILD_1);
        GuildStorageCompartmentLinkType.GUILD_2.addType(GuildStorageCompartmentType.GUILD_2);
        GuildStorageCompartmentLinkType.GUILD_3.addType(GuildStorageCompartmentType.GUILD_3);
        GuildStorageCompartmentLinkType.GUILD_4.addType(GuildStorageCompartmentType.GUILD_4);
        GuildStorageCompartmentLinkType.HOUSE_1.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_1);
        GuildStorageCompartmentLinkType.HOUSE_1.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_1);
        GuildStorageCompartmentLinkType.HOUSE_1.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_1);
        GuildStorageCompartmentLinkType.HOUSE_2.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_2);
        GuildStorageCompartmentLinkType.HOUSE_2.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_2);
        GuildStorageCompartmentLinkType.HOUSE_2.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_2);
        GuildStorageCompartmentLinkType.HOUSE_3.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_3);
        GuildStorageCompartmentLinkType.HOUSE_3.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_3);
        GuildStorageCompartmentLinkType.HOUSE_3.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_3);
        GuildStorageCompartmentLinkType.HOUSE_4.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_4);
        GuildStorageCompartmentLinkType.HOUSE_4.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_4);
        GuildStorageCompartmentLinkType.HOUSE_4.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_4);
        GuildStorageCompartmentLinkType.HOUSE_5.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_5);
        GuildStorageCompartmentLinkType.HOUSE_5.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_5);
        GuildStorageCompartmentLinkType.HOUSE_5.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_5);
        GuildStorageCompartmentLinkType.HOUSE_6.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_6);
        GuildStorageCompartmentLinkType.HOUSE_6.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_6);
        GuildStorageCompartmentLinkType.HOUSE_6.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_6);
        GuildStorageCompartmentLinkType.HOUSE_7.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_7);
        GuildStorageCompartmentLinkType.HOUSE_7.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_7);
        GuildStorageCompartmentLinkType.HOUSE_7.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_7);
        GuildStorageCompartmentLinkType.HOUSE_8.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_1_8);
        GuildStorageCompartmentLinkType.HOUSE_8.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_2_8);
        GuildStorageCompartmentLinkType.HOUSE_8.addType(GuildStorageCompartmentType.HAVEN_WORLD_HOUSE_3_8);
        GuildStorageCompartmentLinkType.MANSION_1.addType(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_1);
        GuildStorageCompartmentLinkType.MANSION_1.addType(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_2_1);
        GuildStorageCompartmentLinkType.MANSION_2.addType(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_2);
        GuildStorageCompartmentLinkType.MANSION_2.addType(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_2_2);
        GuildStorageCompartmentLinkType.MANSION_3.addType(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_3);
        GuildStorageCompartmentLinkType.MANSION_3.addType(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_2_3);
        GuildStorageCompartmentLinkType.MANSION_4.addType(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_1_4);
        GuildStorageCompartmentLinkType.MANSION_4.addType(GuildStorageCompartmentType.HAVEN_WORLD_MANSION_2_4);
        GuildStorageCompartmentLinkType.COLLECT.addType(GuildStorageCompartmentType.COLLECT);
        GuildStorageCompartmentLinkType.GUILD_1.m_authorization = GuildRankAuthorisation.MANAGE_GUILD_CHEST_1;
        GuildStorageCompartmentLinkType.GUILD_2.m_authorization = GuildRankAuthorisation.MANAGE_GUILD_CHEST_2;
        GuildStorageCompartmentLinkType.GUILD_3.m_authorization = GuildRankAuthorisation.MANAGE_GUILD_CHEST_3;
        GuildStorageCompartmentLinkType.GUILD_4.m_authorization = GuildRankAuthorisation.MANAGE_GUILD_CHEST_4;
        GuildStorageCompartmentLinkType.HOUSE_1.m_authorization = GuildRankAuthorisation.MANAGE_HOUSE_CHEST_1;
        GuildStorageCompartmentLinkType.HOUSE_2.m_authorization = GuildRankAuthorisation.MANAGE_HOUSE_CHEST_2;
        GuildStorageCompartmentLinkType.HOUSE_3.m_authorization = GuildRankAuthorisation.MANAGE_HOUSE_CHEST_3;
        GuildStorageCompartmentLinkType.HOUSE_4.m_authorization = GuildRankAuthorisation.MANAGE_HOUSE_CHEST_4;
        GuildStorageCompartmentLinkType.HOUSE_5.m_authorization = GuildRankAuthorisation.MANAGE_HOUSE_CHEST_5;
        GuildStorageCompartmentLinkType.HOUSE_6.m_authorization = GuildRankAuthorisation.MANAGE_HOUSE_CHEST_6;
        GuildStorageCompartmentLinkType.HOUSE_7.m_authorization = GuildRankAuthorisation.MANAGE_HOUSE_CHEST_7;
        GuildStorageCompartmentLinkType.HOUSE_8.m_authorization = GuildRankAuthorisation.MANAGE_HOUSE_CHEST_8;
        GuildStorageCompartmentLinkType.MANSION_1.m_authorization = GuildRankAuthorisation.MANAGE_MANSION_CHEST_1;
        GuildStorageCompartmentLinkType.MANSION_2.m_authorization = GuildRankAuthorisation.MANAGE_MANSION_CHEST_2;
        GuildStorageCompartmentLinkType.MANSION_3.m_authorization = GuildRankAuthorisation.MANAGE_MANSION_CHEST_3;
        GuildStorageCompartmentLinkType.MANSION_4.m_authorization = GuildRankAuthorisation.MANAGE_MANSION_CHEST_4;
        GuildStorageCompartmentLinkType.COLLECT.m_authorization = GuildRankAuthorisation.MANAGE_COLLECT_CHEST;
    }
}
