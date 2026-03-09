package com.yas.webhook.controller;

import com.yas.webhook.model.enums.EventName;
import com.yas.webhook.model.viewmodel.webhook.EventVm;
import com.yas.webhook.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    void testListWebhooks_whenGetAllEvents_thenReturnEventList() {
        EventVm event1 = EventVm.builder()
                .id(1L)
                .name(EventName.ON_ORDER_CREATED)
                .build();
        EventVm event2 = EventVm.builder()
                .id(2L)
                .name(EventName.ON_PRODUCT_UPDATED)
                .build();
        List<EventVm> events = Arrays.asList(event1, event2);

        when(eventService.findAllEvents()).thenReturn(events);

        ResponseEntity<List<EventVm>> response = eventController.listWebhooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals(EventName.ON_ORDER_CREATED, response.getBody().get(0).getName());
    }

    @Test
    void testListWebhooks_whenNoEvents_thenReturnEmptyList() {
        when(eventService.findAllEvents()).thenReturn(List.of());

        ResponseEntity<List<EventVm>> response = eventController.listWebhooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }
}
