package com.github.lanahra.emuredis;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.github.lanahra.emuredis.application.sortedset.AddCommand;
import com.github.lanahra.emuredis.application.sortedset.SortedSetApplicationService;
import com.github.lanahra.emuredis.application.string.StringApplicationService;
import com.github.lanahra.emuredis.domain.model.string.StringValue;
import com.github.lanahra.emuredis.infrastructure.spring.ApplicationContextRule;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

public class ApplicationConcurrencyTest {

    @Test
    public void incrementsAKeyConcurrently() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newFixedThreadPool(100);

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            futures.add(
                    CompletableFuture.runAsync(
                            () -> stringApplicationService.incrementValueOf("mykey"), executor));
        }

        for (CompletableFuture<Void> future : futures) {
            future.get();
        }

        StringValue stringValue = stringApplicationService.getValueOf("mykey");

        assertThat(stringValue, is(StringValue.from("100")));
    }

    @Test
    public void addsMembersToSortedSetConcurrently()
            throws ExecutionException, InterruptedException {
        Executor executor = Executors.newFixedThreadPool(100);

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        IntStream.range(0, 100).forEach(i -> runZAddCommandAsync(futures, executor, i));

        for (CompletableFuture<Void> future : futures) {
            future.get();
        }

        int cardinality = sortedSetApplicationService.cardinalityOfSortedSetOf("myzset");

        assertThat(cardinality, is(100));
    }

    private boolean runZAddCommandAsync(
            List<CompletableFuture<Void>> futures, Executor executor, int i) {
        return futures.add(
                CompletableFuture.runAsync(
                        () ->
                                sortedSetApplicationService.addMember(
                                        new AddCommand("myzset", String.valueOf(i), i)),
                        executor));
    }

    @Before
    public void setup() {
        stringApplicationService = rule.getBean(StringApplicationService.class);
        sortedSetApplicationService = rule.getBean(SortedSetApplicationService.class);
    }

    private StringApplicationService stringApplicationService;
    private SortedSetApplicationService sortedSetApplicationService;

    @ClassRule public static final ApplicationContextRule rule = new ApplicationContextRule();
}
