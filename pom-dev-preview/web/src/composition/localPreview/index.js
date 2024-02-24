import React, { useState, useEffect } from 'react';
import { AutoLayout } from 'zero-element-boot';
import layout from './layout.js';
import cartLayout from './cartLlayout.js';
import presenterlayout from './presenterLayout.js'
import layoutLayout from './layoutslayout.js'

const map = {
  'cart': cartLayout,
  'indicator': layout,
  'selector': layout,
  'presenter': presenterlayout,
  'layout': layoutLayout,
}

export default function Index(props) {

  const { previewData={}, type='', ...rest } = props;

  const layoutConfig = map[type] || layout;

  /**
   * 页面配置
   */
  const config = {
    layout: layoutConfig
  };

  const itemClick = (item) =>{
    // console.log('item == ', item)
  }

  return (
    <AutoLayout {...previewData} {...config} onItemClick={itemClick}/>
  )
}