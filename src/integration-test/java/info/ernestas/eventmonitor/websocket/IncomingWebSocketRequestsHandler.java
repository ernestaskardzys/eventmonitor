package info.ernestas.eventmonitor.websocket;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.util.concurrent.CountDownLatch;

public class IncomingWebSocketRequestsHandler extends StompSessionHandlerAdapter {

    private CountDownLatch errorCounter;

    public IncomingWebSocketRequestsHandler(CountDownLatch errorCounter) {
        this.errorCounter = errorCounter;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.setAutoReceipt(true);
        session.subscribe("/server/list", new WebSocketStompFrameHandler(errorCounter));
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        errorCounter.countDown();
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] p, Throwable throwable) {
        errorCounter.countDown();
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {

    }

}
