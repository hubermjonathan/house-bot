package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class Emoji extends Command {
    public Emoji(String command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        Guild guild = getEvent().getGuild();

        if (getEvent().getMessage().getAttachments().size() != 1 && getArgs().length == 0) {
            getEvent().getMessage().addReaction(Constants.DENY).queue();
            throw new Exception();
        }

        if (getEvent().getMessage().getAttachments().size() == 1) {
            Message message = getEvent().getMessage();
            Message.Attachment attachment = message.getAttachments().get(0);

            if (!attachment.getFileExtension().equals("png")) {
                message.addReaction(Constants.DENY).queue();
                throw new Exception();
            }

            guild.createEmote(attachment.getFileName().substring(0, attachment.getFileName().length() - 4), attachment.retrieveAsIcon().get()).queue();
        } else {
            guild.getEmoteById(getArgs()[0].replaceAll("[^0-9]", "")).delete().queue();
        }
    }
}
