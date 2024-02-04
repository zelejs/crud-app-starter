import React from 'react';
import { ChakraProvider } from "@chakra-ui/react";
// import PreviewAutoLayout from '@/composition/PreviewAutoLayout/Sandbox'
import { history } from 'umi';
import AutolayoutUI from '@/composition/autolayoutUI';
import ComponentsManage from '@/composition/componentsManage'

export default function index (props) {

  return (
    <ChakraProvider>
      {/* <PreviewAutoLayout {...props} /> */}
      <AutolayoutUI {...props}/>
    </ChakraProvider>
  )

}