package com.ankamagames.baseImpl.graphics.ui.shortcuts;

public class Shortcut
{
    private String m_id;
    private int m_keyCode;
    private String m_consoleCommand;
    private boolean m_ctrlKey;
    private boolean m_altKey;
    private boolean m_shiftKey;
    private boolean m_alwaysValid;
    private boolean m_onKeyReleased;
    private ShortcutTrigger m_trigger;
    private boolean m_enabled;
    private String m_groupName;
    private String m_category;
    private int m_modiferMask;
    private String m_paramString;
    
    public Shortcut(final String id, final int keyCode, final String consoleCommand, final boolean ctrlKey, final boolean altKey, final boolean shifKey, final boolean onKeyReleased, final boolean alwaysValid, final String params) {
        super();
        this.m_ctrlKey = false;
        this.m_altKey = false;
        this.m_shiftKey = false;
        this.m_alwaysValid = false;
        this.m_onKeyReleased = false;
        this.m_trigger = null;
        this.m_enabled = true;
        this.m_id = id;
        this.m_keyCode = keyCode;
        this.m_consoleCommand = consoleCommand;
        this.m_ctrlKey = ctrlKey;
        this.m_altKey = altKey;
        this.m_shiftKey = shifKey;
        this.m_onKeyReleased = onKeyReleased;
        this.m_alwaysValid = alwaysValid;
        this.m_paramString = params;
        this.applyModifiers();
    }
    
    private void applyModifiers() {
        this.m_modiferMask = AbstractShortcutManager.getModifierMask(this.m_shiftKey, this.m_altKey, this.m_ctrlKey);
    }
    
    public String getConsoleCommand() {
        return this.m_consoleCommand;
    }
    
    public int getKeyCode() {
        return this.m_keyCode;
    }
    
    public void setCtrlKey(final boolean ctrlKey) {
        this.m_ctrlKey = ctrlKey;
    }
    
    public void setAltKey(final boolean altKey) {
        this.m_altKey = altKey;
    }
    
    public void setShiftKey(final boolean shiftKey) {
        this.m_shiftKey = shiftKey;
    }
    
    public boolean isCtrlKey() {
        return this.m_ctrlKey;
    }
    
    public boolean isAltKey() {
        return this.m_altKey;
    }
    
    public boolean isShiftKey() {
        return this.m_shiftKey;
    }
    
    public boolean isAlwaysValid() {
        return this.m_alwaysValid;
    }
    
    public void setAlwaysValid(final boolean alwaysValid) {
        this.m_alwaysValid = alwaysValid;
    }
    
    public String getId() {
        return this.m_id;
    }
    
    public void setId(final String id) {
        this.m_id = id;
    }
    
    public boolean isKeyValid(final int keyCode) {
        return this.m_keyCode == keyCode;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public void setShortcutTrigger(final ShortcutTrigger trigger) {
        this.m_trigger = trigger;
    }
    
    public ShortcutTrigger getShortcutTrigger() {
        return this.m_trigger;
    }
    
    public String getGroupName() {
        return this.m_groupName;
    }
    
    public void setGroupName(final String groupName) {
        this.m_groupName = groupName;
    }
    
    public String getCategory() {
        return this.m_category;
    }
    
    public void setCategory(final String category) {
        this.m_category = category;
    }
    
    public void setKeyCode(final int keyCode) {
        this.m_keyCode = keyCode;
    }
    
    public int getModiferMask() {
        return this.m_modiferMask;
    }
    
    public void setModiferMask(final int modiferMask) {
        this.m_modiferMask = modiferMask;
        this.m_shiftKey = ((this.m_modiferMask & 0x40) == 0x40);
        this.m_altKey = ((this.m_modiferMask & 0x200) == 0x200);
        this.m_ctrlKey = ((this.m_modiferMask & 0x80) == 0x80);
    }
    
    public void setConsoleCommand(final String consoleCommand) {
        this.m_consoleCommand = consoleCommand;
    }
    
    public String getParamString() {
        return this.m_paramString;
    }
    
    public void setParamString(final String paramString) {
        this.m_paramString = paramString;
    }
    
    public boolean isOnKeyReleased() {
        return this.m_onKeyReleased;
    }
    
    public Shortcut getCopy() {
        final Shortcut shortcut = new Shortcut(this.m_id, this.m_keyCode, this.m_consoleCommand, this.m_ctrlKey, this.m_altKey, this.m_shiftKey, this.m_onKeyReleased, this.m_alwaysValid, this.m_paramString);
        shortcut.m_groupName = this.m_groupName;
        shortcut.m_category = this.m_category;
        return shortcut;
    }
}
