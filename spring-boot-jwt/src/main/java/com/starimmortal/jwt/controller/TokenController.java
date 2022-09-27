package com.starimmortal.jwt.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.jwt.enumeration.Code;
import com.starimmortal.jwt.vo.TokenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author william@StarImmortal
 * @date 2022/09/18
 */
@RestController
@RequestMapping("/jwt/token")
@Api(tags = "JWT")
public class TokenController {

    private static String secret;

    private static Integer expires;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        TokenController.secret = secret;
    }

    @Value("${jwt.expires}")
    public void setExpiredTime(Integer expiredTime) {
        TokenController.expires = expiredTime;
    }

    @ApiOperation(value = "生成令牌")
    @PostMapping("/generate")
    public TokenVO getToken(@RequestParam @ApiParam(value = "用户编号") String uid) {
        Algorithm algorithm = Algorithm.HMAC256(TokenController.secret);
        Map<String, Date> dateMap = this.calculateExpiredIssues();
        String token = JWT.create()
                .withClaim("uid", uid)
                .withExpiresAt(dateMap.get("expires"))
                .withIssuedAt(dateMap.get("issued"))
                .sign(algorithm);
        return new TokenVO(token);
    }

    @ApiOperation(value = "校验令牌")
    @GetMapping("/verify")
    public ResponseVO verifyToken(@RequestHeader(value = "Authorization") String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TokenController.secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return ResponseVO.success("校验成功", jwt);
        } catch (JWTVerificationException e) {
            return ResponseVO.error("校验失败");
        }
    }

    @ApiOperation(value = "Claims")
    @GetMapping("/claims")
    public ResponseVO getClaims(@RequestHeader(value = "Authorization") String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TokenController.secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            return ResponseVO.success(Code.SUCCESS.getDescription(), claims.get("uid").asString());
        } catch (JWTVerificationException e) {
            return ResponseVO.error();
        }
    }

    private Map<String, Date> calculateExpiredIssues() {
        Map<String, Date> dateMap = new HashMap<>(16);
        LocalDateTime issued = LocalDateTime.now();
        LocalDateTime expires = issued.plusSeconds(TokenController.expires);
        dateMap.put("issued", Date.from(issued.atZone(ZoneId.systemDefault()).toInstant()));
        dateMap.put("expires", Date.from(expires.atZone(ZoneId.systemDefault()).toInstant()));
        return dateMap;
    }
}
