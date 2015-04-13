package com.ankamagames.xulor2.core.messagebox;

import org.apache.log4j.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.xulor2.appearance.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;

public class MessageBoxFormater
{
    protected static final Logger m_logger;
    private static final String MSG_BOX_TITLE_ID = "messageBoxTitle";
    private static final String MSG_BOX_IMAGE_ID = "messageBoxImage";
    private static final String MSG_BOX_TEXTVIEW_ID = "messageBoxTextView";
    public static final String MSG_BOX_TEXTEDITOR_ID = "messageBoxTextEditor";
    private static final String MSG_BOX_BUTTONS_CONTAINER_ID = "messageBoxButtonsContainer";
    private static final String MSG_BOX_BUTTON_ID = "messageBoxButton";
    
    public static void format(final Window messageBox, final MessageBoxControler controler, final MessageBoxData data) throws Exception {
        final String iconUrl = data.getIconUrl();
        final long options = data.getOptions();
        final ArrayList<String> customMessages = data.getCustomMessages();
        final ElementMap map = messageBox.getElementMap();
        if (map.containsElement("messageBoxTitle")) {
            final Label label = (Label)map.getElement("messageBoxTitle");
            label.setText(data.getTitle());
        }
        if (!map.containsElement("messageBoxImage")) {
            throw new Exception("Aucun Label n'est r\u00e9f\u00e9renc\u00e9 sous l'id : messageBoxImage");
        }
        final Image image = (Image)map.getElement("messageBoxImage");
        final DocumentParser themeParser = Xulor.getInstance().getDocumentParser();
        if (themeParser != null) {
            Texture texture = null;
            if (iconUrl != null && !iconUrl.isEmpty()) {
                texture = TextureManager.getInstance().createTexturePowerOfTwo(RendererType.OpenGL.getRenderer(), Engine.getTextureName(iconUrl), iconUrl, false);
            }
            else if ((options & 0x200L) == 0x200L) {
                texture = themeParser.getTexture("messageBoxInfoIcon");
            }
            else if ((options & 0x400L) == 0x400L) {
                texture = themeParser.getTexture("messageBoxErrorIcon");
            }
            else if ((options & 0x800L) == 0x800L) {
                texture = themeParser.getTexture("messageBoxQuestionIcon");
            }
            else if ((options & 0x1000L) == 0x1000L) {
                texture = themeParser.getTexture("messageBoxCautionIcon");
            }
            if (texture != null) {
                final PixmapElement pixmapElement = new PixmapElement();
                pixmapElement.onCheckOut();
                pixmapElement.setPixmap(new Pixmap(texture));
                image.add(pixmapElement);
            }
        }
        if (!map.containsElement("messageBoxTextView")) {
            throw new Exception("Aucun textView n'est r\u00e9f\u00e9renc\u00e9 sous l'id : messageBoxTextView");
        }
        final TextView textView = setMessage(messageBox, data);
        if ((options & 0x2000L) == 0x2000L) {
            textView.setSelectable(true);
            textView.setEnableShrinking(false);
        }
        if (!map.containsElement("messageBoxTextEditor")) {
            throw new Exception("Aucun textView n'est r\u00e9f\u00e9renc\u00e9 sous l'id : messageBoxTextView");
        }
        final TextEditor textEditor = (TextEditor)map.getElement("messageBoxTextEditor");
        final boolean enableTextEditor = (options & 0x10000L) == 0x10000L;
        textEditor.setVisible(enableTextEditor);
        final TextEditorParameters textEditorParameters = data.getTextEditorParameters();
        if (textEditorParameters != null) {
            textEditor.setPrefSize(textEditorParameters.getPrefSize());
            textEditor.setMaxWidth(textEditorParameters.getMaxWidth());
            textEditor.setMultiline(textEditorParameters.isMultiline());
            textEditor.setMaxCharacters(textEditorParameters.getMaxCharacters());
        }
        if (!map.containsElement("messageBoxButtonsContainer")) {
            throw new Exception("Aucun container n'est r\u00e9f\u00e9renc\u00e9 sous l'id : messageBoxButtonsContainer");
        }
        if (!map.containsElement("messageBoxButton")) {
            throw new Exception("Aucun button n'est r\u00e9f\u00e9renc\u00e9 sous l'id : messageBoxButton");
        }
        final Container buttonsContainer = (Container)map.getElement("messageBoxButtonsContainer");
        final Button button = (Button)map.getElement("messageBoxButton");
        buttonsContainer.removeWidget(button);
        addButton(8, options, customMessages, button, controler, buttonsContainer, textEditor);
        addButton(16, options, customMessages, button, controler, buttonsContainer, textEditor);
        addButton(2, options, customMessages, button, controler, buttonsContainer, textEditor);
        addButton(4, options, customMessages, button, controler, buttonsContainer, textEditor);
        addButton(32, options, customMessages, button, controler, buttonsContainer, textEditor);
        addButton(64, options, customMessages, button, controler, buttonsContainer, textEditor);
        addButton(128, options, customMessages, button, controler, buttonsContainer, textEditor);
        addButton(256, options, customMessages, button, controler, buttonsContainer, textEditor);
        button.destroySelfFromParent();
    }
    
