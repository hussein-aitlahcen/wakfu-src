package com.ankamagames.baseImpl.graphics.alea.environment;

import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;

public class LandMarkMap
{
    public static final int VERSION = 0;
    private final TIntObjectHashMap<LandMarkDef> m_landMarkData;
    boolean m_loaded;
    
    public LandMarkMap() {
        super();
        this.m_landMarkData = new TIntObjectHashMap<LandMarkDef>();
    }
    
    public TIntObjectHashMap<LandMarkDef> getLandMarkDef() {
        return this.m_landMarkData;
    }
    
    public void addLandMarkData(final TIntObjectHashMap<LandMarkDef> landMarkData) {
        this.m_landMarkData.ensureCapacity(this.m_landMarkData.size() + landMarkData.size());
        this.m_landMarkData.putAll(landMarkData);
    }
    
    public void load(@NotNull final ExtendedDataInputStream istream, final boolean clear) throws IOException {
        this.loadLandMarkData(istream, clear);
    }
    
    public void loadFull(@NotNull final ExtendedDataInputStream istream, final boolean clear) throws IOException {
        final byte version = istream.readByte();
        this.load(istream, clear);
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        this.saveLandMarkData(ostream);
    }
    
    private void loadLandMarkData(final ExtendedDataInputStream istream, final boolean clear) throws IOException {
        if (clear) {
            this.m_landMarkData.clear();
        }
        final int numLandMarks = istream.readShort() & 0xFFFF;
        if (numLandMarks == 0) {
            return;
        }
        for (int i = 0; i < numLandMarks; ++i) {
            final LandMarkDef landMark = new LandMarkDef();
            landMark.load(istream);
            this.m_landMarkData.put(landMark.m_id, landMark);
        }
    }
    
    private void saveLandMarkData(final OutputBitStream ostream) throws IOException {
        final int numLandMarks = this.m_landMarkData.size();
        if (numLandMarks > 32767) {
            throw new IllegalArgumentException("Nombre de landmark > 32767");
        }
        ostream.writeShort((short)(numLandMarks & 0xFFFF));
        final TIntObjectIterator<LandMarkDef> it = this.m_landMarkData.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().save(ostream);
        }
    }
    
    public void clear() {
        this.m_landMarkData.clear();
    }
}
