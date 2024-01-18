import React, { useState, useEffect } from 'react';
import AutoLayout from './index';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');


export default function Index(props) {

    const { type } = props;
    let api =  `/openapi/crud/lc_low_auto_component/lowAutoComponent/lowAutoComponents?componentOption=${type}`

    const [items, setItems] = useState('')

    useEffect(_ => {
        getApiUrl()
    },[])

    function getApiUrl() {
        const queryData = {};
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                setItems(resp.data.records)
            } else {
                console.error("获取数据失败")
            }
        });
    }

    return (
        <AutoLayout items={items}  />
    )
}