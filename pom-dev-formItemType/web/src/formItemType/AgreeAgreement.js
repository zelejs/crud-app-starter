import React from 'react';
import { Checkbox, Button } from 'antd';

export default function (props) {
  const { onChange } = props;

  function handleChange(e) {
    if (e.target.checked) {
      onChange(true);
    } else {
      onChange(undefined);
    }
  }
  return <>
    <Checkbox onChange={handleChange}>已阅读并同意</Checkbox>
    <Button type="link" size="small">《服务协议》</Button>
    <Button type="link" size="small">《隐私政策》</Button>
  </>
}