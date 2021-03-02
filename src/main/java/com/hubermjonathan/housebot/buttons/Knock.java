package com.hubermjonathan.housebot.buttons;

import com.hubermjonathan.housebot.models.Button;

public class Knock extends Button {
    public Knock(String name, boolean isOwnerControlled) {
        super(name, isOwnerControlled);
    }

    @Override
    public void execute() throws Exception {
        getOwner().getUser().openPrivateChannel().complete().sendMessage(getEvent().getMember().getAsMention() + " is knocking on your door!").queue();
    }
}
