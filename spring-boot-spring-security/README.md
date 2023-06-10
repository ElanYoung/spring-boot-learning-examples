# Spring Boot 集成 Spring Security 5.7（安全框架）

## 介绍

Spring Security 是一个能够为基于 Spring 的企业应用系统提供声明式的安全访问控制解决方案的安全框架。

它提供了一组可以在 Spring 应用上下文中配置的 Bean，充分利用了 Spring IOC（控制反转），DI（依赖注入）和 AOP（面向切面编程）功能，为应用系统提供声明式的安全访问控制功能，减少了为企业系统安全控制编写大量重复代码的工作。

认证和授权作为 Spring Security 安全框架的核心功能：

认证（Authentication）：验证当前访问系统用户是否是本系统用户，并且要确认具体是哪个用户。

![认证流程](https://s2.loli.net/2022/12/16/Jkowv7CTpzExhUZ.png)

授权（Authorization）：经过认证后判断当前用户是否具有权限进行某个操作。

![授权流程](https://s2.loli.net/2023/02/16/HjYV4D8mrqohyKd.png)

## 快速开始

### 引入依赖

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<!-- Redis -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<!-- JWT -->
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.4.0</version>
</dependency>
<!-- Lombok 插件 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

### 配置文件

```yml
# 开发环境配置

server:
  # 服务端口
  port: 8081

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/security?useUnicode=true&useSSL=false&serverTimezone=UTC&characterEncoding=UTF8&nullCatalogMeansCurrent=true
    username: "root"
    password: "88888888"
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0
    password: 88888888

security:
  # 密钥
  secret: spring-boot-learning-examples
  # 访问令牌过期时间（1天）
  access-expires: 86400
  # 刷新令牌过期时间（30天）
  refresh-expires: 2592000
  # 白名单
  white-list: /user/login,/user/register,/user/refresh
```

### 测试登录

启动项目后，尝试访问某个接口，会自动跳转到 Spring Security 默认登录页面。

![默认登录页](https://s2.loli.net/2022/12/16/CtxDURWmpwslg7e.png)

![默认退出页](https://s2.loli.net/2022/12/16/d7KIV1qTzyoYtbX.png)

默认用户名：**user**

默认密码：启动项目时会随机生成密码并输出在控制台中：

> Using generated security password: 0b7bb972-ab4c-461c-ab19-7824d23d9b87

![默认密码](https://s2.loli.net/2022/12/16/YaXKhm5A9ClD6vq.png)

## 认证

### 基于数据库加载用户

Spring Security 默认从内存加载用户，需要实现从数据库加载并校验用户。

#### 具体步骤

1）创建 **UserServiceImpl** 类

2）实现 **UserDetailsService** 接口

3）重写 **loadUserByUsername** 方法

4）根据用户名校验用户并查询用户相关权限信息（授权）

5）将数据封装成 **UserDetails**（创建类并实现该接口） 并返回

#### 核心代码

**LoginUser**

```java
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUser implements UserDetails {
    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 权限集合
     */
    @JsonIgnore
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

> 注意：需添加 `@JsonIgnore` 注解，否则会出现序列化失败问题

**UserServiceImpl**

```java
@Service
public class UserServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        UserDO user = getUserByUsername(username);
        // TODO 查询用户权限信息
        return LoginUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    @Override
    public UserDO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username);
        Optional<UserDO> optional = Optional.ofNullable(baseMapper.selectOne(queryWrapper));
        return optional.orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }
}
```

> 注意：如需测试，需要往用户表中写入数据，并且如果用户密码想要明文存储，需要在密码前加 `{noop}`

### 密码加密存储

实际项目中，密码不会以明文形式存储在数据库中，而 Spring Security 密码校验器要求数据库中密码格式为：`{id}password`，而默认使用 `NoOpPasswordEncoder` 加密器，此方式不会对密码进行加密处理，所以不推荐这种形式。

本项目将使用 Spring Security 提供的 `BCryptPasswordEncoder` 来进行密码校验。

#### 具体步骤

1）创建 Spring Security Bean 配置类（避免循环依赖问题）

2）继承 **WebSecurityConfigurerAdapter**（旧用法）

3）将 **BCryptPasswordEncoder** 对象注入 Spring 容器中

> 注意：Spring Security 5.7.x 版本配置方式与以往有所不同，`WebSecurityConfigurerAdapter` 在 Spring Security 5.7 版本中已被标记 `@Deprecated`，未来这个类将被移除，本教程将使用继承 `WebSecurityConfigurerAdapter` 方式来实现 Spring Security 配置，但在实际代码中配置采用最新版本方式！

Spring Security 版本配置区别如下：

1）Spring Boot 2.7.0 版本之前，需要写个配置类继承 `WebSecurityConfigurerAdapter`，然后重写 `Adapter` 中方法进行配置；

2）Spring Boot 2.7.0 版本之后无需再继承 `WebSecurityConfigurerAdapter`，只需直接声明配置类，再配置一个生成 `SecurityFilterChainBean` 方法，把原来 `HttpSecurity` 配置移动到该方法中即可。

[用的挺顺手的 Spring Security 配置类，居然就要被官方弃用了！](https://mp.weixin.qq.com/s/qK-gYDChxxtdFjnIo_ofqw)

#### 核心代码

```java
@Configuration
public class CommonSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

> 注意：同一密码每次加密后生成密文互不相同，因此需使用 matches() 方法来进行比较。

```java
@SpringBootTest
@Slf4j
class SecurityApplicationTests {

    @Test
    void passwordEncoder() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123456");
        log.info("加密密文：{}", password);
        boolean matches = passwordEncoder.matches("123456", password);
        log.info("是否匹配：{}", matches);
    }

}
```

![BCryptPasswordEncoder](https://s2.loli.net/2022/12/16/8WFTb1YgvojPcZI.png)

### 登录接口

#### 具体步骤

1）创建 Spring Security 配置类

2）生成 `SecurityFilterChain` Bean 方法

3）放行登录接口

4）注入 **AuthenticationManager** 认证管理器

5）用户认证

6）生成JWT令牌并返回（双令牌机制）

7）访问令牌（AccessToken）存入 **Redis** 缓存

> 注意：Spring Security 5.7.x 版本配置方式与以往有所不同，`WebSecurityConfigurerAdapter` 在 Spring Security 5.7 版本中已被标记 `@Deprecated`，未来这个类将被移除，所以本教程将采用最新版本配置方式！

Spring Security 版本配置区别如下：

1）Spring Boot 2.7.0 版本之前，需要写个配置类继承 `WebSecurityConfigurerAdapter`，然后重写 `Adapter` 中方法进行配置；

2）Spring Boot 2.7.0 版本之后无需再继承 `WebSecurityConfigurerAdapter`，只需直接声明配置类，再配置一个生成 `SecurityFilterChainBean` 方法，把原来 `HttpSecurity` 配置移动到该方法中即可。

[用的挺顺手的 Spring Security 配置类，居然就要被官方弃用了！](https://mp.weixin.qq.com/s/qK-gYDChxxtdFjnIo_ofqw)

#### 核心代码

1）放行登录接口

需要自定义登陆接口，让 Spring Security 对登录接口放行，之后用户访问该接口时，不用登录也能访问：

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
      httpSecurity
              // 过滤请求
              .authorizeRequests()
              // 接口放行
              .antMatchers("/user/login").permitAll()
              // 除上面外的所有请求全部需要鉴权认证
              .anyRequest()
              .authenticated()
              .and()
              // CSRF禁用
              .csrf().disable()
              // 禁用HTTP响应标头
              .headers().cacheControl().disable()
              .and()
              // 基于JWT令牌，无需Session
              .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      return httpSecurity.build();
  }
}
```

2）注入 **AuthenticationManager** 认证管理器

由于在登录接口中，需通过 `AuthenticationManager` 接口中的 `authenticate` 方法来进行用户认证，所以需要在 `CommonSecurityConfiguration` 配置文件中注入 `AuthenticationManager` 接口。

```java
@Configuration
public class CommonSecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
      return authenticationConfiguration.getAuthenticationManager();
    }
}
```

3）用户认证

调用 `AuthenticationManager` 接口中的 `authenticate` 方法来进行用户认证，该方法需传入 `Authentication` ，由于 `Authentication` 是接口，因此需要传入它的实现类。

由于登录方式采用账号密码形式，所以需使用 `UsernamePasswordAuthenticationToken` 实现类，此类需传入用户名（principal）和密码（credentials）。

认证成功时，Spring Security 将返回 `Authentication` ，内容如下：

![authentication](https://s2.loli.net/2022/12/16/9drPpAJzDGuZQev.png)

> 注意：Authentication 为 NULL 时，说明认证没通过，要么没查询到这个用户，要么密码比对不通过。

此时还需生成 **JWT** 令牌，将其放入响应中返回，为了能够实现双令牌机制需将访问令牌存入 **Redis** 缓存中。

```java
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
  
    @Autowired
    private RedisUtil redisUtil;
  
    @Autowired
    private AuthenticationManager authenticationManager;
  
    @PostMapping("/login")
    public ResponseVO<TokenVO> login(@RequestBody @Validated LoginDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        UserDO user = userService.getUserByUsername(dto.getUsername());
        TokenVO token = JwtUtil.generateTokens(user.getUsername());
        redisUtil.set("user:token:" + user.getUsername() + ":string", token.getAccessToken(), JwtUtil.getAccessExpires());
        return ResponseVO.success("登录成功", token);
    }
}
```

### 认证过滤器

#### 具体步骤

1）接口白名单放行

2）从请求头中解析令牌

3）判断令牌是否存在于黑名单中

4）从 **Redis** 获取令牌

5）校验令牌是否合法或有效

6）存入 **SecurityContextHolder**

7）配置过滤器顺序

#### 核心代码

```java
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
  
    @Autowired
    private UserDetailsService userDetailsService;
  
    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (Arrays.stream(JwtUtil.getWhiteList()).anyMatch(uri -> uri.equals(request.getServletPath()))) {
          filterChain.doFilter(request, response);
          return;
        }
        String token = JwtUtil.decodeTokenFromRequest(request);
        // 判断令牌是否存在黑名单中
        if (redisUtil.hasKey("token:black:" + JwtUtil.getJti(token) + ":string")) {
          throw new RuntimeException(Code.TOKEN_INVALID.getZhDescription());
        }
        String username = JwtUtil.getUsername(token);
        if (StringUtils.hasText(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!StringUtils.hasText(redisUtil.get("user:token:" + username + ":string"))) {
                throw new RuntimeException(Code.ACCESS_TOKEN_EXPIRED.getZhDescription());
            }
            // 校验令牌是否有效
            try {
                JwtUtil.decodeAccessToken(token);
                JwtUtil.checkTokenValid(token, userDetails.getUsername());
            } catch (TokenExpiredException e) {
                // TODO 全局异常处理
                throw new RuntimeException(Code.ACCESS_TOKEN_EXPIRED.getZhDescription());
            } catch (JWTVerificationException e) {
                throw new RuntimeException(Code.TOKEN_INVALID.getZhDescription());
            }
            // 权限信息
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
```

> 注意：该过滤器实现接口并不是之前的Filter，而是去继承 OncePerRequestFilter（过滤器抽象类），通常被用于继承实现并在每次请求时只执行一次过滤）。

在配置文件中，将过滤器加到 **UsernamePasswordAuthenticationFilter** 前面：

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 过滤请求
                .authorizeRequests()
                // 静态资源放行
                .antMatchers(STATIC_RESOURCE_WHITE_LIST).permitAll()
                // 接口放行
                .antMatchers(JwtUtil.getWhiteList()).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated()
                .and()
                // CSRF禁用
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable()
                .and()
                // 基于JWT令牌，无需Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 拦截器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
```

## 退出登录

JWT最大优势在于它是`无状态`，自身包含了认证鉴权所需要的所有信息，服务器端无需对其存储，从而给服务器减少了存储开销。

但是无状态引出的问题也是可想而知的，它无法作废未过期的JWT。举例说明注销场景下，就传统的`cookie/session`认证机制，只需要把存在服务器端的session删掉就OK了。

但是JWT呢，它是不存在服务器端的啊，好的那我删存在客户端的JWT行了吧。额，社会本就复杂别再欺骗自己了好么，被你在客户端删掉的JWT还是可以通过服务器端认证的。

使用JWT要非常明确一点：`JWT失效唯一途径就是等待时间过期`。

本教程借助黑名单方案实现JWT失效：

退出登录时，将访问令牌放入 **Redis** 缓存中，并且设置过期时间为访问令牌过期时间；请求资源时判断该令牌是否在 **Redis** 中，如果存在则拒绝访问。

### 具体步骤

1）全局过滤器中需要判断黑名单是否存在当前访问令牌

2）解析请求头中令牌（`JTI` 与 `EXPIRES_AT`）

3）将JTI字段作为键存放到 **Redis** 缓存中，并设置访问令牌过期时间

4）清除认证信息

5）配置退出登录接口与处理器

[实战！退出登录时如何借助外力使JWT令牌失效？](https://mp.weixin.qq.com/s?__biz=MzU3MDAzNDg1MA==&mid=2247504322&idx=1&sn=4b0a2488a4edcb025d0694604e86f840&chksm=fcf70e0fcb808719b98a65891bc08e9490db09f07debd4521052978a319ab052f8a72b93c1a7&scene=178&cur_album_id=2151150065472569352#rd)

### 核心代码

```java
@Service
@RequiredArgsConstructor
public class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

    @Autowired
    private RedisUtil redisUtil;
  
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = JwtUtil.decodeTokenFromRequest(request);
        blacklist(token);
        SecurityContextHolder.clearContext();
    }
  
    /**
     * 加入黑名单
     *
     * @param token 令牌
     */
    private void blacklist(String token) {
        String jti = JwtUtil.getJti(token);
        Long expires = JwtUtil.getExpires(token);
        redisUtil.set("token:black:" + jti + ":string", StringConstant.EMPTY, DateUtil.minusSeconds(expires));
    }
}
```

退出登录成功处理器：

```java
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      SecurityContextHolder.clearContext();
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("Cache-Control", "no-cache");
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpStatus.OK.value());
      response.getWriter().println(GenericJacksonUtil.objectToJson(ResponseVO.success()));
      response.getWriter().flush();
  }
}
```

在 Spring Security 配置文件中配置退出登录接口与处理器：

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private LogoutHandler logoutHandler;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 过滤请求
                .authorizeRequests()
                // 静态资源放行
                .antMatchers(STATIC_RESOURCE_WHITE_LIST).permitAll()
                // 接口放行
                .antMatchers(JwtUtil.getWhiteList()).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated()
                .and()
                // CSRF禁用
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable()
                .and()
                // 基于JWT令牌，无需Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 拦截器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 退出登录
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .addLogoutHandler(logoutHandler);
        return httpSecurity.build();
    }
}
```

## 授权

在 Spring Security 中，会使用 **FilterSecurityInterceptor**（默认） 来进行权限校验。

在 **FilterSecurityInterceptor** 中会从 **SecurityContextHolder** 获取其中的 **Authentication**，**Authentication** 包含权限信息，用来判断当前用户是否拥有访问当前资源所需的权限。

### 具体步骤

1）开启权限注解

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
}
```

