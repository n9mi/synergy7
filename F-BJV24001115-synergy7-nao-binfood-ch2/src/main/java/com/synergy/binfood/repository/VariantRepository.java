package com.synergy.binfood.repository;

import com.synergy.binfood.entity.Variant;

import java.util.UUID;

public class VariantRepository {
    public VariantRepository() {
        super();
    }

    public boolean isExistsByCode(String code) {
        return Repository.variants.containsKey(code);
    }

    public Variant findByCode(String code) {
        return Repository.variants.get(code);
    }

    public void create (Variant variant) {
        variant.setId(UUID.randomUUID().toString());
        Repository.variants.put(variant.getCode(), variant);
    }

    public void update(Variant variant) {
        Repository.variants.put(variant.getCode(), variant);
    }

    public void delete(Variant variant) {
        Repository.variants.remove(variant.getCode(), variant);
    }
}
