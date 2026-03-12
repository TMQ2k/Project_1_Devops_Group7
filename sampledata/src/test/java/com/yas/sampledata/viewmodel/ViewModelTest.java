package com.yas.sampledata.viewmodel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ViewModelTest {

    @Test
    void sampleDataVm_shouldHoldMessage() {
        SampleDataVm vm = new SampleDataVm("test message");
        assertThat(vm.message()).isEqualTo("test message");
    }

    @Test
    void errorVm_withFieldErrors_shouldHoldData() {
        ErrorVm vm = new ErrorVm("404", "Not Found", "Resource not found", List.of("field1"));

        assertThat(vm.statusCode()).isEqualTo("404");
        assertThat(vm.title()).isEqualTo("Not Found");
        assertThat(vm.detail()).isEqualTo("Resource not found");
        assertThat(vm.fieldErrors()).containsExactly("field1");
    }

    @Test
    void errorVm_withoutFieldErrors_shouldUseEmptyList() {
        ErrorVm vm = new ErrorVm("400", "Bad Request", "Invalid input");

        assertThat(vm.statusCode()).isEqualTo("400");
        assertThat(vm.title()).isEqualTo("Bad Request");
        assertThat(vm.detail()).isEqualTo("Invalid input");
        assertThat(vm.fieldErrors()).isEmpty();
    }
}
