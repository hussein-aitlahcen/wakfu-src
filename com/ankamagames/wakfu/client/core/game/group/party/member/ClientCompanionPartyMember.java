package com.ankamagames.wakfu.client.core.game.group.party.member;

import com.ankamagames.wakfu.common.game.group.member.*;
import com.ankamagames.wakfu.common.game.companion.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;

public class ClientCompanionPartyMember extends CompanionPartyMemberModel
{
    @Override
    public String getName() {
        final String name = super.getName();
        if (name != null && !name.isEmpty()) {
            return name;
        }
        final CompanionModel companionModel = this.getCompanionModel();
        if (companionModel == null) {
            return name;
        }
        final short breedId = companionModel.getBreedId();
        final MonsterBreed breed = MonsterBreedManager.getInstance().getBreedFromId(breedId);
        if (breed == null) {
            return name;
        }
        return breed.getName();
    }
}
