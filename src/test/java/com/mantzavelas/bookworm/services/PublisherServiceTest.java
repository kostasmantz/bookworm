package com.mantzavelas.bookworm.services;

import com.mantzavelas.bookworm.converters.PublisherResourceToPublisher;
import com.mantzavelas.bookworm.converters.PublisherToPublisherResource;
import com.mantzavelas.bookworm.exceptions.ResourceAlreadyExistsException;
import com.mantzavelas.bookworm.models.Publisher;
import com.mantzavelas.bookworm.repositories.PublisherRepository;
import com.mantzavelas.bookworm.resources.PublisherResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    public static final String PUBLISHER_NAME = "Dummy publisher";
    public static final String PUBLISHER_ADDRESS = "Dummy address";
    public static final String PUBLISHER_TELEPHONE = "A telephone";
    public static final long PUBLISHER_ID = 1L;
    @Mock
    private PublisherRepository publisherRepository;

    private PublisherService service;

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        service = new PublisherService(publisherRepository, new PublisherResourceToPublisher(), new PublisherToPublisherResource());

        publisher = new Publisher(PUBLISHER_ID, PUBLISHER_NAME, PUBLISHER_TELEPHONE, PUBLISHER_ADDRESS, null);
    }

    @Test
    void createNewPublisherWithExistingName_ShouldThrowException() {
        PublisherResource resource = PublisherResource.builder().name(PUBLISHER_NAME).build();

        when(publisherRepository.existsByName(any())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.createNewPublisher(resource));
    }

    @Test
    void createNewPublisherWithValidResource_ShouldCreatePublisher() {
        PublisherResource resource = PublisherResource.builder()
            .name(PUBLISHER_NAME)
            .address(PUBLISHER_ADDRESS)
            .telephone(PUBLISHER_TELEPHONE)
            .build();

        when(publisherRepository.existsByName(any())).thenReturn(false);
        when(publisherRepository.save(any())).thenReturn(publisher);

        PublisherResource savedPublisher = service.createNewPublisher(resource);

        assertEquals(PUBLISHER_ID, savedPublisher.getId());
        assertEquals(PUBLISHER_NAME, savedPublisher.getName());
    }
}