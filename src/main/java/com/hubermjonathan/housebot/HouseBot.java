package com.hubermjonathan.housebot;

import com.hubermjonathan.housebot.buttons.*;
import com.hubermjonathan.housebot.commands.*;
import com.hubermjonathan.housebot.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.Timer;

public class HouseBot {
    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = JDABuilder.createDefault(System.getenv("TOKEN"))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
        jda.awaitReady();

        jda.addEventListener(new Create("create"));
        jda.addEventListener(new Emoji("emoji"));
        jda.addEventListener(new Image("image"));
        jda.addEventListener(new Name("name"));
        jda.addEventListener(new Room("room"));
        jda.addEventListener(new Text("text"));

        jda.addEventListener(new Cancel(Constants.CANCEL, false));
        jda.addEventListener(new Confirm(Constants.CONFIRM));
        jda.addEventListener(new Deny(Constants.DENY));
        jda.addEventListener(new HouseFire(Constants.FIRE));
        jda.addEventListener(new Kick(Constants.KICK));
        jda.addEventListener(new Knock(Constants.KNOCK, false));
        jda.addEventListener(new Lock(Constants.LOCK));
        jda.addEventListener(new Unlock(Constants.UNLOCK));

        Timer timer = new Timer();
        timer.schedule(new Visitor(jda.getGuilds().get(0)), 1000 * 60 * 60, 1000 * 60 * 60);
    }
}
