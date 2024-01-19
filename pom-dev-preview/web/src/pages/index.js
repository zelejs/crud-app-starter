import React from 'react';
import { ChakraProvider } from "@chakra-ui/react";
import PreviewAutoLayout from '@/composition/PreviewAutoLayout/Sandbox'
import { history } from 'umi';

export default function index (props) {

  history.push('/cart');

  return (
    // <ChakraProvider>
    //   <PreviewAutoLayout {...props} />
    // </ChakraProvider>
    <div></div>
  )

}