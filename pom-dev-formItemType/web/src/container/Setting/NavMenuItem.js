import React, { useState } from 'react';
import {
  Input,
  // Icon
} from 'antd';


export default function NavMenuItem({ data }) {
  const [edit,
    // setEdit
  ] = useState(false);
  const value = data.title || data.name;

  // function handleEdit() {
  //   setEdit(true);
  // }

  return <div>
    {edit ? <Input
      value={value}
    /> :
      (
        <>
          {/* <Icon type="form"
            onClick={handleEdit}
          /> */}
          {value}
        </>
      )
    }
  </div>
}