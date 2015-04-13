package com.ankamagames.baseImpl.graphics.ui.shortcuts;

import com.ankamagames.framework.kernel.core.controllers.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.util.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.awt.event.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class AbstractShortcutManager implements KeyboardController
{
    protected final String NAME_TAG = "name";
    protected final String ID_TAG = "id";
    protected final String CATEGORY_TAG = "category";
    protected final String GROUP_TAG = "group";
    protected final String SHORTCUT_TAG = "shortcut";
    protected final String CONSOLE_COMMAND_TAG = "consoleCommand";
    protected final String REBIND_ALLOWED_TAG = "rebindAllowed";
    protected final String ALWAYS_VALID_TAG = "alwaysValid";
    protected final String ALT_KEY_TAG = "altKey";
    protected final String CONTROL_KEY_TAG = "ctrlKey";
    protected final String SHIFT_KEY_TAG = "shiftKey";
    protected final String KEY_CODE_TAG = "keyCode";
    protected final String KEY_REGULAR_EXPRESSION_TAG = "keyRegExp";
    protected final String PARAMS_TAG = "params";
    protected final String ON_KEY_RELEASED_TAG = "onKeyReleased";
    protected static final String ROOT_TAG = "shortcuts";
    private static final Logger m_logger;
    protected final HashMap<String, Boolean> m_shortcutGroup;
    protected final ArrayList<Shortcut> m_shortcuts;
    protected final HashMap<String, Shortcut> m_shortcutsById;
    protected final TLongObjectHashMap<Shortcut> m_shortcutByCode;
    protected final ArrayList<Shortcut> m_waitingForKeyReleaseShortcuts;
    private boolean m_allShortcutsEnabled;
    private int m_currentKeyCode;
    public static final String BINDING_SHORTCUT_KEY = "binding";
    
    public AbstractShortcutManager() {
        super();
        this.m_shortcutGroup = new HashMap<String, Boolean>();
        this.m_shortcuts = new ArrayList<Shortcut>();
        this.m_shortcutsById = new HashMap<String, Shortcut>();
        this.m_shortcutByCode = new TLongObjectHashMap<Shortcut>();
        this.m_waitingForKeyReleaseShortcuts = new ArrayList<Shortcut>();
        this.m_allShortcutsEnabled = true;
        this.m_currentKeyCode = -1;
    }
    
    public void enableGroup(final String name, final boolean enable) {
        if (this.groupExists(name)) {
            this.m_shortcutGroup.put(name, enable);
        }
    }
    
    public void enableAllShortcuts() {
        for (final Shortcut shortcut : this.m_shortcutsById.values()) {
            shortcut.setEnabled(true);
        }
    }
    
    public void enableShortcut(final String id, final boolean enable) {
        final Shortcut shortcut = this.m_shortcutsById.get(id);
        if (shortcut != null) {
            shortcut.setEnabled(enable);
        }
    }
    
    public void loadFromXMLFile(final String xmlfilename, final boolean userFile) throws Exception {
        final DocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final DocumentContainer document = accessor.getNewDocumentContainer();
        accessor.open(xmlfilename);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        if (userFile) {
            final String filename = ContentFileHelper.getURL(xmlfilename).getFile();
            this.loadShortcutsFromUserFile(document, filename);
        }
        else {
            this.loadShortcutsFromDefaultFile(document);
        }
    }
    
    public void loadShortcutsFromDefaultFile(final DocumentContainer modelsDocument) {
        final ArrayList<DocumentEntry> groupEntries = modelsDocument.getEntriesByName("group");
        for (final DocumentEntry groupDoc : groupEntries) {
            if (groupDoc.getParameterByName("name") == null) {
                AbstractShortcutManager.m_logger.error((Object)"Nom de groupe invalide dans le chargement des raccourcis");
            }
            final String groupName = groupDoc.getParameterByName("name").getStringValue();
            if (!this.groupExists(groupName)) {
                this.m_shortcutGroup.put(groupName, false);
            }
            final ArrayList<DocumentEntry> shortcutsEntries = groupDoc.getChildrenByName("shortcut");
            if (shortcutsEntries == null) {
                continue;
            }
            this.addShortcutEntries(groupName, shortcutsEntries, false, null);
        }
    }
    
    public void loadShortcutsFromUserFile(final DocumentContainer modelsDocument, final String xmlFile) {
        final ArrayList<DocumentEntry> shortcutsEntries = modelsDocument.getEntriesByName("shortcut");
        if (shortcutsEntries == null) {
            return;
        }
        this.addShortcutEntries(null, shortcutsEntries, true, xmlFile);
    }
    
    private void addShortcutEntries(final String groupName, final List<DocumentEntry> shortcutsEntries, final boolean overwrite, final String xmlFilename) {
        final HashSet<Shortcut> modifiedShortcuts = new HashSet<Shortcut>();
        final HashSet<Shortcut> shortcutsToReset = new HashSet<Shortcut>();
        for (int i = 0, size = shortcutsEntries.size(); i < size; ++i) {
            final DocumentEntry d = shortcutsEntries.get(i);
            final DocumentEntry commandParam = d.getParameterByName("consoleCommand");
            final DocumentEntry idParam = d.getParameterByName("id");
            if (commandParam != null || idParam != null) {
                final String command = (commandParam != null) ? commandParam.getStringValue() : null;
                final boolean ctrlKey = d.getParameterByName("ctrlKey") != null && d.getParameterByName("ctrlKey").getBooleanValue();
                final boolean altKey = d.getParameterByName("altKey") != null && d.getParameterByName("altKey").getBooleanValue();
                final boolean shiftKey = d.getParameterByName("shiftKey") != null && d.getParameterByName("shiftKey").getBooleanValue();
                final boolean alwaysValid = d.getParameterByName("alwaysValid") != null && d.getParameterByName("alwaysValid").getBooleanValue();
                final String params = (d.getParameterByName("params") != null) ? d.getParameterByName("params").getStringValue() : null;
                final boolean onKeyReleased = d.getParameterByName("onKeyReleased") != null && d.getParameterByName("onKeyReleased").getBooleanValue();
                final int keyCode = (d.getParameterByName("keyCode") != null) ? d.getParameterByName("keyCode").getIntValue() : -1;
                final String id = (idParam != null) ? idParam.getStringValue() : null;
                if (overwrite) {
                    final Shortcut shortcutWithSameId = this.getShortcut(id);
                    if (shortcutWithSameId != null) {
                        modifiedShortcuts.add(shortcutWithSameId);
                        shortcutsToReset.remove(shortcutWithSameId);
                        final Shortcut shortcutWithSameCode = this.m_shortcutByCode.get(getKeyCode(keyCode, getModifierMask(shiftKey, altKey, ctrlKey)));
                        if (shortcutWithSameCode != null && !modifiedShortcuts.contains(shortcutWithSameCode)) {
                            shortcutsToReset.add(shortcutWithSameCode);
                        }
                        this.changeShortcut(shortcutWithSameId, keyCode, shiftKey, altKey, ctrlKey, params);
                    }
                }
                else {
                    final Shortcut shortcut = new Shortcut(id, keyCode, command, ctrlKey, altKey, shiftKey, onKeyReleased, alwaysValid, params);
                    final DocumentEntry categoryParam = d.getParameterByName("category");
                    final String category = (categoryParam != null) ? categoryParam.getStringValue() : null;
                    shortcut.setCategory(category);
                    this.addShortcut(shortcut, groupName);
                }
            }
        }
        final Iterator<Shortcut> it = shortcutsToReset.iterator();
        while (it.hasNext()) {
            this.setShortcutToUndefined(it.next(), xmlFilename);
        }
    }
    
    private void changeShortcut(final Shortcut shortcut, final int keyCode, final boolean shiftKey, final boolean altKey, final boolean ctrlKey, final String params) {
        this.changeShortcut(shortcut, keyCode, params, getModifierMask(shiftKey, altKey, ctrlKey));
    }
    
    private void changeShortcut(final Shortcut shortcut, final int keyCode, final String params, final int modifiers) {
        this.removeShortcut(shortcut);
        shortcut.setKeyCode(keyCode);
        shortcut.setModiferMask(modifiers);
        if (params != null) {
            shortcut.setParamString(params);
        }
        this.addShortcut(shortcut, shortcut.getGroupName());
    }
    
    public boolean isExistingShortcut(final Shortcut shortcut) {
        return this.m_shortcutsById.containsKey(shortcut.getId());
    }
    
    public Shortcut getShortcut(final String shortcutId) {
        return this.m_shortcutsById.get(shortcutId);
    }
    
    public void setShortcutToUndefined(final Shortcut shortcut, final String xmlfilename) {
        try {
            this.changeShortcut(shortcut, -1, null, 0, xmlfilename);
        }
        catch (Exception e) {
            AbstractShortcutManager.m_logger.warn((Object)"Probl\u00e8me en d\u00e9sassignant le raccourci", (Throwable)e);
        }
    }
    
    public void changeShortcut(final String shortcutId, final int keyCode, final String params, final int modifiersEx, final String XMLFileName) throws Exception {
        this.changeShortcut(this.getShortcut(shortcutId), keyCode, params, modifiersEx, XMLFileName);
    }
    
    public void changeShortcut(final Shortcut shortcut, final int keyCode, final String params, final int modifiersEx, final String xmlfilename) throws Exception {
        if (!this.isExistingShortcut(shortcut)) {
            return;
        }
        this.changeShortcut(shortcut, keyCode, params, modifiersEx);
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer document = accessor.getNewDocumentContainer();
        if (!FileHelper.isExistingFile(xmlfilename)) {
            accessor.create(xmlfilename);
            document.setRootNode(new XMLDocumentNode("shortcuts", null));
            accessor.writeWithHeader(document, "");
            accessor.close();
        }
        accessor.open(xmlfilename);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        final ArrayList<DocumentEntry> shortcutsEntries = document.getEntriesByName("shortcut");
        DocumentEntry shortcutEntry = null;
        if (shortcutsEntries != null) {
            for (final DocumentEntry d : shortcutsEntries) {
                if (d.getParameterByName("id") != null) {
                    if (!d.getParameterByName("id").getStringValue().equalsIgnoreCase(shortcut.getId())) {
                        continue;
                    }
                    shortcutEntry = d;
                    final int keyCodeStart = shortcut.getKeyCode();
                    if (d.getParameterByName("keyCode") == null) {
                        if (keyCodeStart == -1) {
                            AbstractShortcutManager.m_logger.warn((Object)"Le raccourci trouv\u00e9 ne poss\u00e8de pas de touche associ\u00e9e");
                            break;
                        }
                        final DocumentEntry documentEntry = new XMLNodeAttribute("keyCode", String.valueOf(keyCodeStart));
                        d.addParameter(documentEntry);
                    }
                    else {
                        d.getParameterByName("keyCode").setIntValue(keyCode);
                    }
                    if (shortcut.isShiftKey()) {
                        if (d.getParameterByName("shiftKey") != null) {
                            d.getParameterByName("shiftKey").setBooleanValue(true);
                        }
                        else {
                            final DocumentEntry documentEntry = new XMLNodeAttribute("shiftKey", String.valueOf(true));
                            d.addParameter(documentEntry);
                        }
                    }
                    else if (d.getParameterByName("shiftKey") != null) {
                        d.removeParameter(d.getParameterByName("shiftKey"));
                    }
                    if (shortcut.isAltKey()) {
                        if (d.getParameterByName("altKey") != null) {
                            d.getParameterByName("altKey").setBooleanValue(true);
                        }
                        else {
                            final DocumentEntry documentEntry = new XMLNodeAttribute("altKey", String.valueOf(true));
                            d.addParameter(documentEntry);
                        }
                    }
                    else if (d.getParameterByName("altKey") != null) {
                        d.removeParameter(d.getParameterByName("altKey"));
                    }
                    if (shortcut.isCtrlKey()) {
                        if (d.getParameterByName("ctrlKey") != null) {
                            d.getParameterByName("ctrlKey").setBooleanValue(true);
                        }
                        else {
                            final DocumentEntry documentEntry = new XMLNodeAttribute("ctrlKey", String.valueOf(true));
                            d.addParameter(documentEntry);
                        }
                    }
                    else {
                        if (d.getParameterByName("ctrlKey") == null) {
                            continue;
                        }
                        d.removeParameter(d.getParameterByName("ctrlKey"));
                    }
                }
            }
        }
        if (shortcutEntry == null) {
            this.addShortcutToXML(shortcut, document, false);
        }
        accessor.create(xmlfilename);
        accessor.write(document);
        accessor.close();
    }
    
    private void addShortcut(final Shortcut shortcut, final String groupName) {
        shortcut.setGroupName(groupName);
        this.m_shortcuts.add(shortcut);
        if (shortcut.getId() != null) {
            this.m_shortcutsById.put(shortcut.getId(), shortcut);
        }
        final long shortcutCode = getKeyCode(shortcut);
        this.m_shortcutByCode.put(shortcutCode, shortcut);
    }
    
    private void removeShortcut(final Shortcut shortcut) {
        this.m_shortcuts.remove(shortcut);
        this.m_shortcutsById.remove(shortcut.getId());
        final long shortcutCode = getKeyCode(shortcut);
        if (this.m_shortcutByCode.get(shortcutCode) == shortcut) {
            this.m_shortcutByCode.remove(shortcutCode);
        }
    }
    
    public void createShortcut(final Shortcut shortcut, final String shortcutGroupName, final String xmlfilename) throws Exception {
        if (this.isExistingShortcut(shortcut)) {
            return;
        }
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer document = accessor.getNewDocumentContainer();
        if (!FileHelper.isExistingFile(xmlfilename)) {
            accessor.create(xmlfilename);
            document.setRootNode(new XMLDocumentNode("shortcuts", null));
            accessor.writeWithHeader(document, "");
            accessor.close();
        }
        accessor.open(xmlfilename);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        this.addShortcut(shortcut, shortcutGroupName);
        this.addShortcutToXML(shortcut, document, true);
        accessor.create(xmlfilename);
        accessor.write(document);
        accessor.close();
    }
    
    public void deleteShortcut(final Shortcut shortcut, final String xmlfilename) throws Exception {
        if (!this.isExistingShortcut(shortcut)) {
            return;
        }
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer document = accessor.getNewDocumentContainer();
        if (!FileHelper.isExistingFile(xmlfilename)) {
            accessor.create(xmlfilename);
            document.setRootNode(new XMLDocumentNode("shortcuts", null));
            accessor.writeWithHeader(document, "");
            accessor.close();
        }
        accessor.open(xmlfilename);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        final ArrayList<DocumentEntry> entries = document.getEntriesByName("shortcut");
        if (entries == null) {
            AbstractShortcutManager.m_logger.error((Object)("impossible de supprimer le raccourci du document " + xmlfilename + "qui semble vide"));
            return;
        }
        for (final DocumentEntry d : entries) {
            if (d.getParameterByName("id") != null) {
                if (!d.getParameterByName("id").getStringValue().equalsIgnoreCase(shortcut.getId())) {
                    continue;
                }
                document.getRootNode().removeChild(d);
            }
        }
        this.removeShortcut(shortcut);
        accessor.create(xmlfilename);
        accessor.write(document);
        accessor.close();
    }
    
    public void addShortcutToXML(final Shortcut shortcut, final XMLDocumentContainer documentContainer, final boolean userCreatedShortcut) {
        final DocumentEntry shortcutEntry = new XMLDocumentNode("shortcut", null);
        if (shortcut.getId() != null && shortcut.getId().length() > 0) {
            shortcutEntry.addParameter(new XMLNodeAttribute("id", shortcut.getId()));
        }
        if (userCreatedShortcut) {
            if (shortcut.getGroupName() != null && shortcut.getGroupName().length() > 0) {
                shortcutEntry.addParameter(new XMLNodeAttribute("name", shortcut.getGroupName()));
            }
            if (shortcut.getConsoleCommand() != null && shortcut.getConsoleCommand().length() > 0) {
                shortcutEntry.addParameter(new XMLNodeAttribute("consoleCommand", shortcut.getConsoleCommand()));
            }
        }
        if (shortcut.getParamString() != null && shortcut.getParamString().length() > 0) {
            shortcutEntry.addParameter(new XMLNodeAttribute("params", shortcut.getParamString().replaceAll("\"", "&quot;")));
        }
        if (shortcut.isAltKey()) {
            shortcutEntry.addParameter(new XMLNodeAttribute("altKey", "true"));
        }
        if (shortcut.isCtrlKey()) {
            shortcutEntry.addParameter(new XMLNodeAttribute("ctrlKey", "true"));
        }
        if (shortcut.isShiftKey()) {
            shortcutEntry.addParameter(new XMLNodeAttribute("shiftKey", "true"));
        }
        shortcutEntry.addParameter(new XMLNodeAttribute("keyCode", String.valueOf(shortcut.getKeyCode())));
        documentContainer.getRootNode().addChild(shortcutEntry);
    }
    
    private DocumentEntry addGroup(final String groupName, final DocumentEntry root) {
        final DocumentEntry groupEntry = new XMLDocumentNode("group", null);
        if (groupName != null && groupName.length() > 0) {
            groupEntry.addParameter(new XMLNodeAttribute("name", groupName));
            root.addChild(groupEntry);
        }
        root.addChild(groupEntry);
        return groupEntry;
    }
    
    public void changeShortcutParams(final Shortcut shortcut, final String textToAdd, final String XMLFileName) throws Exception {
        if (!this.isExistingShortcut(shortcut)) {
            return;
        }
        shortcut.setParamString(textToAdd);
        final XMLDocumentAccessor accessor = XMLDocumentAccessor.getInstance();
        final XMLDocumentContainer document = accessor.getNewDocumentContainer();
        if (!FileHelper.isExistingFile(XMLFileName)) {
            accessor.create(XMLFileName);
            document.setRootNode(new XMLDocumentNode("shortcuts", null));
            accessor.writeWithHeader(document, "");
            accessor.close();
        }
        accessor.open(XMLFileName);
        accessor.read(document, new DocumentEntryParser[0]);
        accessor.close();
        final ArrayList<DocumentEntry> shortcutsEntries = document.getRootNode().getChildrenByName("shortcut");
        DocumentEntry shortcutEntry = null;
        if (shortcutsEntries != null) {
            for (final DocumentEntry d : shortcutsEntries) {
                if (d.getParameterByName("id") != null) {
                    if (!d.getParameterByName("id").getStringValue().equalsIgnoreCase(shortcut.getId())) {
                        continue;
                    }
                    shortcutEntry = d;
                    if (d.getParameterByName("params") == null) {
                        d.addParameter(new XMLNodeAttribute("params", textToAdd));
                    }
                    else {
                        d.getParameterByName("params").setStringValue(textToAdd);
                    }
                }
            }
        }
        if (shortcutEntry == null) {
            this.addShortcutToXML(shortcut, document, false);
        }
        accessor.create(XMLFileName);
        accessor.write(document);
    }
    
    public void setAllShortcutsEnabled(final boolean enable) {
        this.m_allShortcutsEnabled = enable;
    }
    
    public boolean areAllShortcutsEnabled() {
        return this.m_allShortcutsEnabled;
    }
    
    public void setShortcutTrigger(final String shortcutId, final ShortcutTrigger trigger) {
        final Shortcut shortcut = this.m_shortcutsById.get(shortcutId);
        if (shortcut != null && this.isGroupEnabled(shortcut.getGroupName())) {
            shortcut.setShortcutTrigger(trigger);
        }
    }
    
    public boolean isShortcutAlwaysActive(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 17 || keyEvent.getKeyCode() == 16 || keyEvent.getKeyCode() == 18) {
            return false;
        }
        final Shortcut shortcut = this.m_shortcutByCode.get(getKeyCode(keyEvent));
        return shortcut != null && this.isGroupEnabled(shortcut.getGroupName()) && shortcut.isAlwaysValid();
    }
    
    private boolean isShortcutExecutable(final Shortcut shortcut) {
        return shortcut != null && this.isGroupEnabled(shortcut.getGroupName()) && shortcut.isEnabled() && (this.m_allShortcutsEnabled || shortcut.isAlwaysValid());
    }
    
    @Override
    public boolean keyReleased(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 17 || keyEvent.getKeyCode() == 16 || keyEvent.getKeyCode() == 18) {
            return false;
        }
        this.m_currentKeyCode = -1;
        final Shortcut shortcut = this.m_shortcutByCode.get(getKeyCode(keyEvent.getKeyCode(), 0));
        if (!this.isShortcutExecutable(shortcut) || !shortcut.isOnKeyReleased()) {
            return false;
        }
        if (!this.m_waitingForKeyReleaseShortcuts.contains(shortcut)) {
            return false;
        }
        this.m_waitingForKeyReleaseShortcuts.remove(shortcut);
        return this.executeShortcut(shortcut);
    }
    
    @Override
    public boolean keyPressed(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 17 || keyEvent.getKeyCode() == 16 || keyEvent.getKeyCode() == 18 || this.m_currentKeyCode == keyEvent.getKeyCode()) {
            return false;
        }
        this.m_currentKeyCode = -1;
        final Shortcut shortcut = this.m_shortcutByCode.get(getKeyCode(keyEvent));
        if (!this.isShortcutExecutable(shortcut) || (shortcut.isOnKeyReleased() && this.m_waitingForKeyReleaseShortcuts.contains(shortcut))) {
            return false;
        }
        if (shortcut.isOnKeyReleased()) {
            this.m_waitingForKeyReleaseShortcuts.add(shortcut);
        }
        return this.executeShortcut(shortcut);
    }
    
    @Override
    public boolean keyTyped(final KeyEvent keyEvent) {
        return false;
    }
    
    public abstract boolean executeShortcut(final Shortcut p0);
    
    public Shortcut[] getShortcuts() {
        return this.m_shortcuts.toArray(new Shortcut[this.m_shortcuts.size()]);
    }
    
    public boolean groupExists(final String groupName) {
        return this.m_shortcutGroup.containsKey(groupName);
    }
    
    public boolean isGroupEnabled(final String groupName) {
        final Boolean enabled = this.m_shortcutGroup.get(groupName);
        return enabled != null && enabled;
    }
    
    public void releaseCurrentKeyCode() {
        for (final Shortcut shortcut : this.m_waitingForKeyReleaseShortcuts) {
            this.executeShortcut(shortcut);
        }
        this.m_waitingForKeyReleaseShortcuts.clear();
        this.setCurrentKeyCode(-1);
    }
    
    public void setCurrentKeyCode(final int currentKeyCode) {
        this.m_currentKeyCode = currentKeyCode;
    }
    
    public static long getKeyCode(final KeyEvent e) {
        final int mod = e.getModifiersEx();
        final boolean shift = (mod & 0x40) == 0x40;
        final boolean alt = (mod & 0x200) == 0x200;
        final boolean ctrl = (mod & 0x80) == 0x80;
        return getKeyCode(e.getKeyCode(), getModifierMask(shift, alt, ctrl));
    }
    
    public static long getKeyCode(final Shortcut shortcut) {
        return getKeyCode(shortcut.getKeyCode(), shortcut.getModiferMask());
    }
    
    public static long getKeyCode(final int code, final int mask) {
        return MathHelper.getLongFromTwoInt(code, mask);
    }
    
    public static int getModifierMask(final boolean shift, final boolean alt, final boolean ctrl) {
        int modifier = 0;
        if (shift) {
            modifier |= 0x40;
        }
        if (alt) {
            modifier |= 0x200;
        }
        if (ctrl) {
            modifier |= 0x80;
        }
        return modifier;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractShortcutManager.class);
    }
}
