package dsuser22.accountservice.account;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

@org.springframework.stereotype.Service
@AllArgsConstructor
@Slf4j
public class Service implements AccountService{
    private AccountRepository repository;

    @Override
    @Cacheable(value = "account", key = "#id")
    public Long getAmount(Integer id){
        Optional<Account> account = repository.findById(id);
        if(account.isPresent()){
            return account.get().getAmount();
        } else return 0L;
    }

    @Override
    @CacheEvict(value = "account", allEntries = true)
    public void addAmount(Integer id, Long value) {
        Account account = repository.findById(id).orElse(new Account(id, 0L));
        account.setAmount(account.getAmount()+value);
        repository.save(account);
    }
}
