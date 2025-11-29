package itsi.api.steuerung.config;

import itsi.api.steuerung.websocket.NoVncWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final NoVncWebSocketHandler noVncWebSocketHandler;

    public WebSocketConfig(NoVncWebSocketHandler noVncWebSocketHandler) {
        this.noVncWebSocketHandler = noVncWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // WebSocket endpoint used by noVNC: ws://host:6081/ws/novnc
        registry.addHandler(noVncWebSocketHandler, "/ws/novnc")
                .setAllowedOrigins("*");
    }
}