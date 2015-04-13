package com.ankamagames.xulor2;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.framework.kernel.core.translator.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.baseImpl.graphics.isometric.text.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.xulor2.util.xmlToJava.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.xulor2.graphics.*;
import java.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.xulor2.util.*;
import java.net.*;
import gnu.trove.*;

public class Xulor implements UISceneEventListener, PreferencePropertyChangeListener, UIManager
{
    private static Logger m_logger;
    private static Xulor m_instance;
    public static final boolean LOGGER_DEBUG = false;
    public static final long OPTION_NONE = 0L;
    public static final long OPTION_ON_TOP = 1L;
    public static final long OPTION_BEFORE_MSGBOX_LAYER = 2L;
    public static final long OPTION_ON_MSGBOX_LAYER = 4L;
    public static final long OPTION_BEFORE_POPUP = 8L;
    public static final long OPTION_FORCE_RELOAD = 16L;
    public static final long OPTION_DONT_REBUILD = 32L;
    public static final long OPTION_PARSE_ONLY = 64L;
    public static final long OPTION_RETURN_CREATED_ELEMENT = 128L;
    public static final long OPTION_MODAL = 256L;
    public static final long OPTION_PSEUDO_MODAL = 512L;
    public static final long OPTION_USER_DEFINED_FULL = 1024L;
    public static final long OPTION_IF_NOT_LOADED = 2048L;
    public static final long OPTION_DELAYED = 4096L;
    public static final long OPTION_UNCLOSABLE = 8192L;
    public static final long OPTION_USER_DEFINED_SIZE = 16384L;
    public static final long OPTION_USER_DEFINED_POSITION = 32768L;
    public static final long OPTION_WORLD_LAYER = 65536L;
    public static final long OPTION_BEFORE_UPPER_CONTENT_LAYER = 131072L;
    public static final long OPTION_BOTTOM_UI_LAYER = 262144L;
    public static final int BASE_LAYER = 0;
    public static final int TOP_LAYER = 1;
    public static final int BEFORE_UPPER_CONTENT_LAYER = 25999;
    public static final int BEFORE_MSGBOX_LAYER = 26999;
    public static final int MSGBOX_LAYER = 27000;
    public static final int BEFORE_POPUP_LAYER = 29999;
    private GLApplicationUI m_appUI;
    private XulorScene m_scene;
    private final DocumentParser m_parser;
    private final PropertiesProvider m_propertiesProvider;
    private final Environment m_environment;
    private final Environment m_privateEnvironment;
    private AbstractDialogClosesManager m_dialogClosesManager;
    private MRUManager m_mruManager;
    private Translator m_translator;
    private AbstractShortcutManager m_shortcutManager;
    public static final int MAX_DURATION = Integer.MAX_VALUE;
    public static final int INVALID_COORDINATE = Integer.MIN_VALUE;
    private final ArrayList<XulorLoadUnload> m_loadUnloadDocuments;
    private final ArrayList<XulorUnload> m_timedUnloadDocuments;
    private final THashMap<String, EventDispatcher> m_loadedElements;
    private final HashMap<String, Class<?>> m_actionClasses;
    private URL m_currentDirectory;
    private boolean m_needToRebuildUI;
    private int m_messageBoxId;
    private final TIntObjectHashMap<DialogPathDefinition> m_messageBoxDefinitions;
    private int m_popupMenuId;
    private DialogPathDefinition m_popupDefinition;
    private PopupMenu m_popupMenu;
    private int m_mruId;
    private DialogPathDefinition m_mruDefinition;
    private UniversalRadialMenu m_mru;
    private boolean m_displayTooltips;
    public String m_animationPath;
    public String m_particlePath;
    public String m_shaderPath;
    private final ArrayList<String> m_loadedModals;
    private final ArrayList<BigDialogLoadListener> m_bigDialogLoadListeners;
    private final ArrayList<BigDialogLoadListener> m_bigDialogLoadListenersToRemove;
    private final ArrayList<DialogUnloadListener> m_dialogUnloadListeners;
    private final ArrayList<DialogUnloadListener> m_dialogUnloadListenersToRemove;
    private final ArrayList<DialogLoadListener> m_dialogLoadListeners;
    private final ArrayList<DialogLoadListener> m_dialogLoadListenersToRemove;
    private final Stack<MessageBoxControler> m_messageBoxControlers;
    private final ArrayList<String> m_bigDialogs;
    private Color m_modalBackgroundColor;
    private int m_elapsedTime;
    private boolean m_readyToLoadDocuments;
    private UserDefinedAdapter m_userDefinedAdapter;
    private final HashMap<String, DialogPathDefinition> m_factories;
    
    public static Xulor getInstance() {
        return Xulor.m_instance;
    }
    
    private Xulor() {
        super();
        this.m_parser = new DocumentParser();
        this.m_propertiesProvider = PropertiesProvider.getInstance();
        this.m_environment = new Environment();
        this.m_privateEnvironment = new Environment();
        this.m_loadUnloadDocuments = new ArrayList<XulorLoadUnload>();
        this.m_timedUnloadDocuments = new ArrayList<XulorUnload>();
        this.m_loadedElements = new THashMap<String, EventDispatcher>();
        this.m_messageBoxDefinitions = new TIntObjectHashMap<DialogPathDefinition>();
        this.m_displayTooltips = true;
        this.m_loadedModals = new ArrayList<String>();
        this.m_bigDialogLoadListeners = new ArrayList<BigDialogLoadListener>();
        this.m_bigDialogLoadListenersToRemove = new ArrayList<BigDialogLoadListener>();
        this.m_dialogUnloadListeners = new ArrayList<DialogUnloadListener>();
        this.m_dialogUnloadListenersToRemove = new ArrayList<DialogUnloadListener>();
        this.m_dialogLoadListeners = new ArrayList<DialogLoadListener>();
        this.m_dialogLoadListenersToRemove = new ArrayList<DialogLoadListener>();
        this.m_messageBoxControlers = new Stack<MessageBoxControler>();
        this.m_bigDialogs = new ArrayList<String>();
        this.m_modalBackgroundColor = new Color(0, 0, 0, 0);
        this.m_readyToLoadDocuments = true;
        this.m_factories = new HashMap<String, DialogPathDefinition>();
        this.m_actionClasses = new HashMap<String, Class<?>>();
        this.putActionClass("xulor", XulorActions.class);
    }
    
