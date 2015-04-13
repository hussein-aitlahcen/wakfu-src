package com.ankamagames.wakfu.client.core.game.characterInfo.name;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.framework.kernel.core.translator.*;

public class WakfuNameGenerator implements RandomNameGenerator
{
    private static final WakfuNameGenerator m_instance;
    private RandomNameGenerator m_generator;
    
    public static WakfuNameGenerator getInstance() {
        return WakfuNameGenerator.m_instance;
    }
    
    private void init() {
        if (this.m_generator != null) {
            return;
        }
        final Language language = WakfuTranslator.getInstance().getLanguage();
        if (language.isUsesStandardCharacters()) {
            this.m_generator = new EuroRandomNameGenerator();
        }
        else {
            try {
                final String filePath = String.format(WakfuConfiguration.getInstance().getString("randomNamePath"), language.getLocale());
                this.m_generator = new FileRandomNameGenerator(filePath);
            }
            catch (IllegalArgumentException ignored) {}
            catch (PropertyException ex) {}
        }
    }
    
    public boolean canGenerateName() {
        this.init();
        return this.m_generator != null;
    }
    
    @Override
    public String getRandomName() {
        this.init();
        return this.m_generator.getRandomName();
    }
    
    @Override
    public void clean() {
        if (this.m_generator != null) {
            this.m_generator.clean();
        }
        this.m_generator = null;
    }
    
    static {
        m_instance = new WakfuNameGenerator();
    }
}
