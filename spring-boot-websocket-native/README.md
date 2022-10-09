# Spring Boot 集成 WebSocket

## 原生注解

### 引入依赖

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### 配置文件

```java
@Configuration
public class WebSocketConfiguration {
    /**
     * 	注入ServerEndpointExporter，
     * 	这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```

### 处理消息

```java
@Component
@Slf4j
@ServerEndpoint("/websocket/{userId}")
public class WebSocket {

    /**
     * 线程安全的无序的集合
     */
    private static final CopyOnWriteArraySet<Session> SESSIONS = new CopyOnWriteArraySet<>();

    /**
     * 存储在线连接数
     */
    private static final Map<String, Session> SESSION_POOL = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            SESSIONS.add(session);
            SESSION_POOL.put(userId, session);
            log.info("【WebSocket消息】有新的连接，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            SESSIONS.remove(session);
            log.info("【WebSocket消息】连接断开，总数为：" + SESSIONS.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【WebSocket消息】收到客户端消息：" + message);
    }

    /**
     * 此为广播消息
     *
     * @param message 消息
     */
    public void sendAllMessage(String message) {
        log.info("【WebSocket消息】广播消息：" + message);
        for (Session session : SESSIONS) {
            try {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息
     *
     * @param userId  用户编号
     * @param message 消息
     */
    public void sendOneMessage(String userId, String message) {
        Session session = SESSION_POOL.get(userId);
        if (session != null && session.isOpen()) {
            try {
                synchronized (session) {
                    log.info("【WebSocket消息】单点消息：" + message);
                    session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息(多人)
     *
     * @param userIds 用户编号列表
     * @param message 消息
     */
    public void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session session = SESSION_POOL.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    log.info("【WebSocket消息】单点消息：" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

+ @ServerEndpoint：将目前的类定义成一个websocket服务器端，注解的值将被用于监听用户连接的终端访问URL地址，客户端可以通过这个URL来连接到WebSocket服务器端
+ @OnOpen：当WebSocket建立连接成功后会触发这个注解修饰的方法。
+ @OnClose：当WebSocket建立的连接断开后会触发这个注解修饰的方法。
+ @OnMessage：当客户端发送消息到服务端时，会触发这个注解修改的方法。
+ @OnError：当WebSocket建立连接时出现异常会触发这个注解修饰的方法。

### WebSocket测试

![原生注解-客户端](https://c2.im5i.com/2022/09/24/HnSjn.png)
![原生注解-服务端](https://c2.im5i.com/2022/09/24/HnNP1.png)