package com.ankamagames.wakfu.client.core.script.video;

import com.ankamagames.framework.kernel.core.translator.*;
import org.apache.commons.lang3.*;

public enum VideoSpu
{
    FR(0, new Language[] { Language.FR }), 
    EN(1, new Language[] { Language.EN }), 
    ES(2, new Language[] { Language.ES }), 
    PT(3, new Language[] { Language.PT }), 
    IT(4, new Language[] { Language.IT }), 
    DE(5, new Language[] { Language.DE }), 
    ZH(6, new Language[] { Language.ZH }), 
    CH(6, new Language[] { Language.CH }), 
    VI(7, new Language[] { Language.VIE }), 
    TH(8, new Language[] { Language.THA }), 
    TW(9, new Language[] { Language.TW }), 
    ZH_CH(9, new Language[] { Language.ZH_TW });
    
    public final int m_trackIdx;
    private final Language[] m_languages;
    
    private VideoSpu(final int trackIdx, final Language[] languages) {
        this.m_trackIdx = trackIdx;
        this.m_languages = languages.clone();
    }
    
    public static VideoSpu fromLanguage(final Language language) {
        final VideoSpu[] values = values();
        for (int i = 0; i < values.length; ++i) {
            final VideoSpu value = values[i];
            if (ArrayUtils.contains(value.m_languages, language)) {
                return value;
            }
        }
        return VideoSpu.EN;
    }
}
