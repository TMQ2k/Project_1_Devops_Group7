package com.yas.sampledata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import com.yas.sampledata.viewmodel.SampleDataVm;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SampleDataServiceTest {

    @Mock
    private DataSource productDataSource;

    @Mock
    private DataSource mediaDataSource;

    @InjectMocks
    private SampleDataService sampleDataService;

    @Test
    void createSampleData_shouldReturnSuccessMessage() {
        SampleDataVm result = sampleDataService.createSampleData();

        assertThat(result).isNotNull();
        assertThat(result.message()).isEqualTo("Insert Sample Data successfully!");
    }
}
