package com.mantzavelas.bookworm.converters;

import com.mantzavelas.bookworm.models.Publisher;
import com.mantzavelas.bookworm.resources.PublisherResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PublisherResourceToPublisher implements Converter<PublisherResource, Publisher> {

    @Override
    public Publisher convert(PublisherResource resource) {
        Publisher publisher = new Publisher();
        publisher.setName(resource.getName());
        publisher.setAddress(resource.getAddress());
        publisher.setTelephone(resource.getTelephone());

        return publisher;
    }
}
