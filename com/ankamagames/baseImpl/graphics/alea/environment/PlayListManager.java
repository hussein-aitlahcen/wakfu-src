package com.ankamagames.baseImpl.graphics.alea.environment;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import org.jetbrains.annotations.*;
import gnu.trove.*;

public final class PlayListManager
{
    private static final Logger m_logger;
    private static final PlayListManager m_instance;
    private String m_fileName;
    private final TShortObjectHashMap<PlayListData> m_playLists;
    private short m_suid;
    
    private PlayListManager() {
        super();
        this.m_playLists = new TShortObjectHashMap<PlayListData>();
        this.m_suid = 0;
    }
    
    public static PlayListManager getInstance() {
        return PlayListManager.m_instance;
    }
    
    public String getFileName() {
        return this.m_fileName;
    }
    
    public void setFile(final String fileName) {
        this.m_fileName = fileName;
    }
    
    public short generateSUID() {
        final short suid = this.m_suid;
        this.m_suid = (short)(suid + 1);
        return suid;
    }
    
    public void load() {
        this.clear();
        if (this.m_fileName == null) {
            return;
        }
        try {
            final ExtendedDataInputStream bitStream = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(this.m_fileName));
            this.load(bitStream);
            bitStream.close();
        }
        catch (IOException e) {
            PlayListManager.m_logger.error((Object)("Error while loading PlayList file : " + this.m_fileName), (Throwable)e);
        }
    }
    
    public void save() {
        if (this.m_fileName == null) {
            return;
        }
        try {
            final OutputStream stream = FileHelper.createFileOutputStream(this.m_fileName);
            final OutputBitStream bitStream = new OutputBitStream(stream);
            this.save(bitStream);
            bitStream.close();
            stream.close();
        }
        catch (IOException e) {
            PlayListManager.m_logger.error((Object)("Error while saving PlayList file : " + this.m_fileName), (Throwable)e);
        }
    }
    
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        final short size = istream.readShort();
        this.m_playLists.ensureCapacity(size);
        for (short i = 0; i < size; ++i) {
            final PlayListData data = new PlayListData();
            data.load(istream);
            this.add(data);
        }
    }
    
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        ostream.writeShort((short)this.m_playLists.size());
        final TShortObjectIterator<PlayListData> it = this.m_playLists.iterator();
        while (it.hasNext()) {
            it.advance();
            it.value().save(ostream);
        }
    }
    
    public final void add(final PlayListData data) {
        this.m_playLists.put(data.getPlayListId(), data);
    }
    
    public PlayListData get(final short playListId) {
        return this.m_playLists.get(playListId);
    }
    
    public short contains(final PlayListData playList) {
        final TShortObjectIterator<PlayListData> it = this.m_playLists.iterator();
        while (it.hasNext()) {
            it.advance();
            if (it.value().isEqualTo(playList)) {
                return it.key();
            }
        }
        return -1;
    }
    
    public void clear() {
        this.m_playLists.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayListManager.class);
        m_instance = new PlayListManager();
    }
}
