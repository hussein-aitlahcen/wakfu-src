package com.ankamagames.wakfu.client.core.contentInitializer;

import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.baseImpl.graphics.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.common.game.basicDungeon.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.wakfu.client.core.*;

public final class MonsterFamilyLoader implements ContentInitializer
{
    @Override
    public void init(final AbstractGameClientInstance clientInstance) throws Exception {
        BinaryDocumentManager.getInstance().foreach(new MonsterTypeBinaryData(), new LoadProcedure<MonsterTypeBinaryData>() {
            @Override
            public void load(final MonsterTypeBinaryData bbs) {
                final int familyId = bbs.getId();
                final int parentFamilyId = bbs.getParentId();
                final byte monsterFamilyType = bbs.getType();
                final MonsterFamily family = new MonsterFamily(familyId, parentFamilyId, MonsterFamilyType.fromId(monsterFamilyType));
                MonsterFamilyManager.getInstance().addMonsterFamily(family);
            }
        });
        BinaryDocumentManager.getInstance().foreach(new MonsterTypeDungeonBinaryData(), new LoadProcedure<MonsterTypeDungeonBinaryData>() {
            @Override
            public void load(final MonsterTypeDungeonBinaryData bbs) {
                final int id = bbs.getId();
                final int dungeonId = bbs.getDungeonId();
                final int familyId = bbs.getFamilyId();
                short level = bbs.getLevel();
                if (dungeonId != 0) {
                    final DungeonDefinition dungeon = DungeonManager.INSTANCE.getDungeon(dungeonId);
                    if (dungeon != null) {
                        final PartyOccupation occupation = new PvePartyOccupation(id, dungeonId, PartyOccupationType.DUNGEON, dungeon.getMinLevel(), familyId);
                        PartyOccupationManager.INSTANCE.registerPartyOccupation(occupation);
                        if (level == 0) {
                            level = (short)(dungeon.getMinLevel() - 5);
                        }
                    }
                }
                if (level >= 0) {
                    final PartyOccupation occupation2 = new PvePartyOccupation(id, familyId, PartyOccupationType.MONSTER, level, familyId);
                    PartyOccupationManager.INSTANCE.registerPartyOccupation(occupation2);
                }
            }
        });
        clientInstance.fireContentInitializerDone(this);
    }
    
    @Override
    public String getName() {
        return WakfuTranslator.getInstance().getString("contentLoader.monsterFamily");
    }
}
