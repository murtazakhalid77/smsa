package com.smsa.backend.service;

import com.smsa.backend.repository.ManifestDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextService {
    @Autowired
    ManifestDataRepository manifestDataRepository;

    public ByteArrayResource getAwbs(){
        List<String> strings = manifestDataRepository.getAllAwb(); // Assuming this method fetches all AWBs as strings
        String content = String.join(System.lineSeparator(), strings);

        // Convert the content to a byte array
        byte[] contentBytes = content.getBytes();

        ByteArrayResource resource = new ByteArrayResource(contentBytes);
        return resource;
    }
}
