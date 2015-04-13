package com.ankamagames.baseImpl.graphics.alea.environment;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;
import org.apache.tools.ant.*;

public class ClientEnvironmentMap extends AbstractClientEnvironmentMap
{
    private static final Logger m_logger;
    private static final int NUM_PARTICLES_MAX = 255;
    private static final int NUM_SOUNDS_MAX = 255;
    private static final int NUM_AMBIANCE_MAX = 4;
    private static final byte AMBIANCE_MASK = 3;
    public static final int NUM_INTERACTIVE_ELEMENTS_MAX = 255;
    public static final int AMBIANCE_SIZE;
    public static final int VERSION = 0;
    public static final int NO_AMBIANCE = -1;
    private InteractiveElementDef[] m_interactiveElements;
    private DynamicElementDef[] m_dynamicElements;
    private static final DynamicElementDef[] EMPTY_DYNAMIC_DEFINITIONS;
    private ParticleDef[] m_particleData;
    private SoundDef[] m_soundData;
    int[] m_ambiancesId;
    byte[] m_ambiances;
    boolean m_loaded;
    
    public ClientEnvironmentMap() {
        super();
        this.m_interactiveElements = null;
        this.m_dynamicElements = null;
        this.m_particleData = null;
        this.m_soundData = null;
    }
    
    public ClientEnvironmentMap(final short x, final short y) {
        super(x, y);
        this.m_interactiveElements = null;
        this.m_dynamicElements = null;
        this.m_particleData = null;
        this.m_soundData = null;
    }
    
    public byte getVersion() {
        return 0;
    }
    
    public ParticleDef[] getParticleData() {
        return this.m_particleData;
    }
    
    public SoundDef[] getSoundData() {
        return this.m_soundData;
    }
    
    public int[] getAmbianceIds() {
        return this.m_ambiancesId;
    }
    
    public int getAmbianceZone(int cellX, int cellY) {
        if (this.m_ambiancesId == null) {
            return -1;
        }
        if (this.m_ambiances == null) {
            return this.m_ambiancesId[0];
        }
        cellX -= this.m_x * 18;
        cellY -= this.m_y * 18;
        assert cellX >= 0 && cellX < 18;
        assert cellY >= 0 && cellY < 18;
        final int cellIndex = cellX + cellY * 18;
        final int index = this.m_ambiances[cellIndex / 4] >>> cellIndex % 4 * 2;
        return this.m_ambiancesId[index & 0x3];
    }
    
    public InteractiveElementDef[] getInteractiveElements() {
        return this.m_interactiveElements;
    }
    
    public DynamicElementDef[] getDynamicElements() {
        return this.m_dynamicElements;
    }
    
    public void setSoundData(final SoundDef[] soundData) {
        this.m_soundData = soundData;
    }
    
    public void setParticleData(final ParticleDef[] particleData) {
        this.m_particleData = particleData;
    }
    
    public void setAmbiancesId(final int[] ambiancesId) {
        this.m_ambiancesId = ambiancesId;
        assert this.m_ambiancesId.length <= 4 : "Trop d'ambiance diff\u00e9rentes";
        if (this.m_ambiancesId.length >= 1) {
            this.m_ambiances = new byte[ClientEnvironmentMap.AMBIANCE_SIZE];
        }
    }
    
    public void setAmbianceZone(final int cellX, final int cellY, final int ambianceIndex) {
        assert cellX >= 0 && cellX < 18;
        assert cellY >= 0 && cellY < 18;
        assert this.m_ambiances != null : "Il faut d'abord appeler setAmbiancesId";
        assert ambianceIndex < this.m_ambiancesId.length;
        final int cellIndex = cellX + cellY * 18;
        final byte[] ambiances = this.m_ambiances;
        final int n = cellIndex / 4;
        ambiances[n] |= (byte)(ambianceIndex << cellIndex % 4 * 2);
    }
    
    public void setInteractiveElement(final InteractiveElementDef[] array) {
        this.m_interactiveElements = array;
    }
    
    public void setDynamicElement(final DynamicElementDef[] array) {
        this.m_dynamicElements = array;
    }
    
    @Override
    public void clear() {
        this.m_particleData = null;
        this.m_soundData = null;
        this.m_ambiances = null;
        this.m_ambiancesId = null;
    }
    
    @Override
    public void load(@NotNull final ExtendedDataInputStream istream) throws IOException {
        super.load(istream);
        this.loadParticleData(istream);
        this.loadSoundData(istream);
        this.loadAmbianceData(istream);
        this.loadInteractiveElements(istream);
        this.loadDynamicElements(istream);
    }
    
    @Override
    public void save(@NotNull final OutputBitStream ostream) throws IOException {
        super.save(ostream);
        this.saveParticleData(ostream);
        this.saveSoundData(ostream);
        this.saveAmbianceData(ostream);
        this.saveInteractiveElements(ostream);
        this.saveDynamicElements(ostream);
    }
    
    private void loadParticleData(final ExtendedDataInputStream istream) throws IOException {
        final int numParticleSystem = istream.readByte() & 0xFF;
        if (numParticleSystem == 0) {
            return;
        }
        this.m_particleData = new ParticleDef[numParticleSystem];
        for (int i = 0; i < numParticleSystem; ++i) {
            final ParticleDef particleDef = new ParticleDef();
            particleDef.load(istream);
            this.m_particleData[i] = particleDef;
        }
    }
    
