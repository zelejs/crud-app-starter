import React, { useState, useEffect } from 'react';
import { Box } from "@chakra-ui/react";
import { AutoLayout } from 'zero-element-boot';
import layout from './layout';
import { set as NamedPresenterSet } from 'zero-element-boot/lib/components/config/NamedPresenterConfig';
import IndicatorsItem from './indicatorsItem'


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

  /**
   * set item
   */
  NamedPresenterSet({IndicatorsItem})

  const itemClick = (item) =>{
    console.log('item == ', item)
  }

  return (
    <AutoLayout {...config} onItemClick={itemClick}/>
  )
}