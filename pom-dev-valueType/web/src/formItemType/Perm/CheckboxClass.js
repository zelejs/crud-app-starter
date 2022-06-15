import React, { useState, useEffect } from 'react';
import { Checkbox } from 'antd';

export default function CheckboxClass({ value, data, onChange }) {
  function handleChange(e) {
    const checked = e.target.checked;
    onChange(checked, data);
  }
  const { perms, selected, onAllChecked } = data;
  const [checked, setChecked] = useState(false);
  const [indeterminate, setIndeterminate] = useState(false);

  useEffect(_ => {
    const checked = perms.every(item => selected[item.id])
    onAllChecked(checked);
    setChecked(checked);
    setIndeterminate(perms.some(item => selected[item.id]));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [perms, selected]);

  return <>
    <Checkbox
      indeterminate={!checked && indeterminate}
      checked={checked}
      onChange={handleChange}
    />
    {value}
  </>;
}