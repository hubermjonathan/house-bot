package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class Image extends Command {
    public Image(String command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        Category category = null;
        for (TextChannel textChannel : getEvent().getGuild().getTextChannels()) {
            if (textChannel.getName().equals(getEvent().getMember().getEffectiveName() + "s-door")) {
                category = textChannel.getParent();
                break;
            }
        }

        if ((getEvent().getMessage().getAttachments().size() != 1 && getArgs().length == 0) || (getEvent().getMessage().getAttachments().size() == 1 && !getEvent().getMessage().getAttachments().get(0).isImage())) {
            getEvent().getMessage().addReaction(Constants.DENY).queue();
            throw new Exception();
        }

        Message roomMessage = category.getTextChannels().get(0).retrievePinnedMessages().complete().get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder(roomMessage.getEmbeds().get(0));

        if (getArgs().length == 1) {
            embedBuilder.setImage(getArgs()[0]);
        } else {
            embedBuilder.setImage(getEvent().getMessage().getAttachments().get(0).getUrl());
        }

        roomMessage.editMessage(embedBuilder.build()).queue();
    }
}
