package com.hubermjonathan.housebot.models;

import com.hubermjonathan.housebot.Constants;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

import java.util.Arrays;

public abstract class Command extends ListenerAdapter {
    private final String command;
    private String[] args;
    private MessageReceivedEvent event;

    public Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void setEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        User user = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String[] tokens = message.getContentRaw().split(" ");

        if (user.isBot()) return;
        if (!tokens[0].equals(String.format("<@!%s>", channel.getJDA().getSelfUser().getId()))) return;
        if (tokens.length == 1) return;
        if (!tokens[1].equals(command)) return;

        Jedis jedis = Constants.JEDIS;
        Role residentRole = event.getGuild().getRoleById(jedis.get(Constants.JEDIS_RESIDENT_ROLE_ID));

        if (!channel.getId().equals(jedis.get(Constants.JEDIS_BOT_CHANNEL_ID))) return;
        if (!event.getMember().getRoles().contains(residentRole)) return;

        setArgs(Arrays.copyOfRange(tokens, 2, tokens.length));
        setEvent(event);

        try {
            execute();
            message.addReaction(Constants.CONFIRM).queue();
        } catch (Exception ignored) {
        }
    }

    public abstract void execute() throws Exception;
}
