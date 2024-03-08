package com.terryscape.util;

import com.google.inject.Singleton;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class EnumValueRetriever {

    public <E extends Enum<E>> Set<E> getEnumValues(Class<E> enumClass) {
        return Stream.of(enumClass.getEnumConstants()).collect(Collectors.toSet());
    }

}
