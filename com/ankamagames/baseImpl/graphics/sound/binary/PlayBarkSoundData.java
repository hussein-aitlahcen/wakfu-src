package com.ankamagames.baseImpl.graphics.sound.binary;

import org.apache.log4j.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.group.*;
import gnu.trove.*;

public class PlayBarkSoundData implements AnimatedElementRunScriptData
{
    private static final Logger m_logger;
    private int m_barkId;
    private byte m_gain;
    private int m_breedId;
    
    public PlayBarkSoundData() {
        super();
        this.m_gain = 100;
        this.m_breedId = -1;
    }
    
    public PlayBarkSoundData(final ExtendedDataInputStream is) {
        super();
        this.m_gain = 100;
        this.m_breedId = -1;
        this.load(is);
    }
    
    public PlayBarkSoundData(final int barkId, final byte gain, final int breedId) {
        super();
        this.m_gain = 100;
        this.m_breedId = -1;
        this.m_barkId = barkId;
        this.m_gain = gain;
        this.m_breedId = breedId;
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        this.m_barkId = is.readInt();
        this.m_gain = is.readByte();
        this.m_breedId = is.readInt();
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeInt(this.m_barkId);
        os.writeByte(this.m_gain);
        os.writeInt(this.m_breedId);
    }
    
    @Override
    public void play(final AnimatedObject ae) {
        if (!SoundDataHelper.canPlaySound(ae)) {
            return;
        }
        if (this.m_barkId == 0) {
            PlayBarkSoundData.m_logger.debug((Object)"Id du son nul");
            return;
        }
        try {
            final SoundFunctionsLibrary.BarkData data = SoundFunctionsLibrary.getInstance().getBarkData(this.m_barkId, (ObservedSource)ae, this.m_breedId);
            if (data == null) {
                PlayBarkSoundData.m_logger.debug((Object)"Impossible de trouver de BarkData ad\u00e9quat");
                return;
            }
            final long soundId = data.getSoundId();
            if (!ae.getSoundValidator().canPlaySound(soundId)) {
                return;
            }
            if (!SoundDataHelper.tryRegisterSound(soundId)) {
                return;
            }
            SoundFunctionsLibrary.getInstance().playSound(soundId, data.getGain() * this.m_gain / 100.0f, 1, -1L, -1L, ae.getCurrentFightId(), (ObservedSource)ae, data.getRollOff());
        }
        catch (Exception e) {
            PlayBarkSoundData.m_logger.debug((Object)("soundExtension or soundPath not initialized " + e));
        }
    }
    
    @Override
    public int getType() {
        return 1;
    }
    
    public void setBarkId(final int barkId) {
        this.m_barkId = barkId;
    }
    
    public void setGain(final byte gain) {
        this.m_gain = gain;
    }
    
    public void setBreedId(final int breedId) {
        this.m_breedId = breedId;
    }
    
    @Override
    public void getSoundIds(final TLongArrayList sounds) {
        sounds.add(this.m_barkId);
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayBarkSoundData.class);
    }
}
