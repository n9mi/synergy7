package com.synergy.binfood.repository;

import com.synergy.binfood.entity.Product;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductRepository {
    public Product findById(int id) {
        return Repository.products.get(id);
    }
}
