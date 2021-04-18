package io.github.codeutilities.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CodeUtilsConfig::getScreen;
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> 0bee843 (Initial commit)
