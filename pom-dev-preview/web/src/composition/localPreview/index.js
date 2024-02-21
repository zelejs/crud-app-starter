import React, { useState, useEffect } from 'react';
import { AutoLayout } from 'zero-element-boot';

export default function Index(props) {

  const { configLayout, items, ...rest } = props;
  /**
   * 页面配置
   */
  const config = {
    items: items,
    layout: configLayout
  };

  console.log('config ', config);
  const itemClick = (item) =>{
    console.log('item == ', item)
  }

  return (
    <AutoLayout {...config} onItemClick={itemClick}/>
  )
}