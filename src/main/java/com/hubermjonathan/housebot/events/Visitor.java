package com.hubermjonathan.housebot.events;

import com.hubermjonathan.housebot.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import redis.clients.jedis.Jedis;

import java.time.OffsetDateTime;
import java.util.TimerTask;

public class Visitor extends TimerTask {
    Jedis jedis;
    Guild guild;

    public Visitor(Guild guild) {
        this.jedis = Constants.JEDIS;
        this.guild = guild;
    }

    @Override
    public void run() {
        Role residentRole = guild.getRoleById(jedis.get(Constants.JEDIS_RESIDENT_ROLE_ID));

        for (Member member : guild.loadMembers().get()) {
            if (member.getUser().isBot()) continue;

            if (!member.getRoles().contains(residentRole) && member.getTimeJoined().isBefore(OffsetDateTime.now().minusHours(12))) {
                member.kick().queue();
            }
        }
    }
}
