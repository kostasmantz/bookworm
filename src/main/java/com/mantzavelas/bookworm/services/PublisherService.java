package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.PublisherResourceToPublisher;
import com.mantzavelas.bookworm.converters.PublisherToPublisherResource;
import com.mantzavelas.bookworm.exceptions.ResourceAlreadyExistsException;
import com.mantzavelas.bookworm.models.Publisher;
import com.mantzavelas.bookworm.repositories.PublisherRepository;
import com.mantzavelas.bookworm.resources.PublisherResource;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final PublisherResourceToPublisher publisherResourceConverter;
    private final PublisherToPublisherResource publisherConverter;

    public PublisherService(PublisherRepository publisherRepository, PublisherResourceToPublisher publisherResourceConverter, PublisherToPublisherResource publisherConverter) {
        this.publisherRepository = publisherRepository;
        this.publisherResourceConverter = publisherResourceConverter;
        this.publisherConverter = publisherConverter;
    }

    public PublisherResource createNewPublisher(PublisherResource resource) {
        validateResource(resource);
        Publisher publisher = publisherResourceConverter.convert(resource);

        Publisher savedPublisher = publisherRepository.save(publisher);

        return publisherConverter.convert(savedPublisher);
    }

    private void validateResource(PublisherResource resource) {
        if (publisherRepository.existsByName(resource.getName())) {
            throw new ResourceAlreadyExistsException("A publisher already exists with name " + resource.getName());
        }
    }
}
