import React, { useRef, useState, useEffect} from "react";
import { useToast } from '@chakra-ui/react';
import { ConfirmContainer } from 'zero-element-boot/lib/components/container'
import { LowCodeDatasetManageList } from 'zero-element-boot/lib/components/list'
import promiseAjax from 'zero-element-boot/lib/components/utils/request';

export default function DataSetManage(props) {

    const { moduleId, containerHeight } = props

    const toast = useToast()


    const config = {
        listApi:"/openapi/crud/lc_low_auto_module_dataset/module_dataset/dataset-name-list",
        containerHeight: containerHeight,
        saveApi: '/openapi/lc/module/add-dataset/(moduleId)',
        saveApiBody:{
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