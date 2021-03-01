package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.models.AdminCommand;
import com.hubermjonathan.housebot.Constants;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import redis.clients.jedis.Jedis;

import java.util.EnumSet;
import java.util.HashMap;

public class Room extends AdminCommand {
    Jedis jedis;

    public Room(String command) {
        super(command);

        this.jedis = Constants.JEDIS;
    }

    @Override
    public void execute() throws Exception {
        Guild guild = getEvent().getGuild();
        Member member = getEvent().getMessage().getMentionedMembers().get(1);
        Role residentRole = guild.getRoleById(jedis.get(Constants.JEDIS_RESIDENT_ROLE_ID));

        if (!member.getRoles().contains(residentRole)) {
            guild.addRoleToMember(member, residentRole).queue();

            Category category = guild.getCategoryById(jedis.get(Constants.JEDIS_ROOMS_CATEGORY_ID));
            VoiceChannel voiceChannel = category.createVoiceChannel(member.getUser().getName() + "'s room")
                    .addRolePermissionOverride(guild.getPublicRole().getIdLong(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                    .addMemberPermissionOverride(member.getIdLong(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS), null)
                    .complete();

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(Constants.JEDIS_ROOM_OWNER_ID, member.getId());
            hashMap.put(Constants.JEDIS_ROOM_ROOMMATE_ID, "0");
            hashMap.put(Constants.JEDIS_ROOM_BURNING, Constants.FALSE);
            jedis.hmset(voiceChannel.getId(), hashMap);
            jedis.set(member.getId(), voiceChannel.getId());
        } else {
            guild.removeRoleFromMember(member, residentRole).queue();

            VoiceChannel voiceChannel = guild.getVoiceChannelById(jedis.get(member.getId()));
            jedis.del(voiceChannel.getId());
            jedis.del(member.getId());
            voiceChannel.delete().queue();
        }
    }
}
