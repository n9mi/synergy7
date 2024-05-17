package com.synergy.binarfood.service;

import com.synergy.binarfood.entity.Merchant;
import com.synergy.binarfood.entity.User;
import com.synergy.binarfood.model.merchant.*;
import com.synergy.binarfood.repository.MerchantRepository;
import com.synergy.binarfood.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;

    public Page<MerchantResponse> findAll(MerchantGetRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getPageSize());
        Page<Merchant> merchants;
        if (request.isOnlyOpen()) {
            merchants = this.merchantRepository.findOpenedMerchants(pageable);
        } else if (!Objects.equals(request.getUsername(), "")) {
            merchants = this.merchantRepository.findAllByUser_Username(request.getUsername(), pageable);
        } else {
            merchants = this.merchantRepository.findAll(pageable);
        }

        return merchants.map(new Function<Merchant, MerchantResponse>() {
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
    }

    public MerchantIncomeResponse getIncomeByRange(MerchantGetIncomeRequest request) {
        User user = this.userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user doesn't exists"));
        Merchant merchant = this.merchantRepository
                .findById(UUID.fromString(request.getMerchantId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists"));
        if (!merchant.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "merchant doesn't exists");
        }

        LocalDate fromDate;
        LocalDate toDate;
        if (request.getIncomeType().equals(IncomeTypeRequest.MONTHLY)) {
            fromDate = LocalDate.now().withDayOfMonth(1);
            toDate = LocalDate.now().plusDays(1);
        } else if (request.getIncomeType().equals(IncomeTypeRequest.WEEKLY)) {
            fromDate = LocalDate.now().with(DayOfWeek.MONDAY);
            toDate = LocalDate.now().plusDays(1);
        } else {
            fromDate = request.getFromDate();
            toDate = request.getToDate();
        }

        List<Object[]> results = this.merchantRepository
                .getIncomeByProductWithinDateRange(merchant.getId(), fromDate, toDate);
        List<ProductPerMerchantResponse> productPerMerchantResponses = results.stream().map(o ->
                ProductPerMerchantResponse.builder()
                        .productId(String.valueOf(o[0]))
                        .productName(String.valueOf(o[1]))
                        .productPrice((double) o[2])
                        .quantity((int) (long) o[3])
                        .totalPricePerProduct((double) o[4])
                        .build()
                ).toList();

        MerchantIncomeResponse merchantIncomeResponse = MerchantIncomeResponse.builder()
                .merchantId(merchant.getId().toString())
                .merchantName(merchant.getName())
                .fromDate(fromDate)
                .toDate(toDate)
                .totalIncomeWithinRange(productPerMerchantResponses
                        .stream()
                        .map(ProductPerMerchantResponse::getTotalPricePerProduct)
                        .reduce(0.0, Double::sum))
                .products(productPerMerchantResponses)
                .build();
        return merchantIncomeResponse;
    }
}
