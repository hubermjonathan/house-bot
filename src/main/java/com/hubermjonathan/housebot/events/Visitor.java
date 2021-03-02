package com.hubermjonathan.housebot.events;

import com.hubermjonathan.housebot.Constants;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.time.OffsetDateTime;
import java.util.TimerTask;

public class Visitor extends TimerTask {
    Guild guild;

    public Visitor(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void run() {
        Role residentRole = null;
        for (Role role : guild.getRoles()) {
            if (role.getName().equals(Constants.RESIDENT_ROLE_NAME)) {
                residentRole = role;
                break;
            }
        }

        for (Member member : guild.loadMembers().get()) {
            if (member.getUser().isBot()) continue;

            if (!member.getRoles().contains(residentRole) && member.getTimeJoined().isBefore(OffsetDateTime.now().minusHours(12))) {
                member.kick().queue();
            }
        }
    }
}
