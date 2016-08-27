package info.ernestas.eventmonitor.websocket;

import com.google.gson.Gson;
import info.ernestas.eventmonitor.model.JsonResult;
import info.ernestas.eventmonitor.model.ResultStatus;
import info.ernestas.eventmonitor.model.dto.WebSocketDto;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WebSocketStompFrameHandler implements StompFrameHandler {

    private CountDownLatch errorCounter;

    public WebSocketStompFrameHandler(CountDownLatch errorCounter) {
        this.errorCounter = errorCounter;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return JsonResult.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        if (payload instanceof JsonResult) {
            @SuppressWarnings("unchecked") JsonResult<WebSocketDto> result = (JsonResult<WebSocketDto>) payload;
            assertEquals(ResultStatus.SUCCESS, result.getStatus());
            String jsonString = new Gson().toJson(result.getData(), LinkedHashMap.class);

            assertTrue(jsonString.contains("INSERT id") || jsonString.contains("UPDATE id") || jsonString.contains("DELETE id"));
        } else {
            errorCounter.countDown();
        }
    }
}