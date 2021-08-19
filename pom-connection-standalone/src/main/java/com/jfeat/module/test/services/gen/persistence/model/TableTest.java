package com.jfeat.module.test.services.gen.persistence.model;

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
 * @since 2021-08-19
 */
@TableName("pom_table_test")
public class TableTest extends Model<TableTest> {

    private static final long serialVersionUID=1L;

      /**
     * 主键id
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 名称
     */
      private String name;

    
    public Long getId() {
        return id;
    }

      public TableTest setId(Long id) {
          this.id = id;
          return this;
      }
    
    public String getName() {
        return name;
    }

      public TableTest setName(String name) {
          this.name = name;
          return this;
      }

      public static final String ID = "id";

      public static final String NAME = "name";

      @Override
    protected Serializable pkVal() {
          return this.id;
      }

    @Override
    public String toString() {
        return "TableTest{" +
              "id=" + id +
                  ", name=" + name +
              "}";
    }
}
