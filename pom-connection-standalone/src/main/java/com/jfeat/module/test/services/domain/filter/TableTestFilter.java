package com.jfeat.module.test.services.domain.filter;

import com.jfeat.crud.plus.CRUDFilter;
import com.jfeat.module.test.services.gen.persistence.model.TableTest;


/**
 * Created by Code generator on 2021-08-19
 */
public class TableTestFilter implements CRUDFilter<TableTest> {

    private String[] ignoreFields = new String[]{};
    private String[] updateIgnoreFields = new String[]{};

    @Override
    public void filter(TableTest entity, boolean insertOrUpdate) {

        //if insertOrUpdate is true,means for insert, do this
        if (insertOrUpdate){

            //then insertOrUpdate is false,means for update,do this
        }else {

        }

    }

    @Override
    public String[] ignore(boolean retrieveOrUpdate) {
        //if retrieveOrUpdate is true,means for retrieve ,do this
        if (retrieveOrUpdate){
            return ignoreFields;
            //then retrieveOrUpdate  if false ,means for update,do this
        }else {
            return updateIgnoreFields;
        }
    }
}
