import React, { useState, useEffect } from 'react';
import { Box } from "@chakra-ui/react";
import { AutoLayout } from 'zero-element-boot';
import layout from './layout';


export default function Index(props) {

  const { items, ...rest } = props;

  // console.log('props =',props)
  /**
   * 页面配置
   */
  const config = {
    items: items && items.length> 0? items :[],
    layout: layout,
    ...rest
  };

  console.log('config = ', config)

  const itemClick = (item) =>{
    console.log('item == ', item)
  }

  return (
    <AutoLayout {...config} onItemClick={itemClick}/>
  )
}