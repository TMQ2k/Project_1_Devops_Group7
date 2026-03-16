package com.yas.sampledata.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.yas.sampledata.service.SampleDataService;
import com.yas.sampledata.viewmodel.SampleDataVm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SampleDataControllerTest {

    @Mock
    private SampleDataService sampleDataService;

    @InjectMocks
    private SampleDataController sampleDataController;

    @Test
    void createSampleData_shouldReturnSampleDataVm() {
        SampleDataVm expected = new SampleDataVm("Insert Sample Data successfully!");
        when(sampleDataService.createSampleData()).thenReturn(expected);

        SampleDataVm result = sampleDataController.createSampleData(new SampleDataVm("test"));

        assertThat(result).isEqualTo(expected);
        assertThat(result.message()).isEqualTo("Insert Sample Data successfully!");
    }
}
