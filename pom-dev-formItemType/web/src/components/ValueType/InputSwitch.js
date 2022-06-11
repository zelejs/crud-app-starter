import React from 'react';
import { Switch, Icon } from 'antd';
import _ from 'lodash';

export default function valueTypeInputSwitch(props) {
  const {
    field,
    handle,
    data: { index, text = '', record },
  } = props;
  const { onEdit } = handle;

  function handleChange(value) {
    _.set(record, field, value);
    onEdit && onEdit(index, record);
  }

  return <Switch
    checkedChildren={<Icon type="check" />}
    unCheckedChildren={<Icon type="close" />}
    defaultChecked={text}
    onChange={handleChange}
  />
}