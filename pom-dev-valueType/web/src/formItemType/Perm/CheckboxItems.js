import React, { Fragment } from 'react';
import { Checkbox } from 'antd';

export default function CheckboxGroup({ value, data, onChange }) {
  function handleChange(item, e) {
    const checked = e.target.checked;
    onChange(checked, item);
  }

  if (Array.isArray(value)) {
    return value.map(item => {
      return <Fragment key={item.id}>
        <Checkbox
          checked={data.selected[item.id]}
          onChange={handleChange.bind(null, item)}
        />
        {item.name}
      </Fragment>
    })

  }
}