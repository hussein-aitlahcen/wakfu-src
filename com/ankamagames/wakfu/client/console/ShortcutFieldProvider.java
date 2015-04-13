package com.ankamagames.wakfu.client.console;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.wakfu.client.core.*;
import java.util.*;

public class ShortcutFieldProvider implements FieldProvider
{
    public static final String KEY_FIELD = "key";
    public static final String EFFECT_FIELD = "effect";
    public static final String TEXT_FIELD = "text";
    public static final String IS_BIND_FIELD = "isBind";
    private final String[] FIELDS;
    private Shortcut m_shortcut;
    
    public ShortcutFieldProvider(final Shortcut shortcut) {
        super();
        this.FIELDS = new String[] { "key", "effect", "text", "isBind" };
        this.m_shortcut = shortcut;
    }
    
    @Override
    public String[] getFields() {
        return this.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("key")) {
            return this.getKeyText();
        }
        if (fieldName.equals("effect")) {
            return this.getEffectString();
        }
        if (fieldName.equals("text")) {
            return this.getText();
        }
        if (fieldName.equals("isBind")) {
            return "binding".equals(this.getGroupName());
        }
        return null;
    }
    
    public String getKeyText() {
        String keyText = null;
        switch (this.m_shortcut.getKeyCode()) {
            case -1: {
                keyText = WakfuTranslator.getInstance().getString("unassignedShortcutKey");
                break;
            }
            case 9: {
                keyText = "Tab";
                break;
            }
            default: {
                keyText = getKeyText(this.m_shortcut.getKeyCode());
                break;
            }
        }
        keyText = (keyText.startsWith("Unknown") ? WakfuTranslator.getInstance().getString("unknown") : keyText);
        if (this.m_shortcut.isShiftKey()) {
            keyText = getKeyText(16) + " + " + keyText;
        }
        if (this.m_shortcut.isAltKey()) {
            keyText = getKeyText(18) + " + " + keyText;
        }
        if (this.m_shortcut.isCtrlKey()) {
            keyText = getKeyText(17) + " + " + keyText;
        }
        return keyText;
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
    
    public int getKeyCodeStart() {
        return this.m_shortcut.getKeyCode();
    }
    
    public String getId() {
        return this.m_shortcut.getId();
    }
    
    public Shortcut getShortcut() {
        return this.m_shortcut;
    }
    
    public int getModifiersEx() {
        return this.m_shortcut.getModiferMask();
    }
    
    public String getEffectString() {
        return WakfuTranslator.getInstance().getString("shortcutEffect." + ("binding".equals(this.getGroupName()) ? "binding" : this.getId()));
    }
    
    public String getGroupName() {
        return this.m_shortcut.getGroupName();
    }
    
    public String getText() {
        return this.m_shortcut.getParamString();
    }
    
    @Override
    public String toString() {
        return this.getText();
    }
    
    public static String getKeyText(final int keyCode) {
        if ((keyCode >= 48 && keyCode <= 57) || (keyCode >= 65 && keyCode <= 90)) {
            return String.valueOf((char)keyCode);
        }
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("sun.awt.resources.awt", WakfuTranslator.getInstance().getLanguage().getActualLocale());
        switch (keyCode) {
            case 10: {
                return resourceBundle.getString("AWT.enter");
            }
            case 8: {
                return resourceBundle.getString("AWT.backSpace");
            }
            case 9: {
                return resourceBundle.getString("AWT.tab");
            }
            case 3: {
                return resourceBundle.getString("AWT.cancel");
            }
            case 12: {
                return resourceBundle.getString("AWT.clear");
            }
            case 65312: {
                return resourceBundle.getString("AWT.compose");
            }
            case 19: {
                return resourceBundle.getString("AWT.pause");
            }
            case 20: {
                return resourceBundle.getString("AWT.capsLock");
            }
            case 27: {
                return resourceBundle.getString("AWT.escape");
            }
            case 32: {
                return resourceBundle.getString("AWT.space");
            }
            case 33: {
                return resourceBundle.getString("AWT.pgup");
            }
            case 34: {
                return resourceBundle.getString("AWT.pgdn");
            }
            case 35: {
                return resourceBundle.getString("AWT.end");
            }
            case 36: {
                return resourceBundle.getString("AWT.home");
            }
            case 37: {
                return resourceBundle.getString("AWT.left");
            }
            case 38: {
                return resourceBundle.getString("AWT.up");
            }
            case 39: {
                return resourceBundle.getString("AWT.right");
            }
            case 40: {
                return resourceBundle.getString("AWT.down");
            }
            case 65368: {
                return resourceBundle.getString("AWT.begin");
            }
            case 16: {
                return resourceBundle.getString("AWT.shift");
            }
            case 17: {
                return resourceBundle.getString("AWT.control");
            }
            case 18: {
                return resourceBundle.getString("AWT.alt");
            }
            case 157: {
                return resourceBundle.getString("AWT.meta");
            }
            case 65406: {
                return resourceBundle.getString("AWT.altGraph");
            }
            case 44: {
                return resourceBundle.getString("AWT.comma");
            }
            case 46: {
                return resourceBundle.getString("AWT.period");
            }
            case 47: {
                return resourceBundle.getString("AWT.slash");
            }
            case 59: {
                return resourceBundle.getString("AWT.semicolon");
            }
            case 61: {
                return resourceBundle.getString("AWT.equals");
            }
            case 91: {
                return resourceBundle.getString("AWT.openBracket");
            }
            case 92: {
                return resourceBundle.getString("AWT.backSlash");
            }
            case 93: {
                return resourceBundle.getString("AWT.closeBracket");
            }
            case 106: {
                return resourceBundle.getString("AWT.multiply");
            }
            case 107: {
                return resourceBundle.getString("AWT.add");
            }
            case 108: {
                return resourceBundle.getString("AWT.separator");
            }
            case 109: {
                return resourceBundle.getString("AWT.subtract");
            }
            case 110: {
                return resourceBundle.getString("AWT.decimal");
            }
            case 111: {
                return resourceBundle.getString("AWT.divide");
            }
            case 127: {
                return resourceBundle.getString("AWT.delete");
            }
            case 144: {
                return resourceBundle.getString("AWT.numLock");
            }
            case 145: {
                return resourceBundle.getString("AWT.scrollLock");
            }
            case 524: {
                return resourceBundle.getString("AWT.windows");
            }
            case 525: {
                return resourceBundle.getString("AWT.context");
            }
            case 112: {
                return resourceBundle.getString("AWT.f1");
            }
            case 113: {
                return resourceBundle.getString("AWT.f2");
            }
            case 114: {
                return resourceBundle.getString("AWT.f3");
            }
            case 115: {
                return resourceBundle.getString("AWT.f4");
            }
            case 116: {
                return resourceBundle.getString("AWT.f5");
            }
            case 117: {
                return resourceBundle.getString("AWT.f6");
            }
            case 118: {
                return resourceBundle.getString("AWT.f7");
            }
            case 119: {
                return resourceBundle.getString("AWT.f8");
            }
            case 120: {
                return resourceBundle.getString("AWT.f9");
            }
            case 121: {
                return resourceBundle.getString("AWT.f10");
            }
            case 122: {
                return resourceBundle.getString("AWT.f11");
            }
            case 123: {
                return resourceBundle.getString("AWT.f12");
            }
            case 61440: {
                return resourceBundle.getString("AWT.f13");
            }
            case 61441: {
                return resourceBundle.getString("AWT.f14");
            }
            case 61442: {
                return resourceBundle.getString("AWT.f15");
            }
            case 61443: {
                return resourceBundle.getString("AWT.f16");
            }
            case 61444: {
                return resourceBundle.getString("AWT.f17");
            }
            case 61445: {
                return resourceBundle.getString("AWT.f18");
            }
            case 61446: {
                return resourceBundle.getString("AWT.f19");
            }
            case 61447: {
                return resourceBundle.getString("AWT.f20");
            }
            case 61448: {
                return resourceBundle.getString("AWT.f21");
            }
            case 61449: {
                return resourceBundle.getString("AWT.f22");
            }
            case 61450: {
                return resourceBundle.getString("AWT.f23");
            }
            case 61451: {
                return resourceBundle.getString("AWT.f24");
            }
            case 154: {
                return resourceBundle.getString("AWT.printScreen");
            }
            case 155: {
                return resourceBundle.getString("AWT.insert");
            }
            case 156: {
                return resourceBundle.getString("AWT.help");
            }
            case 192: {
                return resourceBundle.getString("AWT.backQuote");
            }
            case 222: {
                return resourceBundle.getString("AWT.quote");
            }
            case 224: {
                return resourceBundle.getString("AWT.up");
            }
            case 225: {
                return resourceBundle.getString("AWT.down");
            }
            case 226: {
                return resourceBundle.getString("AWT.left");
            }
            case 227: {
                return resourceBundle.getString("AWT.right");
            }
            case 128: {
                return resourceBundle.getString("AWT.deadGrave");
            }
            case 129: {
                return resourceBundle.getString("AWT.deadAcute");
            }
            case 130: {
                return resourceBundle.getString("AWT.deadCircumflex");
            }
            case 131: {
                return resourceBundle.getString("AWT.deadTilde");
            }
            case 132: {
                return resourceBundle.getString("AWT.deadMacron");
            }
            case 133: {
                return resourceBundle.getString("AWT.deadBreve");
            }
            case 134: {
                return resourceBundle.getString("AWT.deadAboveDot");
            }
            case 135: {
                return resourceBundle.getString("AWT.deadDiaeresis");
            }
            case 136: {
                return resourceBundle.getString("AWT.deadAboveRing");
            }
            case 137: {
                return resourceBundle.getString("AWT.deadDoubleAcute");
            }
            case 138: {
                return resourceBundle.getString("AWT.deadCaron");
            }
            case 139: {
                return resourceBundle.getString("AWT.deadCedilla");
            }
            case 140: {
                return resourceBundle.getString("AWT.deadOgonek");
            }
            case 141: {
                return resourceBundle.getString("AWT.deadIota");
            }
            case 142: {
                return resourceBundle.getString("AWT.deadVoicedSound");
            }
            case 143: {
                return resourceBundle.getString("AWT.deadSemivoicedSound");
            }
            case 150: {
                return resourceBundle.getString("AWT.ampersand");
            }
            case 151: {
                return resourceBundle.getString("AWT.asterisk");
            }
            case 152: {
                return resourceBundle.getString("AWT.quoteDbl");
            }
            case 153: {
                return resourceBundle.getString("AWT.Less");
            }
            case 160: {
                return resourceBundle.getString("AWT.greater");
            }
            case 161: {
                return resourceBundle.getString("AWT.braceLeft");
            }
            case 162: {
                return resourceBundle.getString("AWT.braceRight");
            }
            case 512: {
                return resourceBundle.getString("AWT.at");
            }
            case 513: {
                return resourceBundle.getString("AWT.colon");
            }
            case 514: {
                return resourceBundle.getString("AWT.circumflex");
            }
            case 515: {
                return resourceBundle.getString("AWT.dollar");
            }
            case 516: {
                return resourceBundle.getString("AWT.euro");
            }
            case 517: {
                return resourceBundle.getString("AWT.exclamationMark");
            }
            case 518: {
                return resourceBundle.getString("AWT.invertedExclamationMark");
            }
            case 519: {
                return resourceBundle.getString("AWT.leftParenthesis");
            }
            case 520: {
                return resourceBundle.getString("AWT.numberSign");
            }
            case 45: {
                return resourceBundle.getString("AWT.minus");
            }
            case 521: {
                return resourceBundle.getString("AWT.plus");
            }
            case 522: {
                return resourceBundle.getString("AWT.rightParenthesis");
            }
            case 523: {
                return resourceBundle.getString("AWT.underscore");
            }
            case 24: {
                return resourceBundle.getString("AWT.final");
            }
            case 28: {
                return resourceBundle.getString("AWT.convert");
            }
            case 29: {
                return resourceBundle.getString("AWT.noconvert");
            }
            case 30: {
                return resourceBundle.getString("AWT.accept");
            }
            case 31: {
                return resourceBundle.getString("AWT.modechange");
            }
            case 21: {
                return resourceBundle.getString("AWT.kana");
            }
            case 25: {
                return resourceBundle.getString("AWT.kanji");
            }
            case 240: {
                return resourceBundle.getString("AWT.alphanumeric");
            }
            case 241: {
                return resourceBundle.getString("AWT.katakana");
            }
            case 242: {
                return resourceBundle.getString("AWT.hiragana");
            }
            case 243: {
                return resourceBundle.getString("AWT.fullWidth");
            }
            case 244: {
                return resourceBundle.getString("AWT.halfWidth");
            }
            case 245: {
                return resourceBundle.getString("AWT.romanCharacters");
            }
            case 256: {
                return resourceBundle.getString("AWT.allCandidates");
            }
            case 257: {
                return resourceBundle.getString("AWT.previousCandidate");
            }
            case 258: {
                return resourceBundle.getString("AWT.codeInput");
            }
            case 259: {
                return resourceBundle.getString("AWT.japaneseKatakana");
            }
            case 260: {
                return resourceBundle.getString("AWT.japaneseHiragana");
            }
            case 261: {
                return resourceBundle.getString("AWT.japaneseRoman");
            }
            case 262: {
                return resourceBundle.getString("AWT.kanaLock");
            }
            case 263: {
                return resourceBundle.getString("AWT.inputMethodOnOff");
            }
            case 65481: {
                return resourceBundle.getString("AWT.again");
            }
            case 65483: {
                return resourceBundle.getString("AWT.undo");
            }
            case 65485: {
                return resourceBundle.getString("AWT.copy");
            }
            case 65487: {
                return resourceBundle.getString("AWT.paste");
            }
            case 65489: {
                return resourceBundle.getString("AWT.cut");
            }
            case 65488: {
                return resourceBundle.getString("AWT.find");
            }
            case 65482: {
                return resourceBundle.getString("AWT.props");
            }
            case 65480: {
                return resourceBundle.getString("AWT.stop");
            }
            default: {
                if (keyCode >= 96 && keyCode <= 105) {
                    final String numpad = resourceBundle.getString("AWT.numpad");
                    final char c = (char)(keyCode - 96 + 48);
                    return numpad + "-" + c;
                }
                final String unknown = resourceBundle.getString("AWT.unknown");
                return unknown + " keyCode: 0x" + Integer.toString(keyCode, 16);
            }
        }
    }
}
