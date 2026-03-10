package com.yas.webhook.controller;

import com.yas.webhook.model.enums.EventName;
import com.yas.webhook.model.viewmodel.webhook.EventVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookDetailVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import com.yas.webhook.service.WebhookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebhookControllerTest {

    @Mock
    private WebhookService webhookService;

    @InjectMocks
    private WebhookController webhookController;

    @Test
    void testGetPageableWebhooks_whenValidRequest_thenReturnPagedResult() {
        WebhookVm webhook1 = new WebhookVm();
        webhook1.setId(1L);
        webhook1.setPayloadUrl("http://example.com/webhook1");
        webhook1.setIsActive(true);

        WebhookListGetVm result = WebhookListGetVm.builder()
                .webhooks(List.of(webhook1))
                .pageNo(0)
                .pageSize(10)
                .totalElements(1)
                .totalPages(1)
                .isLast(true)
                .build();

        when(webhookService.getPageableWebhooks(0, 10)).thenReturn(result);

        ResponseEntity<WebhookListGetVm> response = webhookController.getPageableWebhooks(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(1, response.getBody().getWebhooks().size());
    }

    @Test
    void testListWebhooks_whenGetAll_thenReturnWebhookList() {
        WebhookVm webhook1 = new WebhookVm();
        webhook1.setId(1L);
        webhook1.setPayloadUrl("http://example.com/webhook1");

        WebhookVm webhook2 = new WebhookVm();
        webhook2.setId(2L);
        webhook2.setPayloadUrl("http://example.com/webhook2");

        when(webhookService.findAllWebhooks()).thenReturn(Arrays.asList(webhook1, webhook2));

        ResponseEntity<List<WebhookVm>> response = webhookController.listWebhooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetWebhook_whenValidId_thenReturnWebhookDetail() {
        WebhookDetailVm webhookDetail = new WebhookDetailVm();
        webhookDetail.setId(1L);
        webhookDetail.setPayloadUrl("http://example.com/webhook");
        webhookDetail.setContentType("application/json");
        webhookDetail.setIsActive(true);

        EventVm event = EventVm.builder()
                .id(1L)
                .name(EventName.ON_ORDER_CREATED)
                .build();
        webhookDetail.setEvents(List.of(event));

        when(webhookService.findById(1L)).thenReturn(webhookDetail);

        ResponseEntity<WebhookDetailVm> response = webhookController.getWebhook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(1, response.getBody().getEvents().size());
    }

    @Test
    void testCreateWebhook_whenValidRequest_thenReturnCreated() {
        EventVm event = EventVm.builder()
                .id(1L)
                .name(EventName.ON_ORDER_CREATED)
                .build();

        WebhookPostVm postVm = new WebhookPostVm();
        postVm.setPayloadUrl("http://example.com/webhook");
        postVm.setSecret("secret123");
        postVm.setContentType("application/json");
        postVm.setIsActive(true);
        postVm.setEvents(List.of(event));

        WebhookDetailVm createdWebhook = new WebhookDetailVm();
        createdWebhook.setId(1L);
        createdWebhook.setPayloadUrl(postVm.getPayloadUrl());
        createdWebhook.setContentType(postVm.getContentType());
        createdWebhook.setIsActive(postVm.getIsActive());
        createdWebhook.setEvents(postVm.getEvents());

        when(webhookService.create(any(WebhookPostVm.class))).thenReturn(createdWebhook);

        ResponseEntity<WebhookDetailVm> response = webhookController.createWebhook(
                postVm, UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testUpdateWebhook_whenValidRequest_thenReturnNoContent() {
        WebhookPostVm postVm = new WebhookPostVm();
        postVm.setPayloadUrl("http://example.com/webhook-updated");
        postVm.setSecret("newsecret");
        postVm.setContentType("application/json");
        postVm.setIsActive(false);

        doNothing().when(webhookService).update(any(WebhookPostVm.class), eq(1L));

        ResponseEntity<Void> response = webhookController.updateWebhook(1L, postVm);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteWebhook_whenValidId_thenReturnNoContent() {
        doNothing().when(webhookService).delete(1L);

        ResponseEntity<Void> response = webhookController.deleteWebhook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
