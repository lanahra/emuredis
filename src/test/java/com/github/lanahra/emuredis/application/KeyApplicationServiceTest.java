package com.github.lanahra.emuredis.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.lanahra.emuredis.domain.model.Key;
import com.github.lanahra.emuredis.domain.model.KeyValueRepository;
import org.junit.Test;

public class KeyApplicationServiceTest {

    @Test
    public void retrievesDbSizeFromRepository() {
        when(repository.size()).thenReturn(10);

        assertThat(service.dbSize(), is(10));
    }

    @Test
    public void deletesKeysFromRepository() {
        when(repository.delete(Key.from("first"), Key.from("second"), Key.from("third")))
                .thenReturn(2);

        assertThat(service.deleteKeys("first", "second", "third"), is(2));
    }

    private final KeyValueRepository repository = mock(KeyValueRepository.class);
    private final Transaction transaction = new SimpleTransaction();
    private final KeyApplicationService service = new KeyApplicationService(repository, transaction);
}
