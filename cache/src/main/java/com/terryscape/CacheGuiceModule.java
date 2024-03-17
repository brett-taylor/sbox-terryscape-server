package com.terryscape;

import com.google.inject.AbstractModule;
import com.terryscape.cache.CacheLoader;
import com.terryscape.cache.CacheLoaderImpl;

public class CacheGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();

        binder().bind(CacheLoader.class).to(CacheLoaderImpl.class);
    }
}
