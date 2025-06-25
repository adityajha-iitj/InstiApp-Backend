package in.ac.iitj.instiapp.services.impl;

import in.ac.iitj.instiapp.Repository.LostnFoundRepository;
import in.ac.iitj.instiapp.Repository.MediaRepository;
import in.ac.iitj.instiapp.Repository.UserRepository;
import in.ac.iitj.instiapp.Repository.impl.BusRepositoryImpl;
import in.ac.iitj.instiapp.database.entities.LostnFound.Locations;
import in.ac.iitj.instiapp.database.entities.LostnFound.LostnFound;
import in.ac.iitj.instiapp.database.entities.Media.Media;
import in.ac.iitj.instiapp.database.entities.User.User;
import in.ac.iitj.instiapp.mappers.LostnFoundDtoMapper;
import in.ac.iitj.instiapp.payload.LostnFound.LostnFoundDto;
import in.ac.iitj.instiapp.services.LostnFoundService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicMarkableReference;

@Service
public class LostnFoundServiceImpl implements LostnFoundService {

    private final LostnFoundRepository lostnFoundRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final EntityManager entityManager;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LostnFoundServiceImpl.class);

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
    public void updateLocation(String oldLocationName, Locations location) {
        lostnFoundRepository.updateLocation(oldLocationName, location);
    }

    @Override
    @Transactional
    public void saveLostAndFound(LostnFoundDto lostnFoundDto) {
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
        LostnFound lostnFound = LostnFoundDtoMapper.INSTANCE.toEntity(lostnFoundDto);
        lostnFoundRepository.updateLostnFound(lostnFound , lostnFound.getPublicId());
    }

    @Override
    @Transactional
    public void deleteLostAndFound(String publicId) {
        lostnFoundRepository.deleteLostnFound(publicId);
    }

    @Override
    public List<LostnFoundDto> getLostAndFoundByFilter(Optional<Boolean> status, Optional<String> owner, Optional<String> finder, Optional<String> landmark, Pageable pageable) {
        return lostnFoundRepository.getLostnFoundByFilter(status, owner, finder, landmark, pageable);
    }
}
