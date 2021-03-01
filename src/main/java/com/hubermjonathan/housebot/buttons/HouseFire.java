package com.hubermjonathan.housebot.buttons;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Button;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import redis.clients.jedis.Jedis;

import java.util.EnumSet;
import java.util.Timer;
import java.util.TimerTask;

public class HouseFire extends Button {
    Jedis jedis;

    public HouseFire(String name) {
        super(name);

        this.jedis = Constants.JEDIS;
    }

    @Override
    public void execute() throws Exception {
        VoiceChannel voiceChannel = getEvent().getGuild().getVoiceChannelById(jedis.get(getEvent().getMember().getId()));

        if (jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_BURNING).equals(Constants.TRUE)) return;

        jedis.hset(voiceChannel.getId(), Constants.JEDIS_ROOM_BURNING, Constants.TRUE);
        voiceChannel.getManager().setName(Constants.FIRE + " " + voiceChannel.getName()).queue();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Member member : voiceChannel.getMembers()) {
                    getEvent().getGuild().moveVoiceMember(member, null).queue();
                }

                if (jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_ROOMMATE_ID).equals("0")) {
                    voiceChannel.getManager()
                            .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                            .putPermissionOverride(getEvent().getMember(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                            .queue();
                } else {
                    Member member = getEvent().getGuild().retrieveMemberById(jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_ROOMMATE_ID)).complete();

                    voiceChannel.getManager()
                            .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                            .putPermissionOverride(getEvent().getMember(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                            .putPermissionOverride(member, null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                            .queue();
                }
            }
        }, 1000 * 60);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_ROOMMATE_ID).equals("0")) {
                    voiceChannel.getManager()
                            .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                            .putPermissionOverride(getEvent().getMember(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS), null)
                            .queue();
                } else {
                    Member member = getEvent().getGuild().retrieveMemberById(jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_ROOMMATE_ID)).complete();

                    voiceChannel.getManager()
                            .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS))
                            .putPermissionOverride(getEvent().getMember(), EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS), null)
                            .putPermissionOverride(member, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS), null)
                            .queue();
                }

                voiceChannel.getManager().setName(voiceChannel.getName().substring(2)).queue();
                jedis.hset(voiceChannel.getId(), Constants.JEDIS_ROOM_BURNING, Constants.FALSE);
            }
        }, (1000 * 60) + (1000 * 60 * 25));
    }
}
