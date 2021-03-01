package com.hubermjonathan.housebot.buttons;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Button;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;
import redis.clients.jedis.Jedis;

import java.util.EnumSet;

public class Lock extends Button {
    Jedis jedis;

    public Lock(String name) {
        super(name);

        this.jedis = Constants.JEDIS;
    }

    @Override
    public void execute() throws Exception {
        VoiceChannel voiceChannel = getEvent().getGuild().getVoiceChannelById(jedis.get(getEvent().getMember().getId()));

        if (jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_BURNING).equals(Constants.TRUE)) return;

        voiceChannel.getManager().putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS)).queue();
    }
}
