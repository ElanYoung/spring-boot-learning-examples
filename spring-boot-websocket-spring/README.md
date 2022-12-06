# Spring Boot 集成 WebSocket

## Spring封装

### 引入依赖

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

### 自定义处理器

> 处理器作用类似于`@RequestMapping`注解，用于处理某一个路径的`WebSocket`连接，自定义处理器需要实现`WebSocketHandler`接口。

#### WebSocket操作类

```java
public interface WebSocket {
    /**
     * 会话开始回调
     *
     * @param session 会话
     */
    void handleOpen(WebSocketSession session);

    /**
     * 会话结束回调
     *
     * @param session 会话
     */
    void handleClose(WebSocketSession session);

    /**
     * 处理消息
     *
     * @param session 会话
     * @param message 接收的消息
     */
    void handleMessage(WebSocketSession session, String message);

    /**
     * 发送消息
     *
     * @param session 当前会话
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(WebSocketSession session, String message) throws IOException;

    /**
     * 发送消息
     *
     * @param userId  用户id
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(String userId, TextMessage message) throws IOException;

    /**
     * 发送消息
     *
     * @param userId  用户id
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(String userId, String message) throws IOException;

    /**
     * 发送消息
     *
     * @param session 当前会话
     * @param message 要发送的消息
     * @throws IOException 发送io异常
     */
    void sendMessage(WebSocketSession session, TextMessage message) throws IOException;

    /**
     * 广播
     *
     * @param message 字符串消息
     * @throws IOException 异常
     */
    void broadCast(String message) throws IOException;

    /**
     * 广播
     *
     * @param message 文本消息
     * @throws IOException 异常
     */
    void broadCast(TextMessage message) throws IOException;

    /**
     * 处理会话异常
     *
     * @param session 会话
     * @param error   异常
     */
    void handleError(WebSocketSession session, Throwable error);

    /**
     * 获得所有的 websocket 会话
     *
     * @return 所有 websocket 会话
     */
    Set<WebSocketSession> getSessions();

    /**
     * 得到当前连接数
     *
     * @return 连接数
     */
    int getConnectionCount();
}
```

#### WebSocket操作实现类

```java
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
            if (!attributes.containsKey("uid") {
                return false;
            }
            String uid = (String) attributes.get("uid");
            return uid.equals(userId);
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
```

#### 自定义WebSocket处理器

```java
public class DefaultWebSocketHandler implements WebSocketHandler {

    @Autowired
    private WebSocket webSocket;

    /**
     * 建立连接
     *
     * @param session Session
     */
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        webSocket.handleOpen(session);
    }

    /**
     * 接收消息
     *
     * @param session Session
     * @param message 消息
     */
    @Override
    public void handleMessage(@NonNull WebSocketSession session, @NonNull WebSocketMessage<?> message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            webSocket.handleMessage(session, textMessage.getPayload());
        }
    }

    /**
     * 发生错误
     *
     * @param session   Session
     * @param exception 异常
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        webSocket.handleError(session, exception);
    }

    /**
     * 关闭连接
     *
     * @param session     Session
     * @param closeStatus 关闭状态
     */
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) {
        webSocket.handleClose(session);
    }

    /**
     * 是否支持发送部分消息
     *
     * @return false
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
```

### 自定义拦截器

> 自定义处理器需要实现`HandshakeInterceptor`接口

```java
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            // 模拟用户（通常利用JWT令牌解析用户信息）
            String userId = servletServerHttpRequest.getServletRequest().getParameter("uid");
            // TODO 判断用户是否存在
            attributes.put("uid", userId);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response, @NonNull WebSocketHandler wsHandler, Exception exception) {

    }
}
```

> WebSocket 无法使用 header 传递参数，因此这里使用 url params 携带参数。

### WebSocket配置项

> 将自定义处理器、拦截器以及WebSocket操作类依次注入到IOC容器中。

```java
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Bean
    public DefaultWebSocketHandler defaultWebSocketHandler() {
        return new DefaultWebSocketHandler();
    }

    @Bean
    public WebSocket webSocket() {
        return new WebSocketImpl();
    }

    @Bean
    public WebSocketInterceptor webSocketInterceptor() {
        return new WebSocketInterceptor();
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(defaultWebSocketHandler(), "ws/message")
                    .addInterceptors(webSocketInterceptor())
                    .setAllowedOrigins("*");
    }
}
```

+ @EnableWebSocket：开启WebSocket功能
+ addHandler：添加处理器
+ addInterceptors：添加拦截器
+ setAllowedOrigins：设置允许跨域（允许所有请求来源）

### WebSocket测试

![Spring封装-客户端](https://c2.im5i.com/2022/09/24/HnvpJ.png)
![Spring封装-服务端](https://c2.im5i.com/2022/09/24/HnCyw.png)
