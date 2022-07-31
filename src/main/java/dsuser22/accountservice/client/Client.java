package dsuser22.accountservice.client;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        int rCount = Integer.parseInt(args[0]);
        int wCount = Integer.parseInt(args[1]);
        int idStart = Integer.parseInt(args[2]);
        int idEnd = Integer.parseInt(args[3]);
        long maxValue = Long.parseLong(args[4]);
        MultiThreadClientExecution serviceClient = new MultiThreadClientExecution(rCount, wCount, idStart, idEnd, maxValue);
        serviceClient.execute();
    }
}
