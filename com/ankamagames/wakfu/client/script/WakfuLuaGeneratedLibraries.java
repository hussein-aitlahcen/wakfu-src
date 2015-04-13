package com.ankamagames.wakfu.client.script;

import com.ankamagames.wakfu.client.ui.script.function.bubbleText.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.effectArea.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.fightActionGroupFunctionLibrary.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.cast.*;
import com.ankamagames.wakfu.client.core.action.world.*;
import com.ankamagames.framework.script.libraries.scriptedAction.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.ui.script.*;
import com.ankamagames.wakfu.client.core.script.video.*;
import com.ankamagames.wakfu.client.core.script.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.scriptedAction.*;
import com.ankamagames.baseImpl.graphics.script.*;

public enum WakfuLuaGeneratedLibraries
{
    AMBIANCE((JavaFunctionsLibrary)AmbianceFunctionLibrary.getInstance()), 
    BIT_OPERATOR((JavaFunctionsLibrary)new BitOperatorFunctionsLibrary()), 
    BUBBLE_TEXT((JavaFunctionsLibrary)BubbleText.getInstance()), 
    CAMERA((JavaFunctionsLibrary)CameraFunctionsLibrary.getInstance()), 
    CHALLENGE((JavaFunctionsLibrary)ChallengeFunctionsLibrary.getInstance()), 
    CLIMATE((JavaFunctionsLibrary)ClimateFunctionsLibrary.getInstance()), 
    CONTEXT((JavaFunctionsLibrary)ContextFunctionsLibrary.getInstance()), 
    DEFAULT((JavaFunctionsLibrary)DefaultFunctionsLibrary.getInstance()), 
    EFFECT_AREA((JavaFunctionsLibrary)new EffectAreaFunctionsLibrary(null)), 
    EFFECT((JavaFunctionsLibrary)EffectFunctionsLibrary.getInstance()), 
    EVENT((JavaFunctionsLibrary)EventFunctionsLibrary.getInstance()), 
    FIGHT((JavaFunctionsLibrary)FightActionFunctionLibrary.INSTANCE), 
    FIGHT_CAST((JavaFunctionsLibrary)new CastFunctionsLibrary(null)), 
    FIGHT_EVENT((JavaFunctionsLibrary)FightEventFunctionsLibrary.getInstance()), 
    FLYING_ELEMENT((JavaFunctionsLibrary)FlyingElementFunctionsLibrary.getInstance()), 
    INTERACTIVE_ELEMENT((JavaFunctionsLibrary)InteractiveElementFunctionsLibrary.getInstance()), 
    ITEM_ACTION((JavaFunctionsLibrary)new ItemActionFunctionsLibrary(null)), 
    LIGHT((JavaFunctionsLibrary)LightFunctionsLibrary.getInstance()), 
    MOBILE((JavaFunctionsLibrary)MobileFunctionsLibrary.getInstance()), 
    MONSTER_ACTION((JavaFunctionsLibrary)new MonsterActionFunctionsLibrary(null)), 
    MONSTER_BEHAVIOUR((JavaFunctionsLibrary)new MonsterBehaviourFunctionsLibrary(null)), 
    MONSTER_EVOLUTION((JavaFunctionsLibrary)new MonsterEvolutionFunctionsLibrary(null)), 
    PARTICLE((JavaFunctionsLibrary)ParticleSystemFunctionsLibrary.getInstance()), 
    PET((JavaFunctionsLibrary)PetFunctionsLibrary.getInstance()), 
    PIXMAP((JavaFunctionsLibrary)PixmapFunctionsLibrary.getInstance()), 
    RESOURCE((JavaFunctionsLibrary)ResourceFunctionsLibrary.INSTANCE), 
    SCRIPTED_ACTION((JavaFunctionsLibrary)new ScriptedActionFunctionsLibrary(null)), 
    SERVER_EVENT((JavaFunctionsLibrary)ServerEventFunctionsLibrary.getInstance()), 
    SOUND((JavaFunctionsLibrary)SoundFunctionsLibrary.getInstance()), 
    SPELL_EFFECT((JavaFunctionsLibrary)new SpellEffectFunctionsLibrary(null)), 
    SYSTEM_MESSAGE((JavaFunctionsLibrary)SystemMessageFunctionsLibrary.getInstance()), 
    TUTORIAL((JavaFunctionsLibrary)TutorialFunctionsLibrary.getInstance()), 
    UI((JavaFunctionsLibrary)UIFunctionsLibrary.getInstance()), 
    VIDEO((JavaFunctionsLibrary)VideoFunctionLibrary.INSTANCE), 
    WAKFU_CHARACTER((JavaFunctionsLibrary)WakfuCharacterFunctionLibrary.getInstance()), 
    WAKFU_SCRIPTED((JavaFunctionsLibrary)new WakfuScriptedActionFunctionsLibrary(null)), 
    WORLD((JavaFunctionsLibrary)WorldFunctionsLibraries.getInstance());
    
    private final JavaFunctionsLibrary m_library;
    
    private WakfuLuaGeneratedLibraries(final JavaFunctionsLibrary library) {
        this.m_library = library;
    }
    
    public JavaFunctionsLibrary getLibrary() {
        return this.m_library;
    }
    
    public static JavaFunctionsLibrary[] getLibraries() {
        final WakfuLuaGeneratedLibraries[] values = values();
        final JavaFunctionsLibrary[] libs = new JavaFunctionsLibrary[values.length];
        for (int i = 0; i < values.length; ++i) {
            libs[i] = values[i].getLibrary();
        }
        return libs;
    }
}