    @Override
    public void addDialogUnloadListener(final DialogUnloadListener l) {
        if (l != null && !this.m_dialogUnloadListeners.contains(l)) {
            this.m_dialogUnloadListeners.add(l);
        }
    }
    
    @Override
    public void removeDialogUnloadListener(final DialogUnloadListener l) {
        this.m_dialogUnloadListenersToRemove.add(l);
    }
    
    public void addBigDialogLoadListener(final BigDialogLoadListener l) {
        if (l != null && !this.m_bigDialogLoadListeners.contains(l)) {
            this.m_bigDialogLoadListeners.add(l);
        }
    }
    
    public void removeBigDialogLoadListener(final BigDialogLoadListener l) {
        this.m_bigDialogLoadListenersToRemove.add(l);
    }
    
    @Override
    public void addDialogLoadListener(final DialogLoadListener l) {
        if (l != null && !this.m_dialogLoadListeners.contains(l)) {
            this.m_dialogLoadListeners.add(l);
        }
    }
    
    @Override
    public void removeDialogLoadListener(final DialogLoadListener l) {
        this.m_dialogLoadListenersToRemove.add(l);
    }
    
    public void addBigDialog(final String dialogId) {
        if (dialogId != null && !this.m_bigDialogs.contains(dialogId)) {
            this.m_bigDialogs.add(dialogId);
        }
    }
    
    public void removeBigDialog(final String dialogId) {
        this.m_bigDialogs.remove(dialogId);
    }
    
    public void clearBigDialogs() {
        this.m_bigDialogs.clear();
    }
    
    public Color getModalBackgroundColor() {
        return this.m_modalBackgroundColor;
    }
    
    public void setModalBackgroundColor(final Color modalBackgroundColor) {
        this.m_modalBackgroundColor = modalBackgroundColor;
    }
    
    public MRUManager getMruManager() {
        return this.m_mruManager;
    }
    
    public void setMruManager(final MRUManager mruManager) {
        this.m_mruManager = mruManager;
    }
    
    public void setDialogClosesManager(final AbstractDialogClosesManager manager) {
        this.m_dialogClosesManager = manager;
    }
    
    public AbstractDialogClosesManager getDialogClosesManager() {
        return this.m_dialogClosesManager;
    }
    
    public void setShortcutManager(final AbstractShortcutManager shortcutManager) {
        this.m_shortcutManager = shortcutManager;
    }
    
    public AbstractShortcutManager getShortcutManager() {
        return this.m_shortcutManager;
    }
    
    public DocumentParser getDocumentParser() {
        return this.m_parser;
    }
    
    public Environment getEnvironment() {
        return this.m_environment;
    }
    
    @Override
    public void setAppUI(final GLApplicationUI ui) {
        this.m_appUI = ui;
    }
    
    public GLApplicationUI getAppUI() {
        return this.m_appUI;
    }
    
    public XulorScene getScene() {
        return this.m_scene;
    }
    
    public void setScene(final XulorScene scene) {
        this.m_scene = scene;
    }
    
    public UserDefinedAdapter getUserDefinedAdapter() {
        return this.m_userDefinedAdapter;
    }
    
    @Override
    public void setUserDefinedAdapter(final UserDefinedAdapter userDefinedAdapter) {
        this.m_userDefinedAdapter = userDefinedAdapter;
        this.initializePreferences();
    }
    
    private void initializePreferences() {
        final PreferenceStore prefStore = GamePreferences.getDefaultPreferenceStore();
        if (prefStore != null) {
            if (prefStore.contains("tooltipsDisplay")) {
                this.m_displayTooltips = prefStore.getBoolean("tooltipsDisplay");
            }
            if (prefStore.contains("tooltipsDuration")) {
                Tooltip.setDefaultDuration(prefStore.getInt("tooltipsDuration"));
            }
        }
    }
    
    @Nullable
    public PreferenceStore getPreferenceStore(final String id) {
        return this.m_userDefinedAdapter.getPreferenceStoreForDialog(id);
    }
    
    @Override
    public void propertyChange(final PreferencePropertyChangeEvent event) {
        final String name = event.getPropertyName();
        if (name.equalsIgnoreCase("tooltipsDisplay")) {
            this.m_displayTooltips = (boolean)event.getNewValue();
        }
        else if (name.equalsIgnoreCase("tooltipsDuration")) {
            Tooltip.setDefaultDuration((int)event.getNewValue());
        }
    }
    
    public String getTranslatedString(final String key) {
        if (this.m_translator != null) {
            return this.m_translator.getString(key);
        }
        return key;
    }
    
    public void setTranslator(final Translator translator) {
        this.m_translator = translator;
    }
    
    public String getDebugInfos() {
        final StringBuilder builder = new StringBuilder("# XULOR INFOS #");
        builder.append('\n').append("- loadedElementCount = ").append(this.m_loadedElements.size());
        builder.append('\n').append("- loadedElements = \n");
        if (!this.m_loadedElements.isEmpty()) {
            this.m_loadedElements.forEachKey(new TObjectProcedure<String>() {
                @Override
                public boolean execute(final String elementId) {
                    builder.append("\t").append(elementId).append('\n');
                    return true;
                }
            });
        }
        return builder.toString();
    }
    
    public URL getCurrentDirectory() {
        return this.m_currentDirectory;
    }
    
    public void setCurrentDirectory(final URL url) {
        this.m_currentDirectory = url;
    }
    
    public boolean isActionClassLoaded(final String packageName) {
        return this.m_actionClasses.containsKey(packageName);
    }
    
    public void putActionClass(final String packageName, final Class<?> actionClass) {
        this.m_actionClasses.put(packageName, actionClass);
    }
    
    public void removeActionClass(final String packageName) {
        this.m_actionClasses.remove(packageName);
    }
    
    public void removeAllActionClass() {
        this.m_actionClasses.clear();
        this.putActionClass("xulor", XulorActions.class);
    }
    
    public Class<?> getActionClass() {
        return this.getActionClass("xulor");
    }
    
    public Class<?> getActionClass(final String packageName) {
        if (packageName == null) {
            return this.m_actionClasses.get("xulor");
        }
        if (!this.m_actionClasses.containsKey(packageName)) {
            Xulor.m_logger.error((Object)("Le package " + packageName + " est inconnue !"));
            return null;
        }
        return this.m_actionClasses.get(packageName);
    }
    
