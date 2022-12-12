import React, { useState, useEffect } from 'react';

import StandaloneContainer from './index';

const promiseAjax = require('zero-element-boot/lib/components/utils/request');
// import useTokenRequest from 'zero-element-boot/lib/components/hooks/useTokenRequest';

export default function (props) {

  const params = props.location.query ||  qs.parse(props.location.search.split('?')[1])
  
  const [ data, setData ] = useState([])

  useEffect(_ => {
    setData([])
    if(params && params.sign){
      getJarList()
    }else{
      alert('sign 无效')
    }
  }, [params])

  function getJarList() {
    
    const api = `/dev/dependency/json?sign=${params.sign}`;
      const queryData = {};
      promiseAjax(api, queryData).then(resp => {
          if (resp && resp.code === 200) {
            const newData = []
            resp.data.map((item, index) => {
                const newItem = {}
                newItem.id = index + 1;
                newItem.value = item;
                newData.push(newItem)
            })
            setData(newData)
          } else {
              console.error("获取 dependency 列表失败")
          }
        }).catch(err =>{
          alert('签名错误或已过期!')
        })
    
  }

  const dataX = []
  dataX.push({items:data})
  
  return (
        data.length > 0 ? (
          <StandaloneContainer {...props} sign={params.sign} data={dataX}/>
        ):<></>
  )
}