package com.ankamagames.wakfu.client.ui.protocol.frame.helpers;

import org.apache.log4j.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.core.renderer.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.chat.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.wakfu.client.chat.*;

public class UIChatFrameHelper
{
    private static final Logger m_logger;
    
    public static EventDispatcher loadChatWindow(final ChatViewManager chatViewManager, final String lastChatDialogid) {
        final String dialogId = getDialogIdFromWindowId(chatViewManager.getWindowId());
        if (Xulor.getInstance().isLoaded(dialogId)) {
            Xulor.getInstance().unload(dialogId);
        }
        EventDispatcher ed;
        if (lastChatDialogid == null) {
            ed = Xulor.getInstance().load(dialogId, Dialogs.getDialogPath("chatDialog"), 9217L, (short)10000);
        }
        else {
            ed = Xulor.getInstance().loadAsMultiple(dialogId, Dialogs.getDialogPath("chatDialog"), lastChatDialogid, lastChatDialogid, "chatDialog", 9217L, (short)10000);
        }
        PropertiesProvider.getInstance().setLocalPropertyValue("chat", chatViewManager, dialogId);
        PropertiesProvider.getInstance().setLocalPropertyValue("chat.channels.list.displayed", false, dialogId);
        PropertiesProvider.getInstance().setLocalPropertyValue("chat.scrollOffset", -1.0f, dialogId);
        return ed;
    }
    
    public static String getDialogIdFromWindowId(final int id) {
        return "chatDialog" + id;
    }
    
