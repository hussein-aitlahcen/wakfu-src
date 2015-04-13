package com.ankamagames.framework.sound.stream;

import org.apache.log4j.*;
import java.util.concurrent.atomic.*;
import com.jcraft.jogg.*;
import com.jcraft.jorbis.*;
import java.io.*;
import java.nio.*;

public class JOrbisStream implements AudioStream
{
    protected static final Logger m_logger;
    private static final int CHUNK_SIZE = 8500;
    private static final int SEEK_SET = 0;
    private static final int SEEK_END = 1;
    private static final int OV_FALSE = -1;
    private static final int OV_EOF = -2;
    private static final int OV_EREAD = -128;
    private static final int OV_EFAULT = -129;
    private static final int OV_ENOTVORBIS = -130;
    private int m_cacheWeight;
    private boolean m_isReset;
    private final AtomicInteger m_refCount;
    private int m_chunkSize;
    private boolean m_swapBytes;
    private static final boolean m_bigEndian;
    private SyncState m_oggSyncState;
    private StreamState m_oggStreamState;
    private Page m_oggPage;
    private Packet m_oggPacket;
    private DspState m_dspState;
    private Block m_block;
    private int m_links;
    private long m_offset;
    private long[] m_offsets;
    private long[] m_dataOffsets;
    private int[] m_serialnos;
    private long[] m_pcmLengths;
    private Info[] m_info;
    private Comment[] m_comments;
    private long m_pcmOffset;
    private boolean m_decodeReady;
    private int m_currentSerialno;
    private int m_currentLink;
    private AudioStreamProvider m_vorbisStream;
    private float m_timeTotal;
    private static final int BUFFSIZE = 8192;
    private final byte[] m_conversionBuffer;
    private final float[][][] m_pcmf_buffer;
    private final int[] _index;
    
    public JOrbisStream() {
        super();
        this.m_isReset = true;
        this.m_refCount = new AtomicInteger(0);
        this.m_chunkSize = 8500;
        this.m_swapBytes = false;
        this.m_links = 0;
        this.m_decodeReady = false;
        this.m_conversionBuffer = new byte[8192];
        this.m_pcmf_buffer = new float[1][][];
        this._index = new int[2];
    }
    
    @Override
    public String getDescription() {
        return this.m_vorbisStream.getDescription();
    }
    
    @Override
    public String getFileId() {
        return this.m_vorbisStream.getFileId();
    }
    
    private boolean openSeekable() {
        final Info initialInfo = new Info();
        final Comment initialComment = new Comment();
        this.m_chunkSize = Math.min(8500, (int)length(this.m_vorbisStream));
        final Page page = new Page();
        final int[] testSerialno = { 0 };
        final int ret = this.fetchHeaders(initialInfo, initialComment, testSerialno, null);
        final int serialno = testSerialno[0];
        final int dataOffset = (int)this.m_offset;
        this.m_oggStreamState.clear();
        if (ret < 0) {
            return false;
        }
        seek(this.m_vorbisStream, 0L, 1);
        this.m_offset = tell(this.m_vorbisStream);
        final long end = this.getPreviousPage(page);
        if (page.serialno() != serialno) {
            if (this.bisectForwardSerialno(0L, 0L, end + 1L, serialno, 0) < 0) {
                return false;
            }
        }
        else if (this.bisectForwardSerialno(0L, end, end + 1L, serialno, 0) < 0) {
            return false;
        }
        this.prefetchAllHeaders(initialInfo, initialComment, dataOffset);
        this.rawSeek(this.m_dataOffsets[0]);
        return true;
    }
    
    private boolean openNonSeekable() {
        this.m_links = 1;
        this.m_info = new Info[] { new Info() };
        this.m_comments = new Comment[] { new Comment() };
        final int[] serialno = { 0 };
        if (this.fetchHeaders(this.m_info[0], this.m_comments[0], serialno, null) == -1) {
            return false;
        }
        this.m_currentSerialno = serialno[0];
        this.makeDecodeReady();
        return true;
    }
    
