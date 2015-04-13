package com.ankamagames.wakfu.common.game.characterInfo.nameValidation;

import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;

public interface NameValidator
{
    NameCheckerResult checkValidity(@NotNull String p0);
}
