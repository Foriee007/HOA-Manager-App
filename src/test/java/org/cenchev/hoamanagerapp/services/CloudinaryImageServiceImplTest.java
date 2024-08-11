package org.cenchev.hoamanagerapp.services;

import com.cloudinary.Cloudinary;
import org.cenchev.hoamanagerapp.services.impl.CloudinaryImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloudinaryImageServiceImplTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private CloudinaryImageServiceImpl cloudinaryImageService;

    @BeforeEach
    public void setUp() {
/*        Cloudinary uploader = mock(Cloudinary.class);
        when(cloudinary.uploader()).thenReturn(uploader.uploader());*/

    }

    @Test
    public void testUploadImage_FileIsEmpty() throws IOException {

        when(multipartFile.isEmpty()).thenReturn(true);

        // Tests the scenario where the file is empty
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cloudinaryImageService.uploadImage(multipartFile);
        });

        assertEquals("Cannot upload an empty file", exception.getMessage());
        verify(cloudinary, never()).uploader();
    }

    /*@Test
    public void testUploadImage_Success() throws IOException {
        //Tests the scenario where the file is successfully uploaded
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "http://cloudinary.com/sample.jpg");

        when(cloudinary.uploader().upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);

        String result = cloudinaryImageService.uploadImage(multipartFile);


        assertEquals("http://cloudinary.com/sample.jpg", result);
        verify(cloudinary, times(1)).uploader().upload(any(byte[].class), any(Map.class));
    }*/

    /*@Test
    public void testUploadImage_IOException() throws IOException {

        when(cloudinary.uploader()).thenThrow(new IOException("Test IOException"));

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getBytes()).thenThrow(new IOException("Test IOException"));

        // Perform the test
        IOException exception = assertThrows(IOException.class, () -> {
            cloudinaryImageService.uploadImage(multipartFile);
        });

        // Verify the exception message
        assertEquals("Test IOException", exception.getMessage());

        verify(cloudinary, never()).uploader();
    }*/
}