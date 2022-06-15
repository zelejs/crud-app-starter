import React from 'react';
import { Radio } from 'antd';

const RadioGroup = Radio.Group;
const RadioButton = Radio.Button;

export default ({ props, onChange, ...rest }) => {

  const { steps, nextSteps } = rest.formdata;

  function handleChange(e) {
    onChange(e.target.value);
  }

  function getCurrentProcessSteps(steps) {
    return steps ? steps : [];
  }

  return <RadioGroup
    onChange={handleChange}
  >
    {((steps) => steps.map((item, index) =>
      <RadioButton key={item.id} value={item.id}>{item.name}</RadioButton>)
    )(getCurrentProcessSteps(nextSteps))
    }
  </RadioGroup>
}