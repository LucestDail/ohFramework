package io.framework.oh.service;

import io.framework.oh.model.Code;
import io.framework.oh.model.CodeGroup;

import java.util.List;

public interface CodeService {
    // 코드 그룹 관련
    List<CodeGroup> getAllCodeGroups();
    CodeGroup getCodeGroupById(Long id);
    CodeGroup createCodeGroup(CodeGroup codeGroup);
    CodeGroup updateCodeGroup(CodeGroup codeGroup);
    void deleteCodeGroup(Long id);

    // 코드 관련
    List<Code> getAllCodes();
    Code getCodeById(Long id);
    List<Code> getCodesByGroupId(Long groupId);
    List<Code> getChildCodes(Long parentId);
    Code createCode(Code code);
    Code updateCode(Code code);
    void deleteCode(Long id);
} 