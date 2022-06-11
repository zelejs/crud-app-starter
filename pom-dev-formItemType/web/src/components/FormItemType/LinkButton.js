import React from 'react';
import { Button } from 'antd';

export default (props) => {
  const {
    name,
    props: propsOtp,
    defaultValue,
    value,
    options,
    ...rest
  } = props;

  const { title, linkUrl } = options;

  return <Button type="link" href={linkUrl} target="_blank" >{title}</Button>;
}