    private int getData() {
        final int index = this.m_oggSyncState.buffer(this.m_chunkSize);
        if (index == -1) {
            JOrbisStream.m_logger.debug((Object)("Stream corrompu : " + this.getDescription()));
            return -128;
        }
        final byte[] buffer = this.m_oggSyncState.getData();
        int bytes;
        try {
            bytes = this.m_vorbisStream.read(buffer, index, this.m_chunkSize);
        }
        catch (Exception e) {
            return -128;
        }
        this.m_oggSyncState.wrote(bytes);
        if (bytes == -1) {
            bytes = 0;
        }
        return bytes;
    }
    
    private void makeDecodeReady() {
        this.m_dspState.synthesis_init(this.m_info[0]);
        this.m_block.init(this.m_dspState);
        this.m_decodeReady = true;
    }
    
    private void decodeClear() {
        this.m_oggStreamState.clear();
        this.m_dspState.clear();
        this.m_block.clear();
        this.m_decodeReady = false;
    }
    
    private int getNextPage(final Page page, long boundary) {
        if (boundary > 0L) {
            boundary += this.m_offset;
        }
        while (boundary <= 0L || this.m_offset < boundary) {
            final int more = this.m_oggSyncState.pageseek(page);
            if (more < 0) {
                this.m_offset -= more;
            }
            else {
                if (more != 0) {
                    final int ret = (int)this.m_offset;
                    this.m_offset += more;
                    return ret;
                }
                if (boundary == 0L) {
                    return -1;
                }
                final int ret = this.getData();
                if (ret == 0) {
                    return -2;
                }
                if (ret < 0) {
                    return -128;
                }
                continue;
            }
        }
        return -1;
    }
    
    private int getPreviousPage(final Page page) {
        long begin = this.m_offset;
        int offset = -1;
        while (offset == -1) {
            begin -= this.m_chunkSize;
            if (begin < 0L) {
                begin = 0L;
            }
            this.seekHelper(begin);
            while (this.m_offset < begin + this.m_chunkSize) {
                final int ret = this.getNextPage(page, begin + this.m_chunkSize - this.m_offset);
                if (ret == -128) {
                    return -128;
                }
                if (ret < 0) {
                    if (offset == -1) {
                        return -1;
                    }
                    continue;
                }
                else {
                    offset = ret;
                }
            }
        }
        this.seekHelper(offset);
        final int ret = this.getNextPage(page, this.m_chunkSize);
        if (ret < 0) {
            return -129;
        }
        return offset;
    }
    
    private void seekHelper(final long offset) {
        seek(this.m_vorbisStream, offset, 0);
        this.m_offset = offset;
        this.m_oggSyncState.reset();
    }
    
    private int bisectForwardSerialno(final long begin, long searched, final long end, final int currentno, final int m) {
        long endsearched = end;
        long next = end;
        final Page page = new Page();
        while (searched < endsearched) {
            long bisect;
            if (endsearched - searched < this.m_chunkSize) {
                bisect = searched;
            }
            else {
                bisect = (searched + endsearched) / 2L;
            }
            this.seekHelper(bisect);
            final int ret = this.getNextPage(page, -1L);
            if (ret == -128) {
                return -128;
            }
            if (ret < 0 || page.serialno() != currentno) {
                endsearched = bisect;
                if (ret < 0) {
                    continue;
                }
                next = ret;
            }
            else {
                searched = ret + page.header_len + page.body_len;
            }
        }
        this.seekHelper(next);
        int ret = this.getNextPage(page, -1L);
        if (ret == -128) {
            return -128;
        }
        if (searched >= end || ret == -1) {
            this.m_links = m + 1;
            (this.m_offsets = new long[m + 2])[m + 1] = searched;
        }
        else {
            ret = this.bisectForwardSerialno(next, this.m_offset, end, page.serialno(), m + 1);
            if (ret == -128) {
                return -128;
            }
        }
        this.m_offsets[m] = begin;
        return 0;
    }
    
