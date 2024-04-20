package com.synergy.binfood.repository;

import com.synergy.binfood.entity.Merchant;
import com.synergy.binfood.entity.Product;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class MerchantRepository {
    public List<Merchant> findOpened() {
        return Repository.merchants.values().stream().filter(Merchant::isOpen).
                collect(Collectors.toList());
    }

    public boolean isExistsAndOpened(int id) {
        return Repository.merchants.containsKey(id) && Repository.merchants.get(id).isOpen();
    }

    public Merchant findById(int id) {
        return Repository.merchants.get(id);
    }

    public List<Product> findProducts(int merchantId) {
        return Repository.products.values().stream().filter(product ->
                product.getMerchantId() == merchantId). collect(Collectors.toList());
    }

    public boolean isProductExistsOnMerchant(int merchantId, int productId) {
        return Repository.products.values().stream().anyMatch(product ->
                product.getId() == productId && product.getMerchantId() == merchantId);
    }
}
