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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.models.Task;
import com.api.services.TaskRepository;

import com.api.models.AssigneeEnum;
import com.api.models.StatusEnum;
import com.api.models.TaskFieldsEnum;
import com.api.models.TaskReferences;

import com.api.services.AssigneeEnumRepository;
import com.api.services.StatusEnumRepository;
import com.api.services.TaskFieldsEnumRepository;

@RestController
@RequestMapping(value={"/api/Task/","/api/Task"})

public class TaskPagedController {

    private final TaskRepository repository;
    private final AssigneeEnumRepository assigneeEnumRepository;
    private final StatusEnumRepository statusEnumRepository;
    private final TaskFieldsEnumRepository taskFieldsEnumRepository;

    public TaskPagedController(
            TaskRepository repository,
            AssigneeEnumRepository assigneeEnumRepository,
            StatusEnumRepository statusEnumRepository,
            TaskFieldsEnumRepository taskFieldsEnumRepository) {
        this.repository = repository;
        this.assigneeEnumRepository = assigneeEnumRepository;
        this.statusEnumRepository = statusEnumRepository;
        this.taskFieldsEnumRepository = taskFieldsEnumRepository;
    }

    void setPaging(ApiResponsePaged<List<Task>> pr, Integer pageSize, boolean isLastPage) {
        var res = pr.result;
        Task firstEntity = repository.getFirstEntity(Task.class, "task", "task_id");
        res._paging.first_cursor = firstEntity.task_id;

        Task lastEntity = repository.getLastEntity(Task.class, "task", "task_id");
        res._paging.last_cursor = lastEntity.task_id;

        res._paging.entity_count = repository.getEntityCount("task");

        res._paging.page_size = pageSize;

        res._paging.prev_cursor = res.entities.getFirst().task_id;
        res._paging.next_cursor = res.entities.getLast().task_id;

        var selfNavPath = "/api/Task/";

        res._links.self = "%s?page_size=%s&prev_cursor=%d&next_cursor=%d".formatted(selfNavPath,
                res._paging.page_size,
                res._paging.prev_cursor, res._paging.next_cursor);
        res._links.next = "%s?page_size=%s&next_cursor=%d".formatted(selfNavPath, res._paging.page_size,
                res._paging.next_cursor);
        res._links.prev = "%s?page_size=%d&prev_cursor=%d".formatted(selfNavPath, res._paging.page_size,
                res._paging.prev_cursor);

        res._links.first = "%s?page_size=%d&first_cursor=%d".formatted(selfNavPath, res._paging.page_size,
                res._paging.first_cursor);

        res._links.last = "%s?page_size=%d&last_cursor=%d".formatted(selfNavPath, res._paging.page_size,
                res._paging.last_cursor);

        // remove invalid links
        if (res._paging.prev_cursor <= res._paging.first_cursor)
            res._links.prev = "";
        if (isLastPage || res._paging.next_cursor >= res._paging.last_cursor)
            res._links.next = "";
    }

    void setReferences(ApiResponsePaged<List<Task>> pr) {
        var refs = new TaskReferences();
        for (var e : assigneeEnumRepository.getAll(AssigneeEnum.class, "assignee_enum")){
            refs.assignee_enum.add((AssigneeEnum) e);
        }
        for (var e : statusEnumRepository.getAll(StatusEnum.class, "status_enum")){
            refs.status_enum.add((StatusEnum) e);
        }
        pr.result.references = refs;
    }

    void setEntityFields(ApiResponsePaged<List<Task>> pr) {
        var efs = pr.result.entity_fields;
        for (var e : taskFieldsEnumRepository.getAll(TaskFieldsEnum.class,
                "task_fields_enum")) {
            efs.add(((TaskFieldsEnum) e).task_fields_enum);
        }
    }

    boolean isNullEmpty(String s) {
        return (s == null) || s.isEmpty() || s.isBlank();
    }