    public int fetchHeaders(final Info info, final Comment comment, final int[] serialno, Page page) {
        if (page == null) {
            page = new Page();
            final int retValue = this.getNextPage(page, this.m_chunkSize);
            if (retValue == -128) {
                return -128;
            }
            if (retValue < 0) {
                return -130;
            }
        }
        if (serialno != null) {
            serialno[0] = page.serialno();
        }
        this.m_oggStreamState.init(page.serialno());
        info.init();
        comment.init();
        final Packet packet = new Packet();
        int i = 0;
        while (i < 3) {
            this.m_oggStreamState.pagein(page);
            while (i < 3) {
                final int result = this.m_oggStreamState.packetout(packet);
                if (result == 0) {
                    break;
                }
                if (result == -1) {
                    info.clear();
                    this.m_oggStreamState.clear();
                    return -1;
                }
                if (info.synthesis_headerin(comment, packet) != 0) {
                    info.clear();
                    this.m_oggStreamState.clear();
                    return -1;
                }
                ++i;
            }
            if (i < 3 && this.getNextPage(page, 1L) < 0) {
                info.clear();
                this.m_oggStreamState.clear();
                return -1;
            }
        }
        return 0;
    }
    
    public Info[] getInfo() {
        return this.m_info;
    }
    
    private void prefetchAllHeaders(final Info firstInfo, final Comment firstComment, final int dataoffset) {
        final Page page = new Page();
        this.m_info = new Info[this.m_links];
        this.m_comments = new Comment[this.m_links];
        this.m_dataOffsets = new long[this.m_links];
        this.m_pcmLengths = new long[this.m_links];
        this.m_serialnos = new int[this.m_links];
        for (int i = 0; i < this.m_links; ++i) {
            if (firstInfo != null && firstComment != null && i == 0) {
                this.m_info[i] = firstInfo;
                this.m_comments[i] = firstComment;
                this.m_dataOffsets[i] = dataoffset;
            }
            else {
                this.seekHelper(this.m_offsets[i]);
                this.m_info[i] = new Info();
                this.m_comments[i] = new Comment();
                if (this.fetchHeaders(this.m_info[i], this.m_comments[i], null, null) == -1) {
                    this.m_dataOffsets[i] = -1L;
                }
                else {
                    this.m_dataOffsets[i] = this.m_offset;
                    this.m_oggStreamState.clear();
                }
            }
            final long end = this.m_offsets[i + 1];
            this.seekHelper(end);
            while (true) {
                final int ret = this.getPreviousPage(page);
                if (ret == -1) {
                    this.m_info[i].clear();
                    break;
                }
                if (page.granulepos() != -1L) {
                    this.m_serialnos[i] = page.serialno();
                    this.m_pcmLengths[i] = page.granulepos();
                    break;
                }
            }
        }
    }
    
    @Override
    public boolean reinitialize() {
        return this.initialize(this.m_vorbisStream);
    }
    
    @Override
    public boolean initialize(final AudioStreamProvider asp) {
        this.m_vorbisStream = asp;
        try {
            this.m_vorbisStream.openStream();
        }
        catch (IOException e) {
            JOrbisStream.m_logger.info((Object)("Probl\u00e8me \u00e0 l'ouverture du stream " + asp.getDescription()));
            try {
                this.m_vorbisStream.close();
            }
            catch (IOException e2) {
                JOrbisStream.m_logger.info((Object)("Probl\u00e8me au nettoyage du stream " + asp.getDescription()));
            }
            return false;
        }
        this.m_offset = 0L;
        final boolean reuse = this.m_oggPage != null;
        if (reuse) {
            assert this.m_oggPacket != null && this.m_oggSyncState != null && this.m_oggStreamState != null && this.m_dspState != null && this.m_block != null;
            this.m_oggPage.reinit();
            this.m_oggPacket.reinit();
            this.m_oggSyncState.reinit();
            this.m_oggStreamState.reinit();
            this.m_dspState = new DspState();
            this.m_block.init(this.m_dspState);
        }
        else {
            assert this.m_oggPacket == null && this.m_oggSyncState == null && this.m_oggStreamState == null && this.m_dspState == null && this.m_block == null;
            this.m_oggPage = new Page();
            this.m_oggPacket = new Packet();
            this.m_oggSyncState = new SyncState();
            this.m_oggStreamState = new StreamState();
            this.m_dspState = new DspState();
            this.m_block = new Block(this.m_dspState);
        }
        if (asp.isSeekable()) {
            return this.openSeekable();
        }
        return this.openNonSeekable();
    }
    
