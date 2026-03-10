package com.yas.webhook.service;

import com.yas.commonlibrary.exception.NotFoundException;
import com.yas.webhook.integration.api.WebhookApi;
import com.yas.webhook.model.Event;
import com.yas.webhook.model.Webhook;
import com.yas.webhook.model.WebhookEvent;
import com.yas.webhook.model.WebhookEventNotification;
import com.yas.webhook.model.dto.WebhookEventNotificationDto;
import com.yas.webhook.model.enums.EventName;
import com.yas.webhook.model.mapper.WebhookMapper;
import com.yas.webhook.model.viewmodel.webhook.EventVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookDetailVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookListGetVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookPostVm;
import com.yas.webhook.model.viewmodel.webhook.WebhookVm;
import com.yas.webhook.repository.EventRepository;
import com.yas.webhook.repository.WebhookEventNotificationRepository;
import com.yas.webhook.repository.WebhookEventRepository;
import com.yas.webhook.repository.WebhookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookServiceTest {

    @Mock
    WebhookEventNotificationRepository webhookEventNotificationRepository;
    @Mock
    WebhookApi webHookApi;
    @Mock
    WebhookRepository webhookRepository;
    @Mock
    WebhookMapper webhookMapper;
    @Mock
    EventRepository eventRepository;
    @Mock
    WebhookEventRepository webhookEventRepository;

    @InjectMocks
    WebhookService webhookService;

    @Test
    void test_notifyToWebhook_ShouldNotException() {
        WebhookEventNotificationDto notificationDto = WebhookEventNotificationDto
            .builder()
            .notificationId(1L)
            .url("")
            .secret("")
            .build();

        WebhookEventNotification notification = new WebhookEventNotification();
        when(webhookEventNotificationRepository.findById(notificationDto.getNotificationId()))
            .thenReturn(Optional.of(notification));

        webhookService.notifyToWebhook(notificationDto);

        verify(webhookEventNotificationRepository).save(notification);
        verify(webHookApi).notify(notificationDto.getUrl(), notificationDto.getSecret(), notificationDto.getPayload());
    }

    @Test
    void test_getPageableWebhooks_shouldReturnPagedResults() {
        Webhook webhook = new Webhook();
        webhook.setId(1L);
        Page<Webhook> page = new PageImpl<>(List.of(webhook));
        WebhookListGetVm expected = WebhookListGetVm.builder()
                .webhooks(List.of())
                .pageNo(0)
                .pageSize(10)
                .build();

        when(webhookRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(webhookMapper.toWebhookListGetVm(any(), anyInt(), anyInt())).thenReturn(expected);

        WebhookListGetVm result = webhookService.getPageableWebhooks(0, 10);

        assertNotNull(result);
        assertEquals(0, result.getPageNo());
        verify(webhookRepository).findAll(any(PageRequest.class));
    }

    @Test
    void test_findAllWebhooks_shouldReturnAllWebhooks() {
        Webhook webhook1 = new Webhook();
        webhook1.setId(1L);
        Webhook webhook2 = new Webhook();
        webhook2.setId(2L);
List<Webhook> webhooks = List.of(webhook1, webhook2);
        WebhookVm vm1 = new WebhookVm();
        vm1.setId(1L);
        WebhookVm vm2 = new WebhookVm();
        vm2.setId(2L);

        when(webhookRepository.findAll(any(Sort.class))).thenReturn(webhooks);
        when(webhookMapper.toWebhookVm(webhook1)).thenReturn(vm1);
        when(webhookMapper.toWebhookVm(webhook2)).thenReturn(vm2);

        List<WebhookVm> result = webhookService.findAllWebhooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(webhookRepository).findAll(any(Sort.class));
    }

    @Test
    void test_findById_whenWebhookExists_shouldReturnWebhookDetail() {
        Webhook webhook = new Webhook();
        webhook.setId(1L);
        WebhookDetailVm detailVm = new WebhookDetailVm();
        detailVm.setId(1L);

        when(webhookRepository.findById(1L)).thenReturn(Optional.of(webhook));
        when(webhookMapper.toWebhookDetailVm(webhook)).thenReturn(detailVm);

        WebhookDetailVm result = webhookService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void test_findById_whenWebhookNotFound_shouldThrowNotFoundException() {
        when(webhookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> webhookService.findById(999L));
    }

    @Test
    void test_create_withEvents_shouldCreateWebhookAndEvents() {
        EventVm eventVm = EventVm.builder().id(1L).name(EventName.ON_ORDER_CREATED).build();
        WebhookPostVm postVm = new WebhookPostVm();
        postVm.setPayloadUrl("http://example.com");
        postVm.setEvents(List.of(eventVm));

        Webhook webhook = new Webhook();
        webhook.setId(1L);
        WebhookDetailVm detailVm = new WebhookDetailVm();

        Event event = new Event();
        event.setId(1L);

        when(webhookMapper.toCreatedWebhook(postVm)).thenReturn(webhook);
        when(webhookRepository.save(webhook)).thenReturn(webhook);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(webhookEventRepository.saveAll(anyList())).thenReturn(List.of());
        when(webhookMapper.toWebhookDetailVm(webhook)).thenReturn(detailVm);

        WebhookDetailVm result = webhookService.create(postVm);

        assertNotNull(result);
        verify(webhookRepository).save(webhook);
        verify(webhookEventRepository).saveAll(anyList());
    }

    @Test
    void test_update_shouldUpdateWebhookAndEvents() {
        EventVm eventVm = EventVm.builder().id(1L).name(EventName.ON_ORDER_CREATED).build();
        WebhookPostVm postVm = new WebhookPostVm();
        postVm.setEvents(List.of(eventVm));

        Webhook existingWebhook = new Webhook();
        existingWebhook.setId(1L);
        existingWebhook.setWebhookEvents(List.of());

        Event event = new Event();
        event.setId(1L);

        when(webhookRepository.findById(1L)).thenReturn(Optional.of(existingWebhook));
        when(webhookMapper.toUpdatedWebhook(existingWebhook, postVm)).thenReturn(existingWebhook);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        webhookService.update(postVm, 1L);

        verify(webhookRepository).save(existingWebhook);
        verify(webhookEventRepository).saveAll(anyList());
    }

    @Test
    void test_update_whenWebhookNotFound_shouldThrowNotFoundException() {
        WebhookPostVm postVm = new WebhookPostVm();
        when(webhookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> webhookService.update(postVm, 999L));
    }

    @Test
    void test_delete_whenWebhookExists_shouldDeleteWebhookAndEvents() {
        when(webhookRepository.existsById(1L)).thenReturn(true);

        webhookService.delete(1L);

        verify(webhookEventRepository).deleteByWebhookId(1L);
        verify(webhookRepository).deleteById(1L);
    }

    @Test
    void test_delete_whenWebhookNotFound_shouldThrowNotFoundException() {
        when(webhookRepository.existsById(999L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> webhookService.delete(999L));
    }
}
