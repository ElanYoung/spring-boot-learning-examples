package com.starimmortal.websocket.controller;

import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.websocket.message.WebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author william@StarImmortal
 * @date 2022/9/22
 */
@RestController
@RequestMapping("/websocket")
@Api(tags = "WebSocket")
public class WebSocketController {

    @Autowired
    private WebSocket webSocket;

    @ApiOperation(value = "发送广播消息")
    @PostMapping("/broadcast/send")
    public ResponseVO sendBroadcastMessage(@RequestParam String message) throws IOException {
        webSocket.broadCast(message);
        return ResponseVO.success();
    }

    @ApiOperation(value = "发送单点消息")
    @PostMapping("/single/send")
    public ResponseVO sendSingleMessage(@RequestParam String userId, @RequestParam String message) throws IOException {
        webSocket.sendMessage(userId, message);
        return ResponseVO.success();
    }
}