    private int processPacket(final boolean readPage) {
        while (true) {
            if (this.m_decodeReady) {
                final int result = this.m_oggStreamState.packetout(this.m_oggPacket);
                if (result > 0) {
                    long granulepos = this.m_oggPacket.granulepos;
                    if (this.m_block.synthesis(this.m_oggPacket) == 0) {
                        this.m_dspState.synthesis_blockin(this.m_block);
                        if (granulepos != -1L) {
                            final int link = this.m_vorbisStream.isSeekable() ? this.m_currentLink : 0;
                            final int samples = this.m_dspState.synthesis_pcmout(null, null);
                            granulepos = Math.max(0L, granulepos - samples);
                            for (int i = 0; i < link; ++i) {
                                granulepos += this.m_pcmLengths[i];
                            }
                            this.m_pcmOffset = granulepos;
                        }
                        return 1;
                    }
                }
            }
            if (!readPage) {
                return 0;
            }
            if (this.getNextPage(this.m_oggPage, -1L) < 0) {
                return 0;
            }
            if (this.m_decodeReady && this.m_currentSerialno != this.m_oggPage.serialno()) {
                this.decodeClear();
            }
            if (!this.m_decodeReady) {
                if (this.m_vorbisStream.isSeekable()) {
                    this.m_currentSerialno = this.m_oggPage.serialno();
                    int j;
                    for (j = 0; j < this.m_links && this.m_serialnos[j] != this.m_currentSerialno; ++j) {}
                    if (j == this.m_links) {
                        return -1;
                    }
                    this.m_currentLink = j;
                    this.m_oggStreamState.init(this.m_currentSerialno);
                    this.m_oggStreamState.reset();
                }
                else {
                    final int[] serialnos = { 0 };
                    final int ret = this.fetchHeaders(this.m_info[0], this.m_comments[0], serialnos, this.m_oggPage);
                    this.m_currentSerialno = serialnos[0];
                    if (ret != 0) {
                        return ret;
                    }
                    ++this.m_currentLink;
                }
                this.makeDecodeReady();
            }
            this.m_oggStreamState.pagein(this.m_oggPage);
        }
    }
    
    public long rawTotal(final int i) {
        if (!this.m_vorbisStream.isSeekable() || i >= this.m_links) {
            return -1L;
        }
        if (i < 0) {
            long acc = 0L;
            for (int j = 0; j < this.m_links; ++j) {
                acc += this.rawTotal(j);
            }
            return acc;
        }
        return this.m_offsets[i + 1] - this.m_offsets[i];
    }
    
    public long pcmTotal(final int i) {
        if (!this.m_vorbisStream.isSeekable() || i >= this.m_links) {
            return -1L;
        }
        if (i < 0) {
            long acc = 0L;
            for (int j = 0; j < this.m_links; ++j) {
                acc += this.pcmTotal(j);
            }
            return acc;
        }
        return this.m_pcmLengths[i];
    }
    
    public long uncompressedBytesTotal() {
        if (!this.m_vorbisStream.isSeekable()) {
            return -1L;
        }
        long total = 0L;
        for (int j = 0; j < this.m_links; ++j) {
            total += this.m_pcmLengths[j] * this.m_info[j].channelsCount * 2L;
        }
        return total;
    }
    
