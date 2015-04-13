package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AvatarBreedColorsBinaryData implements BinaryData
{
    protected int m_id;
    protected Data[] m_values;
    
    public int getId() {
        return this.m_id;
    }
    
    public Data[] getValues() {
        return this.m_values;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_values = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        final int valueCount = buffer.getInt();
        this.m_values = new Data[valueCount];
        for (int iValue = 0; iValue < valueCount; ++iValue) {
            (this.m_values[iValue] = new Data()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.AVATAR_BREED_COLORS.getId();
    }
    
    public static class Color
    {
        protected float m_red;
        protected float m_green;
        protected float m_blue;
        protected float m_alpha;
        
        public float getRed() {
            return this.m_red;
        }
        
        public float getGreen() {
            return this.m_green;
        }
        
        public float getBlue() {
            return this.m_blue;
        }
        
        public float getAlpha() {
            return this.m_alpha;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_red = buffer.getFloat();
            this.m_green = buffer.getFloat();
            this.m_blue = buffer.getFloat();
            this.m_alpha = buffer.getFloat();
        }
    }
    
    public static class Data
    {
        protected byte m_sex;
        protected byte m_defaultSkinIndex;
        protected byte m_defaultSkinFactor;
        protected byte m_defaultHairIndex;
        protected byte m_defaultHairFactor;
        protected byte m_defaultPupilIndex;
        protected Color[] m_skinColors;
        protected Color[] m_hairColors;
        protected Color[] m_pupilColors;
        
        public byte getSex() {
            return this.m_sex;
        }
        
        public byte getDefaultSkinIndex() {
            return this.m_defaultSkinIndex;
        }
        
        public byte getDefaultSkinFactor() {
            return this.m_defaultSkinFactor;
        }
        
        public byte getDefaultHairIndex() {
            return this.m_defaultHairIndex;
        }
        
        public byte getDefaultHairFactor() {
            return this.m_defaultHairFactor;
        }
        
        public byte getDefaultPupilIndex() {
            return this.m_defaultPupilIndex;
        }
        
        public Color[] getSkinColors() {
            return this.m_skinColors;
        }
        
        public Color[] getHairColors() {
            return this.m_hairColors;
        }
        
        public Color[] getPupilColors() {
            return this.m_pupilColors;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_sex = buffer.get();
            this.m_defaultSkinIndex = buffer.get();
            this.m_defaultSkinFactor = buffer.get();
            this.m_defaultHairIndex = buffer.get();
            this.m_defaultHairFactor = buffer.get();
            this.m_defaultPupilIndex = buffer.get();
            final int skinColorCount = buffer.getInt();
            this.m_skinColors = new Color[skinColorCount];
            for (int iSkinColor = 0; iSkinColor < skinColorCount; ++iSkinColor) {
                (this.m_skinColors[iSkinColor] = new Color()).read(buffer);
            }
            final int hairColorCount = buffer.getInt();
            this.m_hairColors = new Color[hairColorCount];
            for (int iHairColor = 0; iHairColor < hairColorCount; ++iHairColor) {
                (this.m_hairColors[iHairColor] = new Color()).read(buffer);
            }
            final int pupilColorCount = buffer.getInt();
            this.m_pupilColors = new Color[pupilColorCount];
            for (int iPupilColor = 0; iPupilColor < pupilColorCount; ++iPupilColor) {
                (this.m_pupilColors[iPupilColor] = new Color()).read(buffer);
            }
        }
    }
}
