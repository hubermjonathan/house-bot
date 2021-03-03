package com.hubermjonathan.housebot.buttons;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class Knock extends Button {
    public Knock(String name, boolean isOwnerControlled) {
        super(name, isOwnerControlled);
    }

    @Override
    public void execute() throws Exception {
        getEvent().getChannel().getIterableHistory().complete();
        for (Message message : getEvent().getChannel().getIterableHistory().complete()) {
            for (Member member : message.getMentionedMembers()) {
                if (member.getId().equals(getOwner().getId())) {
                    throw new Exception();
                }
            }
        }

        Message message = getEvent().getChannel().sendMessage("hey " + getOwner().getAsMention() + ", " + getEvent().getMember().getAsMention() + " is knocking on your door!").complete();
        message.addReaction(Constants.CONFIRM).queue();
        message.addReaction(Constants.DENY).queue();
        message.addReaction(Constants.CANCEL).queue();
    }
}
