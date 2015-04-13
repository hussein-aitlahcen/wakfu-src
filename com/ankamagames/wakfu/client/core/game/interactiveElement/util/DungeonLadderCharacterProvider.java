package com.ankamagames.wakfu.client.core.game.interactiveElement.util;

import com.ankamagames.wakfu.common.game.dungeon.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class DungeonLadderCharacterProvider extends CharacterStatueProvider
{
    private DungeonLadderResultCharacter m_characterData;
    private final BinarSerialPart SHARED_DATAS;
    
    public DungeonLadderCharacterProvider(final CharacterStatue statue, final String animName, final int equipmentGfxId) {
        super(statue, animName);
        this.SHARED_DATAS = new BinarSerialPart() {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final boolean hasData = buffer.get() == 1;
                if (hasData) {
                    DungeonLadderCharacterProvider.this.m_characterData = DungeonLadderResultCharacter.fromBuild(buffer);
                }
                else {
                    DungeonLadderCharacterProvider.this.m_characterData = null;
                }
                DungeonLadderCharacterProvider.this.getStatue().initialize();
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => pas de s\u00e9rialisation");
            }
        };
        if (equipmentGfxId != 0) {
            this.putEquipmentGfxId(EquipmentPosition.ACCESSORY.m_id, equipmentGfxId);
        }
    }
    
    @Override
    public BinarSerialPart getSynchronizationPart() {
        return this.SHARED_DATAS;
    }
    
    @Override
    public AbstractCharacterData getCharacterData() {
        return this.m_characterData;
    }
    
    public void unserialize(final ByteBuffer buffer) {
        final boolean hasData = buffer.get() == 1;
        if (hasData) {
            this.m_characterData = DungeonLadderResultCharacter.fromBuild(buffer);
        }
        else {
            this.m_characterData = null;
        }
        this.getStatue().initialize();
    }
}
