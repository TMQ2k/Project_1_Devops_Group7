package com.yas.webhook.model.mapper;

import com.yas.webhook.model.Event;
import com.yas.webhook.model.enums.EventName;
import com.yas.webhook.model.viewmodel.webhook.EventVm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventMapperTest {

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    @Test
    void testToEventVm_whenValidEvent_thenReturnEventVm() {
        Event event = new Event();
        event.setId(1L);
        event.setName(EventName.ON_ORDER_CREATED);

        EventVm result = eventMapper.toEventVm(event);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(EventName.ON_ORDER_CREATED, result.getName());
    }

    @Test
    void testToEventVm_whenNullEvent_thenReturnNull() {
        EventVm result = eventMapper.toEventVm(null);

        assertNull(result);
    }

    @Test
    void testToEventVm_whenEventWithDifferentName_thenMapCorrectly() {
        Event event = new Event();
        event.setId(2L);
        event.setName(EventName.ON_PRODUCT_UPDATED);

        EventVm result = eventMapper.toEventVm(event);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(EventName.ON_PRODUCT_UPDATED, result.getName());
    }
}
