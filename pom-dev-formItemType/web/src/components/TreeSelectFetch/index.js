import React from 'react';
import TreeSelectFetch from './TreeSelectFetch';
import _ from 'lodash';

// export default function valueTypeInputSelectFetch(props) {
//   const {
//     field,
//     handle,
//     options,
//     data: { index, text = '', record },
//   } = props;
//   const { onEdit } = handle;

//   function handleChange(e) {
//     const { target } = e;
//     const { value } = target;

//     _.set(record, field, value);
//     onEdit && onEdit(index, record);
//   }

//   return <SelectFetch
//     value={text}
//     options={options}
//     onChange={handleChange}
//   />;
// }

// 测试组件 未测试通过

export default function valueTypeInputSelectFetch(props) {
  const {
    name,
    handle,
    options,
    onChange
  } = props;

  const field = name;
  
  // console.log('props = ', props)
  // console.log('handle = ', handle)
  const { onEdit } = handle;
  const text = name;
  function handleChange(value) {
    onChange(value);
  }

  return <TreeSelectFetch 
      value={text}
      options={options}
      onChange={handleChange}/>;
}