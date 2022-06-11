import React from 'react';
import { history } from 'umi';
import { Button } from 'antd';
import qs from 'qs';

function handleClick(path, data, blank) {
  if (blank) {
    const url = qs.stringify(data);
    window.open(`${path}?${url}`)
  } else {
    history.push({
      pathname: path,
      query: data,
    });
  }
}
export default (props) => {
  const { data: { text = '', record }, options = {} } = props;
  const { path, query = { id: 'id' }, blank = false } = options;
  const data = {};

  Object.keys(query).forEach(toKey => {
    const formKey = query[toKey];
    data[toKey] = record[formKey] || formKey;
  });

  if (text) {
    return <Button type="link" onClick={handleClick.bind(null, path, data, blank)}>
      {text}
    </Button>;
  }
  return null;
}