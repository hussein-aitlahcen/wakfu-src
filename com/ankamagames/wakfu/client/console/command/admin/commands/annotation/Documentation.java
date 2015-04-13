package com.ankamagames.wakfu.client.console.command.admin.commands.annotation;

import java.lang.annotation.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.admin.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Documentation {
    String commandName();
    
    String commandDescription();
    
    String commandParameters();
    
    AdminRightsGroup[] commandRights();
    
    boolean commandObsolete();
}
