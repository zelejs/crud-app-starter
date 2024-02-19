import React, { useState, useEffect } from 'react';
import { AutoLayout } from 'zero-element-boot';
import layout from './layout.json';

const testItems = [
    { id: 11, content: '文字1'},
    { id: 21, content: '文字2'},
    { id: 31, content: '文字3'},
]

export default function Index(props) {

  const { configLayout, ...rest } = props;
  /**
   * 页面配置
   */
  const config = {
    items: testItems,
    layout: {...layout, ...configLayout}
  };

  const itemClick = (item) =>{
    console.log('item == ', item)
  }

  console.log('config == ', config)

  return (
    <AutoLayout {...config} onItemClick={itemClick}/>
  )
}