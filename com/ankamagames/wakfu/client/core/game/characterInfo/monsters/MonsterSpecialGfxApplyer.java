package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import gnu.trove.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;

public class MonsterSpecialGfxApplyer
{
    private static final Logger m_logger;
    
    public static void applyCustoms(final MonsterSpecialGfx info, final CharacterInfo npc) {
        if (info == null) {
            return;
        }
        if (!info.hasEquipement()) {
            return;
        }
        String fileFormatter;
        try {
            fileFormatter = WakfuConfiguration.getInstance().getString("ANMEquipmentPath");
        }
        catch (PropertyException e) {
            MonsterSpecialGfxApplyer.m_logger.error((Object)"", (Throwable)e);
            return;
        }
        final CharacterActor actor = npc.getActor();
        info.foreachEquipement(new TObjectProcedure<MonsterSpecialGfx.Equipment>() {
            @Override
            public boolean execute(final MonsterSpecialGfx.Equipment equipment) {
                final String path = String.format(fileFormatter, equipment.m_fileId);
                actor.applyParts(path, AnmPartHelper.getParts(equipment.m_parts));
                return true;
            }
        });
    }
    
    public static void applyDefaultAnims(final MonsterSpecialGfx info, final CharacterInfo npc) {
        if (info == null) {
            return;
        }
        final MonsterSpecialGfx.Anim[] anims = info.getAnims();
        if (anims == null) {
            return;
        }
        final CharacterActor actor = npc.getActor();
        for (final MonsterSpecialGfx.Anim anim : anims) {
            switch (anim.m_key) {
                case 1: {
                    actor.setStaticAnimationKey(anim.m_anim);
                    actor.setAnimation(anim.m_anim);
                    break;
                }
                case 2: {
                    actor.setHitAnimationKey(anim.m_anim);
                    break;
                }
                case 3: {
                    actor.setAnimation(anim.m_anim);
                    break;
                }
            }
        }
    }
    
    public static void applyColors(final MonsterSpecialGfx info, final CharacterInfo npc) {
        if (info == null) {
            return;
        }
        final CharacterActor actor = npc.getActor();
        info.foreachColor(new TObjectProcedure<MonsterSpecialGfx.Colors>() {
            @Override
            public boolean execute(final MonsterSpecialGfx.Colors c) {
                MonsterSpecialGfxApplyer.applyColor(actor, c);
                return true;
            }
        });
    }
    
    public static void applyColor(final Mobile actor, final MonsterSpecialGfx.Colors c) {
        final float[] floatRGBA;
        final float[] rgba = floatRGBA = c.m_color.getFloatRGBA();
        final int n = 0;
        floatRGBA[n] *= 1.25f;
        final float[] array = rgba;
        final int n2 = 1;
        array[n2] *= 1.25f;
        final float[] array2 = rgba;
        final int n3 = 2;
        array2[n3] *= 1.25f;
        actor.setCustomColor(c.m_partIndex, rgba);
    }
    
    static {
        m_logger = Logger.getLogger((Class)MonsterSpecialGfxApplyer.class);
    }
}
