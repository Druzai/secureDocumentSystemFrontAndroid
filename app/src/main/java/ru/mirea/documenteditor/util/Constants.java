package ru.mirea.documenteditor.util;

import java.text.MessageFormat;

public class Constants {
    public static final String KEY_PREFERENCE_NAME = "documentEditorAppPreference";
    public static final String LOG_TAG = "DocumentEditor";

    public static final String BASE_DOMAIN_NAME = "192.168.100.44";
    public static final String BASE_PORT = "8080";
    public static final String BASE_URL = MessageFormat.format("http://{0}:{1}", BASE_DOMAIN_NAME, BASE_PORT);
    public static final String WEB_SOCKET_TOPIC_LISTEN = "/topic/messages";
    public static final String WEB_SOCKET_MESSAGE_SEND = "/app/send";
    public static final String WEB_SOCKET_URL = MessageFormat.format("ws://{0}:{1}/message-ws/websocket", BASE_DOMAIN_NAME, BASE_PORT);

    public static final String ACCESS_KEY = "accessKey";
    public static final String REFRESH_KEY = "refreshKey";
    public static final String USER_KEY = "userKey";
    public static final String DOCUMENT_WS_KEY = "documentWsKey";
    public static final String USER_NAME = "userName";

    public static final String TEST_MESSAGE = "Тестовое сообщение";
}
