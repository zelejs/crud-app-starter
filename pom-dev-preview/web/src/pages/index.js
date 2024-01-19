import React from 'react';
import { ChakraProvider } from "@chakra-ui/react";
// import PreviewAutoLayout from '@/composition/PreviewAutoLayout/Sandbox'
import Home from '@/pages/home/page';
// import { history } from 'umi';

export default function index (props) {

  return (
    // <ChakraProvider>
    //   <PreviewAutoLayout {...props} />
    // </ChakraProvider>
    <Home/>
  )

}