> 开启之后，在需要权限才能访问的接口上打上 **@PreAuthorize** 注解即可。

2）查询用户权限信息（见核心代码）

3）封装权限信息

重写 **loadUserByUsername** 方法时，查询出用户后，还需将用户对应的权限信息，封装到之前定义的 **UserDetails** 的实现类 LoginUser 并返回：

```java
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUser implements UserDetails {
    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 菜单集合
     */
    private List<MenuDO> menuList;
  
    /**
     * 权限集合
     */
    @JsonIgnore
    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (Objects.nonNull(authorities)) {
          return authorities;
        }
        return menuList.stream()
                .filter(menu -> StringUtils.hasText(menu.getPermission()))
                .map(menu -> new SimpleGrantedAuthority(menu.getPermission()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

```java
@Service
public class UserServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        UserDO user = getUserByUsername(username);
        // 查询用户菜单列表
        List<MenuDO> menuList = listUserPermissions(user.getId());
        // 查询用户权限信息
        return new LoginUser(user, menuList);
    }
}
```

### 核心代码

```xml
<select id="listMenusByRoleIds" resultType="com.starimmortal.security.pojo.MenuDO">
    SELECT t1.id, t1.parent_id, t1.`name`, t1.`path`, t1.permission, t1.`icon`, t1.component, t1.`type`, t1.`visible`, t1.`status`, t1.keep_alive, t1.sort_order, t1.create_time, t1.update_time, t1.is_deleted
    FROM `sys_menu` AS t1
    JOIN sys_role_menu AS t2 ON t1.id = t2.menu_id
    WHERE t1.is_deleted = 0 AND t1.`status` = 0
    AND t2.role_id IN
    <foreach collection="roleIds" item="roleId" index="index" open="(" separator="," close=")">
      #{roleId}
    </foreach>
    GROUP BY t1.id
