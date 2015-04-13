package com.ankamagames.baseImpl.graphics.alea.sound;

import org.apache.log4j.*;
import java.io.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;

public class SoundBank
{
    private static final Logger m_logger;
    private final TIntObjectHashMap<SoundData> m_sounds;
    private String m_fileName;
    private static final SoundBank m_instance;
    
    public static SoundBank getInstance() {
        return SoundBank.m_instance;
    }
    
    private SoundBank() {
        super();
        this.m_sounds = new TIntObjectHashMap<SoundData>();
    }
    
    public String getFileName() {
        return this.m_fileName;
    }
    
    public void setFile(final String fileName) {
        this.m_fileName = fileName;
    }
    
    public void load() {
        try {
            assert this.m_fileName != null && !new File(this.m_fileName).isDirectory();
            final ExtendedDataInputStream bitStream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(this.m_fileName));
            this.load(bitStream);
            bitStream.close();
        }
        catch (IOException e) {
            SoundBank.m_logger.error((Object)"", (Throwable)e);
        }
    }
    
    private void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        for (int size = istream.readInt(), i = 0; i < size; ++i) {
            final SoundData data = new SoundData();
            data.load(istream);
            if (this.m_sounds.put(data.getId(), data) != null) {
                SoundBank.m_logger.error((Object)("SoundBank already loaded : " + data.getId()));
            }
        }
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        final TIntObjectIterator<SoundData> iter = this.m_sounds.iterator();
        ostream.writeInt(this.m_sounds.size());
        while (iter.hasNext()) {
            iter.advance();
            iter.value().save(ostream);
        }
    }
    
    public final SoundData add(final SoundData soundData) {
        assert !this.m_sounds.contains(soundData.getId());
        return this.m_sounds.put(soundData.getId(), soundData);
    }
    
    public final SoundData get(final int soundId) {
        return this.m_sounds.get(soundId);
    }
    
    public final void clear() {
        this.m_sounds.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoundBank.class);
        m_instance = new SoundBank();
    }
}
