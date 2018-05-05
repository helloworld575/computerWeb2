package gbn_method.server;

public class ServerDriver {
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args){
        Server server = new Server(SERVER_PORT);
        server.start();
    }
}
