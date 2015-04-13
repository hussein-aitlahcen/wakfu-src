package com.ankamagames.wakfu.client.core.game.group.guild;

import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.wakfu.common.datas.group.*;

public class GuildBannerData
{
    private long m_textureName;
    private byte m_firstPart;
    private byte m_secondPart;
    private Color m_background;
    private Color m_foreground;
    
    public GuildBannerData(final byte firstPart, final byte secondPart, final Color foreground, final Color background) {
        super();
        this.m_firstPart = 1;
        this.m_secondPart = 1;
        this.m_firstPart = firstPart;
        this.m_secondPart = secondPart;
        this.m_foreground = foreground;
        this.m_background = background;
        this.m_textureName = this.generateTextureName();
    }
    
    public byte getFirstPart() {
        return this.m_firstPart;
    }
    
    public void setFirstPart(final byte firstPart) {
        this.m_firstPart = firstPart;
        this.m_textureName = this.generateTextureName();
    }
    
    public byte getSecondPart() {
        return this.m_secondPart;
    }
    
    public void setSecondPart(final byte secondPart) {
        this.m_secondPart = secondPart;
        this.m_textureName = this.generateTextureName();
    }
    
    public Color getForeground() {
        return this.m_foreground;
    }
    
    public void setForeground(final Color foreground1) {
        if (this.m_foreground == foreground1) {
            return;
        }
        this.m_foreground = foreground1;
        this.m_textureName = this.generateTextureName();
    }
    
    public Color getBackground() {
        return this.m_background;
    }
    
    public void setBackground(final Color background1) {
        if (this.m_background == background1) {
            return;
        }
        this.m_background = background1;
        this.m_textureName = this.generateTextureName();
    }
    
    public void cleanUp() {
    }
    
    public String getTextureRealName() {
        final StringBuilder sb = new StringBuilder("generatedGuildBanner-");
        sb.append(this.m_firstPart).append('-').append(this.m_secondPart);
        sb.append('-').append(this.m_foreground.get()).append('-').append(this.m_background.get());
        return sb.toString();
    }
    
    private long generateTextureName() {
        return Engine.getTextureName(this.getTextureRealName());
    }
    
    public long getTextureName() {
        return this.m_textureName;
    }
    
    public GuildBlazon getBlazon() {
        final short firstColorIndex = GuildBannerColor.getInstance().indexOf(this.m_background);
        final short secondColorIndex = GuildBannerColor.getInstance().indexOf(this.m_foreground);
        return new GuildBlazon(this.m_firstPart, firstColorIndex, this.m_secondPart, secondColorIndex);
    }
}