    public DialogPathDefinition getDialogPathDefinition(final String path) {
        final DialogPathDefinition def = DialogPathDefinition.createDialogPathDefinition(path);
        this.m_factories.put(path, def);
        return def;
    }
    
    public void setMessageBoxPath(final String path) {
        final DialogPathDefinition definition = this.getDialogPathDefinition(path);
        if (definition != null) {
            this.m_messageBoxDefinitions.put(0, definition);
        }
    }
    
    public void setMessageBoxPathes(final TIntObjectHashMap<String> paths) {
        final TIntObjectIterator<String> it = paths.iterator();
        while (it.hasNext()) {
            it.advance();
            final DialogPathDefinition definition = this.getDialogPathDefinition(it.value());
            if (definition != null) {
                this.m_messageBoxDefinitions.put(it.key(), definition);
            }
        }
    }
    
    @Deprecated
    public MessageBoxControler msgBox(final String message) {
        return this.msgBox(message, " ", 2L, 0, 0);
    }
    
    @Deprecated
    public MessageBoxControler msgBox(final String message, final long options) {
        return this.msgBox(message, null, options, 0, 0);
    }
    
    @Deprecated
    public MessageBoxControler msgBox(final String message, final String title, final long options) {
        return this.msgBox(message, title, null, options, 0, 0);
    }
    
    public MessageBoxControler msgBox(final String message, final int category, final int level) {
        return this.msgBox(message, " ", null, 2L, category, level);
    }
    
    public MessageBoxControler msgBox(final String message, final long options, final int category, final int level) {
        return this.msgBox(message, " ", null, options, category, level);
    }
    
    public MessageBoxControler msgBox(final String message, final String iconUrl, final long options, final int category, final int level) {
        return this.msgBox(message, " ", iconUrl, options, category, level);
    }
    
    public MessageBoxControler msgBox(final String message, final String title, final String iconUrl, final long options, final int category, final int level) {
        return this.msgBox(message, title, iconUrl, options, null, category, level);
    }
    
    public MessageBoxControler msgBox(final String message, final String title, final String iconUrl, final long options, @Nullable final ArrayList<String> customMessages, final int category, final int level) {
        return this.msgBox(new MessageBoxData(category, level, message, title, iconUrl, options, customMessages, 0));
    }
    
    public MessageBoxControler msgBox(final MessageBoxData data) {
        return MessageBoxManager.getInstance().msgBox(data);
    }
    
    public MessageBoxControler messageBox(final MessageBoxData data) {
        MessageBoxControler controler = null;
        final DialogPathDefinition definition = this.m_messageBoxDefinitions.get(data.getType());
        if (definition != null) {
            final String id = "MessageBox_" + this.m_messageBoxId++;
            if (this.m_messageBoxId > 2147483646) {
                this.m_messageBoxId = 0;
            }
            controler = new MessageBoxControler(id, data);
            this.messageBox(definition, controler, data);
        }
        return controler;
    }
    
    private void messageBox(final DialogPathDefinition definition, final MessageBoxControler controler, final MessageBoxData data) {
        if (definition == null) {
            return;
        }
        try {
            final ElementMap elementMap = this.m_environment.createElementMap(controler.getMessageBoxId());
            this.m_environment.setCurrentElementMap(elementMap);
            final EventDispatcher element = this.parse(definition, this.m_environment, elementMap);
            if (element instanceof Window) {
                element.setElementMapRoot(true);
                final Window messageBox = (Window)element;
                messageBox.setId(controler.getMessageBoxId());
                messageBox.setModalLevel(ModalManager.MSG_BOX_MODAL_LEVEL);
                this.insert(messageBox.getId(), messageBox, MasterRootContainer.getInstance().getLayeredContainer(), 26000, 256L);
                MessageBoxFormater.format(messageBox, controler, data);
                data.addListener(new MessageBoxDataListener() {
                    @Override
                    public void messageChanged() {
                        try {
                            MessageBoxFormater.setMessage(messageBox, data);
                        }
                        catch (Exception e) {
                            Xulor.m_logger.error((Object)("Unable to reformat MessageBox " + data), (Throwable)e);
                        }
                    }
                });
                this.registerMessageBoxControler(controler);
                XulorSoundManager.getInstance().alert();
            }
            this.m_needToRebuildUI = true;
        }
        catch (Exception e) {
            Xulor.m_logger.error((Object)"Erreur lors du chargement de la messageBox", (Throwable)e);
        }
    }
    
    public void setPopupMenuPath(final String path) {
        this.m_popupDefinition = this.getDialogPathDefinition(path);
        if (this.m_popupDefinition == null) {
            Xulor.m_logger.error((Object)("Le chemin '" + path + "' vers le fichier de d\u00e9finition des popupMenu est invalide !"));
        }
    }
    
    public PopupMenu popupMenu() {
        if (this.m_popupDefinition == null) {
            return null;
        }
        PopupMenu popupMenu = null;
        try {
            final String id = "PopupMenu_" + this.m_popupMenuId++;
            if (this.m_popupMenuId > 2147483646) {
                this.m_popupMenuId = 0;
            }
            final ElementMap elementMap = this.m_environment.createElementMap(id);
            this.m_environment.setCurrentElementMap(elementMap);
            final EventDispatcher element = this.parse(this.m_popupDefinition, this.m_environment, elementMap);
            if (element != null && element instanceof PopupMenu) {
                element.setElementMapRoot(true);
                popupMenu = (PopupMenu)element;
                popupMenu.setId(id);
                popupMenu.setModalLevel(ModalManager.POP_UP_MODAL_LEVEL);
                popupMenu.setVisible(false);
                this.m_scene.getMasterRootContainer().getLayeredContainer().addWidgetToLayer(popupMenu, 30000);
                this.m_loadedElements.put(id, popupMenu);
            }
            this.m_needToRebuildUI = true;
        }
        catch (Exception e) {
            Xulor.m_logger.error((Object)e.getMessage());
        }
        return popupMenu;
    }
    
    public void showPopupMenu(final PopupMenu popupMenu) {
        this.hidePopupMenu();
        (this.m_popupMenu = popupMenu).show();
    }
    
    public void showPopupMenu(final PopupMenu popupMenu, final int x, final int y) {
        this.hidePopupMenu();
        (this.m_popupMenu = popupMenu).show(x, y);
    }
    
    public void hidePopupMenu() {
        if (this.m_popupMenu != null) {
            this.unload(this.m_popupMenu.getId());
        }
    }
    
