package com.ankamagames.baseImpl.graphics.alea.environment;

import com.ankamagames.baseImpl.common.clientAndServer.alea.environment.*;
import java.util.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LandMarkDef implements IElementDef
{
    public static byte DEFAULT_SUB_TYPE;
    public int m_id;
    public int m_x;
    public int m_y;
    public short m_z;
    public byte m_type;
    public byte m_subType;
    public int m_achievementGoalId;
    public short m_versionMajor;
    public short m_versionMinor;
    public byte m_exportType;
    public int m_exportTypeLinkedId;
    public ArrayList<LandMarkDescriptionDef> m_descriptions;
    
    public LandMarkDef() {
        super();
    }
    
    public LandMarkDef(final int id, final int x, final int y, final short z, final byte type, final byte subType, final int achievementGoalId, final short versionMajor, final short versionMinor, final byte exportType, final int exportTypeLinkedId) {
        super();
        this.m_id = id;
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
        this.m_type = type;
        this.m_subType = subType;
        this.m_achievementGoalId = achievementGoalId;
        this.m_versionMajor = versionMajor;
        this.m_versionMinor = versionMinor;
        this.m_exportType = exportType;
        this.m_exportTypeLinkedId = exportTypeLinkedId;
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_id = istream.readInt();
        this.m_x = istream.readInt();
        this.m_y = istream.readInt();
        this.m_z = istream.readShort();
        this.m_type = istream.readByte();
        this.m_subType = istream.readByte();
        this.m_achievementGoalId = istream.readInt();
        this.m_versionMajor = istream.readShort();
        this.m_versionMinor = istream.readShort();
        this.m_exportType = istream.readByte();
        this.m_exportTypeLinkedId = istream.readInt();
        final byte descSize = istream.readByte();
        if (descSize > 0) {
            this.m_descriptions = new ArrayList<LandMarkDescriptionDef>(2);
        }
        for (int i = 0; i < descSize; ++i) {
            final LandMarkDescriptionDef def = new LandMarkDescriptionDef();
            def.load(istream);
            this.m_descriptions.add(def);
        }
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_id);
        ostream.writeInt(this.m_x);
        ostream.writeInt(this.m_y);
        ostream.writeShort(this.m_z);
        ostream.writeByte(this.m_type);
        ostream.writeByte(this.m_subType);
        ostream.writeInt(this.m_achievementGoalId);
        ostream.writeShort(this.m_versionMajor);
        ostream.writeShort(this.m_versionMinor);
        ostream.writeByte(this.m_exportType);
        ostream.writeInt(this.m_exportTypeLinkedId);
        final byte size = (byte)((this.m_descriptions == null) ? 0 : ((byte)this.m_descriptions.size()));
        ostream.writeByte(size);
        for (int i = 0; i < size; ++i) {
            this.m_descriptions.get(i).save(ostream);
        }
    }
    
    public void addDescription(final LandMarkDescriptionDef def) {
        if (this.m_descriptions == null) {
            this.m_descriptions = new ArrayList<LandMarkDescriptionDef>();
        }
        this.m_descriptions.add(def);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("LandMarkDef{").append("m_x=").append(this.m_x).append(", ").append("m_y=").append(this.m_y).append(", ").append("m_z=").append(this.m_z).append(", ").append("m_type=").append(this.m_type).append(", ").append("m_subType=").append(this.m_subType).append(", ").append("m_achievementGoalId=").append(this.m_achievementGoalId).append(", ").append("m_version").append(this.m_versionMajor).append(".").append(this.m_versionMinor).append(", ").append("}");
        return sb.toString();
    }
    
    static {
        LandMarkDef.DEFAULT_SUB_TYPE = -1;
    }
}
