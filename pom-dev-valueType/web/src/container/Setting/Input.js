import React from 'react';
import { Input } from 'antd';
import { Render } from 'zero-element/lib/config/valueType';

export default function InputWarpped({
  namespace,
  value, data, index,
  editID,
  onSaveChange,
}) {
  const isThisEdit = data.id === editID;
  return isThisEdit ?
    <Input defaultValue={value} onChange={onSaveChange} />
    : <Render
      n="ellipsis"
      namespace={namespace}
      data={{
        text: value,
        record: data,
        index,
      }}
      options={{}}
      handle={{}}
    />;
}