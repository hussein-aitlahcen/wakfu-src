package com.ankamagames.wakfu.client.core.game.protector.inventory;

import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.wakfu.common.game.protector.*;

public class ProtectorChallengeItemView extends ProtectorMerchantItemView
{
    private final int m_challengeId;
    
    public ProtectorChallengeItemView(final ProtectorMerchantInventoryItem merchantItem, final int challengeId) {
        super(merchantItem);
        this.m_challengeId = challengeId;
    }
    
    @Override
    public String getName() {
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.m_challengeId);
        return model.getChallengeTitle();
    }
    
    @Override
    public ProtectorWalletContext getWalletContext() {
        return ProtectorWalletContext.CHALLENGE;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        final ChallengeDataModel model = ChallengeManager.getInstance().getChallengeDataModel(this.m_challengeId);
        if (model == null) {
            ProtectorChallengeItemView.m_logger.error((Object)("Impossible de cr\u00e9er la vue pour le challenge id=" + this.m_challengeId));
            return null;
        }
        if (fieldName.equals("description")) {
            return model.getChallengeDescription();
        }
        if (fieldName.equals("title")) {
            return this.getName();
        }
        if (fieldName.equals("nameWithRemainingTime")) {
            final Object s = this.getRemainingTimeString();
            if (s == null) {
                return this.getName();
            }
            return new StringBuilder(this.getName()).append(" (").append(s).append(")");
        }
        else {
            if (fieldName.equals("nameWithDuration")) {
                return new StringBuilder(this.getName()).append(" (").append(this.getDurationString()).append(")");
            }
            if (fieldName.equals("name")) {
                return this.getName();
            }
            if (fieldName.equals("iconUrl")) {
                return model.getIconUrl();
            }
            return super.getFieldValue(fieldName);
        }
    }
    
    public int getChallengeId() {
        return this.m_challengeId;
    }
}
