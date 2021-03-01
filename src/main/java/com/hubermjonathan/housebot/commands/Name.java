package com.hubermjonathan.housebot.commands;

import com.hubermjonathan.housebot.Constants;
import com.hubermjonathan.housebot.models.Command;
import net.dv8tion.jda.api.entities.VoiceChannel;
import redis.clients.jedis.Jedis;

public class Name extends Command {
    Jedis jedis;

    public Name(String command) {
        super(command);

        this.jedis = Constants.JEDIS;
    }

    @Override
    public void execute() throws Exception {
        VoiceChannel voiceChannel = getEvent().getGuild().getVoiceChannelById(jedis.get(getEvent().getMember().getId()));

        if (jedis.hget(voiceChannel.getId(), Constants.JEDIS_ROOM_BURNING).equals(Constants.TRUE)) {
            getEvent().getMessage().addReaction(Constants.DENY).queue();
            throw new Exception();
        }

        voiceChannel.getManager().setName(String.join(" ", getArgs())).queue();
    }
}
