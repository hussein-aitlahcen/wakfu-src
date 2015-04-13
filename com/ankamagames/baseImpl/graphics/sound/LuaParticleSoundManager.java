package com.ankamagames.baseImpl.graphics.sound;

import com.ankamagames.framework.script.*;
import com.ankamagames.framework.fileFormat.io.*;

public class LuaParticleSoundManager extends BaseParticleSoundManager
{
    @Override
    public boolean playApsSound(final int apsFileId, final int apsId, final int fightId, final int duration) {
        final boolean exists = FileHelper.isExistingFile(LuaManager.getInstance().getPath() + ParticleScriptManager.getInstance().createParticleFileName(apsFileId));
        return exists && ParticleScriptManager.getInstance().playParticleSound(apsFileId, new int[] { apsId, fightId, duration });
    }
}
