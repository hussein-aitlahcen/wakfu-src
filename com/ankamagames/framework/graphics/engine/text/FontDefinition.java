package com.ankamagames.framework.graphics.engine.text;

import java.util.*;
import com.ankamagames.framework.kernel.core.translator.*;

public class FontDefinition
{
    private final String m_name;
    private final ArrayList<SubFontDefinition> m_definitions;
    
    public FontDefinition(final String name) {
        super();
        this.m_definitions = new ArrayList<SubFontDefinition>();
        this.m_name = name;
    }
    
    public void addDefinition(final String languages, final String path, final int deltaSize, final int deltaX, final int deltaY) {
        final SubFontDefinition subFontDefinition = new SubFontDefinition();
        subFontDefinition.setLanguages(languages);
        subFontDefinition.setPath(path);
        subFontDefinition.setFontSizeModificator(deltaSize);
        subFontDefinition.setDeltaX(deltaX);
        subFontDefinition.setDeltaY(deltaY);
        this.m_definitions.add(subFontDefinition);
    }
    
    public String getPath() {
        final SubFontDefinition def = this.getSubFontDefinition();
        return (def == null) ? null : def.getPath();
    }
    
    private SubFontDefinition getSubFontDefinition() {
        final Language language = Translator.getInstance().getLanguage();
        for (int i = 0, size = this.m_definitions.size(); i < size; ++i) {
            final SubFontDefinition definition = this.m_definitions.get(i);
            if (definition.contains(language)) {
                return definition;
            }
        }
        return null;
    }
    
    public int getFontSizeModificator() {
        final SubFontDefinition def = this.getSubFontDefinition();
        return (def == null) ? 0 : def.getFontSizeModificator();
    }
    
    public int getDeltaX() {
        final SubFontDefinition def = this.getSubFontDefinition();
        return (def == null) ? 0 : def.getDeltaX();
    }
    
    public int getDeltaY() {
        final SubFontDefinition def = this.getSubFontDefinition();
        return (def == null) ? 0 : def.getDeltaY();
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isEmpty() {
        return this.m_definitions.isEmpty();
    }
}
