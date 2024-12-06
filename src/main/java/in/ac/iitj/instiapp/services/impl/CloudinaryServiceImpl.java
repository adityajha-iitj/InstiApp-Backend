package in.ac.iitj.instiapp.services.impl;


import com.cloudinary.Cloudinary;
import in.ac.iitj.instiapp.services.CloudinaryService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {


    @Resource
    Cloudinary cloudinary;





    public CompletableFuture uploadFile(byte[] file, String folderName) {
        try {
            HashMap<Object, Object> options = new HashMap<>();
            options.put("folder", folderName);


            Map uploadedFile = cloudinary.uploader().upload(file, options);
            return CompletableFuture.completedFuture(Optional.of(Map.of("public_id", uploadedFile.get("public_id"), "url", uploadedFile.get("url"), "asset_id", uploadedFile.get("asset_id").toString())));
        } catch (IOException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }

    @Async
    @Override
    public CompletableFuture deleteFileAsync(String public_id) {

        try {
            HashMap<Object, Object> options = new HashMap<>();
            options.put("invalidate", true);

           Map result =  cloudinary.uploader().destroy(public_id, options);
           return CompletableFuture.completedFuture(Optional.of(result));

        } catch (IOException e) {
           e.printStackTrace();
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }
}