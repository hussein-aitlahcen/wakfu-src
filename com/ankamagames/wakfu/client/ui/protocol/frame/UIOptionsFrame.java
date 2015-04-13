package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.wakfu.client.ui.providers.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.game.region.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.message.options.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.console.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.kernel.core.controllers.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.io.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.embeddedTutorial.*;
import gnu.trove.*;
import java.net.*;
import java.awt.event.*;

public class UIOptionsFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static UIOptionsFrame m_instance;
    private static final int FIRST_TUTORIAL_ID = 366366;
    private DialogUnloadListener m_dialogUnloadListener;
    private DialogCloseRequestListener m_dialogCloseListener;
    private ShorctutKeyBoardControler m_keyBoardControler;
    private TIntObjectHashMap<Shortcut> m_modifiedShortcuts;
    private GLApplicationUIOptionsFieldProvider m_uiFieldProvider;
    
    public UIOptionsFrame() {
        super();
        this.m_modifiedShortcuts = new TIntObjectHashMap<Shortcut>();
    }
    
    public static UIOptionsFrame getInstance() {
        return UIOptionsFrame.m_instance;
    }
    
    public GLApplicationUIOptionsFieldProvider getUIFieldProvider() {
        return this.m_uiFieldProvider;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17740: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final ObjectPair<RegionsView, RegionView> pair = msg.getObjectValue();
                final RegionsView regions = pair.getFirst();
                final RegionView selectedRegion = pair.getSecond();
                regions.setCurrentRegion(selectedRegion);
                final String msgText = WakfuTranslator.getInstance().getString("region.info.changedRegionWarning");
                final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 2L);
                Xulor.getInstance().msgBox(data);
                return false;
            }
            case 16403: {
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            case 17738: {
                final UIBindShortcutMessage uiBindShortcutMessage = (UIBindShortcutMessage)message;
                if (ShortcutsFieldProvider.getInstance().getCurrentBind() != null) {
                    return false;
                }
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.confirmBindedTest", uiBindShortcutMessage.getShortcut().getShortcut().getParamString()), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final Shortcut shortcut = uiBindShortcutMessage.getShortcut().getShortcut();
                            try {
                                ShortcutManager.getInstance().createShortcut(shortcut, "binding", getShortcutsFilePath());
                            }
                            catch (Exception e) {
                                UIOptionsFrame.m_logger.error((Object)"Exception", (Throwable)e);
                            }
                            ShortcutsFieldProvider.getInstance().reflow();
                            final ShortcutFieldProvider shortcutFieldProvider = ShortcutsFieldProvider.getInstance().getShortcutFieldProviderFromKeyCode(MathHelper.getLongFromTwoInt(shortcut.getKeyCode(), shortcut.getModiferMask()));
                            if (shortcutFieldProvider != null) {
                                OptionsDialogActions.bindKey(shortcutFieldProvider, 17731);
                            }
                        }
                    }
                });
                return false;
            }
            case 17739: {
                final ShortcutFieldProvider shortcutFieldProvider = ShortcutsFieldProvider.getInstance().getCurrentBind();
                if (shortcutFieldProvider == null) {
                    return false;
                }
                try {
                    ShortcutManager.getInstance().deleteShortcut(shortcutFieldProvider.getShortcut(), getShortcutsFilePath());
                }
                catch (Exception e) {
                    UIOptionsFrame.m_logger.error((Object)"Exception", (Throwable)e);
                }
                ShortcutsFieldProvider.getInstance().reflow();
                return false;
            }
            case 17731: {
                final UIBindShortcutMessage uiBindShortcutMessage = (UIBindShortcutMessage)message;
                final StringBuilder sb = new StringBuilder();
                sb.append("<u>").append(uiBindShortcutMessage.getShortcut().getEffectString()).append("</u>\n");
                sb.append(WakfuTranslator.getInstance().getString("options.enterKeysCombining"));
                PropertiesProvider.getInstance().setPropertyValue("waitingForKeyTypedDescription", sb.toString());
                PropertiesProvider.getInstance().setPropertyValue("waitingForKeyTyped", true);
                this.m_keyBoardControler = new ShorctutKeyBoardControler(uiBindShortcutMessage.getShortcut());
                WakfuClientInstance.getInstance().getRenderer().pushKeyboardController(this.m_keyBoardControler, true);
                return false;
            }
            case 17732: {
                final UIBindShortcutMessage uiBindShortcutMessage = (UIBindShortcutMessage)message;
                final Shortcut shortcut = uiBindShortcutMessage.getShortcut().getShortcut();
                this.addModifiedShortcut(shortcut);
                this.changeBindText(shortcut, uiBindShortcutMessage.getStringValue());
                ShortcutsFieldProvider.getInstance().reflow();
                return false;
            }
            case 17733: {
                final MessageBoxControler messageBoxControler2 = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.restoreOptions"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 2073L, 102, 1);
                messageBoxControler2.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            UIOptionsFrame.this.restoreOptions();
                        }
                    }
                });
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public void restoreOptions() {
        WakfuClientInstance.getInstance().getGamePreferences().resetToDefault();
        final ShortcutManager shortcutManager = ShortcutManager.getInstance();
        try {
            final String defaultShortcutsFile = WakfuConfiguration.getContentPath("defaultShortcutsFile");
            shortcutManager.loadFromXMLFile(defaultShortcutsFile, false);
            final String url = getShortcutsFilePath();
            FileHelper.deleteFile(url);
        }
        catch (Exception e) {
            UIOptionsFrame.m_logger.error((Object)"impossible de recharger le fichier de raccourcis par d\u00e9faut");
        }
        ShortcutsFieldProvider.getInstance().initialize();
        PropertiesProvider.getInstance().firePropertyValueChanged(ShortcutsFieldProvider.getInstance(), ShortcutsFieldProvider.getInstance().getFields());
        PropertiesProvider.getInstance().firePropertyValueChanged(WakfuClientInstance.getInstance().getGamePreferences(), WakfuClientInstance.getInstance().getGamePreferences().getFields());
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private static void initRegions() {
        PropertiesProvider.getInstance().setPropertyValue("regions", new RegionsView());
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogCloseListener = new DialogCloseRequestListener() {
                @Override
                public int onDialogCloseRequest(final String id) {
                    OptionsDialogActions.cancel(null);
                    return 2;
                }
            };
            DialogClosesManager.getInstance().addDialogCloseRequestListener(this.m_dialogCloseListener);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("optionsDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIOptionsFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            this.m_uiFieldProvider = new GLApplicationUIOptionsFieldProvider(WakfuClientInstance.getInstance().getAppUI());
            PropertiesProvider.getInstance().setPropertyValue("resolutionsManager", this.m_uiFieldProvider);
            PropertiesProvider.getInstance().setPropertyValue("waitingForKeyTyped", false);
            PropertiesProvider.getInstance().setPropertyValue("shortcutOver", null);
            PropertiesProvider.getInstance().setPropertyValue("options.numLOD", 3);
            PropertiesProvider.getInstance().setPropertyValue("options.numFightLOD", 3);
            PropertiesProvider.getInstance().setPropertyValue("options.graphicalPresets", WakfuGraphicalPresets.Level.values());
            initRegions();
            Xulor.getInstance().load("optionsDialog", Dialogs.getDialogPath("optionsDialog"), 256L, (short)26000);
            Xulor.getInstance().putActionClass("wakfu.options", OptionsDialogActions.class);
            EmbeddedTutorialManager.getInstance().launchTutorial(TutorialEvent.OPTIONS_GAME, "optionsDialog");
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_modifiedShortcuts.clear();
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            DialogClosesManager.getInstance().removeDialogCloseRequestListener(this.m_dialogCloseListener);
            PropertiesProvider.getInstance().removeProperty("waitingForKeyTyped");
            PropertiesProvider.getInstance().removeProperty("shortcutOver");
            PropertiesProvider.getInstance().removeProperty("options.numLOD");
            PropertiesProvider.getInstance().removeProperty("resolutionsManager");
            this.m_uiFieldProvider = null;
            WakfuClientInstance.getInstance().getRenderer().removeKeyboardController(this.m_keyBoardControler);
            Xulor.getInstance().unload("optionsDialog");
            Xulor.getInstance().removeActionClass("wakfu.options");
            OptionsDialogActions.setSelectedTabIndex(0);
        }
    }
    
    public TIntObjectIterator<Shortcut> getModifiedShortcuts() {
        return this.m_modifiedShortcuts.iterator();
    }
    
    private void addModifiedShortcut(final Shortcut shortcut) {
        final String id = shortcut.getId();
        if (id == null) {
            UIOptionsFrame.m_logger.error((Object)"On tente de modifier un raccourci sans id !");
            return;
        }
        final Shortcut copy = shortcut.getCopy();
        final int hash = id.hashCode();
        if (!this.m_modifiedShortcuts.containsKey(hash)) {
            this.m_modifiedShortcuts.put(hash, copy);
        }
    }
    
    public void changeBindText(final Shortcut shortcut, final String text) {
        try {
            ShortcutManager.getInstance().changeShortcutParams(shortcut, text, WakfuConfiguration.getContentPath("shortcutsFile").substring(5));
        }
        catch (Exception e) {
            UIOptionsFrame.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    private static String getShortcutsFilePath() throws Exception {
        return new URL(WakfuConfiguration.getContentPath("shortcutsFile")).getFile();
    }
    
    public void changeShortcut(final String shortcutId, final int keyCode, final int modifiersEx) {
        try {
            ShortcutManager.getInstance().changeShortcut(shortcutId, keyCode, null, modifiersEx, getShortcutsFilePath());
        }
        catch (Exception e) {
            UIOptionsFrame.m_logger.error((Object)"Exception", (Throwable)e);
        }
    }
    
    private void stopWaiting() {
        WakfuClientInstance.getInstance().getRenderer().removeKeyboardController(this.m_keyBoardControler);
        PropertiesProvider.getInstance().setPropertyValue("waitingForKeyTyped", false);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIOptionsFrame.class);
        UIOptionsFrame.m_instance = new UIOptionsFrame();
    }
    
    private class ShorctutKeyBoardControler implements KeyboardController
    {
        private ShortcutFieldProvider m_shortcut;
        private boolean keyReceived;
        
        public ShorctutKeyBoardControler(final ShortcutFieldProvider shortcut) {
            super();
            this.keyReceived = false;
            this.m_shortcut = shortcut;
        }
        
        @Override
        public boolean keyTyped(final KeyEvent keyEvent) {
            return false;
        }
        
        @Override
        public boolean keyPressed(final KeyEvent keyEvent) {
            final int keyCode = keyEvent.getKeyCode();
            if (this.keyReceived) {
                return true;
            }
            if ((this.m_shortcut.getKeyCodeStart() == keyCode && this.m_shortcut.getModifiersEx() == keyEvent.getModifiersEx()) || keyEvent.getKeyCode() == 27) {
                UIOptionsFrame.this.stopWaiting();
                return true;
            }
            if (keyCode != 18 && keyCode != 16 && keyCode != 17) {
                this.keyReceived = true;
                final long keyCombinationCode = MathHelper.getLongFromTwoInt(keyCode, keyEvent.getModifiersEx());
                if (ShortcutsFieldProvider.getInstance().isReservedKey(keyCombinationCode)) {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("notification.reservedKey"), WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 3L, 102, 1);
                    UIOptionsFrame.this.stopWaiting();
                    return true;
                }
                final ShortcutFieldProvider shorctutFieldProvider = ShortcutsFieldProvider.getInstance().getShortcutFieldProviderFromKeyCode(keyCombinationCode);
                final Shortcut shortcutFromKeyCode = (shorctutFieldProvider == null) ? null : shorctutFieldProvider.getShortcut();
                if (shortcutFromKeyCode != null) {
                    final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("question.swapShortcutsKey"), WakfuMessageBoxConstants.getMessageBoxIconUrl(7), 2073L, 102, 1);
                    messageBoxControler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            if (type == 8) {
                                UIOptionsFrame.this.addModifiedShortcut(shortcutFromKeyCode);
                                UIOptionsFrame.this.addModifiedShortcut(ShorctutKeyBoardControler.this.m_shortcut.getShortcut());
                                UIOptionsFrame.this.changeShortcut(shortcutFromKeyCode.getId(), ShorctutKeyBoardControler.this.m_shortcut.getKeyCodeStart(), ShorctutKeyBoardControler.this.m_shortcut.getModifiersEx());
                                UIOptionsFrame.this.changeShortcut(ShorctutKeyBoardControler.this.m_shortcut.getId(), keyEvent.getKeyCode(), keyEvent.getModifiersEx());
                                ShortcutsFieldProvider.getInstance().reflow();
                                UIOptionsFrame.this.stopWaiting();
                            }
                            else {
                                ShorctutKeyBoardControler.this.keyReceived = false;
                            }
                        }
                    });
                }
                else {
                    UIOptionsFrame.this.addModifiedShortcut(this.m_shortcut.getShortcut());
                    UIOptionsFrame.this.changeShortcut(this.m_shortcut.getId(), keyEvent.getKeyCode(), keyEvent.getModifiersEx());
                    ShortcutsFieldProvider.getInstance().reflow();
                    UIOptionsFrame.this.stopWaiting();
                }
            }
            return true;
        }
        
        @Override
        public boolean keyReleased(final KeyEvent keyEvent) {
            return false;
        }
    }
}
