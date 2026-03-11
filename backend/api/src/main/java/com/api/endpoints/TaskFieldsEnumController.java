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

import com.api.models.TaskFieldsEnum;
import com.api.services.TaskFieldsEnumRepository;

@RestController
@RequestMapping({ "/api/TaskFieldsEnum/", "/api/TaskFieldsEnum" })
public class TaskFieldsEnumController {

    private final TaskFieldsEnumRepository repository;

    public TaskFieldsEnumController(TaskFieldsEnumRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<TaskFieldsEnum>> getAll() {
        var request = "read TaskFieldsEnum";
        try {
            var result = repository.findAll();
            return ApiResponse.success(request, result);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @GetMapping(value = "/{task_fields_enum_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<TaskFieldsEnum>> getById(@PathVariable Long task_fields_enum_id) {
        var request = "read TaskFieldsEnum";
        try {
            request = "%s%s%d".formatted(request, "/", task_fields_enum_id);
            var result = repository.findById(task_fields_enum_id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
            return ApiResponse.success(request, result.getBody());
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<TaskFieldsEnum>> create(@RequestBody TaskFieldsEnum entity) {
        var request = "create TaskFieldsEnum";
        try {
            // ensure sequence is correct (deletions create gaps)
            repository.resetIndex("task_fields_enum", "task_fields_enum_id");
            // ORM requires null ID for new object
            entity.task_fields_enum_id = null;
            var result = repository.save(entity);
            return ApiResponse.success(request, result); // TODO: check if should return object
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<TaskFieldsEnum>> update(@RequestBody TaskFieldsEnum update) {
        var request = "update TaskFieldsEnum";
        try {
            var task_fields_enum_id = update.task_fields_enum_id;
            request = "%s%s%d".formatted(request, "/", task_fields_enum_id);
            var existing = repository.findById(task_fields_enum_id);
            if (existing.isEmpty()) {
                return ApiResponse.error(request, "no entity with task_fields_enum_id=".formatted(task_fields_enum_id));
            }
            var updated = repository.save(update);
            return ApiResponse.success(request, updated);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @DeleteMapping(value = "/{task_fields_enum_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<TaskFieldsEnum>> delete(@PathVariable Long task_fields_enum_id) {
        var request = "delete TaskFieldsEnum";
        try {
            request = "%s%s%d".formatted(request, "/", task_fields_enum_id);
            var existing = repository.findById(task_fields_enum_id);
            if (existing.isEmpty()) {
                return ApiResponse.error(request, "no entity with task_fields_enum_id=".formatted(task_fields_enum_id));
            }
            repository.deleteById(task_fields_enum_id);
            return ApiResponse.success(request);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }
}
