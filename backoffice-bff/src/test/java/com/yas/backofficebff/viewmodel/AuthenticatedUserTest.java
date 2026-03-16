package com.yas.backofficebff.viewmodel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AuthenticatedUserTest {

    @Test
    void testAuthenticatedUser_whenCreatedWithUsername_returnUsername() {
        AuthenticatedUser user = new AuthenticatedUser("testuser");

        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo("testuser");
    }

    @Test
    void testAuthenticatedUser_whenCreatedWithNullUsername_returnNull() {
        AuthenticatedUser user = new AuthenticatedUser(null);

        assertThat(user).isNotNull();
        assertThat(user.username()).isNull();
    }

    @Test
    void testAuthenticatedUser_whenCompared_equalityWorks() {
        AuthenticatedUser user1 = new AuthenticatedUser("testuser");
        AuthenticatedUser user2 = new AuthenticatedUser("testuser");
        AuthenticatedUser user3 = new AuthenticatedUser("otheruser");

        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);
    }
}