</select>
```

### 权限控制

#### 基于方法注解

Spring Security 默认是关闭方法注解，开启它只需要通过引入 `@EnableGlobalMethodSecurity` 注解即可：

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
}
```

`@EnableGlobalMethodSecurity` 提供了以下三种方式：

1）**prePostEnabled**：基于表达式（**Spring EL**）注解：

- @PreAuthorize：进入**方法之前**验证授权：

  ```java
  @PreAuthorize("#userId == authentication.principal.userId or hasAuthority(‘ADMIN’)")
  ```

  表示方法执行之前，判断方法参数值是否等于 `principal` 中保存的参数值；或者当前用户是否具有 `ROLE_ADMIN` 权限，两者符合其中一种即可访问该方法，内置如下方法：

  - hasAuthority：只能传入一个权限，只有用户有这个权限才可以访问资源；

  - hasAnyAuthority：可以传入多个权限，只有用户有其中任意一个权限都可以访问对应资源；

  - hasRole：要求有对应角色才可以访问，但是它内部会把传入的参数拼接上 **ROLE_** 后再去比较：

    ```
    @PreAuthorize("hasRole('system:dept:list')")
    ```

    > 注意：用户有 `system:dept:list` 权限是无法访问的，得有 `ROLE_system:dept:list` 权限才可以。

  - hasAnyRole：有任意角色即可访问。

