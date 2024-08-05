package org.cenchev.hoamanagerapp.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryImageService {

    String uploadImage(MultipartFile file) throws IOException;
}
