package com.ankamagames.wakfu.client.core.game.group.guild;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.*;

public class GuildBannerGenerator
{
    private static final Logger m_logger;
    private static final GuildBannerGenerator m_instance;
    private final int FIRST_PART_MAX_ID = 11;
    private final int SECOND_PART_MAX_ID = 59;
    private final int FIRST_PART_COUNT = 12;
    private final int SECOND_PART_COUNT = 60;
    private static final Color.GrayColorKeying GRAY_COLOR_KEYING;
    
    public static GuildBannerGenerator getInstance() {
        return GuildBannerGenerator.m_instance;
    }
    
    private String getFirstPartPath(int id) {
        id = MathHelper.clamp(id, 0, 11);
        try {
            return String.format(WakfuConfiguration.getInstance().getString("guildBlazonBackgroundPartPath"), id);
        }
        catch (PropertyException e) {
            GuildBannerGenerator.m_logger.warn((Object)"Impossible de trouver la propri\u00e9t\u00e9 guildBlazonBackgroundPartPath");
            return null;
        }
    }
    
    private String getSecondPartPath(int id) {
        id = MathHelper.clamp(id, 0, 59);
        try {
            return String.format(WakfuConfiguration.getInstance().getString("guildBlazonForegroundPartPath"), id);
        }
        catch (PropertyException e) {
            GuildBannerGenerator.m_logger.warn((Object)"Impossible de trouver la propri\u00e9t\u00e9 guildBlazonForegroundPartPath");
            return null;
        }
    }
    
    public Texture getGuildBannerTexture(final GuildBannerData data) {
        final long textureName = data.getTextureName();
        Texture tex = TextureManager.getInstance().getTexture(textureName);
        if (tex != null) {
            return tex;
        }
        final String[] fileNames = { this.getFirstPartPath(data.getFirstPart()), this.getSecondPartPath(data.getSecondPart()) };
        final Image img = new Image();
        img.readFromFiles(fileNames);
        GuildBannerGenerator.GRAY_COLOR_KEYING.setDestColor(data.getBackground().getRedByte(), data.getBackground().getGreenByte(), data.getBackground().getBlueByte(), data.getBackground().getAlphaByte());
        img.replaceColorWith(0, GuildBannerGenerator.GRAY_COLOR_KEYING);
        GuildBannerGenerator.GRAY_COLOR_KEYING.setDestColor(data.getForeground().getRedByte(), data.getForeground().getGreenByte(), data.getForeground().getBlueByte(), data.getForeground().getAlphaByte());
        img.replaceColorWith(1, GuildBannerGenerator.GRAY_COLOR_KEYING);
        img.flatten();
        tex = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), textureName, img, true);
        img.removeReference();
        return tex;
    }
    
    public GuildBannerData getRandomBannerPart() {
        final int firstPart = MathHelper.random(0, 12);
        final int secondPart = MathHelper.random(0, 60);
        final int colorSize = GuildBannerColor.getInstance().getNumColors();
        final Color fc = GuildBannerColor.getInstance().getColor(MathHelper.random(colorSize));
        final Color bc = GuildBannerColor.getInstance().getColor(MathHelper.random(colorSize));
        return new GuildBannerData((byte)firstPart, (byte)secondPart, fc, bc);
    }
    
    public void applyNextBackground(final GuildBannerData data) {
        byte part = data.getFirstPart();
        part = (byte)((part + 1) % 12);
        data.setFirstPart(part);
    }
    
    public void applyPreviousBackground(final GuildBannerData data) {
        byte part = data.getFirstPart();
        part = (byte)((12 + part - 1) % 12);
        data.setFirstPart(part);
    }
    
    public void applyNextForeground(final GuildBannerData data) {
        byte part = data.getSecondPart();
        part = (byte)((part + 1) % 60);
        data.setSecondPart(part);
    }
    
    public void applyPreviousForeground(final GuildBannerData data) {
        byte part = data.getSecondPart();
        part = (byte)((60 + part - 1) % 60);
        data.setSecondPart(part);
    }
    
    static {
        m_logger = Logger.getLogger((Class)GuildBannerGenerator.class);
        m_instance = new GuildBannerGenerator();
        GRAY_COLOR_KEYING = new Color.GrayColorKeying(true);
    }
    
    public enum GuildBannerColorType
    {
        FOREGROUND {
            @Override
            public void applyToGuildBannerData(final Color color, final GuildBannerData data) {
                data.setForeground(color);
            }
        }, 
        BACKGROUND {
            @Override
            public void applyToGuildBannerData(final Color color, final GuildBannerData data) {
                data.setBackground(color);
            }
        };
        
        public abstract void applyToGuildBannerData(final Color p0, final GuildBannerData p1);
    }
}
