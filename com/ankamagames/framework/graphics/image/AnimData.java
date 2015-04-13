package com.ankamagames.framework.graphics.image;

import org.apache.tools.ant.*;
import java.io.*;
import com.sun.opengl.util.texture.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.fileFormat.io.*;
import gnu.trove.*;
import java.nio.*;

public abstract class AnimData
{
    protected final int m_totalTime;
    protected final short[] m_animationTimes;
    
    public AnimData(final int totalTime, final short[] animationTimes) {
        super();
        if (totalTime == 0) {
            throw new BuildException("animation sans frame");
        }
        this.m_totalTime = totalTime;
        this.m_animationTimes = animationTimes;
    }
    
    public abstract void write(final OutputBitStream p0) throws IOException;
    
    public abstract TextureCoords getTextureCoordinates(final short p0);
    
    public int getTotalTime() {
        return this.m_totalTime;
    }
    
    @Override
    public String toString() {
        return "dur\u00e9e: " + this.m_totalTime + "ms " + " images: " + this.m_animationTimes.length;
    }
    
    public static TextureCoords[] computeTextureCoords(final short[] textureCoords, final short imgWidth, final short imgHeight, final int imgWidthTotal, final int imgHeightTotal, final boolean flip) {
        assert imgWidthTotal > 0;
        assert imgHeightTotal > 0;
        final float widthTotal = MathHelper.nearestGreatestPowOfTwo(imgWidthTotal);
        final float heightTotal = MathHelper.nearestGreatestPowOfTwo(imgHeightTotal) - 0.5f;
        final float right = imgWidth / widthTotal;
        final float bottom = imgHeight / heightTotal;
        final TextureCoords[] result = new TextureCoords[textureCoords.length / 2];
        for (int i = 0; i < result.length; ++i) {
            final float offsetX = (textureCoords[i * 2] + 0.5f) / widthTotal;
            final float offsetY = (textureCoords[i * 2 + 1] + 0.5f) / heightTotal;
            if (flip) {
                result[i] = new TextureCoords(right + offsetX, bottom + offsetY, offsetX, offsetY);
            }
            else {
                result[i] = new TextureCoords(offsetX, bottom + offsetY, right + offsetX, offsetY);
            }
        }
        return result;
    }
    
    private static AnimData fromBuffer(final boolean typeExport, final ExtendedDataInputStream buffer, final boolean flip) {
        final int animCount = buffer.readByte() & 0xFF;
        if (animCount == 0) {
            return null;
        }
        final int totalTime = buffer.readInt();
        final short imgWidth = buffer.readShort();
        final short imgHeight = buffer.readShort();
        final short imgWidthTotal = buffer.readShort();
        final short imgHeightTotal = buffer.readShort();
        final short[] animationTimes = buffer.readShorts(animCount);
        final short[] textureCoords = buffer.readShorts(animCount * 2);
        if (typeExport) {
            return new Export(totalTime, imgWidth, imgHeight, imgWidthTotal, imgHeightTotal, animationTimes, textureCoords);
        }
        final TextureCoords[] animationTextureCoords = computeTextureCoords(textureCoords, imgWidth, imgHeight, imgWidthTotal, imgHeightTotal, flip);
        return new Use(totalTime, animationTimes, animationTextureCoords);
    }
    
    public static class Export extends AnimData
    {
        private final short m_imgWidth;
        private final short m_imgHeight;
        private final short m_imgWidthTotal;
        private final short m_imgHeightTotal;
        private final short[] m_animationTextureCoord;
        
        public static Export fromTotal(final short imgWidthTotal, final short imgHeightTotal, final short[] animationTimes, final short[] animationTextureCoord) {
            if (animationTimes.length <= 1 || animationTextureCoord.length <= 2) {
                throw new BuildException("pas une animation");
            }
            if (animationTimes.length * 2 != animationTextureCoord.length) {
                throw new BuildException("valeurs incorrectes");
            }
            int totalTime = 0;
            for (int i = 0; i < animationTimes.length; ++i) {
                totalTime += animationTimes[i];
            }
            final TShortHashSet distinctWidth = new TShortHashSet();
            final TShortHashSet distinctHeight = new TShortHashSet();
            for (int j = 0; j < animationTextureCoord.length; j += 2) {
                distinctWidth.add(animationTextureCoord[j]);
                distinctHeight.add(animationTextureCoord[j + 1]);
            }
            final short imgWidth = (short)(imgWidthTotal / distinctWidth.size());
            final short imgHeight = (short)(imgHeightTotal / distinctHeight.size());
            return new Export(totalTime, imgWidth, imgHeight, imgWidthTotal, imgHeightTotal, animationTimes, animationTextureCoord);
        }
        
