import React, { useState } from 'react';
import { Radio } from 'antd';

const RadioGroup = Radio.Group;
const RadioButton = Radio.Button;

export default ({ props, onChange, ...rest }) => {

  const { options, value } = rest;

  function handleChange(e) {
    onChange(e.target.value);
  }

  function getCurrentProcessSteps(steps) {
    return steps ? steps : [];
  }

  return <RadioGroup
    defaultValue={value}
    onChange={handleChange}
  >
    {((steps) => steps.map((item, index) =>
      <RadioButton key={`${index}-${item.value}`} value={item.value}>{item.label}</RadioButton>)
    )(getCurrentProcessSteps(options))
    }
  </RadioGroup>
}