package util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by stripes on 2017. 2. 19..
 */
public class UuidCreateTest {
    private static final Logger logger = LoggerFactory.getLogger(UuidCreateTest.class);

    @Test
    public void UuidCreate() {
        UUID uuid = UUID.randomUUID();
        logger.debug("{}", uuid);
    }
}
