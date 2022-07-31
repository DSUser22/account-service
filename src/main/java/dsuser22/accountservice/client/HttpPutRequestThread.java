package dsuser22.accountservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dsuser22.accountservice.account.AccountRequest;
import lombok.AllArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class HttpPutRequestThread extends Thread{
    private int idStart;
    private int idEnd;
    private CloseableHttpClient client;
    private String uri;
    private int wCount;
    private long maxValue;


    @Override
    public void run() {
        List<HttpPut> requests = new ArrayList<>();
        for (int i = 0; i < wCount; i++) {
            int id = ThreadLocalRandom.current().nextInt(idStart, idEnd+1);
            long value = ThreadLocalRandom.current().nextLong(maxValue);
            ObjectMapper mapper = new ObjectMapper();
            StringEntity entity;
            try {
                entity = new StringEntity(mapper.writeValueAsString(new AccountRequest(id, value)));
            } catch (UnsupportedEncodingException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            HttpPut httpPut = new HttpPut(uri);
            httpPut.setEntity(entity);
            httpPut.setHeader("Content-type", "application/json");
            requests.add(httpPut);
        }
        List<Integer> responses = requests.stream().map(request -> {
            CloseableHttpResponse response = null;
            try {
                response = client.execute(request);
                return response.getStatusLine().getStatusCode();
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
