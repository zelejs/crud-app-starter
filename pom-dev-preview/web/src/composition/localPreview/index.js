import React, { useState, useEffect } from 'react';
import { AutoLayout } from 'zero-element-boot';
import layout from './layout.js'

export default function Index(props) {

  const { previewData={}, ...rest } = props;

  /**
   * 页面配置
   */
  const config = {
    layout: layout
  };

  const itemClick = (item) =>{
    // console.log('item == ', item)
  }

  return (
    <AutoLayout {...previewData} {...config} onItemClick={itemClick}/>
  )
}