    public static void fadeChatWindow(final int id, final ChatWindow chatWindow, final boolean fadeIn) {
        final Window window = chatWindow.getWindow();
        if (!fadeIn && window != null) {
            PropertiesProvider.getInstance().setLocalPropertyValue("chat.channels.list.displayed", false, window.getElementMap());
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(getDialogIdFromWindowId(id));
        if (map == null) {
            return;
        }
        final Widget textEditor = (Widget)map.getElement("textEditorRenderableContainer.chatInput");
        final Widget focused = FocusManager.getInstance().getFocused();
        if (focused != null && (focused == textEditor || "viewNameEditor".equals(focused.getId())) && !fadeIn) {
            return;
        }
        final ArrayList<ModulationColorClient> modulationColorWidgets = new ArrayList<ModulationColorClient>();
        final ArrayList<ModulationColorClient> modulationColorWidgetsQuarterAlpha = new ArrayList<ModulationColorClient>();
        final ArrayList<ModulationColorClient> modulationColorWidgetsHalfAlpha = new ArrayList<ModulationColorClient>();
        final ArrayList<ModulationColorClient> modulationColorWidgetsGrey = new ArrayList<ModulationColorClient>();
        addAppearanceToModulationColorWidgets(map, "chatMainContainer", modulationColorWidgetsQuarterAlpha);
        final ChatViewManager viewManager = ChatWindowManager.getInstance().getWindow(id);
        if (viewManager == null || viewManager.getCurrentView() == null) {
            UIChatFrameHelper.m_logger.error((Object)("Erreur au fade de la fenetre de chat d'id=" + id));
            return;
        }
        if (!viewManager.getCurrentView().isPrivateView()) {
            final List list = (List)map.getElement("filterList");
            fadeFilters(list, fadeIn);
        }
        if (textEditor == null) {
            UIChatFrameHelper.m_logger.warn((Object)"le textEditor n'existe pas");
        }
        else {
            modulationColorWidgetsHalfAlpha.add(textEditor.getAppearance());
        }
        addAppearanceToModulationColorWidgets(map, "alphaCorner", modulationColorWidgetsQuarterAlpha);
        addAppearanceToModulationColorWidgets(map, "addButton", modulationColorWidgets);
        addAppearanceToModulationColorWidgets(map, "addLabel", modulationColorWidgets);
        final List list = (List)map.getElement("viewsList");
        for (final RenderableContainer renderableContainer : list.getRenderables()) {
            final ElementMap innerElementMap = renderableContainer.getInnerElementMap();
            if (innerElementMap == null) {
                continue;
            }
            final Object value = renderableContainer.getItemValue();
            if (value != null && value.equals(viewManager.getCurrentView())) {
                addAppearanceToModulationColorWidgets(innerElementMap, "viewLabel", modulationColorWidgetsGrey);
            }
            else {
                addAppearanceToModulationColorWidgets(innerElementMap, "viewLabel", modulationColorWidgetsHalfAlpha);
            }
            addAppearanceToModulationColorWidgets(innerElementMap, "viewButton", modulationColorWidgetsQuarterAlpha);
            addAppearanceToModulationColorWidgets(innerElementMap, "viewDeleteButton", modulationColorWidgetsQuarterAlpha);
        }
        fadeElements(fadeIn, window, modulationColorWidgets, modulationColorWidgetsQuarterAlpha, modulationColorWidgetsHalfAlpha, modulationColorWidgetsGrey);
        setWidgetsNonBlocking(map, !fadeIn);
    }
    
    private static void fadeFilters(final EditableRenderableCollection list, final boolean fadeIn) {
        final ArrayList<RenderableContainer> renderables = list.getRenderables();
        for (int i = 0, size = renderables.size(); i < size; ++i) {
            final RenderableContainer renderableContainer = renderables.get(i);
            final ElementMap innerElementMap = renderableContainer.getInnerElementMap();
            if (innerElementMap != null) {
                final ChatFilterFieldProvider filter = (ChatFilterFieldProvider)renderableContainer.getItemValue();
                final Widget bg = (Widget)innerElementMap.getElement("filterBg");
                final Widget button = (Widget)innerElementMap.getElement("filterBtn");
                bg.removeTweensOfType(ModulationColorTween.class);
                button.removeTweensOfType(ModulationColorTween.class);
                if (fadeIn) {
                    bg.addTween(new ModulationColorTween(Color.WHITE_ALPHA, Color.WHITE, bg.getAppearance(), 0, 1000, 1, false, TweenFunction.PROGRESSIVE));
                    button.addTween(new ModulationColorTween(Color.WHITE_ALPHA, filter.getColor(), button.getAppearance(), 0, 1000, 1, false, TweenFunction.PROGRESSIVE));
                }
                else {
                    bg.addTween(new ModulationColorTween(Color.WHITE, Color.WHITE_ALPHA, bg.getAppearance(), 0, 1000, 1, false, TweenFunction.PROGRESSIVE));
                    button.addTween(new ModulationColorTween(filter.getColor(), Color.WHITE_ALPHA, button.getAppearance(), 0, 1000, 1, false, TweenFunction.PROGRESSIVE));
                }
            }
        }
    }
    
    public static void highlightDownBundaryButton(final Window chatWindow) {
        final Button button = (Button)chatWindow.getElementMap().getElement("downBundaryButton");
        final Color c = Color.WHITE_QUARTER_ALPHA;
        final Color c2 = new Color(Color.WHITE.get());
        final ButtonAppearance buttonAppearance = button.getAppearance();
        final AbstractTween t = new ModulationColorTween(c, c2, buttonAppearance, 0, 500, 3, TweenFunction.PROGRESSIVE);
        buttonAppearance.addTween(t);
    }
    
    public static void closeExchangeChatWindow() {
        final int exchangeWindowId = ChatWindowManager.getInstance().getExchangeWindowId();
        if (exchangeWindowId == -1) {
            UIChatFrameHelper.m_logger.error((Object)"on tente de fermer la vue de chat d'\u00e9change alors qu'elle n'est pas initialis\u00e9e");
            return;
        }
        ChatWindowManager.getInstance().removeChatWindow(exchangeWindowId, -1);
        ChatWindowManager.getInstance().setExchangeWindowId(-1);
    }
    
    public static void closeModeratorChatWindow() {
        final int windowId = ChatWindowManager.getInstance().getModeratorWindowId();
        if (windowId == -1) {
            UIChatFrameHelper.m_logger.error((Object)"on tente de fermer la vue de chat de moderation alors qu'elle n'est pas initialis\u00e9e");
            return;
        }
        ChatWindowManager.getInstance().removeChatWindow(windowId, -1);
        ChatWindowManager.getInstance().setModeratorWindowId(-1);
    }
    
    public static void fadeElements(final boolean fadeIn, final Window window, final ArrayList<ModulationColorClient> modulationColorWidgets, final ArrayList<ModulationColorClient> modulationColorWidgetsAlpha, final ArrayList<ModulationColorClient> modulationColorWidgetsHalfAlpha, final ArrayList<ModulationColorClient> modulationColorWidgetsGrey) {
        window.removeTweensOfType(ModulationColorListTween.class);
        fadeElement(modulationColorWidgets, fadeIn, Color.WHITE_ALPHA, window);
        fadeElement(modulationColorWidgetsAlpha, fadeIn, Color.WHITE_QUARTER_ALPHA, window);
        fadeElement(modulationColorWidgetsHalfAlpha, fadeIn, Color.WHITE_SEMI_ALPHA, window);
        fadeElement(modulationColorWidgetsGrey, fadeIn, Color.LIGHT_GRAY, window);
    }
    
    private static void fadeElement(final ArrayList<ModulationColorClient> appL, final boolean fadeIn, final Color alphaColor, final Window w) {
        if (appL.isEmpty()) {
            return;
        }
        final ModulationColorClient app = appL.get(0);
        int aValue;
        int bValue;
        if (fadeIn) {
            if (app.getModulationColor() == null) {
                return;
            }
            aValue = app.getModulationColor().get();
            bValue = Color.WHITE.get();
        }
        else {
            aValue = Color.WHITE.get();
            bValue = alphaColor.get();
        }
        if (aValue != bValue) {
            if (fadeIn) {
                WakfuSoundManager.getInstance().windowFadeIn();
            }
            else {
                WakfuSoundManager.getInstance().windowFadeOut();
            }
            final Color a = new Color(aValue);
            final Color b = new Color(bValue);
            w.addTween(new ModulationColorListTween(a, b, appL, 0, 1000, 1, TweenFunction.PROGRESSIVE));
        }
    }
    
    public static void setWidgetsNonBlocking(final ElementMap map, final boolean nonBlocking) {
        setWidgetElementNonBlocking(map, "addButton", nonBlocking);
        setWidgetElementNonBlocking(map, "resizePoint0", nonBlocking);
        setWidgetElementNonBlocking(map, "resizePoint1", nonBlocking);
        setWidgetElementNonBlocking(map, "resizePoint2", nonBlocking);
        setWidgetElementNonBlocking(map, "resizePoint3", nonBlocking);
        setWidgetElementNonBlocking(map, "resizePoint4", nonBlocking);
        setWidgetElementNonBlocking(map, "resizePoint5", nonBlocking);
        setWidgetElementNonBlocking(map, "resizePoint6", nonBlocking);
        setWidgetElementNonBlocking(map, "resizePoint7", nonBlocking);
    }
    
    private static void setWidgetElementNonBlocking(final ElementMap map, final String elementName, final boolean nonBlocking) {
        ((Widget)map.getElement(elementName)).setNonBlocking(nonBlocking);
    }
    
    private static void addAppearanceToModulationColorWidgets(final ElementMap map, final String elementName, final ArrayList<ModulationColorClient> modulationColorWidgets) {
        final EventDispatcher widget = map.getElement(elementName);
        if (widget == null) {
            UIChatFrameHelper.m_logger.warn((Object)("la propri\u00e9t\u00e9 " + elementName + " n'existe pas."));
            return;
        }
        assert widget instanceof Widget;
        final DecoratorAppearance app = ((Widget)widget).getAppearance();
        app.removeTweensOfType(ModulationColorTween.class);
        modulationColorWidgets.add(app);
    }
    
    public static void blinkViewButton(final ChatView chatView) {
        final ChatViewManager chatWindow = ChatWindowManager.getInstance().getWindowFromView(chatView);
        if (chatWindow == null || chatWindow.getCurrentView() == chatView) {
            return;
        }
        final ElementMap map = Xulor.getInstance().getEnvironment().getElementMap(getDialogIdFromWindowId(chatWindow.getWindowId()));
        if (map == null) {
            return;
        }
        final List list = (List)map.getElement("viewsList");
        final RenderableContainer renderable = list.getRenderableByOffset(chatView.getViewIndex());
        if (renderable == null) {
            return;
        }
        final Widget button = (Button)renderable.getInnerElementMap().getElement("viewButton");
        final Color c = new Color(Color.WHITE_QUARTER_ALPHA.get());
        final Color c2 = new Color(Color.WHITE.get());
        final DecoratorAppearance decoratorAppearance = button.getAppearance();
        decoratorAppearance.removeTweensOfType(ModulationColorTween.class);
        final AbstractTween t3 = new ModulationColorTween(c, c2, decoratorAppearance, 0, 500, 10, true, TweenFunction.PROGRESSIVE);
        decoratorAppearance.addTween(t3);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIChatFrameHelper.class);
    }
}
