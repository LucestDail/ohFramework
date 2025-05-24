package io.framework.oh.mapper;

import io.framework.oh.model.Code;
import io.framework.oh.model.CodeGroup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeMapper {
    // 코드 그룹 관련
    List<CodeGroup> selectAllCodeGroups();
    CodeGroup selectCodeGroupById(Long id);
    void insertCodeGroup(CodeGroup codeGroup);
    void updateCodeGroup(CodeGroup codeGroup);
    void deleteCodeGroup(Long id);

    // 코드 관련
    List<Code> selectAllCodes();
    Code selectCodeById(Long id);
    List<Code> selectCodesByGroupId(Long groupId);
    List<Code> selectChildCodes(Long parentId);
    void insertCode(Code code);
    void updateCode(Code code);
    void deleteCode(Long id);
} 