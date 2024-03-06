import React, { useState, useEffect } from 'react';
import { AutoLayout } from 'zero-element-boot';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');


export default function Index(props) {

  const { previewComponentName='', ...rest } = props;

  const [layoutConfig, setLayoutConfig] = useState({})

  useEffect(() => {
      getLayoutJson()
  },[])

  function getLayoutJson() {
    const api = `/api/${previewComponentName}`
    const queryData = {

    }
    promiseAjax(api, queryData, {method: 'POST'}).then(resp => {
        if (resp && resp.code === 200) {
            setLayoutConfig()
        } else {
            console.error("getLayoutJson = ", resp)
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

  return (
    <AutoLayout {...previewData} {...config} onItemClick={itemClick}/>
  )
}