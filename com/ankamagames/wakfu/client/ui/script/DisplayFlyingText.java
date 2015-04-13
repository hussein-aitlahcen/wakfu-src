package com.ankamagames.wakfu.client.ui.script;

import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.ai.targetfinder.*;
import com.ankamagames.wakfu.client.core.game.IsoWorldTarget.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.*;
import com.ankamagames.baseImpl.graphics.alea.adviser.text.flying.*;
import java.util.*;

final class DisplayFlyingText extends JavaFunctionEx
{
    private final int FLYING_EFFECT_DURATION = 4500;
    private int FLYING_EFFECT_WAITING_TIME;
    
    DisplayFlyingText(final LuaState luaState) {
        super(luaState);
        this.FLYING_EFFECT_WAITING_TIME = 600;
    }
    
    @Override
    public String getName() {
        return "displayFlyingText";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("R", "Valeur rouge appliqu?e au texte", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("G", "Valeur verte appliqu?e au texte", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("B", "Valeur bleue appliqu?e au texte", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("font", "Font ? utiliser", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("targetId", "Id du mobile concern?", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("textKey", "Clef de traduction du texte", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("size", "Taille du texte", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("textParams", "Param?tres ?ventuels du texte (20 par d?faut)", LuaScriptParameterType.BLOOPS, true) };
    }
    
    @Override
    public String getDescription() {
        return "Affiche un texte volant au-dessus d'un mobile donn?";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final float r = (float)this.getParamDouble(0);
        final float g = (float)this.getParamDouble(1);
        final float b = (float)this.getParamDouble(2);
        String font = this.getParamString(3);
        final long casterId = this.getParamLong(4);
        final String textKey = this.getParamString(5);
        int style = 0;
        int size = 20;
        if (paramCount > 6) {
            size = Math.max(1, this.getParamInt(6));
        }
        String[] textParams;
        if (paramCount > 7) {
            textParams = new String[paramCount - 7];
            for (int i = 0; i < textParams.length; ++i) {
                textParams[i] = this.getParamForcedAsString(7 + i);
            }
        }
        else {
            textParams = new String[0];
        }
        if (font.isEmpty()) {
            style = 5;
            font = "wci";
            size = 23;
        }
        WorldPositionable target = null;
        final InteractiveIsoWorldTarget isotarget = InteractiveIsoWorldTargetManager.getInstance().getInteractiveIsoWorldTarget(casterId);
        if (isotarget != null && isotarget.isVisible()) {
            target = isotarget;
        }
        if (target == null) {
            return;
        }
        final String svalue = WakfuTranslator.getInstance().getString(textKey, (Object[])textParams);
        if (svalue != null && svalue.length() > 0) {
            this.displayText(r, g, b, font, style, size, target, svalue);
        }
    }
    
    private void displayText(final float r, final float g, final float b, final String font, final int style, final int size, final WorldPositionable target, final String text) {
        final FlyingTextDeformer deformer = new FlyingText.DefaultFlyingTextDeformer();
        final FlyingText flyingText = new FlyingText(FontFactory.createFont(font, style, size), text, deformer, 4500);
        flyingText.setColor(r, g, b, 1.0f);
        flyingText.setTarget(target);
        final HashSet<Adviser> advisers = AdviserManager.getInstance().getAdvisers(target);
        if (advisers != null) {
            flyingText.setWaitingTime(advisers.size() * this.FLYING_EFFECT_WAITING_TIME);
        }
        AdviserManager.getInstance().addAdviser(flyingText);
    }
}
