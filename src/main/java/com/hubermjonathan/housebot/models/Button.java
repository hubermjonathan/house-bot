package com.hubermjonathan.housebot.models;

import com.hubermjonathan.housebot.Constants;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

public abstract class Button extends ListenerAdapter {
    private final String name;
    private GuildMessageReactionAddEvent event;

    public Button(String name) {
        this.name = name;
    }

    public GuildMessageReactionAddEvent getEvent() {
        return event;
    }

    public void setEvent(GuildMessageReactionAddEvent event) {
        this.event = event;
    }

    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {
        User user = event.getUser();
        TextChannel textChannel = event.getChannel();
        Jedis jedis = Constants.JEDIS;
        String emoji = event.getReactionEmote().getEmoji();
        MessageReaction messageReaction = event.getReaction();

        if (user.isBot() || !textChannel.getId().equals(jedis.get(Constants.JEDIS_ROOMS_CHANNEL_ID))) {
            return;
        }

        if (!emoji.equals(name)) {
            messageReaction.removeReaction(event.getUser()).queue();
            return;
        }

        setEvent(event);

        try {
            messageReaction.removeReaction(event.getUser()).queue();
            execute();
        } catch (Exception ignored) {
        }
    }

    public abstract void execute() throws Exception;
}