    public void setMRUPath(final String path) {
        this.m_mruDefinition = this.getDialogPathDefinition(path);
        if (this.m_mruDefinition == null) {
            Xulor.m_logger.error((Object)("Le chemin '" + path + "' vers le fichier de d\u00e9finition des mrus est invalide !"));
        }
    }
    
    public void closeAllMRU() {
        final Collection<EventDispatcher> dispatchersToUnload = new ArrayList<EventDispatcher>();
        if (!this.m_loadedElements.isEmpty()) {
            this.m_loadedElements.forEachValue(new TObjectProcedure<EventDispatcher>() {
                @Override
                public boolean execute(final EventDispatcher ed) {
                    try {
                        if (!ed.isUnloading() && ed.getElementMap().getId().equals("MRU")) {
                            dispatchersToUnload.add(ed);
                        }
                    }
                    catch (Exception e) {
                        Xulor.m_logger.error((Object)"Exception lev\u00e9e lors du parcours des \u00e9l\u00e9ments charg\u00e9s pour pouvoir fermer les MRU", (Throwable)e);
                    }
                    return true;
                }
            });
        }
        for (final EventDispatcher ed : dispatchersToUnload) {
            this.unload(ed.getElementMap().getId(), true);
        }
    }
    
    public UniversalRadialMenu mru() {
        if (this.m_mruDefinition == null) {
            return null;
        }
        UniversalRadialMenu mru = null;
        try {
            final String id = "MRU";
            final Widget previousMRU = this.m_loadedElements.get("MRU");
            if (previousMRU != null) {
                final WidgetRemovalRequestedEvent e = new WidgetRemovalRequestedEvent(previousMRU);
                e.onCheckOut();
                previousMRU.dispatchEvent(e);
                this.unload("MRU", true);
            }
            final ElementMap elementMap = this.m_environment.createElementMap("MRU");
            this.m_environment.setCurrentElementMap(elementMap);
            final EventDispatcher element = this.parse(this.m_mruDefinition, this.m_environment, elementMap);
            if (element != null && element instanceof UniversalRadialMenu) {
                element.setElementMapRoot(true);
                mru = (UniversalRadialMenu)element;
                mru.setId("MRU");
                mru.setModalLevel(ModalManager.POP_UP_MODAL_LEVEL);
                mru.setVisible(false);
                this.getScene().getMasterRootContainer().getLayeredContainer().addWidgetToLayer(mru, 0);
                this.m_loadedElements.put("MRU", mru);
            }
            this.m_needToRebuildUI = true;
        }
        catch (Exception e2) {
            Xulor.m_logger.error((Object)e2.getMessage());
        }
        return mru;
    }
    
    public void showMRU(final UniversalRadialMenu mru) {
        this.hideMRU();
        (this.m_mru = mru).show();
    }
    
    public void showMRU(final UniversalRadialMenu mru, int x, int y) {
        this.hideMRU();
        this.m_mru = mru;
        final XulorScene scene = getInstance().getScene();
        if (scene.isScaled()) {
            x = scene.getScaleMouseX(x);
            y = scene.getScaleMouseY(y);
        }
        mru.show(x, y);
    }
    
    public void hideMRU() {
        if (this.m_mru != null && !this.m_mru.isUnloading()) {
            this.unload(this.m_mru.getElementMap().getId());
        }
    }
    
    public boolean getDisplayTooltips() {
        return this.m_displayTooltips;
    }
    
    public EventDispatcher getLoadedElement(final String id) {
        return this.m_loadedElements.get(id);
    }
    
    public Iterator<EventDispatcher> loadedElementIterator() {
        return this.m_loadedElements.values().iterator();
    }
    
    public void setWatcherTarget(final String id, final ScreenTarget target) {
        this.m_loadUnloadDocuments.add(new XulorSetWatcherTarget(id, target));
    }
    
    public void generateTheme(final String file, final String themeDirectory, final ThemeCompileData data) throws Exception {
        this.m_parser.generateThemeFile(ContentFileHelper.getURL(file), themeDirectory, data);
    }
    
    public void loadTheme(final StyleProvider sp, final ThemeLoader tl, final String themeDirectory) {
        this.m_parser.loadThemeFile(sp, tl, themeDirectory);
    }
    
    public void loadTheme(final String file, final String themeDirectory) {
        try {
            this.m_parser.loadThemeFile(ContentFileHelper.getURL(file), themeDirectory);
        }
        catch (Exception e) {
            Xulor.m_logger.error((Object)("Erreur lors du chargement du Theme : " + e.getMessage()));
        }
    }
    
    @Override
    public void reloadTextures() {
        TextureLoader.getInstance().removeAllTextures();
        this.m_parser.setNeedToLoadTextures(true);
    }
    
    public EventDispatcher load(final String id, final String resource, final short modalLevel) {
        return this.load(id, resource, 0L, modalLevel);
    }
    
    public EventDispatcher loadNear(final String id, final String resource, final String relativeElementId, final boolean isOver, final short modalLevel) {
        return this.loadNear(id, resource, relativeElementId, isOver, 0L, modalLevel);
    }
    
    public EventDispatcher loadAsTooltip(final String id, final String resource, final String relativeElementId, final boolean isOver, final int tooltipX, final int tooltipY, final short modalLevel) {
        return this.loadAsTooltip(id, resource, relativeElementId, isOver, tooltipX, tooltipY, 0L, modalLevel);
    }
    
    public EventDispatcher load(final String id, final String resource, final int duration, final short modalLevel) {
        return this.load(id, resource, duration, 0L, modalLevel);
    }
    
    public EventDispatcher loadNear(final String id, final String resource, final int duration, final String relativeElementId, final boolean isOver, final short modalLevel) {
        return this.loadNear(id, resource, duration, relativeElementId, isOver, 0L, modalLevel);
    }
    
    public EventDispatcher loadAsTooltip(final String id, final String resource, final int duration, final String relativeElementId, final boolean isOver, final int tooltipX, final int tooltipY, final short modalLevel) {
        return this.loadAsTooltip(id, resource, duration, relativeElementId, isOver, tooltipX, tooltipY, 0L, modalLevel);
    }
    
    public EventDispatcher load(final String id, final String resource, final long options, final short modalLevel) {
        return this.load(id, resource, Integer.MAX_VALUE, options, modalLevel);
    }
    
    public EventDispatcher loadNear(final String id, final String resource, final String relativeElementId, final boolean isOver, final long options, final short modalLevel) {
        return this.loadNear(id, resource, Integer.MAX_VALUE, relativeElementId, isOver, options, modalLevel);
    }
    
