package com.synergy.binarfood.controller;

import com.synergy.binarfood.model.DataResponse;
import com.synergy.binarfood.model.user.UserCreateRequest;
import com.synergy.binarfood.model.user.UserUpdateRequest;
import com.synergy.binarfood.model.user.UserWithOrderResponse;
import com.synergy.binarfood.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(
            path = "/api/v1/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<List<UserWithOrderResponse>> getAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        Page<UserWithOrderResponse> response = this.userService.findAll(page, pageSize);

        return DataResponse.<List<UserWithOrderResponse>>builder()
                .data(response.getContent())
                .build();
    }

    @PostMapping(
            path = "/api/v1/users",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> create(@RequestBody UserCreateRequest request) {
        this.userService.create(request);

        return DataResponse.<String>builder().data("").build();
    }

    @PutMapping(
            path = "/api/v1/users/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> update(@PathVariable("id") String id,
                                       @RequestBody UserUpdateRequest request) {
        request.setId(id);
        this.userService.update(request);

        return DataResponse.<String>builder().data("").build();
    }

    @DeleteMapping(
            path = "/api/v1/users/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> delete(@PathVariable("id") String id) {
        this.userService.delete(id);

        return DataResponse.<String>builder().data("").build();
    }

    @PostMapping (
            path = "/api/v1/users/generate/dummy",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> generateDummyUsers() {
        this.userService.generateDummyUser();

        return DataResponse.<String>builder().data("").build();
    }

    @PostMapping (
            path = "/api/v1/users/{id}/authorize",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> authorizeUser(@PathVariable("id") String id) {
        this.userService.authorizeUser(id);

        return DataResponse.<String>builder().data("").build();
    }

    @PostMapping (
            path = "/api/v1/users/clean/dummy",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public DataResponse<String> cleanDummyUsers() {
        this.userService.cleanDummyUser();

        return DataResponse.<String>builder().data("").build();
    }
}
