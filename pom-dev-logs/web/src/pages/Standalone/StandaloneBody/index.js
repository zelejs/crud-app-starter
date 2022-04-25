import React from 'react';

import { AutoLayout as AutoComponent }  from 'zero-element-boot';

// import JarItem from '../JarItem';

import _layout from './_layout';

export default function StandaloneBody(props) {

  const config = {
    layout: _layout,
    ...props,
  };

  return (
    <AutoComponent {...config} />
  )

}