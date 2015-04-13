package com.ankamagames.wakfu.client.alea.graphics;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.guild.*;
import com.ankamagames.wakfu.common.datas.group.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;

public class AnmGuildBlazonApplicator
{
    private static final Logger m_logger;
    private final AnimatedElement m_element;
    private final String m_logoLinkage;
    private final String m_logoBgLinkage;
    private Anm m_guildBlason;
    private Anm m_guildBlasonBg;
    
    private AnmGuildBlazonApplicator(final AnimatedElement element, final String logoLinkage, final String logoBgLinkage) {
        super();
        this.m_element = element;
        this.m_logoLinkage = logoLinkage;
        this.m_logoBgLinkage = logoBgLinkage;
    }
    
    public void removeGuildAppearance() {
        this.m_element.setCustomColor(3, null);
        this.m_element.setCustomColor(4, null);
        if (this.m_guildBlason != null) {
            this.m_element.getAnmInstance().dettachAnmTo(this.m_guildBlason);
            this.m_guildBlason = null;
        }
        if (this.m_guildBlasonBg != null) {
            this.m_element.getAnmInstance().dettachAnmTo(this.m_guildBlasonBg);
            this.m_guildBlasonBg = null;
        }
    }
    
    public void setGuildAppearance(final Color bgColor, final Color fgColor, final byte symbolId, final byte shapeId) {
        this.m_element.setCustomColor(3, bgColor.getFloatRGBA());
        this.m_element.setCustomColor(4, fgColor.getFloatRGBA());
        boolean changed = false;
        final Anm guilBlason = this.attachLogo(this.m_logoLinkage, symbolId, this.m_guildBlason);
        if (guilBlason != null) {
            changed = true;
            this.m_guildBlason = guilBlason;
        }
        final Anm guilBlasonBg = this.attachLogo(this.m_logoBgLinkage, shapeId, this.m_guildBlasonBg);
        if (guilBlasonBg != null) {
            changed = true;
            this.m_guildBlasonBg = guilBlasonBg;
        }
        if (changed) {
            this.m_element.forceUpdateEquipment();
        }
    }
    
    private Anm attachLogo(final String linkage, final int logoId, final Anm currentLogo) {
        if (linkage == null) {
            return null;
        }
        final String logoFileName = linkage + logoId;
        try {
            final String equipmentFileName = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
            final String format = String.format(equipmentFileName, logoFileName);
            if (currentLogo != null && currentLogo.getFileName().equals(logoFileName)) {
                return null;
            }
            final Anm blason = AnimatedElement.loadEquipment(format);
            if (blason == null) {
                AnmGuildBlazonApplicator.m_logger.error((Object)("Pas de blason trouv\u00e9 ID=" + logoFileName));
                return null;
            }
            this.m_element.getAnmInstance().attachAnmTo(blason);
            this.m_element.forceUpdateEquipment();
            return blason;
        }
        catch (PropertyException e) {
            AnmGuildBlazonApplicator.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
    }
    
    public static AnmGuildBlazonApplicator create(final AnimatedElement guildApplyable, final ClientGuildInformationHandler guildHandler, final String logoLinkage, final String logoBgLinkage) {
        return create(guildApplyable, guildHandler.getBlazon(), logoLinkage, logoBgLinkage);
    }
    
    public static AnmGuildBlazonApplicator create(final AnimatedElement guildApplyable, final long guildBlazon, final String logoLinkage, final String logoBgLinkage) {
        if (guildBlazon == 0L) {
            return null;
        }
        try {
            final GuildBlazon blazon = new GuildBlazon(guildBlazon);
            final Color bgColor = GuildBannerColor.getInstance().getColor(blazon.getShapeColor());
            final Color fgColor = GuildBannerColor.getInstance().getColor(blazon.getSymbolColor());
            final AnmGuildBlazonApplicator applicator = new AnmGuildBlazonApplicator(guildApplyable, logoLinkage, logoBgLinkage);
            applicator.setGuildAppearance(bgColor, fgColor, blazon.getSymbolId(), blazon.getShapeId());
            return applicator;
        }
        catch (Exception e) {
            AnmGuildBlazonApplicator.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AnmGuildBlazonApplicator.class);
    }
}