    public static TextView setMessage(final Window messageBox, final MessageBoxData data) {
        final ElementMap map = messageBox.getElementMap();
        final TextView textView = (TextView)map.getElement("messageBoxTextView");
        textView.setText(data.getMessage());
        return textView;
    }
    
    private static void addButton(final int type, final long options, final ArrayList<String> customMessages, final Button button, final MessageBoxControler controler, final Container container, final TextEditor textEditor) {
        if ((options & type) == type) {
            addButton(button, controler, container, type, getTextButton(type, customMessages), textEditor);
        }
    }
    
    private static Button addButton(final Button modelButton, final MessageBoxControler controler, final Container container, final int type, final String text, final TextEditor textEditor) {
        Button button = null;
        try {
            button = (Button)modelButton.getClass().newInstance();
            button.onCheckOut();
            modelButton.copyElement(button);
            button.setText(text);
            button.setOnClick(new MouseClickedListener() {
                @Override
                public boolean run(final Event event) {
                    controler.messageBoxClosed(type, (textEditor != null) ? textEditor.getText() : null);
                    return false;
                }
            });
            button.setElementMap(modelButton.getElementMap());
            button.onChildrenAdded();
            switch (type) {
                case 2:
                case 8: {
                    button.setClickSoundId(XulorSoundManager.getInstance().getMessageBoxYesButtonId());
                    button.setFocusable(true);
                    button.setFocused(true);
                    break;
                }
                case 4:
                case 16: {
                    button.setClickSoundId(XulorSoundManager.getInstance().getMessageBoxNoButtonId());
                    break;
                }
            }
            container.add(button);
        }
        catch (Exception e) {
            MessageBoxFormater.m_logger.error((Object)"Exception", (Throwable)e);
        }
        return button;
    }
    
    private static String getTextButton(final int type, final ArrayList<String> customMessages) {
        switch (type) {
            case 2: {
                return Xulor.getInstance().getTranslatedString("ok");
            }
            case 4: {
                return Xulor.getInstance().getTranslatedString("cancel");
            }
            case 8: {
                return Xulor.getInstance().getTranslatedString("yes");
            }
            case 16: {
                return Xulor.getInstance().getTranslatedString("no");
            }
            case 32: {
                if (customMessages == null || customMessages.size() < 1) {
                    return "";
                }
                return customMessages.get(0);
            }
            case 64: {
                if (customMessages == null || customMessages.size() < 2) {
                    return "";
                }
                return customMessages.get(1);
            }
            case 128: {
                if (customMessages == null || customMessages.size() < 3) {
                    return "";
                }
                return customMessages.get(2);
            }
            case 256: {
                if (customMessages == null || customMessages.size() < 4) {
                    return "";
                }
                return customMessages.get(3);
            }
            default: {
                return "";
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)MessageBoxFormater.class);
    }
}
