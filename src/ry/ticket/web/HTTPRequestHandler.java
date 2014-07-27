package ry.ticket.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import static ry.ticket.web.TicketFileServer.CAL;
import static ry.ticket.web.TicketFileServer.SF;

/**
 *
 * @author ry
 */
public class HTTPRequestHandler implements Callable<Void> {

    private final Socket socket;

    public HTTPRequestHandler(Socket connection) {
        socket = connection;
        try {
            socket.setKeepAlive(true);
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Void call() throws Exception {
        try (OutputStream out = new BufferedOutputStream(socket.getOutputStream());
             InputStream in = new BufferedInputStream(socket.getInputStream());) {
            StringBuilder request = new StringBuilder(128);
            while (true) {
                int c = in.read();
                if (c == '\r' || c == '\n' || c == -1) {
                    break;
                }
                request.append((char) c);
            }
            Path get = Paths.get(System.getProperty("user.dir") + "/web/compute.jar");
            String header = "HTTP/1.0 200 OK\r\n"                                          
                            + "Accept-Ranges:bytes\r\n"                         
                            + "Connection:Keep-Alive\r\n"                       
                            + "Content-Length: " + get.toFile().length() + "\r\n"
                            + "Content-Type: application/java-archive\r\n"
                            + "Date: " + SF.format(CAL.getTime()) + "\r\n"
                            + "Keep-Alive: timeout=5, max=100\r\n"
                            + "Last-Modified: " + SF.format(CAL.getTime()) + "\r\n"
                            + "Server: REI ClassLoader\r\n"
                            + "\r\n";                                           
            out.write(header.getBytes(Charset.forName("US-ASCII")));
            out.write(Files.readAllBytes(get));
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
