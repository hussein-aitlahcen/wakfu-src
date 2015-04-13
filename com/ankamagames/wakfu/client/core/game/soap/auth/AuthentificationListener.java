package com.ankamagames.wakfu.client.core.game.soap.auth;

import java.util.*;

public interface AuthentificationListener
{
    void onSessionId(Map<String, List<String>> p0);
    
    void onError();
}
