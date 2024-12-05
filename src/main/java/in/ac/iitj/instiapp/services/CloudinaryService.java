package in.ac.iitj.instiapp.services;

import org.springframework.stereotype.Service;

import java.util.Map;


public interface CloudinaryService {


    public Map<String, String> deleteFile(String public_id);

    public Map<Object, Object> uploadFile(byte[] file, String folderName);

    public void deleteFileAsync(String public_id);

}