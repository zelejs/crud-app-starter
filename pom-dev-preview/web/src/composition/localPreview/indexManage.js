import React, { useState, useEffect } from 'react';
import { VStack, Box, HStack, Button, Text } from '@chakra-ui/react';
import { AutoLayout } from 'zero-element-boot';

import Menus from './Menus';
import PreviewFetch from './indeFetch.js'
import EditComponent from '../editComponent'

export default function Index(props) {

  const { previewAutoLayoutId={}, moduleName, cb, ...rest } = props;

  const [ layoutId, setLayoutId ] = useState(previewAutoLayoutId);
  const [ menuId, setMenuId ] = useState(0);
  const [ resetData, setResetData ] = useState(0);

  // useEffect(_=>{
  //   const count = resetData + 1;
  //   setResetData(count)
  // }, [previewAutoLayoutId])

  const itemClick = (id) => {
    setMenuId(id)
    if(menuId === 4){
      if(cb){
        cb(true)
      }
    }
  }

  return (
    <VStack alignItems={'flex-start'} align='stretch' spacing='4' w={'100%'}>
      <Menus itemClick={itemClick} resetData={resetData} />
      <Box w={'100%'}>
        { menuId === 1 && layoutId ? (
          <PreviewFetch previewAutoLayoutId={layoutId}/>
        ):( 
          menuId === 2 ? (
            <EditComponent componentId={previewAutoLayoutId} moduleName={moduleName} />
          ) : (
            menuId === 3 ? (
              <AutoLayout layout={menuLayoutJson} isScroll={false} onItemClick={menuItemClick} />
            ):<></>
          )
        )}
      </Box>
    </VStack>
  )
}