        public static Export fromSize(final short imgWidth, final short imgHeight, final short[] animationTimes, final short[] animationTextureCoord) {
            if (animationTimes.length <= 1 || animationTextureCoord.length <= 2) {
                throw new BuildException("pas une animation");
            }
            if (animationTimes.length * 2 != animationTextureCoord.length) {
                throw new BuildException("valeurs incorrectes");
            }
            int totalTime = 0;
            for (int i = 0; i < animationTimes.length; ++i) {
                totalTime += animationTimes[i];
            }
            final TShortHashSet distinctWidth = new TShortHashSet();
            final TShortHashSet distinctHeight = new TShortHashSet();
            for (int j = 0; j < animationTextureCoord.length; j += 2) {
                distinctWidth.add(animationTextureCoord[j]);
                distinctHeight.add(animationTextureCoord[j + 1]);
            }
            final short imgWidthTotal = (short)(imgWidth * distinctWidth.size());
            final short imgHeightTotal = (short)(imgHeight * distinctHeight.size());
            return new Export(totalTime, imgWidth, imgHeight, imgWidthTotal, imgHeightTotal, animationTimes, animationTextureCoord);
        }
        
        public static Export from(final Export src, final short imgWidthTotal, final short imgHeightTotal) {
            return new Export(src.m_totalTime, src.m_imgWidth, src.m_imgHeight, imgWidthTotal, imgHeightTotal, src.m_animationTimes, src.m_animationTextureCoord);
        }
        
        private Export(final int totalTime, final short imgWidth, final short imgHeight, final short imgWidthTotal, final short imgHeightTotal, final short[] animationTimes, final short[] animationTextureCoord) {
            super(totalTime, animationTimes);
            this.m_imgWidth = imgWidth;
            this.m_imgHeight = imgHeight;
            this.m_imgWidthTotal = imgWidthTotal;
            this.m_imgHeightTotal = imgHeightTotal;
            this.m_animationTextureCoord = animationTextureCoord;
        }
        
        public short[] getAnimationTextureCoord() {
            return this.m_animationTextureCoord;
        }
        
        @Override
        public int getTotalTime() {
            return this.m_totalTime;
        }
        
        public short[] getAnimationTimes() {
            return this.m_animationTimes;
        }
        
        @Override
        public void write(final OutputBitStream ostream) throws IOException {
            if (this.m_animationTimes == null || this.m_animationTimes.length <= 1) {
                ostream.writeByte((byte)0);
            }
            else {
                final byte frameCount = (byte)this.m_animationTimes.length;
                ostream.writeByte(frameCount);
                ostream.writeInt(this.m_totalTime);
                ostream.writeShort(this.m_imgWidth);
                ostream.writeShort(this.m_imgHeight);
                ostream.writeShort(this.m_imgWidthTotal);
                ostream.writeShort(this.m_imgHeightTotal);
                for (int i = 0; i < frameCount; ++i) {
                    ostream.writeShort(this.m_animationTimes[i]);
                }
                assert this.m_animationTextureCoord.length == 2 * frameCount;
                for (int i = 0; i < this.m_animationTextureCoord.length; ++i) {
                    ostream.writeShort(this.m_animationTextureCoord[i]);
                }
            }
        }
        
        public static Export fromBuffer(final ExtendedDataInputStream buffer) {
            return (Export)fromBuffer(true, buffer, true);
        }
        
        @Override
        public TextureCoords getTextureCoordinates(final short animationTime) {
            throw new UnsupportedOperationException("utiliser la classe AnimData.Use");
        }
        
        public int getImgWidth() {
            return this.m_imgWidth;
        }
        
        public int getImgHeight() {
            return this.m_imgHeight;
        }
        
        public short getImgWidthTotal() {
            return this.m_imgWidthTotal;
        }
        
        public short getImgHeightTotal() {
            return this.m_imgHeightTotal;
        }
    }
    
    public static class Use extends AnimData
    {
        protected final TextureCoords[] m_animationTextureCoords;
        
        public Use(final int totalTime, final short[] animationTimes, final TextureCoords[] animationTextureCoords) {
            super(totalTime, animationTimes);
            this.m_animationTextureCoords = animationTextureCoords;
        }
        
        public static Use from(final Export animData) {
            final TextureCoords[] texCoords = AnimData.computeTextureCoords(animData.m_animationTextureCoord, animData.m_imgWidth, animData.m_imgHeight, animData.m_imgWidthTotal, animData.m_imgHeightTotal, false);
            return new Use(animData.m_totalTime, animData.m_animationTimes, texCoords);
        }
        
        public static Use fromBuffer(final ByteBuffer buffer, final boolean flip) {
            return fromBuffer(ExtendedDataInputStream.wrap(buffer), flip);
        }
        
        public static Use fromBuffer(final ExtendedDataInputStream buffer, final boolean flip) {
            return (Use)fromBuffer(false, buffer, flip);
        }
        
        @Override
        public void write(final OutputBitStream ostream) throws IOException {
            throw new UnsupportedOperationException("utiliser la classe AnimData.Export");
        }
        
        @Override
        public TextureCoords getTextureCoordinates(final short animationTime) {
            int time = (animationTime & 0xFFFF) % this.m_totalTime;
            for (int i = 0; i < this.m_animationTimes.length; ++i) {
                time -= this.m_animationTimes[i];
                if (time < 0) {
                    return this.m_animationTextureCoords[i];
                }
            }
            return this.m_animationTextureCoords[0];
        }
    }
}
