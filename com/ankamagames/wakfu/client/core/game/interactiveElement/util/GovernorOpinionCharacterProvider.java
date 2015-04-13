package com.ankamagames.wakfu.client.core.game.interactiveElement.util;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.nation.government.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.actor.characterActorHelpers.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public class GovernorOpinionCharacterProvider extends CharacterStatueProvider
{
    private GovernmentInfo m_characterData;
    private final BinarSerialPart PART;
    
    public GovernorOpinionCharacterProvider(final CharacterStatue statue, final String animName) {
        super(statue, animName);
        this.PART = new BinarSerialPart() {
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final boolean hasData = buffer.get() == 1;
                if (hasData) {
                    GovernorOpinionCharacterProvider.this.m_characterData = GovernmentInfo.fromBuild(buffer);
                }
                GovernorOpinionCharacterProvider.this.clearEquipments();
                if (hasData) {
                    NationRankEquipmentHelper.foreachEquipement(NationRank.GOVERNOR, GovernorOpinionCharacterProvider.this.m_characterData.getNationId(), new TObjectIntProcedure<EquipmentPosition>() {
                        @Override
                        public boolean execute(final EquipmentPosition position, final int gfxId) {
                            GovernorOpinionCharacterProvider.this.putEquipmentGfxId(position.m_id, gfxId);
                            return true;
                        }
                    });
                }
                GovernorOpinionCharacterProvider.this.getStatue().initialize();
            }
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                throw new UnsupportedOperationException("La synchronisation du contenu de l'objet est faite depuis le serveur => pas de s\u00e9rialisation");
            }
        };
    }
    
    @Override
    public BinarSerialPart getSynchronizationPart() {
        return this.PART;
    }
    
    @Override
    public AbstractCharacterData getCharacterData() {
        return this.m_characterData;
    }
}
