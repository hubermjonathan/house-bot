package com.hubermjonathan.housebot;

import redis.clients.jedis.Jedis;

public class Constants {
    public static final String TRUE = "1";
    public static final String FALSE = "0";

    public static final Jedis JEDIS = new Jedis(System.getenv("REDIS_URL"));
    public static final String JEDIS_RESIDENT_ROLE_ID = "RESIDENT_ROLE_ID";
    public static final String JEDIS_BOT_CHANNEL_ID = "BOT_CHANNEL_ID";
    public static final String JEDIS_MAIN_VOICE_CHANNEL_ID = "MAIN_VOICE_CHANNEL_ID";
    public static final String JEDIS_ROOMS_CATEGORY_ID = "ROOMS_CATEGORY_ID";
    public static final String JEDIS_ROOMS_CHANNEL_ID = "ROOMS_CHANNEL_ID";
    public static final String JEDIS_ROOM_OWNER_ID = "ROOM_OWNER_ID";
    public static final String JEDIS_ROOM_ROOMMATE_ID = "ROOM_ROOMMATE_ID";
    public static final String JEDIS_ROOM_BURNING = "ROOM_BURNING";

    public static final String BOT_OWNER_ID = "196141424318611457";

    public static final String RESIDENT_ROLE_NAME = "resident";

    public static final String MAIN_CATEGORY_NAME = "__________🏠__________";
    public static final String MAIN_TEXT_CHANNEL_NAME = "living-room";
    public static final String MAIN_TEXT_CHANNEL_TOPIC = "for chatting with residents of the house";
    public static final String BOT_CHANNEL_NAME = "bot-commands";
    public static final String BOT_CHANNEL_TOPIC = "for chatting with the bots of the house";
    public static final String MAIN_VOICE_CHANNEL_NAME = "couch";

    public static final String ROOMS_CATEGORY_NAME = "__________🚪__________";
    public static final String ROOMS_TEXT_CHANNEL_NAME = "room-controls";
    public static final String ROOMS_TEXT_CHANNEL_TOPIC = "for controlling your room";
    public static final String ROOMS_TEXT_CHANNEL_TITLE = "controls";
    public static final String ROOMS_TEXT_CHANNEL_MESSAGE = "```🔒: lock door\n🔑: unlock door\n✌: empty room\n🔥: house fire```";

    public static final String CONFIRM = "\uD83D\uDC4D";
    public static final String DENY = "\uD83D\uDC4E";
    public static final String LOCK = "\uD83D\uDD12";
    public static final String UNLOCK = "\uD83D\uDD11";
    public static final String KICK = "✌";
    public static final String FIRE = "\uD83D\uDD25";
}
