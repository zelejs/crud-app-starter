import React, { useState, useEffect } from 'react';
import queryMethod from '@/components/utils/promiseAjax';

/**
 * 
 * @param {boolean} extend 从 API 获取的数据, 是展开后传给子组件, 还是作为 data 传给子组件
 */
export default function APIContainer(props) {
  const [data, setData] = useState({});
  const { API, queryData={}, extend = true, token, children, ...rest } = props;

  useEffect(_ => {
    queryMethod(API, queryData, token)
      .then(responseData => {
        if (responseData && responseData.code === 200) {
          setData(responseData.data);
        }
      })
  }, []);

  return React.cloneElement(children, {
    ...(extend ? { ...data } : { data }),
    ...rest,
  })
}

function regQueryMethod(func) {
  queryMethod = func;
}

export {
  regQueryMethod,
}