    public EventDispatcher loadAsTooltip(final String id, final String resource, final String relativeElementId, final boolean isOver, final int tooltipX, final int tooltipY, final long options, final short modalLevel) {
        return this.loadAsTooltip(id, resource, Integer.MAX_VALUE, relativeElementId, isOver, tooltipX, tooltipY, options, modalLevel);
    }
    
    public EventDispatcher load(final String id, final String resource, final int duration, final long options, final short modalLevel) {
        final XulorLoadBuilder b = new XulorLoadBuilder();
        b.setId(id).setPath(resource).setDuration(duration).setOptions(options).setLevel(modalLevel);
        return this.loadInto(b.build());
    }
    
    public EventDispatcher loadAsMultiple(final String id, final String resource, final String relativeCascadeElementId, final String relativePositionElementId, final String controlGroupId, final long options, final short modalLevel) {
        final XulorLoadBuilder b = new XulorLoadBuilder();
        b.setId(id).setPath(resource).setRelativeCascadeElementId(relativeCascadeElementId).setRelativePositionElementId(relativePositionElementId).setControlGroupId(controlGroupId).setOptions(options).setLevel(modalLevel);
        return this.loadInto(b.build());
    }
    
    public EventDispatcher loadNear(final String id, final String resource, final int duration, final String relativeElement, final boolean isOver, final long options, final short modalLevel) {
        final XulorLoadBuilder b = new XulorLoadBuilder();
        b.setId(id).setPath(resource).setRelativeElementId(relativeElement).setOverRelative(isOver).setDuration(duration).setOptions(options).setLevel(modalLevel);
        return this.loadInto(b.build());
    }
    
    public EventDispatcher loadAsTooltip(final String id, final String resource, final int duration, final String relativeElement, final boolean isOver, final int tooltipX, final int tooltipY, final long options, final short modalLevel) {
        final XulorLoadBuilder b = new XulorLoadBuilder();
        b.setId(id).setPath(resource).setOverRelative(isOver).setAsTooltip(true).setTooltipX(tooltipX).setTooltipY(tooltipY).setDuration(duration).setOptions(options).setLevel(modalLevel);
        return this.loadInto(b.build());
    }
    
    public EventDispatcher loadInto(final String resource, final EventDispatcher container, final String relativeCascadeElementId, final String relativePositionElementId, final String controlGroupId, final ElementMap elementMap, final long options, final short modalLevel) {
        final XulorLoadBuilder b = new XulorLoadBuilder();
        b.setPath(resource).setParent(container).setRelativeCascadeElementId(relativeCascadeElementId).setRelativePositionElementId(relativePositionElementId).setControlGroupId(controlGroupId).setElementMap(elementMap).setOptions(options).setLevel(modalLevel);
        return this.loadInto(b.build());
    }
    
    public EventDispatcher loadNear(final String resource, final EventDispatcher container, final String relativeElementId, final boolean isOverRelative, final ElementMap elementMap, final long options, final short modalLevel) {
        final XulorLoadBuilder b = new XulorLoadBuilder();
        b.setPath(resource).setParent(container).setRelativeElementId(relativeElementId).setOverRelative(isOverRelative).setElementMap(elementMap).setOptions(options).setLevel(modalLevel);
        return this.loadInto(b.build());
    }
    
    public EventDispatcher loadAsTooltip(final String resource, final EventDispatcher container, final String relativeElementId, final boolean isOverRelative, final int tooltipX, final int tooltipY, final ElementMap elementMap, final long options, final short modalLevel) {
        final XulorLoadBuilder b = new XulorLoadBuilder();
        b.setPath(resource).setParent(container).setRelativeElementId(relativeElementId).setOverRelative(isOverRelative).setAsTooltip(true).setTooltipX(tooltipX).setTooltipY(tooltipY).setElementMap(elementMap).setOptions(options).setLevel(modalLevel);
        return this.loadInto(b.build());
    }
    
    public EventDispatcher loadInto(final XulorLoad loadData) {
        final long options = loadData.getOptions();
        if ((options & 0x1000L) != 0x1000L) {
            final EventDispatcher element = this._loadInto(loadData);
            final int duration = loadData.getDuration();
            if (duration != Integer.MAX_VALUE) {
                this.m_loadUnloadDocuments.add(new XulorUnload(loadData.getId(), duration, 0L));
            }
            return element;
        }
        synchronized (this.m_loadUnloadDocuments) {
            this.m_loadUnloadDocuments.add(loadData);
        }
        return null;
    }
    
