package com.yas.webhook.model.mapper;

import com.yas.webhook.model.Event;
import com.yas.webhook.model.Webhook;
import com.yas.webhook.model.WebhookEvent;
import com.yas.webhook.model.enums.EventName;
import com.yas.webhook.model.viewmodel.webhook.EventVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookDetailVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WebhookMapperTest {

    private final WebhookMapper webhookMapper = Mappers.getMapper(WebhookMapper.class);

    @Test
    void testToWebhookVm_whenValidWebhook_thenReturnWebhookVm() {
        Webhook webhook = new Webhook();
        webhook.setId(1L);
        webhook.setPayloadUrl("http://example.com");
        webhook.setIsActive(true);

        WebhookVm result = webhookMapper.toWebhookVm(webhook);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("http://example.com", result.getPayloadUrl());
        assertTrue(result.getIsActive());
    }

    @Test
    void testToWebhookEventVms_whenValidList_thenReturnEventVmList() {
        Event event = new Event();
        event.setId(1L);
        event.setName(EventName.ON_ORDER_CREATED);

        WebhookEvent webhookEvent = new WebhookEvent();
        webhookEvent.setEventId(1L);

        List<WebhookEvent> webhookEvents = List.of(webhookEvent);

        List<EventVm> result = webhookMapper.toWebhookEventVms(webhookEvents);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testToWebhookEventVms_whenEmptyList_thenReturnEmptyList() {
        List<EventVm> result = webhookMapper.toWebhookEventVms(Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToWebhookEventVms_whenNullList_thenReturnEmptyList() {
        List<EventVm> result = webhookMapper.toWebhookEventVms(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToWebhookListGetVm_whenValidPage_thenReturnListVm() {
        Webhook webhook1 = new Webhook();
        webhook1.setId(1L);
        webhook1.setPayloadUrl("http://example.com/webhook1");

        Webhook webhook2 = new Webhook();
        webhook2.setId(2L);
        webhook2.setPayloadUrl("http://example.com/webhook2");

        Page<Webhook> webhooksPage = new PageImpl<>(List.of(webhook1, webhook2));

        WebhookListGetVm result = webhookMapper.toWebhookListGetVm(webhooksPage, 0, 10);

        assertNotNull(result);
        assertEquals(0, result.getPageNo());
        assertEquals(10, result.getPageSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
        assertEquals(2, result.getWebhooks().size());
    }

    @Test
    void testToUpdatedWebhook_whenValidPostVm_thenUpdateWebhook() {
        Webhook existingWebhook = new Webhook();
        existingWebhook.setId(1L);
        existingWebhook.setPayloadUrl("http://old.com");

        WebhookPostVm postVm = new WebhookPostVm();
        postVm.setPayloadUrl("http://new.com");
        postVm.setSecret("newsecret");
        postVm.setIsActive(false);

        Webhook result = webhookMapper.toUpdatedWebhook(existingWebhook, postVm);

        assertNotNull(result);
        assertEquals("http://new.com", result.getPayloadUrl());
        assertEquals("newsecret", result.getSecret());
        assertFalse(result.getIsActive());
    }

    @Test
    void testToCreatedWebhook_whenValidPostVm_thenCreateWebhook() {
        EventVm eventVm = EventVm.builder()
                .id(1L)
                .name(EventName.ON_ORDER_CREATED)
                .build();

        WebhookPostVm postVm = new WebhookPostVm();
        postVm.setPayloadUrl("http://example.com/webhook");
        postVm.setSecret("secret123");
        postVm.setContentType("application/json");
        postVm.setIsActive(true);
        postVm.setEvents(List.of(eventVm));

        Webhook result = webhookMapper.toCreatedWebhook(postVm);

        assertNotNull(result);
        assertEquals("http://example.com/webhook", result.getPayloadUrl());
        assertEquals("secret123", result.getSecret());
        assertEquals("application/json", result.getContentType());
        assertTrue(result.getIsActive());
    }

    @Test
    void testToWebhookDetailVm_whenValidWebhook_thenReturnDetailVm() {
        Webhook webhook = new Webhook();
        webhook.setId(1L);
        webhook.setPayloadUrl("http://example.com");
        webhook.setContentType("application/json");
        webhook.setIsActive(true);
        webhook.setSecret("secret");

        WebhookEvent webhookEvent = new WebhookEvent();
        webhookEvent.setEventId(1L);
        webhook.setWebhookEvents(new ArrayList<>(List.of(webhookEvent)));

        WebhookDetailVm result = webhookMapper.toWebhookDetailVm(webhook);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("http://example.com", result.getPayloadUrl());
        assertEquals("application/json", result.getContentType());
        assertTrue(result.getIsActive());
        assertNull(result.getSecret()); // secret is ignored in mapping
        assertEquals(1, result.getEvents().size());
    }

    @Test
    void testToWebhookDetailVm_whenNullWebhookEvents_thenReturnEmptyEvents() {
        Webhook webhook = new Webhook();
        webhook.setId(1L);
        webhook.setPayloadUrl("http://example.com");
        webhook.setWebhookEvents(null);

        WebhookDetailVm result = webhookMapper.toWebhookDetailVm(webhook);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getEvents());
        assertTrue(result.getEvents().isEmpty());
    }
}
