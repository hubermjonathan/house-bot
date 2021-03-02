package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.models.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class Text extends Command {
    public Text(String command) {
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

        Message roomMessage = category.getTextChannels().get(0).getHistory().retrievePast(1).complete().get(0);
        EmbedBuilder embedBuilder = new EmbedBuilder(roomMessage.getEmbeds().get(0));
        embedBuilder.setDescription("```" + String.join(" ", getArgs()) + "```");
        roomMessage.editMessage(embedBuilder.build()).queue();
    }
}