    private EventDispatcher _loadInto(final XulorLoad loadData) {
        final DialogPathDefinition pathDefinition = loadData.getPathDefinition();
        if (pathDefinition == null) {
            return null;
        }
        final String id = loadData.getId();
        if (this.m_bigDialogs.contains(id)) {
            for (int i = this.m_bigDialogLoadListeners.size() - 1; i >= 0; --i) {
                this.m_bigDialogLoadListeners.get(i).dialogLoaded(id);
            }
        }
        final long options = loadData.getOptions();
        if ((options & 0x10L) == 0x10L) {
            this.unloadId(id);
        }
        boolean rootElementMap = false;
        final URL baseDirectory = loadData.getCurrentDirectory();
        EventDispatcher newElement = loadData.getNewElement();
        ElementMap elementMap = loadData.getElementMap();
        EventDispatcher container = loadData.getParent();
        if (newElement != null && elementMap == null) {
            elementMap = newElement.getElementMap();
            if (elementMap != null) {
                elementMap.setId(id);
            }
        }
        if (id != null && elementMap == null && !this.m_loadedElements.containsKey(id)) {
            elementMap = this.m_environment.createElementMap(id);
            rootElementMap = true;
        }
        if (elementMap != null) {
            try {
                final HashMap<String, String> environmentProperties = loadData.getEnvironmentProperties();
                if (environmentProperties != null) {
                    for (final Map.Entry<String, String> entry : environmentProperties.entrySet()) {
                        elementMap.putEnvironmentProperty(entry.getKey(), entry.getValue());
                    }
                }
                final ElementMap previousElementMap = this.m_environment.getCurrentElementMap();
                this.m_environment.setCurrentElementMap(elementMap);
                final URL oldDirectory = this.m_currentDirectory;
                this.m_currentDirectory = baseDirectory;
                if (newElement == null) {
                    if (pathDefinition == null) {
                        return null;
                    }
                    newElement = this.parse(pathDefinition, this.m_environment, elementMap);
                }
                if ((options & 0x40L) != 0x40L) {
                    if (container == null) {
                        final LayeredContainer lc = this.getScene().getMasterRootContainer().getLayeredContainer();
                        int layer;
                        if ((options & 0x100L) == 0x100L) {
                            layer = 26000;
                            if (this.m_mruManager != null) {
                                this.m_mruManager.closeCurrentMRU();
                            }
                        }
                        else if ((options & 0x40000L) == 0x40000L) {
                            layer = -10000;
                        }
                        else if ((options & 0x10000L) == 0x10000L) {
                            layer = -40000;
                        }
                        else if ((options & 0x4L) == 0x4L) {
                            layer = 27000;
                        }
                        else if ((options & 0x20000L) == 0x20000L) {
                            layer = 25999;
                        }
                        else if ((options & 0x2L) == 0x2L) {
                            layer = 26999;
                        }
                        else if ((options & 0x8L) == 0x8L) {
                            layer = 29999;
                        }
                        else {
                            layer = 0;
                        }
                        if (newElement instanceof Widget) {
                            lc.addWidgetToLayer((Widget)newElement, layer);
                            container = lc;
                        }
                        else {
                            lc.addFromXML(newElement);
                        }
                    }
                    else if (container != null) {
                        container.addFromXML(newElement);
                    }
                }
                newElement.setElementMapRoot(rootElementMap);
                if ((options & 0x100L) == 0x100L) {
                    ModalManager.getInstance().addModalElement(newElement);
                    this.m_loadedModals.add(id);
                }
                if ((options & 0x200L) == 0x200L) {
                    ModalManager.getInstance().addPseudoModalElement(newElement);
                    this.m_loadedModals.add(id);
                }
                this.m_currentDirectory = oldDirectory;
                this.m_environment.setCurrentElementMap(previousElementMap);
            }
            catch (Exception e) {
                Xulor.m_logger.error((Object)("Le chargement de " + pathDefinition.m_url + " a \u00e9chou\u00e9"), (Throwable)e);
            }
            this.insertEventDispatcher(newElement, id, container, loadData.getRelativeCascadeElementId(), loadData.getRelativePositionElementId(), loadData.getRelativeElementId(), loadData.getControlGroupId(), loadData.isOverRelative(), loadData.isAsTooltip(), loadData.getTooltipX(), loadData.getTooltipY(), elementMap, baseDirectory, options, loadData.getLevel());
        }
        else {
            Xulor.m_logger.error((Object)"On essaye de charger une dialog existant d\u00e9j\u00e0 sans avoir utilis\u00e9 l'option FORCE_RELOAD");
        }
        for (int j = this.m_dialogLoadListeners.size() - 1; j >= 0; --j) {
            final DialogLoadListener l = this.m_dialogLoadListeners.get(j);
            if (!this.m_dialogLoadListenersToRemove.contains(l)) {
                l.dialogLoaded(id);
            }
        }
        return newElement;
    }
    
    private void insertEventDispatcher(final EventDispatcher newElement, final String id, final EventDispatcher container, final String relativeCascadeElementId, final String relativePositionElementId, final String relativeElementId, final String controlGroupId, final boolean overRelative, final boolean asTooltip, final int tooltipX, final int tooltipY, final ElementMap elementMap, final URL baseDirectory, final long options, final short modalLevel) {
        if (newElement != null) {
            if (id != null) {
                this.m_loadedElements.put(id, newElement);
            }
            if ((options & 0x20L) != 0x20L) {
                this.m_needToRebuildUI = true;
            }
            WidgetUserDefinedManager userDefinedManager = null;
            if ((options & 0x400L) == 0x400L) {
                if (userDefinedManager == null) {
                    userDefinedManager = new WidgetUserDefinedManager((Widget)newElement);
                }
                userDefinedManager.setUsePosition(true);
                userDefinedManager.setUseSize(true);
            }
            if ((options & 0x8000L) == 0x8000L) {
                if (userDefinedManager == null) {
                    userDefinedManager = new WidgetUserDefinedManager((Widget)newElement);
                }
                userDefinedManager.setUsePosition(true);
            }
            if ((options & 0x4000L) == 0x4000L) {
                if (userDefinedManager == null) {
                    userDefinedManager = new WidgetUserDefinedManager((Widget)newElement);
                }
                userDefinedManager.setUseSize(true);
            }
            if (userDefinedManager != null) {
                newElement.setUserDefinedManager(userDefinedManager);
                newElement.loadPreferences();
                this.m_userDefinedAdapter.getPreferenceStoreForDialog(id).addPreferencePropertyChangedListener(newElement);
            }
            if (this.m_dialogClosesManager != null) {
                if ((options & 0x2000L) == 0x2000L) {
                    this.m_dialogClosesManager.addUnremovableDialog(id);
                }
                else {
                    this.m_dialogClosesManager.pushElementMap(id, false);
                }
            }
            if (relativePositionElementId != null) {
                final Widget widget = (Widget)newElement;
                if (!(widget.getLayoutData() instanceof StaticLayoutData)) {
                    widget.setLayoutData(new StaticLayoutData());
                }
                final StaticLayoutData staticLayoutData = (StaticLayoutData)widget.getLayoutData();
                staticLayoutData.setReferentWidget(this.m_loadedElements.get(relativePositionElementId));
                if (relativeCascadeElementId != null) {
                    staticLayoutData.setCascadeMethodEnabled(true);
                }
                staticLayoutData.setControlGroup(controlGroupId);
            }
            if (controlGroupId != null) {
                final Object parent = newElement.getParentOfType(RootContainer.class);
                if (parent != null) {
                    final RootContainer root = (RootContainer)parent;
                    final Widget widget2 = (Widget)newElement;
                    root.getWindowManager().addWidgetToControlGroup(widget2, controlGroupId);
                    if (relativeCascadeElementId != null) {
                        root.getWindowManager().addWidgetToCascadeGroup(widget2, controlGroupId);
                    }
                    final DialogUnloadListener dialogUnloadListener = new DialogUnloadListener() {
                        @Override
                        public void dialogUnloaded(final String id) {
                            if (widget2 != null && widget2.getElementMap() != null && id.equals(widget2.getElementMap().getId())) {
                                root.getWindowManager().removeWidgetFromControlGroup(widget2, controlGroupId);
                                if (relativeCascadeElementId != null) {
                                    root.getWindowManager().removeWidgetFromCascadeGroup(widget2, controlGroupId);
                                }
                                Xulor.this.removeDialogUnloadListener(this);
                            }
                        }
                    };
                    this.addDialogUnloadListener(dialogUnloadListener);
                }
            }
            if (asTooltip && newElement instanceof Widget) {
                if (tooltipX != Integer.MIN_VALUE) {
                    ((Widget)newElement).setX(tooltipX);
                }
                if (tooltipY != Integer.MIN_VALUE) {
                    ((Widget)newElement).setY(tooltipY);
                }
            }
        }
    }
    