- @PostAuthorize：检查授权**方法之后**才被执行并且可以影响执行方法的返回值：

  ```
  @PostAuthorize("returnObject.username == authentication.principal.nickName")
  public CustomUser loadUserDetail(String username) {
      return userRoleRepository.loadUserByUserName(username);
  }
  ```

- @PostFilter：在方法执行之后执行，而且这里可以调用方法的返回值，然后对返回值进行过滤或处理或修改并返回。

- @PreFilter：在方法执行之前执行，而且这里可以调用方法的参数，然后对参数值进行过滤或处理或修改。

2）**securedEnabled**：开启基于角色注解：

```java
@Secured("ROLE_VIEWER")
public String getUsername() {}

@Secured({ "ROLE_DBA", "ROLE_ADMIN" })
public String getNickname() {}
```

@Secured("ROLE_VIEWER")：只有拥有 `ROLE_VIEWER` 角色的用户，才能够访问；

@Secured({ "ROLE_DBA", "ROLE_ADMIN" })：拥有 `"ROLE_DBA", "ROLE_ADMIN"` 两个角色中的任意一个角色，均可访问。

> 注意：@Secured 注解不支持 Spring EL 表达式！

3）jsr250Enabled：开启对JSR250注解：

- @DenyAll：拒绝所有权限

