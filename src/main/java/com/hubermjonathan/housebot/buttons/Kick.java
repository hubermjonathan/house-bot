package com.hubermjonathan.housebot.buttons;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import redis.clients.jedis.Jedis;

public class Kick extends Button {
    Jedis jedis;

    public Kick(String name) {
        super(name);

        this.jedis = Constants.JEDIS;
    }

    @Override
    public void execute() throws Exception {
        VoiceChannel voiceChannel = getEvent().getGuild().getVoiceChannelById(jedis.get(getEvent().getMember().getId()));
        VoiceChannel newVoiceChannel = getEvent().getGuild().getVoiceChannelById(jedis.get(Constants.JEDIS_MAIN_VOICE_CHANNEL_ID));

        if (jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_BURNING).equals(Constants.TRUE)) return;

        for (Member member : voiceChannel.getMembers()) {
            if (member.getId().equals(getEvent().getMember().getId())) continue;
            getEvent().getGuild().moveVoiceMember(member, newVoiceChannel).queue();
        }
    }
}
