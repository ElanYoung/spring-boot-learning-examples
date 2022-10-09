# Spring Boot 集成 JWT

## 介绍

>JSON Web Token（JWT）是一个开放标准（RFC 7519），它定义了一种紧凑且独立的方式，用于在各方之间作为JSON对象安全地传输信息。
>
>因为数字签名的存在，这些信息是可信的，JWT可以使用HMAC算法或者是RSA的公私钥对进行签名。
>
>[Json Web Token](https://jwt.io/)

## 组成部分

JWT 实际上就是一个字符串，它由三部分组成：头部、载荷与签名。

前两部分需要经过 Base64 编码，后一部分需通过前两部分 Base64 编码后再加密而成。

### 头部（Header）

用于描述关于该 JWT 的最基本的信息，例如其类型以及签名所用的算法等，也可以被表示成一个 JSON 对象。

```json
{
    "typ": "JWT",
    "alg": "HS256"
}
```

### 载荷（playload）

载荷用于存放有效信息，有效信息包含三个部分：

1. 标准中注册声明（建议但不强制使用）

    + iss：jwt签发者
    + sub：jwt所面向的用户
    + aud：接收jwt的一方
    + exp：jwt过期时间（过期时间必须要大于签发时间）
    + nbf：定义在什么时间之前，该jwt都是不可用的
    + iat：jwt签发时间
    + jti：jwt唯一身份标识，主要用来作为一次性token，从而回避重放攻击

2. 公共声明：可以添加任何信息，一般添加用户的相关信息或其他业务需要的必要信息，但不建议添加敏感信息，因为该部分在客户端可解密。

3. 私有声明：提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为 base64 是对称解密的，意味着该部分信息可以归类为明文信息。

>注意：载荷中的这3个声明并不是都要同时设置。

### 签名（signature）

这个部分需要 Base64 加密后的 header 和 Base64 加密后的 payload 使用 “.” 连接组成的字符串，然后通过 header 中声明的加密方式进行加盐 secret 组合加密，然后就构成了 jwt 的第三部分。

## 快速开始

### 引入依赖

```xml
 <dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>4.0.0</version>
</dependency>
```

### 配置文件

```yml
jwt:
  # 秘钥
  secret: spring-boot-learning-examples-jwt
  # 过期时间（秒）
  expires: 604800
```

### 生成令牌

```java
@PostMapping("/generate")
public void getToken(@RequestParam String uid) {
    Algorithm algorithm = Algorithm.HMAC256("spring-boot-learning-examples-jwt");
    LocalDateTime issued = LocalDateTime.now();
    LocalDateTime expires = issued.plusSeconds(604800);
    String token = JWT.create()
            .withClaim("uid", uid)
            .withExpiresAt(Date.from(expires.atZone(ZoneId.systemDefault()).toInstant()))
            .withIssuedAt(Date.from(issued.atZone(ZoneId.systemDefault()).toInstant()))
            .sign(algorithm);
}
```

+ withClaim：存放有效信息
+ withExpiresAt：设置过期时间
+ withIssuedAt：设置签发时间

### 校验令牌

```java
@GetMapping("/verify")
public void verifyToken(@RequestHeader(value = "Authorization") String token) {
    try {
        Algorithm algorithm = Algorithm.HMAC256("spring-boot-learning-examples-jwt");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
    } catch (JWTVerificationException e) {
        e.printStackTrace();
    }
}
```

### Claims

```java
@GetMapping("/claims")
public ResponseVO getClaims(@RequestHeader(value = "Authorization") String token) {
    try {
        Algorithm algorithm = Algorithm.HMAC256("spring-boot-learning-examples-jwt");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        String uid = claims.get("uid").asString();
    } catch (JWTVerificationException e) {
        e.printStackTrace();
    }
}
```