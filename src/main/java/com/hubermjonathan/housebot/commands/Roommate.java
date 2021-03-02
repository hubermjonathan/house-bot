package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.EnumSet;

public class Roommate extends Command {
    public Roommate(String command) {
        super(command);
    }

    @Override
    public void execute() throws Exception {
//        String roomId = jedis.get(getEvent().getAuthor().getId());
//        VoiceChannel voiceChannel = getEvent().getGuild().getVoiceChannelById(roomId);
//
//        if (jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_BURNING).equals(Constants.TRUE)) {
//            getEvent().getMessage().addReaction(Constants.DENY).queue();
//            throw new Exception();
//        }
//
//        if (getEvent().getMessage().getMentionedMembers().size() == 1) {
//            Member member = getEvent().getGuild().retrieveMemberById(jedis.hget(roomId, Constants.JEDIS_ROOM_ROOMMATE_ID)).complete();
//            voiceChannel.getManager().removePermissionOverride(member).queue();
//            jedis.hset(roomId, Constants.JEDIS_ROOM_ROOMMATE_ID, "0");
//        } else {
//            Member member = getEvent().getMessage().getMentionedMembers().get(1);
//            Role residentRole = getEvent().getGuild().getRoleById(jedis.get(Constants.JEDIS_RESIDENT_ROLE_ID));
//
//            if (member.getId().equals(getEvent().getAuthor().getId()) || !member.getRoles().contains(residentRole)) {
//                getEvent().getMessage().addReaction(Constants.DENY).queue();
//                throw new Exception();
//            }
//
//            voiceChannel.getManager().putPermissionOverride(member, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS), null).queue();
//            jedis.hset(roomId, Constants.JEDIS_ROOM_ROOMMATE_ID, member.getId());
//        }
    }
}
