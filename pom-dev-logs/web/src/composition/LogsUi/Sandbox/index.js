import React, { useState, useEffect } from 'react';
import LogsUi from '@/composition/LogsUi'
const promiseAjax = require('zero-element-boot/lib/components/utils/request');

export default function index (props) {

  const params = props.location.query ||  qs.parse(props.location.search.split('?')[1])

  const [ data, setData ] = useState([])

  const [ sign, setSign ] = useState('')

  const [ errorMessage, setErrorMessage ] = useState('')

  useEffect(_ => {
    setSign('')
    setData([])
    if(params && params.sign){
      setSign(params.sign)
      getLogList(params.sign)
    }else{
      setSign('sign 无效')
    }
  }, [params])

  function getLogList(sign) {
    
    const api = `/dev/logs/json?sign=${sign}`;
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
      });
    
  }

  return (
    <>
      { sign ? (
        <>
          { data && data.length > 0 ? (
              <LogsUi data={data} sign={sign}/>
          ):<div style={{margin: '10px'}}>{errorMessage}</div>}
        </>
      ):<div style={{margin: '10px'}}>{errorMessage}</div>}
    </>
  )

}