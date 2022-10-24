package com.jfeat.am.module.navigation.services.gen.persistence.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Code generator
 * @since 2022-04-18
 */
@TableName("t_navigation")
public class Navigation extends Model<Navigation> {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      @TableField("`name`")
    private String name;

    private String url;

    private String path;

    private String title;
    @TableField("`desc`")
    private String desc;

    private Long orgId;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getId() {
        return id;
    }

      public Navigation setId(Long id) {
          this.id = id;
          return this;
      }
    
    public String getName() {
        return name;
    }

      public Navigation setName(String name) {
          this.name = name;
          return this;
      }
    
    public String getUrl() {
        return url;
    }

      public Navigation setUrl(String url) {
          this.url = url;
          return this;
      }
    
    public String getTitle() {
        return title;
    }

      public Navigation setTitle(String title) {
          this.title = title;
          return this;
      }
    
    public String getDesc() {
        return desc;
    }

      public Navigation setDesc(String desc) {
          this.desc = desc;
          return this;
      }

      public static final String ID = "id";

      public static final String NAME = "name";

      public static final String URL = "url";

      public static final String TITLE = "title";

      public static final String DESC = "desc";

      @Override
    protected Serializable pkVal() {
          return this.id;
      }

    @Override
    public String toString() {
        return "Navigation{" +
              "id=" + id +
                  ", name=" + name +
                  ", url=" + url +
                  ", title=" + title +
                  ", desc=" + desc +
              "}";
    }
}
