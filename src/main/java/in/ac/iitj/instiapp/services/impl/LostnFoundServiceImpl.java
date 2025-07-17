package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.BusRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFoundType;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.mappers.LostnFoundDtoMapper;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.services.BucketService;
import in.ac.iitj.instiapp.services.LostnFoundService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicMarkableReference;

@Service
public class LostnFoundServiceImpl implements LostnFoundService {

    private final LostnFoundRepository lostnFoundRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final EntityManager entityManager;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LostnFoundServiceImpl.class);

    @Autowired
    private BucketService bucketService;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Autowired
    public LostnFoundServiceImpl(LostnFoundRepository lostnFoundRepository, EntityManager entityManager, UserRepository userRepository, MediaRepository mediaRepository) {
        this.lostnFoundRepository = lostnFoundRepository;
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<String> getListOfLocationsName(Pageable pageable) {
        return lostnFoundRepository.getListOfLocationsName(pageable);
    }

    @Override
    @Transactional
    public void saveLocation(Locations location) {
        lostnFoundRepository.saveLocation(location);
    }

    @Override
    @Transactional
    public void deleteLocationByName(String locationName) {
        lostnFoundRepository.deleteLocationByName(locationName);
    }

    @Override
    @Transactional
    public void updateLocation(String oldLocationName, String newLocationName) {
        Locations location = new Locations();
        location.setName(newLocationName);
        lostnFoundRepository.updateLocation(oldLocationName, location);
    }

    @Override
    @Transactional
    public void saveLostAndFound(LostnFoundDto lostnFoundDto) {
        LostnFound lostnFound = new LostnFound();

        lostnFound.setPublicId(lostnFoundDto.getPublicId());
        lostnFound.setExtraInfo(lostnFoundDto.getExtraInfo());
        lostnFound.setStatus(lostnFoundDto.getStatus());
        lostnFound.setType(lostnFoundDto.getType());


        // Set managed User (finder)
        if (lostnFoundDto.getFinder() != null && lostnFoundDto.getFinder().getUserName() != null) {
            Long finderId = userRepository.getUserIdFromUsername(lostnFoundDto.getFinder().getUserName());
            User finder = entityManager.getReference(User.class, finderId);
            lostnFound.setFinder(finder);
        } else {
            lostnFound.setFinder(null);
        }

        // Set managed User (owner)
        if (lostnFoundDto.getOwner() != null && lostnFoundDto.getOwner().getUserName() != null) {
            Long ownerId = userRepository.getUserIdFromUsername(lostnFoundDto.getOwner().getUserName());
            User owner = entityManager.getReference(User.class, ownerId);
            lostnFound.setOwner(owner);
        } else {
            lostnFound.setOwner(null);
        }

        log.info("Looking for location: " + lostnFoundDto.getLandmarkName());

        // Set managed Locations (landmark)
        if (lostnFoundDto.getLandmarkName() != null) {
            Long landmarkId = lostnFoundRepository.existLocation(lostnFoundDto.getLandmarkName());
            if (landmarkId != -1) {
                Locations landmark = entityManager.getReference(Locations.class, landmarkId);
                lostnFound.setLandmark(landmark);
            } else {
                throw new DataIntegrityViolationException("Location with name '" + lostnFoundDto.getLandmarkName() + "' does not exist.");
            }
        } else {
            lostnFound.setLandmark(null);
        }

        // Set managed Media (if present)
        if (lostnFoundDto.getMedia() != null && lostnFoundDto.getMedia().getPublicId() != null) {
            Long mediaId = mediaRepository.getIdByPublicId(lostnFoundDto.getMedia().getPublicId());
            if (mediaId != null) {
                Media media = entityManager.getReference(Media.class, mediaId);
                lostnFound.setMedia(media);
            } else {
                throw new DataIntegrityViolationException("Media with publicId '" + lostnFoundDto.getMedia().getPublicId() + "' does not exist.");
            }
        } else {
            lostnFound.setMedia(null);
        }

        // Persist the fully prepared entity
        lostnFoundRepository.saveLostnFoundDetails(lostnFound);
    }

    @Override
    @Transactional
    public void updateLostAndFound(LostnFoundDto lostnFoundDto) {

        LostnFound lostnFound = new LostnFound();

        lostnFound.setPublicId(lostnFoundDto.getPublicId());
        lostnFound.setExtraInfo(lostnFoundDto.getExtraInfo());
        lostnFound.setStatus(lostnFoundDto.getStatus());


        // Set managed User (finder)
        if (lostnFoundDto.getFinder() != null && lostnFoundDto.getFinder().getUserName() != null) {
            Long finderId = userRepository.getUserIdFromUsername(lostnFoundDto.getFinder().getUserName());
            User finder = entityManager.getReference(User.class, finderId);
            lostnFound.setFinder(finder);
        } else {
            lostnFound.setFinder(null);
        }

        // Set managed User (owner)
        if (lostnFoundDto.getOwner() != null && lostnFoundDto.getOwner().getUserName() != null) {
            Long ownerId = userRepository.getUserIdFromUsername(lostnFoundDto.getOwner().getUserName());
            User owner = entityManager.getReference(User.class, ownerId);
            lostnFound.setOwner(owner);
        } else {
            lostnFound.setOwner(null);
        }

        // Set managed Locations (landmark)
        if (lostnFoundDto.getLandmarkName() != null) {
            Long landmarkId = lostnFoundRepository.existLocation(lostnFoundDto.getLandmarkName());
            if (landmarkId != -1) {
                Locations landmark = entityManager.getReference(Locations.class, landmarkId);
                lostnFound.setLandmark(landmark);
            } else {
                throw new DataIntegrityViolationException("Location with name '" + lostnFoundDto.getLandmarkName() + "' does not exist.");
            }
        } else {
            lostnFound.setLandmark(null);
        }

        // Set managed Media (if present)
        if (lostnFoundDto.getMedia() != null && lostnFoundDto.getMedia().getPublicId() != null) {
            Long mediaId = mediaRepository.getIdByPublicId(lostnFoundDto.getMedia().getPublicId());
            if (mediaId != null) {
                Media media = entityManager.getReference(Media.class, mediaId);
                lostnFound.setMedia(media);
            } else {
                throw new DataIntegrityViolationException("Media with publicId '" + lostnFoundDto.getMedia().getPublicId() + "' does not exist.");
            }
        } else {
            lostnFound.setMedia(null);
        }

        //LostnFound lostnFound = LostnFoundDtoMapper.INSTANCE.toEntity(lostnFoundDto);
        lostnFoundRepository.updateLostnFound(lostnFound , lostnFound.getPublicId());
    }

    @Override
    @Transactional
    public void deleteLostAndFound(String publicId) {
        lostnFoundRepository.deleteLostnFound(publicId);
    }

    @Override
    public List<LostnFoundDto> getLostAndFoundByFilter(LostnFoundType type, Optional<Boolean> status, Optional<String> owner, Optional<String> finder, Optional<String> landmark, Pageable pageable) {
        return lostnFoundRepository.getLostnFoundByFilter(type, status, owner, finder, landmark, pageable);
    }

    public boolean isOwner(String userName, String publicId){
        return lostnFoundRepository.isOwner(userName,publicId);
    }
    public boolean isFinder(String userName, String publicId){
        return lostnFoundRepository.isFinder(userName,publicId);
    }

    public LostnFoundType findTypeByPublicId(String publicId){
        return lostnFoundRepository.findTypeByPublicId(publicId);
    }


    @Override
    @Transactional
    public String uploadLostnFoundImage(String publicId, MultipartFile file) throws Exception {
        // 1. Find the LostnFound item
        Long lostnFoundId = lostnFoundRepository.exsitLostnFound(publicId);
        if (lostnFoundId == -1L) {
            throw new IllegalArgumentException("LostnFound item not found for publicId: " + publicId);
        }

        // 2. Upload file to S3
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String objectKey = "lostnfound/" + publicId + "/" + UUID.randomUUID() + extension;

        File temp = File.createTempFile("lostnfound-", extension);
        file.transferTo(temp);

        bucketService.uploadFile(bucketName, objectKey, temp.getAbsolutePath());
        String s3Url = bucketService.getFileUrl(bucketName, objectKey);

        temp.delete();

        // 3. Save Media entity
        Media media = new Media();
        media.setPublicUrl(s3Url);
        mediaRepository.save(media);

        // 4. Link Media to LostnFound
        LostnFound lostnFound = entityManager.find(LostnFound.class, lostnFoundId);
        lostnFound.setMedia(media);
        entityManager.merge(lostnFound);

        return s3Url;
    }

    @Override
    public String getLostnFoundImageUrl(String publicId) throws Exception {
        Long lostnFoundId = lostnFoundRepository.exsitLostnFound(publicId);
        if (lostnFoundId == -1L) {
            throw new IllegalArgumentException("LostnFound item not found for publicId: " + publicId);
        }
        LostnFound lostnFound = entityManager.find(LostnFound.class, lostnFoundId);
        if (lostnFound.getMedia() == null) {
            throw new IllegalArgumentException("No image associated with this LostnFound item.");
        }
        return lostnFound.getMedia().getPublicUrl();
    }
}
