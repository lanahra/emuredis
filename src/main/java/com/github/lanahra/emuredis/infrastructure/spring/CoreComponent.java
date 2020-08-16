package com.github.lanahra.emuredis.infrastructure.spring;

import com.github.lanahra.emuredis.application.KeyApplicationService;
import com.github.lanahra.emuredis.application.Transaction;
import com.github.lanahra.emuredis.application.sortedset.SortedSetApplicationService;
import com.github.lanahra.emuredis.application.string.StringApplicationService;
import com.github.lanahra.emuredis.domain.model.Clock;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import com.github.lanahra.emuredis.infrastructure.repository.InMemoryKeyValueRepository;
import java.time.Instant;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreComponent {

    @Bean
    public KeyApplicationService keyApplicationService() {
        return new KeyApplicationService(keyValueRepository(), transaction());
    }

    @Bean
    public StringApplicationService stringApplicationService() {
        return new StringApplicationService(clock(), keyValueRepository(), transaction());
    }

    @Bean
    public SortedSetApplicationService sortedSetApplicationService() {
        return new SortedSetApplicationService(keyValueRepository(), transaction());
    }

    @Bean
    public KeyValueRepository keyValueRepository() {
        return new InMemoryKeyValueRepository(clock());
    }

    @Bean
    public Clock clock() {
        return Instant::now;
    }

    @Bean
    public Transaction transaction() {
        return new Transaction() {

            @Override
            public void run(Runnable action) {
                action.run();
            }

            @Override
            public <T> T supply(Supplier<T> action) {
                return action.get();
            }
        };
    }
}
