package com.starimmortal.websocket.message;

import com.starimmortal.websocket.constant.WebSocketConstant;
import com.starimmortal.websocket.pojo.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket操作服务实现类
 *
 * @author william@StarImmortal
 * @date 2022/09/23
 */
@Slf4j
public class WebSocketImpl implements WebSocket {
    /**
     * 在线连接数（线程安全）
     */
    private final AtomicInteger connectionCount = new AtomicInteger(0);

    /**
     * 线程安全的无序集合（存储会话）
     */
    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void handleOpen(WebSocketSession session) {
        sessions.add(session);
        int count = connectionCount.incrementAndGet();
        log.info("a new connection opened，current online count：{}", count);
    }

    @Override
    public void handleClose(WebSocketSession session) {
        sessions.remove(session);
        int count = connectionCount.decrementAndGet();
        log.info("a new connection closed，current online count：{}", count);
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) {
        // 只处理前端传来的文本消息，并且直接丢弃了客户端传来的消息
        log.info("received a message：{}", message);
    }

    @Override
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        this.sendMessage(session, new TextMessage(message));
    }

    @Override
    public void sendMessage(String userId, TextMessage message) throws IOException {
        Optional<WebSocketSession> userSession = sessions.stream().filter(session -> {
            if (!session.isOpen()) {
                return false;
            }
            Map<String, Object> attributes = session.getAttributes();
            if (!attributes.containsKey(WebSocketConstant.USER_KEY)) {
                return false;
            }
            UserDO user = (UserDO) attributes.get(WebSocketConstant.USER_KEY);
            return user.getId().equals(userId);
        }).findFirst();
        if (userSession.isPresent()) {
            userSession.get().sendMessage(message);
        }
    }

    @Override
    public void sendMessage(String userId, String message) throws IOException {
        this.sendMessage(userId, new TextMessage(message));
    }

    @Override
    public void sendMessage(WebSocketSession session, TextMessage message) throws IOException {
        session.sendMessage(message);
    }

    @Override
    public void broadCast(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                continue;
            }
            this.sendMessage(session, message);
        }
    }

    @Override
    public void broadCast(TextMessage message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                continue;
            }
            session.sendMessage(message);
        }
    }

    @Override
    public void handleError(WebSocketSession session, Throwable error) {
        log.error("websocket error：{}，session id：{}", error.getMessage(), session.getId());
        log.error("", error);
    }

    @Override
    public Set<WebSocketSession> getSessions() {
        return sessions;
    }

    @Override
    public int getConnectionCount() {
        return connectionCount.get();
    }
}
