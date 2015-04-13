package com.ankamagames.wakfu.common.constants;

import com.ankamagames.framework.external.*;
import java.awt.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public enum KrosmozGame implements ExportableEnum
{
    KROSMASTER_ARENA((byte)1, "Krosmaster Arena", true, ActivationConstants.KROZMASTER_UNLOCK_DATE, false, true, new Dimension(920, 500), false), 
    BROWSER((byte)2, "Browser", true, (GameDateConst)new GameDate(0, 0, 0, 1, 1, 2000), true, false, new Dimension(920, 500), true), 
    SHOP((byte)3, "Shop", true, (GameDateConst)new GameDate(0, 0, 0, 1, 1, 2000), false, true, new Dimension(920, 500), true), 
    FULL_SCREEN_BROWSER((byte)4, "FullScreenBrowser", true, (GameDateConst)new GameDate(0, 0, 0, 1, 1, 2000), true, true, new Dimension(920, 500), true), 
    FULL_SCREEN_BROWSER_WITHOUT_CONTROLS((byte)5, "FullScreenBrowserWithoutControls", true, (GameDateConst)new GameDate(0, 0, 0, 1, 1, 2000), false, true, new Dimension(920, 500), false);
    
    private final byte m_id;
    private final String m_label;
    private final boolean m_displayOnLoad;
    private final GameDateConst m_unlockDate;
    private final boolean m_displayBrowserControls;
    private final boolean m_fullScreen;
    private final boolean m_displayDecorations;
    private final Dimension m_size;
    
    private KrosmozGame(final byte id, final String label, final boolean displayOnLoad, final GameDateConst unlockDate, final boolean displayBrowserControls, final boolean fullScreen, final Dimension size, final boolean displayDecorations) {
        this.m_id = id;
        this.m_label = label;
        this.m_displayOnLoad = displayOnLoad;
        this.m_unlockDate = unlockDate;
        this.m_displayBrowserControls = displayBrowserControls;
        this.m_fullScreen = fullScreen;
        this.m_size = size;
        this.m_displayDecorations = displayDecorations;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public boolean isDisplayOnLoad() {
        return this.m_displayOnLoad;
    }
    
    public boolean isDisplayBrowserControls() {
        return this.m_displayBrowserControls;
    }
    
    public boolean isFullScreen() {
        return this.m_fullScreen || OS.isMacOs();
    }
    
    public Dimension getSize() {
        return this.m_size;
    }
    
    public boolean isDisplayDecorations() {
        return this.m_displayDecorations;
    }
    
    public static KrosmozGame byId(final byte id) {
        for (final KrosmozGame game : values()) {
            if (game.m_id == id) {
                return game;
            }
        }
        return null;
    }
    
    public GameDateConst getUnlockDate() {
        return this.m_unlockDate;
    }
}
