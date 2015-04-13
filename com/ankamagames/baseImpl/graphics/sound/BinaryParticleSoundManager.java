package com.ankamagames.baseImpl.graphics.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.sound.binary.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.sound.openAL.*;
import gnu.trove.*;

public class BinaryParticleSoundManager extends BaseParticleSoundManager
{
    private static final Logger m_logger;
    private static final int VERSION = 1;
    private final TIntObjectHashMap<ParticleSoundData> m_soundData;
    
    public BinaryParticleSoundManager() {
        super();
        this.m_soundData = new TIntObjectHashMap<ParticleSoundData>();
    }
    
    public void loadBinary(final String file) throws IOException {
        final ExtendedDataInputStream is = ExtendedDataInputStream.wrap(ContentFileHelper.readFile(file));
        final int version = is.readInt();
        for (int numData = is.readInt(), i = 0; i < numData; ++i) {
            final int id = is.readInt();
            final ParticleSoundData particleSoundData = new ParticleSoundData(is);
            this.m_soundData.put(id, particleSoundData);
        }
        is.close();
    }
    
    public void saveBinary(final String file) throws IOException {
        final OutputBitStream os = new OutputBitStream(FileHelper.createFileOutputStream(file));
        os.writeInt(1);
        os.writeInt(this.m_soundData.size());
        final TIntObjectIterator<ParticleSoundData> it = this.m_soundData.iterator();
        while (it.hasNext()) {
            it.advance();
            os.writeInt(it.key());
            it.value().save(os);
        }
        os.close();
    }
    
    public void putData(final int id, final ParticleSoundData data) {
        this.m_soundData.put(id, data);
    }
    
    @Override
    public boolean playApsSound(final int apsFileId, final int apsId, final int fightId, final int duration) {
        final ParticleSoundData data = this.m_soundData.get(apsFileId);
        if (data == null) {
            return false;
        }
        final boolean canStopSound = data.isStopOnRemoveAps() || data.isLoop();
        if (data.getDelay() != 0) {
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    BinaryParticleSoundManager.this.doPlayAps(apsId, fightId, duration, data, canStopSound);
                }
            }, data.getDelay(), 1);
            return canStopSound;
        }
        return this.doPlayAps(apsId, fightId, duration, data, canStopSound);
    }
    
    private boolean doPlayAps(final int apsId, final int fightId, final int duration, final ParticleSoundData data, final boolean canStopSound) {
        if (!this.canPlaySound()) {
            return canStopSound;
        }
        final long[] soundIds = data.getSoundIds();
        final byte[] gains = data.getGains();
        final int index = MathHelper.random(soundIds.length);
        final long date = System.currentTimeMillis();
        final long soundFileId = soundIds[index];
        if (!this.canPlaySoundById(date, soundFileId)) {
            return canStopSound;
        }
        final float gain = gains[index] / 100.0f;
        final long endDate = (duration != -1) ? (date + duration) : -1L;
        final long fadeDate = (data.getFadeOutTime() != 0) ? (endDate - data.getFadeOutTime()) : -1L;
        final IsoParticleSystem ps = IsoParticleSystemManager.getInstance().getParticleSystem(apsId);
        try {
            if (soundFileId != 0L) {
                AudioSourceDefinition source;
                if (ps != null) {
                    source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gain, data.isLoop() ? 0 : 1, endDate, fadeDate, fightId, ps, data.getRollOffId(), false);
                }
                else {
                    source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gain, data.isLoop() ? 0 : 1, endDate, fadeDate, fightId);
                }
                if (source != null) {
                    this.registerSound(apsId, source.getSoundUID(), date, soundFileId);
                }
            }
            else {
                BinaryParticleSoundManager.m_logger.error((Object)"Id du son nul");
            }
        }
        catch (Exception e) {
            BinaryParticleSoundManager.m_logger.error((Object)("soundExtension or soundPath not initialized " + e));
        }
        return canStopSound;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BinaryParticleSoundManager.class);
    }
}
