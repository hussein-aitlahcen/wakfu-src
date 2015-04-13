package com.ankamagames.baseImpl.graphics.alea.environment;

import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LandMarkDescriptionDef
{
    public int m_id;
    public short m_iconId;
    public String m_criterionString;
    public SimpleCriterion m_criterion;
    
    public LandMarkDescriptionDef() {
        super();
    }
    
    public LandMarkDescriptionDef(final int id, final short iconId, final String criterion) {
        super();
        this.m_id = id;
        this.m_iconId = iconId;
        this.m_criterionString = criterion;
    }
    
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        this.m_id = istream.readInt();
        this.m_iconId = istream.readShort();
        this.m_criterionString = istream.readString();
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeInt(this.m_id);
        ostream.writeShort(this.m_iconId);
        ostream.writeString(this.m_criterionString);
    }
}
