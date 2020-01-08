package se.cygni.webapp.socket;

import com.google.common.eventbus.EventBus;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

@WebServlet(value = "/websockets/test", loadOnStartup = 1)
public class PokerWebSocketServlet extends org.eclipse.jetty.websocket.servlet.WebSocketServlet {

    private EventBus eventBus;

    public PokerWebSocketServlet() {
        System.out.println("Created hellowebsocketservlet");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        EventBus eventBus = springContext.getBean(EventBus.class);
        this.eventBus = eventBus;

        System.out.println("EventBus: " + eventBus);
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        System.out.println("Created PokerWebSocketAdapter via factory!");
//        factory.register(PokerWebSocketAdapter.class);

        /* ToDo: Fix this to return a Spring-bean

         */
        factory.setCreator(new WebSocketCreator() {
            @Override
            public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
                return new PokerWebSocketAdapter(eventBus);
            }
        });
    }

}
