package com.pet.comes.service.s3;


import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.InputStream;

public interface UploadService {

    String uploadFile(InputStream inputStream,
        ObjectMetadata objectMetadata, String fileName);

    String getFileUrl(String fileName);

}
