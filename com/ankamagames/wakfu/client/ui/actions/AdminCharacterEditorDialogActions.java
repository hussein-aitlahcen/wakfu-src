package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.colors.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.console.command.debug.anim.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.core.game.spell.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class AdminCharacterEditorDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.adminCharacterEditor";
    private static final ArrayList<ColorEditedFieldProvider> m_activeColors;
    private static final ArrayList<ColorEditedFieldProvider> m_inactiveColors;
    
    public static void openCharacterColorEditor(final Event e) {
        if (Xulor.getInstance().isLoaded("adminCharacterEditorDialog")) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            AdminCharacterEditorDialogActions.m_activeColors.clear();
            AdminCharacterEditorDialogActions.m_inactiveColors.clear();
            for (final ColoredPart coloredPart : ColoredPart.values()) {
                final CharacterColor randomColor = new CharacterColor(-1, MathHelper.randomFloat(), MathHelper.randomFloat(), MathHelper.randomFloat());
                if (coloredPart.isDefaultColor()) {
                    AdminCharacterEditorDialogActions.m_activeColors.add(new ColorEditedFieldProvider(randomColor, coloredPart));
                }
                else {
                    AdminCharacterEditorDialogActions.m_inactiveColors.add(new ColorEditedFieldProvider(randomColor, coloredPart));
                }
            }
            reloadAnmimation(null);
            PropertiesProvider.getInstance().setPropertyValue("adminColors", AdminCharacterEditorDialogActions.m_activeColors);
            PropertiesProvider.getInstance().setPropertyValue("adminInactiveColors", AdminCharacterEditorDialogActions.m_inactiveColors);
            Xulor.getInstance().load("adminCharacterColorEditorDialog", Dialogs.getDialogPath("adminCharacterColorEditorDialog"), 1L, (short)10000);
            Xulor.getInstance().unload("adminCharacterEditorDialog");
        }
        else {
            Xulor.getInstance().load("adminCharacterEditorDialog", Dialogs.getDialogPath("adminCharacterEditorDialog"), 1L, (short)10000);
            Xulor.getInstance().unload("adminCharacterColorEditorDialog");
            PropertiesProvider.getInstance().removeProperty("adminAnimDirection");
            PropertiesProvider.getInstance().removeProperty("adminAnimName");
            PropertiesProvider.getInstance().removeProperty("adminColors");
            PropertiesProvider.getInstance().removeProperty("adminInactiveColors");
        }
    }
    
    public static void loadAnmDebugger(final Event e) {
        if (Xulor.getInstance().isLoaded("debugAnmDialog")) {
            Xulor.getInstance().unload("debugAnmDialog");
        }
        else {
            AnmDebuggerCommand.loadAnmDebugger();
        }
    }
    
    public static void reloadAnmimation(final Event e) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final CharacterActor characterActor = localPlayer.getActor();
        PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer, "actorDescriptorLibrary");
        PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer, "actorEquipment");
        PropertiesProvider.getInstance().setPropertyValue("adminAnimDirection", characterActor.getDirection().m_index);
        PropertiesProvider.getInstance().setPropertyValue("adminAnimName", characterActor.getStaticAnimationKey());
    }
    
    public static void deleteActiveColor(final Event e, final ColorEditedFieldProvider colorEditedFieldProvider) {
        AdminCharacterEditorDialogActions.m_activeColors.remove(colorEditedFieldProvider);
        AdminCharacterEditorDialogActions.m_inactiveColors.add(colorEditedFieldProvider);
        PropertiesProvider.getInstance().setPropertyValue("adminColors", null);
        PropertiesProvider.getInstance().setPropertyValue("adminInactiveColors", null);
        PropertiesProvider.getInstance().setPropertyValue("adminColors", AdminCharacterEditorDialogActions.m_activeColors);
        PropertiesProvider.getInstance().setPropertyValue("adminInactiveColors", AdminCharacterEditorDialogActions.m_inactiveColors);
    }
    
    public static void activateColor(final Event e) {
        final ComboBox comboBox = (ComboBox)e.getTarget().getElementMap().getElement("comboColor");
        final ColorEditedFieldProvider colorEditedFieldProvider = (ColorEditedFieldProvider)comboBox.getSelectedValue();
        AdminCharacterEditorDialogActions.m_inactiveColors.remove(colorEditedFieldProvider);
        AdminCharacterEditorDialogActions.m_activeColors.add(colorEditedFieldProvider);
        PropertiesProvider.getInstance().setPropertyValue("adminColors", null);
        PropertiesProvider.getInstance().setPropertyValue("adminInactiveColors", null);
        PropertiesProvider.getInstance().setPropertyValue("adminColors", AdminCharacterEditorDialogActions.m_activeColors);
        PropertiesProvider.getInstance().setPropertyValue("adminInactiveColors", AdminCharacterEditorDialogActions.m_inactiveColors);
    }
    
    public static void setFilterRed(final SliderMovedEvent e, final ColorEditedFieldProvider colorEditedFieldProvider) {
        if (colorEditedFieldProvider != null) {
            colorEditedFieldProvider.setRed(e.getValue());
            colorEditedFieldProvider.applyColor((AnimatedElementViewer)e.getCurrentTarget().getElementMap().getElement("localPlayerDisplay"));
        }
    }
    
    public static void setFilterGreen(final SliderMovedEvent e, final ColorEditedFieldProvider colorEditedFieldProvider) {
        if (colorEditedFieldProvider != null) {
            colorEditedFieldProvider.setGreen(e.getValue());
            colorEditedFieldProvider.applyColor((AnimatedElementViewer)e.getCurrentTarget().getElementMap().getElement("localPlayerDisplay"));
        }
    }
    
    public static void setFilterBlue(final SliderMovedEvent e, final ColorEditedFieldProvider colorEditedFieldProvider) {
        if (colorEditedFieldProvider != null) {
            colorEditedFieldProvider.setBlue(e.getValue());
            colorEditedFieldProvider.applyColor((AnimatedElementViewer)e.getCurrentTarget().getElementMap().getElement("localPlayerDisplay"));
        }
    }
    
    public static void setFilterRed(final Event e, final TextEditor te, final ColorEditedFieldProvider colorEditedFieldProvider) {
        if (te.getText().length() == 0) {
            return;
        }
        final float red = PrimitiveConverter.getShort(te.getText()) / 255.0f;
        colorEditedFieldProvider.setRed(red);
        colorEditedFieldProvider.applyColor((AnimatedElementViewer)e.getCurrentTarget().getElementMap().getElement("localPlayerDisplay"));
    }
    
    public static void setFilterGreen(final Event e, final TextEditor te, final ColorEditedFieldProvider colorEditedFieldProvider) {
        if (te.getText().length() == 0) {
            return;
        }
        final float green = PrimitiveConverter.getShort(te.getText()) / 255.0f;
        colorEditedFieldProvider.setGreen(green);
        colorEditedFieldProvider.applyColor((AnimatedElementViewer)e.getCurrentTarget().getElementMap().getElement("localPlayerDisplay"));
    }
    
    public static void setFilterBlue(final Event e, final TextEditor te, final ColorEditedFieldProvider colorEditedFieldProvider) {
        if (e.getType() != Events.KEY_TYPED) {
            return;
        }
        if (te.getText().length() == 0) {
            return;
        }
        final float blue = PrimitiveConverter.getShort(te.getText()) / 255.0f;
        colorEditedFieldProvider.setBlue(blue);
        colorEditedFieldProvider.applyColor((AnimatedElementViewer)e.getCurrentTarget().getElementMap().getElement("localPlayerDisplay"));
    }
    
    public static void applyColor(final Event e, final ColorEditedFieldProvider colorEditedFieldProvider) {
        colorEditedFieldProvider.applyColor((AnimatedElementViewer)e.getCurrentTarget().getElementMap().getElement("localPlayerDisplay"));
    }
    
    private static final void enableApplyColorButton(final Event e) {
        final Button button = (Button)e.getCurrentTarget().getElementMap().getElement("applyColorButton");
        button.setEnabled(true);
    }
    
    public static void validLevel(final MouseEvent e) {
        final TextEditor textEditor = (TextEditor)Xulor.getInstance().getEnvironment().getElementMap("adminCharacterEditorDialog").getElement("level");
        if (textEditor == null || textEditor.getText() == null || textEditor.getText().length() == 0) {
            return;
        }
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)44);
        netMessage.addShortParameter(Short.parseShort(textEditor.getText()));
        networkEntity.sendMessage(netMessage);
        updateAll();
    }
    
    public static void validSpellLevel(final MouseEvent e, final SpellLevel spellLevel, final TextEditor textEditor) {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage netMessage = new ModerationCommandMessage();
        netMessage.setServerId((byte)3);
        netMessage.setCommand((short)26);
        netMessage.addIntParameter(spellLevel.getReferenceId());
        netMessage.addShortParameter(Short.parseShort(textEditor.getText()));
        networkEntity.sendMessage(netMessage);
        updateAll();
    }
    
    public static void learnSkill(final MouseEvent e, final CraftView craftView) {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)97);
        msg.addIntParameter(craftView.getCraftReferenceId());
        networkEntity.sendMessage(msg);
        updateAll();
    }
    
    public static void addSkillXp(final MouseEvent e, final CraftView craftView, final TextEditor textEditor) {
        final NetworkEntity networkEntity = WakfuGameEntity.getInstance().getNetworkEntity();
        if (networkEntity == null) {
            ConsoleManager.getInstance().err("Pour acc\u00e9der \u00e0 ces commandes il faut \u00eatre connect\u00e9 !");
            return;
        }
        long s = 0L;
        try {
            s = Long.parseLong(textEditor.getText());
        }
        catch (Exception ex) {
            AdminCharacterEditorDialogActions.m_logger.error((Object)"Exception", (Throwable)ex);
        }
        textEditor.setText("0");
        final ModerationCommandMessage msg = new ModerationCommandMessage();
        msg.setServerId((byte)3);
        msg.setCommand((short)99);
        msg.addIntParameter(craftView.getCraftReferenceId());
        msg.addLongParameter(s);
        networkEntity.sendMessage(msg);
        updateAll();
    }
    
    private static void updateAll() {
        ProcessScheduler.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getFields());
                PropertiesProvider.getInstance().firePropertyValueChanged(CraftDisplayer.INSTANCE, "crafts");
                UIAdminCharacterEditorFrame.updateSpellsList();
                UIAdminCharacterEditorFrame.updateSkillsList();
            }
        }, 1500L, 1);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AdminCharacterEditorDialogActions.class);
        m_activeColors = new ArrayList<ColorEditedFieldProvider>();
        m_inactiveColors = new ArrayList<ColorEditedFieldProvider>();
    }
    
    private static class ColorEditedFieldProvider implements FieldProvider
    {
        private CharacterColor m_currentColor;
        private final ColoredPart m_coloredPart;
        public static final String CURRENT_COLOR_FIELD = "currentColor";
        public static final String RED_FIELD = "red";
        public static final String GREEN_FIELD = "green";
        public static final String BLUE_FIELD = "blue";
        public static final String RED255_FIELD = "red255";
        public static final String GREEN255_FIELD = "green255";
        public static final String BLUE255_FIELD = "blue255";
        public static final String NAME_FIELD = "name";
        public static final String FLOAT_STRING_FIELD = "floatString";
        public static final String[] FIELDS;
        
        public ColorEditedFieldProvider(final CharacterColor currentColor, final ColoredPart coloredPart) {
            super();
            this.m_currentColor = currentColor;
            this.m_coloredPart = coloredPart;
        }
        
        @Override
        public String[] getFields() {
            return ColorEditedFieldProvider.FIELDS;
        }
        
        @Override
        public Object getFieldValue(final String fieldName) {
            if (fieldName.equals("currentColor")) {
                return this.m_currentColor;
            }
            if (fieldName.equals("red")) {
                return this.m_currentColor.getRed();
            }
            if (fieldName.equals("green")) {
                return this.m_currentColor.getGreen();
            }
            if (fieldName.equals("blue")) {
                return this.m_currentColor.getBlue();
            }
            if (fieldName.equals("red255")) {
                return (int)(this.m_currentColor.getRed() * 255.0f);
            }
            if (fieldName.equals("green255")) {
                return (int)(this.m_currentColor.getGreen() * 255.0f);
            }
            if (fieldName.equals("blue255")) {
                return (int)(this.m_currentColor.getBlue() * 255.0f);
            }
            if (fieldName.equals("name")) {
                return this.m_coloredPart.name();
            }
            if (fieldName.equals("floatString")) {
                return MathHelper.round(this.m_currentColor.getRed(), 2) + "f, " + MathHelper.round(this.m_currentColor.getGreen(), 2) + "f, " + MathHelper.round(this.m_currentColor.getBlue(), 2) + "f";
            }
            return null;
        }
        
        @Override
        public void setFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public void prependFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public void appendFieldValue(final String fieldName, final Object value) {
        }
        
        @Override
        public boolean isFieldSynchronisable(final String fieldName) {
            return false;
        }
        
        public void setRed(final float red) {
            this.m_currentColor = new CharacterColor(-1, red, this.m_currentColor.getGreen(), this.m_currentColor.getBlue());
            PropertiesProvider.getInstance().firePropertyValueChanged(this, ColorEditedFieldProvider.FIELDS);
        }
        
        public void setGreen(final float green) {
            this.m_currentColor = new CharacterColor(-1, this.m_currentColor.getRed(), green, this.m_currentColor.getBlue());
            PropertiesProvider.getInstance().firePropertyValueChanged(this, ColorEditedFieldProvider.FIELDS);
        }
        
        public void setBlue(final float blue) {
            this.m_currentColor = new CharacterColor(-1, this.m_currentColor.getRed(), this.m_currentColor.getGreen(), blue);
            PropertiesProvider.getInstance().firePropertyValueChanged(this, ColorEditedFieldProvider.FIELDS);
        }
        
        public void applyColor(final AnimatedElementViewer animatedElementViewer) {
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final CharacterActor characterActor = localPlayer.getActor();
            CharacterColor.applyColor(this.m_currentColor, characterActor, this.m_coloredPart.getColoredPartId());
            characterActor.setColorChanged(true);
            PropertiesProvider.getInstance().firePropertyValueChanged(localPlayer, localPlayer.getFields());
        }
        
        static {
            FIELDS = new String[] { "currentColor", "red", "green", "blue", "red255", "green255", "blue255", "name", "floatString" };
        }
    }
    
    private enum ColoredPart
    {
        HAIR(2, true), 
        SKIN(1, true), 
        PUPIL(8, true), 
        SYMBOL_BG(3, false), 
        SYMBOL_FG(4, false), 
        SYMBOL_BORDER(5, false), 
        COLOR_1(6, false), 
        COLOR_2(7, false), 
        CLOTHES(9, false);
        
        private final int m_coloredPartId;
        private final boolean m_defaultColor;
        
        private ColoredPart(final int coloredPartId, final boolean defaultColor) {
            this.m_coloredPartId = coloredPartId;
            this.m_defaultColor = defaultColor;
        }
        
        public int getColoredPartId() {
            return this.m_coloredPartId;
        }
        
        public boolean isDefaultColor() {
            return this.m_defaultColor;
        }
    }
}
