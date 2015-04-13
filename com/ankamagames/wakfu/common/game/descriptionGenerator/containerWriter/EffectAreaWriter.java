package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import java.util.*;

public class EffectAreaWriter extends DefaultContainerWriter<BasicEffectArea<WakfuEffect, EffectAreaParameters>>
{
    public EffectAreaWriter(final BasicEffectArea<WakfuEffect, EffectAreaParameters> container, final short containerLevel, final int freeDescriptionTranslationType) {
        super(container, (int)container.getBaseId(), containerLevel, true, null, null, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, freeDescriptionTranslationType);
    }
}
