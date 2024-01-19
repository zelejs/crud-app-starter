import React, { useEffect, useState } from 'react';
import { ChakraProvider, Spinner } from '@chakra-ui/react';
import ComponentListPage from '../../composition/componentListPage/index';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

export default function Index (props) {

  const [items, setItems] = useState('');
  const [isLoading, setLoading] = useState(false);

  useEffect(_=>{
    setContent('cart', 0)
  },[])

  function setContent(type, tabIndex){
    setLoading(true)
    getApiUrl(type)
  }

  function getApiUrl(type) {
    setItems([])
    let api =  `/openapi/crud/lc_low_auto_component/lowAutoComponent/lowAutoComponents?componentOption=${type}`
    const queryData = {};
    promiseAjax(api, queryData).then(resp => {
        setLoading(false)
        if (resp && resp.code === 200) {
            setItems(resp.data.records)
        } else {
            console.error("获取cart数据失败")
        }
    });
  }

  return (
    <ChakraProvider>
      <div style={{padding: '8px'}}>
        {
          isLoading
          ? <Spinner/>
          : items && items.length > 0 && <ComponentListPage items={items}/>
        }
        </div>
    </ChakraProvider>
  )

}