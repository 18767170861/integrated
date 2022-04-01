package org.wmc.integrated.tomcat;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {
    private int serverPort;

    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://" + address.getHostAddress() + ":" + this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        // webserver:org.springframework.boot.web.embedded.tomcat.TomcatWebServer@30b97fcf
        TomcatWebServer webServer = (TomcatWebServer) event.getWebServer();
        System.out.println("webserver:" + webServer);
        Connector connector = webServer.getTomcat().getConnector();
        // connector:Connector[HTTP/1.1-8081]
        System.out.println("connector:" + connector);
        this.serverPort = event.getWebServer().getPort();
        // tomcat init port:8081
        System.out.println("tomcat init port:" + serverPort);
    }

}