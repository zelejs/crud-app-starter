import React, { useState, useEffect } from 'react';
import { AutoLayout } from 'zero-element-boot';
import { useToast } from '@chakra-ui/react'
const promiseAjax = require('zero-element-boot/lib/components/utils/request');


export default function Index(props) {

  const { previewAutoLayoutId='', ...rest } = props;

  const [layoutConfig, setLayoutConfig] = useState({})
  const toast = useToast()

  useEffect(() => {
    if(previewAutoLayoutId){
        getLayoutJson()
    }else{
        setLayoutConfig()
    }
  },[previewAutoLayoutId])

  function getLayoutJson() {
    const api = `/openapi/lc/module/preview/${previewAutoLayoutId}`
    const queryData = {

    }
    promiseAjax(api, queryData, {method: 'GET'}).then(resp => {
        if (resp && resp.code === 200) {
            setLayoutConfig(resp.data)
        } else {
            console.error("getLayoutJson = ", resp)
            setLayoutConfig({})
            toastTips(resp.message)
        }
    }).finally(_ => {
    });
  }

  /**
   * 页面配置
   */
  const config = {
    layout: layoutConfig
  };

  const itemClick = (item) =>{
    // console.log('item == ', item)
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
    layoutConfig && JSON.stringify(layoutConfig) != '{}' && (
        <AutoLayout {...config} onItemClick={itemClick}/>
    )
  )
}