    public float timeTotal(final int i) {
        if (!this.m_vorbisStream.isSeekable() || i >= this.m_links) {
            return -1.0f;
        }
        if (i < 0) {
            if (this.m_timeTotal == 0.0f) {
                for (int j = 0; j < this.m_links; ++j) {
                    this.m_timeTotal += this.timeTotal(j);
                }
            }
            return this.m_timeTotal;
        }
        return this.m_pcmLengths[i] / this.m_info[i].rate;
    }
    
    @Override
    public int rawSeek(final long pos) {
        if (!this.m_vorbisStream.isSeekable()) {
            return -1;
        }
        this.m_pcmOffset = -1L;
        this.decodeClear();
        if (pos < 0L || pos > this.m_offsets[this.m_links]) {
            return -1;
        }
        this.seekHelper(pos);
        switch (this.processPacket(true)) {
            case 0: {
                this.m_pcmOffset = this.pcmTotal(-1);
                return 0;
            }
            case -1: {
                this.m_pcmOffset = -1L;
                this.decodeClear();
                return -1;
            }
            default: {
                while (true) {
                    switch (this.processPacket(false)) {
                        case 0: {
                            return 0;
                        }
                        case -1: {
                            this.m_pcmOffset = -1L;
                            this.decodeClear();
                            return -1;
                        }
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public int pcmSeek(final long pos) {
        if (!this.m_vorbisStream.isSeekable()) {
            return -1;
        }
        long total = this.pcmTotal(-1);
        if (pos < 0L || pos > total) {
            this.m_pcmOffset = -1L;
            this.decodeClear();
            return -1;
        }
        int link;
        for (link = this.m_links - 1; link >= 0; --link) {
            total -= this.m_pcmLengths[link];
            if (pos >= total) {
                break;
            }
        }
        final long target = pos - total;
        long end = this.m_offsets[link + 1];
        long begin = this.m_offsets[link];
        int best = (int)begin;
        final Page page = new Page();
        while (begin < end) {
            long bisect;
            if (end - begin < this.m_chunkSize) {
                bisect = begin;
            }
            else {
                bisect = (end + begin) / 2L;
            }
            this.seekHelper(bisect);
            final int ret = this.getNextPage(page, end - bisect);
            if (ret == -1) {
                end = bisect;
            }
            else {
                final long granulepos = page.granulepos();
                if (granulepos < target) {
                    best = ret;
                    begin = this.m_offset;
                }
                else {
                    end = bisect;
                }
            }
        }
        if (this.rawSeek(best) != 0) {
            this.m_pcmOffset = -1L;
            this.decodeClear();
            return -1;
        }
        if (this.m_pcmOffset >= pos) {
            this.m_pcmOffset = -1L;
            this.decodeClear();
            return -1;
        }
        if (pos > this.pcmTotal(-1)) {
            this.m_pcmOffset = -1L;
            this.decodeClear();
            return -1;
        }
        while (this.m_pcmOffset < pos) {
            final int target2 = (int)(pos - this.m_pcmOffset);
            final int[] _index = new int[this.m_info[this.m_currentLink].channelsCount];
            int samples = this.m_dspState.synthesis_pcmout(this.m_pcmf_buffer, _index);
            if (samples > target2) {
                samples = target2;
            }
            this.m_dspState.synthesis_read(samples);
            this.m_pcmOffset += samples;
            if (samples < target2 && this.processPacket(true) == 0) {
                this.m_pcmOffset = this.pcmTotal(-1);
            }
        }
        return 0;
    }
    
    @Override
    public int timeSeek(final float seconds) {
        if (!this.m_vorbisStream.isSeekable()) {
            return -1;
        }
        long pcmTotal = this.pcmTotal(-1);
        float timeTotal = this.timeTotal(-1);
        if (seconds < 0.0f || seconds > timeTotal) {
            this.m_pcmOffset = -1L;
            this.decodeClear();
            return -1;
        }
        int link;
        for (link = this.m_links - 1; link >= 0; --link) {
            pcmTotal -= this.m_pcmLengths[link];
            timeTotal -= this.timeTotal(link);
            if (seconds >= timeTotal) {
                break;
            }
        }
        final long target = (long)(pcmTotal + (seconds - timeTotal) * this.m_info[link].rate);
        return this.pcmSeek(target);
    }
    
    @Override
    public long rawTell() {
        return this.m_offset;
    }
    
    @Override
    public long pcmTell() {
        return this.m_pcmOffset;
    }
    
    @Override
    public float timeTell() {
        if (this.m_links < 1) {
            return 0.0f;
        }
        float timeTotal = 0.0f;
        long pcmTotal = 0L;
        int link = -1;
        if (this.m_vorbisStream.isSeekable()) {
            pcmTotal = this.pcmTotal(-1);
            timeTotal = this.timeTotal(-1);
            for (link = this.m_links - 1; link >= 0; --link) {
                pcmTotal -= this.m_pcmLengths[link];
                timeTotal -= this.timeTotal(link);
                if (this.m_pcmOffset >= pcmTotal) {
                    break;
                }
            }
        }
        return timeTotal + (this.m_pcmOffset - pcmTotal) / this.m_info[link].rate;
    }
    
    @Override
    public int read(final ByteBuffer bb, final int pos) {
        this.m_isReset = false;
        bb.position(pos);
        while (bb.remaining() > 0) {
            boolean needToProcessPacket = true;
            if (this.m_decodeReady) {
                final Info info = this.m_info[this.m_currentLink];
                final int totalSamples = this.m_dspState.synthesis_pcmout(this.m_pcmf_buffer, this._index);
                final float[][] pcm = this.m_pcmf_buffer[0];
                if (totalSamples > 0) {
                    final int channelsCount = info.channelsCount;
                    final int bytesPerSample = channelsCount * 2;
                    int samples = Math.min(totalSamples, bb.remaining() / bytesPerSample);
                    samples = Math.min(samples, 8192 / bytesPerSample);
                    needToProcessPacket = (samples == totalSamples);
                    for (int i = 0; i < channelsCount; ++i) {
                        int ptr = i * 2;
                        final int mono = this._index[i];
                        for (int j = 0; j < samples; ++j) {
                            int val = (int)(pcm[i][mono + j] * 32767.0f);
                            if (val > 32767) {
                                val = 32767;
                            }
                            if (val < -32768) {
                                val = -32768;
                            }
                            if (val < 0) {
                                val |= 0x8000;
                            }
                            if (JOrbisStream.m_bigEndian) {
                                this.m_conversionBuffer[ptr] = (byte)(val >>> 8 & 0xFF);
                                this.m_conversionBuffer[ptr + 1] = (byte)(val & 0xFF);
                            }
                            else {
                                this.m_conversionBuffer[ptr] = (byte)(val & 0xFF);
                                this.m_conversionBuffer[ptr + 1] = (byte)(val >>> 8 & 0xFF);
                            }
                            ptr += 2 * channelsCount;
                        }
                    }
                    final int writtenBytesLength = samples * bytesPerSample;
                    bb.put(this.m_conversionBuffer, 0, writtenBytesLength);
                    this.m_dspState.synthesis_read(samples);
                    this.m_pcmOffset += samples;
                }
            }
            if (needToProcessPacket) {
                switch (this.processPacket(true)) {
                    case 0: {
                        return -(bb.position() - pos);
                    }
                    case -1: {
                        return -(bb.position() - pos);
                    }
                    default: {
                        continue;
                    }
                }
            }
        }
        return bb.position() - pos;
    }
    
    @Override
    public int getCurrentBytesPerSample() {
        return this.m_info[this.m_currentLink].channelsCount * 2;
    }
    
    @Override
    public int getNumChannels() {
        return this.m_info[0].channelsCount;
    }
    
    @Override
    public int getSampleRate() {
        return this.m_info[0].rate;
    }
    
    @Override
    public void setSwap(final boolean swap) {
        this.m_swapBytes = swap;
    }
    
    @Override
    public int getDurationInMs() {
        return (int)(this.timeTotal(-1) * 1000.0f);
    }
    
    public void loop() {
        if (this.m_vorbisStream.isSeekable()) {
            this.rawSeek(this.m_dataOffsets[0]);
        }
        else {
            this.reset();
        }
    }
    
    @Override
    public void close() {
        if (this.m_vorbisStream != null) {
            try {
                this.m_vorbisStream.close();
            }
            catch (IOException e) {
                JOrbisStream.m_logger.error((Object)("Impossible de fermer le flux pour le stream " + this.m_vorbisStream.getDescription()));
            }
        }
    }
    
    @Override
    public void reset() {
        if (!this.m_isReset) {
            if (this.m_vorbisStream.isSeekable()) {
                this.rawSeek(this.m_dataOffsets[0]);
            }
            else {
                this.m_oggStreamState.clear();
                this.m_block.clear();
                this.m_dspState.clear();
                if (this.m_info != null) {
                    for (final Info info : this.m_info) {
                        info.clear();
                    }
                }
                this.m_oggSyncState.clear();
                try {
                    if (this.m_vorbisStream != null) {
                        this.m_vorbisStream.reset();
                    }
                }
                catch (Exception e) {
                    JOrbisStream.m_logger.error((Object)"Exception", (Throwable)e);
                }
                this.m_links = 0;
                this.m_offset = 0L;
                this.m_offsets = null;
                this.m_dataOffsets = null;
                this.m_serialnos = null;
                this.m_pcmLengths = null;
                this.m_info = null;
                this.m_comments = null;
                this.m_pcmOffset = 0L;
                this.m_decodeReady = false;
                this.m_currentSerialno = 0;
                this.m_currentLink = 0;
                this.initialize(this.m_vorbisStream);
            }
            this.m_isReset = true;
        }
    }
    
    @Override
    public int getWeight() {
        return this.m_cacheWeight;
    }
    
    @Override
    public void setWeight(final int weight) {
        this.m_cacheWeight = weight;
    }
    
    @Override
    public int getRefCount() {
        return this.m_refCount.get();
    }
    
    @Override
    public void addRefCount() {
        this.m_refCount.incrementAndGet();
    }
    
    @Override
    public void subRefCount() {
        this.m_refCount.decrementAndGet();
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName());
        buffer.append(" : url=").append(this.m_vorbisStream.getDescription());
        return buffer.toString();
    }
    
    private static long length(final AudioStreamProvider stream) {
        if (!stream.isSeekable()) {
            return -1L;
        }
        try {
            return stream.length();
        }
        catch (IOException ex) {
            JOrbisStream.m_logger.debug((Object)"Probl\u00e8me lors du length() sur le stream !", (Throwable)ex);
            return -1L;
        }
    }
    
    private static int seek(final AudioStreamProvider stream, final long offset, final int type) {
        if (!stream.isSeekable()) {
            return -1;
        }
        try {
            if (type == 0) {
                stream.seek(offset);
            }
            else if (type == 1) {
                stream.seek(stream.length() - offset);
            }
            return 0;
        }
        catch (IOException ex) {
            JOrbisStream.m_logger.debug((Object)"Probl\u00e8me lors du seek sur le stream !", (Throwable)ex);
            return -1;
        }
    }
    
    private static long tell(final AudioStreamProvider stream) {
        if (!stream.isSeekable()) {
            return 0L;
        }
        try {
            return stream.tell();
        }
        catch (IOException e) {
            JOrbisStream.m_logger.debug((Object)"Probl\u00e8me lors du tell sur le stream !", (Throwable)e);
            return 0L;
        }
    }
    
    public Comment getComment() {
        if (this.m_comments == null || this.m_comments.length == 0) {
            return null;
        }
        return this.m_comments[0];
    }
    
    static {
        m_logger = Logger.getLogger((Class)JOrbisStream.class);
        m_bigEndian = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
    }
}
