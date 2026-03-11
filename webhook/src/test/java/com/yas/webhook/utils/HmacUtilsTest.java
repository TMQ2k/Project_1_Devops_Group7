package com.yas.webhook.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class HmacUtilsTest {

    @Test
    void testHash_WithValidDataAndKey_ShouldReturnHash() throws NoSuchAlgorithmException, InvalidKeyException {
        String data = "test data";
        String key = "secret key";

        String hash = HmacUtils.hash(data, key);

        assertThat(hash).isNotNull();
        assertThat(hash).isNotEmpty();
    }

    @Test
    void testHash_WithSameInputs_ShouldReturnSameHash() throws NoSuchAlgorithmException, InvalidKeyException {
        String data = "test data";
        String key = "secret key";

        String hash1 = HmacUtils.hash(data, key);
        String hash2 = HmacUtils.hash(data, key);

        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void testHash_WithDifferentData_ShouldReturnDifferentHash() throws NoSuchAlgorithmException, InvalidKeyException {
        String key = "secret key";

        String hash1 = HmacUtils.hash("data1", key);
        String hash2 = HmacUtils.hash("data2", key);

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void testHash_WithDifferentKey_ShouldReturnDifferentHash() throws NoSuchAlgorithmException, InvalidKeyException {
        String data = "test data";

        String hash1 = HmacUtils.hash(data, "key1");
        String hash2 = HmacUtils.hash(data, "key2");

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void testHash_WithEmptyData_ShouldReturnHash() throws NoSuchAlgorithmException, InvalidKeyException {
        String hash = HmacUtils.hash("", "secret key");

        assertThat(hash).isNotNull();
    }
}
