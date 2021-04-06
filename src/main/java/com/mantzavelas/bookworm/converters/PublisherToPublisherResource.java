package com.mantzavelas.bookworm.converters;

import com.mantzavelas.bookworm.models.Publisher;
import com.mantzavelas.bookworm.resources.PublisherResource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PublisherToPublisherResource implements Converter<Publisher, PublisherResource> {

    @Override
    public PublisherResource convert(Publisher publisher) {
        return PublisherResource.builder()
            .id(publisher.getId())
            .name(publisher.getName())
            .address(publisher.getAddress())
            .telephone(publisher.getTelephone())
            .build();
    }
}
