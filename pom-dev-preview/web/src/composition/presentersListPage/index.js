import React, { useState, useEffect } from 'react';
import { Box } from "@chakra-ui/react";
import { AutoLayout } from 'zero-element-boot';
import layout from './layout';
import { set as NamedPresenterSet } from 'zero-element-boot/lib/components/config/NamedPresenterConfig';


export default function Index(props) {

  const { items, onPresenterItemClick, ...rest } = props;

  // console.log('props =',props)
  /**
   * 页面配置
   */
  const config = {
    items: items && items.length> 0? items :[],
    layout: layout,
    ...rest
  };

  const itemClick = (item) =>{
    // console.log('item == ', item)
    onPresenterItemClick(item)
  }

  return (
    <AutoLayout {...config} onItemClick={itemClick}/>
  )
}