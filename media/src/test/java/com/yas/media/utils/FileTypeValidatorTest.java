package com.yas.media.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class FileTypeValidatorTest {

    private FileTypeValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new FileTypeValidator();

        ValidFileType annotation = mock(ValidFileType.class);
        when(annotation.allowedTypes()).thenReturn(new String[]{"image/jpeg", "image/png", "image/gif"});
        when(annotation.message()).thenReturn("Invalid file type");
        validator.initialize(annotation);

        when(context.buildConstraintViolationWithTemplate("Invalid file type")).thenReturn(violationBuilder);
    }

    @Test
    void isValid_whenFileIsNull_shouldReturnFalse() {
        assertFalse(validator.isValid(null, context));
    }

    @Test
    void isValid_whenContentTypeIsNull_shouldReturnFalse() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", null, new byte[]{1});
        assertFalse(validator.isValid(file, context));
    }

    @Test
    void isValid_whenContentTypeNotAllowed_shouldReturnFalse() {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", new byte[]{1});
        assertFalse(validator.isValid(file, context));
    }

    @Test
    void isValid_whenValidPngImage_shouldReturnTrue() throws IOException {
        byte[] imageBytes = createValidImageBytes("png");
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", imageBytes);
        assertTrue(validator.isValid(file, context));
    }

    @Test
    void isValid_whenValidJpegImage_shouldReturnTrue() throws IOException {
        byte[] imageBytes = createValidImageBytes("jpg");
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageBytes);
        assertTrue(validator.isValid(file, context));
    }

    @Test
    void isValid_whenValidGifImage_shouldReturnTrue() throws IOException {
        byte[] imageBytes = createValidImageBytes("gif");
        MockMultipartFile file = new MockMultipartFile("file", "test.gif", "image/gif", imageBytes);
        assertTrue(validator.isValid(file, context));
    }

    @Test
    void isValid_whenAllowedTypeButNotImage_shouldReturnFalse() {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", "not-an-image".getBytes());
        assertFalse(validator.isValid(file, context));
    }

    private byte[] createValidImageBytes(String format) throws IOException {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 1, 1);
        g.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }
}
