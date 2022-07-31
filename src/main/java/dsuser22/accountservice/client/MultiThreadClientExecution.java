package dsuser22.accountservice.client;

import lombok.AllArgsConstructor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;

@AllArgsConstructor
public class MultiThreadClientExecution {
    private int rCount;
    private int wCount;
    private int idStart;
    private int idEnd;
    private long maxValue;
    private static final String URL = "http://localhost:8080/api/account/";

    public CloseableHttpClient concurrentClient(){
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager();
        return HttpClients.custom().setConnectionManager(connectionManager)
                .build();
    }
    public void execute() throws IOException, InterruptedException {
        CloseableHttpClient client = concurrentClient();

        HttpGetRequestThread getRequestThread = new HttpGetRequestThread(idStart, idEnd, client, URL, rCount);
        HttpPutRequestThread putRequestThread = new HttpPutRequestThread(idStart, idEnd, client, URL, wCount, maxValue);

        getRequestThread.start();
        putRequestThread.start();

        getRequestThread.join();
        putRequestThread.join();
        client.close();
    }
}
