package com.yas.commonlibrary.csv;

import com.yas.commonlibrary.csv.anotation.CsvColumn;
import com.yas.commonlibrary.csv.anotation.CsvName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvExporterTest {

    @SuperBuilder
    @CsvName(fileName = "TestFile")
    @Getter
    @Setter
    static class TestData extends BaseCsv {

        @CsvColumn(columnName = "Name")
        private String name;

        @CsvColumn(columnName = "Tags")
        private List<String> tags;

        // Field WITHOUT @CsvColumn — exercises the filter false branch (lines 55, 68, 79)
        private String ignoredField;
    }

    @SuperBuilder
    @CsvName(fileName = "NoGetter")
    @Getter
    @Setter
    static class NoGetterData extends BaseCsv {
        @CsvColumn(columnName = "Value")
        private String value;

        // Override getter name to cause NoSuchMethodException
        public String fetchValue() {
            return value;
        }
    }

    @Test
    void testExportToCsv_withValidData_shouldReturnCorrectCsvContent() throws IOException {
        // Given
        List<BaseCsv> dataList = Arrays.asList(
            TestData.builder()
                .id(1L)
                .name("Alice")
                .tags(Arrays.asList("tag1", "tag2"))
                .ignoredField("should-be-ignored")
                .build(),
            TestData.builder()
                .id(2L)
                .name("Bob")
                .tags(Arrays.asList("tag3", "tag4"))
                .ignoredField("also-ignored")
                .build()
        );
        // When
        byte[] csvBytes = CsvExporter.exportToCsv(dataList, TestData.class);
        String csvContent = new String(csvBytes);

        // Then
        String expectedCsv = """
            Id,Name,Tags
            1,Alice,[tag1|tag2]
            2,Bob,[tag3|tag4]
            """;

        assertEquals(expectedCsv, csvContent);
    }

    @Test
    void testExportToCsv_withNullFieldValue_shouldReturnEmptyString() throws IOException {
        // Given — name is null, tags is null → covers null branches in getFieldValueAsString
        List<BaseCsv> dataList = List.of(
            TestData.builder()
                .id(1L)
                .name(null)
                .tags(null)
                .build()
        );

        // When
        byte[] csvBytes = CsvExporter.exportToCsv(dataList, TestData.class);
        String csvContent = new String(csvBytes);

        // Then — null values become empty strings
        String expectedCsv = """
            Id,Name,Tags
            1,,
            """;

        assertEquals(expectedCsv, csvContent);
    }

    @Test
    void testExportToCsv_withEmptyDataList_shouldReturnOnlyHeader() throws IOException {
        // Given
        List<BaseCsv> dataList = new ArrayList<>();

        // When
        byte[] csvBytes = CsvExporter.exportToCsv(dataList, TestData.class);
        String csvContent = new String(csvBytes);

        // Then
        String expectedCsv = "Id,Name,Tags\n";
        assertEquals(expectedCsv, csvContent);
    }

    @Test
    void testCreateFileName_withValidClass_shouldReturnCorrectFileName() {
        // Given
        Class<TestData> clazz = TestData.class;

        // When
        String fileName = CsvExporter.createFileName(clazz);

        // Then
        String expectedPrefix = "TestFile_";
        assertTrue(fileName.startsWith(expectedPrefix));
        assertTrue(fileName.endsWith(".csv"));
    }
}