- @RolesAllowed：在功能及使用方法上与 `@Secured` 完全相同

- @PermitAll：接受所有权限

#### 基于配置文件

| 方法名称                   | 方法作用                                                                                 |
|:-----------------------|:-------------------------------------------------------------------------------------|
| `permitAll()`          | 表示所匹配的URL任何人都允许访问                                                                    |
| `anonymous()`          | 表示可以**匿名访问**匹配的URL。和`permitAll()`效果类似，只是设置为`anonymous()`的url会执行`filterChain`中的filter |
| `denyAll()`            | 表示所匹配的URL都不允许被访问。                                                                    |
| `authenticated()`      | 表示所匹配的URL都需要被认证才能访问                                                                  |
| `rememberMe()`         | 允许通过remember-me登录的用户访问                                                               |
| `access()`             | `SpringEl`表达式结果为true时可以访问                                                            |
| `fullyAuthenticated()` | 用户完全认证可以访问（非remember-me下自动登录）                                                        |
| `hasRole()`            | 如果有参数，参数表示角色，则其角色可以访问                                                                |
| `hasAnyRole()`         | 如果有参数，参数表示角色，则其中任何一个角色可以访问                                                           |
| `hasAuthority()`       | 如果有参数，参数表示权限，则其权限可以访问                                                                |
| `hasAnyAuthority()`    | 如果有参数，参数表示权限，则其中任何一个权限可以访问                                                           |
| `hasIpAddress()`       | 如果有参数，参数表示`IP`地址，如果用户`IP`和参数匹配，则可以访问                                                 |

