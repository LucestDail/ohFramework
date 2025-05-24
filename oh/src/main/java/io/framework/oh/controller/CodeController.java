package io.framework.oh.controller;

import io.framework.oh.model.Code;
import io.framework.oh.model.CodeGroup;
import io.framework.oh.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/codes")
@CrossOrigin(origins = "*")
public class CodeController {
    private final CodeService codeService;

    // 테스트용 엔드포인트
    @GetMapping(value = "/test", produces = "application/json")
    public ResponseEntity<Map<String, Object>> testCodeManagement() {
        log.info("Testing code management endpoints...");
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. 코드 그룹 조회
            List<CodeGroup> codeGroups = codeService.getAllCodeGroups();
            result.put("codeGroups", codeGroups);
            result.put("codeGroupsCount", codeGroups.size());
            
            // 2. 특정 그룹의 코드 조회
            List<Code> codes = List.of();
            if (!codeGroups.isEmpty()) {
                Long groupId = codeGroups.get(0).getId();
                codes = codeService.getCodesByGroupId(groupId);
                result.put("selectedGroupId", groupId);
            }
            result.put("codes", codes);
            result.put("codesCount", codes.size());
            
            // 3. 하위 코드 조회 (있는 경우)
            List<Code> childCodes = List.of();
            if (!codes.isEmpty()) {
                Optional<Code> parentCode = codes.stream()
                    .filter(code -> code.getParentId() != null)
                    .findFirst();
                
                if (parentCode.isPresent()) {
                    Long parentCodeId = parentCode.get().getId();
                    childCodes = codeService.getChildCodes(parentCodeId);
                    result.put("selectedParentCodeId", parentCodeId);
                }
            }
            result.put("childCodes", childCodes);
            result.put("childCodesCount", childCodes.size());
            
            // 4. 상태 정보 추가
            result.put("status", "success");
            result.put("timestamp", LocalDateTime.now().toString());
            
            log.info("Test completed successfully");
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Error during test execution", e);
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 코드 그룹 관련 엔드포인트
    @GetMapping("/groups")
    public ResponseEntity<List<CodeGroup>> getAllCodeGroups() {
        return ResponseEntity.ok(codeService.getAllCodeGroups());
    }

    @GetMapping("/groups/{groupId}")
    public ResponseEntity<CodeGroup> getCodeGroupById(@PathVariable Long groupId) {
        return ResponseEntity.ok(codeService.getCodeGroupById(groupId));
    }

    @PostMapping("/groups")
    public ResponseEntity<CodeGroup> createCodeGroup(@RequestBody CodeGroup codeGroup) {
        return ResponseEntity.ok(codeService.createCodeGroup(codeGroup));
    }

    @PutMapping("/groups/{groupId}")
    public ResponseEntity<CodeGroup> updateCodeGroup(@PathVariable Long groupId, @RequestBody CodeGroup codeGroup) {
        codeGroup.setId(groupId);
        return ResponseEntity.ok(codeService.updateCodeGroup(codeGroup));
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<Void> deleteCodeGroup(@PathVariable Long groupId) {
        codeService.deleteCodeGroup(groupId);
        return ResponseEntity.ok().build();
    }

    // 코드 관련 엔드포인트
    @GetMapping
    public ResponseEntity<List<Code>> getAllCodes() {
        return ResponseEntity.ok(codeService.getAllCodes());
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Code>> getCodesByGroupId(@PathVariable Long groupId) {
        return ResponseEntity.ok(codeService.getCodesByGroupId(groupId));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Code>> getChildCodes(@PathVariable Long parentId) {
        return ResponseEntity.ok(codeService.getChildCodes(parentId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Code> getCodeById(@PathVariable Long id) {
        return ResponseEntity.ok(codeService.getCodeById(id));
    }

    @PostMapping
    public ResponseEntity<Code> createCode(@RequestBody Code code) {
        return ResponseEntity.ok(codeService.createCode(code));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Code> updateCode(@PathVariable Long id, @RequestBody Code code) {
        code.setId(id);
        return ResponseEntity.ok(codeService.updateCode(code));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCode(@PathVariable Long id) {
        codeService.deleteCode(id);
        return ResponseEntity.ok().build();
    }
} 