    private void saveParticleData(final OutputBitStream ostream) throws IOException {
        if (this.m_particleData == null) {
            ostream.writeByte((byte)0);
            return;
        }
        final int numParticleSystem = this.m_particleData.length;
        if (numParticleSystem >= 255) {
            throw new BuildException("trop de particules sur la map (" + this.m_x + " " + this.m_y + ")");
        }
        ostream.writeByte((byte)(numParticleSystem & 0xFF));
        for (int i = 0; i < numParticleSystem; ++i) {
            this.m_particleData[i].save(ostream);
        }
    }
    
    private void loadSoundData(final ExtendedDataInputStream istream) throws IOException {
        final int numSounds = istream.readByte() & 0xFF;
        if (numSounds == 0) {
            return;
        }
        this.m_soundData = new SoundDef[numSounds];
        for (int i = 0; i < numSounds; ++i) {
            final SoundDef sound = new SoundDef();
            sound.load(istream);
            this.m_soundData[i] = sound;
        }
    }
    
    private void saveSoundData(final OutputBitStream ostream) throws IOException {
        if (this.m_soundData == null) {
            ostream.writeByte((byte)0);
            return;
        }
        final int numSound = this.m_soundData.length;
        if (numSound >= 255) {
            throw new BuildException("trop de sons sur la map (" + this.m_x + " " + this.m_y + ")");
        }
        ostream.writeByte((byte)(numSound & 0xFF));
        for (int i = 0; i < numSound; ++i) {
            this.m_soundData[i].save(ostream);
        }
    }
    
    private void loadAmbianceData(final ExtendedDataInputStream istream) throws IOException {
        final int numAmbianceId = istream.readByte() & 0xFF;
        if (numAmbianceId == 0) {
            this.m_ambiances = null;
            this.m_ambiancesId = null;
            return;
        }
        this.m_ambiancesId = istream.readInts(numAmbianceId);
        final int size = istream.readByte() & 0xFF;
        if (size == 0) {
            assert this.m_ambiancesId.length == 1;
            this.m_ambiances = null;
        }
        else {
            assert size == ClientEnvironmentMap.AMBIANCE_SIZE;
            this.m_ambiances = istream.readBytes(size);
        }
    }
    
    private void saveAmbianceData(final OutputBitStream ostream) throws IOException {
        if (this.m_ambiancesId == null) {
            assert this.m_ambiances == null;
            ostream.writeByte((byte)0);
        }
        else {
            final int numAmbianceIds = this.m_ambiancesId.length;
            assert this.m_ambiancesId.length < 4;
            ostream.writeByte((byte)(numAmbianceIds & 0xFF));
            for (int i = 0; i < numAmbianceIds; ++i) {
                ostream.writeInt(this.m_ambiancesId[i]);
            }
            if (this.m_ambiances == null) {
                assert this.m_ambiancesId.length == 1;
                ostream.writeByte((byte)0);
            }
            else {
                assert this.m_ambiances.length == ClientEnvironmentMap.AMBIANCE_SIZE;
                ostream.writeByte((byte)(this.m_ambiances.length & 0xFF));
                ostream.writeBytes(this.m_ambiances);
            }
        }
    }
    
    protected void loadInteractiveElements(final ExtendedDataInputStream istream) throws IOException {
        final int numInteractiveElt = istream.readByte() & 0xFF;
        if (numInteractiveElt == 0) {
            return;
        }
        this.m_interactiveElements = new InteractiveElementDef[numInteractiveElt];
        for (int i = 0; i < numInteractiveElt; ++i) {
            final InteractiveElementDef interactiveElements = new InteractiveElementDef();
            interactiveElements.load(istream);
            this.m_interactiveElements[i] = interactiveElements;
        }
    }
    
    private void saveInteractiveElements(final OutputBitStream ostream) throws IOException {
        if (this.m_interactiveElements == null) {
            ostream.writeByte((byte)0);
            return;
        }
        final int numInteractiveElementsSystem = this.m_interactiveElements.length;
        if (numInteractiveElementsSystem >= 255) {
            throw new BuildException("trop de interactivs sur la map (" + this.m_x + " " + this.m_y + ")");
        }
        ostream.writeByte((byte)(numInteractiveElementsSystem & 0xFF));
        for (int i = 0; i < numInteractiveElementsSystem; ++i) {
            this.m_interactiveElements[i].save(ostream);
        }
    }
    
    protected void loadDynamicElements(final ExtendedDataInputStream istream) throws IOException {
        final int numDynamicElt = istream.readByte() & 0xFF;
        if (numDynamicElt == 0) {
            this.m_dynamicElements = ClientEnvironmentMap.EMPTY_DYNAMIC_DEFINITIONS;
            return;
        }
        this.m_dynamicElements = new DynamicElementDef[numDynamicElt];
        for (int i = 0; i < numDynamicElt; ++i) {
            final DynamicElementDef dynamicElements = new DynamicElementDef();
            dynamicElements.load(istream);
            this.m_dynamicElements[i] = dynamicElements;
        }
    }
    
    private void saveDynamicElements(final OutputBitStream ostream) throws IOException {
        if (this.m_dynamicElements == null) {
            ostream.writeByte((byte)0);
            return;
        }
        final int numDynamicElementsSystem = this.m_dynamicElements.length;
        if (numDynamicElementsSystem >= 255) {
            throw new BuildException("trop de dynamic sur la map (" + this.m_x + " " + this.m_y + ")");
        }
        ostream.writeByte((byte)(numDynamicElementsSystem & 0xFF));
        for (int i = 0; i < numDynamicElementsSystem; ++i) {
            this.m_dynamicElements[i].save(ostream);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ClientEnvironmentMap.class);
        AMBIANCE_SIZE = (int)Math.ceil(81.0);
        EMPTY_DYNAMIC_DEFINITIONS = new DynamicElementDef[0];
    }
}
