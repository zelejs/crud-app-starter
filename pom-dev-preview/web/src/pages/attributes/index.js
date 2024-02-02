import React, { useEffect, useState } from 'react';
import { ChakraProvider, Spinner } from '@chakra-ui/react';
import ListPage from '@/composition/attributeListPage/Sandbox';
import { set as NamedPresenterSet } from 'zero-element-boot/lib/components/config/NamedPresenterConfig';
import SettingItem from './compx/SettingItem';

export default function Index (props) {

  NamedPresenterSet({SettingItem})
  return (
    <ChakraProvider>
      <div style={{padding: '8px'}}>
        <ListPage {...props}/>
        </div>
    </ChakraProvider>
  )

}