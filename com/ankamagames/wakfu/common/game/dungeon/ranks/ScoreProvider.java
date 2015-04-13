package com.ankamagames.wakfu.common.game.dungeon.ranks;

public interface ScoreProvider
{
    int getTotalScore();
    
    int getMonsterScore();
    
    int getChallengeScore();
}
