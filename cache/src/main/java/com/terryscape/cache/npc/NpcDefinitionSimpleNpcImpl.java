package com.terryscape.cache.npc;

import java.util.List;

public class NpcDefinitionSimpleNpcImpl implements NpcDefinitionSimpleNpc {

    private List<String> variants;

    @Override
    public List<String> getVariants() {
        return variants;
    }

    public NpcDefinitionSimpleNpcImpl setVariants(List<String> variants) {
        this.variants = variants;
        return this;
    }
}
