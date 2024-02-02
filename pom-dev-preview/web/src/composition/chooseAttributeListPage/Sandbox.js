import React, { useState, useEffect } from 'react';
import AutoLayout from './index';
import { Box } from '@chakra-ui/layout';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

const testData = [
    {
        "id": 1,
        "propValue": "10px 30px",
        "propName": "padding",
        "dataType": "TEXT",
    },
    {
        "id": 2,
        "propValue": "10px 30px",
        "propName": "padding",
        "dataType": "TEXT",
    },
    {
        "id": 3,
        "propValue": "10px 30px",
        "propName": "padding",
        "dataType": "TEXT",
    },
]

export default function Index(props) {

    const { type } = props;
    const { attrId, moduleId } = props.location && (props.location.query ||  qs.parse(props.location.search.split('?')[1])) 
    
    
    const [items, setItems] = useState(testData)

    useEffect(_ => {
        // getData()
    },[])

    function getData() {
        let api =  `/openapi/crud/lc_low_auto_component/lowAutoComponent/lowAutoComponents?componentOption=${type}`
        const queryData = {};
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                setItems(resp.data.records)
            } else {
                console.error("获取数据失败")
            }
        });
    }

    const saveData = (data) => {
        const api = '/openapi/crud/lc_low_auto_module_prop/lowAutoModuleProp/lowAutoModuleProps/'+ attrId
        delete data.id
        const queryData = {
            moduleId,
            ...data
        };
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
            } else {
                console.error("保存数据失败")
            }
        });
    }

    const onIClick = (item) => {
        // console.log('item == ', item)
        saveData(item)
    }

    return (
        <Box width={600}>
            <AutoLayout items={items} onIClick={onIClick} />
        </Box>
    )
}