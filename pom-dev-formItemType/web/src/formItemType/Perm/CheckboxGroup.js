import React from 'react';
import { Checkbox } from 'antd';

export default function CheckboxGroup({ value, data, onChange }) {
  const { groupId, selectedClass } = data;
  const checked = selectedClass.every(item => item);
  const indeterminate = selectedClass.some(item => item);

  function handleChange(e) {
    const checked = e.target.checked;
    onChange(checked, groupId);
  }

  return <>
    <Checkbox
      indeterminate={!checked && indeterminate}
      checked={checked}
      onChange={handleChange}
    />
    {value}
  </>;
}