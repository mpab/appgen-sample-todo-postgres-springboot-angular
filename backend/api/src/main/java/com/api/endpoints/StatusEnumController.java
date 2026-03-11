package com.api.endpoints;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.models.StatusEnum;
import com.api.services.StatusEnumRepository;

@RestController
@RequestMapping({ "/api/StatusEnum/", "/api/StatusEnum" })
public class StatusEnumController {

    private final StatusEnumRepository repository;

    public StatusEnumController(StatusEnumRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<StatusEnum>> getAll() {
        var request = "read StatusEnum";
        try {
            var result = repository.findAll();
            return ApiResponse.success(request, result);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @GetMapping(value = "/{status_enum_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<StatusEnum>> getById(@PathVariable Long status_enum_id) {
        var request = "read StatusEnum";
        try {
            request = "%s%s%d".formatted(request, "/", status_enum_id);
            var result = repository.findById(status_enum_id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
            return ApiResponse.success(request, result.getBody());
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<StatusEnum>> create(@RequestBody StatusEnum entity) {
        var request = "create StatusEnum";
        try {
            // ensure sequence is correct (deletions create gaps)
            repository.resetIndex("status_enum", "status_enum_id");
            // ORM requires null ID for new object
            entity.status_enum_id = null;
            var result = repository.save(entity);
            return ApiResponse.success(request, result); // TODO: check if should return object
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<StatusEnum>> update(@RequestBody StatusEnum update) {
        var request = "update StatusEnum";
        try {
            var status_enum_id = update.status_enum_id;
            request = "%s%s%d".formatted(request, "/", status_enum_id);
            var existing = repository.findById(status_enum_id);
            if (existing.isEmpty()) {
                return ApiResponse.error(request, "no entity with status_enum_id=".formatted(status_enum_id));
            }
            var updated = repository.save(update);
            return ApiResponse.success(request, updated);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @DeleteMapping(value = "/{status_enum_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<StatusEnum>> delete(@PathVariable Long status_enum_id) {
        var request = "delete StatusEnum";
        try {
            request = "%s%s%d".formatted(request, "/", status_enum_id);
            var existing = repository.findById(status_enum_id);
            if (existing.isEmpty()) {
                return ApiResponse.error(request, "no entity with status_enum_id=".formatted(status_enum_id));
            }
            repository.deleteById(status_enum_id);
            return ApiResponse.success(request);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }
}
