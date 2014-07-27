TicketFileServer
================

RMI

It's used for "java.rmi.server.codebase"

=the journey of classNotFound=

It worked with browser but doesn't with rmiregistry ...

First I looked up the HTTP Header from Chrome F12. 
The HTTP response header was messed up with unreadable code.
So I figured out you need to specify the end of the header.
After that I have google a bit on Stackoverflow and found something with "\r\n\r\n".
But it's still broken. After that I installed wireshark to analyse 
the tcp stream and found out the normal apache server only sends "\r\n".
And then I read RFC(2616#section-4.1) to confirm it.

So you dont need a 'blown' header like this one.
You just have to seperate the header and the payload with "\r\n"