    private void insert(final String id, final EventDispatcher e, final EventDispatcher parent, final int layer, final long options) {
        if (parent instanceof LayeredContainer) {
            ((LayeredContainer)parent).addWidgetToLayer((Widget)e, layer);
        }
        else {
            parent.addFromXML(e);
        }
        if ((options & 0x100L) == 0x100L) {
            ModalManager.getInstance().addModalElement(e);
            this.m_loadedModals.add(id);
        }
        if ((options & 0x200L) == 0x200L) {
            ModalManager.getInstance().addPseudoModalElement(e);
            this.m_loadedModals.add(id);
        }
        this.m_loadedElements.put(id, e);
    }
    
    public EventDispatcher parse(final URL url, final Environment env, final ElementMap map, final boolean generateClass, final URL parentDirectory, final String className, final String packageName) throws Exception {
        final XMLDocumentContainer doc = loadDoc(url);
        return this.m_parser.parse(doc, url, env, map, generateClass, parentDirectory, className, packageName);
    }
    
    public static XMLDocumentContainer loadDoc(final URL url) throws Exception {
        final XMLDocumentContainer doc = new XMLDocumentContainer();
        final BufferedInputStream stream = new BufferedInputStream(url.openStream());
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        accessor.open(stream);
        accessor.read(doc, new DocumentEntryParser[0]);
        accessor.close();
        stream.close();
        return doc;
    }
    
    public EventDispatcher parse(final DialogPathDefinition definition, final Environment env, final ElementMap map) throws Exception {
        if (definition.m_factory != null) {
            return this.parse(definition.m_factory, env, map);
        }
        return this.parse(definition.m_url, env, map, false, null, null, null);
    }
    
    public EventDispatcher parse(final Class<? extends BasicElementFactory> type, final Environment env, final ElementMap map) throws Exception {
        return (EventDispatcher)((BasicElementFactory)type.newInstance()).getElement(env, map);
    }
    
    public void unload(final String id) {
        this.unload(id, false);
    }
    
    public void unload(final String id, final boolean atOnce) {
        if (!this.m_loadedElements.containsKey(id)) {
            return;
        }
        if (atOnce) {
            this.unloadId(id);
        }
        else {
            this.m_loadUnloadDocuments.add(new XulorUnload(id));
        }
    }
    
    private void unloadId(final String id) {
        if (this.m_loadedElements.containsKey(id)) {
            final EventDispatcher element = this.m_loadedElements.get(id);
            if (element != null) {
                element.destroySelfFromParent();
            }
        }
    }
    
    public void cleanId(final String id) {
        if (this.m_loadedElements.containsKey(id)) {
            for (int i = this.m_dialogUnloadListeners.size() - 1; i >= 0; --i) {
                final DialogUnloadListener l = this.m_dialogUnloadListeners.get(i);
                if (!this.m_dialogUnloadListenersToRemove.contains(l)) {
                    l.dialogUnloaded(id);
                }
            }
            final EventDispatcher element = this.m_loadedElements.remove(id);
            if (element != null && this.m_loadedModals.contains(id)) {
                this.m_loadedModals.remove(id);
                ModalManager.getInstance().removeElement(element);
            }
            if (this.m_dialogClosesManager != null) {
                this.m_dialogClosesManager.onDialogUnload(id);
            }
        }
    }
    
    public void unloadAll() {
        this.unloadAllId();
    }
    
    private void unloadAllId() {
        if (!this.m_loadedElements.isEmpty()) {
            Xulor.m_logger.info((Object)"Unloading All !");
            this.m_loadedElements.forEachKey(new TObjectProcedure<String>() {
                @Override
                public boolean execute(final String id) {
                    if (Xulor.this.m_dialogClosesManager != null) {
                        Xulor.this.m_dialogClosesManager.onDialogUnload(id);
                    }
                    for (final DialogUnloadListener l : Xulor.this.m_dialogUnloadListeners) {
                        l.dialogUnloaded(id);
                    }
                    if (Xulor.this.m_dialogUnloadListenersToRemove.size() > 0) {
                        Xulor.this.m_dialogUnloadListeners.removeAll(Xulor.this.m_dialogUnloadListenersToRemove);
                        Xulor.this.m_dialogUnloadListenersToRemove.clear();
                    }
                    return true;
                }
            });
            for (int i = this.m_loadUnloadDocuments.size() - 1; i >= 0; --i) {
                final XulorLoadUnload doc = this.m_loadUnloadDocuments.get(i);
                if (doc instanceof XulorUnload) {
                    this.m_loadUnloadDocuments.remove(i);
                }
            }
        }
        ModalManager.getInstance().removeAllElements();
        this.m_loadedModals.clear();
        this.m_scene.getMasterRootContainer().reInitializeContent();
        this.m_loadedElements.clear();
    }
    
    public boolean isLoaded(final String id) {
        final EventDispatcher e = this.m_loadedElements.get(id);
        return e != null && !e.isUnloading();
    }
    
    public boolean changeLoadId(final String oldId, final String newId) {
        if (this.m_loadedElements.containsKey(newId)) {
            return false;
        }
        final EventDispatcher e = this.m_loadedElements.remove(oldId);
        this.m_loadedElements.put(newId, e);
        return true;
    }
    
