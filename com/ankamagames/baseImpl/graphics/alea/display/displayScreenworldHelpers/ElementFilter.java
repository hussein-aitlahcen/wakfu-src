package com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers;

import com.ankamagames.baseImpl.graphics.alea.display.*;

public interface ElementFilter
{
    public static final ElementFilter ALL = new ElementFilter() {
        @Override
        public boolean isValid(DisplayedScreenElement element) {
            return true;
        }
    };
    public static final ElementFilter VISIBLE_ONLY = new ElementFilter() {
        @Override
        public boolean isValid(DisplayedScreenElement element) {
            return element.isVisible();
        }
    };
    public static final ElementFilter NOT_EMPTY = new ElementFilter() {
        @Override
        public boolean isValid(DisplayedScreenElement element) {
            return !element.getElement().getCommonProperties().isMoveTop();
        }
    };
    
    boolean isValid(DisplayedScreenElement p0);
}
