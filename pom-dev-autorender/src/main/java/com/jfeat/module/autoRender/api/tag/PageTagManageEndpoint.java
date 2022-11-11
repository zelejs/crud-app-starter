package com.jfeat.module.autoRender.api.tag;


import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jfeat.am.crud.tag.services.domain.service.StockTagRelationService;
import com.jfeat.am.crud.tag.services.domain.service.StockTagService;
import com.jfeat.am.crud.tag.services.persistence.dao.StockTagMapper;
import com.jfeat.am.crud.tag.services.persistence.dao.StockTagRelationMapper;
import com.jfeat.am.crud.tag.services.persistence.model.StockTag;
import com.jfeat.am.crud.tag.services.persistence.model.StockTagRelation;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.module.autoRender.service.gen.persistence.model.AutoPageTag;
import com.jfeat.module.frontPage.services.domain.service.FrontPageService;
import com.jfeat.module.frontPage.services.gen.persistence.model.FrontPage;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api("Route")
@RequestMapping("/dev/auto/tag")
public class PageTagManageEndpoint {

    @Resource
    StockTagRelationService stockTagRelationService;
    @Resource
    StockTagRelationMapper stockTagRelationMapper;
    @Resource
    StockTagService stockTagService;
    @Resource
    StockTagMapper stockTagMapper;
    @Resource
    FrontPageService frontPageService;

    @GetMapping
    public Tip getPageTagList(@RequestParam(value = "isAll", defaultValue = "false", required = false) Boolean isALl) {
        String tagType = frontPageService.getEntityName();
        QueryWrapper<StockTag> stockTagQueryWrapper = new QueryWrapper<>();
        stockTagQueryWrapper.eq(StockTag.TAG_TYPE, tagType);

        List<StockTag> stockTagList = stockTagMapper.selectList(stockTagQueryWrapper);

        if (isALl) {
            StockTag stockTag = new StockTag();
            stockTag.setTagName("全部");
            stockTag.setTagType(tagType);
            stockTag.setSortOrder(1);
            if (stockTagList != null) {
                stockTagList.add(0, stockTag);
            }
        }

        List<AutoPageTag> autoPageTagList = new ArrayList<>();

        for (StockTag stockTag:stockTagList){
            AutoPageTag autoPageTag = new AutoPageTag();
            autoPageTag.setId(stockTag.getId());
            autoPageTag.setIsPrimary(stockTag.getIsPrimary());
            autoPageTag.setSortOrder(stockTag.getSortOrder());
            autoPageTag.setTagName(stockTag.getTagName());
            if (autoPageTag.getTagName().equals("全部")){
                autoPageTag.setTagName("");
            }
            autoPageTag.setTagType(stockTag.getTagType());
            autoPageTag.setName(stockTag.getTagName());
            autoPageTagList.add(autoPageTag);
        }

        return SuccessTip.create(autoPageTagList);
    }

    @PutMapping("/{id}")
    public Tip updatePageTag(@PathVariable Long id, @RequestBody StockTag entity) {
        entity.setId(id);
        entity.setTagType(frontPageService.getEntityName());
        return SuccessTip.create(stockTagMapper.updateById(entity));
    }

    @PostMapping
    public Tip createPageTag(@RequestBody StockTag entity) {
        entity.setTagType(frontPageService.getEntityName());
        return SuccessTip.create(stockTagMapper.insert(entity));
    }

    @DeleteMapping("/{id}")
    public Tip deletePageTag(@PathVariable Long id) {
        return SuccessTip.create(stockTagMapper.deleteById(id));
    }

    @PostMapping("/bing")
    public Tip bingPageTag(@RequestBody StockTagRelation entity){
        Integer affected = 0;

        if (entity.getTagId()==null || entity.getStockId()==null){
            throw new BusinessException(BusinessCode.BadRequest,"tageId 和 stockId 为必填项");
        }
        entity.setStockType(frontPageService.getEntityName());

        QueryWrapper<StockTagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StockTagRelation.TAG_ID,entity.getTagId()).eq(StockTagRelation.STOCK_ID,entity.getStockId()).eq(StockTagRelation.STOCK_TYPE,entity.getStockType());
        StockTagRelation stockTagRelation = stockTagRelationMapper.selectOne(queryWrapper);

        if (stockTagRelation==null){
            stockTagRelation = new StockTagRelation();
            stockTagRelation.setTagId(entity.getTagId());
            stockTagRelation.setStockId(entity.getStockId());
            stockTagRelation.setStockType(frontPageService.getEntityName());
           affected+= stockTagRelationMapper.insert(stockTagRelation);
        }
        return SuccessTip.create(affected);

    }

    @PostMapping("/unbing")
    public Tip unbindPageTag(@RequestBody StockTagRelation entity){
        Integer affected = 0;

        if (entity.getTagId()==null || entity.getStockId()==null){
            throw new BusinessException(BusinessCode.BadRequest,"tageId 和 stockId 为必填项");
        }
        QueryWrapper<StockTagRelation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StockTagRelation.TAG_ID,entity.getTagId()).eq(StockTagRelation.STOCK_ID,entity.getStockId()).eq(StockTagRelation.STOCK_TYPE,frontPageService.getEntityName());
        affected+=stockTagRelationMapper.delete(queryWrapper);
        return SuccessTip.create(affected);
    }

}
