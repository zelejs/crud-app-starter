package com.jfeat.dev.devops.services.gen.persistence.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author Code generator
 * @since 2022-09-16
 */
@TableName("t_dev_develop")
@ApiModel(value = "DevDevelop对象", description = "")
public class DevDevelop extends Model<DevDevelop> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "查询文件名")
    private String queryFileName;

    @ApiModelProperty(value = "执行文件名")
    private String executionFileName;


    @ApiModelProperty(value = "是否上架 默认上架 0-不上架 -1上架")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer sortNumber;

    @ApiModelProperty(value = "是否需要参数 0-不需要 1-需要 默认0")
    private Integer paramStatus;

    @ApiModelProperty(value = "数据库版本")
    private String sqlVersion;

    @ApiModelProperty(value = "备用")
    private String note;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public DevDevelop setId(Long id) {
        this.id = id;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getTitle() {
        return title;
    }

    public DevDevelop setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DevDevelop setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getQueryFileName() {
        return queryFileName;
    }

    public DevDevelop setQueryFileName(String queryFileName) {
        this.queryFileName = queryFileName;
        return this;
    }

    public String getExecutionFileName() {
        return executionFileName;
    }

    public DevDevelop setExecutionFileName(String executionFileName) {
        this.executionFileName = executionFileName;
        return this;
    }

    public Integer getParamStatus() {
        return paramStatus;
    }

    public DevDevelop setParamStatus(Integer paramStatus) {
        this.paramStatus = paramStatus;
        return this;
    }

    public String getSqlVersion() {
        return sqlVersion;
    }

    public DevDevelop setSqlVersion(String sqlVersion) {
        this.sqlVersion = sqlVersion;
        return this;
    }

    public String getNote() {
        return note;
    }

    public DevDevelop setNote(String note) {
        this.note = note;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public DevDevelop setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public DevDevelop setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public static final String ID = "id";

    public static final String TITLE = "title";

    public static final String DESCRIPTION = "description";

    public static final String QUERY_FILE_NAME = "query_file_name";

    public static final String EXECUTION_FILE_NAME = "execution_file_name";

    public static final String PARAM_STATUS = "param_status";

    public static final String SQL_VERSION = "sql_version";

    public static final String NOTE = "note";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String STATUS = "status";

    public static final String SORT_NUMBER="sort_number";

    public static final Integer STATUS_STOP = 0;

    public static final Integer STATUS_START=1;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DevDevelop{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", queryFileName=" + queryFileName +
                ", executionFileName=" + executionFileName +
                ", paramStatus=" + paramStatus +
                ", sqlVersion=" + sqlVersion +
                ", note=" + note +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
