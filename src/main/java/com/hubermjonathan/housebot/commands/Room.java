package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.models.AdminCommand;
import com.hubermjonathan.housebot.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.EnumSet;

public class Room extends AdminCommand {
    public Room(String command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
        Guild guild = getEvent().getGuild();
        Member member = getEvent().getMessage().getMentionedMembers().get(1);

        Role residentRole = null;
        for (Role role : getEvent().getGuild().getRoles()) {
            if (role.getName().equals(Constants.RESIDENT_ROLE_NAME)) {
                residentRole = role;
                break;
            }
        }

        if (!member.getRoles().contains(residentRole)) {
            guild.addRoleToMember(member, residentRole).queue();

            Category category = guild.createCategory(Constants.ROOMS_CATEGORY_NAME).complete();
            TextChannel textChannel = category.createTextChannel(member.getEffectiveName() + "s-door")
                    .setTopic(Constants.ROOM_TEXT_CHANNEL_TOPIC)
                    .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.MESSAGE_WRITE))
                    .complete();
            category.createVoiceChannel(member.getEffectiveName() + "'s room")
                    .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                    .addMemberPermissionOverride(member.getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS), null)
                    .complete();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setTitle(member.getEffectiveName() + "'s room")
                    .setDescription("``` ```")
                    .addField(Constants.ROOM_CONTROLS_TITLE, Constants.ROOM_CONTROLS_MESSAGE, false);
            Message message = textChannel.sendMessage(embedBuilder.build()).complete();
            message.addReaction(Constants.KNOCK).queue();
            message.addReaction(Constants.LOCK).queue();
            message.addReaction(Constants.UNLOCK).queue();
            message.addReaction(Constants.KICK).queue();
            message.addReaction(Constants.FIRE).queue();

            member.modifyNickname(getArgs()[1]).queue();
        } else {
            guild.removeRoleFromMember(member, residentRole).queue();

            Category category = null;
            for (TextChannel textChannel : guild.getTextChannels()) {
                if (textChannel.getName().equals(member.getEffectiveName() + "s-door")) {
                    category = textChannel.getParent();
                    break;
                }
            }

            category.getTextChannels().get(0).delete().queue();
            category.getVoiceChannels().get(0).delete().queue();
            category.delete().queue();

            member.modifyNickname(null).queue();
        }
    }
}
