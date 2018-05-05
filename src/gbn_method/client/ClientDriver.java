package gbn_method.client;

public class ClientDriver {

    private static final int CLIENT_PORT = 1234;

    public static void main(String[] args){
        Client client = new Client(CLIENT_PORT);
        client.start();
    }
}
