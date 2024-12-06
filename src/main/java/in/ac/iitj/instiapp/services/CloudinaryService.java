package in.ac.iitj.instiapp.services;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public interface CloudinaryService {



    public CompletableFuture<Optional<Map<Object, Object>>> uploadFile(byte[] file, String folderName);

    public CompletableFuture<Optional<Map<Object,Object>>> deleteFileAsync(String public_id);

}