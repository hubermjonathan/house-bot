package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.models.AdminCommand;
import com.hubermjonathan.housebot.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.ChannelManager;
import redis.clients.jedis.Jedis;

import java.util.EnumSet;

public class Create extends AdminCommand {
    Jedis jedis;

    public Create(String command) {
        super(command);

        this.jedis = Constants.JEDIS;
    }

    @Override
    public void execute() throws Exception {
        Guild guild = getEvent().getGuild();
        TextChannel oldTextChannel = null;

        for (Role role : guild.getRoles()) {
            if (role.isPublicRole() || role.isManaged()) {
                continue;
            }

            role.delete().queue();
        }

        for (Category category : guild.getCategories()) {
            category.delete().queue();
        }

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

        jedis.flushDB();

        Role residentRole = guild.createRole()
                .setName(Constants.RESIDENT_ROLE_NAME)
                .setColor(Integer.parseInt("02c39a", 16))
                .setHoisted(true)
                .setPermissions(Permission.VOICE_MOVE_OTHERS)
                .complete();

        Category mainCategory = guild.createCategory(Constants.MAIN_CATEGORY_NAME).complete();
        if (oldTextChannel != null) {
            oldTextChannel.getManager()
                    .setParent(mainCategory)
                    .setName(Constants.MAIN_TEXT_CHANNEL_NAME)
                    .setTopic(Constants.MAIN_TEXT_CHANNEL_TOPIC)
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
        TextChannel botTextChannel = mainCategory.createTextChannel(Constants.BOT_CHANNEL_NAME)
                .setTopic(Constants.BOT_CHANNEL_TOPIC)
                .complete();
        VoiceChannel mainVoiceChannel = mainCategory.createVoiceChannel(Constants.MAIN_VOICE_CHANNEL_NAME).complete();

        Category roomsCategory = guild.createCategory(Constants.ROOMS_CATEGORY_NAME).complete();
        TextChannel roomsTextChannel = roomsCategory.createTextChannel(Constants.ROOMS_TEXT_CHANNEL_NAME)
                .setTopic(Constants.ROOMS_TEXT_CHANNEL_TOPIC)
                .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.MESSAGE_WRITE))
                .complete();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(Constants.ROOMS_TEXT_CHANNEL_TITLE).setDescription(Constants.ROOMS_TEXT_CHANNEL_MESSAGE);
        Message message = roomsTextChannel.sendMessage(embedBuilder.build()).complete();
        message.addReaction(Constants.LOCK).queue();
        message.addReaction(Constants.UNLOCK).queue();
        message.addReaction(Constants.KICK).queue();
        message.addReaction(Constants.FIRE).queue();

        jedis.set(Constants.JEDIS_RESIDENT_ROLE_ID, residentRole.getId());
        jedis.set(Constants.JEDIS_BOT_CHANNEL_ID, botTextChannel.getId());
        jedis.set(Constants.JEDIS_MAIN_VOICE_CHANNEL_ID, mainVoiceChannel.getId());
        jedis.set(Constants.JEDIS_ROOMS_CATEGORY_ID, roomsCategory.getId());
        jedis.set(Constants.JEDIS_ROOMS_CHANNEL_ID, roomsTextChannel.getId());
    }
}
