package com.jiaruiblog.controller;

import com.jiaruiblog.common.MessageConstant;
import com.jiaruiblog.entity.CateDocRelationship;
import com.jiaruiblog.entity.Category;
import com.jiaruiblog.entity.dto.CategoryDTO;
import com.jiaruiblog.entity.dto.RelationDTO;
import com.jiaruiblog.entity.Tag;
import com.jiaruiblog.entity.TagDocRelationship;
import com.jiaruiblog.enums.Type;
import com.jiaruiblog.service.CategoryService;
import com.jiaruiblog.service.TagService;
import com.jiaruiblog.utils.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

/**
 * @ClassName CategoryController
 * @Description 分类的控制器
 * @Author luojiarui
 * @Date 2022/6/4 3:10 下午
 * @Version 1.0
 **/
@Api(tags = "文档分类模块")
@RestController
@Slf4j
@RequestMapping("/category")
@CrossOrigin
public class CategoryController {

    // 一个文章只能有一个分类项目
    // 一个文章下可能有多个列表

    @Autowired
    CategoryService categoryService;

    @Autowired
    TagService tagService;

    @ApiOperation(value = "3.2 新增单个分类", notes = "新增单个分类")
    @PostMapping(value = "/insert")
    public ApiResult insert(@RequestBody CategoryDTO categoryDTO){
        // 插入进来的参数必需经过清洗
        categoryDTO.setId(null);
        switch (categoryDTO.getType()) {
            case CATEGORY:
                Category category = new Category();
                category.setName(categoryDTO.getName());
                category.setCreateDate(new Date());
                category.setUpdateDate(new Date());
                return categoryService.insert(category);
            case TAG:
                Tag tag = new Tag();
                tag.setName(categoryDTO.getName());
                tag.setCreateDate(new Date());
                tag.setUpdateDate(new Date());
                return tagService.insert(tag);
            default:
                return ApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "3.3 更新分类", notes = "更新分类")
    @PutMapping(value = "/update")
    public ApiResult update(@RequestBody CategoryDTO categoryDTO){
        switch (categoryDTO.getType()) {
            case CATEGORY:
                Category category = new Category();
                category.setName(categoryDTO.getName());
                category.setId(categoryDTO.getId());
                category.setUpdateDate(new Date());
                return categoryService.update(category);
            case TAG:
                Tag tag = new Tag();
                tag.setName(categoryDTO.getName());
                tag.setId(categoryDTO.getId());
                tag.setUpdateDate(new Date());
                return tagService.update(tag);
            default:
                return ApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "3.4 根据id移除某个分类", notes = "根据id移除某个分类")
    @DeleteMapping(value = "/remove")
    public ApiResult remove(@RequestBody CategoryDTO categoryDTO){
        switch (categoryDTO.getType()) {
            case CATEGORY:
                Category category = new Category();
                category.setId(categoryDTO.getId());
                return categoryService.remove(category);
            case TAG:
                Tag tag = new Tag();
                tag.setId(categoryDTO.getId());
                return tagService.remove(tag);
            default:
                return ApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "3.7 查询所有的分类或者是标签", notes = "查询列表")
    @GetMapping(value = "/all")
    public ApiResult list(@RequestParam Type type){
        switch (type) {
            case CATEGORY:
                return categoryService.list();
            case TAG:
                return tagService.list();
            default:
                return ApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    // 同步动作，一个文档只能有一个分类关系，不能出现一对多
    @ApiOperation(value = "3.5 增加关系", notes = "检索分类")
    @PostMapping(value = "/addRelationship")
    public synchronized ApiResult addRelationship(@RequestBody RelationDTO relationDTO) {
        switch (relationDTO.getType()) {
            case CATEGORY:
                CateDocRelationship category = new CateDocRelationship();
                category.setCategoryId(relationDTO.getId());
                category.setFileId(relationDTO.getDocId());
                return categoryService.addRelationShip(category);
            case TAG:
                TagDocRelationship tag = new TagDocRelationship();
                tag.setTagId(relationDTO.getId());
                tag.setFileId(relationDTO.getDocId());
                tag.setCreateDate(new Date());
                tag.setUpdateDate(new Date());
                return tagService.addRelationShip(tag);
            default:
                return ApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }

    @ApiOperation(value = "3.6 断开连接关系", notes = "检索分类")
    @DeleteMapping(value = "/removeRelationship")
    public ApiResult removeRelationship(@RequestBody RelationDTO relationDTO) {
        switch (relationDTO.getType()) {
            case CATEGORY:
                CateDocRelationship category = new CateDocRelationship();
                category.setCategoryId(relationDTO.getId());
                category.setFileId(relationDTO.getDocId());
                return categoryService.cancleCategoryRelationship(category);
            case TAG:
                TagDocRelationship tag = new TagDocRelationship();
                tag.setTagId(relationDTO.getId());
                tag.setFileId(relationDTO.getDocId());
                return tagService.cancleTagRelationship(tag);
            default:
                return ApiResult.error(MessageConstant.PARAMS_ERROR_CODE, MessageConstant.PARAMS_FORMAT_ERROR);
        }
    }
}
