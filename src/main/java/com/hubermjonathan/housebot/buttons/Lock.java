package com.hubermjonathan.housebot.buttons;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Button;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;

import java.util.EnumSet;

public class Lock extends Button {
    public Lock(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent().getChannel().getParent();

        if (category.getTextChannels().get(0).getName().contains(Constants.FIRE)) {
            throw new Exception();
        }

        category.getVoiceChannels().get(0).getManager()
                .clearOverridesAdded()
                .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                .queue();
    }
}
