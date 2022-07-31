package dsuser22.accountservice.client;

import lombok.AllArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class HttpGetRequestThread extends Thread{
    private int idStart;
    private int idEnd;
    private CloseableHttpClient client;
    private String uri;
    private int rCount;


    @Override
    public void run() {
        List<HttpGet> requests = new ArrayList<>();
        for (int i = 0; i < rCount; i++) {
            int id = ThreadLocalRandom.current().nextInt(idStart, idEnd+1);
            HttpGet httpGet = new HttpGet(uri+id);
            requests.add(httpGet);
        }
        List<String> responses = requests.stream().map(request -> {
            CloseableHttpResponse response = null;
            try {
                response = client.execute(request);
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).toList();
    }
}
