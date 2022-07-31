package dsuser22.accountservice.account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping(path = "api")
@AllArgsConstructor
@Slf4j
public class AccountController {
    private static AtomicLong rRequest = new AtomicLong();
    private static AtomicLong wRequest = new AtomicLong();
    private static AtomicLong rCountPerSecond = new AtomicLong();
    private static AtomicLong wCountPerSecond = new AtomicLong();

    private Service service;
    @PutMapping(path = "account")
    public ResponseEntity<?> addAmount(@RequestBody AccountRequest request){
        service.addAmount(request.getId(), request.getValue());
        wRequest.incrementAndGet();
        wCountPerSecond.incrementAndGet();
        return ResponseEntity.ok().build();
    }
    @GetMapping(path = "account/{id}")
    public ResponseEntity<Long> getAmount(@PathVariable("id") Integer id){
        rRequest.incrementAndGet();
        rCountPerSecond.incrementAndGet();
        return ResponseEntity.ok(service.getAmount(id));
    }
    @PostMapping(path = "reset")
    public ResponseEntity<?> resetStatistics(){
        rRequest.set(0);
        wRequest.set(0);
        rCountPerSecond.set(0);
        wCountPerSecond.set(0);
        return ResponseEntity.ok().build();
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedDelay = 1)
    public void countOfAllRequest(){
        log.info("read requests: {}", rRequest.get());
        log.info("write requests: {}", wRequest.get());
    }
    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedDelay = 1)
    public void requestsPerSecond(){
        log.info("read requests per second: {}", rCountPerSecond.getAndSet(0));
        log.info("write requests per second: {}", wCountPerSecond.getAndSet(0));
    }
}
