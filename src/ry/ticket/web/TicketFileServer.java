package ry.ticket.web;

import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author ry
 */
public class TicketFileServer {
    
    public static Calendar CAL = Calendar.getInstance();
    public static SimpleDateFormat SF = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

    public TicketFileServer(int port) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket srv = new ServerSocket(port)) {
            while (true) {
                pool.submit(new HTTPRequestHandler(srv.accept()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
