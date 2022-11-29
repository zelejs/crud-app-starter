import React, { useState, useEffect } from 'react';
import qs from 'qs';
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

import PreviewAutoLayout from 'zero-element-boot/lib/components/PreviewAutoLayout'
import PreviewItem from './PreviewItem';
// import { useSetState } from 'ahooks';

export default function (props) {

  const params = props.location.query ||  qs.parse(props.location.search.split('?')[1])

  // 获取要显示的数据的接口
  // let api = '/api/crud/fieldModel/fieldModels'

  const [ apiPath, setApiPath ] = useState('')

  useEffect(_ => {
    setApiPath('')
    getApiUrl()
  }, [params])

  function getApiUrl() {
    if(params.api){
      setApiPath(params.api)
    }else if(params.apiName){
      //通过apiName获取API路径
      const api = `/openapi/lc/apis/${params.apiName}`;
      const queryData = {};
      promiseAjax(api, queryData).then(resp => {
          if (resp && resp.code === 200) {
            setApiPath(resp.data.api)
          } else {
              console.error("获取api path 数据失败")
          }
      }).finally(_ => {
      });
    }
    
  }
  
  // 获取layoutJson的本地接口
  // let layoutJsonApi = '/api/layoutJson'

  // let layoutJsonApi = `/openapi/lc/module/getAutoLayout/autoLayOut`

  let layoutJsonApi = params.layoutJsonApi

  // if (process.env.NODE_ENV === 'development') {
  //   layoutJsonApi = 'http://192.168.3.112:8080/openapi/lc/module/getAutoLayout/autoLayOut'
  // }

  // 获取layoutJson的api接口，如果本地接口为空，则会使用该接口请求api
  // let layoutName = 'thisAutoLayout'
  let layoutName = params.layoutName || ''
  let layoutId = params.layoutId || ''

  const allComponents = { PreviewItem }

  return (
    <>
      { apiPath ? (
        <PreviewAutoLayout api={apiPath} layoutApi={layoutJsonApi} layoutName={layoutName} layoutId={layoutId} allComponents={allComponents} />
      ):<></>}
    </>
  )
}

