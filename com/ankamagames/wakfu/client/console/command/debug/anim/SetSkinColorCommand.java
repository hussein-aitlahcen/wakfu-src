package com.ankamagames.wakfu.client.console.command.debug.anim;

import com.ankamagames.baseImpl.client.proxyclient.base.console.command.*;
import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.*;
import com.ankamagames.baseImpl.client.proxyclient.base.console.command.descriptors.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import java.util.regex.*;

public class SetSkinColorCommand implements Command
{
    private static final Logger m_logger;
    private static TIntObjectHashMap<float[]> m_oldColor;
    
    @Override
    public void execute(final ConsoleManager manager, final CommandPattern pattern, final ArrayList<String> args) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final int argCount = args.size();
        if (argCount != 3 && argCount != 4) {
            return;
        }
        if (localPlayer == null) {
            manager.err("pas de localPlayer");
            return;
        }
        final CharacterActor actor = localPlayer.getActor();
        final String part = args.get(2);
        final int index = Anm.getColorIndex(part);
        if (index == 0) {
            return;
        }
        boolean removed = false;
        float[] color;
        if (args.get(3) == null) {
            color = SetSkinColorCommand.m_oldColor.remove(index);
            removed = true;
        }
        else {
            final String[] values = args.get(3).split("\\s+");
            final float r = Float.parseFloat(values[0]);
            final float g = Float.parseFloat(values[1]);
            final float b = Float.parseFloat(values[2]);
            final float a = (values.length == 4) ? Float.parseFloat(values[3]) : 1.0f;
            color = new float[] { r * a, g * a, b * a, a };
        }
        final float[] oldColor = actor.getCustomColor(index);
        actor.setCustomColor(index, color);
        if (!SetSkinColorCommand.m_oldColor.contains(index) && oldColor != null && !removed) {
            SetSkinColorCommand.m_oldColor.put(index, oldColor);
        }
        actor.forceReloadAnimation();
    }
    
    @Override
    public boolean isPassThrough() {
        return false;
    }
    
    public static void main(final String[] args) {
        final Pattern argsPattern = Pattern.compile("(setSkinColor){1}\\s+([\\w\\-]+)\\s*((?:[0-1](?:\\.[0-9]+)?\\s*){3,4})?");
        final Matcher argsMatcher = argsPattern.matcher("setSkinColor Cheveux 1.0");
        if (argsMatcher.matches()) {
            argsMatcher.reset();
            final ArrayList<String> arg = new ArrayList<String>();
            while (argsMatcher.find()) {
                for (int i = 0; i <= argsMatcher.groupCount(); ++i) {
                    if (i == 3) {
                        final String[] a = argsMatcher.group(3).split("\\s+");
                        for (int j = 0; j < a.length; ++j) {
                            System.out.println(a[j]);
                        }
                    }
                    else {
                        System.out.println(argsMatcher.group(i));
                    }
                }
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetSkinColorCommand.class);
        SetSkinColorCommand.m_oldColor = new TIntObjectHashMap<float[]>();
    }
}
