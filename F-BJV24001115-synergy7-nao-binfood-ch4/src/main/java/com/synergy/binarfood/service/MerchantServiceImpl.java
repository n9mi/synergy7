package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.Merchant;
import com.synergy.binarfood.model.merchant.MerchantCreateRequest;
import com.synergy.binarfood.model.merchant.MerchantResponse;
import com.synergy.binarfood.model.merchant.MerchantUpdateRequest;
import com.synergy.binarfood.repository.MerchantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.function.Function;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ValidationService validationService;

    public Page<MerchantResponse> findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Merchant> results = this.merchantRepository.findOpenedMerchants(pageable);
        Page<MerchantResponse> merchants = results.map(new Function<Merchant, MerchantResponse>() {
            @Override
            public MerchantResponse apply(Merchant merchant) {
                return MerchantResponse.builder()
                        .id(String.valueOf(merchant.getId()))
                        .name(merchant.getName())
                        .location(merchant.getLocation())
                        .open(merchant.isOpen())
                        .build();
            }
        });

        return merchants;
    }

    public MerchantResponse findById(String id) {
        Merchant merchant = this.merchantRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found"));

        return MerchantResponse.builder()
                .id(String.valueOf(merchant.getId()))
                .name(merchant.getName())
                .location(merchant.getLocation())
                .open(merchant.isOpen())
                .build();
    }

    @Transactional
    public MerchantResponse create(MerchantCreateRequest request) {
        this.validationService.validate(request);

        Merchant merchant = Merchant.builder()
                .name(request.getName())
                .location(request.getLocation())
                .isOpen(request.isOpen())
                .build();
        this.merchantRepository.save(merchant);

        return MerchantResponse.builder()
                .id(String.valueOf(merchant.getId()))
                .name(merchant.getName())
                .location(merchant.getLocation())
                .open(merchant.isOpen())
                .build();
    }

    @Transactional
    public MerchantResponse update(MerchantUpdateRequest request) {
        this.validationService.validate(request);

        Merchant merchant = this.merchantRepository.findById(UUID.fromString(request.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found"));
        merchant.setName(request.getName());
        merchant.setLocation(request.getLocation());
        merchant.setOpen(request.isOpen());

        this.merchantRepository.save(merchant);

        return MerchantResponse.builder()
                .id(String.valueOf(merchant.getId()))
                .name(merchant.getName())
                .location(merchant.getLocation())
                .open(merchant.isOpen())
                .build();
    }

    @Transactional
    public void delete(String id) {
        Merchant merchant = this.merchantRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant not found"));

        this.merchantRepository.delete(merchant);
    }
}
