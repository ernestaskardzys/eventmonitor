package info.ernestas.eventmonitor.web;

import info.ernestas.eventmonitor.model.JsonResult;
import info.ernestas.eventmonitor.model.dto.WebSocketDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/events")
    @SendTo("/server/list")
    public JsonResult<WebSocketDto> websocketEndpoint() {
        return new JsonResult<>(new WebSocketDto("Websocket connection established."));
    }

    // Sockjs requires this endpoint
    @MessageMapping("/events/info")
    public JsonResult<WebSocketDto> websocketInfoEndpoint() {
        return new JsonResult<>(new WebSocketDto("Websocket info endpoint"));
    }

}
