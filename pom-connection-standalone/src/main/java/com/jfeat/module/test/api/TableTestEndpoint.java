package com.jfeat.module.test.api;
    
                                            
                    import com.jfeat.crud.plus.META;
import com.jfeat.am.core.jwt.JWTKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.dao.DuplicateKeyException;
import com.jfeat.module.test.services.domain.dao.QueryTableTestDao;
import com.jfeat.crud.base.tips.SuccessTip;
import com.jfeat.crud.base.request.Ids;
import com.jfeat.crud.base.tips.Tip;
import com.jfeat.am.module.log.annotation.BusinessLog;
import com.jfeat.crud.base.exception.BusinessCode;
import com.jfeat.crud.base.exception.BusinessException;
import com.jfeat.crud.plus.CRUDObject;
import com.jfeat.module.test.api.permission.*;
import com.jfeat.am.common.annotation.Permission;
import java.math.BigDecimal;

import com.jfeat.module.test.services.domain.service.*;
import com.jfeat.module.test.services.domain.model.TableTestRecord;
import com.jfeat.module.test.services.gen.persistence.model.TableTest;

        import org.springframework.web.bind.annotation.RestController;
        
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import com.alibaba.fastjson.JSONArray;
/**
 * <p>
 *  api
 * </p>
 *
 * @author Code generator
 * @since 2021-08-19
 */
@RestController

@Api("TableTest")
@RequestMapping("/api/crud/tableTest/tableTests")
public class TableTestEndpoint {

@Resource
            TableTestService tableTestService;



@Resource
    QueryTableTestDao queryTableTestDao;

@BusinessLog(name = "TableTest", value = "create TableTest")
@Permission(TableTestPermission.TABLETEST_NEW)
@PostMapping
@ApiOperation(value = "新建 TableTest",response = TableTest.class)
public Tip createTableTest(@RequestBody TableTest entity){

        Integer affected=0;
        try{
                affected= tableTestService.createMaster(entity);
    
        }catch(DuplicateKeyException e){
        throw new BusinessException(BusinessCode.DuplicateKey);
        }

        return SuccessTip.create(affected);
        }

@Permission(TableTestPermission.TABLETEST_VIEW)
@GetMapping("/{id}")
@ApiOperation(value = "查看 TableTest",response = TableTest.class)
public Tip getTableTest(@PathVariable Long id){
                            return SuccessTip.create(tableTestService.queryMasterModel(queryTableTestDao, id));
            }

@BusinessLog(name = "TableTest", value = "update TableTest")
@Permission(TableTestPermission.TABLETEST_EDIT)
@PutMapping("/{id}")
@ApiOperation(value = "修改 TableTest",response = TableTest.class)
public Tip updateTableTest(@PathVariable Long id,@RequestBody TableTest entity){
        entity.setId(id);
                return SuccessTip.create(tableTestService.updateMaster(entity));
            }

@BusinessLog(name = "TableTest", value = "delete TableTest")
@Permission(TableTestPermission.TABLETEST_DELETE)
@DeleteMapping("/{id}")
@ApiOperation("删除 TableTest")
public Tip deleteTableTest(@PathVariable Long id){
                return SuccessTip.create(tableTestService.deleteMaster(id));
            }

@Permission(TableTestPermission.TABLETEST_VIEW)
@ApiOperation(value = "TableTest 列表信息",response = TableTestRecord.class)
@GetMapping
@ApiImplicitParams({
        @ApiImplicitParam(name= "pageNum", dataType = "Integer"),
        @ApiImplicitParam(name= "pageSize", dataType = "Integer"),
        @ApiImplicitParam(name= "search", dataType = "String"),
                                                                                        @ApiImplicitParam(name = "id", dataType = "Long"),
                                                                                    @ApiImplicitParam(name = "name", dataType = "String") ,
                @ApiImplicitParam(name = "orderBy", dataType = "String"),
                @ApiImplicitParam(name = "sort", dataType = "String")
            })
public Tip queryTableTests(Page<TableTestRecord> page,
@RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
@RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
  @RequestParam(name = "search", required = false) String search,
                                                                                                                            @RequestParam(name = "id", required = false) Long id,
        
                                                                                                                @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "orderBy", required = false) String orderBy,
        @RequestParam(name = "sort", required = false)  String sort) {
        
            if(orderBy!=null&&orderBy.length()>0){
        if(sort!=null&&sort.length()>0){
        String pattern = "(ASC|DESC|asc|desc)";
        if(!sort.matches(pattern)){
        throw new BusinessException(BusinessCode.BadRequest.getCode(), "sort must be ASC or DESC");//此处异常类型根据实际情况而定
        }
        }else{
        sort = "ASC";
        }
        orderBy = "`"+orderBy+"`" +" "+sort;
        }
        page.setCurrent(pageNum);
        page.setSize(pageSize);

    TableTestRecord record = new TableTestRecord();
                                                                                    record.setId(id);
                                                                                        record.setName(name);
            
    
        List<TableTestRecord> tableTestPage = queryTableTestDao.findTableTestPage(page, record, search, orderBy, null, null);

            page.setRecords(tableTestPage);

        return SuccessTip.create(page);
        }
        }
