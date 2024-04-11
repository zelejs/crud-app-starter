import React, { useRef, useState, useEffect} from "react";
import { ConfirmContainer } from 'zero-element-boot/lib/components/container'
import { LowCodeDatasetManageList } from 'zero-element-boot/lib/components/list'
import { useForceUpdate } from 'zero-element-boot/lib/components/hooks/lifeCycle';

export default function DataSetManage(props) {

    const { moduleId, containerHeight, type } = props

    const forceUpdate = useForceUpdate()

    useEffect(_ => {
    }, [type])

    //原dataset配置
    const config = {
        listApi:"/openapi/crud/lc_low_auto_module_dataset/module_dataset/dataset-name-list",
        containerHeight: containerHeight,
        // saveApi: '/openapi/lc/module/add-dataset/(moduleId)',
        // saveApiBody:{
        //     datasetName: '(datasetName)'
        // }
    }

    //new dataset配置
    if(type === 'dataSet'){
        config.saveApi = '/openapi/lc/module/add-dataset/(moduleId)'
        config.saveApiBody={
            datasetName: '(datasetName)'
        }
    }else if(type === 'new dataset'){
        config.saveApi = '/openapi/lc/module/presenter/from-dataset-create'
        config.saveApiBody ={
            mainModuleId: '(moduleId)',
            datasetName: '(datasetName)'
        }
    }

    return (
        <div style={ {height: '100%'} } >
            <ConfirmContainer {...config} moduleId={moduleId}>
                <LowCodeDatasetManageList />
            </ConfirmContainer>
        </div>
        
    )
}