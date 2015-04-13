package com.ankamagames.wakfu.client.core.game.characterInfo;

import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.core.utils.*;
import gnu.trove.*;

public class CharacterInfoZoneComputer
{
    public static void execute() {
        FamilyZoneComputer.execute(EventType.FAUNA_EVENT, new FamilyZoneComputer.Select() {
            @Override
            protected void fill() {
                CharacterInfoManager.getInstance().forEachCharacter(new TLongObjectProcedure<CharacterInfo>() {
                    @Override
                    public boolean execute(final long id, final CharacterInfo info) {
                        final int familyId = info.getBreed().getFamilyId();
                        Select.this.add(familyId);
                        return true;
                    }
                });
            }
        });
    }
}
