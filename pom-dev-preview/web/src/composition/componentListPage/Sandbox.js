import React, { useState, useEffect } from 'react';
import AutoLayout from './index';
import { Spinner, useToast } from '@chakra-ui/react';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

export default function Index(props) {

    const { layoutId, type, onComponentItemClick } = props;

    const toast = useToast()
    const [items, setItems] = useState('')
    const [layout, setLayout] = useState({})
    const [isLoading, setLoading] = useState(false);

    useEffect(_ => {
        setLoading(true)
        getLayoutData()
    },[])

    function getLayoutData() {
        let api =  `/openapi/crud/lc_low_auto_module/lowAutoModule/lowAutoModules/${layoutId}`
        const queryData = {};
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                toastTips('获取layout成功')
                const layoutJson = JSON.parse(resp.data.descriptor)
                setLayout(layoutJson)
                getData()
            } else {
                setLoading(false)
                toastTips("获取layout数据失败")
                console.error("获取layout数据失败")
            }
        });
    }

    function getData() {
        let api =  `/openapi/lc/module?componentOption=${type}`
        const queryData = {};
        promiseAjax(api, queryData).then(resp => {
            if (resp && resp.code === 200) {
                setItems(resp.data.records)
            } else {
                console.error("获取列表数据集失败")
            }
        }).finally(_ => {
            setLoading(false)
        });
    }

    // tips
    function toastTips(text, status = 'success') {
        toast({
            title: text,
            description: "",
            status: status,
            duration: 3000,
            isClosable: true,
            position: 'top'
        })
    }

    return (
        isLoading
        ? <Spinner/>
        : <AutoLayout items={items} layout={layout} onComponentItemClick={onComponentItemClick} />
    )
}