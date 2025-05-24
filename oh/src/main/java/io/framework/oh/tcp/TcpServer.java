package io.framework.oh.tcp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class TcpServer {

    @Value("${tcp.server.port:9090}")
    private int port;

    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private volatile boolean running = true;

    @PostConstruct
    public void start() {
        executorService = Executors.newCachedThreadPool();
        try {
            serverSocket = new ServerSocket(port);
            log.info("TCP Server started on port: {}", port);
            
            executorService.submit(this::acceptConnections);
        } catch (IOException e) {
            log.error("Failed to start TCP server", e);
        }
    }

    private void acceptConnections() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                log.info("New client connected: {}", clientSocket.getInetAddress());
                
                executorService.submit(() -> handleClient(clientSocket));
            } catch (IOException e) {
                if (running) {
                    log.error("Error accepting client connection", e);
                }
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (clientSocket) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            
            while (running && (bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
                String message = new String(buffer, 0, bytesRead);
                log.info("Received from client: {}", message);
                
                // Echo back to client
                clientSocket.getOutputStream().write(message.getBytes());
            }
        } catch (IOException e) {
            log.error("Error handling client connection", e);
        }
    }

    @PreDestroy
    public void stop() {
        running = false;
        executorService.shutdown();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log.error("Error closing TCP server", e);
        }
    }
} 