import React, { useState, useEffect } from 'react';
import { Spinner } from '@chakra-ui/react';
import AutoLayout from './index';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');


const testItems = [
    {id: 1, name: 'test1'},
    {id: 2, name: 'test2'},
    {id: 3, name: 'test3'},
    {id: 4, name: 'test4'},
    {id: 5, name: 'test5'},
    {id: 6, name: 'test6'},
    {id: 7, name: 'test7'},
    {id: 8, name: 'test8'},
]

export default function Index(props) {

    const [items, setItems] = useState('');
    const [isLoading, setLoading] = useState(false);

    useEffect(_ => {
        getData()
    },[])

    function getData() {
        
        setItems([])
        let api = '/openapi/lc/module?componentOption=layout&pageNum=1&pageSize=100'
        const queryData = {};
        promiseAjax(api, queryData).then(resp => {
            setLoading(false)
            if (resp && resp.code === 200) {
                const data = resp.data.records.map((item, index) => {
                    return {...item, items: testItems}
                })
                setItems(data)
                // setItems( resp.data.records)
            } else {
                console.error("获取layout数据失败")
            }
        });
    }
    
    return (
        isLoading
          ? <Spinner/>
          : items && items.length > 0 && <AutoLayout items={items}  />
        
    )
}