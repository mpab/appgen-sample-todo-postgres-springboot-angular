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

import com.api.models.AssigneeEnum;
import com.api.services.AssigneeEnumRepository;

@RestController
@RequestMapping({ "/api/AssigneeEnum/", "/api/AssigneeEnum" })
public class AssigneeEnumController {

    private final AssigneeEnumRepository repository;

    public AssigneeEnumController(AssigneeEnumRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<AssigneeEnum>> getAll() {
        var request = "read AssigneeEnum";
        try {
            var result = repository.findAll();
            return ApiResponse.success(request, result);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @GetMapping(value = "/{assignee_enum_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<AssigneeEnum>> getById(@PathVariable Long assignee_enum_id) {
        var request = "read AssigneeEnum";
        try {
            request = "%s%s%d".formatted(request, "/", assignee_enum_id);
            var result = repository.findById(assignee_enum_id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
            return ApiResponse.success(request, result.getBody());
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<AssigneeEnum>> create(@RequestBody AssigneeEnum entity) {
        var request = "create AssigneeEnum";
        try {
            // ensure sequence is correct (deletions create gaps)
            repository.resetIndex("assignee_enum", "assignee_enum_id");
            // ORM requires null ID for new object
            entity.assignee_enum_id = null;
            var result = repository.save(entity);
            return ApiResponse.success(request, result); // TODO: check if should return object
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<AssigneeEnum>> update(@RequestBody AssigneeEnum update) {
        var request = "update AssigneeEnum";
        try {
            var assignee_enum_id = update.assignee_enum_id;
            request = "%s%s%d".formatted(request, "/", assignee_enum_id);
            var existing = repository.findById(assignee_enum_id);
            if (existing.isEmpty()) {
                return ApiResponse.error(request, "no entity with assignee_enum_id=".formatted(assignee_enum_id));
            }
            var updated = repository.save(update);
            return ApiResponse.success(request, updated);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @DeleteMapping(value = "/{assignee_enum_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<AssigneeEnum>> delete(@PathVariable Long assignee_enum_id) {
        var request = "delete AssigneeEnum";
        try {
            request = "%s%s%d".formatted(request, "/", assignee_enum_id);
            var existing = repository.findById(assignee_enum_id);
            if (existing.isEmpty()) {
                return ApiResponse.error(request, "no entity with assignee_enum_id=".formatted(assignee_enum_id));
            }
            repository.deleteById(assignee_enum_id);
            return ApiResponse.success(request);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }
}