    @Override
    public void onProcess(final UIScene scene, final int deltaTime) {
        this.m_elapsedTime += deltaTime;
        if (this.m_parser.needsToLoadTextures()) {
            this.m_parser.loadTextures();
        }
        this.m_propertiesProvider.onProcess();
        if (this.m_bigDialogLoadListenersToRemove.size() > 0) {
            this.m_bigDialogLoadListeners.removeAll(this.m_bigDialogLoadListenersToRemove);
            this.m_bigDialogLoadListenersToRemove.clear();
        }
        if (this.m_dialogLoadListenersToRemove.size() > 0) {
            this.m_dialogLoadListeners.removeAll(this.m_dialogLoadListenersToRemove);
            this.m_dialogLoadListenersToRemove.clear();
        }
        if (this.m_dialogUnloadListenersToRemove.size() > 0) {
            this.m_dialogUnloadListeners.removeAll(this.m_dialogUnloadListenersToRemove);
            this.m_dialogUnloadListenersToRemove.clear();
        }
        try {
            if (this.m_readyToLoadDocuments) {
                synchronized (this.m_loadUnloadDocuments) {
                    for (int size = this.m_timedUnloadDocuments.size(), i = 0; i < size; ++i) {
                        final XulorUnload unload = this.m_timedUnloadDocuments.get(i);
                        if (this.m_elapsedTime - unload.getDuration() - unload.getStartTime() > 0L) {
                            unload.setReady();
                            this.m_loadUnloadDocuments.add(this.m_timedUnloadDocuments.remove(i));
                            --i;
                            --size;
                        }
                    }
                    while (!this.m_loadUnloadDocuments.isEmpty()) {
                        final XulorLoadUnload loadUnload = this.m_loadUnloadDocuments.remove(0);
                        if (loadUnload instanceof XulorUnload) {
                            final XulorUnload unload = (XulorUnload)loadUnload;
                            if (!unload.isReady()) {
                                unload.setStartTime(this.m_elapsedTime);
                                this.m_timedUnloadDocuments.add(unload);
                            }
                            else if (unload.isAll()) {
                                this.unloadAllId();
                            }
                            else {
                                this.unloadId(unload.getId());
                            }
                        }
                        else if (loadUnload instanceof XulorLoad) {
                            final XulorLoad doc = (XulorLoad)loadUnload;
                            if ((doc.getOptions() & 0x800L) == 0x800L && this.isLoaded(doc.getId())) {
                                continue;
                            }
                            if (doc.getDuration() != Integer.MAX_VALUE) {
                                this.m_timedUnloadDocuments.add(new XulorUnload(doc.getId(), doc.getDuration(), this.m_elapsedTime));
                            }
                            this._loadInto(doc);
                        }
                        else if (loadUnload instanceof XulorInsert) {
                            final XulorInsert doc2 = (XulorInsert)loadUnload;
                            this.insert(doc2.m_id, doc2.m_element, doc2.m_parent, doc2.m_layer, doc2.m_options);
                        }
                        else if (loadUnload instanceof XulorSetWatcherTarget) {
                            final XulorSetWatcherTarget xswt = (XulorSetWatcherTarget)loadUnload;
                            final EventDispatcher element = this.m_loadedElements.get(xswt.getContainerId());
                            if (element instanceof WatcherContainer) {
                                ((WatcherContainer)element).setTarget(xswt.getTarget());
                            }
                            else {
                                Xulor.m_logger.error((Object)("Tentative de SetWatcherTarget avec une id invalide " + xswt.getContainerId()));
                            }
                        }
                        else if (loadUnload instanceof XulorLoadMouseImage) {
                            final XulorLoadMouseImage xlmi = (XulorLoadMouseImage)loadUnload;
                            GraphicalMouseManager.getInstance().setWidget(xlmi.WIDGET);
                            GraphicalMouseManager.getInstance().setXOffset(xlmi.XOFFSET);
                            GraphicalMouseManager.getInstance().setYOffset(xlmi.YOFFSET);
                            GraphicalMouseManager.getInstance().setHotPoint(xlmi.HOTPOINT);
                            GraphicalMouseManager.getInstance().show();
                        }
                        else {
                            if (!(loadUnload instanceof XulorUnloadMouseImage)) {
                                continue;
                            }
                            GraphicalMouseManager.getInstance().hide();
                        }
                    }
                }
            }
        }
        catch (Throwable t) {
            Xulor.m_logger.error((Object)"Exception dans Xulor.onProcess() : ", t);
        }
    }
    
    @Override
    public void onResize(final UIScene scene, final int deltaWidth, final int deltaHeight) {
        GlobalUserDefinedManager.getInstance().onResize(this.m_scene, deltaWidth, deltaHeight);
    }
    
    @Override
    public void onSceneInitializationComplete(final UIScene scene) {
        this.m_scene = (XulorScene)scene;
    }
    
    public void registerMessageBoxControler(final MessageBoxControler controler) {
        this.m_messageBoxControlers.push(controler);
    }
    
    public void unregisterMessageBoxControler(final MessageBoxControler controler) {
        this.m_messageBoxControlers.remove(controler);
    }
    
    public boolean unloadFirstMessageBox() {
        if (this.m_messageBoxControlers.empty()) {
            return false;
        }
        final MessageBoxControler controller = this.m_messageBoxControlers.pop();
        controller.messageBoxClosed(32768, null);
        return true;
    }
    
    static {
        Xulor.m_logger = Logger.getLogger((Class)Xulor.class);
        Xulor.m_instance = new Xulor();
    }
    
    public static class DialogPathDefinition
    {
        public final Class<? extends BasicElementFactory> m_factory;
        public final URL m_url;
        public BasicElementFactory m_factoryInstance;
        
        private DialogPathDefinition(final Class<? extends BasicElementFactory> factory) {
            super();
            this.m_factory = factory;
            try {
                this.m_factoryInstance = (BasicElementFactory)this.m_factory.newInstance();
            }
            catch (InstantiationException e) {
                Xulor.m_logger.error((Object)"", (Throwable)e);
            }
            catch (IllegalAccessException e2) {
                Xulor.m_logger.error((Object)"", (Throwable)e2);
            }
            this.m_url = null;
        }
        
        private DialogPathDefinition(final URL url) {
            super();
            this.m_factory = null;
            this.m_url = url;
        }
        
        public static DialogPathDefinition createDialogPathDefinition(final String path) {
            try {
                final Class<? extends BasicElementFactory> factory = (Class<? extends BasicElementFactory>)Class.forName(path);
                return new DialogPathDefinition(factory);
            }
            catch (ClassNotFoundException e) {
                try {
                    final URL url = ContentFileHelper.getURL(path);
                    return new DialogPathDefinition(url);
                }
                catch (MalformedURLException e2) {
                    Xulor.m_logger.error((Object)("Le chemin '" + path + "' vers le fichier de d\u00e9finition des dialog est invalide !"));
                    return null;
                }
            }
        }
    }
}