## 自定义异常处理

在 Spring Security 中，认证或者授权的过程中出现异常会被 ExceptionTranslationFilter 捕获，在 ExceptionTranslationFilter 中会去判断是认证失败还是授权失败出现的异常。

1）自定义认证失败异常

```java
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().println(GenericJacksonUtil.objectToJson(ResponseVO.error(Code.UN_AUTHORIZATION.getCode(), Code.UN_AUTHORIZATION.getZhDescription(), authException.getMessage())));
        response.getWriter().flush();
    }
}
```

2）自定义授权失败异常

```java
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().println(GenericJacksonUtil.objectToJson(ResponseVO.error(Code.UN_AUTHENTICATION.getCode(), Code.UN_AUTHENTICATION.getZhDescription(), accessDeniedException.getMessage())));
        response.getWriter().flush();
    }
}
```

3）Spring Security 配置

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestAccessDeniedHandler restAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 过滤请求
                .authorizeRequests()
                // 静态资源放行
                .antMatchers(STATIC_RESOURCE_WHITE_LIST).permitAll()
                // 接口放行
                .antMatchers(JwtUtil.getWhiteList()).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .authenticated()
                .and()
                // CSRF禁用
                .csrf().disable()
                // 禁用HTTP响应标头
                .headers().cacheControl().disable()
                .and()
                // 基于JWT令牌，无需Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 认证与授权失败处理类
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(restAccessDeniedHandler)
                .and()
                // 拦截器
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
```

## 跨域

浏览器出于安全考虑，使用 XMLHttpRequest 对象发起 HTTP 请求时必须遵守同源策略，否则就是跨域的 HTTP 请求，默认情
况下是被禁止的，同源策略要求源相同才能正常进行通信，即协议、域名、端口号都完全一致。

1）Spring Boot 跨域配置

```java
@Configuration(proxyBeanMethods = false)
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

}
```

2）Spring Security 跨域配置

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
              // 跨域
              .cors()
              .and()
              .headers().frameOptions().disable();
        return httpSecurity.build();
    }
}
```

