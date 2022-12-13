import React, { useState, useEffect } from 'react';

import StandaloneContainer from './index';

const promiseAjax = require('zero-element-boot/lib/components/utils/request');
// import useTokenRequest from 'zero-element-boot/lib/components/hooks/useTokenRequest';

export default function (props) {

  const params = props.location.query ||  qs.parse(props.location.search.split('?')[1])
  
  const [ data, setData ] = useState([])
  const [ sign, setSign ] = useState('')
  const [ errorMessage, setErrorMessage ] = useState('')

  useEffect(_ => {
    setSign('')
    setData([])
    if(params && params.sign){
      setSign(params.sign)
      getJarList(params.sign)
    }else{
      setErrorMessage('sign 无效')
    }
  }, [params])

  function getJarList(sign) {
    
    const api = `/dev/dependency/json?sign=${sign}`;
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
            setErrorMessage('签名错误或已过期!')
          }
        }).catch(err =>{
          setErrorMessage('签名错误或已过期!')
        })
    
  }

  return (
    <>
        { sign ? (
          <>
            { data && data.length > 0 ? (
              <StandaloneContainer {...props} sign={params.sign} data={data}/>
            ):<div style={{margin: '10px'}}>{errorMessage}</div>}
          </>
        ):<div style={{margin: '10px'}}>{errorMessage}</div>}
      </>
  )
}