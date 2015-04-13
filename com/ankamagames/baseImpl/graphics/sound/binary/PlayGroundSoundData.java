package com.ankamagames.baseImpl.graphics.sound.binary;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.framework.sound.ground.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;
import gnu.trove.*;

public class PlayGroundSoundData implements AnimatedElementRunScriptData
{
    private static final Logger m_logger;
    private byte m_walkType;
    private byte m_gain;
    
    public PlayGroundSoundData() {
        super();
        this.m_gain = 100;
    }
    
    public PlayGroundSoundData(final ExtendedDataInputStream is) {
        super();
        this.m_gain = 100;
        this.load(is);
    }
    
    public PlayGroundSoundData(final byte walkType, final byte gain) {
        super();
        this.m_gain = 100;
        this.m_walkType = walkType;
        this.m_gain = gain;
    }
    
    @Override
    public void play(final AnimatedObject ae) {
        if (!SoundDataHelper.canPlaySound(ae)) {
            return;
        }
        if (!ae.getSoundValidator().canPlayGroundSound()) {
            return;
        }
        final AnimatedElement elem = (AnimatedElement)ae;
        final byte groundType = getGroundType(elem);
        try {
            final SoundFunctionsLibrary.GroundSoundData data = SoundFunctionsLibrary.getInstance().getGroundSoundData(groundType, this.m_walkType);
            if (data == null) {
                PlayGroundSoundData.m_logger.debug((Object)"Impossible de trouver de GroundSoundData ad\u00e9quat");
                return;
            }
            if (!elem.getSoundValidator().canPlaySound(data.getSoundId())) {
                return;
            }
            if (!SoundDataHelper.tryRegisterSound(data.getSoundId())) {
                return;
            }
            SoundFunctionsLibrary.getInstance().playSound(data.getSoundId(), data.getGain() * this.m_gain / 100.0f, 1, -1L, -1L, elem.getCurrentFightId(), elem, data.getRollOff());
        }
        catch (Exception e) {
            PlayGroundSoundData.m_logger.debug((Object)("soundExtension or soundPath not initialized " + e));
        }
    }
    
    private static byte getGroundType(final AnimatedElement elem) {
        final short z = TopologyMapManager.getNearestWalkableZ(elem.getWorldCellX(), elem.getWorldCellY(), elem.getWorldCellAltitude());
        final DisplayedScreenElement element = DisplayedScreenWorld.getInstance().getElementAtTop(elem.getWorldCellX(), elem.getWorldCellY(), z, ElementFilter.NOT_EMPTY);
        if (element == null) {
            return GroundSoundType.DEFAULT.getType();
        }
        final ElementProperties properties = element.getElement().getCommonProperties();
        if (properties != null) {
            return properties.getGroundSoundType();
        }
        return GroundSoundType.DEFAULT.getType();
    }
    
    @Override
    public int getType() {
        return 2;
    }
    
    @Override
    public void load(final ExtendedDataInputStream is) {
        this.m_walkType = is.readByte();
        this.m_gain = is.readByte();
    }
    
    @Override
    public void save(final OutputBitStream os) throws IOException {
        os.writeByte(this.m_walkType);
        os.writeByte(this.m_gain);
    }
    
    public void setWalkType(final byte walkType) {
        this.m_walkType = walkType;
    }
    
    public void setGain(final byte gain) {
        this.m_gain = gain;
    }
    
    @Override
    public void getSoundIds(final TLongArrayList sounds) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayGroundSoundData.class);
    }
}
