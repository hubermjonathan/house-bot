package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.models.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class Name extends Command {

    public Name(String command) {
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

        Message roomMessage = category.getTextChannels().get(0).retrievePinnedMessages().complete().get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder(roomMessage.getEmbeds().get(0));
        embedBuilder.setTitle(String.join(" ", getArgs()));
        roomMessage.editMessage(embedBuilder.build()).queue();
        category.getVoiceChannels().get(0).getManager().setName(String.join(" ", getArgs())).queue();
    }
}
