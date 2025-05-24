package io.framework.oh.service.impl;

import io.framework.oh.mapper.CodeMapper;
import io.framework.oh.model.Code;
import io.framework.oh.model.CodeGroup;
import io.framework.oh.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeServiceImpl implements CodeService {
    private final CodeMapper codeMapper;

    @Override
    public List<CodeGroup> getAllCodeGroups() {
        return codeMapper.selectAllCodeGroups();
    }

    @Override
    public CodeGroup getCodeGroupById(Long id) {
        return codeMapper.selectCodeGroupById(id);
    }

    @Override
    @Transactional
    public CodeGroup createCodeGroup(CodeGroup codeGroup) {
        codeMapper.insertCodeGroup(codeGroup);
        return codeGroup;
    }

    @Override
    @Transactional
    public CodeGroup updateCodeGroup(CodeGroup codeGroup) {
        codeMapper.updateCodeGroup(codeGroup);
        return codeGroup;
    }

    @Override
    @Transactional
    public void deleteCodeGroup(Long id) {
        codeMapper.deleteCodeGroup(id);
    }

    @Override
    public List<Code> getAllCodes() {
        return codeMapper.selectAllCodes();
    }

    @Override
    public Code getCodeById(Long id) {
        return codeMapper.selectCodeById(id);
    }

    @Override
    public List<Code> getCodesByGroupId(Long groupId) {
        return codeMapper.selectCodesByGroupId(groupId);
    }

    @Override
    public List<Code> getChildCodes(Long parentId) {
        return codeMapper.selectChildCodes(parentId);
    }

    @Override
    @Transactional
    public Code createCode(Code code) {
        codeMapper.insertCode(code);
        return code;
    }

    @Override
    @Transactional
    public Code updateCode(Code code) {
        codeMapper.updateCode(code);
        return code;
    }

    @Override
    @Transactional
    public void deleteCode(Long id) {
        codeMapper.deleteCode(id);
    }
} 