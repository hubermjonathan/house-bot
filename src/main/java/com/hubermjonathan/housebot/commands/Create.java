package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.models.AdminCommand;
import com.hubermjonathan.housebot.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.ChannelManager;

import java.util.EnumSet;

public class Create extends AdminCommand {
    public Create(String command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        Guild guild = getEvent().getGuild();

        for (Role role : guild.getRoles()) {
            if (role.isPublicRole() || role.isManaged()) {
                continue;
            }

            role.delete().queue();
        }

        for (Category category : guild.getCategories()) {
            category.delete().queue();
        }

        TextChannel oldTextChannel = null;
        for (TextChannel textChannel : guild.getTextChannels()) {
            if (getArgs().length == 1 && textChannel.getId().equals(getArgs()[0])) {
                oldTextChannel = textChannel;
                oldTextChannel.getManager().reset(ChannelManager.PERMISSION).queue();
                continue;
            }

            textChannel.delete().queue();
        }

        for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
            voiceChannel.delete().queue();
        }

        guild.getPublicRole().getManager().revokePermissions(Permission.NICKNAME_CHANGE).queue();

        Role residentRole = guild.createRole()
                .setName(Constants.RESIDENT_ROLE_NAME)
                .setColor(Integer.parseInt("02c39a", 16))
                .setHoisted(true)
                .complete();

        Category mainCategory = guild.createCategory(Constants.MAIN_CATEGORY_NAME).complete();
        if (oldTextChannel != null) {
            oldTextChannel.getManager()
                    .setParent(mainCategory)
                    .setName(Constants.MAIN_TEXT_CHANNEL_NAME)
                    .setTopic(Constants.MAIN_TEXT_CHANNEL_TOPIC)
                    .clearOverridesAdded()
                    .putPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.MESSAGE_READ))
                    .putPermissionOverride(residentRole, EnumSet.of(Permission.MESSAGE_READ), null)
                    .queue();
        } else {
            mainCategory.createTextChannel(Constants.MAIN_TEXT_CHANNEL_NAME)
                    .setTopic(Constants.MAIN_TEXT_CHANNEL_TOPIC)
                    .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.MESSAGE_READ))
                    .addRolePermissionOverride(residentRole.getIdLong(), EnumSet.of(Permission.MESSAGE_READ), null)
                    .queue();
        }
        mainCategory.createTextChannel(Constants.BOT_CHANNEL_NAME)
                .setTopic(Constants.BOT_CHANNEL_TOPIC)
                .complete();
        mainCategory.createVoiceChannel(Constants.MAIN_VOICE_CHANNEL_NAME).complete();
        mainCategory.createVoiceChannel(Constants.MOVIE_VOICE_CHANNEL_NAME).complete();
    }
}