    // ---------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponsePaged<List<Task>> getAll(
            @RequestParam(required = false) String page_size,
            @RequestParam(required = false) String prev_cursor,
            @RequestParam(required = false) String next_cursor,
            @RequestParam(required = false) String first_cursor,
            @RequestParam(required = false) String last_cursor) {
        var request = "be4fe: read all Task";
        try {
            List<Task> data;
            Integer pageSize = isNullEmpty(page_size) ? 5 : Integer.parseInt(page_size);
            boolean isLastPage = false;
            if (!isNullEmpty(prev_cursor) && !isNullEmpty(next_cursor)) {
                var prevCursor = Integer.parseInt(prev_cursor);
                var nextCursor = Integer.parseInt(next_cursor);
                data = repository.getPagingDataByCursor(prevCursor, nextCursor, Task.class,
                        "task",
                        "task_id");
            } else if (!isNullEmpty(next_cursor)) {
                var cursor = Integer.parseInt(next_cursor);
                data = repository.getPagingDataByPageSize(pageSize, cursor, Task.class, "task",
                        "task_id");
            } else if (!isNullEmpty(prev_cursor)) {
                var cursor = Integer.parseInt(prev_cursor);
                data = repository.getPagingDataByPageSizePrevCursor(pageSize, cursor, Task.class,
                        "task",
                        "task_id");
            } else if (!isNullEmpty(first_cursor)) {
                var cursor = Integer.parseInt(first_cursor);
                data = repository.getPagingDataByPageSizeFirstCursor(pageSize, cursor, Task.class,
                        "task",
                        "task_id");
            } else if (!isNullEmpty(last_cursor)) {
                var cursor = Integer.parseInt(last_cursor);
                data = repository.getPagingDataByPageSizeLastCursor(pageSize, cursor, Task.class,
                        "task",
                        "task_id");
                isLastPage = true;
            } else {
                data = repository.getPagingDataByPageSize(pageSize, -1, Task.class, "task",
                        "task_id"); // initial
                // data
                // set
            }

            var response = ApiResponsePaged.success(request, data);
            setPaging(response, pageSize, isLastPage);
            setReferences(response);
            setEntityFields(response);
            return response;

        } catch (Exception e) {
            return ApiResponsePaged.error(request, e);
        }
    }

    @GetMapping(value = "/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<Task>> getById(@PathVariable Long task_id) {
        var request = "read Task/%d".formatted(task_id);
        try {
            request = "%s%s%d".formatted(request, "/", task_id);
            var result = repository.findById(task_id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
            return ApiResponse.success(request, result.getBody());
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<Task>> create(@RequestBody Task entity) {
        var request = "create Task";
        try {
            // ensure sequence is correct (deletions create gaps)
            repository.resetIndex("task", "task_id");
            // ORM requires null ID for new object
            entity.task_id = null;
            var result = repository.save(entity);
            return ApiResponse.success(request, result); // TODO: check if should return object
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<Task>> update(@RequestBody Task update) {
        var request = "update Task";
        try {
            var task_id = update.task_id;
            request = "%s%s%d".formatted(request, "/", task_id);
            var existing = repository.findById(task_id);
            if (existing.isEmpty()) {
                return ApiResponse.error(request, "no entity with task_id=".formatted(task_id));
            }
            var updated = repository.save(update);
            return ApiResponse.success(request, updated);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }

    @DeleteMapping(value = "/{task_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<Task>> delete(@PathVariable Long task_id) {
        var request = "delete Task";
        try {
            request = "%s%s%d".formatted(request, "/", task_id);
            var existing = repository.findById(task_id);
            if (existing.isEmpty()) {
                return ApiResponse.error(request, "no entity with task_id=".formatted(task_id));
            }
            repository.deleteById(task_id);
            return ApiResponse.success(request);
        } catch (Exception e) {
            return ApiResponse.error(request, e);
        }
    }
}