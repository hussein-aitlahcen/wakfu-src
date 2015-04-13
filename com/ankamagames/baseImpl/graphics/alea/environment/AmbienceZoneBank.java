package com.ankamagames.baseImpl.graphics.alea.environment;

import org.apache.log4j.*;
import java.io.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;

public class AmbienceZoneBank
{
    private static final Logger m_logger;
    private final TIntObjectHashMap<AmbienceData> m_ambiences;
    private int m_worldId;
    private String m_fileName;
    private static final AmbienceZoneBank m_instance;
    
    public static AmbienceZoneBank getInstance() {
        return AmbienceZoneBank.m_instance;
    }
    
    private AmbienceZoneBank() {
        super();
        this.m_ambiences = new TIntObjectHashMap<AmbienceData>();
    }
    
    public String getFileName() {
        return this.m_fileName;
    }
    
    public void setFile(final String fileName) {
        this.m_fileName = fileName;
    }
    
    public void setWorldId(final int worldId) {
        this.m_worldId = worldId;
    }
    
    public void load() {
        this.clear();
        if (this.m_fileName == null) {
            return;
        }
        final String filename = String.format(this.m_fileName, this.m_worldId);
        try {
            final ExtendedDataInputStream bitStream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(filename));
            this.load(bitStream);
            bitStream.close();
        }
        catch (IOException e) {
            AmbienceZoneBank.m_logger.error((Object)("Error while loading AmbianceZone file : " + this.m_fileName + " (world " + this.m_worldId + ") : " + filename), (Throwable)e);
        }
    }
    
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        final int size = istream.readInt();
        this.m_ambiences.ensureCapacity(size);
        for (int i = 0; i < size; ++i) {
            final AmbienceData data = new AmbienceData();
            data.load(istream);
            assert !this.m_ambiences.contains(data.m_zoneId);
            this.m_ambiences.put(data.m_zoneId, data);
        }
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        final TIntObjectIterator<AmbienceData> iter = this.m_ambiences.iterator();
        ostream.writeInt(this.m_ambiences.size());
        while (iter.hasNext()) {
            iter.advance();
            iter.value().save(ostream);
        }
    }
    
    public final void add(final AmbienceData data) {
        assert !this.m_ambiences.contains(data.m_zoneId);
        this.m_ambiences.put(data.m_zoneId, data);
    }
    
    public AmbienceData get(final int zoneId) {
        return this.m_ambiences.get(zoneId);
    }
    
    public void clear() {
        this.m_ambiences.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AmbienceZoneBank.class);
        m_instance = new AmbienceZoneBank();
    }
}
